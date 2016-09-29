package me.shalvah.dbhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * Models a table in the database
 */
public class Table
	{
		/**
		 * The name of the table
		 */
		private String name;

		/**
		 * The columns in the table
		 */
		private ArrayList<Column> columns;

		/**
		 * Create a new table and add columns to it
		 * Automatically adds "_id" column if it was not included
		 * If you do not want this to happen, use the other constructor and pass a third parameter,
		 * false
		 *
		 * @param tableName Name of the table
		 * @param cols array of Columns to be created in the table
		 */
		public Table(String tableName, Column[] cols)
		{
			//set table name, initialize columns
			this.name=tableName;
			columns=new ArrayList<Column>();

			//if "_id" column doesn't exist, create it
			Column idColumn = new Column("_id", "int")
					.primaryKey()
					.autoIncrement()
					.notNull();
			Arrays.sort(cols, new Column.ColumnComparator());
			if (Arrays.binarySearch(cols, idColumn, new Column.ColumnComparator()) < 0)
			{
				columns.add(idColumn);
			}

			//then add all other columns
			Collections.addAll(this.columns, cols);
		}

		/**
		 * Create a new table and add columns to it
		 * Adds "_id" column if addID is true
		 *
		 * @param tableName Name of the table
		 * @param cols      array of Columns to be created in the table
		 * @param addID Whether or not to add an "_id" column
		 */
		public Table(String tableName, Column[] cols, boolean addID)
		{
			this.name = tableName;
			columns = new ArrayList<Column>();
			if (addID)
			{
				Column idColumn = new Column("_id", "int")
						.primaryKey()
						.autoIncrement()
						.notNull();
				columns.add(idColumn);
			}
			Collections.addAll(this.columns, cols);

			for (Column c :
					columns)
			{
				c.belongsTo(this.name);
			}
		}

		/**
		 * Create a new table with no columns
		 *
		 * @param tableName Name of the table
		 */
		public Table(String tableName)
		{
			this.name = tableName;
			columns = new ArrayList<Column>();
		}

		/**
		 * Generate SQLite CREATE statement for the table.
		 * Only to be called by its containing database
		 *
		 * @return the create statement
		 */
		String create()
		{
			String colsCreateStmnt= "";
			for (Iterator<Column> iterator = columns.iterator(); iterator.hasNext(); )
			{
				Column column = iterator.next();
				colsCreateStmnt += column.create();
				if (iterator.hasNext())
				{
					colsCreateStmnt += ", ";
				}
			}

			String createStatement="CREATE TABLE "
					+ this.name + " ("
					+ colsCreateStmnt + ");";
			return createStatement;
		}

		/**
		 * Generate SQLite DROP statement for the table.
		 * Only to be called by its containing database
		 *
		 * @return the drop statement
		 */
		String drop()
		{
			return "DROP TABLE IF EXISTS " + this.name + ";";
		}

		/**
		 * Add a new column to the table
		 *
		 * @return the table
		 */
		public Table add(Column col)
		{
			this.columns.add(col);
			for (Column c :
					columns)
			{
				c.belongsTo(this.name);
			}
			return this;
		}

		/**
		 * Get the name of the table
		 *
		 * @return the name of the table
		 */
		public String name()
		{
			return name;
		}

		public String id()
		{
			for (Column c :
					columns)
			{
				if (c.name().equalsIgnoreCase("_id"))
				{
					return c.name();
				}
			}
			throw new IllegalArgumentException("No _id column in table");
		}

		public String[] getAllColumns()
		{
			ArrayList<String> names=new ArrayList<String>();
			for (Column col :
					this.columns)
			{
				names.add(col.name());
			}
			return (String[]) names.toArray();
		}
	}
