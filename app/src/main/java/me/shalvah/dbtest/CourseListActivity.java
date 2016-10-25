package me.shalvah.dbtest;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
					new String[]{TestSimpleContentProvider.COLUMN_ID,
							TestSimpleContentProvider.COLUMN_COURSES_CODE,
							TestSimpleContentProvider.COLUMN_COURSES_TITLE,
							TestSimpleContentProvider.COLUMN_COURSES_UNITS,
							TestSimpleContentProvider.COLUMN_COURSES_DESCRIPTION,
							TestSimpleContentProvider.COLUMN_COURSES_TOP_STUDENT
					}, new int[]
					{R.id._id, R.id.code, R.id.title, R.id.units, R.id.description, R.id.studentId}, 0);
			lv.setAdapter(sca);

		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args)
		{
			String[] projection = new String[]{TestSimpleContentProvider.COLUMN_ID,
					TestSimpleContentProvider.COLUMN_COURSES_CODE,
					TestSimpleContentProvider.COLUMN_COURSES_TITLE,
					TestSimpleContentProvider.COLUMN_COURSES_UNITS,
					TestSimpleContentProvider.COLUMN_COURSES_DESCRIPTION,
					TestSimpleContentProvider.COLUMN_COURSES_TOP_STUDENT};
			return new CursorLoader(this, TestSimpleContentProvider.contentUri
					(TestSimpleContentProvider.TABLE_COURSES), projection, null,
					null,
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

		public void add(View view)
		{
			String text = ((EditText) findViewById(R.id.inputCourseET)).getText().toString();
			String[] data = text.split(",");
			ContentValues values = new ContentValues();
			try
			{
				values.put(TestSimpleContentProvider.COLUMN_COURSES_CODE, data[0]);
				values.put(TestSimpleContentProvider.COLUMN_COURSES_TITLE, data[1]);
				values.put(TestSimpleContentProvider.COLUMN_COURSES_UNITS, data[2]);
				values.put(TestSimpleContentProvider.COLUMN_COURSES_DESCRIPTION, data[3]);
				values.put(TestSimpleContentProvider.COLUMN_COURSES_TOP_STUDENT, data[4]);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			getContentResolver().insert(TestSimpleContentProvider.contentUri(TestSimpleContentProvider
					.TABLE_COURSES), values);
			((EditText) findViewById(R.id.inputCourseET)).setText("");
		}

		public void clear(View view)
		{
			String text = ((EditText) findViewById(R.id.inputCourseET)).getText().toString();
			if (text.equalsIgnoreCase(""))
			{
				getContentResolver().delete(TestSimpleContentProvider.contentUri
						(TestSimpleContentProvider.TABLE_COURSES), null, null);
			} else
			{
				getContentResolver().delete(TestSimpleContentProvider.contentUri
								(TestSimpleContentProvider.TABLE_COURSES + "/" + text),
						null,
						null);
				((EditText) findViewById(R.id.inputCourseET)).setText("");
			}
		}
	}