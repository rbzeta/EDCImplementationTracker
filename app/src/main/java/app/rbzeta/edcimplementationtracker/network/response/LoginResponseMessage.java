package app.rbzeta.edcimplementationtracker.network.response;

import com.google.gson.annotations.SerializedName;

import app.rbzeta.edcimplementationtracker.model.User;

/**
 * Created by Robyn on 1/8/2017.
 */

public class LoginResponseMessage {

    @SerializedName("code")
    private String code;

    @SerializedName("title")
    private String title;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
