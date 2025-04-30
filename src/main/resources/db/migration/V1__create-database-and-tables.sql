-- Migración 1: Crear la base de datos solo si no existe
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'airlinemanagementsystem')
BEGIN
    CREATE DATABASE airlinemanagementsystem;
END;

-- Migración 2: Usar la base de datos
USE airlinemanagementsystem;

-- Migración 3: Crear tabla 'cancel'
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name = 'cancel' AND xtype = 'U')
BEGIN
    CREATE TABLE cancel (
        PNR VARCHAR(200) NULL,
        name VARCHAR(45) NULL,
        cancelno VARCHAR(45) NOT NULL,
        fcode VARCHAR(45) NULL,
        date DATE NULL,
        PRIMARY KEY (cancelno)
    );
END;

-- Migración 4: Crear tabla 'flight'
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name = 'flight' AND xtype = 'U')
BEGIN
    CREATE TABLE flight (
        f_code VARCHAR(45) NOT NULL,
        f_name VARCHAR(45) NULL,
        source VARCHAR(50) NULL,
        destination VARCHAR(50) NULL,
        PRIMARY KEY (f_code)
    );
END;

-- Migración 5: Crear tabla 'passenger'
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name = 'passenger' AND xtype = 'U')
BEGIN
    CREATE TABLE passenger (
        name VARCHAR(45) NULL,
        nationality VARCHAR(45) NULL,
        phone VARCHAR(45) NULL,
        address VARCHAR(45) NULL,
        aadhar INT NOT NULL,
        gender VARCHAR(45) NULL,
        PRIMARY KEY (aadhar)
    );
END;

-- Migración 6: Crear tabla 'reservation'
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name = 'reservation' AND xtype = 'U')
BEGIN
    CREATE TABLE reservation (
        PNR VARCHAR(200) NOT NULL,
        TIC VARCHAR(200) NULL,
        aadhar INT NULL,
        name VARCHAR(45) NULL,
        nationality VARCHAR(45) NULL,
        flightname VARCHAR(45) NULL,
        flightcode VARCHAR(45) NULL,
        src VARCHAR(45) NULL,
        des VARCHAR(45) NULL,
        ddate DATE NULL,
        PRIMARY KEY (PNR),
        CONSTRAINT FK_aadhar FOREIGN KEY (aadhar) REFERENCES passenger (aadhar)
    );
END;
