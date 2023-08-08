package com.example.shoponline.retrofit;

import com.example.shoponline.model.CategoryModel;
import com.example.shoponline.model.ModelUser;
import com.example.shoponline.model.ProductModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIShop {

    @POST("auth/api/sign-up/")
    @FormUrlEncoded
    Observable<ModelUser> register(
            @Field("email") String email,
            @Field("username") String name,
            @Field("password") String password
    );

    @POST("auth/api/login/")
    @FormUrlEncoded
    Observable<ModelUser> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("auth/api/login/google")
    @FormUrlEncoded
    Observable<ModelUser> loginGoogle(
        @Field("id_google") String id_google,
        @Field("name") String name,
        @Field("avatar") String avatar,
        @Field("email") String email
    );

    @GET("api/category/")
    Observable<CategoryModel> getCategory();

    @GET("api/product/")
    Observable<ProductModel> getProduct();
}
