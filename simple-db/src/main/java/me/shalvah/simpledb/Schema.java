package me.shalvah.simpledb;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Models the database concept while extending the SQLiteOpenHelper
 *
 * @author Shalvah Adebayo shalvah@shalvah.me
 */
public class Schema extends SQLiteOpenHelper
	{
		/**
		 * The tables in the database
		 */
		private LinkedHashMap<String, Table> tables;

		/**
		 * List of drop statements
		 */
		private ArrayList<String> dropStatements;

		/**
		 * Create a new database and add tables
		 *
		 * @param context The application context
		 * @param dbName  The database name
		 * @param dbVersion The database version
		 * @param dbTables array of tales to be added
		 */
		public Schema(Context context, String dbName, int dbVersion, Table... dbTables)
		{
			super(context, dbName, null, dbVersion);
			tables = new LinkedHashMap<String, Table>();
			dropStatements = new ArrayList<>();
			for (Table t :
					dbTables)
			{
				this.tables.put(t.name(), t);
				this.dropStatements.add(t.drop());
			}
		}

		/**
		 * Create an SQLite Database
		 * Should be called only after initializing tables and columns
		 *
		 * @return the SQLite database
		 */
		public SQLiteDatabase create()
		{
			return this.getWritableDatabase();
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			for (Table table : tables.values())
			{
				db.execSQL(table.create());
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			for (String dropStatement : dropStatements)
			{
				db.execSQL(dropStatement);
			}
			onCreate(db);
		}

		/**
		 * Gets a table, given a name
		 *
		 * @param tableName the name of the table
		 * @return a Table object of that table name if it exists in the schema; otherwise an
		 * Exception is thrown
		 */
		public Table table(String tableName)
		{
			if (!this.tables.containsKey(tableName))
				throw new IllegalArgumentException("Table doesn't exist") ;
			return this.tables.get(tableName);
		}

	}
