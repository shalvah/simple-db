package me.shalvah.dbtest;

import me.shalvah.simpledb.Column;
import me.shalvah.simpledb.SimpleContentProvider;
import me.shalvah.simpledb.Table;


public class TestSimpleContentProvider extends SimpleContentProvider
	{

		public static final String DB_NAME = "dbtest";

		public static final String PROVIDER_NAME = "me.shalvah.dbtest.dprovider";

		public static final String TABLE_STUDENTS = "students";
		public static final String COLUMN_STUDENTS_NAME = "name";
		public static final String COLUMN_STUDENTS_AGE = "age";

		public static final String TABLE_COURSES = "courses";
		public static final String COLUMN_COURSES_CODE = "code";
		public static final String COLUMN_COURSES_TITLE = "title";
		public static final String COLUMN_COURSES_UNITS = "units";
		public static final String COLUMN_COURSES_DESCRIPTION = "description";
		public static final String COLUMN_COURSES_TOP_STUDENT = "top_student";
		private static final int DB_VERSION = 2;

		public void setup()
		{
			//create columns
			Column studentName = Column.Text(COLUMN_STUDENTS_NAME);
			Column studentAge = Column.Integer(COLUMN_STUDENTS_AGE);

			//create table and add columns
			Table students = new Table(TABLE_STUDENTS, studentName, studentAge);

			Column courseCode = Column.Text(COLUMN_COURSES_CODE);
			Column courseTitle = Column.Text(COLUMN_COURSES_TITLE);
			Column courseUnits = Column.Integer(COLUMN_COURSES_UNITS);

			Table courses = new Table(TABLE_COURSES, courseTitle, courseCode, courseUnits);

			//you can set additional properties
			Column courseDesc = Column.Text(COLUMN_COURSES_DESCRIPTION)
					.notNull();
			Column courseTopStudent = Column.Text(COLUMN_COURSES_TOP_STUDENT)
					.foreignKey(TABLE_STUDENTS, COLUMN_ID);

			//add a column to the table
			courses.add(courseDesc);
			courses.add(courseTopStudent);

			init(PROVIDER_NAME, DB_NAME, DB_VERSION, students, courses);
		}
	}
