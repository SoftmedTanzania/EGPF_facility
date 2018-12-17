package com.softmed.htmr_facility.fragments;

import android.content.Context;
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
import com.softmed.htmr_facility.activities.NewReferalsActivity;
import com.softmed.htmr_facility.activities.ReferedClientsActivity;
import com.softmed.htmr_facility.activities.ReferralListActivity;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;

import static com.softmed.htmr_facility.utils.constants.CHW_TO_FACILITY;
import static com.softmed.htmr_facility.utils.constants.HIV_SERVICE_ID;
import static com.softmed.htmr_facility.utils.constants.INTERFACILITY;
import static com.softmed.htmr_facility.utils.constants.INTRAFACILITY;

/**
 * Created by issy on 12/4/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class HivFragment extends Fragment {

    private TextView referalCountText, referalFeedbackCount, chwReferralCounts, hfReferralCount;
    private CardView referalListCard, referedClientsCard, newReferalsCard;

    private Context context;

    AppDatabase database;

    public HivFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getDatabase(HivFragment.this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView    = inflater.inflate(R.layout.fragment_hiv, container, false);

        setupviews(rootView);

        new ReferalCountsTask().execute();

        referalListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HivFragment.this.getActivity(), ReferralListActivity.class);
                intent.putExtra("service", HIV_SERVICE_ID);
                startActivity(intent);
            }
        });

        referedClientsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HivFragment.this.getActivity(), ReferedClientsActivity.class);
                intent.putExtra("service_id", HIV_SERVICE_ID);
                startActivity(intent);
            }
        });

        newReferalsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HivFragment.this.getActivity(), NewReferalsActivity.class);
                intent.putExtra("service", HIV_SERVICE_ID);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void setupviews(View view){

        chwReferralCounts = (TextView) view.findViewById(R.id.chw_referal_count_text);
        hfReferralCount = (TextView) view.findViewById(R.id.hf_referal_count_text);
        referalCountText = (TextView) view.findViewById(R.id.referal_count_text);
        referalFeedbackCount = (TextView) view.findViewById(R.id.referal_feedback_count);
        referedClientsCard = (CardView) view.findViewById(R.id.refered_clients_card);
        referalListCard = (CardView) view.findViewById(R.id.referal_list_card);
        newReferalsCard = (CardView) view.findViewById(R.id.new_referals_card);

    }

    private class ReferalCountsTask extends AsyncTask<Void, Void, Void> {

        String referralCounts = "";
        String feedbackCount = "";
        String chwCount = "";
        String hfCount = "";

        @Override
        protected Void doInBackground(Void... voids) {
            referralCounts = database.referalModel().geCounttUnattendedReferalsByService(HIV_SERVICE_ID)+" Rufaa Mpya";
            feedbackCount = "Zinazosubiri Majibu : "+database.referalModel().geCountPendingReferalFeedback(HIV_SERVICE_ID, BaseActivity.session.getKeyHfid());
            chwCount = "CHW : "+database.referalModel().getCountReferralsByType(HIV_SERVICE_ID, new int[]{CHW_TO_FACILITY});
            hfCount = "Kituo cha Afya : "+database.referalModel().getCountReferralsByType(HIV_SERVICE_ID, new int[] {INTERFACILITY, INTRAFACILITY});
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
