package com.multilingual.rupali.accesspoints.api;

import com.multilingual.rupali.accesspoints.models.AccessPointAddress;
import com.multilingual.rupali.accesspoints.models.Order;
import com.multilingual.rupali.accesspoints.response.AccessPointsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AccessPointAPI {
    @POST("search")
    Call<AccessPointsResponse> getAccessPoints(@Body AccessPointAddress address);
}
