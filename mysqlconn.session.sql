CREATE TABLE Users (
    user_id INT primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    email varchar(100) UNIQUE not null
);

INSERT INTO Users(user_id,first_name,last_name,email)
values 
(1, 'Harsh','Kumar','hdagfhkb12@gmail.com'),
(2,'Pinku','Mahto','hvgig7738@gmail.com'),
(3,'Uday','Kumar','ufhdgf57@hotmail.com');
SELECT * from users;