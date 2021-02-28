INSERT INTO customer (created_at,username,name,notes,phone_number,updated_at)
VALUES
(CURRENT_TIMESTAMP,'marygarcia','Mary W Gracia',null,'917-923-0940',null),
(CURRENT_TIMESTAMP,'kenneth123','Kenneth K Payan',null,'561-652-6640',null)
;

INSERT INTO pet (name,birth_date,owner_id,notes,type,created_at,updated_at)
VALUES
('Tucker','2018-09-23',1,null,1,CURRENT_TIMESTAMP,null),
('Olive','2020-05-30',1,null,0,CURRENT_TIMESTAMP,null),
('Cooper','2016-01-25',2,null,1,CURRENT_TIMESTAMP,null)
;

INSERT INTO employee (username,name,created_at,updated_at)
VALUES
('apenner','Amaleta Penner',CURRENT_TIMESTAMP,null),
('tbeddarda','Teddy Beddard',CURRENT_TIMESTAMP,null),
('mdanielskib','Maureene Danielski',CURRENT_TIMESTAMP,null),
('charlenel','Charlene Lynch',CURRENT_TIMESTAMP,null),
('srichardson29','Sherri Richardson',CURRENT_TIMESTAMP,null),
('robertbyrd','Robert Byrd',CURRENT_TIMESTAMP,null)
;

INSERT INTO skill (skill) VALUES
(0),
(1),
(2),
(3),
(4)
;

INSERT INTO employee_skill (employee_id,skill_id)
VALUES
(1,1),
(1,2),
(1,3),
(1,4),
(2,1),
(2,2),
(3,1),
(3,2),
(3,3),
(4,1),
(4,2),
(5,1),
(5,2),
(5,3),
(6,4)
;

INSERT INTO available_day (day)
VALUES
(0),
(1),
(2),
(3),
(4),
(5),
(6)
;

INSERT INTO employee_available_day (employee_id,available_day_id)
VALUES
(1,1),
(1,2),
(1,4),
(2,5),
(2,6),
(3,1),
(3,3),
(4,1),
(4,5),
(5,6),
(5,7),
(6,6),
(6,7)
;

INSERT INTO schedule (date,created_at,updated_at)
VALUES
('2021-01-10',CURRENT_TIMESTAMP,null),
('2021-01-16',CURRENT_TIMESTAMP,null),
('2021-01-20',CURRENT_TIMESTAMP,null)
;

INSERT INTO schedule_pet (pet_id,schedule_id)
VALUES
(1,1),
(2,1),
(3,2),
(1,3)
;

INSERT INTO activity (activity)
VALUES
(0),
(1),
(2),
(3),
(4)
;

INSERT INTO schedule_activity (schedule_id, activity_id)
VALUES
(1,1),
(1,2),
(1,3),
(2,4),
(3,2)
;

INSERT INTO schedule_employee (schedule_id, employee_id)
VALUES
(1,5),
(2,6),
(3,3)
;
