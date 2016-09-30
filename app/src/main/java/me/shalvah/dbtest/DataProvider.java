package me.shalvah.dbtest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import me.shalvah.dbhelper.Column;
import me.shalvah.dbhelper.Database;
import me.shalvah.dbhelper.Table;


public class DataProvider extends ContentProvider
	{
		public static final String PROVIDER_NAME = "me.shalvah.dbtest.dprovider";

		public static final String BASE_PATH_STUDENTS = "students";
		public static final String URL_STUDENTS = "content://" + PROVIDER_NAME + "/" + BASE_PATH_STUDENTS;
		public static final Uri CONTENT_URI_STUDENTS = Uri.parse(URL_STUDENTS);

		public static final String BASE_PATH_COURSES = "courses";
		public static final String URL_COURSES = "content://" + PROVIDER_NAME + "/" + BASE_PATH_COURSES;
		public static final Uri CONTENT_URI_COURSES = Uri.parse(URL_COURSES);
		//for UriMatcher
		public static final int STUDENTS = 1;
		public static final int STUDENT_ID = 2;
		public static final int COURSES = 10;
		public static final int COURSE_ID = 20;
		private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		public static Database db;
		private static SQLiteDatabase sqldb;

		static
		{
			matcher.addURI(PROVIDER_NAME, BASE_PATH_STUDENTS, STUDENT_ID);
			matcher.addURI(PROVIDER_NAME, BASE_PATH_STUDENTS + "/#", STUDENT_ID);
			matcher.addURI(PROVIDER_NAME, BASE_PATH_COURSES, COURSES);
			matcher.addURI(PROVIDER_NAME, BASE_PATH_COURSES + "/#", COURSE_ID);
		}

		Table students;
		Table courses;

		public DataProvider()
		{
			//create columns
			Column studentName= new Column("name", "text");
			Column studentAge=new Column("age", "int");

			//place columns in array so we can easily add them
			Column[] studentColumns=new Column[]{studentName, studentAge};

			//create table and add columns
			students=new Table("students", studentColumns);

			Column courseTitle = new Column("title", "text");
			Column courseCode = new Column("code", "text");
			Column courseUnits = new Column("units", "int");

			//you can set additional properties
			Column courseDesc = new Column("description", "text")
					.notNull();

			Column[] courseColumns = new Column[]{courseTitle, courseCode, courseDesc};
			courses = new Table("courses", courseColumns);

			//add a column to the table
			courses.add(courseUnits);

			Table[] tables=new Table[]{students, courses};
			db=new Database(getContext(), "dbtest", tables);
		}

		@Override
		public boolean onCreate()
		{
			//all columns and tables must be created before this is run!
			sqldb=db.create();
			return (sqldb!=null);
		}

		@Nullable
		@Override
		public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
		{
			SQLiteQueryBuilder qBuild = new SQLiteQueryBuilder();

			int uriType = matcher.match(uri);
			try
			{
				checkColumns(projection, uriType);
			} catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}

			switch (uriType)
			{
				case STUDENTS:
					qBuild.setTables(db.table("students").name());
					if (sortOrder == null || sortOrder.equals(""))
					{
						//will work on this
						sortOrder = "name";
					}
					break;
				case STUDENT_ID:
					qBuild.setTables(students.name());
					qBuild.appendWhere(students.id() + "="
							+ uri.getLastPathSegment());
					break;
				case COURSES:
					qBuild.setTables(courses.name());
					break;
				case COURSE_ID:
					qBuild.setTables(courses.name());
					qBuild.appendWhere(courses.id() + "="
							+ uri.getLastPathSegment());
					break;
				default:
					throw new IllegalArgumentException("Unknown URI: " + uri);
			}

			Cursor cursor = qBuild.query(sqldb, projection, selection, selectionArgs, null, null,
					sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
			return cursor;
			}

		@Nullable
		@Override
		public String getType(Uri uri)
		{
			return null;
		}

		@Nullable
		@Override
		public Uri insert(Uri uri, ContentValues values)
		{
			int uriType = matcher.match(uri);
			long id;

			switch (uriType)
			{
				case STUDENTS:
					id = sqldb.insert(students.name(), null, values);
					getContext().getContentResolver().notifyChange(uri, null);
					return Uri.parse(BASE_PATH_STUDENTS + "/" + id);
				case COURSES:
					id = sqldb.insert(courses.name(), null, values);
					getContext().getContentResolver().notifyChange(uri, null);
					return Uri.parse(BASE_PATH_COURSES + "/" + id);
				default:
					throw new IllegalArgumentException("Unknown URI: " + uri);
			}

		}

		@Override
		public int delete(Uri uri, String selection, String[] selectionArgs)
		{
			int uriType = matcher.match(uri);
			int rowsDeleted;
			String id;

			switch (uriType)

			{
				case STUDENTS:
					rowsDeleted = sqldb.delete(students.name(), selection, selectionArgs);
					break;
				case STUDENT_ID:
					id = uri.getLastPathSegment();
					if (TextUtils.isEmpty(selection))
					{
						rowsDeleted = sqldb.delete(students.name(), students
								.id() + "="
								+ id, null);
					} else
					{
						rowsDeleted = sqldb.delete(students.name(), students
								.id() + "="
								+ id + " and " + selection, null);
					}
					break;
				case COURSES:
					rowsDeleted = sqldb.delete(courses.name(), selection, selectionArgs);
					break;
				case COURSE_ID:
					id = uri.getLastPathSegment();
					if (TextUtils.isEmpty(selection))
					{
						rowsDeleted = sqldb.delete(courses.name(), courses
								.id() + "="
								+ id, null);
					} else
					{
						rowsDeleted = sqldb.delete(courses.name(), courses
								.id() + "="
								+ id + " and " + selection, null);
					}
					break;
				default:
					throw new IllegalArgumentException("Unknown URI: " + uri);
			}
			getContext().getContentResolver().notifyChange(uri, null);
			return rowsDeleted;
		}

		@Override
		public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
		{
			int rowsUpdated;
			String id;
			int uriType = matcher.match(uri);

			switch (uriType)
			{
				case STUDENTS:
					rowsUpdated = sqldb.update(students.name(), values, selection,
							selectionArgs);
					break;
				case STUDENT_ID:
					id = uri.getLastPathSegment();
					if (TextUtils.isEmpty(selection))
					{
						rowsUpdated = sqldb.update(students.name(), values, students
								.id() + "=" + id, null);
					} else
					{
						rowsUpdated = sqldb.update(students.name(), values, students
								.id() + "=" + id + " and " + selection, selectionArgs);
					}
					break;
				case COURSES:
					rowsUpdated = sqldb.update(courses.name(), values, selection,
							selectionArgs);
					break;
				case COURSE_ID:
					id = uri.getLastPathSegment();
					if (TextUtils.isEmpty(selection))
					{
						rowsUpdated = sqldb.update(courses.name(), values, courses
								.id() + "=" + id, null);
					} else
					{
						rowsUpdated = sqldb.update(courses.name(), values, courses
								.id() + "=" + id + " and " + selection, selectionArgs);
					}
					break;
				default:
					throw new IllegalArgumentException("Unknown URI: " + uri);
			}
			getContext().getContentResolver().notifyChange(uri, null);
			return rowsUpdated;
		}


		//checs if requested columns exist in the requested table


		private void checkColumns(String[] projection, int uriType)
		{
			HashSet<String> requestedColumns;
			HashSet<String> availableColumns;

			String[] available;

			switch (uriType)
			{
				case STUDENTS:
				case STUDENT_ID:

					available = students.getAllColumns();
					break;
				case COURSES:
				case COURSE_ID:
					available = courses.getAllColumns();
					break;
				default:
					throw new IllegalArgumentException("Unknown this in projection");
			}

			if (projection != null)
			{
				requestedColumns = new HashSet<String>(Arrays.asList(projection));
				availableColumns = new HashSet<String>(Arrays.asList(available));
				if (!availableColumns.containsAll(requestedColumns))
				{
					throw new IllegalArgumentException("Unknown columns in projection");
				}
			}

		}
	}
