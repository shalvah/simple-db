package me.shalvah.simpledb;


/**
 * Models a column in a table in the database
 */
public class Column
	{
		/**
		 * The name of the column.
		 * By convention, this is automatically converted to lowercase
		 */
		private String name;

		/**
		 * The data type of the column.
		 * By convention, this is automatically converted to uppercase
		 */
		private String dataType;

		/**
		 * If the column is a primary key. By default, this is FALSE
		 */
		private boolean primaryKey;

		/**
		 * If the column value is automatically incremented. By default, this is FALSE
		 */
		private boolean autoIncrement;

		/**
		 * If the column value is to be unique.
		 * By default, this is FALSE
		 */
		private boolean unique;

		/**
		 * If the column value is allowed to be null.
		 * By default, this is TRUE
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
		 * The name of the table to which this column belongs
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
			this.name = name.toLowerCase();
			this.dataType = type.toUpperCase();
		}

		////////////////////////////////////////////////////////
		//
		//----------------------Setters----------------------//
		//
		///////////////////////////////////////////////////////

		public static Column Integer(String name)
		{
			return new Column(name, "INTEGER");
		}

		public static Column Text(String name)
		{
			return new Column(name, "TEXT");
		}

		public static Column Blob(String name)
		{
			return new Column(name, "BLOB");
		}

		public static Column Real(String name)
		{
			return new Column(name, "REAL");
		}

		public static Column Null(String name)
		{
			return new Column(name, "NULL");
		}

		////////////////////////////////////////////////////////
		//
		//----------------------Getters----------------------//
		//
		///////////////////////////////////////////////////////

		/**
		 * Sets a column as a primary key
		 *
		 * @return the column
		 */
		public Column primaryKey()
		{
			this.primaryKey = true;
			this.nullable = false;
			this.autoIncrement = true;
			return this;
		}

		/**
		 * Sets a column value as a unique index
		 *
		 * @return the column
		 */
		public Column unique()
		{
			this.unique = true;
			return this;
		}

		/**
		 * Sets a column as a foreign key
		 * Also sets the column and table which the column references (if column is a foreign key)
		 *
		 * @return the column
		 */
		public Column foreignKey(String referencesTable, String referencesColumn)
		{
			this.foreignKey = true;
			this.referencesTable = referencesTable;
			this.referencesColumn = referencesColumn;
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
		 * Sets the table to which the column belongs
		 *
		 * @return the column
		 */
		public Column belongsTo(String tableName)
		{
			this.table = tableName;
			return this;
		}

		////////////////////////////////////////////////////////

		/**
		 * Gets the name of the column
		 *
		 * @return the name of the column
		 */
		public String name()
		{
			return name;
		}

		/**
		 * Gets the type of the column
		 *
		 * @return the data type of the column
		 */
		public String type()
		{
			return this.dataType;
		}

		public boolean isForeignKey()
		{
			return foreignKey;
		}

		/**
		 * Gets the table to which the column belongs
		 *
		 * @return the name of the table of the column
		 */
		public String belongsTo()
		{
			return this.table;
		}

		/**
		 * Gets the table and column which the column refernces (if the column is a foreign key)
		 *
		 * @return string array with first elemsnt as the name of the referenced table and second the
		 * name* of the referenced column
		 */
		public String[] references()
		{
			return new String[]{this.referencesTable, this.referencesColumn};
		}

		/**
		 * Generates SQLite CREATE statement for the column.
		 * Only to be called by its containing table
		 *
		 * @return the create statement
		 */
		public String create()
		{
			String nullValue = (this.nullable) ? ("NULL") : ("NOT NULL");
			String pkValue= (this.primaryKey) ? ("PRIMARY KEY") : ("");
			String aiValue= (this.autoIncrement) ? ("AUTOINCREMENT") : ("");
			String uniqueValue=(this.unique) ? ("UNIQUE") : ("");


			String createStatement = this.name + " "
					+ this.dataType + " "
					+ nullValue + " "
					+ pkValue + " "
					+ aiValue + " "
					+ uniqueValue;

			return createStatement;
		}

		public String foreignKeyStatement()
		{
			if (foreignKey)
			{
				return "FOREIGN KEY (" + name + ") "
						+ "REFERENCES " + referencesTable
						+ "(" + referencesColumn + ")";
			} else
			{
				return "";
			}
		}

	}
