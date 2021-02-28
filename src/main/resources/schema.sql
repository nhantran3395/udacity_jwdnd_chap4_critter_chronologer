DROP TABLE IF EXISTS employee_available_day;
DROP TABLE IF EXISTS schedule_employee;
DROP TABLE IF EXISTS schedule_pet;
DROP TABLE IF EXISTS employee_skill;
DROP TABLE IF EXISTS schedule_activity;
DROP TABLE IF EXISTS available_day;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS skill;
DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS pet;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS employee;

CREATE TABLE employee (
  id bigint PRIMARY KEY AUTO_INCREMENT,
  username varchar(255) UNIQUE NOT NULL,
  name nvarchar(255) NOT NULL,
  created_at datetime NOT NULL,
  updated_at datetime NULL
);

CREATE TABLE skill (
  id bigint PRIMARY KEY AUTO_INCREMENT,
  skill int UNIQUE NOT NULL
);

CREATE TABLE available_day (
  id bigint PRIMARY KEY AUTO_INCREMENT,
  day int NOT NULL
);

CREATE TABLE schedule (
  id bigint PRIMARY KEY AUTO_INCREMENT,
  date date NOT NULL,
  created_at datetime NOT NULL,
  updated_at datetime NULL
);

CREATE TABLE pet (
  id bigint PRIMARY KEY AUTO_INCREMENT,
  name nvarchar(255) NOT NULL,
  birth_date date NOT NULL,
  owner_id bigint,
  notes nvarchar(255) NULL,
  type int NOT NULL,
  created_at datetime NOT NULL,
  updated_at datetime NULL
);

CREATE TABLE customer (
  id bigint PRIMARY KEY AUTO_INCREMENT,
  username varchar(255) UNIQUE NOT NULL,
  name nvarchar(255) NOT NULL,
  phone_number nvarchar(255) NOT NULL,
  notes nvarchar(255) NULL,
  created_at datetime NOT NULL,
  updated_at datetime NULL
);

CREATE TABLE activity (
  id bigint PRIMARY KEY AUTO_INCREMENT,
  activity int UNIQUE NOT NULL
);

CREATE TABLE employee_skill (
  employee_id bigint,
  skill_id bigint,
  PRIMARY KEY (employee_id, skill_id)
);

CREATE TABLE employee_available_day (
  employee_id bigint,
  available_day_id bigint,
  PRIMARY KEY (employee_id, available_day_id)
);

CREATE TABLE schedule_employee (
  employee_id bigint,
  schedule_id bigint,
  PRIMARY KEY (employee_id, schedule_id)
);

CREATE TABLE schedule_pet (
  pet_id bigint,
  schedule_id bigint,
  PRIMARY KEY (pet_id, schedule_id)
);

CREATE TABLE schedule_activity (
  schedule_id bigint,
  activity_id bigint,
  PRIMARY KEY (schedule_id, activity_id)
);

ALTER TABLE pet ADD FOREIGN KEY (owner_id) REFERENCES customer (id);

ALTER TABLE employee_skill ADD FOREIGN KEY (employee_id) REFERENCES employee (id);

ALTER TABLE employee_skill ADD FOREIGN KEY (skill_id) REFERENCES skill (id);

ALTER TABLE employee_available_day ADD FOREIGN KEY (employee_id) REFERENCES employee (id);

ALTER TABLE employee_available_day ADD FOREIGN KEY (available_day_id) REFERENCES available_day (id);

ALTER TABLE schedule_employee ADD FOREIGN KEY (employee_id) REFERENCES employee (id);

ALTER TABLE schedule_employee ADD FOREIGN KEY (schedule_id) REFERENCES schedule (id);

ALTER TABLE schedule_pet ADD FOREIGN KEY (pet_id) REFERENCES pet (id);

ALTER TABLE schedule_pet ADD FOREIGN KEY (schedule_id) REFERENCES schedule (id);

ALTER TABLE schedule_activity ADD FOREIGN KEY (schedule_id) REFERENCES schedule (id);

ALTER TABLE schedule_activity ADD FOREIGN KEY (activity_id) REFERENCES activity (id);

