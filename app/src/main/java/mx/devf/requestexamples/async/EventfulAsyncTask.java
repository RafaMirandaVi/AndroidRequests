package mx.devf.requestexamples.async;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import mx.devf.requestexamples.EventfulContract;
import mx.devf.requestexamples.model.Event;

public class EventfulAsyncTask extends AsyncTask<String, Void, ArrayList<Event>> {


    public static final String LOG_TAG = EventfulAsyncTask.class.getSimpleName();

    EventfulListener responseListener;

    public EventfulAsyncTask(EventfulListener responseListener) {
        this.responseListener = responseListener;
    }

    @Override
    protected ArrayList<Event> doInBackground(String... params) {

        if(params == null){
            responseListener.onError();
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String eventfulResponse;
        String location = params[0];

        try {
            URL urlEventful = new URL(EventfulContract
                    .getSearchEventsUrl(location));

            //Abrimos la conexión
            urlConnection = (HttpURLConnection) urlEventful.openConnection();
            //Asignamos el método de la url
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //El buffer que almacena la respuesta
            InputStream inputStream = urlConnection.getInputStream();
            //El objeto que construye la respuesta
            StringBuilder responseBuilder = new StringBuilder();

            if (inputStream == null) {
                responseListener.onError();
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line)
                        .append("\n");
            }

            if (responseBuilder.length() == 0) {
                responseListener.onError();
                return null;
            }

            eventfulResponse = responseBuilder.toString();

        } catch (IOException ex) {
            responseListener.onError();
            return null;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    responseListener.onError();
                }
            }
        }

        //TODO: hacer un refactor
        try {
            return EventfulContract.parseEventsFromString(eventfulResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Event> events) {
        super.onPostExecute(events);
        responseListener.onResponseSuccess(events);
    }

    public interface EventfulListener {
        public void onResponseSuccess(ArrayList<Event> events);

        public void onError();
    }
}
