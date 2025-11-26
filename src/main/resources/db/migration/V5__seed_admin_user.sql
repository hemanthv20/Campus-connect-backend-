-- ============================================
-- CampusConnect - Seed Admin User
-- Creates the master admin account if it doesn't exist
-- ============================================

-- Insert admin user if not exists
-- Password: Admin@123 (will be hashed by the application)
INSERT INTO users (username, password, first_name, last_name, email, admin, created_on, updated_on)
SELECT 'admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'System', 'Administrator', 'admin@campusconnect.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);

-- Ensure admin role exists
INSERT INTO roles (role_name, description, created_on)
SELECT 'ADMIN', 'System Administrator with full platform control', CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE role_name = 'ADMIN'
);

-- Assign admin role to admin user
INSERT INTO user_roles (user_id, role_id, assigned_on)
SELECT u.user_id, r.role_id, CURRENT_TIMESTAMP
FROM users u, roles r
WHERE u.username = 'admin' 
  AND r.role_name = 'ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur 
    WHERE ur.user_id = u.user_id AND ur.role_id = r.role_id
  );

-- Create indexes for admin operations
CREATE INDEX IF NOT EXISTS idx_users_admin ON users(admin);
CREATE INDEX IF NOT EXISTS idx_user_roles_lookup ON user_roles(user_id, role_id);