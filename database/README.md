# 🗄️ CampusConnect Database Schema

Complete SQL scripts for creating all database tables.

## 📁 Files

### 1. `complete_schema.sql` - Full Schema with All Features

- All 7 tables with complete structure
- Indexes for performance optimization
- Views for common queries
- Functions for business logic
- Triggers for automatic updates
- Sample data for testing
- Verification queries

### 2. `simple_schema.sql` - Essential Tables Only

- All 7 tables with basic structure
- Essential indexes only
- Default roles
- No triggers, views, or functions
- Quick setup for development

## 📊 Database Tables

### 1. **users** - User Accounts

```sql
- user_id (PRIMARY KEY)
- username (UNIQUE)
- password
- first_name, last_name
- email (UNIQUE)
- profile_picture
- college, semester, batch
- admin (BOOLEAN)
- created_on, updated_on
```

### 2. **roles** - User Roles

```sql
- role_id (PRIMARY KEY)
- role_name (UNIQUE)
- description
- created_on
```

### 3. **user_roles** - User-Role Assignments

```sql
- user_role_id (PRIMARY KEY)
- user_id (FOREIGN KEY → users)
- role_id (FOREIGN KEY → roles)
- assigned_on
```

### 4. **posts** - User Posts

```sql
- post_id (PRIMARY KEY)
- content
- image, video
- user_id (FOREIGN KEY → users)
- created_on, updated_on
```

### 5. **follows** - Follow Relationships

```sql
- follow_id (PRIMARY KEY)
- follower_id (FOREIGN KEY → users)
- following_id (FOREIGN KEY → users)
- created_on
- CONSTRAINT: follower_id != following_id
```

### 6. **chats** - Chat Conversations

```sql
- id (PRIMARY KEY)
- user1_id (FOREIGN KEY → users)
- user2_id (FOREIGN KEY → users)
- created_at
- last_message_at
- CONSTRAINT: user1_id != user2_id
```

### 7. **messages** - Chat Messages

```sql
- id (PRIMARY KEY)
- chat_id (FOREIGN KEY → chats)
- sender_id (FOREIGN KEY → users)
- receiver_id (FOREIGN KEY → users)
- content (MAX 100 chars)
- is_read (BOOLEAN)
- created_on
- CONSTRAINT: sender_id != receiver_id
```

## 🚀 Usage

### Option 1: Automatic (Recommended for Railway)

```bash
# Hibernate + Flyway will create tables automatically
# Just deploy your backend and tables are created
```

### Option 2: Manual Setup (Local Development)

```bash
# 1. Create database
createdb campusconnect

# 2. Connect to database
psql -U postgres -d campusconnect

# 3. Run schema script
\i database/simple_schema.sql

# 4. Verify tables
\dt

# 5. Check data
SELECT * FROM users;
```

### Option 3: Using psql Command Line

```bash
# Run complete schema
psql -U postgres -d campusconnect -f database/complete_schema.sql

# Or run simple schema
psql -U postgres -d campusconnect -f database/simple_schema.sql
```

### Option 4: Using pgAdmin

```
1. Open pgAdmin
2. Connect to your PostgreSQL server
3. Right-click on database → Query Tool
4. Open complete_schema.sql or simple_schema.sql
5. Execute (F5)
```

## 🔍 Verification Queries

### Check All Tables

```sql
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public'
ORDER BY table_name;
```

### Check Table Structure

```sql
\d users
\d posts
\d follows
\d chats
\d messages
```

### Check Foreign Keys

```sql
SELECT
    tc.table_name,
    kcu.column_name,
    ccu.table_name AS foreign_table_name
FROM information_schema.table_constraints AS tc
JOIN information_schema.key_column_usage AS kcu
    ON tc.constraint_name = kcu.constraint_name
JOIN information_schema.constraint_column_usage AS ccu
    ON ccu.constraint_name = tc.constraint_name
WHERE tc.constraint_type = 'FOREIGN KEY';
```

### Check Indexes

```sql
SELECT tablename, indexname
FROM pg_indexes
WHERE schemaname = 'public'
ORDER BY tablename;
```

## 📈 Database Relationships

```
users (1) ──→ (N) posts
users (1) ──→ (N) follows (as follower)
users (1) ──→ (N) follows (as following)
users (1) ──→ (N) chats (as user1)
users (1) ──→ (N) chats (as user2)
users (1) ──→ (N) messages (as sender)
users (1) ──→ (N) messages (as receiver)
chats (1) ──→ (N) messages
users (N) ←→ (N) roles (through user_roles)
```

## 🔐 Important Constraints

### Users Table

- Username must be unique
- Email must be unique
- Password is hashed (BCrypt)

### Follows Table

- Cannot follow yourself
- Unique follower-following pair
- Cascading delete when user is deleted

### Chats Table

- Cannot chat with yourself
- Unique user pair
- Cascading delete when user is deleted

### Messages Table

- Cannot message yourself
- Content limited to 100 characters
- Cascading delete when chat is deleted

## 🎯 Sample Queries

### Get User's Posts

```sql
SELECT * FROM posts
WHERE user_id = 1
ORDER BY created_on DESC;
```

### Get User's Followers

```sql
SELECT u.* FROM users u
JOIN follows f ON u.user_id = f.follower_id
WHERE f.following_id = 1;
```

### Get User's Following

```sql
SELECT u.* FROM users u
JOIN follows f ON u.user_id = f.following_id
WHERE f.follower_id = 1;
```

### Get Mutual Followers

```sql
SELECT u.* FROM users u
WHERE EXISTS (
    SELECT 1 FROM follows f1
    WHERE f1.follower_id = 1 AND f1.following_id = u.user_id
) AND EXISTS (
    SELECT 1 FROM follows f2
    WHERE f2.follower_id = u.user_id AND f2.following_id = 1
);
```

### Get User's Chats

```sql
SELECT * FROM chats
WHERE user1_id = 1 OR user2_id = 1
ORDER BY last_message_at DESC;
```

### Get Unread Messages

```sql
SELECT * FROM messages
WHERE receiver_id = 1 AND is_read = FALSE
ORDER BY created_on DESC;
```

## 🛠️ Maintenance

### Backup Database

```bash
pg_dump -U postgres campusconnect > backup.sql
```

### Restore Database

```bash
psql -U postgres campusconnect < backup.sql
```

### Drop All Tables

```bash
psql -U postgres -d campusconnect -f database/drop_all.sql
```

### Reset Database

```bash
# Drop and recreate
dropdb campusconnect
createdb campusconnect
psql -U postgres -d campusconnect -f database/simple_schema.sql
```

## 📝 Notes

- **Flyway Migrations**: The project uses Flyway for version control
  - `V1__create_follows_table.sql` - Creates follows table
  - `V2__create_chat_tables.sql` - Creates chats and messages tables
- **Hibernate**: JPA entities will create/update tables automatically

  - Set `spring.jpa.hibernate.ddl-auto=update` in application.properties

- **Railway**: Tables are created automatically on first deployment

- **Local Development**: Use simple_schema.sql for quick setup

## 🔗 Related Files

- Entity Classes: `src/main/java/.../entities/`
- Repositories: `src/main/java/.../repository/`
- Flyway Migrations: `src/main/resources/db/migration/`
- Application Config: `src/main/resources/application.properties`

---

**Last Updated:** November 2025
