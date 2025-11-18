-- ============================================
-- CampusConnect - Simplified Database Schema
-- PostgreSQL - Essential Tables Only
-- ============================================

-- Create database (run as postgres superuser)
-- CREATE DATABASE socialmedia_db;
-- \c socialmedia_db;

-- ============================================
-- 1. USERS TABLE
-- ============================================
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    gender VARCHAR(20),
    profile_picture TEXT,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    college VARCHAR(255),
    semester VARCHAR(50),
    batch VARCHAR(50),
    is_admin BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- ============================================
-- 2. ROLE TABLE
-- ============================================
CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default roles
INSERT INTO role (role_name, description) VALUES
    ('USER', 'Standard user'),
    ('ADMIN', 'Administrator'),
    ('MODERATOR', 'Moderator');

-- ============================================
-- 3. USER_ROLE TABLE (Junction Table)
-- ============================================
CREATE TABLE user_role (
    user_role_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE,
    UNIQUE (user_id, role_id)
);

CREATE INDEX idx_user_role_user_id ON user_role(user_id);
CREATE INDEX idx_user_role_role_id ON user_role(role_id);

-- ============================================
-- 4. POST TABLE
-- ============================================
CREATE TABLE post (
    post_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    content TEXT,
    image TEXT,
    video TEXT,
    created_on DATE DEFAULT CURRENT_DATE,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_post_user_id ON post(user_id);
CREATE INDEX idx_post_created_on ON post(created_on DESC);

-- ============================================
-- Verification Queries
-- ============================================

-- Check all tables
-- SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';

-- Check table structure
-- \d users
-- \d role
-- \d user_role
-- \d post

COMMIT;
