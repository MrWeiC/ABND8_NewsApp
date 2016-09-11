package uno.weichen.abnd8_newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by weichen on 9/10/16.
 */
public class NewsAdapter extends BaseAdapter {
    List<News> newsList;
    Context context;

    public NewsAdapter(Context context,List<News> newsList) {
        super();
        this.newsList = newsList;
        this.context = context;

    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public News getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * inflate the view and set the view holder
         */
        View listItemView = convertView;
        ViewHolder holder;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent,
                false);
            holder = new ViewHolder();
            holder.webTitleTextView = (TextView) listItemView.findViewById(R.id.webTitle_textview);
            holder.authorTextView = (TextView) listItemView.findViewById(R.id.author_textview);
            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }
        /**
         * get the new object and set the value
         */
        News news = getItem(position);

        holder.authorTextView.setText(news.getmWebTitle());
        holder.webTitleTextView.setText(news.getmAuthor());

        return listItemView;
    }


    static class ViewHolder {
        private TextView webTitleTextView;
        private TextView authorTextView;
    }
}
