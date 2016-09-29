package me.shalvah.dbhelper;


import java.util.Comparator;

/**
 * This class models a column in a table in the database
 */
public class Column
	{
		/**
		 * The name of thw column.
		 * By convention, this is automatically converted to lowercase
		 */
		private String name;

		/**
		 * The data type of thw column.
		 * By convention, this is automatically converted to uppercase
		 */
		private String dataType;

		/**
		 * If the column is a primary key.By default, this is FALSE
		 */
		private boolean primaryKey;

		/**
		 * If the column value is automatically incremented.By default, this is FALSE
		 */
		private boolean autoIncrement;

		/**
		 * If the column value is to be unique.By default, this is FALSE
		 */
		private boolean unique;

		/**
		 * If the column value is allowed to be null.By default, this is TRUE
		 */
		private boolean nullable=true;

		/**
		 * If the column value is a foreign key.By default, this is FALSE
		 */
		private boolean foreignKey;

		/**
		 * The name of the table referenced by this column (if this column is a foreign key).
		 *
		 */
		private String referencesTable;

		/**
		 * The name of the column in @link referencesTable referenced by this column (if this column
		 * is a foreign key).
		 */
		private String referencesColumn;

		/**
		 * The table to which this column belongs
		 */
		private String table;


		/**
		 * Adds a new column to the schema
		 *
		 * @param name The name of the column
		 * @param type The data type of the column
		 */
		public Column(String name, String type)
		{
			this.name = name;
			this.dataType = type;
		}


		/**
		 * Sets a column as a primary key
		 *
		 * @return the column
		 */
		public Column primaryKey()
		{
			this.primaryKey = true;
			return this;
		}

		/**
		 * Sets a column as a foreign key
		 *
		 * @return the column
		 */
		public Column foreignKey()
		{
			this.foreignKey = true;
			return this;
		}

		/**
		 * Sets a column to auto increment
		 *
		 * @return the column
		 */
		public Column autoIncrement()
		{
			this.autoIncrement = true;
			return this;
		}


		/**
		 * Sets a column value as unique
		 *
		 * @return the column
		 */
		public Column unique()
		{
			this.unique = true;
			return this;
		}

		/**
		 * Sets a column as non-null
		 *
		 * @return the column
		 */
		public Column notNull()
		{
			this.nullable = false;
			return this;
		}

		/**
		 * Generates SQLite CREATE statement for the column.
		 * Only to be called by its containing table
		 *
		 * @return the create statement
		 */
		String create()
		{
			String nullValue = (this.nullable) ? ("NULL") : ("NOT NULL");
			String pkValue= (this.primaryKey) ? ("PRIMARY KEY") : ("");
			String aiValue= (this.autoIncrement) ? ("AUTOINCREMENT") : ("");
			String uniqueValue=(this.unique) ? ("UNIQUE") : ("");

			String createStatement = this.name + " "
					+ this.dataType + " "
					+ nullValue + " "
					+ pkValue + " "
					+ aiValue;

			return createStatement;
		}

		/**Get the name of the column
		 *
		 * @return the name of the column
		 */
		public String name()
		{
			return name;
		}

		/**Set the name of the column
		 *
		 * @return the column
		 */
		public Column name(String newName)
		{
			this.name=newName;
			return this;
		}

		/**
		 * Set the data type of the column
		 *
		 * @return the column
		 */
		public Column type(String newType)
		{
			this.dataType = newType;
			return this;
		}

		/**
		 * Get the type of the column
		 *
		 * @return the data type of the column
		 */
		public String type()
		{
			return this.dataType;
		}

		/**
		 * Get the table to which the column belongs
		 *
		 * @return the name of the table of the column
		 */
		public String belongsTo()
		{
			return this.table;
		}

		/**
		 * Set the table to which the column belongs
		 *
		 * @return the column
		 */
		public Column belongsTo(String tableName)
		{
			this.table = tableName;
			return this;
		}

		/**
		 * Comparison of two columns
		 */
		static class ColumnComparator implements Comparator<Column>
			{

				/**Compare two columns by their tables and names
				 *
				 * @param lhs first column
				 * @param rhs second column
				 * @return
				 */
				@Override
				public int compare(Column lhs, Column rhs)
				{
					if (lhs.belongsTo().equalsIgnoreCase(rhs.belongsTo()))
					{
						return lhs.name().compareToIgnoreCase(rhs.name());
					}
					throw new UnsupportedOperationException("Can't compare the two columns; they are in " +
							"different tables");
				}
			}
	}
