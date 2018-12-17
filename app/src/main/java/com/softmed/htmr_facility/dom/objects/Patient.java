package com.softmed.htmr_facility.dom.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import com.softmed.htmr_facility.utils.DateConverter;

/**
 * Created by issy on 11/28/17.
 */

@Entity
public class Patient implements Serializable{

    private Long id;

    @NonNull
    @PrimaryKey(autoGenerate = false)
    @SerializedName("patientId")
    private String patientId;

    @SerializedName("firstName")
    private String patientFirstName;

    @SerializedName("middleName")
    private String patientMiddleName;

    @SerializedName("surname")
    private String patientSurname;

    @SerializedName("communityBasedHivService")
    private String cbhs;

    @SerializedName("ctcNumber")
    private String ctcNumber;

    @SerializedName("phoneNumber")
    private String phone_number;

    @SerializedName("ward")
    private String ward;

    @SerializedName("village")
    private String village;

    @SerializedName("hamlet")
    private String hamlet;

    @SerializedName("dateOfBirth")
    private long dateOfBirth;

    @SerializedName("gender")
    private String gender;

    @SerializedName("dateOfDeath")
    @TypeConverters(DateConverter.class)
    private long dateOfDeath;

    @SerializedName("hivStatus")
    private boolean hivStatus = false;

    @SerializedName("currentOnTbTreatment")
    private boolean currentOnTbTreatment;

    @SerializedName("careTakerName")
    private String careTakerName;

    @SerializedName("careTakerPhoneNumber")
    private String careTakerPhoneNumber;

    @SerializedName("careTakerRelationship")
    private String careTakerRelationship;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getHamlet() {
        return hamlet;
    }

    public void setHamlet(String hamlet) {
        this.hamlet = hamlet;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(long dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getPatientMiddleName() {
        return patientMiddleName;
    }

    public void setPatientMiddleName(String patientMiddleName) {
        this.patientMiddleName = patientMiddleName;
    }

    public boolean isCurrentOnTbTreatment() {
        return currentOnTbTreatment;
    }

    public void setCurrentOnTbTreatment(boolean currentOnTbTreatment) {
        this.currentOnTbTreatment = currentOnTbTreatment;
    }

    public String getCareTakerName() {
        return careTakerName;
    }

    public void setCareTakerName(String careTakerName) {
        this.careTakerName = careTakerName;
    }

    public String getCareTakerPhoneNumber() {
        return careTakerPhoneNumber;
    }

    public void setCareTakerPhoneNumber(String careTakerPhoneNumber) {
        this.careTakerPhoneNumber = careTakerPhoneNumber;
    }

    public String getCareTakerRelationship() {
        return careTakerRelationship;
    }

    public void setCareTakerRelationship(String careTakerRelationship) {
        this.careTakerRelationship = careTakerRelationship;
    }

    public String getCbhs() {
        return cbhs;
    }

    public void setCbhs(String cbhs) {
        this.cbhs = cbhs;
    }

    public String getCtcNumber() {
        return ctcNumber;
    }

    public void setCtcNumber(String ctcNumber) {
        this.ctcNumber = ctcNumber;
    }

    public boolean isHivStatus() {
        return hivStatus;
    }

    public void setHivStatus(boolean hivStatus) {
        this.hivStatus = hivStatus;
    }
}