package com.softmed.htmr_facility.utils;

/**
 * Created by issy on 11/23/17.
 */

public class constants {

    public static String BASE_URL = "http://45.56.90.103:8080/opensrp/";
    //public static String BASE_URL = "http://192.168.43.251:8080/opensrp/";

    public static final String STATUS_COMPLETED = "Tayari";
    public static final String STATUS_NEW = "Mpya";
    public static final String STATUS_PENDING = "Bado";
    public static final String STATUS_CANCELLED = "Imefutwa";

    public static final String MALE = "Male";
    public static final String FEMALE = "Female";

    public static final String TREATMENT_TYPE_1 = "2RHZE/4RH";
    public static final String TREATMENT_TYPE_2 = "2RHZ/4RH";
    public static final String TREATMENT_TYPE_3 = "2SRHZE/1RHZE/5RHE";
    public static final String TREATMENT_TYPE_4 = "2HRZE/10RH";
    public static final String TREATMENT_TYPE_5 = "3RHZE/5RHE";

    public static final String TB_NEGATIVE = "Negative";
    public static final String TB_SCANTY = "Scanty";
    public static final String TB_1_PLUS = "1+";
    public static final String TB_2_PLUS = "2+";
    public static final String TB_3_PLUS = "3+";

    public static final String MATOKEO_AMEPONA = "Amepona";
    public static final String MATOKEO_AMEMALIZA_TIBA = "Amemaliza tiba";
    public static final String MATOKEO_AMEFARIKI = "Amefariki";
    public static final String MATOKEO_AMEHAMA = "Amehama";
    public static final String MATOKEO_AMETOROKA = "Ametoroka";
    public static final String MATOKEO_HAKUPONA = "Hakupona (Failure)";

    /** Database Names **/
    public static final String DEVICE_REGISTRATION_ID = "device_registration_id";

    public static final int REFERRAL_STATUS_NEW = 0;
    public static final int REFERRAL_STATUS_COMPLETED = 1;
    public static final int REFERRAL_STATUS_REJECTED = -1;

    public static final String POST_DATA_TYPE_REFERRAL = "r";
    public static final String POST_DATA_REFERRAL_FEEDBACK = "rf";
    public static final String POST_DATA_TYPE_PATIENT = "p";
    public static final String POST_DATA_TYPE_TB_PATIENT = "tp";
    public static final String POST_DATA_TYPE_ENCOUNTER = "e";

    public static final int ENTRY_NOT_SYNCED = 0;
    public static final int ENTRY_SYNCED = 1;

    public static final String HIV_SERVICE = "CTC";
    public static final String TB_SERVICE = "Kifua Kikuu";
    public static final String MALARIA_SERVICE = "Malaria";

    public static final int OPD_SERVICE_ID = 0;
    public static final int HIV_SERVICE_ID = 2;
    public static final int TB_SERVICE_ID = 1;
    public static final int MALARIA_SERVICE_ID = 3;

    public static final int CHW_TO_FACILITY = 1;
    public static final int INTRAFACILITY = 2;
    public static final int INTERFACILITY = 3;
    public static final int FACILITY_TO_CHW = 4;

    public static final int SOURCE_CHW = 0;
    public static final int SOURCE_HF = 1;


    public static int getReferralStatusValue(String statusString){
        if (statusString.equals(STATUS_NEW)){
            return REFERRAL_STATUS_NEW;
        }else if (statusString.equals(STATUS_COMPLETED)){
            return REFERRAL_STATUS_COMPLETED;
        }else {
            return REFERRAL_STATUS_REJECTED;
        }
    }

}