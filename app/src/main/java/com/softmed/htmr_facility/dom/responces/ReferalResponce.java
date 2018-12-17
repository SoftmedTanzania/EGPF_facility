package com.softmed.htmr_facility.dom.responces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.Referral;

/**
 * Created by issy on 12/3/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferal
 */

public class ReferalResponce implements Serializable {

    @SerializedName("patientsDTO")
    private Patient patient;

    @SerializedName("referralsDTOS")
    private List<Referral> referralDTOS;

    @Expose
    @SerializedName("patientReferralsList")
    private List<Referral> patientReferalList;

    public ReferalResponce(){}

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Referral> getPatientReferalList() {
        return patientReferalList;
    }

    public void setPatientReferalList(List<Referral> patientReferalList) {
        this.patientReferalList = patientReferalList;
    }

    public List<Referral> getReferralDTOS() {
        return referralDTOS;
    }

    public void setReferralDTOS(List<Referral> referralDTOS) {
        this.referralDTOS = referralDTOS;
    }
}
