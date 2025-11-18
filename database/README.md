# CampusConnect Database Setup Guide

## 📊 Database Schema Overview

This directory contains SQL scripts to set up the CampusConnect PostgreSQL database.

---

## 📁 Files

1. **schema.sql** - Complete schema with all features:

   - All tables with constraints
   - Indexes for performance
   - Triggers for automatic timestamps
   - Views for common queries
   - Utility functions
   - Sample data (commented out)

2. **schema_simple.sql** - Minimal schema:
   - Essential tables only
   - Basic indexes
   - No triggers or views
   - Quick setup

---

## 🚀 Quick Setup

### Option 1: Using psql Command Line

```bash
# 1. Create database
createdb -U postgres socialmedia_db

# 2. Run schema
psql -U postgres -d socialmedia_db -f schema_simple.sql

# Or for full schema:
psql -U postgres -d socialmedia_db -f schema.sql
```

### Option 2: Using pgAdmin

1. Open pgAdmin
2. Right-click on "Databases" → Create → Database
3. Name: `socialmedia_db`
4. Right-click on the database → Query Tool
5. Open and execute `schema_simple.sql` or `schema.sql`

### Option 3: Using DBeaver

1. Connect to PostgreSQL server
2. Right-click on server → Create → Database
3. Name: `socialmedia_db`
4. Right-click on database → SQL Editor → New SQL Script
5. Paste contents of `schema_simple.sql` or `schema.sql`
6. Execute (Ctrl+Enter)

---

## 📋 Database Tables

### 1. **users** - User Information

```sql
Columns:
- user_id (PK, SERIAL)
- username (UNIQUE, NOT NULL)
- first_name (NOT NULL)
- last_name (NOT NULL)
- email (UNIQUE, NOT NULL)
- password (NOT NULL) - BCrypt hashed
- gender
- profile_picture (URL)
- created_on (TIMESTAMP)
- college
- semester
- batch
- is_admin (BOOLEAN)
```

### 2. **role** - User Roles

```sql
Columns:
- role_id (PK, SERIAL)
- role_name (UNIQUE, NOT NULL)
- description
- created_at (TIMESTAMP)

Default Roles:
- USER
- ADMIN
- MODERATOR
```

### 3. **user_role** - User-Role Mapping

```sql
Columns:
- user_role_id (PK, SERIAL)
- user_id (FK → users)
- role_id (FK → role)
- assigned_at (TIMESTAMP)

Constraints:
- UNIQUE(user_id, role_id)
- CASCADE DELETE
```

### 4. **post** - User Posts

```sql
Columns:
- post_id (PK, SERIAL)
- user_id (FK → users)
- content (TEXT)
- image (URL)
- video (URL)
- created_on (DATE)

Constraints:
- CASCADE DELETE on user deletion
```

---

## 🔍 Verification

After running the schema, verify the setup:

```sql
-- Check all tables exist
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public';

-- Check users table structure
\d users

-- Check if roles were inserted
SELECT * FROM role;

-- Count tables
SELECT COUNT(*) FROM information_schema.tables
WHERE table_schema = 'public';
-- Should return 4
```

---

## 📊 Entity Relationships

```
users (1) ←→ (N) post
  ↓
  ↓ (N)
  ↓
user_role (N) ←→ (1) role
```

- One user can have many posts
- One user can have many roles
- One role can be assigned to many users

---

## 🔧 Common Operations

### Create Admin User

```sql
INSERT INTO users (
    username, first_name, last_name, email, password,
    gender, college, semester, batch, is_admin
) VALUES (
    'admin',
    'Admin',
    'User',
    'admin@campusconnect.com',
    '$2a$10$YourBCryptHashedPasswordHere', -- Hash your password first!
    'Other',
    'CampusConnect University',
    'N/A',
    'N/A',
    TRUE
);
```

### Assign Role to User

```sql
-- Get user_id and role_id first
INSERT INTO user_role (user_id, role_id)
VALUES (1, 2); -- User 1 gets Admin role (role_id 2)
```

### Query User with Posts

