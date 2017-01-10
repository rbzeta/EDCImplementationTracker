package app.rbzeta.edcimplementationtracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 1/4/2017.
 */

public class EDCItem implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("tid")
    private String tid;
    @SerializedName("mid")
    private String mid;
    @SerializedName("type")
    private String type;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("init_region")
    private String initRegion;
    @SerializedName("init_branch")
    private String initBranch;
    @SerializedName("impl_region")
    private String implRegion;
    @SerializedName("impl_branch")
    private String implBranch;
    @SerializedName("impl_branch_name")
    private String implBranchName;
    @SerializedName("sn_edc")
    private String snEdc;
    @SerializedName("sn_sim")
    private String snSim;
    @SerializedName("sn_sam")
    private String snSam;
    @SerializedName("status")
    private String status;
    @SerializedName("url_ba")
    private String urlBa;
    @SerializedName("url_picture")
    private String urlPicture;
    @SerializedName("description")
    private String description;
    @SerializedName("create_dt")
    private String createDt;
    @SerializedName("update_dt")
    private String updateDt;
    @SerializedName("create_usr")
    private String createUser;
    @SerializedName("update_usr")
    private String updateUser;
    @SerializedName("impl_main_code")
    private String implMainCode;
    @SerializedName("impl_main_branch")
    private String implMainBranch;
    @SerializedName("phone")
    private String phoneNumber;

    protected EDCItem(Parcel in) {
        id = in.readInt();
        tid = in.readString();
        mid = in.readString();
        type = in.readString();
        name = in.readString();
        address = in.readString();
        initRegion = in.readString();
        initBranch = in.readString();
        implRegion = in.readString();
        implBranch = in.readString();
        implBranchName = in.readString();
        snEdc = in.readString();
        snSim = in.readString();
        snSam = in.readString();
        status = in.readString();
        urlBa = in.readString();
        urlPicture = in.readString();
        description = in.readString();
        createDt = in.readString();
        updateDt = in.readString();
        createUser = in.readString();
        updateUser = in.readString();
        implMainCode = in.readString();
        implMainBranch = in.readString();
        phoneNumber = in.readString();
    }

    public static final Creator<EDCItem> CREATOR = new Creator<EDCItem>() {
        @Override
        public EDCItem createFromParcel(Parcel in) {
            return new EDCItem(in);
        }

        @Override
        public EDCItem[] newArray(int size) {
            return new EDCItem[size];
        }
    };

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImplMainCode() {
        return implMainCode;
    }

    public void setImplMainCode(String implMainCode) {
        this.implMainCode = implMainCode;
    }

    public String getImplMainBranch() {
        return implMainBranch;
    }

    public void setImplMainBranch(String implMainBranch) {
        this.implMainBranch = implMainBranch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInitRegion() {
        return initRegion;
    }

    public void setInitRegion(String initRegion) {
        this.initRegion = initRegion;
    }

    public String getInitBranch() {
        return initBranch;
    }

    public void setInitBranch(String initBranch) {
        this.initBranch = initBranch;
    }

    public String getImplRegion() {
        return implRegion;
    }

    public void setImplRegion(String implRegion) {
        this.implRegion = implRegion;
    }

    public String getImplBranch() {
        return implBranch;
    }

    public void setImplBranch(String implBranch) {
        this.implBranch = implBranch;
    }

    public String getImplBranchName() {
        return implBranchName;
    }

    public void setImplBranchName(String implBranchName) {
        this.implBranchName = implBranchName;
    }

    public String getSnEdc() {
        return snEdc;
    }

    public void setSnEdc(String snEdc) {
        this.snEdc = snEdc;
    }

    public String getSnSim() {
        return snSim;
    }

    public void setSnSim(String snSim) {
        this.snSim = snSim;
    }

    public String getSnSam() {
        return snSam;
    }

    public void setSnSam(String snSam) {
        this.snSam = snSam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrlBa() {
        return urlBa;
    }

    public void setUrlBa(String urlBa) {
        this.urlBa = urlBa;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(String updateDt) {
        this.updateDt = updateDt;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(tid);
        parcel.writeString(mid);
        parcel.writeString(type);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(initRegion);
        parcel.writeString(initBranch);
        parcel.writeString(implRegion);
        parcel.writeString(implBranch);
        parcel.writeString(implBranchName);
        parcel.writeString(snEdc);
        parcel.writeString(snSim);
        parcel.writeString(snSam);
        parcel.writeString(status);
        parcel.writeString(urlBa);
        parcel.writeString(urlPicture);
        parcel.writeString(description);
        parcel.writeString(createDt);
        parcel.writeString(updateDt);
        parcel.writeString(createUser);
        parcel.writeString(updateUser);
        parcel.writeString(implMainCode);
        parcel.writeString(implMainBranch);
        parcel.writeString(phoneNumber);
    }
}
