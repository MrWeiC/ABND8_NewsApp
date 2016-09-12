package uno.weichen.abnd8_newsapp;

/**
 * Created by weichen on 9/10/16.
 */
public class News {
    private String mWebTitle;
    private String mWebUrl;
    private String mAuthor;
    private String mWebPublicationDate;

    public News(String mWebTitle, String mWebUrl, String mAuthor, String mWebPublicationDate) {
        this.mWebTitle = mWebTitle;
        this.mWebUrl = mWebUrl;
        this.mAuthor = mAuthor;
        this.mWebPublicationDate = mWebPublicationDate;
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

    public String getmWebPublicationDate() {
        return mWebPublicationDate;
    }
}
