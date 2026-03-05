-- H2 Schema 定義

DROP TABLE IF EXISTS LikeList;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS Users;

CREATE TABLE Users (
    UserID   VARCHAR(20)  PRIMARY KEY,
    UserName VARCHAR(50)  NOT NULL,
    Email    VARCHAR(100) NOT NULL,
    Account  VARCHAR(50)  NOT NULL
);

CREATE TABLE Products (
    No          INT            AUTO_INCREMENT PRIMARY KEY,
    ProductName VARCHAR(100)   NOT NULL,
    Price       DECIMAL(18, 2) NOT NULL,
    FeeRate     DECIMAL(8, 6)  NOT NULL
);

CREATE TABLE LikeList (
    SN               INT            AUTO_INCREMENT PRIMARY KEY,
    UserID           VARCHAR(20)    NOT NULL,
    ProductNo        INT            NOT NULL,
    PurchaseQuantity INT            NOT NULL,
    Account          VARCHAR(50)    NOT NULL,
    TotalFee         DECIMAL(18, 2) NOT NULL,
    TotalAmount      DECIMAL(18, 2) NOT NULL,
    CONSTRAINT fk_likelist_user    FOREIGN KEY (UserID)    REFERENCES Users(UserID),
    CONSTRAINT fk_likelist_product FOREIGN KEY (ProductNo) REFERENCES Products(No)
);
