package uno.weichen.abnd8_newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    /**
     * URL for news data from Guardianapis
     */
    private static final String GUARDIANAPIS_REQUEST_URL = "http://content.guardianapis.com/search?type=article&page-size=24&api-key=test&show-tags=contributor";
    //Test No result URL
    //private static final String GUARDIANAPIS_REQUEST_URL = "http://content.guardianapis.com/search?type=articldfdfdffdfdfe&page-size=24&api-key=test&show-tags=contributor";/**/
    private static final int NEWS_LOADER_ID = 1;
    public List<News> newsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mEmptyStateTextView;
    private ProgressBar mProgressbarView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout_empty;
    private android.app.LoaderManager loaderManager;
    private int mInterval = 30000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Resolve View
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        // Get the ProgressBar view
        mProgressbarView = (ProgressBar) findViewById(R.id.loading_spinner);
        // Get the TextView view
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_list_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout_empty = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout_empty);
        /**
         * Set View
         */
        // Create a new {@link NewsAdapter} of news
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NewsAdapter(newsList);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout_empty.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Log.v(LOG_TAG, "onRefresh previous no internet/no result called");
                loaderManager = getLoaderManager();
                refreshContent();

            }
        });

        //Check if there are internet connection
        ConnectivityManager connMgr = (ConnectivityManager)
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Create a Async tasks loader to query the list of news.
            // Get a reference to the LoaderManager, in order to interact with loaders.
            loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

            //Should have internet connection then we could set the RefreshListener
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    Log.v(LOG_TAG, "onRefresh previous with internet was called");
                    refreshContent();
                }
            });

            //Should have internet connection then we could set the RefreshListener

            /**
             * Use Handler to create task to check news every 30 sec
             */
            mHandler = new Handler();
            startRepeatingTask();
        } else {
            mProgressbarView.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        return new NewsLoader(this, GUARDIANAPIS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        mEmptyStateTextView.setText(R.string.no_news);
        mProgressbarView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        if (newsList == null || newsList.isEmpty()) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            return;
        }
        Log.v(LOG_TAG, "After pass data to newslist. Size is " + newsList.size());
        Log.v(LOG_TAG, "Start update ui");
        updateUi(newsList);
        //Question for reviewer, where is the best place to set those setRefreshing(false);?
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout_empty.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        updateUi(new ArrayList<News>());
        Log.v(LOG_TAG, "Start onLoaderReset");
    }

    /**
     * To update ui in the asynctask
     *
     * @param newsList
     */
    private void updateUi(List<News> newsList) {
        Log.v(LOG_TAG, "newsList size is from newsList. newsList.size() is " + newsList.size());
        Log.v(LOG_TAG, "newsList size from mAdapter.getItemCount()" + mAdapter.getItemCount());
        if (newsList != null) {
            mAdapter.setData(newsList);
            Log.v(LOG_TAG, "newsList size is from newsList. newsList.size() after clear()and addAll() is " + newsList.size());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void refreshContent() {
        if (loaderManager != null) {
            Log.v(LOG_TAG, "RefreshContent was called.");
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        }
    }

    /**
     * For repeating Tasks
     */

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mStatusChecker);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                refreshContent(); //this function can change value of mInterval.
            } finally {

                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };
}
