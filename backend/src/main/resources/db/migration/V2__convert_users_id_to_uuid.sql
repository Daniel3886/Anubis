CREATE EXTENSION IF NOT EXISTS "pgcrypto";

ALTER TABLE users
    ADD COLUMN id_new UUID;

UPDATE users
SET id_new = gen_random_uuid();


ALTER TABLE users
DROP CONSTRAINT users_pkey;

ALTER TABLE users
DROP COLUMN id;

ALTER TABLE users
    RENAME COLUMN id_new TO id;

ALTER TABLE users
    ALTER COLUMN id SET NOT NULL;

ALTER TABLE users
    ADD CONSTRAINT pk_users PRIMARY KEY (id);