package uno.weichen.abnd8_newsapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by weichen on 9/10/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    public static final String LOG_TAG = NewsAdapter.class.getName();

    private List<News> mNewsList;

    public NewsAdapter(List<News> newsList) {
        super();
        this.mNewsList = newsList;
        Log.i(LOG_TAG, "mNewList.size =  " + mNewsList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView webTitleTextView;
        public TextView authorTextView;
        public TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            webTitleTextView = (TextView) itemView.findViewById(R.id.webTitle_textview);
            authorTextView = (TextView) itemView.findViewById(R.id.author_textview);
            dateTextView = (TextView) itemView.findViewById(R.id.date_textview);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         * inflate the view and set the view holder
         */
        View recyclerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,
            false);
        ViewHolder vh = new ViewHolder(recyclerView);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = mNewsList.get(position);
        holder.webTitleTextView.setText(news.getmWebTitle());
        holder.authorTextView.setText(news.getmAuthor());
        holder.dateTextView.setText(news.getmWebPublicationDate());
        Log.v(LOG_TAG, "The position is " + position + ". And Title is " + news.getmWebTitle());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addEntity(int i, News entity) {
        mNewsList.add(i, entity);
        notifyItemInserted(i);
    }

    public void deleteEntity(int i) {
        mNewsList.remove(i);
        notifyItemRemoved(i);
    }

    public void setData( List<News> data) {
        // Remove all deleted items.
        for (int i = mNewsList.size() - 1; i >= 0; --i) {
            deleteEntity(i);
        }

        // Add and move items.
        for (int i = 0; i < data.size(); ++i) {
            News entity = data.get(i);
            addEntity(i, entity);
        }
    }


    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

}
