-- ============================================
-- CampusConnect - Complete Database Schema
-- PostgreSQL Database Creation Script
-- ============================================

-- Drop existing tables if they exist (in correct order to handle foreign keys)
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS chats CASCADE;
DROP TABLE IF EXISTS follows CASCADE;
DROP TABLE IF EXISTS posts CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================
-- 1. USERS TABLE
-- ============================================
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    profile_picture TEXT,
    college VARCHAR(100),
    semester VARCHAR(50),
    batch VARCHAR(50),
    admin BOOLEAN DEFAULT FALSE,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for users table
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_created_on ON users(created_on);

-- ============================================
-- 2. ROLES TABLE
-- ============================================
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default roles
INSERT INTO roles (role_name, description) VALUES
('USER', 'Regular user with basic permissions'),
('ADMIN', 'Administrator with full permissions'),
('MODERATOR', 'Moderator with content management permissions');

-- ============================================
-- 3. USER_ROLES TABLE (Many-to-Many)
-- ============================================
CREATE TABLE user_roles (
    user_role_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    assigned_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    UNIQUE(user_id, role_id)
);

-- Create indexes for user_roles table
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles(role_id);

-- ============================================
-- 4. POSTS TABLE
-- ============================================
CREATE TABLE posts (
    post_id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    image TEXT,
    video TEXT,
    user_id INTEGER NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create indexes for posts table
CREATE INDEX idx_posts_user_id ON posts(user_id);
CREATE INDEX idx_posts_created_on ON posts(created_on DESC);

-- ============================================
-- 5. FOLLOWS TABLE
-- ============================================
CREATE TABLE follows (
    follow_id SERIAL PRIMARY KEY,
    follower_id INTEGER NOT NULL,
    following_id INTEGER NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (follower_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE(follower_id, following_id),
    CHECK (follower_id != following_id)
);

-- Create indexes for follows table
CREATE INDEX idx_follows_follower_id ON follows(follower_id);
CREATE INDEX idx_follows_following_id ON follows(following_id);
CREATE INDEX idx_follows_composite ON follows(follower_id, following_id);

-- ============================================
-- 6. CHATS TABLE
-- ============================================
CREATE TABLE chats (
    id BIGSERIAL PRIMARY KEY,
    user1_id INTEGER NOT NULL,
    user2_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_message_at TIMESTAMP,
    FOREIGN KEY (user1_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (user2_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CHECK (user1_id != user2_id),
    UNIQUE(user1_id, user2_id)
);

-- Create indexes for chats table
CREATE INDEX idx_chats_user1_id ON chats(user1_id);
CREATE INDEX idx_chats_user2_id ON chats(user2_id);
CREATE INDEX idx_chats_last_message_at ON chats(last_message_at DESC);
CREATE INDEX idx_chats_users ON chats(user1_id, user2_id);

-- ============================================
-- 7. MESSAGES TABLE
-- ============================================
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    sender_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    content VARCHAR(100) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CHECK (sender_id != receiver_id),
    CHECK (LENGTH(content) <= 100)
);

-- Create indexes for messages table
CREATE INDEX idx_messages_chat_id ON messages(chat_id);
CREATE INDEX idx_messages_sender_id ON messages(sender_id);
CREATE INDEX idx_messages_receiver_id ON messages(receiver_id);
CREATE INDEX idx_messages_created_on ON messages(created_on DESC);
CREATE INDEX idx_messages_unread ON messages(receiver_id, is_read) WHERE is_read = FALSE;

-- ============================================
-- VIEWS (Optional - for easier queries)
-- ============================================

-- View for user statistics
CREATE OR REPLACE VIEW user_stats AS
SELECT 
    u.user_id,
    u.username,
    u.first_name,
    u.last_name,
    COUNT(DISTINCT p.post_id) as post_count,
    COUNT(DISTINCT f1.follow_id) as follower_count,
    COUNT(DISTINCT f2.follow_id) as following_count
FROM users u
LEFT JOIN posts p ON u.user_id = p.user_id
LEFT JOIN follows f1 ON u.user_id = f1.following_id
LEFT JOIN follows f2 ON u.user_id = f2.follower_id
GROUP BY u.user_id, u.username, u.first_name, u.last_name;

-- View for mutual follows
CREATE OR REPLACE VIEW mutual_follows AS
SELECT 
    f1.follower_id as user1_id,
    f1.following_id as user2_id,
    f1.created_on as follow_date
FROM follows f1
INNER JOIN follows f2 
    ON f1.follower_id = f2.following_id 
    AND f1.following_id = f2.follower_id
WHERE f1.follower_id < f1.following_id;

-- ============================================
-- FUNCTIONS (Optional - for common operations)
-- ============================================

-- Function to check if users are mutual followers
CREATE OR REPLACE FUNCTION is_mutual_follow(user1_id INTEGER, user2_id INTEGER)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (
        SELECT 1 FROM follows 
        WHERE follower_id = user1_id AND following_id = user2_id
    ) AND EXISTS (
        SELECT 1 FROM follows 
        WHERE follower_id = user2_id AND following_id = user1_id
    );
END;
$$ LANGUAGE plpgsql;

-- Function to get unread message count for a user
CREATE OR REPLACE FUNCTION get_unread_count(p_user_id INTEGER)
RETURNS BIGINT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*) 
        FROM messages 
        WHERE receiver_id = p_user_id AND is_read = FALSE
    );
END;
$$ LANGUAGE plpgsql;

-- ============================================
-- TRIGGERS (Optional - for automatic updates)
-- ============================================

-- Trigger to update updated_on timestamp for users
CREATE OR REPLACE FUNCTION update_updated_on_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_on = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_users_updated_on
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_on_column();

CREATE TRIGGER update_posts_updated_on
    BEFORE UPDATE ON posts
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_on_column();

-- Trigger to update last_message_at in chats
CREATE OR REPLACE FUNCTION update_chat_last_message()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE chats 
    SET last_message_at = NEW.created_on 
    WHERE id = NEW.chat_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_chat_timestamp
    AFTER INSERT ON messages
    FOR EACH ROW
    EXECUTE FUNCTION update_chat_last_message();

-- ============================================
-- SAMPLE DATA (Optional - for testing)
-- ============================================

-- Insert sample users
INSERT INTO users (username, password, first_name, last_name, email, college, semester, batch, admin) VALUES
('admin', '$2a$10$dummyhashedpassword1', 'Admin', 'User', 'admin@campusconnect.com', 'RNS Institute of Technology', '7th Semester', '2022-2026', TRUE),
('john_doe', '$2a$10$dummyhashedpassword2', 'John', 'Doe', 'john@example.com', 'RNS Institute of Technology', '7th Semester', '2022-2026', FALSE),
('jane_smith', '$2a$10$dummyhashedpassword3', 'Jane', 'Smith', 'jane@example.com', 'RNS Institute of Technology', '7th Semester', '2022-2026', FALSE);

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 2), -- Admin user gets ADMIN role
(2, 1), -- John gets USER role
(3, 1); -- Jane gets USER role

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

