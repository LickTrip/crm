CREATE TABLE IF NOT EXISTS contacts(
  contact_id int IDENTITY (1,1) PRIMARY KEY,
  address_id int null,
  file_id int null,
  constraint contact_address_fk FOREIGN KEY(address_id) REFERENCES addresses(address_id) ON DELETE CASCADE,
  constraint contact_file_fk FOREIGN KEY(file_id) REFERENCES files(file_id) ON DELETE CASCADE,
  first_name nvarchar(25) NOT NULL,
  surname nvarchar(25) NOT NULL,
  academic_degree nvarchar(25) NULL,
  company nvarchar(70) NULL,
  work_position nvarchar(50) NULL,
  bornDate DATE NULL
);

CREATE TABLE IF NOT EXISTS addresses(
  address_id int IDENTITY (1,1) PRIMARY KEY,
  state nvarchar(40) NOT NULL,
  city nvarchar(40) NOT NULL,
  street nvarchar(40) NOT NULL,
  street_no int NOT NULL,
  zip int NULL
);

CREATE TABLE IF NOT EXISTS files(
  file_id int IDENTITY (1,1) PRIMARY KEY,
  file_name nvarchar(40) NOT NULL,
  file_type nvarchar(40) NOT NULL,
  file_size int NULL,
  file_data int NULL,
  upload_date VARBINARY(MAX) NULL
);

CREATE TABLE IF NOT EXISTS tasks(
  task_id int IDENTITY (1,1) PRIMARY KEY,
  task_name nvarchar(30) NOT NULL,
  create_date DATE NOT NULL,
  term DATE NULL,
  note nvarchar(MAX) NULL,
  priority nvarchar(40) NULL,
  complete BIT NOT NULL
);

CREATE TABLE IF NOT EXISTS meetings(
  meeting_id int IDENTITY (1,1) PRIMARY KEY,
  meeting_name nvarchar(30) NOT NULL,
  create_date DATE NOT NULL,
  term DATE NULL,
  note nvarchar(MAX) NULL,
  place nvarchar(40) NULL,
  complete BIT NOT NULL
);

CREATE TABLE IF NOT EXISTS contact_comm(
  contact_comm_id int IDENTITY (1,1) PRIMARY KEY,
  contact_id int null,
  constraint contcom_contact_fk FOREIGN KEY(contact_id) REFERENCES contacts(contact_id) ON DELETE CASCADE,
  email_description nvarchar(50) NULL,
  email nvarchar(30) NULL,
  telephone_description nvarchar(50) NULL,
  telephone int NULL
);

CREATE TABLE IF NOT EXISTS contact_files(
  contact_file_id int IDENTITY (1,1) PRIMARY KEY,
  file_id int null,
  contact_id int null,
  constraint contfile_file_fk FOREIGN KEY(file_id) REFERENCES files(file_id) ON DELETE CASCADE,
  constraint contfile_contact_fk FOREIGN KEY(contact_id) REFERENCES contacts(contact_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS meeting_contacts(
  meeting_contact_id int IDENTITY (1,1) PRIMARY KEY,
  meeting_id int null,
  contact_id int null,
  constraint meetcont_meeting_fk FOREIGN KEY(meeting_id) REFERENCES meetings(meeting_id) ON DELETE CASCADE,
  constraint meetcont_contact_fk FOREIGN KEY(contact_id) REFERENCES contacts(contact_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS task_contacts(
  task_contact_id int IDENTITY (1,1) PRIMARY KEY,
  task_id int null,
  contact_id int null,
  constraint taskcont_task_fk FOREIGN KEY(task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
  constraint taskcont_contact_fk FOREIGN KEY(contact_id) REFERENCES contacts(contact_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS contact_notes(
  contact_notes_id int IDENTITY (1,1) PRIMARY KEY,
  contact_id int null,
  constraint contnote_contact_fk FOREIGN KEY(contact_id) REFERENCES contacts(contact_id) ON DELETE CASCADE,
  text nvarchar(MAX) NOT NULL,
  create_date DATE NOT NULL
);
