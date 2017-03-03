<h1  align="center" >SimpleDB</h1>

<p align="center" ><a href="https://bintray.com/shalvah/maven/simple-db/_latestVersion" ><img src="https://api.bintray.com/packages/shalvah/maven/simple-db/images/download.svg" alt="Download"></a></p>

SimpleDB is an Android library that makes working with SQLite databases reaaaally simple. The aim is to get your app up and running with a functional SQLite database within minutes (true) without you worrying about the internals, so you can focus on building a great app.

You don't need to craft complex SQL statements anymore. All you need to do now is define your schema, and SimpleDB handles the rest!

## Setup
* Step 0: Add the dependency to the `build.gradle`file of your `app` module.
```
compile 'me.shalvah.simpledb:simple-db:0.5.0'
```
* Step 1: Create a class extending the `SimpleContentProvider`. This is your app's ContentProvider. Define constants representing your provider, database, table and column names (recommended, but not required)
```
public class MySimpleContentProvider extends SimpleContentProvider
{

  public static final String DB_NAME = "mydb";

  public static final String PROVIDER_NAME = "me.shalvah.myapp.provider";

  public static final String TABLE_PRODUCERS = "producers";
  public static final String COLUMN_PRODUCERS_NAME = "name";
  public static final String COLUMN_PRODUCERS_AGE = "age";

  public static final String TABLE_ITEMS = "items";
  public static final String COLUMN_ITEMS_NAME = "name";
  public static final String COLUMN_ITEMS_TYPE = "type";
  public static final String COLUMN_ITEMS_UNITS = "price";
  public static final String COLUMN_ITEMS_PRODUCED_BY = "produced_by";

}
```
Note that there is a hidden `COLUMN_ID` inherited from `SimpleContentProvider` set to `_id`. By default, SimpleDB adds this column to each table in your database, so you do not need to define that.

* Step 2: Implement the `setup()` function in your SimpleContentProvider. This is where you define your database schema.

```
public void setup()
{
 //create columns
 Column producerName = Column.Text(COLUMN_PRODUCERS_NAME);
 Column producerAge = Column.Integer(COLUMN_PRODUCERS_AGE);

 //create table and add columns
 Table producers = new Table(TABLE_PRODUCERS, producerName, producerAge);

 Column itemName = Column.Text(COLUMN_ITEMS_NAME).unique();
 Column itemType = Column.Text(COLUMN_ITEMS_TYPE);
 Column itemPrice = Column.Integer(COLUMN_ITEMS_PRICE).notNull();
 Column itemProducedBy = Column.Integer(COLUMN_ITEMS_PRODUCED_BY).foreignKey(TABLE_PRODUCERS, COLUMN_ID);

 Table items = new Table(TABLE_ITEMS, itemName, itemType, itemPrice, itemProducedBy);

 init(PROVIDER_NAME, DB_NAME, DB_VERSION, producers, items);
}
```
Note that your `setup()` function MUST end with a call to `init(yourPoviderName, yourDbName, yourDbVersion, table1, table2, table3, ...)`.
Also, if a table has a column which is a foreign key to another, make sure you add the "independent" table first.

* Step 3: You're good to go! Use the class you created as your ContentPovider.

## Quickstart
### Creating colums
```
Column.Integer(columnName);
Column.Text(columnName);
Column.Real(columnName);
Column.Blob(columnName);
```
These methods all return Column objects, so you can chain the methods below to set additional properties

### Setting column properties
If `col` is a `Column` object:
```
col.notNull();
col.unique();
col.autoIncrement();
col.foreignKey(referencesTableName, referencesColumnName);
```
Again, these methods all return Column objects, so you can chain them to set additional properties

### Creating a Table
```
Table t=new Table(tableName, col1, col2, col3, ...);

Column[] cols=new Column[] {col1, col2, col3, ...};
Table t=new Table(tableName, cols);```
```

### CRUD
```
//insert a new item
getContentResolver().insert(TestSimpleContentProvider.contentUri(tableName), values);

//delete all items
getContentResolver().delete(MySimpleContentProvider.contentUri("tableName"), null, null);

//delete a particular item by id
getContentResolver().delete(MySimpleContentProvider.contentUri
                                (tableName + "/" + id),
                        null,
                        null);
```

### Using with a CursorLoader
Loading data:
```
private void fillData()
        {
            getLoaderManager().initLoader(0, null, this);

            sca = new SimpleCursorAdapter(this, R.layout.item_list_item, null,
                    new String[]{TestSimpleContentProvider.COLUMN_ID,
                            TestSimpleContentProvider.COLUMN_ITEM_NAME,
                            TestSimpleContentProvider.COLUMN_ITEM_TYPE,
                            TestSimpleContentProvider.COLUMN_ITEM_PRICE
                    }, new int[]
                    {R.id._id, R.id.name, R.id.type, R.id.price}, 0);
            mListView.setAdapter(sca);
        }
```

Callbacks:
```
@Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args)
        {
            String[] projection = new String[]{TestSimpleContentProvider.COLUMN_ID,
                    TestSimpleContentProvider.COLUMN_ITEM_NAME,
                    TestSimpleContentProvider.COLUMN_ITEM_TYPE,
                    TestSimpleContentProvider.COLUMN_ITEM_PRICE};
            return new CursorLoader(this, TestSimpleContentProvider.contentUri
                    (TestSimpleContentProvider.TABLE_ITEMS), projection, null,
                    null,
                    null);
        }
```

A simple test app using the library can be found [here] (https://github.com/Shalvah/dbtest).

## More complex databases
If your app's database needs are more complex, you may override other methods in the `SimpleContentProvider` class as needed (for instance, if you need to define a custom `sortOrder`. Future updates may provide a simpler interface for you to do this.)

## Features
* Foreign key support. SimpleDB also automatically sets a foreign key column to be non-null if referencing an "_id" column; otherwise, you'll have to do that yourself or risk getting an SQL error
* Content URIs: SimpleDB automatically generates these.

## Future features :)
* Schema changes: Currently, you have to manually increment the databse version if you change your schema.

## Contributing
All contributions are welcome! Starting with improvements to this README! Correcting typos and feature additions equally welcome. You could also work on one of the "Future Features" above.
If you discover an issue and don't have time to fix it, please add it in the issue tracker. Thanks!
Please see the contribution guide

Thanks for checking out SimpleDB! I hope it does make your work simpler.
Please star if you find it useful! Thanks!