```sql
SELECT
    u.username,
    u.first_name,
    u.last_name,
    COUNT(p.post_id) as post_count
FROM users u
LEFT JOIN post p ON u.user_id = p.user_id
GROUP BY u.user_id, u.username, u.first_name, u.last_name;
```

### Get User's Roles

```sql
SELECT
    u.username,
    r.role_name
FROM users u
JOIN user_role ur ON u.user_id = ur.user_id
JOIN role r ON ur.role_id = r.role_id
WHERE u.username = 'admin';
```

---

## 🔒 Security Notes

1. **Password Storage**: Always use BCrypt hashing

   ```java
   BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
   String hashedPassword = encoder.encode("plainPassword");
   ```

2. **Database User**: Create a dedicated database user

   ```sql
   CREATE USER campusconnect_user WITH PASSWORD 'secure_password';
   GRANT ALL PRIVILEGES ON DATABASE socialmedia_db TO campusconnect_user;
   GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO campusconnect_user;
   GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO campusconnect_user;
   ```

3. **Connection String**: Use environment variables
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/socialmedia_db
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
   ```

---

## 📈 Performance Tips

### Indexes Already Created

- `idx_users_username` - Fast username lookups
- `idx_users_email` - Fast email lookups
- `idx_post_user_id` - Fast user's posts queries
- `idx_post_created_on` - Fast chronological queries
- `idx_user_role_user_id` - Fast role lookups
- `idx_user_role_role_id` - Fast user lookups by role

### Additional Indexes (if needed)

```sql
-- For searching by college
CREATE INDEX idx_users_college ON users(college);

-- For full-text search on posts
CREATE INDEX idx_post_content_fts ON post USING gin(to_tsvector('english', content));
```

---

## 🔄 Migration from Existing Database

If you have existing data:

1. **Backup existing database**:

   ```bash
   pg_dump -U postgres socialmedia_db > backup.sql
   ```

2. **Create new database**:

   ```bash
   createdb -U postgres socialmedia_db_new
   ```

3. **Run new schema**:

   ```bash
   psql -U postgres -d socialmedia_db_new -f schema.sql
   ```

4. **Migrate data** (adjust as needed):

   ```sql
   -- Copy users (passwords need to be hashed!)
   INSERT INTO socialmedia_db_new.users
   SELECT * FROM socialmedia_db.users;

   -- Copy posts
   INSERT INTO socialmedia_db_new.post
   SELECT * FROM socialmedia_db.post;
   ```

---

## 🧪 Testing

### Test Data

```sql
-- Insert test user
INSERT INTO users (username, first_name, last_name, email, password, gender, college, semester, batch)
VALUES ('testuser', 'Test', 'User', 'test@test.com', '$2a$10$test', 'Male', 'Test College', '1st', '2024');

-- Insert test post
INSERT INTO post (user_id, content)
VALUES (1, 'This is a test post');

-- Verify
SELECT u.username, p.content
FROM users u
JOIN post p ON u.user_id = p.user_id;
```

---

## 🐛 Troubleshooting

### Issue: "relation already exists"

```sql
-- Drop all tables and recreate
DROP TABLE IF EXISTS user_role CASCADE;
DROP TABLE IF EXISTS post CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS role CASCADE;
-- Then run schema again
```

### Issue: "permission denied"

```sql
-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO your_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO your_user;
```

### Issue: Foreign key constraint fails

- Ensure parent records exist before inserting child records
- Insert in order: users → role → user_role, post

---

## 📚 Additional Resources

- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [BCrypt Password Hashing](https://en.wikipedia.org/wiki/Bcrypt)

---

## ✅ Checklist

After setup, verify:

- [ ] All 4 tables created
- [ ] All indexes created
- [ ] Default roles inserted (USER, ADMIN, MODERATOR)
- [ ] Foreign keys working
- [ ] Can insert test user
- [ ] Can insert test post
- [ ] Application can connect to database

---

**Database Version**: 1.0  
**Last Updated**: November 2025  
**Compatible with**: PostgreSQL 12+
