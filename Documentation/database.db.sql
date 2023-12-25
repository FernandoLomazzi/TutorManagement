BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Teacher" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	"surname"	TEXT NOT NULL,
	"birthday"	NUMERIC,
	"description"	TEXT,
	UNIQUE("name","surname"),
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Student" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	"surname"	TEXT NOT NULL,
	"address"	TEXT,
	"phone_number"	TEXT,
	"birthday"	TEXT,
	"social_media"	NUMERIC,
	"description"	TEXT,
	UNIQUE("surname","name"),
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Institution" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL UNIQUE,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Subject" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL UNIQUE,
	"id_institution"	INTEGER NOT NULL,
	FOREIGN KEY("id_institution") REFERENCES "Institution"("id"),
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Class" (
	"id"	INTEGER,
	"total_hours"	REAL NOT NULL,
	"day"	NUMERIC NOT NULL,
	"price_per_hour"	REAL NOT NULL,
	"state"	TEXT NOT NULL CHECK("state" IN ("UNPAID", "STUDENTUNPAID", "TEACHERUNPAID", "PAID")),
	"description"	TEXT,
	"id_subject"	INTEGER NOT NULL,
	FOREIGN KEY("id_subject") REFERENCES "Subject"("id"),
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Commission" (
	"id_class"	INTEGER,
	"id_teacher"	INTEGER,
	"price_per_hour"	REAL NOT NULL,
	"total"	REAL NOT NULL,
	"total_paid"	REAL NOT NULL,
	FOREIGN KEY("id_teacher") REFERENCES "Teacher"("id"),
	FOREIGN KEY("id_class") REFERENCES "Class"("id"),
	PRIMARY KEY("id_class","id_teacher")
);
CREATE TABLE IF NOT EXISTS "Payment" (
	"id_student"	INTEGER,
	"id_class"	INTEGER,
	"total_paid"	REAL NOT NULL,
	FOREIGN KEY("id_student") REFERENCES "Student"("id"),
	PRIMARY KEY("id_student","id_class")
);
COMMIT;
