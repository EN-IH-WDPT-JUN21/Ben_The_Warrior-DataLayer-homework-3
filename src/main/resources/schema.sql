-- CREATE TABLES IF NOT EXIST
CREATE TABLE IF NOT EXISTS account (
  account_id INT NOT NULL AUTO_INCREMENT,
  city VARCHAR(255),
  country VARCHAR(255),
  employee_count INT,
  industry VARCHAR(255),
  PRIMARY KEY (`account_id`));

  CREATE TABLE IF NOT EXISTS sales_rep (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255),
  PRIMARY KEY (id));

  CREATE TABLE IF NOT EXISTS leads (
  lead_id INT NOT NULL AUTO_INCREMENT,
  company_name VARCHAR(255),
  email VARCHAR(255),
  name VARCHAR(255),
  phone_number VARCHAR(255),
  sales_rep_id INT,
  PRIMARY KEY (lead_id),
  FOREIGN KEY (sales_rep_id) REFERENCES sales_rep(id));

CREATE TABLE IF NOT EXISTS contact (
  contact_id INT NOT NULL AUTO_INCREMENT,
  company_name VARCHAR(255) NULL DEFAULT NULL,
  email VARCHAR(255) NULL DEFAULT NULL,
  name VARCHAR(255) NULL DEFAULT NULL,
  phone_number VARCHAR(255) NULL DEFAULT NULL,
  account_id INT NULL DEFAULT NULL,
  PRIMARY KEY (contact_id),
  FOREIGN KEY (account_id) REFERENCES account(account_id));

  CREATE TABLE IF NOT EXISTS opportunity (
  opportunity_id INT NOT NULL AUTO_INCREMENT,
  product VARCHAR(255),
  quantity INT,
  status VARCHAR(255),
  account_id INT,
  decision_maker_id INT,
  sales_rep_id INT,
  PRIMARY KEY (opportunity_id),
  FOREIGN KEY (sales_rep_id) REFERENCES sales_rep (id),
  FOREIGN KEY (account_id) REFERENCES account (account_id),
  FOREIGN KEY (decision_maker_id) REFERENCES contact (contact_id));
