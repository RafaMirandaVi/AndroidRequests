package mx.devf.requestexamples.retrofit;

import mx.devf.requestexamples.EventfulContract;
import retrofit.http.GET;
import retrofit.http.Query;

public interface EventfulService {
    @GET(EventfulContract.URL_EVENTS_SEARCH)
    public void findEvents(@Query(EventfulContract.PARAM_APP_KEY) String appKey ,
                           @Query(EventfulContract.PARAM_DATE) String date,
                           @Query(EventfulContract.PARAM_LOCATION) String location,
                           retrofit.Callback<EventRequestModel.EventsModelResponse> eventsCallback);
}
