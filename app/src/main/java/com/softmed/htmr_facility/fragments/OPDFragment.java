package com.softmed.htmr_facility.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.activities.ClientRegisterActivity;
import com.softmed.htmr_facility.activities.NewReferalsActivity;
import com.softmed.htmr_facility.activities.ReferedClientsActivity;
import com.softmed.htmr_facility.activities.ReferralListActivity;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;

import static com.softmed.htmr_facility.utils.constants.CHW_TO_FACILITY;
import static com.softmed.htmr_facility.utils.constants.HIV_SERVICE_ID;
import static com.softmed.htmr_facility.utils.constants.INTERFACILITY;
import static com.softmed.htmr_facility.utils.constants.INTRAFACILITY;
import static com.softmed.htmr_facility.utils.constants.OPD_SERVICE_ID;

/**
 * Created by issy on 12/4/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class OPDFragment extends Fragment {

    private CardView opdRegistrationCard, opdReferralListCard, opdClientListCard, opdReferedClientsCard;
    private TextView referalCountText, referalFeedbackCount, chwReferralCounts, hfReferralCount;

    private AppDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getDatabase(OPDFragment.this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView    = inflater.inflate(R.layout.fragment_opd, container, false);
        setUpView(rootView);

        new OPDFragment.ReferalCountsTask().execute();

        opdRegistrationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OPDFragment.this.getActivity(), ClientRegisterActivity.class);
                intent.putExtra("isTbClient", false);
                startActivity(intent);
            }
        });

        opdReferralListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OPDFragment.this.getActivity(), ReferralListActivity.class);
                intent.putExtra("service", OPD_SERVICE_ID);
                startActivity(intent);
            }
        });

        opdClientListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OPDFragment.this.getActivity(), NewReferalsActivity.class);
                intent.putExtra("service", OPD_SERVICE_ID);
                startActivity(intent);
            }
        });

        opdReferedClientsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OPDFragment.this.getActivity(), ReferedClientsActivity.class);
                intent.putExtra("service_id", OPD_SERVICE_ID);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void setUpView(View v){

        chwReferralCounts = (TextView) v.findViewById(R.id.chw_referal_count_text);
        hfReferralCount = (TextView) v.findViewById(R.id.hf_referal_count_text);
        referalCountText = (TextView) v.findViewById(R.id.referal_count_text);
        referalFeedbackCount = (TextView) v.findViewById(R.id.referal_feedback_count);

        opdRegistrationCard = (CardView) v.findViewById(R.id.opd_registration_card);
        opdReferralListCard = (CardView) v.findViewById(R.id.opd_referral_list_card);
        opdClientListCard   = (CardView) v.findViewById(R.id.opd_client_list_card);
        opdReferedClientsCard = (CardView) v.findViewById(R.id.refered_clients_card);
    }

    private class ReferalCountsTask extends AsyncTask<Void, Void, Void> {

        String referralCounts = "";
        String chwCount = "";
        String hfCount = "";
        String feedbackCount = "";

        @Override
        protected Void doInBackground(Void... voids) {
            referralCounts = database.referalModel().getCountReferralsByService(OPD_SERVICE_ID)+" Rufaa Mpya";
            chwCount = "CHW : "+database.referalModel().getCountReferralsBySource(new int[] {CHW_TO_FACILITY});
            hfCount = "Kituo cha Afya : "+database.referalModel().getCountReferralsBySource(new int[] {INTRAFACILITY, INTERFACILITY});
            feedbackCount = "Zinazosubiri Majibu : "+database.referalModel().geCountPendingReferalFeedback(HIV_SERVICE_ID, BaseActivity.session.getKeyHfid());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            referalCountText.setText(referralCounts);
            chwReferralCounts.setText(chwCount);
            hfReferralCount.setText(hfCount);
            referalFeedbackCount.setText(feedbackCount);
            super.onPostExecute(aVoid);
        }

    }

}
