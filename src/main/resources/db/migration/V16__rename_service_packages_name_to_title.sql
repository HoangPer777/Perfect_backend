-- Rename service_packages name column to title if name column exists
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'service_packages' AND column_name = 'name'
    ) THEN
        ALTER TABLE service_packages RENAME COLUMN name TO title;
    END IF;
END $$;
