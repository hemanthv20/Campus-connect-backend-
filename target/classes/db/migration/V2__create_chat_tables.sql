-- Migration script to create chats and messages tables
-- This script can be run manually if needed, or JPA will auto-create with ddl-auto=update

CREATE TABLE IF NOT EXISTS chats (
    id BIGSERIAL PRIMARY KEY,
    user1_id INTEGER NOT NULL,
    user2_id INTEGER NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_message_at TIMESTAMP,
    
    -- Foreign key constraints with cascade delete
    CONSTRAINT fk_chat_user1 FOREIGN KEY (user1_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_chat_user2 FOREIGN KEY (user2_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    
    -- Check constraint to prevent users from chatting with themselves
    CONSTRAINT check_different_users CHECK (user1_id != user2_id)
);

-- Create indexes for performance optimization
CREATE INDEX IF NOT EXISTS idx_chats_user1 ON chats(user1_id);
CREATE INDEX IF NOT EXISTS idx_chats_user2 ON chats(user2_id);
CREATE INDEX IF NOT EXISTS idx_chats_last_message ON chats(last_message_at DESC NULLS LAST);

-- Create messages table
CREATE TABLE IF NOT EXISTS messages (
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    sender_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    content VARCHAR(100) NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Foreign key constraints with cascade delete
    CONSTRAINT fk_message_chat FOREIGN KEY (chat_id) 
        REFERENCES chats(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_sender FOREIGN KEY (sender_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_message_receiver FOREIGN KEY (receiver_id) 
        REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create indexes for performance optimization
CREATE INDEX IF NOT EXISTS idx_messages_chat ON messages(chat_id, created_on DESC);
CREATE INDEX IF NOT EXISTS idx_messages_receiver_unread ON messages(receiver_id, is_read) WHERE is_read = FALSE;
CREATE INDEX IF NOT EXISTS idx_messages_sender ON messages(sender_id);

-- Comments for documentation
COMMENT ON TABLE chats IS 'Stores one-on-one chat conversations between users';
COMMENT ON COLUMN chats.user1_id IS 'First participant in the chat';
COMMENT ON COLUMN chats.user2_id IS 'Second participant in the chat';
COMMENT ON COLUMN chats.last_message_at IS 'Timestamp of the most recent message for sorting';

COMMENT ON TABLE messages IS 'Stores individual messages within chats';
COMMENT ON COLUMN messages.content IS 'Message text content (max 100 characters)';
COMMENT ON COLUMN messages.is_read IS 'Whether the receiver has read the message';
