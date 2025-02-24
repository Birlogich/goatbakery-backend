-- CREATE DATABASE trueCare;
-- GO

-- USE trueCare
-- GO

-- DROP TABLE Orders;

-- USE trueCare
-- GO



CREATE TABLE Orders (
                        id INT PRIMARY KEY IDENTITY,
                        customer INT NOT NULL,
                        createDate DATETIME NOT NULL
);


CREATE TABLE Order_Items (
                             id INT PRIMARY KEY IDENTITY,
                             order_id INT NOT NULL,
                             item_id INT NOT NULL,
                             quantity INT NOT NULL,
                             CONSTRAINT FK_Order FOREIGN KEY (order_id) REFERENCES Orders(id),
);

go

use trueCare
go
