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

		/**
		 * Create a column of type INTEGER
		 *
		 * @param name the column name
		 * @return the column
		 */
		public static Column Integer(String name)
		{
			return new Column(name, "INTEGER");
		}

		/**
		 * Create a column of type TEXT
		 *
		 * @param name the column name
		 * @return the column
		 */
		public static Column Text(String name)
		{
			return new Column(name, "TEXT");
		}

		/**
		 * Create a column of type BLOB
		 *
		 * @param name the column name
		 * @return the column
		 */
		public static Column Blob(String name)
		{
			return new Column(name, "BLOB");
		}

		/**
		 * Create a column of type REAL
		 *
		 * @param name the column name
		 * @return the column
		 */
		public static Column Real(String name)
		{
			return new Column(name, "REAL");
		}

		/**
		 * Create a column of type NuLL
		 *
		 * @param name the column name
		 * @return the column
		 */
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
			if(referencesColumn.equalsIgnoreCase("_id"))
			{
				this.nullable=false;
			}
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
		 * @param tableName The name of the table
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

		/**
		 * If the column is a foreign key index
		 *
		 * @return true if the column is a foreign key, false otherwise
		 */
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

		/**
		 * Generates the foreign key portion of the create statement of this column
		 *
		 * @return "FOREIGN KEY(columnName) REFERENCES tableName(columnName)" if the column is a
		 * foreign key; otherwise an empty string
		 */
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
