DROP DATABASE IF EXISTS webshop_be_better;
CREATE DATABASE webshop_be_better;
USE webshop_be_better;

-- >>>>>>>>>> REFERENCES <<<<<<<<<
CREATE TABLE category (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE brand (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE color (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE size (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(10) NOT NULL UNIQUE
);

CREATE TABLE country (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- >>>>>>>>>> ENTITIES <<<<<<<<<
CREATE TABLE product (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    retail_purchase_price DECIMAL(10,2) NOT NULL,
    retail_sale_price DECIMAL(10,2) NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    brand_id INT NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brand(id)
);

CREATE TABLE stock_unit (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	quantity INT NOT NULL,
    product_id INT NOT NULL,
    color_id INT NOT NULL,
    size_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (color_id) REFERENCES color(id),
    FOREIGN KEY (size_id) REFERENCES size(id),
    UNIQUE(product_id, color_id, size_id)
);

CREATE TABLE customer (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL
);

CREATE TABLE address (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    type ENUM('shipping', 'billing') NOT NULL,
    street_address VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country_id INT NOT NULL,
    FOREIGN KEY (country_id) REFERENCES country(id),
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE -- If the customer is deleted, all of their personal information should be too.
);

CREATE TABLE order_information (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    status ENUM('unpaid', 'paid', 'shipped', 'received') NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    customer_id INT,
    shipping_address_id INT NULL,
    billing_address_id INT NULL,
    currency CHAR(3) NOT NULL DEFAULT 'SEK',
    total_gross DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE SET NULL,
    FOREIGN KEY (billing_address_id) REFERENCES address(id) ON DELETE SET NULL,
    FOREIGN KEY (shipping_address_id) REFERENCES address(id) ON DELETE SET NULL
);

CREATE TABLE order_product_map (
	order_id INT,
    stock_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price_snapshot DECIMAL(10,2) NOT NULL,
    tax_rate DECIMAL(5,2) NOT NULL DEFAULT 0.25,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES order_information(id) ON DELETE SET NULL,
    FOREIGN KEY (stock_id) REFERENCES stock_unit(id) 
);

-- >>>>>>>>>> N:M MAPS <<<<<<<<<
CREATE TABLE product_category_map (
	category_id INT NOT NULL,
    product_id INT NOT NULL,
    PRIMARY KEY(category_id, product_id),
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- >>>>>>>>>> INDEXES <<<<<<<<<
-- In a real world application for a webb database, a customer is more likely to search on product names.
CREATE INDEX ix_product_name ON product(name);

-- Since we use the stock_unit to uniquely identify products, there will be a lot of instances
-- where we need to look up the product based on the stock_unit.product_id. This both speeds up
-- the lookup and joins.
CREATE INDEX ix_stock_unit_product ON stock_unit(product_id);

-- It is likely a customer registered on our website will use their mail
-- for identifying. This column demands unique email adresses and
-- speeds up the process.
CREATE INDEX ix_customer_mail ON customer(mail);

-- To find the specific order lines of the products we use order_product_map.order_id,
-- therefore we create an index to speed up the process of related rows.
CREATE INDEX ix_opm_order ON order_product_map(order_id); -- faster look up on orders

-- ====== REFERENCE DATA ======
INSERT INTO brand (name) VALUES
  ('NordicGear'),
  ('Arctic Trail'),
  ('Svea Home'),
  ('Skog & Hav'),
  ('ByteCraft');

INSERT INTO category (name) VALUES
  ('Outdoor'),
  ('Clothing'),
  ('Footwear'),
  ('Accessories'),
  ('Home & Kitchen'),
  ('Electronics'),
  ('Fitness');

INSERT INTO color (name) VALUES
  ('Black'),
  ('White'),
  ('Navy'),
  ('Forest Green'),
  ('Red');

-- Storlekar inkl. OneSize + några siffror för skor
INSERT INTO size (name) VALUES
  ('OneSize'),
  ('S'),
  ('M'),
  ('L'),
  ('XL'),
  ('42'),
  ('43');

INSERT INTO country (name) VALUES
  ('Sweden'),
  ('Norway'),
  ('Denmark'),
  ('Finland');

-- ====== PRODUCTS ======
INSERT INTO product (name, description, retail_purchase_price, retail_sale_price, brand_id) VALUES
  ('All-Weather Jacket', 'Vatten- och vindtät jacka för alla årstider.', 800.00, 1299.00, 1),  -- id 1
  ('Trail Running Shoes', 'Lätta och greppvänliga löparskor för terräng.', 700.00, 1099.00, 2), -- id 2
  ('Thermal Water Bottle 1L', 'Isolerad flaska som håller drycken kall/varm länge.', 120.00, 249.00, 4), -- id 3
  ('Smart Fitness Band', 'Aktivitetsarmband med puls, sömn och notiser.', 300.00, 599.00, 5), -- id 4
  ('Merino Wool T-Shirt', 'Mjukt merinoullstyg som andas och torkar snabbt.', 180.00, 349.00, 3), -- id 5
  ('Camping Backpack 45L', 'Ryggsäck med 45L volym och bra bärsystem.', 500.00, 899.00, 1), -- id 6
  ('Non-stick Frying Pan 28cm', 'Stekpanna med non-stick och jämn värmespridning.', 220.00, 399.00, 3), -- id 7
  ('Bluetooth Earbuds', 'Trådlösa hörlurar med brusreducering.', 350.00, 799.00, 5), -- id 8
  ('Yoga Mat Pro', 'Greppvänlig yogamatta med hög densitet.', 150.00, 299.00, 4), -- id 9
  ('Hiking Socks (2-pack)', 'Vandringsstrumpor med zonstöd och ventilation.', 60.00, 119.00, 2); -- id 10

-- Produkt ↔ kategori (category_id, product_id)
INSERT INTO product_category_map (category_id, product_id) VALUES
  (2,1),(1,1),          -- Jacket: Clothing, Outdoor
  (3,2),(7,2),(1,2),    -- Trail Shoes: Footwear, Fitness, Outdoor
  (4,3),(1,3),(7,3),    -- Bottle: Accessories, Outdoor, Fitness
  (6,4),(7,4),          -- Fitness Band: Electronics, Fitness
  (2,5),(7,5),          -- Merino T-shirt: Clothing, Fitness
  (1,6),(4,6),          -- Backpack: Outdoor, Accessories
  (5,7),                -- Frying Pan: Home & Kitchen
  (6,8),                -- Earbuds: Electronics
  (7,9),(4,9),          -- Yoga Mat: Fitness, Accessories
  (2,10),(1,10);        -- Socks: Clothing, Outdoor

-- ====== STOCK UNITS (product_id, color_id, size_id, quantity) ======
INSERT INTO stock_unit (product_id, color_id, size_id, quantity) VALUES
  (1, 1, 3, 20),  -- id 1: Jacket Black M
  (1, 3, 4, 15),  -- id 2: Jacket Navy L
  (1, 4, 3, 10),  -- id 3: Jacket Forest Green M
  (2, 1, 6, 12),  -- id 4: Trail Shoes Black 42
  (2, 5, 7,  8),  -- id 5: Trail Shoes Red 43
  (3, 4, 1, 50),  -- id 6: Bottle Forest Green OneSize
  (3, 5, 1, 40),  -- id 7: Bottle Red OneSize
  (4, 1, 1, 30),  -- id 8: Fitness Band Black OneSize
  (4, 2, 1, 20),  -- id 9: Fitness Band White OneSize
  (5, 2, 3, 25),  -- id10: Merino Tee White M
  (5, 3, 4, 20),  -- id11: Merino Tee Navy L
  (5, 1, 2, 10),  -- id12: Merino Tee Black S
  (6, 4, 1, 18),  -- id13: Backpack Forest Green OneSize
  (6, 1, 1, 12),  -- id14: Backpack Black OneSize
  (7, 1, 1, 35),  -- id15: Frying Pan Black OneSize
  (8, 2, 1, 40),  -- id16: Earbuds White OneSize
  (8, 1, 1, 30),  -- id17: Earbuds Black OneSize
  (9, 3, 1, 22),  -- id18: Yoga Mat Navy OneSize
  (9, 5, 1, 18),  -- id19: Yoga Mat Red OneSize
  (10,1, 3, 40),  -- id20: Socks Black M
  (10,1, 4, 35),  -- id21: Socks Black L
  (10,2, 3, 20);  -- id22: Socks White M

-- ====== CUSTOMERS ======
INSERT INTO customer (firstName, lastName, email, phone) VALUES
  ('Anna',  'Svensson',  'anna.svensson@example.com',  '+46701234567'), -- id 1
  ('Erik',  'Johansson', 'erik.johansson@example.com', '+46702345678'), -- id 2
  ('Maria', 'Lindberg',  'maria.lindberg@example.com', '+46703456789'), -- id 3
  ('Johan', 'Karlsson',  'johan.karlsson@example.com', '+46704567890'), -- id 4
  ('Elin',  'Andersson', 'elin.andersson@example.com', '+46705678901'), -- id 5
  ('Peter', 'Nilsson',   'peter.nilsson@example.com',  '+46706789012'), -- id 6
  ('Sara',  'Berg',      'sara.berg@example.com',      '+46707890123'); -- id 7

-- ====== ADDRESSES (2 per kund: billing, shipping) ======
INSERT INTO address (customer_id, type, street_address, postal_code, country_id, city) VALUES
  (1,'billing',  'Sveavägen 10',       '11122', 1, 'Stockholm'), -- id 1
  (1,'shipping', 'Sveavägen 10',       '11122', 1, 'Stockholm'), -- id 2
  (2,'billing',  'Storgatan 5',        '41103', 1, 'Vänersborg'), -- id 3
  (2,'shipping', 'Storgatan 5',        '41103', 1, 'Vänersborg'), -- id 4
  (3,'billing',  'Mannerheimintie 12', '00100', 4, 'Helsingfors'), -- id 5
  (3,'shipping', 'Mannerheimintie 12', '00100', 4, 'Helsingfors'), -- id 6
  (4,'billing',  'Västra Hamngatan 3', '41117', 1, 'Göteborg'), -- id 7
  (4,'shipping', 'Västra Hamngatan 3', '41117', 1, 'Göteborg'), -- id 8
  (5,'billing',  'Karl Johans gate 20','0159',  2, 'Oslo'), -- id 9
  (5,'shipping', 'Karl Johans gate 20','0159',  2, 'Oslo'), -- id10
  (6,'billing',  'Nørrebrogade 15',    '2200',  3, 'Köpenhamn'), -- id11
  (6,'shipping', 'Nørrebrogade 15',    '2200',  3, 'Köpenhamn'), -- id12
  (7,'billing',  'Lilla Nygatan 7',    '21138', 1, 'Stockholm'), -- id13
  (7,'shipping', 'Lilla Nygatan 7',    '21138', 1, 'Stockholm'); -- id14

-- ====== ORDERS ======
-- Status: 'unpaid','paid','shipped','received'
INSERT INTO order_information (status, customer_id, shipping_address_id, billing_address_id, currency, total_gross) VALUES
  ('paid',     1,  2,  1, 'SEK', 1921.25), -- id 1
  ('shipped',  2,  4,  3, 'SEK', 1810.00), -- id 2
  ('unpaid',   3,  6,  5, 'SEK', 1122.50), -- id 3
  ('received', 4,  8,  7, 'SEK',  998.75), -- id 4
  ('paid',     5, 10,  9, 'SEK', 1746.25), -- id 5
  ('shipped',  6, 12, 11, 'SEK', 1083.75), -- id 6
  ('paid',     7, 14, 13, 'SEK', 1935.00), -- id 7
  ('unpaid',   1,  2,  1, 'SEK', 1747.50), -- id 8
  ('received', 2,  4,  3, 'SEK', 1671.25), -- id 9
  ('paid',     3,  6,  5, 'SEK', 1933.75); -- id10

-- ====== ORDER LINES (order_product_map) ======
-- total = unit_price_snapshot * quantity * (1 + tax_rate)  (25% moms)
INSERT INTO order_product_map (order_id, stock_id, quantity, unit_price_snapshot, tax_rate, total) VALUES
  -- Order 1
  (1,  1, 1, 1299.00, 0.25, 1623.75),
  (1, 20, 2,  119.00, 0.25,  297.50),

  -- Order 2
  (2,  4, 1, 1099.00, 0.25, 1373.75),
  (2, 10, 1,  349.00, 0.25,  436.25),

  -- Order 3
  (3,  8, 1,  599.00, 0.25,  748.75),
  (3, 18, 1,  299.00, 0.25,  373.75),

  -- Order 4
  (4, 16, 1,  799.00, 0.25,  998.75),

  -- Order 5
  (5, 13, 1,  899.00, 0.25, 1123.75),
  (5,  7, 2,  249.00, 0.25,  622.50),

  -- Order 6
  (6, 15, 1,  399.00, 0.25,  498.75),
  (6, 11, 1,  349.00, 0.25,  436.25),
  (6, 21, 1,  119.00, 0.25,  148.75),

  -- Order 7
  (7,  2, 1, 1299.00, 0.25, 1623.75),
  (7,  6, 1,  249.00, 0.25,  311.25),

  -- Order 8
  (8,  9, 1,  599.00, 0.25,  748.75),
  (8, 17, 1,  799.00, 0.25,  998.75),

  -- Order 9
  (9,  5, 1, 1099.00, 0.25, 1373.75),
  (9, 22, 2,  119.00, 0.25,  297.50),

  -- Order 10
  (10, 19, 1,  299.00, 0.25,  373.75),
  (10, 14, 1,  899.00, 0.25, 1123.75),
  (10, 12, 1,  349.00, 0.25,  436.25);

SELECT * FROM order_information;
