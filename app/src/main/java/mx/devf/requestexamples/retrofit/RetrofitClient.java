package mx.devf.requestexamples.retrofit;

import mx.devf.requestexamples.EventfulContract;
import retrofit.RestAdapter;

public class RetrofitClient {

    private EventfulService apiContract;

    public RetrofitClient() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint(EventfulContract.BASE_URL)
                .build();

        apiContract = restAdapter.create(EventfulService.class);
    }

    public EventfulService getApiContract() {
        return apiContract;

    }
}