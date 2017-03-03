<h1  align="center" >SimpleDB</h1>

<p align="center" ><a href="https://bintray.com/shalvah/maven/simple-db/_latestVersion" ><img src="https://api.bintray.com/packages/shalvah/maven/simple-db/images/download.svg" alt="Download"></a></p>

SimpleDB is an Android library that makes working with SQLite databases reaaaally simple. The aim is to get your app up and running with a functional SQLite database within minutes (true) without you worrying about the internals, so you can focus on building a great app.

You don't need to craft complex SQL statements anymore. All you need to do now is define your schema, and SimpleDB handles the rest!

# Setup
* Step 0: Add the dependency to your app's `build.gradle`
```
compile 'me.shalvah.simpledb:simple-db:0.5.0'
```

* Step 1: Create a class extending the `SimpleContentProvider`. This is your app's
ContentProvider. Define your provider, database, and database version in a static block:
names if you wish.
```
public class MySimpleContentProvider extends SimpleContentProvider
{

  static
  {
    DB_NAME = "mydb";
    PROVIDER_NAME = "me.shalvah.myapp.provider";
    DB_VERSION = 1;
  }

}
```

* Step 2: Implement the `setup()` function in your SimpleContentProvider. This is where you define your database schema.

```
public void setup()
{
 //create columns
 Column producerId = Column.id("_id");
 Column producerName = Column.text("name");
 Column producerAge = Column.integer("age");

 //create table and add columns
 Table producers = new Table("producers", producerName, producerAge);

 Column itemName = Column.text("name").unique();
 Column itemTypee = Column.text("type");
 Column itemPrice = Column.integer("price").notNull();
 Column itemProducedBy = Column.integer("produced_by").foreignKey("producers","id");

 Table items = new Table("courses", itemName, itemType, itemPrice, itemProducedBy);

 }
```
Note that if a table has a column which is a foreign key to another, make sure you add the "independent" table first.

* Step 3: You're good to go! Use the class you created as your ContentPovider.

# Quickstart
## Creating columns
```
Column.integer(columnName);
Column.text(columnName);
Column.real(columnName);
Column.null(columnName);
Column.blob(columnName);
```
These methods all return Column objects, so you can chain the methods below to set additional properties

## Setting column properties
If `col` is a `Column` object:
```
col.notNull();
col.primaryKey();
col.id();
col.unique();
col.autoIncrement();
col.foreignKey(referencesTableName, referencesColumnName);
```
Again, these methods all return Column objects, so you can chain them to set additional properties

## Creating a Table
```Table t=new Table(tableName, col1, col2, col3, ...);

Column[] cols=new Column[] {col1, col2, col3, ...};
Table t=new Table(tableName, cols);```
```

## CRUD
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

## Using with a CursorLoader
Loading data:
```private void fillData()
        {
            getLoaderManager().initLoader(0, null, this);

            sca = new SimpleCursorAdapter(this, R.layout.item_list_item, null,
                    new String[]{"id", "name", "type", "price"},
                    new int[] {R.id._id, R.id.name, R.id.type, R.id.price}, 0);
            mListView.setAdapter(sca);
        }
```

Callbacks:
```
@Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args)
        {
            String[] projection = new String[]{"id", "name", "type", "price"};
            return new CursorLoader(this, TestSimpleContentProvider.contentUri
                    ("items"), projection, null,
                    null,
                    null);
        }
```

A simple test app using the library can be found [here] (https://github.com/Shalvah/dbtest).

# Features
* Foreign key support. Simpledb also automatically sets an fk column to be on-null if referencing an "_id" column; otherwise, you'll have to do that yourself or risk getting an SQL error
* Content URIs: Simpledb automatically generates these. 

# Future features :)
* Migrations: Currently, you have to manually increment the database version if you change your
schema, and you lose all data (db is recreated)

# Changelog (0.6.0)
- Changed method namesto match Java convention
- Removed auto adding of id column to prevent confusion
- Removed requirement to call init() in setup()

# Contributing
All contributions are welcome! Starting with improvements to this doc! Correcting typos and feature additions equally welcome. You could also work on one of the "Future Features" above. 
If you discover an issue and don't have time to fix it, please add it in the issue tracker. Thanks!
Please see the contribution guide

Thanks for checking out Simpledb! I hope it does make your work simpler.
Please star if you find it useful! Thanks!

