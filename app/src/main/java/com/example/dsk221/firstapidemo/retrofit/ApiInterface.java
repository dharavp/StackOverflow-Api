package com.example.dsk221.firstapidemo.retrofit;

import com.example.dsk221.firstapidemo.models.ListResponse;
import com.example.dsk221.firstapidemo.models.QuestionDetailItem;
import com.example.dsk221.firstapidemo.models.TagItem;
import com.example.dsk221.firstapidemo.models.UserItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dsk-221 on 15/3/17.
 */

public interface ApiInterface {
    @GET("questions")
    Call<ListResponse<QuestionDetailItem>> getQuestionList(@Query("page") int page,
                                                           @Query("fromdate") String fromdate,
                                                           @Query("todate") String todate,
                                                           @Query("order") String order,
                                                           @Query("sort") String sort,
                                                           @Query("tagged") String tagged,
                                                           @Query("site") String site);

    @GET("users")
    Call<ListResponse<UserItem>> getUserList(@Query("page") int page,
                                             @Query("fromdate") String fromdate,
                                             @Query("todate") String todate,
                                             @Query("order") String order,
                                             @Query("sort") String sort,
                                             @Query("inname") String inname,
                                             @Query("site") String site);

    @GET("tags")
    Call<ListResponse<TagItem>> getTagList(@Query("page") int page,
                                           @Query("order") String order,
                                           @Query("sort") String sort,
                                           @Query("inname") String inname,
                                           @Query("site") String site);
    @GET("search")
    Call<ListResponse<QuestionDetailItem>> getSearchQuestionList(@Query("page") int page,
                                                      @Query("fromdate") String fromdate,
                                                      @Query("todate") String todate,
                                                      @Query("order") String order,
                                                      @Query("sort") String sort,
                                                      @Query("intitle") String intitle,
                                                      @Query("site") String site);
}
