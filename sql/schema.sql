CREATE TABLE Clients (
    user_id serial primary key,
    user_nic VARCHAR(200)
);

CREATE TABLE Pets(
    pet_id  serial primary key,
    client_id int NOT NULL references Clients(user_id),
    nic VARCHAR (200),
    kind_of_pet VARCHAR (200)
);

INSERT into Clients VALUES (0, 'Vasyal Hotsuliak');

INSERT into Pets VALUES (0, 0, 'Nic', 'Pet');

SELECT * FROM Clients AS clients where first_name = 'Vasyal';

SELECT * FROM Pets AS pets where nic = 'Pet';

UPDATE Clients SET first_name = 'Vasyl' WHERE first_name = 'Vasyal';

UPDATE Pets SET nic = 'Bars' WHERE nic = 'Pet';

SELECT first_name, last_name, phone_number, nic AS pet FROM Clients JOIN Pets on Clients.user_id = Pets.client_id;

DELETE FROM Pets WHERE nic = 'Bars'

DELETE FROM Clients WHERE Clients.user_id = 0;

select * from clients join pets on client_id = user_id