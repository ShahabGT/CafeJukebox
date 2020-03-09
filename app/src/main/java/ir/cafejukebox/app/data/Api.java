package ir.cafejukebox.app.data;


import ir.cafejukebox.app.models.GeneralResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {


    @FormUrlEncoded
    @POST("login.php")
    Call<GeneralResponse> login(
            @Field("number") String number);

    @FormUrlEncoded
    @POST("verify.php")
    Call<GeneralResponse> verityCode(
            @Field("number") String number,
            @Field("reg_code") String code,
            @Field("fb_token") String fbToken);


}
