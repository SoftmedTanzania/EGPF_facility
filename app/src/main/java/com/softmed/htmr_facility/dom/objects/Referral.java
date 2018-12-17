package com.softmed.htmr_facility.dom.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import com.softmed.htmr_facility.utils.DateConverter;
import com.softmed.htmr_facility.utils.ListStringConverter;

/**
 *  Created by issy on 11/28/17.
 */

@Entity
public class Referral implements Serializable{

    private Long id;

    @SerializedName("patientId")
    private String patient_id;

    @NonNull
    @PrimaryKey(autoGenerate = false)
    @SerializedName("referralId")
    private String referral_id;

    @SerializedName("communityBasedHivService")
    private String communityBasedHivService;

    @SerializedName("referralReason")
    private String referralReason;

    /*
    Malaria, Tb or Hiv
     */
    @SerializedName("serviceId")
    private int serviceId;

    @SerializedName("referralUUID")
    private String referralUUID;

    @SerializedName("ctcNumber")
    private String ctcNumber;

    @SerializedName("serviceProviderUIID")
    private String serviceProviderUIID;

    @SerializedName("serviceProviderGroup")
    private String serviceProviderGroup;

    @SerializedName("villageLeader")
    private String villageLeader;

    @SerializedName("referralDate")
    @TypeConverters(DateConverter.class)
    private long referralDate;

    //This is the facility ID the referral is going
    @SerializedName("facilityId")
    private String facilityId;

    @SerializedName("fromFacilityId")
    private String fromFacilityId;

    @TypeConverters(ListStringConverter.class)
    @SerializedName("serviceIndicatorIds")
    private List<Long> serviceIndicatorIds;

    /**
     * This represent the service in which the referral is originated
     */
    @SerializedName("referralSource")
    private int referralSource;


    /*
     * chw ->facility = 1
     * Intra-facility = 2
     * Inter-facility = 3
     * facility - chw = 4
     */
    @SerializedName("referralType")
    private int referralType;

    /*
     *  0 = new
     * -1 = rejected/discarded
     *  1 = complete referral
     */
    @SerializedName("referralStatus")
    private int referralStatus;

    @SerializedName("testResults")
    private boolean testResults;

    /*
    The following two are the feedback portion of a referal
     */
    @SerializedName("serviceGivenToPatient")
    private String serviceGivenToPatient;

    @SerializedName("otherNotes")
    private String otherNotesAndAdvices;

    @SerializedName("otherClinicalInformation")
    private String otherClinicalInformation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getReferral_id() {
        return referral_id;
    }

    public void setReferral_id(String id) {
        this.referral_id = id;
    }

    public String getCommunityBasedHivService() {
        return communityBasedHivService;
    }

    public void setCommunityBasedHivService(String communityBasedHivService) {
        this.communityBasedHivService = communityBasedHivService;
    }

    public String getReferralReason() {
        return referralReason;
    }

    public void setReferralReason(String referralReason) {
        this.referralReason = referralReason;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getCtcNumber() {
        return ctcNumber;
    }

    public void setCtcNumber(String ctcNumber) {
        this.ctcNumber = ctcNumber;
    }

    public int getReferralSource() {
        return referralSource;
    }

    public void setReferralSource(int referralSource) {
        this.referralSource = referralSource;
    }

    public String getServiceProviderUIID() {
        return serviceProviderUIID;
    }

    public void setServiceProviderUIID(String serviceProviderUIID) {
        this.serviceProviderUIID = serviceProviderUIID;
    }

    public String getServiceProviderGroup() {
        return serviceProviderGroup;
    }

    public void setServiceProviderGroup(String serviceProviderGroup) {
        this.serviceProviderGroup = serviceProviderGroup;
    }

    public String getVillageLeader() {
        return villageLeader;
    }

    public void setVillageLeader(String villageLeader) {
        this.villageLeader = villageLeader;
    }

    public long getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(long referralDate) {
        this.referralDate = referralDate;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public int getReferralStatus() {
        return referralStatus;
    }

    public void setReferralStatus(int referralStatus) {
        this.referralStatus = referralStatus;
    }

    public String getFromFacilityId() {
        return fromFacilityId;
    }

    public void setFromFacilityId(String fromFacilityId) {
        this.fromFacilityId = fromFacilityId;
    }

    public String getServiceGivenToPatient() {
        return serviceGivenToPatient;
    }

    public void setServiceGivenToPatient(String serviceGivenToPatient) {
        this.serviceGivenToPatient = serviceGivenToPatient;
    }

    public String getOtherNotesAndAdvices() {
        return otherNotesAndAdvices;
    }

    public void setOtherNotesAndAdvices(String otherNotesAndAdvices) {
        this.otherNotesAndAdvices = otherNotesAndAdvices;
    }

    public String getOtherClinicalInformation() {
        return otherClinicalInformation;
    }

    public void setOtherClinicalInformation(String otherClinicalInformation) {
        this.otherClinicalInformation = otherClinicalInformation;
    }

    public boolean isTestResults() {
        return testResults;
    }

    public void setTestResults(boolean testResults) {
        this.testResults = testResults;
    }

    public String getReferralUUID() {
        return referralUUID;
    }

    public void setReferralUUID(String referralUUID) {
        this.referralUUID = referralUUID;
    }

    public List<Long> getServiceIndicatorIds() {
        return serviceIndicatorIds;
    }

    public void setServiceIndicatorIds(List<Long> serviceIndicatorIds) {
        this.serviceIndicatorIds = serviceIndicatorIds;
    }

    public int getReferralType() {
        return referralType;
    }

    public void setReferralType(int referralType) {
        this.referralType = referralType;
    }
}
