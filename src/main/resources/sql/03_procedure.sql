DROP PROCEDURE IF EXISTS sp_create_product $$
DROP PROCEDURE IF EXISTS sp_get_available_products $$
DROP PROCEDURE IF EXISTS sp_preview_order $$
DROP PROCEDURE IF EXISTS sp_create_order $$

CREATE PROCEDURE sp_create_product(
    IN p_product_id VARCHAR(20),
    IN p_product_name VARCHAR(100),
    IN p_price INT,
    IN p_quantity INT
)
BEGIN
    IF p_product_id IS NULL OR TRIM(p_product_id) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'INVALID_PRODUCT_ID';
    END IF;
    IF p_product_name IS NULL OR TRIM(p_product_name) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'INVALID_PRODUCT_NAME';
    END IF;
    IF p_price IS NULL OR p_price <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'INVALID_PRICE';
    END IF;
    IF p_quantity IS NULL OR p_quantity < 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'INVALID_QUANTITY';
    END IF;

    INSERT INTO product (product_id, product_name, price, quantity)
    VALUES (p_product_id, p_product_name, p_price, p_quantity);
END $$

CREATE PROCEDURE sp_get_available_products()
BEGIN
    SELECT product_id, product_name, price, quantity
    FROM product
    WHERE quantity > 0
    ORDER BY product_id;
END $$

CREATE PROCEDURE sp_preview_order(IN p_items_json JSON)
BEGIN
    IF p_items_json IS NULL OR JSON_LENGTH(p_items_json) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'EMPTY_ORDER_ITEMS';
    END IF;

    SELECT p.product_id,
           p.product_name,
           j.quantity,
           p.price AS stand_price,
           (p.price * j.quantity) AS item_price
    FROM JSON_TABLE(p_items_json, '$[*]'
        COLUMNS (
            product_id VARCHAR(20) PATH '$.productId',
            quantity INT PATH '$.quantity'
        )) j
    JOIN product p ON p.product_id = j.product_id;
END $$

CREATE PROCEDURE sp_create_order(
    IN p_order_id VARCHAR(30),
    IN p_member_id VARCHAR(20),
    IN p_items_json JSON
)
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE v_product_id VARCHAR(20);
    DECLARE v_quantity INT;
    DECLARE v_stock INT;
    DECLARE v_price INT;
    DECLARE v_total INT DEFAULT 0;

    DECLARE item_cursor CURSOR FOR
        SELECT product_id, quantity
        FROM JSON_TABLE(p_items_json, '$[*]'
            COLUMNS (
                product_id VARCHAR(20) PATH '$.productId',
                quantity INT PATH '$.quantity'
            )) jt;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    IF p_order_id IS NULL OR TRIM(p_order_id) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'INVALID_ORDER_ID';
    END IF;
    IF p_member_id IS NULL OR TRIM(p_member_id) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'INVALID_MEMBER_ID';
    END IF;
    IF p_items_json IS NULL OR JSON_LENGTH(p_items_json) = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'EMPTY_ORDER_ITEMS';
    END IF;

    START TRANSACTION;

    OPEN item_cursor;

    read_loop: LOOP
        FETCH item_cursor INTO v_product_id, v_quantity;
        IF done = 1 THEN
            LEAVE read_loop;
        END IF;

        IF v_product_id IS NULL OR TRIM(v_product_id) = '' THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'PRODUCT_NOT_FOUND';
        END IF;

        IF v_quantity IS NULL OR v_quantity <= 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'INVALID_ORDER_QUANTITY';
        END IF;

        SELECT quantity, price INTO v_stock, v_price
        FROM product
        WHERE product_id = v_product_id
        FOR UPDATE;

        IF v_stock IS NULL THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'PRODUCT_NOT_FOUND';
        END IF;

        IF v_quantity > v_stock THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'OUT_OF_STOCK';
        END IF;

        SET v_total = v_total + (v_price * v_quantity);
    END LOOP;

    CLOSE item_cursor;

    INSERT INTO orders (order_id, member_id, price, pay_status)
    VALUES (p_order_id, p_member_id, v_total, 0);

    INSERT INTO order_detail (order_id, product_id, quantity, stand_price, item_price)
    SELECT p_order_id,
           p.product_id,
           j.quantity,
           p.price,
           p.price * j.quantity
    FROM JSON_TABLE(p_items_json, '$[*]'
        COLUMNS (
            product_id VARCHAR(20) PATH '$.productId',
            quantity INT PATH '$.quantity'
        )) j
    JOIN product p ON p.product_id = j.product_id;

    UPDATE product p
    JOIN JSON_TABLE(p_items_json, '$[*]'
        COLUMNS (
            product_id VARCHAR(20) PATH '$.productId',
            quantity INT PATH '$.quantity'
        )) j ON p.product_id = j.product_id
    SET p.quantity = p.quantity - j.quantity;

    COMMIT;
END $$
