package com.app.la_newsfeed;

import java.util.ArrayList;

import com.util.*;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.support.v4.widget.DrawerLayout;
//import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsFeed extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private NewsGetter newsGetter = new NewsGetter();
	private ImportanceSetter importance = new ImportanceSetter();
	private Links link = new Links();
	private RSSReader reader = new RSSReader();
	private ArrayList<Article> derpy;
	private String url;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		link.addLink("http://rss.cnn.com/rss/cnn_topstories.rss", "CNN");
		link.addLink("http://feeds.bbci.co.uk/news/rss.xml", "BBC");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feed);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.layout);
		// Article[] derp = {reader.reader(new
		// typeLink("http://rss.cnn.com/rss/cnn_topstories.rss", "CNN"))};

		new Thread(new Runnable() {

			public synchronized void run() {
				derpy = newsGetter.getNews(5);// reader.reader(new
												// typeLink("http://rss.cnn.com/rss/cnn_topstories.rss",
												// "CNN"));
			}

		}).start();

		try {
			new Thread().sleep(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		importance.setImportance(derpy);

		for (int i = 0; i < derpy.size(); i++) {

			int redVal = derpy.get(i).getPriority();
			int hexVal = 0xFFFF0000;
			hexVal += + (255 - redVal)*Math.pow(16, 2);

			Button b1 = new Button(this);
			b1.setText(derpy.get(i).getTitle());
			b1.setTextSize(10);

			b1.getBackground().setColorFilter(hexVal, PorterDuff.Mode.MULTIPLY); // 255-redVal
			b1.setGravity(Gravity.CENTER);
			url = derpy.get(i).getLink();
			b1.setOnClickListener(new DerpyListener(this, url));
			myLinearLayout.addView(b1);
		}

	}

	/*
	 * @Override protected void onStart() { System.out.println("Hello");
	 * super.onStart(); // show dialog here }
	 */

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = "Ibis";// getString(R.string.title_section1);
			break;
		case 2:
			mTitle = "CNN"; // getString(R.string.title_section2);
			break;
		case 3:
			mTitle = "Options";// getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.news_feed, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.refresh) {
			LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.layout);
			new Thread(new Runnable() {

				public synchronized void run() {
					derpy = newsGetter.getNews(5);// reader.reader(new
													// typeLink("http://rss.cnn.com/rss/cnn_topstories.rss",
													// "CNN"));
				}

			}).start();

			try {
				new Thread().sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			importance.setImportance(derpy);

			for (int i = 0; i < derpy.size(); i++) {

				int redVal = derpy.get(i).getPriority();
				int hexVal = 0xFFFF0000;
				hexVal += + (255 - redVal)*Math.pow(16, 2);

				Button b1 = new Button(this);
				b1.setText(derpy.get(i).getTitle());
				b1.setTextSize(10);

				b1.getBackground().setColorFilter(hexVal, PorterDuff.Mode.MULTIPLY); // 255-redVal
				b1.setGravity(Gravity.CENTER);
				url = derpy.get(i).getLink();
				b1.setOnClickListener(new DerpyListener(this, url));
				myLinearLayout.addView(b1);
			}
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_news_feed,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((NewsFeed) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
