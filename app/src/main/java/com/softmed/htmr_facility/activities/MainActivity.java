package com.softmed.htmr_facility.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;

import static com.softmed.htmr_facility.utils.SessionManager.KEY_NAME;
import static com.softmed.htmr_facility.utils.constants.HIV_SERVICE_ID;

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView referalListText, referedClientsText, newReferalText, hivText, tbText, tbReferalListText, tbReferedClientsText, tbReferNewClientsText;
    private TextView referalCountText, referalFeedbackCount, toolbarTitle;
    private CardView referalListCard, referedClientsCard, newReferalsCard;
    private ImageView tbReferalListIcon, tbReferedClientsIcon, tbNewReferalsIcon;

    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupviews();

        //session.checkLogin();

        database = AppDatabase.getDatabase(this);

        if (session.isLoggedIn()){
            toolbarTitle.setText("Karibu "+session.getUserDetails().get(KEY_NAME));
        }

        //referalCountText.setText();
        ReferalCountsTask referalCountsTask = new ReferalCountsTask();
        referalCountsTask.execute();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

        referalListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ReferalListActivityOld.class));
            }
        });

        referedClientsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ReferedClientsActivity.class));
            }
        });

        newReferalsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewReferalsActivity.class));
            }
        });

    }

    private void setupviews(){

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        referalCountText = (TextView) findViewById(R.id.referal_count_text);
        referalCountText.setTypeface(Avenir);

        referalFeedbackCount = (TextView) findViewById(R.id.referal_feedback_count);
        referalFeedbackCount.setTypeface(Avenir);

        tbReferalListText = (TextView) findViewById(R.id.tb_referal_list_text);
        tbReferalListText.setTypeface(Julius);

        tbReferedClientsText = (TextView) findViewById(R.id.tb_refered_clients_text);
        tbReferedClientsText.setTypeface(Julius);

        tbReferNewClientsText = (TextView) findViewById(R.id.tb_new_referals_text);
        tbReferNewClientsText.setTypeface(Julius);

        tbNewReferalsIcon = (ImageView) findViewById(R.id.tb_refer_new_client);
        tbNewReferalsIcon.setColorFilter(getResources().getColor(R.color.card_grid_tex));

        tbReferalListIcon = (ImageView) findViewById(R.id.tb_referals_icon);
        tbReferalListIcon.setColorFilter(getResources().getColor(R.color.card_grid_tex));

        tbReferedClientsIcon = (ImageView) findViewById(R.id.tb_refered_clients_icon);
        tbReferedClientsIcon.setColorFilter(getResources().getColor(R.color.card_grid_tex));

        hivText = (TextView) findViewById(R.id.hiv_text);
        hivText.setTypeface(RobotoCondencedItalic);

        tbText = (TextView) findViewById(R.id.tb_text);
        tbText.setTypeface(RobotoCondencedItalic);

        referalListText = (TextView) findViewById(R.id.referal_list_text);
        referalListText.setTypeface(Julius);
        referedClientsText = (TextView) findViewById(R.id.refered_clients_text);
        referedClientsText.setTypeface(Julius);
        newReferalText = (TextView) findViewById(R.id.new_referals_text);
        newReferalText.setTypeface(Julius);

        referedClientsCard = (CardView) findViewById(R.id.refered_clients_card);

        referalListCard = (CardView) findViewById(R.id.referal_list_card);

        newReferalsCard = (CardView) findViewById(R.id.new_referals_card);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout){
            session.logoutUser();
        }

        View menuItemView = findViewById(R.id.notifications); // SAME ID AS MENU ID
        PopupMenu popupMenu = new PopupMenu(this, menuItemView);
        popupMenu.inflate(R.menu.notification_list);
        // ...
        popupMenu.show();
        // ...
        return true;
    }

    private class ReferalCountsTask extends AsyncTask<Void, Void, Void> {

        String referralCounts = "";

        @Override
        protected Void doInBackground(Void... voids) {
            referralCounts = database.referalModel().geCounttUnattendedReferalsByService(HIV_SERVICE_ID)+" New referrals unattended";
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            referalCountText.setText(referralCounts);
            super.onPostExecute(aVoid);
        }

    }

}

