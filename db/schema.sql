-- HanapBahay: A Real Estate Listing, Inquiry, and Sales Monitoring System
-- Database design from the project paper, section 3.4 (Database Tables).
-- Run this in phpMyAdmin (Import) to create the database. Re-run it after pulling changes to this file.
-- WARNING: it deletes the old "hanapbahay" database first.

DROP DATABASE IF EXISTS hanapbahay;
CREATE DATABASE hanapbahay;
USE hanapbahay;

-- 3.4.1 Users: login accounts of the Owner, Admins and Agents.
-- Passwords are stored as SHA-256 hashes, never as plain text.
-- There is no sign-up: the Owner account is created here (at installation); every other
-- account is created inside the app by a user with higher authority (created_by).
CREATE TABLE users (
    user_id        INT AUTO_INCREMENT PRIMARY KEY,
    username       VARCHAR(50) NOT NULL UNIQUE,
    password_hash  CHAR(64)    NOT NULL,
    role           ENUM('OWNER', 'ADMIN', 'AGENT') NOT NULL DEFAULT 'AGENT',
    is_active      TINYINT(1)  NOT NULL DEFAULT 1,       -- 0 = deactivated, cannot log in
    created_by     INT NULL,                             -- the user who created this account
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);

-- 3.4.2 Agents: the profile of each agent account
CREATE TABLE agents (
    agent_id        INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL UNIQUE,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    contact_number  VARCHAR(30),
    email           VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 3.4.3 Properties: one row per listing
CREATE TABLE properties (
    property_id       INT AUTO_INCREMENT PRIMARY KEY,
    title             VARCHAR(150)  NOT NULL,
    property_type     VARCHAR(30)   NOT NULL,   -- House and Lot, Vacant Lot, Condominium
    property_subtype  VARCHAR(50),              -- e.g. Townhouse, Residential, Studio
    developer         VARCHAR(100),
    location          VARCHAR(200)  NOT NULL,
    price             DECIMAL(14,2) NOT NULL,
    lot_area          DECIMAL(10,2) NOT NULL DEFAULT 0,   -- square meters
    floor_area        DECIMAL(10,2) NOT NULL DEFAULT 0,   -- square meters (0 for a vacant lot)
    status            ENUM('AVAILABLE', 'RESERVED', 'SOLD') NOT NULL DEFAULT 'AVAILABLE',
    agent_id          INT NOT NULL,
    FOREIGN KEY (agent_id) REFERENCES agents(agent_id)
);

-- 3.4.4 PropertyPhotos: up to 4 photos per property. Only the file path is stored;
-- the image files are saved in the "photos" folder of the project.
CREATE TABLE property_photos (
    photo_id     INT AUTO_INCREMENT PRIMARY KEY,
    property_id  INT NOT NULL,
    image_path   VARCHAR(255) NOT NULL,
    caption      VARCHAR(150),
    FOREIGN KEY (property_id) REFERENCES properties(property_id) ON DELETE CASCADE
);

-- 3.4.5 Clients: buyers and what they are looking for
CREATE TABLE clients (
    client_id           INT AUTO_INCREMENT PRIMARY KEY,
    first_name          VARCHAR(50)  NOT NULL,
    last_name           VARCHAR(50)  NOT NULL,
    contact_number      VARCHAR(30)  NOT NULL,
    email               VARCHAR(100),
    budget              DECIMAL(14,2) NOT NULL,
    preferred_location  VARCHAR(200),
    preferred_type      VARCHAR(30),             -- a property type, or "Any"
    agent_id            INT NOT NULL,
    FOREIGN KEY (agent_id) REFERENCES agents(agent_id)
);

-- 3.4.6 Viewings: scheduled property visits
CREATE TABLE viewings (
    viewing_id    INT AUTO_INCREMENT PRIMARY KEY,
    client_id     INT NOT NULL,
    property_id   INT NOT NULL,
    agent_id      INT NOT NULL,
    viewing_date  DATE NOT NULL,
    viewing_time  TIME NOT NULL,
    status        ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'SCHEDULED',
    FOREIGN KEY (client_id)   REFERENCES clients(client_id),
    FOREIGN KEY (property_id) REFERENCES properties(property_id),
    FOREIGN KEY (agent_id)    REFERENCES agents(agent_id)
);

-- 3.4.7 PaymentMethods: PAG-IBIG, Bank Financing, Cash
CREATE TABLE payment_methods (
    payment_method_id  INT AUTO_INCREMENT PRIMARY KEY,
    method_name        VARCHAR(50) NOT NULL,
    description        VARCHAR(200)
);

-- 3.4.8 Deals: every property transaction
CREATE TABLE deals (
    deal_id            INT AUTO_INCREMENT PRIMARY KEY,
    property_id        INT NOT NULL,
    client_id          INT NOT NULL,
    agent_id           INT NOT NULL,
    payment_method_id  INT NOT NULL,
    agreed_price       DECIMAL(14,2) NOT NULL,
    deal_date          DATE NOT NULL,
    status             ENUM('PENDING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (property_id)       REFERENCES properties(property_id),
    FOREIGN KEY (client_id)         REFERENCES clients(client_id),
    FOREIGN KEY (agent_id)          REFERENCES agents(agent_id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(payment_method_id)
);

-- ===================== Sample data for testing =====================
-- Passwords: owner / owner123, admin / admin123, agent1 / agent123, agent2 / agent123
-- (stored as SHA-256 hashes). The owner is created first; the owner created the admin,
-- and the admin created the two agents.
INSERT INTO users (username, password_hash, role, created_by) VALUES
    ('owner',  '43a0d17178a9d26c9e0fe9a74b0b45e38d32f27aed887a008a54bf6e033bf7b9', 'OWNER', NULL);
INSERT INTO users (username, password_hash, role, created_by) VALUES
    ('admin',  '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN', 1),
    ('agent1', 'f44d1ac9bf0c69b083380b86dbdf3b73797150e3cca4820ac399f7917e607647', 'AGENT', 2),
    ('agent2', 'f44d1ac9bf0c69b083380b86dbdf3b73797150e3cca4820ac399f7917e607647', 'AGENT', 2);

INSERT INTO agents (user_id, first_name, last_name, contact_number, email) VALUES
    (3, 'Maria', 'Santos', '0917 123 4567', 'maria.santos@hanapbahay.ph'),
    (4, 'Paolo', 'Reyes',  '0918 765 4321', 'paolo.reyes@hanapbahay.ph');

INSERT INTO payment_methods (method_name, description) VALUES
    ('PAG-IBIG',       'Housing loan through the Pag-IBIG Fund'),
    ('Bank Financing', 'Housing loan from a bank'),
    ('Cash',           'Full payment in cash');

INSERT INTO properties (title, property_type, property_subtype, developer, location, price, lot_area, floor_area, status, agent_id) VALUES
    ('2BR Townhouse',     'House and Lot', 'Townhouse', 'Camella Homes', 'Quezon City', 3500000.00, 60.00, 72.00, 'AVAILABLE', 1),
    ('Studio Condo Unit', 'Condominium',   'Studio',    'SMDC',          'Makati City', 2800000.00,  0.00, 24.00, 'AVAILABLE', 1),
    ('Corner Lot',        'Vacant Lot',    'Residential', 'Ayala Land',  'Lipa City',   1800000.00, 150.00, 0.00, 'AVAILABLE', 2);

INSERT INTO clients (first_name, last_name, contact_number, email, budget, preferred_location, preferred_type, agent_id) VALUES
    ('Ana',  'Cruz',   '0919 111 2222', 'ana.cruz@email.com', 4000000.00, 'Quezon City', 'House and Lot', 1),
    ('Ben',  'Garcia', '0920 333 4444', '',                   3000000.00, 'Makati',      'Condominium',   1);
