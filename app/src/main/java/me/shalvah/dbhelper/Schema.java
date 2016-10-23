package me.shalvah.dbhelper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Models the database concept while extending the SQLiteOpenHelper
 *
 * @author Shalvah Adebayo <shalvah@shalvah.me>
 */
public class Schema extends SQLiteOpenHelper
	{
		/**
		 * The database version.
		 * Automatically incremented when a new table is added or an old one dropped
		 */
		private static int version = 1;
		/**
		 * The name of the database
		 */
		private String name;
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
		 * @param dbTables array of tales to be added
		 */
		public Schema(Context context, String dbName, Table... dbTables)
		{
			super(context, dbName, null, version);
			name = dbName;
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
		 * Add a new table to the database
		 *
		 * @param dbTable The table object to be added
		 */
		public Schema add(Table dbTable)
		{
			this.tables.put(dbTable.name(), dbTable);
			this.dropStatements.add(dbTable.drop());
			Schema.version++;
			return this;
		}

		/**
		 * Remove a table from the database
		 *
		 * @param tableName The name of the table to be removed
		 */
		public Schema remove(String tableName)
		{
			this.tables.remove(tableName);
			Schema.version++;
			return this;
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

		public Table table(String tableName)
		{
			if (!this.tables.containsKey(tableName))
				throw new IllegalArgumentException("Table doesn't exist") ;
			return this.tables.get(tableName);
		}

	}
