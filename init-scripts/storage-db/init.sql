DROP TABLE IF EXISTS storages CASCADE;

DROP SEQUENCE IF EXISTS storages_seq;

CREATE SEQUENCE IF NOT EXISTS storages_seq
START WITH 1
INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS storages (
	id INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('storages_seq'),
    storage_type VARCHAR NOT NULL,
    bucket VARCHAR NOT NULL,
    path VARCHAR NOT NULL
);

INSERT INTO storages (storage_type, bucket, path)
VALUES
('STAGING', 'ds-resources-us-east-1', 'staging/'),
('PERMANENT', 'ds-resources-us-east-1', 'permanent/');