-- ============================================
-- CampusConnect Database Schema
-- PostgreSQL Database Creation Script
-- ============================================

-- Drop existing tables if they exist (in correct order due to foreign keys)
DROP TABLE IF EXISTS user_role CASCADE;
DROP TABLE IF EXISTS post CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS role CASCADE;

-- ============================================
-- Table: users
-- Description: Stores user account information
-- ============================================
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- BCrypt hashed password
    gender VARCHAR(20),
    profile_picture TEXT, -- URL to profile image
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- College/University Information
    college VARCHAR(255),
    semester VARCHAR(50),
    batch VARCHAR(50),
    
    -- Admin flag
    is_admin BOOLEAN DEFAULT FALSE,
    
    -- Account status
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT chk_username_length CHECK (LENGTH(username) >= 3 AND LENGTH(username) <= 50),
    CONSTRAINT chk_gender CHECK (gender IN ('Male', 'Female', 'Other'))
);

-- Indexes for users table
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_created_on ON users(created_on DESC);
CREATE INDEX idx_users_college ON users(college);
CREATE INDEX idx_users_is_admin ON users(is_admin);

-- ============================================
-- Table: role
-- Description: Defines user roles in the system
-- ============================================
CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_role_name CHECK (role_name IN ('ADMIN', 'USER', 'MODERATOR'))
);

-- Index for role table
CREATE INDEX idx_role_name ON role(role_name);

-- ============================================
-- Table: user_role
-- Description: Junction table for many-to-many relationship between users and roles
-- ============================================
CREATE TABLE user_role (
    user_role_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by INTEGER, -- User ID of who assigned this role
    
    -- Foreign Keys
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) 
        REFERENCES role(role_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_assigned_by FOREIGN KEY (assigned_by) 
        REFERENCES users(user_id) ON DELETE SET NULL,
    
    -- Unique constraint to prevent duplicate role assignments
    CONSTRAINT uk_user_role UNIQUE (user_id, role_id)
);

-- Indexes for user_role table
CREATE INDEX idx_user_role_user_id ON user_role(user_id);
CREATE INDEX idx_user_role_role_id ON user_role(role_id);