-- Check all tables were created
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
ORDER BY table_name;

-- Check all indexes
SELECT 
    tablename, 
    indexname, 
    indexdef 
FROM pg_indexes 
WHERE schemaname = 'public' 
ORDER BY tablename, indexname;

-- Check all foreign keys
SELECT
    tc.table_name, 
    kcu.column_name, 
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name 
FROM information_schema.table_constraints AS tc 
JOIN information_schema.key_column_usage AS kcu
    ON tc.constraint_name = kcu.constraint_name
    AND tc.table_schema = kcu.table_schema
JOIN information_schema.constraint_column_usage AS ccu
    ON ccu.constraint_name = tc.constraint_name
    AND ccu.table_schema = tc.table_schema
WHERE tc.constraint_type = 'FOREIGN KEY' 
    AND tc.table_schema = 'public'
ORDER BY tc.table_name;

-- ============================================
-- GRANT PERMISSIONS (Optional - for production)
-- ============================================

-- Grant permissions to application user (replace 'app_user' with your username)
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO app_user;
-- GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO app_user;

-- ============================================
-- NOTES
-- ============================================

/*
1. This script creates all tables for the CampusConnect application
2. Includes proper foreign keys, indexes, and constraints
3. Flyway migrations (V1 and V2) will handle this automatically
4. For Railway deployment, this runs automatically via Hibernate + Flyway
5. For manual setup, run this script in your PostgreSQL database

TABLES CREATED:
- users: User accounts and profiles
- roles: User roles (USER, ADMIN, MODERATOR)
- user_roles: User-role assignments
- posts: User posts with media
- follows: Follow relationships
- chats: Chat conversations
- messages: Chat messages

FEATURES:
- Cascading deletes for data integrity
- Indexes for query performance
- Constraints to prevent invalid data
- Triggers for automatic timestamp updates
- Views for common queries
- Functions for business logic

USAGE:
1. Connect to PostgreSQL: psql -U postgres -d campusconnect
2. Run this script: \i complete_schema.sql
3. Verify tables: \dt
4. Check data: SELECT * FROM users;
*/
