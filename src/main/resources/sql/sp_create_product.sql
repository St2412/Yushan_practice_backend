DELIMITER $$

DROP PROCEDURE IF EXISTS sp_create_product $$

CREATE PROCEDURE sp_create_product(
    IN p_product_id VARCHAR(20),
    IN p_product_name VARCHAR(100),
    IN p_price INT,
    IN p_quantity INT
)
BEGIN
    IF p_product_id IS NULL OR TRIM(p_product_id) = '' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'INVALID_PRODUCT_ID';
    END IF;

    IF p_product_name IS NULL OR TRIM(p_product_name) = '' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'INVALID_PRODUCT_NAME';
    END IF;

    IF p_price <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'INVALID_PRICE';
    END IF;

    IF p_quantity < 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'INVALID_QUANTITY';
    END IF;

    INSERT INTO product (
        product_id,
        product_name,
        price,
        quantity
    )
    VALUES (
        p_product_id,
        p_product_name,
        p_price,
        p_quantity
    );
END $$

DELIMITER ;
