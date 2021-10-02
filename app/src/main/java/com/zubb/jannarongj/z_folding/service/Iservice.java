package com.zubb.jannarongj.z_folding.service;

import com.zubb.jannarongj.z_folding.Contributor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Iservice {
    @GET("/scale/qr/{bar_id}")
    Call<List<Contributor>> contributor(
            @Path("bar_id") String bar_id
    );
}
