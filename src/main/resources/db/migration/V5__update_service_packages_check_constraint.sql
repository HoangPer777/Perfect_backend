-- Drop the old constraint if it exists
ALTER TABLE service_packages DROP CONSTRAINT IF EXISTS service_packages_package_type_check;

-- Add the new constraint with updated enum values
ALTER TABLE service_packages ADD CONSTRAINT service_packages_package_type_check 
CHECK (package_type IN ('BASIC', 'MEDIUM', 'PREMIUM', 'PRO_MAX', 'PRO', 'VIP', 'CUSTOM'));
