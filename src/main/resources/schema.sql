DROP TABLE IF EXISTS EmployeeAvailability;
DROP TABLE IF EXISTS EmployeeSchedule;
DROP TABLE IF EXISTS EmployeeSkill;
DROP TABLE IF EXISTS ScheduleActivity;
DROP TABLE IF EXISTS Availability;
DROP TABLE IF EXISTS Schedule;
DROP TABLE IF EXISTS Skill;
DROP TABLE IF EXISTS Activity;
DROP TABLE IF EXISTS Schedule;
DROP TABLE IF EXISTS Pet;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Employee;

CREATE TABLE [Employee] (
  [id] bigint PRIMARY KEY IDENTITY(1, 1),
  [username] varchar(20) UNIQUE NOT NULL,
  [name] nvarchar(255) NOT NULL,
  [created_at] datetime NOT NULL,
  [updated_at] datetime NULL
);

CREATE TABLE [Skill] (
  [id] bigint PRIMARY KEY IDENTITY(1, 1),
  [skill] int NOT NULL,
  [created_at] datetime NOT NULL,
  [updated_at] datetime NULL
);

CREATE TABLE [Availability] (
  [id] bigint PRIMARY KEY IDENTITY(1, 1),
  [day_of_week] nvarchar(255) NOT NULL
);

CREATE TABLE [Schedule] (
  [id] bigint PRIMARY KEY IDENTITY(1, 1),
  [pet_id] bigint,
  [date] datetime NOT NULL,
  [customer_id] bigint,
  [created_at] datetime NOT NULL,
  [updated_at] datetime NULL
);

CREATE TABLE [Pet] (
  [id] bigint PRIMARY KEY IDENTITY(1, 1),
  [name] nvarchar(255) NOT NULL,
  [birthday] date NOT NULL,
  [owner_id] bigint,
  [notes] nvarchar(255) NULL,
  [type] int NOT NULL,
  [created_at] datetime NOT NULL,
  [updated_at] datetime NULL
);

CREATE TABLE [Customer] (
  [id] bigint PRIMARY KEY IDENTITY(1, 1),
  [username] varchar(20) UNIQUE NOT NULL,
  [name] nvarchar(255) NOT NULL,
  [phone_number] nvarchar(255) NOT NULL,
  [notes] nvarchar(255) NULL,
  [created_at] datetime NOT NULL,
  [updated_at] datetime NULL
);

CREATE TABLE [Activity] (
  [id] bigint PRIMARY KEY IDENTITY(1, 1),
  [activity] int NOT NULL,
  [created_at] datetime NOT NULL,
  [updated_at] datetime NULL
);

CREATE TABLE [EmployeeSkill] (
  [employee_id] bigint,
  [skill_id] bigint,
  PRIMARY KEY ([employee_id], [skill_id])
);

CREATE TABLE [EmployeeAvailability] (
  [employee_id] bigint,
  [availability_id] bigint,
  PRIMARY KEY ([employee_id], [availability_id])
);

CREATE TABLE [EmployeeSchedule] (
  [employee_id] bigint,
  [schedule_id] bigint,
  PRIMARY KEY ([employee_id], [schedule_id])
);

CREATE TABLE [ScheduleActivity] (
  [schedule_id] bigint,
  [activity_id] bigint,
  PRIMARY KEY ([schedule_id], [activity_id])
);

ALTER TABLE [Schedule] ADD FOREIGN KEY ([pet_id]) REFERENCES [Pet] ([id]);

ALTER TABLE [Schedule] ADD FOREIGN KEY ([customer_id]) REFERENCES [Customer] ([id]);

ALTER TABLE [Pet] ADD FOREIGN KEY ([owner_id]) REFERENCES [Customer] ([id]);

ALTER TABLE [EmployeeSkill] ADD FOREIGN KEY ([employee_id]) REFERENCES [Employee] ([id]);

ALTER TABLE [EmployeeSkill] ADD FOREIGN KEY ([skill_id]) REFERENCES [Skill] ([id]);

ALTER TABLE [EmployeeAvailability] ADD FOREIGN KEY ([employee_id]) REFERENCES [Employee] ([id]);

ALTER TABLE [EmployeeAvailability] ADD FOREIGN KEY ([availability_id]) REFERENCES [Availability] ([id]);

ALTER TABLE [EmployeeSchedule] ADD FOREIGN KEY ([employee_id]) REFERENCES [Employee] ([id]);

ALTER TABLE [EmployeeSchedule] ADD FOREIGN KEY ([schedule_id]) REFERENCES [Schedule] ([id]);

ALTER TABLE [ScheduleActivity] ADD FOREIGN KEY ([schedule_id]) REFERENCES [Schedule] ([id]);

ALTER TABLE [ScheduleActivity] ADD FOREIGN KEY ([activity_id]) REFERENCES [Activity] ([id]);