-- ============================================
-- Table: post
-- Description: Stores user posts/articles
-- ============================================
CREATE TABLE post (
    post_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    content TEXT,
    image TEXT, -- URL to image
    video TEXT, -- URL to video
    created_on DATE DEFAULT CURRENT_DATE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Post metadata
    is_edited BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP,
    
    -- Engagement metrics (for future use)
    view_count INTEGER DEFAULT 0,
    like_count INTEGER DEFAULT 0,
    comment_count INTEGER DEFAULT 0,
    
    -- Foreign Keys
    CONSTRAINT fk_post_user FOREIGN KEY (user_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    
    -- Constraints
    CONSTRAINT chk_post_has_content CHECK (
        content IS NOT NULL OR image IS NOT NULL OR video IS NOT NULL
    )
);

-- Indexes for post table
CREATE INDEX idx_post_user_id ON post(user_id);
CREATE INDEX idx_post_created_on ON post(created_on DESC);
CREATE INDEX idx_post_is_deleted ON post(is_deleted);
CREATE INDEX idx_post_user_created ON post(user_id, created_on DESC);

-- ============================================
-- Triggers for automatic timestamp updates
-- ============================================

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for users table
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger for role table
CREATE TRIGGER update_role_updated_at
    BEFORE UPDATE ON role
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger for post table
CREATE TRIGGER update_post_updated_at
    BEFORE UPDATE ON post
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- Insert default roles
-- ============================================
INSERT INTO role (role_name, description) VALUES
    ('USER', 'Standard user with basic permissions'),
    ('ADMIN', 'Administrator with full system access'),
    ('MODERATOR', 'Moderator with content management permissions')
ON CONFLICT (role_name) DO NOTHING;

-- ============================================
-- Views for common queries
-- ============================================

-- View: User posts with user information
CREATE OR REPLACE VIEW v_user_posts AS
SELECT 
    p.post_id,
    p.content,
    p.image,
    p.video,
    p.created_on,
    p.view_count,
    p.like_count,
    p.comment_count,
    u.user_id,
    u.username,
    u.first_name,
    u.last_name,
    u.profile_picture,
    u.college,
    u.semester,
    u.batch,
    u.is_admin
FROM post p
INNER JOIN users u ON p.user_id = u.user_id
WHERE p.is_deleted = FALSE
ORDER BY p.created_on DESC;

-- View: User roles
CREATE OR REPLACE VIEW v_user_roles AS
SELECT 
    u.user_id,
    u.username,
    u.email,
    r.role_id,
    r.role_name,
    ur.assigned_at
FROM users u
INNER JOIN user_role ur ON u.user_id = ur.user_id
INNER JOIN role r ON ur.role_id = r.role_id;

-- ============================================
-- Useful queries and functions
-- ============================================

-- Function to get user's post count
CREATE OR REPLACE FUNCTION get_user_post_count(p_user_id INTEGER)
RETURNS INTEGER AS $$
DECLARE
    post_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO post_count
    FROM post
    WHERE user_id = p_user_id AND is_deleted = FALSE;
    
    RETURN post_count;
END;
$$ LANGUAGE plpgsql;

-- Function to check if username exists
CREATE OR REPLACE FUNCTION username_exists(p_username VARCHAR)
RETURNS BOOLEAN AS $$
DECLARE
    exists_flag BOOLEAN;
BEGIN
    SELECT EXISTS(
        SELECT 1 FROM users WHERE username = p_username
    ) INTO exists_flag;
    
    RETURN exists_flag;
END;
$$ LANGUAGE plpgsql;

-- Function to check if email exists
CREATE OR REPLACE FUNCTION email_exists(p_email VARCHAR)
RETURNS BOOLEAN AS $$
DECLARE
    exists_flag BOOLEAN;
BEGIN
    SELECT EXISTS(
        SELECT 1 FROM users WHERE email = p_email
    ) INTO exists_flag;
    
    RETURN exists_flag;
END;
$$ LANGUAGE plpgsql;

-- ============================================
-- Sample data for testing (optional)
-- ============================================

-- Uncomment to insert sample data
/*
-- Sample admin user (password: Admin@123 - hashed with BCrypt)
INSERT INTO users (username, first_name, last_name, email, password, gender, college, semester, batch, is_admin)
VALUES (
    'admin',
    'Admin',
    'User',
    'admin@campusconnect.com',
    '$2a$10$YourBCryptHashedPasswordHere',
    'Other',
    'CampusConnect University',
    'N/A',
    'N/A',
    TRUE
);

-- Sample regular user
INSERT INTO users (username, first_name, last_name, email, password, gender, college, semester, batch)
VALUES (
    'johndoe',
    'John',
    'Doe',
    'john.doe@university.edu',
    '$2a$10$YourBCryptHashedPasswordHere',
    'Male',
    'State University',
    '5th Semester',
    '2023-2027'
);

-- Sample post
INSERT INTO post (user_id, content)
VALUES (
    1,
    'Welcome to CampusConnect! This is my first post.'
);
*/

-- ============================================
-- Database statistics and maintenance
-- ============================================

-- Query to check table sizes
-- SELECT 
--     schemaname,
--     tablename,
--     pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
-- FROM pg_tables
-- WHERE schemaname = 'public'
-- ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- ============================================
-- Backup and restore commands
-- ============================================

-- Backup database:
-- pg_dump -U postgres -d socialmedia_db -F c -b -v -f campusconnect_backup.dump

-- Restore database:
-- pg_restore -U postgres -d socialmedia_db -v campusconnect_backup.dump

-- ============================================
-- End of schema
-- ============================================

-- Grant permissions (adjust as needed)
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO campusconnect_user;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO campusconnect_user;
-- GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO campusconnect_user;

COMMIT;
