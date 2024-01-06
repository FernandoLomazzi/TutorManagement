BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Institution" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL UNIQUE,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Student" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	"surname"	TEXT NOT NULL,
	"address"	TEXT,
	"phone_number"	TEXT,
	"birthday"	TEXT,
	"social_media"	TEXT,
	"description"	TEXT,
	"education_level"	TEXT NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT),
	UNIQUE("surname","name")
);
CREATE TABLE IF NOT EXISTS "Teacher" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	"surname"	TEXT NOT NULL,
	"birthday"	TEXT,
	"description"	TEXT,
	PRIMARY KEY("id" AUTOINCREMENT),
	UNIQUE("name","surname")
);
CREATE TABLE IF NOT EXISTS "Lesson" (
	"id"	INTEGER,
	"total_hours"	REAL NOT NULL,
	"day"	TEXT NOT NULL,
	"price_per_hour"	REAL NOT NULL,
	"state"	TEXT NOT NULL CHECK("state" IN ("UNPAID", "STUDENTUNPAID", "TEACHERUNPAID", "PAID")),
	"id_subject"	INTEGER NOT NULL,
	FOREIGN KEY("id_subject") REFERENCES "Subject"("id") ON DELETE RESTRICT,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "Subject" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	"id_institution"	INTEGER NOT NULL,
	FOREIGN KEY("id_institution") REFERENCES "Institution"("id") ON DELETE RESTRICT,
	PRIMARY KEY("id" AUTOINCREMENT),
	UNIQUE("id_institution","name")
);
CREATE TABLE IF NOT EXISTS "Payment" (
	"id_student"	INTEGER,
	"id_lesson"	INTEGER,
	"paid"	NUMERIC NOT NULL COLLATE BINARY,
	"is_notified"	NUMERIC NOT NULL COLLATE BINARY,
	FOREIGN KEY("id_student") REFERENCES "Student"("id") ON DELETE RESTRICT,
	FOREIGN KEY("id_lesson") REFERENCES "Lesson"("id") ON DELETE CASCADE,
	PRIMARY KEY("id_student","id_lesson")
);
CREATE TABLE IF NOT EXISTS "Commission" (
	"id_lesson"	INTEGER,
	"id_teacher"	INTEGER,
	"price_per_hour"	REAL NOT NULL,
	"total"	REAL NOT NULL,
	"paid"	NUMERIC NOT NULL COLLATE BINARY,
	FOREIGN KEY("id_teacher") REFERENCES "Teacher"("id") ON DELETE RESTRICT,
	FOREIGN KEY("id_lesson") REFERENCES "Lesson"("id") ON DELETE CASCADE,
	PRIMARY KEY("id_lesson","id_teacher")
);
INSERT INTO "Institution" VALUES (1,'UTN');
INSERT INTO "Institution" VALUES (2,'UNL');
INSERT INTO "Student" VALUES (1,'Pedro','Picapiedras','','','2024-01-05','','','PRIMARIA');
INSERT INTO "Student" VALUES (2,'Sancho','Panza','','','2024-01-04','','','INGRESO');
INSERT INTO "Student" VALUES (3,'Martin','Fort','','','2024-01-19','','','PRIMARIA');
INSERT INTO "Student" VALUES (4,'Sergio','Maza','','','2024-01-14','','','PRIMARIA');
INSERT INTO "Teacher" VALUES (1,'Romina','Malas','2024-01-02','');
INSERT INTO "Teacher" VALUES (2,'Sofi','Kimiiy','2024-01-02','');
INSERT INTO "Lesson" VALUES (1,54.0,'2024-01-10',2.0,'UNPAID',1);
INSERT INTO "Lesson" VALUES (2,2.0,'2024-01-09',3.0,'UNPAID',2);
INSERT INTO "Lesson" VALUES (3,5.0,'2024-01-01',2.0,'STUDENTUNPAID',3);
INSERT INTO "Lesson" VALUES (4,1.0,'2024-01-22',2.0,'STUDENTUNPAID',2);
INSERT INTO "Subject" VALUES (1,'AEDD',1);
INSERT INTO "Subject" VALUES (2,'Arquitectura de computadoras',1);
INSERT INTO "Subject" VALUES (3,'AMI 2',2);
INSERT INTO "Payment" VALUES (1,1,0,0);
INSERT INTO "Payment" VALUES (2,1,0,0);
INSERT INTO "Payment" VALUES (3,1,0,0);
INSERT INTO "Payment" VALUES (4,1,0,0);
INSERT INTO "Payment" VALUES (1,2,0,0);
INSERT INTO "Payment" VALUES (3,2,0,0);
INSERT INTO "Payment" VALUES (4,2,0,0);
INSERT INTO "Payment" VALUES (1,3,0,0);
INSERT INTO "Payment" VALUES (2,3,0,0);
INSERT INTO "Payment" VALUES (3,3,0,0);
INSERT INTO "Payment" VALUES (4,3,0,0);
INSERT INTO "Payment" VALUES (1,4,0,0);
INSERT INTO "Payment" VALUES (2,4,0,0);
INSERT INTO "Payment" VALUES (3,4,0,0);
INSERT INTO "Payment" VALUES (4,4,0,0);
INSERT INTO "Commission" VALUES (1,1,2.0,108.0,0);
INSERT INTO "Commission" VALUES (2,2,3.0,6.0,0);
INSERT INTO "Commission" VALUES (3,1,1.0,5.0,1);
INSERT INTO "Commission" VALUES (4,2,1.0,1.0,1);
COMMIT;
