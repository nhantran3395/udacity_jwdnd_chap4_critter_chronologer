INSERT INTO customer(id,created_at,name,notes,phone_number,updated_at)
VALUES
(1,GETDATE(),'Mary W Gracia',null,'917-923-0940',null),
(2,GETDATE(),'Kenneth K Payan',null,'561-652-6640',null)
;

INSERT INTO pet(id,name,birthday,owner_id,notes,type,created_at,updated_at)
VALUES
(1,'Tucker','2018-09-23',1,null,1,GETDATE(),null),
(2,'Olive','2020-05-30',1,null,0,GETDATE(),null),
(3,'Cooper','2016-01-25',2,null,1,GETDATE(),null)
;