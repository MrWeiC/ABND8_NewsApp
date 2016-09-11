package uno.weichen.abnd8_newsapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * URL for news data from Guardianapis
     */
    private static final String GUARDIANAPIS_REQUEST_URL = "http://content.guardianapis.com/search?q=game&type=article&api-key=test&show-references=author;";
    public static final String LOG_TAG = MainActivity.class.getName();

    private static final int NEWS_LOADER_ID = 1;
    public List<News> newsList = new ArrayList<>();
    private NewsAdapter mAdapter;
    private ListView mNewsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Setup View/Data components
         */
        mNewsListView = (ListView) findViewById(R.id.list);
        // Create a new {@link NewsAdapter} of news
        mAdapter = new NewsAdapter(this, newsList);
        mNewsListView.setAdapter(mAdapter);


        // Create a Async tasks loader to query the list of earthquake locations.
        // Get a reference to the LoaderManager, in order to interact with loaders.
        android.app.LoaderManager loaderManager = getLoaderManager();
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);


    }


    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, GUARDIANAPIS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        Log.v("EarthQuakeActivity", "Start update ui");
        updateUi(newsList);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        updateUi(new ArrayList<News>());
        Log.v("EarthQuakeActivity", "Start onLoaderReset");
    }

    /**
     * To update ui in the asynctask
     *
     * @param newsList
     */
    private void updateUi(List<News> newsList) {
        //mNewsListView.setAdapter(null);
        if (newsList != null) {
            //mNewsListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
}
