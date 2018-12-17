package com.softmed.htmr_facility.dom.responces;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.Referral;

/**
 * Created by issy on 1/9/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class PushNotificationData implements Serializable {

    @SerializedName("patientsDTO")
    private Patient patient;

    @SerializedName("referralsDTOS")
    private List<Referral> referralDTOS;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Referral> getReferralDTOS() {
        return referralDTOS;
    }

    public void setReferralDTOS(List<Referral> referralDTOS) {
        this.referralDTOS = referralDTOS;
    }
}
