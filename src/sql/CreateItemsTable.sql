-- CREATE DATABASE trueCare;
-- GO

-- USE trueCare
-- GO

-- DROP TABLE Items;

-- USE trueCare
-- GO

-- DROP TABLE Items;

CREATE TABLE Items (
                       id INT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       price DECIMAL(10, 2) NOT NULL,
                       description VARCHAR(100) NOT NULL,
);


go

use trueCare
insert into Items (id, name, price, description)
values (1, 'CAKE', 100, 'A soft, sweet baked treat with a light, fluffy texture and various flavor options.'),
       (2, 'MUFFIN', 200, 'A rich, sweet dessert made from flour, sugar, eggs, and often frosted or layered.'),
       (3, 'ECLAIR', 300, ' A French pastry filled with cream and topped with chocolate glaze.')
go
