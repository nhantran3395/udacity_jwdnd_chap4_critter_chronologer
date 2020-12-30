INSERT INTO customer(created_at,username,name,notes,phone_number,updated_at)
VALUES
(GETDATE(),'marygarcia','Mary W Gracia',null,'917-923-0940',null),
(GETDATE(),'kenneth123','Kenneth K Payan',null,'561-652-6640',null)
;

INSERT INTO pet(name,birthday,owner_id,notes,type,created_at,updated_at)
VALUES
('Tucker','2018-09-23',1,null,1,GETDATE(),null),
('Olive','2020-05-30',1,null,0,GETDATE(),null),
('Cooper','2016-01-25',2,null,1,GETDATE(),null)
;