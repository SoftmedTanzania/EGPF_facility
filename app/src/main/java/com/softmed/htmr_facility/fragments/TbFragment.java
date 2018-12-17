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
import com.softmed.htmr_facility.activities.ReferedClientsActivity;
import com.softmed.htmr_facility.activities.TbClientListActivity;
import com.softmed.htmr_facility.activities.TbReferalListActivity;
import com.softmed.htmr_facility.activities.ClientRegisterActivity;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.utils.constants;

import static com.softmed.htmr_facility.utils.constants.CHW_TO_FACILITY;
import static com.softmed.htmr_facility.utils.constants.INTERFACILITY;
import static com.softmed.htmr_facility.utils.constants.INTRAFACILITY;
import static com.softmed.htmr_facility.utils.constants.TB_SERVICE_ID;

/**
 * Created by issy on 12/4/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class TbFragment extends Fragment {

    private CardView referalListCard, newReferalsCard, referredListCard, tbRegister;
    private TextView referalCountText, referalFeedbackCount, toolbarTitle, chwReferralCounts, hfReferralCount;

    AppDatabase database;

    public TbFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getDatabase(TbFragment.this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView    = inflater.inflate(R.layout.fragment_tb, container, false);
        setUpView(rootView);

        new ReferalCountsTask().execute();

        referalListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TbFragment.this.getActivity(), TbReferalListActivity.class));
            }
        });

        newReferalsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TbFragment.this.getActivity(), TbClientListActivity.class));
            }
        });


        referredListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TbFragment.this.getActivity(), ReferedClientsActivity.class);
                intent.putExtra("service_id",  constants.TB_SERVICE_ID);
                startActivity(intent); //TODO: Implement Service Id in querying List of referred clients
            }
        });

        tbRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TbFragment.this.getActivity(), ClientRegisterActivity.class);
                intent.putExtra("isTbClient", true);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void setUpView(View v){
        referalListCard = (CardView) v.findViewById(R.id.tb_referal_list_card);
        newReferalsCard = (CardView) v.findViewById(R.id.tb_new_referals_card);
        referredListCard = (CardView) v.findViewById(R.id.tb_refered_clients_card);
        tbRegister = (CardView) v.findViewById(R.id.tb_register);

        referalCountText = (TextView) v.findViewById(R.id.tb_referal_count_text);
        chwReferralCounts = (TextView) v.findViewById(R.id.tb_chw_referal_count_text);
        hfReferralCount = (TextView) v.findViewById(R.id.tb_hf_referal_count_text);
        referalFeedbackCount = (TextView) v.findViewById(R.id.tb_referal_feedback_count);

    }

    private class ReferalCountsTask extends AsyncTask<Void, Void, Void> {

        String referralCounts = "";
        String chwCount = "";
        String hfCount = "";
        String feedbackCount = "";

        @Override
        protected Void doInBackground(Void... voids) {
            referralCounts = database.referalModel().geCounttUnattendedReferalsByService(TB_SERVICE_ID)+" Rufaa Mpya";
            chwCount = "CHW : "+database.referalModel().getCountReferralsByType(TB_SERVICE_ID, new int[]{CHW_TO_FACILITY});
            hfCount = "Kituo cha Afya : "+database.referalModel().getCountReferralsByType(TB_SERVICE_ID, new int[] {INTERFACILITY, INTRAFACILITY});
            feedbackCount = "Zinazosubiri Majibu : "+database.referalModel().geCountPendingReferalFeedback(TB_SERVICE_ID, BaseActivity.getThisFacilityId());
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
