-- Rollback script to remove follows table and related objects
-- Run this script if you need to revert the follows feature

-- Drop indexes first
DROP INDEX IF EXISTS idx_follows_composite;
DROP INDEX IF EXISTS idx_follows_created;
DROP INDEX IF EXISTS idx_follows_following;
DROP INDEX IF EXISTS idx_follows_follower;

-- Drop the follows table (cascade will handle any dependencies)
DROP TABLE IF EXISTS follows CASCADE;

-- Note: This will permanently delete all follow relationships
-- Make sure to backup data before running this rollback script
