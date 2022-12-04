-- Cleanup (not anymore)
-- DROP DATABASE IF EXISTS petstoreDB;
-- CREATE DATABASE petstoreDB;
-- USE petstoreDB;

-- Create
CREATE TABLE customer (
    id VARCHAR(10), 
    PRIMARY KEY(id),
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    telephone VARCHAR(10), 
    street1 VARCHAR(50), 
    street2 VARCHAR(50), 
    city VARCHAR(25), 
    state VARCHAR(25),
    zipcode VARCHAR(10),
    country VARCHAR(25)
) ENGINE=INNODB;

CREATE TABLE category(
    id VARCHAR(10), 
    PRIMARY KEY(id), 
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL
) ENGINE=INNODB ;

CREATE TABLE product(
    id VARCHAR(10),
    PRIMARY KEY(id), 
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL, 
    category_fk VARCHAR(10) NOT NULL, 
    INDEX category_fk_ind (category_fk), 
    FOREIGN KEY (category_fk) REFERENCES category(id) ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE item(
    id VARCHAR(10), 
    PRIMARY KEY(id), 
    name VARCHAR(50) NOT NULL,
    unit_cost DOUBLE NOT NULL, 
    product_fk VARCHAR(10) NOT NULL, 
    INDEX product_fk_ind (product_fk), 
    FOREIGN KEY (product_fk) REFERENCES product(id) ON DELETE CASCADE
) ENGINE=INNODB;

-- Alter
alter table item add column image_path varchar(255);

-- added for tp 05

-- note : mysql n'a pas de séquences !

create table sequence_id (
    table_name VARCHAR(10),
    primary key(table_name),
    max_id integer
) ENGINE=INNODB;
