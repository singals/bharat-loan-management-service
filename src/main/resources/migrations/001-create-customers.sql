CREATE TABLE IF NOT EXISTS customers(
    id UUID PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    middle_name VARCHAR(20),
    primary_contact_no VARCHAR(13),
    alternate_contact_no VARCHAR(13),
    permanent_address TEXT,
    temporary_address TEXT,
    link_to_folder_with_docs TEXT,
    is_active BOOLEAN,
    is_blacklisted BOOLEAN
);