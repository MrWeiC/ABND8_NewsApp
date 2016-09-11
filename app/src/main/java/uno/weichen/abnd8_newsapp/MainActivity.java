package uno.weichen.abnd8_newsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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


        newsList.add(new News("test1", "test2", "test3"));
        newsList.add(new News("test4", "test5", "test6"));
        mAdapter = new NewsAdapter(this, newsList);
        mNewsListView.setAdapter(mAdapter);

    }

}
