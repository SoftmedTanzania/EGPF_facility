package com.softmed.htmr_facility.dom.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import com.softmed.htmr_facility.utils.DateConverter;

/**
 * Created by issy on 12/28/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

@Entity
public class TbPatient implements Serializable{

    /*
    This is the ID of tbPatient table
     */
    @NonNull
    private String tempID;

    @PrimaryKey(autoGenerate = false)
    private long patientId;

    @SerializedName("tbPatientId")
    private long tbPatientId;

    @SerializedName("patient_type")
    private int patientType;

    @SerializedName("transfer_type")
    private int transferType;

    @SerializedName("referral_type")
    private int referralType;

    @SerializedName("veo")
    private String veo;

    @SerializedName("weight")
    private double weight;

    @SerializedName("xray")
    private String xray;

    @SerializedName("makohozi")
    private String makohozi;

    @SerializedName("other_tests")
    private String otherTests;

    @SerializedName("treatment_type")
    private String treatment_type;

    @SerializedName("outcome")
    private String outcome;

    @SerializedName("outcome_date")
    @TypeConverters(DateConverter.class)
    private long outcomeDate;

    @SerializedName("outcome_details")
    private String outcomeDetails;

    @SerializedName("isPregnant")
    private boolean isPregnant;

    public void setTempID(@NonNull String id) {
        this.tempID = id;
    }

    public String getOtherTests() {
        return otherTests;
    }

    public void setOtherTests(String otherTests) {
        this.otherTests = otherTests;
    }

    @NonNull
    public String getTempID() {
        return tempID;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public int getPatientType() {
        return patientType;
    }

    public void setPatientType(int patientType) {
        this.patientType = patientType;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public int getReferralType() {
        return referralType;
    }

    public void setReferralType(int referralType) {
        this.referralType = referralType;
    }

    public String getVeo() {
        return veo;
    }

    public void setVeo(String veo) {
        this.veo = veo;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getXray() {
        return xray;
    }

    public void setXray(String xray) {
        this.xray = xray;
    }

    public String getMakohozi() {
        return makohozi;
    }

    public void setMakohozi(String makohozi) {
        this.makohozi = makohozi;
    }

    public String getTreatment_type() {
        return treatment_type;
    }

    public void setTreatment_type(String treatment_type) {
        this.treatment_type = treatment_type;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public long getOutcomeDate() {
        return outcomeDate;
    }

    public void setOutcomeDate(long outcomeDate) {
        this.outcomeDate = outcomeDate;
    }

    public String getOutcomeDetails() {
        return outcomeDetails;
    }

    public void setOutcomeDetails(String outcomeDetails) {
        this.outcomeDetails = outcomeDetails;
    }

    public Long getTbPatientId() {
        return tbPatientId;
    }

    public void setTbPatientId(Long tbPatientId) {
        this.tbPatientId = tbPatientId;
    }

    public boolean isPregnant() {
        return isPregnant;
    }

    public void setPregnant(boolean pregnant) {
        isPregnant = pregnant;
    }
}
