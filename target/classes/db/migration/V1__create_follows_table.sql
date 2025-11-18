-- Migration script to create follows table
-- This script can be run manually if needed, or JPA will auto-create with ddl-auto=update

CREATE TABLE IF NOT EXISTS follows (
    id BIGSERIAL PRIMARY KEY,
    follower_id INTEGER NOT NULL,
    following_id INTEGER NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints with cascade delete
    CONSTRAINT fk_follower FOREIGN KEY (follower_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_following FOREIGN KEY (following_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    
    -- Unique constraint to prevent duplicate follows
    CONSTRAINT unique_follow UNIQUE(follower_id, following_id),
    
    -- Check constraint to prevent self-follows
    CONSTRAINT check_no_self_follow CHECK (follower_id != following_id)
);

-- Create indexes for performance optimization
CREATE INDEX IF NOT EXISTS idx_follows_follower ON follows(follower_id);
CREATE INDEX IF NOT EXISTS idx_follows_following ON follows(following_id);
CREATE INDEX IF NOT EXISTS idx_follows_created ON follows(created_on);

-- Composite index for efficient mutual follow checks
CREATE INDEX IF NOT EXISTS idx_follows_composite ON follows(follower_id, following_id);

-- Comments for documentation
COMMENT ON TABLE follows IS 'Stores follower-following relationships between users';
COMMENT ON COLUMN follows.follower_id IS 'User who is following';
COMMENT ON COLUMN follows.following_id IS 'User being followed';
COMMENT ON COLUMN follows.created_on IS 'Timestamp when the follow relationship was created';
