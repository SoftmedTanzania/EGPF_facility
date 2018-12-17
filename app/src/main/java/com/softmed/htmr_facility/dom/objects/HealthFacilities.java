package com.softmed.htmr_facility.dom.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by issy on 1/7/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Entity
public class HealthFacilities implements Serializable {

    @SerializedName("id")
    @PrimaryKey
    @NonNull
    private int ID;

    @SerializedName("openMRSUIID")
    private String openMRSUIID;

    @SerializedName("facilityName")
    private String facilityName;

    @SerializedName("facilityCtcCode")
    private String facilityCtcCode;

    @SerializedName("parentOpenmrsUIID")
    private String parentOpenmrsUIID;

    @SerializedName("parentHFRCode")
    private String parentHFRCode;

    @SerializedName("hfrCode")
    private String hfrCode;

    @NonNull
    public int getID() {
        return ID;
    }

    public void setID(@NonNull int ID) {
        this.ID = ID;
    }

    public String getOpenMRSUIID() {
        return openMRSUIID;
    }

    public void setOpenMRSUIID(String openMRSUIID) {
        this.openMRSUIID = openMRSUIID;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityCtcCode() {
        return facilityCtcCode;
    }

    public void setFacilityCtcCode(String facilityCtcCode) {
        this.facilityCtcCode = facilityCtcCode;
    }

    public String getParentOpenmrsUIID() {
        return parentOpenmrsUIID;
    }

    public void setParentOpenmrsUIID(String parentOpenmrsUIID) {
        this.parentOpenmrsUIID = parentOpenmrsUIID;
    }

    public String getParentHFRCode() {
        return parentHFRCode;
    }

    public void setParentHFRCode(String parentHFRCode) {
        this.parentHFRCode = parentHFRCode;
    }

    public String getHfrCode() {
        return hfrCode;
    }

    public void setHfrCode(String hfrCode) {
        this.hfrCode = hfrCode;
    }

}
