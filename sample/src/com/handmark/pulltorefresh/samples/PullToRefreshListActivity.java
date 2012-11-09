/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.samples;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

public final class PullToRefreshListActivity extends ListActivity {

	static final int MENU_MANUAL_REFRESH = 0;
	static final int MENU_DISABLE_SCROLL = 1;
	static final int MENU_SET_MODE = 2;

	private LinkedList<String> mListItems;
	private PullToRefreshListView mPullRefreshListView;
	private ArrayAdapter<String> mAdapter;
    private Random mRandom;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ptr_list);
        mRandom = new Random(1);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);

		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				mPullRefreshListView.setLastUpdatedLabel(DateUtils.formatDateTime(getApplicationContext(),
//						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
//								| DateUtils.FORMAT_ABBREV_ALL));

				// Do work to refresh the list here.
				new GetDataTask(false).execute();
			}
		});


        mPullRefreshListView.setOnAutoLoadMoreListener(new PullToRefreshBase.OnAutoLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPullRefreshListView.setLoadMore();
                new GetDataTask(true).execute();
                Toast.makeText(PullToRefreshListActivity.this, "On Last One", Toast.LENGTH_SHORT).show();
            }
        });

		ListView actualListView = mPullRefreshListView.getRefreshableView();

		mListItems = new LinkedList<String>();
		mListItems.addAll(Arrays.asList(mStrings));

		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);

		/**
		 * Add Sound Event Listener
		 */
//		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(this);
//		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
//		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
//		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
//		mPullRefreshListView.setOnPullEventListener(soundListener);

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		actualListView.setAdapter(mAdapter);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        private boolean loadmore = false;

        private GetDataTask(boolean loadmore) {
            this.loadmore = loadmore;
        }

        @Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {

            int fp = mPullRefreshListView.getRefreshableView().getFirstVisiblePosition();
            int p = 0;
            if (!loadmore) {
                int r = mRandom.nextInt(10);
                for (int i=0; i < r; i ++) {
                    mListItems.addFirst("Added after refresh... " + mRandom.nextInt());
                }

                p = fp + r - 1;

                // Call onRefreshComplete when the list has been refreshed.
                mPullRefreshListView.onRefreshComplete();
            } else {
                int r = mRandom.nextInt(10);
                r= 0;
                for (int i=0; i < r; i ++) {
                    mListItems.addLast("added after load more " + mRandom.nextInt());
                }
                Log.d("PullToRefresh", "none");
//                if (r == 0) {
                    mPullRefreshListView.onNoLoadMore("没有更多");
//                }
//                mListItems.addLast("added after load more " + mRandom.nextInt());
//                mPullRefreshListView.onLoadMoreComplete();
                p = fp + 1;
            }

            mAdapter.notifyDataSetChanged();
            mPullRefreshListView.getRefreshableView().setSelection(p);
			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_MANUAL_REFRESH, 0, "Manual Refresh");
		menu.add(0, MENU_DISABLE_SCROLL, 1,
				mPullRefreshListView.isDisableScrollingWhileRefreshing() ? "Enable Scrolling while Refreshing"
						: "Disable Scrolling while Refreshing");
		menu.add(0, MENU_SET_MODE, 0, mPullRefreshListView.getMode() == Mode.BOTH ? "Change to MODE_PULL_DOWN"
				: "Change to MODE_PULL_BOTH");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem disableItem = menu.findItem(MENU_DISABLE_SCROLL);
		disableItem
				.setTitle(mPullRefreshListView.isDisableScrollingWhileRefreshing() ? "Enable Scrolling while Refreshing"
						: "Disable Scrolling while Refreshing");

		MenuItem setModeItem = menu.findItem(MENU_SET_MODE);
		setModeItem.setTitle(mPullRefreshListView.getMode() == Mode.BOTH ? "Change to MODE_PULL_DOWN"
				: "Change to MODE_PULL_BOTH");

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case MENU_MANUAL_REFRESH:
				new GetDataTask(false).execute();
				mPullRefreshListView.setRefreshing(false);
				break;
			case MENU_DISABLE_SCROLL:
				mPullRefreshListView.setDisableScrollingWhileRefreshing(!mPullRefreshListView
						.isDisableScrollingWhileRefreshing());
				break;
			case MENU_SET_MODE:
				mPullRefreshListView.setMode(mPullRefreshListView.getMode() == Mode.BOTH ? Mode.PULL_DOWN_TO_REFRESH
						: Mode.BOTH);
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };
}
