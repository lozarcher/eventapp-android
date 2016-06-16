package com.loz.iyaf.feed;

import com.squareup.okhttp.RequestBody;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.Path;

public interface EventappService {
    @GET("/events")
    Call<EventList> getEvents();

    @GET("/gallery")
    Call<GalleryList> getGallery();

    @Multipart
    @POST("/gallery2")
    Call<String> uploadGallery2(
            @Part("photo\"; filename=\"123123213123.png\" ") RequestBody file,
            @Part("name") String user,
            @Part("caption") String caption,
            @Part("filename") String filename);

    @Multipart
    @POST("/gallery")
    Call<GalleryList> uploadGallery (@PartMap Map<String, RequestBody> params);

    @GET("/traders")
    Call<TraderList> getTraders();

    @GET("/info")
    Call<InfoList> getInfo();

    @GET("/posts")
    Call<NewsList> getNews();

    @GET("/posts/{page}")
    Call<NewsList> getNews(@Path("page") int page);

    @GET("/tweets")
    Call<TwitterList> getTweets();

    @GET("/tweets/{page}")
    Call<TwitterList> getTweets(@Path("page") int page);

    @GET("/vouchers")
    Call<VoucherList> getVouchers();
}
