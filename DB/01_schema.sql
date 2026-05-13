DROP TABLE IF EXISTS order_detail;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS product;

CREATE TABLE product (
    product_id VARCHAR(20) PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    quantity INT NOT NULL
);

CREATE TABLE orders (
    order_id VARCHAR(30) PRIMARY KEY,
    member_id VARCHAR(20) NOT NULL,
    price INT NOT NULL,
    pay_status TINYINT NOT NULL DEFAULT 0
);

CREATE TABLE order_detail (
    order_item_sn BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(30) NOT NULL,
    product_id VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    stand_price INT NOT NULL,
    item_price INT NOT NULL,
    CONSTRAINT fk_order_detail_order FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT fk_order_detail_product FOREIGN KEY (product_id) REFERENCES product(product_id)
);
