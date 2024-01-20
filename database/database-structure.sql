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
	"phone_number"	TEXT NOT NULL,
	"birthday"	TEXT NOT NULL,
	"social_media"	TEXT,
	"description"	TEXT,
	"education_level"	TEXT NOT NULL,
	UNIQUE("surname","name"),
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Teacher" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	"surname"	TEXT NOT NULL,
	"birthday"	TEXT NOT NULL,
	"description"	TEXT,
	UNIQUE("name","surname"),
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Lesson" (
	"id"	INTEGER,
	"total_hours"	REAL NOT NULL,
	"day"	TEXT NOT NULL,
	"price_per_hour"	REAL NOT NULL,
	"id_subject"	INTEGER NOT NULL,
	FOREIGN KEY("id_subject") REFERENCES "Subject"("id") ON DELETE RESTRICT,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "Subject" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	"id_institution"	INTEGER NOT NULL,
	UNIQUE("id_institution","name"),
	FOREIGN KEY("id_institution") REFERENCES "Institution"("id") ON DELETE RESTRICT,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Payment" (
	"id_student"	INTEGER NOT NULL,
	"id_lesson"	INTEGER NOT NULL,
	"paid"	NUMERIC NOT NULL COLLATE BINARY,
	"is_notified" NUMERIC NOT NULL COLLATE BINARY,
	FOREIGN KEY("id_student") REFERENCES "Student"("id") ON DELETE RESTRICT,
	FOREIGN KEY("id_lesson") REFERENCES "Lesson"("id") ON DELETE CASCADE,
	PRIMARY KEY("id_student","id_lesson")
);
CREATE TABLE IF NOT EXISTS "Commission" (
	"id_lesson"	INTEGER NOT NULL,
	"id_teacher"	INTEGER NOT NULL,
	"price_per_hour"	REAL NOT NULL,
	"total"	REAL NOT NULL,
	"paid"	NUMERIC NOT NULL COLLATE BINARY,
	FOREIGN KEY("id_lesson") REFERENCES "Lesson"("id") ON DELETE CASCADE,
	FOREIGN KEY("id_teacher") REFERENCES "Teacher"("id") ON DELETE RESTRICT,
	PRIMARY KEY("id_lesson","id_teacher")
);
INSERT INTO "Teacher" VALUES (1,'Ingres','Ar','2024-01-19','');
COMMIT;
