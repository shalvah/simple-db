package me.shalvah.dbtest;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class CourseListActivity extends AppCompatActivity implements LoaderManager
		.LoaderCallbacks<Cursor>
	{
		private ListView lv;
		private SimpleCursorAdapter sca;

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_course_list);

			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);

			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null)
			{
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
			lv = (ListView) findViewById(android.R.id.list);
			fillData();
		}

		private void fillData()
		{
			getLoaderManager().initLoader(0, null, this);

			sca = new SimpleCursorAdapter(this, R.layout.course_list_item, null,
					DataProvider.db.table("courses").getAllColumns(), new int[]
					{R.id._id, R.id.code, R.id.title, R.id.description}, 0);
			lv.setAdapter(sca);

		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			int id = item.getItemId();
			switch (id)
			{
				case android.R.id.home:
					// This ID represents the Home or Up button. In the case of this
					// activity, the Up button is shown. Use NavUtils to allow users
					// to navigate up one level in the application structure. For
					// more details, see the Navigation pattern on Android Design:
					//
					// http://developer.android.com/design/patterns/navigation.html#up-vs-back
					//
					NavUtils.navigateUpFromSameTask(this);
					return true;
				case R.id.clear_all:
					getContentResolver().delete(DataProvider.CONTENT_URI_COURSES, null, null);
					return true;

			}
			return super.onOptionsItemSelected(item);
		}


		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args)
		{
			String[] projection =
					DataProvider.db.table("courses").getAllColumns();

			return new CursorLoader(this, DataProvider.CONTENT_URI_COURSES, projection, null, null,
					null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data)
		{
			sca.swapCursor(data);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader)
		{
			sca.swapCursor(null);
		}

		public boolean onCreateOptionsMenu(Menu menu)
		{

			MenuInflater mi = getMenuInflater();
			mi.inflate(R.menu.menu_list, menu);
			return true;
		}

		public void add(View view)
		{
			String text = ((EditText) findViewById(R.id.inputCourseET)).getText().toString();
			String[] data = text.split(",");
			ContentValues values = new ContentValues();
			try
			{
				values.put("title", data[0]);
				values.put("code", data[1]);
				values.put("units", data[2]);
				values.put("description", data[3]);

			} catch (Exception e)
			{
				e.printStackTrace();
			}
			getContentResolver().insert(DataProvider.CONTENT_URI_COURSES, values);
			((EditText) findViewById(R.id.inputCourseET)).setText("");
		}

		public void clear(View view)
		{
			String text = ((EditText) findViewById(R.id.inputCourseET)).getText().toString();
			if (text.equalsIgnoreCase(""))
			{
				getContentResolver().delete(DataProvider.CONTENT_URI_COURSES, null, null);
			} else
			{
				getContentResolver().delete(DataProvider.CONTENT_URI_COURSES, "id", new
						String[]{text});
			}
		}
	}