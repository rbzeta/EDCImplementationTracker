package app.rbzeta.edcimplementationtracker.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.rbzeta.edcimplementationtracker.model.EDCItem;

/**
 * Created by Robyn on 1/4/2017.
 */

public class EDCResponseMessage {
    @SerializedName("code")
    private String code;

    @SerializedName("title")
    private String title;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("edcs")
    private List<EDCItem> edcList;

    @SerializedName("edc")
    private EDCItem edc;

    public List<EDCItem> getEdcList() {
        return edcList;
    }

    public void setEdcList(List<EDCItem> edcList) {
        this.edcList = edcList;
    }

    public EDCItem getEdc() {
        return edc;
    }

    public void setEdc(EDCItem edc) {
        this.edc = edc;
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
