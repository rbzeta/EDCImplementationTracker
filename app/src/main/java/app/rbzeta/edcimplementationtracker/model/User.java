package app.rbzeta.edcimplementationtracker.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 1/8/2017.
 */

public class User {
    @SerializedName("user_id")
    private String userId;
   @SerializedName("user_lname")
    private String userName;
    @SerializedName("user_pn")
    private String userPN;
    @SerializedName("user_jabatan")
    private String userJabatan;
    @SerializedName("user_level_id")
    private int userLevelId;
    @SerializedName("user_uker")
    private int userUker;
    @SerializedName("user_email")
    private String userEmail;
    @SerializedName("user_status")
    private int userStatus;
    @SerializedName("user_creadt")
    private String userCreaDt;
    @SerializedName("user_upddt")
    private String userUpdDt;
    @SerializedName("user_creausr")
    private String userCreaUsr;
    @SerializedName("user_updusr")
    private String userUpdUsr;
    @SerializedName("branch_mbcode")
    private String userBranchCode;
    @SerializedName("branch_mbname")
    private String userBranchName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPN() {
        return userPN;
    }

    public void setUserPN(String userPN) {
        this.userPN = userPN;
    }

    public String getUserJabatan() {
        return userJabatan;
    }

    public void setUserJabatan(String userJabatan) {
        this.userJabatan = userJabatan;
    }

    public int getUserLevelId() {
        return userLevelId;
    }

    public void setUserLevelId(int userLevelId) {
        this.userLevelId = userLevelId;
    }

    public int getUserUker() {
        return userUker;
    }

    public void setUserUker(int userUker) {
        this.userUker = userUker;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserCreaDt() {
        return userCreaDt;
    }

    public void setUserCreaDt(String userCreaDt) {
        this.userCreaDt = userCreaDt;
    }

    public String getUserUpdDt() {
        return userUpdDt;
    }

    public void setUserUpdDt(String userUpdDt) {
        this.userUpdDt = userUpdDt;
    }

    public String getUserCreaUsr() {
        return userCreaUsr;
    }

    public void setUserCreaUsr(String userCreaUsr) {
        this.userCreaUsr = userCreaUsr;
    }

    public String getUserUpdUsr() {
        return userUpdUsr;
    }

    public void setUserUpdUsr(String userUpdUsr) {
        this.userUpdUsr = userUpdUsr;
    }

    public String getUserBranchCode() {
        return userBranchCode;
    }

    public void setUserBranchCode(String userBranchCode) {
        this.userBranchCode = userBranchCode;
    }

    public String getUserBranchName() {
        return userBranchName;
    }

    public void setUserBranchName(String userBranchName) {
        this.userBranchName = userBranchName;
    }
}
