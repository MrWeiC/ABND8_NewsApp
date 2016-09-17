package uno.weichen.abnd8_newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from guardian. Created by weichen on
 * 9/10/16.
 */
public class QueryUtils {
    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed directly
     * from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response from the
     * server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset
                .forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Make a http call to get all the json from the API query
     *
     * @param url
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();//might got IOException
                jsonResponse = readFromStream(inputStream);
                Log.d(LOG_TAG, "Successfully get json.");

            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Status code:" + urlConnection.getResponseCode(), e);
        }

        return jsonResponse;
    }

    /**
     * Return a list of {@link News} objects that has been built up from parsing a JSON response.
     */
    public static ArrayList<News> extractNews(String jsonString) {

        // Create an empty ArrayList that we can start adding News to
        ArrayList<News> newsData = new ArrayList<>();

        // Try to parse the GUARDIANAPIS_REQUEST_URL. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of News objects with the corresponding data.
            JSONObject root = new JSONObject(jsonString);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String webTitle = result.getString("webTitle");
                String webUrl = result.getString("webUrl");
                String webPublicationDate = formatDate(result.getString("webPublicationDate"));
                String author = "";
                JSONArray tags = result.getJSONArray("tags");
                if (tags.length() > 0) {
                    try {
                        JSONObject tagWithAuthor = tags.getJSONObject(0);
                        /**
                         *  Questions, I tried this...
                         *  String firstName = tags.get(0).getString("firstName")
                         *  it says getString cannot be resolved, why is that??
                         */

                        String firstName = tagWithAuthor.getString("firstName");
                        String lastName = tagWithAuthor.getString("lastName");
                        if(!firstName.equals("")){
                            author = firstName + " ";
                        }
                        author += lastName;

                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "Problem get author text even if there are author info.", e);

                    }
                }
                newsData.add(new News(webTitle, webUrl, author, webPublicationDate));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of News
        return newsData;
    }

    private static String formatDate(String webPublicationDate) {
        //I don't find good way to covert an 8601 format 8601 format to Date. So I will truncaket time
        if (webPublicationDate == null) {
            return "";
        }

        return webPublicationDate.substring(0, 10);
    }

    /**
     * Overal function to get the newsData list from an Url
     *
     * @param stringUrl
     * @return
     */
    public static List<News> fetchNewsData(String stringUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(stringUrl);

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
            Log.v(LOG_TAG, "Successfully make Http Request");
        } catch (IOException e) {
            Log.v(LOG_TAG, "Failed make Http Request");

        }
        List<News> newsData = extractNews(jsonResponse);
        Log.v(LOG_TAG, "Successfully extract News ArrayList. Size is "+newsData.size());
        return newsData;
    }

}
