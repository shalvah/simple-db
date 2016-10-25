package me.shalvah.simpledb;

import java.util.ArrayList;
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
		 *
		 * @param tableName Name of the table
		 * @param cols      array of Columns to be created in the table
		 */
		public Table(String tableName, Column... cols)
		{
			//set table name, initialize columns
			this.name = tableName;
			columns = new ArrayList<Column>();

			boolean addId = true;
			for (Column c :
					cols)
			{
				if (c.toString().equalsIgnoreCase("_id") || c.toString().equalsIgnoreCase("id"))
				{
					addId = false;
					break;
				}
			}

			//if "_id" column doesn't exist, create it
			if (addId)
			{
				Column idColumn = new Column("_id", "integer")
						.primaryKey();
				columns.add(idColumn);
			}

			//add all other columns
			Collections.addAll(this.columns, cols);
		}

		/**
		 * Generate SQLite CREATE statement for the table.
		 * Only to be called by its containing database
		 *
		 * @return the create statement
		 */
		String create()
		{
			String colsCreateStmnt = "";
			ArrayList<String> foreignKeys = new ArrayList<String>();
			for (Iterator<Column> iterator = columns.iterator(); iterator.hasNext(); )
			{
				Column column = iterator.next();
				colsCreateStmnt += column.create();
				if (column.isForeignKey())
				{
					foreignKeys.add(column.foreignKeyStatement());
				}
				if (iterator.hasNext())
				{
					colsCreateStmnt += ", ";
				}
			}
			String fkStatement = "";

			for (Iterator<String> iterator = foreignKeys.iterator(); iterator.hasNext(); )
			{
				String stmnt = iterator.next();
				fkStatement += stmnt;
				if (iterator.hasNext())
				{
					fkStatement += ", ";
				}
			}

			if (!fkStatement.equals(""))
			{
				colsCreateStmnt+=",";
			}

			return "CREATE TABLE "
					+ name + " ("
					+ colsCreateStmnt + " "
					+ fkStatement + ");";
		}

		/**
		 * Generate SQLite DROP statement for the table.
		 * Only to be called by its containing database
		 *
		 * @return the drop statement
		 */
		String drop()
		{
			return "DROP TABLE IF EXISTS " + name + ";";
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

		public String[] getAllColumns()
		{
			ArrayList<String> names = new ArrayList<String>();
			for (Column col :
					this.columns)
			{
				names.add(col.name());
			}

			return names.toArray(new String[0]);
		}
	}
