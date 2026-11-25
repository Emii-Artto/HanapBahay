-- HanapBahay: Real Estate Listing, Inquiry, and Sales Monitoring System
-- Owner: Database member. Run this in MySQL (e.g. phpMyAdmin or MySQL Workbench)
-- to create the database on your computer. Re-run it after pulling changes to this file.
--
-- Only Admins and Agents log in. Clients have no account: an agent opens Client Mode,
-- types the client's name, and hands the device over. Each unit the client selects
-- is saved as one row in "inquiries".

DROP DATABASE IF EXISTS hanapbahay;
CREATE DATABASE hanapbahay;
USE hanapbahay;

CREATE TABLE users (
    user_id      INT AUTO_INCREMENT PRIMARY KEY,
    username     VARCHAR(50)  NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,          -- backend should store a hash, not plain text
    full_name    VARCHAR(100) NOT NULL,
    role         ENUM('ADMIN', 'AGENT') NOT NULL DEFAULT 'AGENT'
);

CREATE TABLE properties (
    property_id  INT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(150)  NOT NULL,
    location     VARCHAR(200)  NOT NULL,
    type         VARCHAR(50)   NOT NULL,         -- e.g. House, Condo, Lot
    price        DECIMAL(14,2) NOT NULL,
    status       ENUM('AVAILABLE', 'RESERVED', 'SOLD') NOT NULL DEFAULT 'AVAILABLE',
    agent_id     INT NOT NULL,
    FOREIGN KEY (agent_id) REFERENCES users(user_id)
);

-- One row per unit a client selected in Client Mode
CREATE TABLE inquiries (
    inquiry_id     INT AUTO_INCREMENT PRIMARY KEY,
    property_id    INT NOT NULL,                 -- the unit the client selected
    client_name    VARCHAR(100) NOT NULL,        -- typed by the agent when starting Client Mode
    client_contact VARCHAR(30),                  -- optional
    message        TEXT,                         -- optional note from the client
    agent_id       INT NOT NULL,                 -- the agent who opened Client Mode
    status         ENUM('NEW', 'RESPONDED', 'CLOSED') NOT NULL DEFAULT 'NEW',
    inquiry_date   DATE NOT NULL,
    FOREIGN KEY (property_id) REFERENCES properties(property_id),
    FOREIGN KEY (agent_id)    REFERENCES users(user_id)
);

CREATE TABLE sales (
    sale_id        INT AUTO_INCREMENT PRIMARY KEY,
    property_id    INT NOT NULL UNIQUE,          -- a property can only be sold once
    buyer_name     VARCHAR(100) NOT NULL,
    buyer_contact  VARCHAR(30),
    agent_id       INT NOT NULL,
    sale_price     DECIMAL(14,2) NOT NULL,
    sale_date      DATE NOT NULL,
    FOREIGN KEY (property_id) REFERENCES properties(property_id),
    FOREIGN KEY (agent_id)    REFERENCES users(user_id)
);

-- Sample data for testing (plain-text passwords are for testing only)
INSERT INTO users (username, password, full_name, role) VALUES
    ('admin',  'admin123', 'System Admin', 'ADMIN'),
    ('agent1', 'agent123', 'Maria Santos', 'AGENT'),
    ('agent2', 'agent123', 'Paolo Reyes',  'AGENT');

INSERT INTO properties (title, location, type, price, status, agent_id) VALUES
    ('2BR Townhouse', 'Quezon City', 'House', 3500000.00, 'AVAILABLE', 2),
    ('Studio Condo Unit', 'Makati City', 'Condo', 2800000.00, 'AVAILABLE', 2);
