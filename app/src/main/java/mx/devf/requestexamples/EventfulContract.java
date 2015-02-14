package mx.devf.requestexamples;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mx.devf.requestexamples.model.Event;

public class EventfulContract {
    public static final String BASE_URL = "http://www.eventful.com";
    public static final String PATH_JSON = "json";
    public static final String PATH_EVENTS = "events";
    public static final String PATH_SEARCH = "search";

    public static final String URL_EVENTS_SEARCH = "/" + PATH_JSON + "/" + PATH_EVENTS + "/" + PATH_SEARCH;

    public static final String PARAM_LOCATION = "location";
    public static final String PARAM_APP_KEY = "app_key";
    public static final String PARAM_DATE = "date";

    public static final String APP_KEY = "4Q7hC5bJXwHMZ99t";
    public static final String VALUE_THIS_WEEK = "This Week";

    public static final String JSON_KEY_EVENTS = "events";
    public static final String JSON_KEY_PACKAGE_EVENTS = "tabular";
    public static final String JSON_KEY_DATE_EVENT = "rf_start_time";
    public static final String JSON_KEY_DESCRIPTION_EVENT = "description";
    public static final String JSON_KEY_TITLE_EVENT = "title";

    public static String getSearchEventsUrl (String location){
        Uri eventUrl = Uri.parse(EventfulContract.BASE_URL).buildUpon()
                .appendPath(EventfulContract.PATH_JSON)
                .appendPath(EventfulContract.PATH_EVENTS)
                .appendPath(EventfulContract.PATH_SEARCH)
                .appendQueryParameter(EventfulContract.PARAM_APP_KEY, EventfulContract.APP_KEY)
                .appendQueryParameter(EventfulContract.PARAM_LOCATION, location)
                .appendQueryParameter(EventfulContract.PARAM_DATE, VALUE_THIS_WEEK)
                .build();

        return eventUrl.toString();
    }

    public static ArrayList<Event> parseEventsFromString (String response) throws JSONException {
        JSONArray events = new JSONObject(response)
                .getJSONObject(JSON_KEY_EVENTS)
                .getJSONObject(JSON_KEY_PACKAGE_EVENTS)
                .getJSONArray(JSON_KEY_EVENTS);

        ArrayList<Event> listEvents = new ArrayList<>();

        for (int i = 0; i < events.length(); i++) {
            JSONObject currentEvent = events.getJSONObject(i);

            String title =  currentEvent.getString(JSON_KEY_TITLE_EVENT);
            String date = currentEvent.getString(JSON_KEY_DATE_EVENT);
            String description =  currentEvent.getString(JSON_KEY_DESCRIPTION_EVENT);

            Event event = new Event(title, date, description);
            listEvents.add(event);
        }

        return listEvents;
    }
}
