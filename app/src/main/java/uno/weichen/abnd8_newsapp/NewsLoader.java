package uno.weichen.abnd8_newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weichen on 9/10/16.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {
    public String mUrl;
    public List<News> newsList = new ArrayList<>();

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null){
            return null;
        }
        newsList = QueryUtils.fetchNewsData(mUrl);

        return newsList;

    }
}
