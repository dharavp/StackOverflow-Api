package com.example.dsk221.firstapidemo.retrofit;

import com.example.dsk221.firstapidemo.models.ListResponse;
import com.example.dsk221.firstapidemo.models.QuestionDetailItem;
import com.example.dsk221.firstapidemo.models.TagItem;
import com.example.dsk221.firstapidemo.models.UserItem;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dsk-221 on 15/3/17.
 */

public interface ApiInterface {
    @GET("questions")
    Call<ListResponse<QuestionDetailItem>>getQuestionDetail(@Query("page") int page,
                                                            @Query("fromdate") String fromdate,
                                                            @Query("todate") String todate,
                                                            @Query("order") String order,
                                                            @Query("sort") String sort,
                                                            @Query("site") String site);

    @GET("users")
    Call<ListResponse<UserItem>> getUserDetail(@Query("page") int page,
                                               @Query("fromdate") String fromdate,
                                               @Query("todate") String todate,
                                               @Query("order") String order,
                                               @Query("sort") String sort,
                                               @Query("site") String site);

    @GET("tags")
    Call<ListResponse<TagItem>> getTagDetail(@Query("page") int page,
                                             @Query("order") String order,
                                             @Query("sort") String sort,
                                             @Query("site") String site);
}
