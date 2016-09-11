package uno.weichen.abnd8_newsapp;

/**
 * Created by weichen on 9/10/16.
 */
public class News {
    private String mWebTitle;
    private String mWebUrl;
    private String mAuthor;

    public News(String mWebTitle, String mWebUrl, String mAuthor) {
        this.mWebTitle = mWebTitle;
        this.mWebUrl = mWebUrl;
        this.mAuthor = mAuthor;
    }

    public String getmWebTitle() {
        return mWebTitle;
    }

    public String getmWebUrl() {
        return mWebUrl;
    }

    public String getmAuthor() {
        return mAuthor;
    }
}
