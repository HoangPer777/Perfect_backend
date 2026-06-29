CREATE TABLE IF NOT EXISTS chat_messages (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sender_id   UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    receiver_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content     TEXT NOT NULL,
    timestamp   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Index values to speed up query chat history
CREATE INDEX idx_chat_messages_sender_receiver ON chat_messages(sender_id, receiver_id);
