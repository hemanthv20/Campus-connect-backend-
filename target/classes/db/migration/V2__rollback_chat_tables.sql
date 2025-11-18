-- Rollback script to remove chats and messages tables
-- Run this script if you need to revert the chat feature

-- Drop messages table first (due to foreign key dependency)
DROP TABLE IF EXISTS messages CASCADE;

-- Drop chats table
DROP TABLE IF EXISTS chats CASCADE;

-- Note: This will permanently delete all chat and message data
-- Make sure to backup data before running this rollback script
