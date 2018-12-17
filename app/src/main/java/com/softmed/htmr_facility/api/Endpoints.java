package com.softmed.htmr_facility.api;

import java.util.List;

import com.softmed.htmr_facility.dom.objects.HealthFacilities;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.Referral;
import com.softmed.htmr_facility.dom.objects.ReferralServiceIndicatorsResponse;
import com.softmed.htmr_facility.dom.objects.TbEncounters;
import com.softmed.htmr_facility.dom.responces.LoginResponse;
import com.softmed.htmr_facility.dom.responces.ReferalResponce;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by issy on 11/23/17.
 */

public class Endpoints {

    private String HFUUID = "";

    public interface LoginService {

        @POST("security/authenticate/")
        Call<LoginResponse> basicLogin();

    }

    public interface ReferalService {

        @GET("all-patients-referrals")
        Call<List<ReferalResponce>> getHfReferrals();

        @GET("get-facility-referrals/{facilityUUID}")
        Call<List<ReferalResponce>> getHealthFacilityReferrals(@Path("facilityUUID") String facilityUUID );

        @POST("save-facility-referral")
        Call<Referral> postReferral(@Body RequestBody r);

        @POST("receive-feedback")
        Call<String> sendReferralFeedback(@Body RequestBody f);

        @GET("boresha-afya-services")
        Call<List<ReferralServiceIndicatorsResponse>> getAllServices();

        @GET("get-health-facilities")
        Call<List<HealthFacilities>> getHealthFacilities();

    }

    public interface PatientServices{

        @POST("save-patients")
        Call<Patient> postPatient(@Body RequestBody p);

        @POST("save-tb-encounters")
        Call<TbEncounters> postEncounter(@Body RequestBody e);

    }

    public interface NotificationServices{

        @POST("save-push-notification-token")
        Call<String> registerDevice(@Body RequestBody u);

    }

}
