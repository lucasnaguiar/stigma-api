-- SQL Script for the Tattoo Studio Management System Database
-- Version: 1.4 (Lowercase Naming Convention)
-- Author: Gemini
-- Description: This version uses lowercase and snake_case for all table and column names.
--            The structure remains multi-tenant ready.

-- Ensures tables are created from a clean state.
-- WARNING: This will delete existing data. Comment out if not desired.
DROP TABLE IF EXISTS appointment_supplies;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS supplies;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS artists;
DROP TABLE IF EXISTS studios;


-- Manages the studios (tenants).
CREATE TABLE studios (
    id SERIAL PRIMARY KEY,
    studio_name VARCHAR(255) NOT NULL,
    subdomain VARCHAR(100) NOT NULL UNIQUE, -- e.g., 'alpha-ink' for alpha-ink.myapp.com
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE studios IS 'Tenant table. Each row represents a studio client of the system.';


-- Stores data for artists, associated with a studio.
CREATE TABLE artists (
    id SERIAL PRIMARY KEY,
    studio_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    FOREIGN KEY (studio_id) REFERENCES studios(id) ON DELETE CASCADE,
    UNIQUE (studio_id, email) -- The artist's email must be unique within a studio.
);


-- Stores customer data, associated with a studio.
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    studio_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (studio_id) REFERENCES studios(id) ON DELETE CASCADE,
    UNIQUE (studio_id, phone_number) -- A customer's phone number can only be registered once per studio.
);


-- Manages the inventory of supplies for each studio.
CREATE TABLE supplies (
    id SERIAL PRIMARY KEY,
    studio_id INT NOT NULL,
    supply_name VARCHAR(255) NOT NULL,
    description TEXT,
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    unit_of_measure VARCHAR(20) NOT NULL, -- e.g., 'unit', 'pair', 'box'
    reorder_point INT NOT NULL DEFAULT 0,

    FOREIGN KEY (studio_id) REFERENCES studios(id) ON DELETE CASCADE,
    UNIQUE (studio_id, supply_name) -- The supply name must be unique within a studio.
);

COMMENT ON COLUMN supplies.reorder_point IS 'The minimum quantity to trigger a reorder alert.';


-- Main table for appointments, also linked to a studio for quick queries and isolation.
CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    studio_id INT NOT NULL,
    artist_id INT NOT NULL,
    customer_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    period VARCHAR(10) NOT NULL CHECK (period IN ('MORNING', 'AFTERNOON', 'NIGHT')),
    status VARCHAR(15) NOT NULL CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELED')),
    tattoo_description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (studio_id) REFERENCES studios(id) ON DELETE CASCADE,
    FOREIGN KEY (artist_id) REFERENCES artists(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

COMMENT ON COLUMN appointments.period IS 'The period of the day for the appointment: MORNING, AFTERNOON, NIGHT.';


-- Junction table to record which supplies are used in each appointment.
CREATE TABLE appointment_supplies (
    id SERIAL PRIMARY KEY,
    appointment_id INT NOT NULL,
    supply_id INT NOT NULL,
    quantity_used INT NOT NULL CHECK (quantity_used > 0),

    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE,
    FOREIGN KEY (supply_id) REFERENCES supplies(id),
    UNIQUE (appointment_id, supply_id) -- Prevents adding the same supply twice to the same appointment.
);


-- --- INITIAL DATA ---
-- Inserts the first studio into the system. All initial operations will use the ID generated here (which will be 1).
INSERT INTO studios (studio_name, subdomain) VALUES ('My First Studio', 'default');


-- --- INDEXES FOR PERFORMANCE ---
-- Creates an optimized index for calendar-related queries.
CREATE INDEX idx_appointments_calendar
ON appointments (studio_id, appointment_date, artist_id);


-- End of Script