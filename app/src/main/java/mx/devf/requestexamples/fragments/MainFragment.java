package mx.devf.requestexamples.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import mx.devf.requestexamples.EventfulContract;
import mx.devf.requestexamples.R;
import mx.devf.requestexamples.async.EventfulAsyncTask;
import mx.devf.requestexamples.model.Event;
import mx.devf.requestexamples.retrofit.EventRequestModel;
import mx.devf.requestexamples.retrofit.RetrofitClient;
import mx.devf.requestexamples.volley.VolleyClient;
import retrofit.Callback;
import retrofit.RetrofitError;

public class MainFragment extends Fragment
        implements EventfulAsyncTask.EventfulListener,
        View.OnClickListener {

    private EditText mEtxLocation;
    private TextView mTxtTitle, mTextDate, mTextDescription;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mEtxLocation = (EditText) rootView.findViewById(R.id.etx_location_event);
        mTxtTitle = (TextView) rootView.findViewById(R.id.event_title);
        mTextDate = (TextView) rootView.findViewById(R.id.event_date);
        mTextDescription = (TextView) rootView.findViewById(R.id.event_description);
        Button btnAsync = (Button) rootView.findViewById(R.id.btn_async);
        Button btnVolley = (Button) rootView.findViewById(R.id.btn_volley);
        Button btnRetrofit = (Button) rootView.findViewById(R.id.btn_retrofit);

        btnRetrofit.setOnClickListener(this);
        btnVolley.setOnClickListener(this);
        btnAsync.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View viewClicked) {
        switch (viewClicked.getId()) {
            case R.id.btn_async:
                executeRequestByAsyncTask();
                break;

            case R.id.btn_volley:
                executeRequestWithVolley();
                break;

            case R.id.btn_retrofit:
                executeRequestWithRetrofit();
                break;
        }

    }

    private void executeRequestWithRetrofit() {

        String location;
        if (mEtxLocation.getText().toString().isEmpty()) {
            //TODO: Mostrar un mensaje al usuario de que no ha ingresado nada
            return;
        }

        location = mEtxLocation.getText().toString();

       RetrofitClient retrofitClient = new RetrofitClient();

        retrofitClient.getApiContract().findEvents(EventfulContract.APP_KEY , EventfulContract.VALUE_THIS_WEEK , location,
                new Callback<EventRequestModel.EventsModelResponse>() {
                    @Override
                    public void success(EventRequestModel.EventsModelResponse eventsModelResponse,
                                        retrofit.client.Response response) {

                        List<EventRequestModel.EventGson> listEvents = eventsModelResponse.getListEvents();
                        EventRequestModel.EventGson randomEvent = listEvents.get(2);
                        refreshInfoEvent(randomEvent.getTitle(), randomEvent.getDate(), randomEvent.getDescription());
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

    }

    private void executeRequestByAsyncTask() {
        if (!mEtxLocation.getText().toString().isEmpty()) {
            new EventfulAsyncTask(this)
                    .execute(mEtxLocation.getText().toString());
        }

    }

    private void executeRequestWithVolley() {

        String location;
        if (mEtxLocation.getText().toString().isEmpty()) {
            //TODO: Mostrar un mensaje al usuario de que no ha ingresado nada
            return;
        }

        location = mEtxLocation.getText().toString();

        StringRequest eventfulRequest = new StringRequest(Request.Method.GET,
                EventfulContract.getSearchEventsUrl(location),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<Event> eventList = EventfulContract.parseEventsFromString(response);
                            Event randomEvent = eventList.get(1);

                            refreshInfoEvent(randomEvent.getTitle(),randomEvent.getDate(),randomEvent.getDescription());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
        });

        VolleyClient.getInstance(getActivity())
                .addToRequestQueue(eventfulRequest);

    }

    @Override
    public void onResponseSuccess(ArrayList<Event> events) {
        //Definir que hacer cuando la petición se haya completado
        Event randomEvent = events.get(0);

        refreshInfoEvent(randomEvent.getTitle(),randomEvent.getDate(),randomEvent.getDescription());
    }

    @Override
    public void onError() {
        //Qué hacer en caso de error
    }

    public void refreshInfoEvent (String title, String date, String description) {
        mTxtTitle.setText(title);
        mTextDate.setText(date);
        mTextDescription.setText(description);
    }


}
