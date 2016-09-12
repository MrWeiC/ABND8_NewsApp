package uno.weichen.abnd8_newsapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by weichen on 9/10/16.
 */
public class NewsAdapter extends BaseAdapter {
    public static final String LOG_TAG = NewsAdapter.class.getName();

    List<News> newsList;
    Context context;

    public NewsAdapter(Context context, List<News> newsList) {
        super();
        this.newsList = newsList;
        this.context = context;

    }

    @Override
    public int getCount() {
        Log.d(LOG_TAG, "GetCount number is " + newsList.size());
        return this.newsList.size();
    }

    @Override
    public News getItem(int position) {
        return this.newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addAll(List newList) {
        newsList.clear();
        newsList.addAll(newList);
    }

    public void clear() {
        newsList.clear();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * inflate the view and set the view holder
         */
        Log.d(LOG_TAG, "getView start");
        View listItemView = convertView;
        ViewHolder holder;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent,
                false);
            holder = new ViewHolder();
            holder.webTitleTextView = (TextView) listItemView.findViewById(R.id.webTitle_textview);
            holder.authorTextView = (TextView) listItemView.findViewById(R.id.author_textview);
            holder.dateTextView = (TextView) listItemView.findViewById(R.id.date_textview);
            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }
        /**
         * get the new object and set the value
         */
        News news = getItem(position);

        holder.webTitleTextView.setText(news.getmWebTitle());
        holder.authorTextView.setText(news.getmAuthor());
        holder.dateTextView.setText(news.getmWebPublicationDate());

        return listItemView;
    }


    static class ViewHolder {
        private TextView webTitleTextView;
        private TextView authorTextView;
        private TextView dateTextView;
    }
}
