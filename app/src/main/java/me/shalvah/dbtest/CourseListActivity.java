package me.shalvah.dbtest;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class CourseListActivity extends AppCompatActivity implements LoaderManager
		.LoaderCallbacks<Cursor>, NavigationView.OnNavigationItemSelectedListener
	{
		private ListView lv;
		private SimpleCursorAdapter sca;

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			// TODO: 05/09/2016 This layout activity_course_list should not be using assignment's view
			setContentView(R.layout.activity_course_list);

			setupActionBar();
			//populate the list
			lv = (ListView) findViewById(android.R.id.list);
			fillData();
		}

		private void setupActionBar()
		{
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			assert toolbar != null;
			toolbar.setTitle(getTitle());

			// Show the Up button in the action bar.
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null)
			{
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
					this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
			assert drawer != null;
			//noinspection deprecation,deprecation,deprecation,deprecation,deprecation
			drawer.setDrawerListener(toggle);
			toggle.syncState();

			NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
			assert navigationView != null;
			navigationView.setNavigationItemSelectedListener(this);
			navigationView.setCheckedItem(R.id.nav_courses);
		}

		private void fillData()
		{
			getLoaderManager().initLoader(0, null, this);

			sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null,
					new String[]{DatabaseTables.COURSES_COLUMN_CODE, DatabaseTables.COURSES_COLUMN_TITLE}, new int[]
					{android.R.id.text1, android.R.id.text2}, 0);
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
					AlertDialog dialog = confirmDelete(DataProvider.CONTENT_URI_ASSIGNMENTS, null, null);
					dialog.show();
					return true;

			}
			return super.onOptionsItemSelected(item);
		}


		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args)
		{
			String[] projection = {DatabaseTables.COURSES_COLUMN_ID, DatabaseTables.COURSES_COLUMN_CODE,
					DatabaseTables.COURSES_COLUMN_TITLE, DatabaseTables.COURSES_COLUMN_UNITS};

			return new CursorLoader(this, DataProvider.CONTENT_URI_COURSES, projection, null, null,
					null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data)
		{
			sca.swapCursor(data);
			if (data.getCount() == 0)
				emptyView.setVisibility(View.VISIBLE); //// TODO: 06/06/2016 make sure this works IMMEDIATELY AFTER DELETION
			else
				emptyView.setVisibility(View.GONE);

		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader)
		{
			sca.swapCursor(null);
		}

		@Override
		public void onBackPressed()
		{
			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			assert drawer != null;
			if (drawer.isDrawerOpen(GravityCompat.START))
			{
				drawer.closeDrawer(GravityCompat.START);
			} else
			{
				NavUtils.navigateUpFromSameTask(this);
			}
		}

		public boolean onCreateOptionsMenu(Menu menu)
		{

			MenuInflater mi = getMenuInflater();
			mi.inflate(R.menu.menu_asignment_list, menu);
			return true;
		}

		@SuppressWarnings("StatementWithEmptyBody")
		@Override
		public boolean onNavigationItemSelected(MenuItem item)
		{
			// Handle navigation view item clicks here.
			int id = item.getItemId();

			switch (id)
			{
				case R.id.nav_timetable:
					Intent i = new Intent(this, TimetableActivity.class);
					startActivity(i);
					break;
				case R.id.nav_projects:
				{
					Intent alIntent = new Intent(this, AssignmentListActivity.class);
					startActivity(alIntent);

					break;
				}
				case R.id.nav_home:
				{
					Intent alIntent = new Intent(this, HomeActivity.class);
					startActivity(alIntent);

					break;
				}

				case R.id.nav_reader:
				{
					Intent alIntent = new Intent(this, StoryListActivity.class);
					startActivity(alIntent);

					break;
				}
				case R.id.nav_about:
					Intent boutIntent = new Intent(this, AboutActivity.class);
					startActivity(boutIntent);

					break;
			}

			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			assert drawer != null;
			drawer.closeDrawer(GravityCompat.START);
			return true;
		}

		@Override
		protected void onResume()
		{
			super.onResume();
			NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
			assert navigationView != null;
			navigationView.setCheckedItem(R.id.nav_courses);

		}

		public AlertDialog confirmDelete(final Uri uri, final String where, final String[]
				selectionArgs)
		{
			AlertDialog deleteConfirmation = new AlertDialog.Builder(getBaseContext())
					.setMessage("Are you sure you want to delete all assignments?")
					.setTitle("All Assignments")
					.setPositiveButton("Delete", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								getContentResolver().delete(uri, where, selectionArgs);
							}
						})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
							}
						})
					.create();
			return deleteConfirmation;
		}
	}
