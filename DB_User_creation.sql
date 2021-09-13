CREATE DATABASE IF NOT EXISTS datalayer;
CREATE DATABASE IF NOT EXISTS datalayer_tests;

CREATE USER IF NOT EXISTS 'Ben'@'localhost' IDENTIFIED BY 'Password-123';
GRANT ALL PRIVILEGES ON datalayer.* TO 'Ben'@'localhost';
GRANT ALL PRIVILEGES ON datalayer_tests.* TO 'Ben'@'localhost';
FLUSH PRIVILEGES;