package ir.cafejukebox.app.models;

import com.google.gson.annotations.SerializedName;

public class GeneralResponse {


    private String status;

    private String message;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("mobile_number")
    private String mobileNumber;

    private String name;

    private String sex;

    private String age;

    @SerializedName("reg_date")
    private String regDate;

    private String score;

    @SerializedName("first_time")
    private String firstTime;



    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getAge() {
        return age;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getScore() {
        return score;
    }

    public String getFirstTime() {
        return firstTime;
    }
}
