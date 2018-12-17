package com.softmed.htmr_facility.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.adapters.HealthFacilitiesAdapter;
import com.softmed.htmr_facility.adapters.ServicesAdapter;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.HealthFacilities;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.PostOffice;
import com.softmed.htmr_facility.dom.objects.Referral;
import com.softmed.htmr_facility.dom.objects.ReferralIndicator;
import com.softmed.htmr_facility.dom.objects.ReferralServiceIndicators;
import fr.ganfra.materialspinner.MaterialSpinner;

import static com.softmed.htmr_facility.utils.SessionManager.KEY_UUID;
import static com.softmed.htmr_facility.utils.constants.ENTRY_NOT_SYNCED;
import static com.softmed.htmr_facility.utils.constants.FACILITY_TO_CHW;
import static com.softmed.htmr_facility.utils.constants.INTERFACILITY;
import static com.softmed.htmr_facility.utils.constants.INTRAFACILITY;
import static com.softmed.htmr_facility.utils.constants.POST_DATA_TYPE_REFERRAL;
import static com.softmed.htmr_facility.utils.constants.REFERRAL_STATUS_NEW;

/**
 * Created by issy on 1/6/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class IssueReferralDialogueFragment extends DialogFragment{

    private TextView patientNames;
    private MaterialSpinner spinnerService, spinnerToHealthFacility, spinnerReferralDestination;
    private EditText referralReasons, otherClinicalInformation;
    private Button cancelButton, issueButton;
    private RecyclerView indicatorsRecycler;

    private Patient currentPatient;
    private String referralReasonsValue, otherClinicalInformationValue, toHealthFacilityID;
    private int serviceID;
    private static final String TAG = "IssueReferralDialogueFragment";
    private List<ReferralIndicator> selectedIndicators = new ArrayList<>();

    private AppDatabase database;

    private HealthFacilitiesAdapter healthFacilitiesAdapter;
    List<HealthFacilities> healthFacilities = new ArrayList<>();

    private mAdapter madapter;
    List<String> destinations = new ArrayList<>();

    private ServicesAdapter servicesAdapter;
    private int sourceService;
    List<ReferralServiceIndicators> referralServiceIndicators = new ArrayList<>();

    private int referralType;

    public IssueReferralDialogueFragment() {}

    public static IssueReferralDialogueFragment newInstance(Patient patient, int sourceServiceId) {
        Bundle args = new Bundle();
        IssueReferralDialogueFragment fragment = new IssueReferralDialogueFragment();
        args.putSerializable("currentPatient", patient);
        args.putInt("sourceID", sourceServiceId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_dialogue_layout, container);
        database = AppDatabase.getDatabase(this.getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //this.getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        currentPatient = (Patient) getArguments().getSerializable("currentPatient");
        sourceService = getArguments().getInt("sourceID");

        setupviews(view);

        if (currentPatient != null){
            patientNames.setText(
                    currentPatient.getPatientFirstName()+" "+
                    currentPatient.getPatientMiddleName()+" "+
                    currentPatient.getPatientSurname());
        }

        healthFacilitiesAdapter = new HealthFacilitiesAdapter(this.getContext(),R.layout.subscription_plan_items_drop_down, healthFacilities);
        servicesAdapter = new ServicesAdapter(this.getContext(), R.layout.subscription_plan_items_drop_down , referralServiceIndicators);

        destinations.add("CHW");
        destinations.add("Health Facility");

        madapter = new mAdapter(IssueReferralDialogueFragment.this.getActivity(),R.layout.subscription_plan_items_drop_down, destinations);
        spinnerReferralDestination.setAdapter(madapter);

        new getFacilitiesAndServices(this.getContext()).execute();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        issueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getCurrentInputs()){
                    createReferralObject();
                }
            }
        });

        spinnerReferralDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == -1){

                }else if (i==0){
                    spinnerToHealthFacility.setEnabled(false);
                    spinnerToHealthFacility.setSelection(0);
                    referralType = FACILITY_TO_CHW;
                }else if (i == 1){
                    spinnerToHealthFacility.setEnabled(true);
                    referralType = INTERFACILITY;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*TODO
                * Get the selected sourceService
                * Query all the indicators associated with that sourceService
                * display indicators
                * */

                if (i != -1){
                    selectedIndicators.clear();
                    ReferralServiceIndicators service = (ReferralServiceIndicators) adapterView.getSelectedItem();
                    new getServiceIndicator(database).execute(service.getServiceId());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerToHealthFacility.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("IssueReferral", i+" : to health Facility");
                HealthFacilities hf = (HealthFacilities)spinnerToHealthFacility.getSelectedItem();
                if (i!=-1){
                    if (hf.getOpenMRSUIID().equals(BaseActivity.getThisFacilityId())){
                        referralType = INTRAFACILITY;
                    }else {
                        referralType = INTERFACILITY;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void createReferralObject(){

        Referral referral = new Referral();
        List<Long> indicatorIDs = new ArrayList<>();

        long range = 1234567L;
        Random r = new Random();
        long number = (long)(r.nextDouble()*range);

        long today = Calendar.getInstance().getTimeInMillis();

        referral.setReferral_id(number+"");
        referral.setId(number);
        referral.setPatient_id(currentPatient.getPatientId());
        referral.setCommunityBasedHivService("");
        referral.setReferralReason(referralReasonsValue);
        referral.setServiceId(serviceID);
        referral.setReferralUUID(UUID.randomUUID()+"");
        referral.setCtcNumber("");
        referral.setServiceProviderUIID(BaseActivity.session.getUserDetails().get(KEY_UUID));
        referral.setServiceProviderGroup("");
        referral.setVillageLeader("");

        referral.setReferralSource(sourceService);
        referral.setReferralType(referralType);

        referral.setReferralDate(today);
        referral.setFacilityId(toHealthFacilityID);
        referral.setFromFacilityId(BaseActivity.session.getKeyHfid());
        referral.setReferralStatus(REFERRAL_STATUS_NEW);
        referral.setOtherClinicalInformation(otherClinicalInformationValue);

        for (ReferralIndicator indicator : selectedIndicators){
            indicatorIDs.add(indicator.getReferralServiceIndicatorId());
        }

        referral.setServiceIndicatorIds(indicatorIDs);

        SaveReferral saveReferral = new SaveReferral(database);
        saveReferral.execute(referral);

    }

    private boolean getCurrentInputs(){
        Log.d("IssueReferral", spinnerService.getSelectedItemPosition()+" Service Selected Position");
        Log.d("IssueReferral", spinnerToHealthFacility.getSelectedItemPosition()+" To Facility Selected Position");
        if (spinnerService.getSelectedItemPosition() == 0){
            toastThis("Chagua huduma ya kutoa rufaa");
            return false;
        }else {
            ReferralServiceIndicators services = (ReferralServiceIndicators) spinnerService.getSelectedItem();
            serviceID = Integer.parseInt(services.getServiceId()+"");
        }

        if (spinnerReferralDestination.getSelectedItemPosition() == 2) {
            if (spinnerToHealthFacility.getSelectedItemPosition() == 0) {
                toastThis("Chagua Kituo cha afya cha kutuma rufaa");
                return false;
            } else {
                HealthFacilities hf = (HealthFacilities) spinnerToHealthFacility.getSelectedItem();
                toHealthFacilityID = hf.getOpenMRSUIID();
            }
        }else if (spinnerReferralDestination.getSelectedItemPosition() == 0){
            toastThis("Chagua ruffaa inapokwenda kabla ya kuendelea");
            return false;
        }else{
            //TODO referral destination selected to go to CHW, handle the implementation accordingly
            return  true;
        }

        if (referralReasons.getText().toString().isEmpty()){
            toastThis("Tafadhali andika sababu za rufaa");
            return false;
        }else {
            referralReasonsValue = referralReasons.getText().toString();
        }

        otherClinicalInformationValue = otherClinicalInformation.getText().toString();
        return true;

    }

    private void toastThis(String message){
        Toast.makeText(IssueReferralDialogueFragment.this.getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void setupviews(View v) {

        indicatorsRecycler = (RecyclerView) v.findViewById(R.id.indicators_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(IssueReferralDialogueFragment.this.getContext(), 3);
        indicatorsRecycler.setLayoutManager(layoutManager);
        indicatorsRecycler.setHasFixedSize(true);

        patientNames = (TextView) v.findViewById(R.id.patient_name);

        spinnerService = (MaterialSpinner) v.findViewById(R.id.spin_service);
        spinnerToHealthFacility = (MaterialSpinner) v.findViewById(R.id.spin_to_facility);
        spinnerReferralDestination = (MaterialSpinner) v.findViewById(R.id.spin_destination);

        referralReasons = (EditText) v.findViewById(R.id.referal_reasons_text);
        otherClinicalInformation = (EditText) v.findViewById(R.id.other_clinical_information_text);

        cancelButton = (Button) v.findViewById(R.id.cancel_button);
        issueButton = (Button) v.findViewById(R.id.tuma_button);

    }

    class IndicatorsRecyclerAdapter  extends RecyclerView.Adapter<IndicatorsViewHolder> {

        private List<ReferralIndicator> indicators = new ArrayList<>();
        private LayoutInflater mInflater;

        // data is passed into the constructor
        public IndicatorsRecyclerAdapter(Context context, List<ReferralIndicator> items) {
            this.mInflater = LayoutInflater.from(context);
            this.indicators = items;
        }

        // inflates the cell layout from xml when needed
        @Override
        public IssueReferralDialogueFragment.IndicatorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.indicator_grid_item, parent, false);
            IndicatorsViewHolder holder = new IndicatorsViewHolder(view);
            return holder;
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(IndicatorsViewHolder holder, int position) {
            ReferralIndicator indicator = indicators.get(position);
            holder.bindIndicator(indicator);
        }

        // total number of cells
        @Override
        public int getItemCount() {
            return indicators.size();
        }

        // convenience method for getting data at click position
        ReferralIndicator getItem(int id) {
            return indicators.get(id);
        }

    }

    class IndicatorsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView indicatorName;
        private RelativeLayout indicatorWrapper;
        private ReferralIndicator referralIndicator;
        private boolean isSelected = false;

        private IndicatorsViewHolder(View itemView) {
            super(itemView);
            indicatorName = (TextView) itemView.findViewById(R.id.indicator_name);
            indicatorWrapper = (RelativeLayout) itemView.findViewById(R.id.indicator_wrapper);
            itemView.setOnClickListener(this);
        }

        private void bindIndicator(ReferralIndicator indicator){
            this.referralIndicator = indicator;
            indicatorName.setText(referralIndicator.getIndicatorName());
        }

        @Override
        public void onClick(View view) {
            if (this.isSelected){
                isSelected = false;
                selectedIndicators.remove(referralIndicator);
                indicatorWrapper.setBackground(getResources().getDrawable(R.drawable.border_indicators_unselected));
            }else {
                isSelected = true;
                selectedIndicators.add(referralIndicator);
                indicatorWrapper.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }

    }

    class getServiceIndicator extends AsyncTask<Long, Void, Void>{

        AppDatabase database;
        List<ReferralIndicator> referralIndicators;

        getServiceIndicator(AppDatabase db){
            database = db;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            long serviceId = longs[0];
            List<ReferralIndicator> indicators = database.referralIndicatorDao().getIndicatorsByServiceId(serviceId);
            referralIndicators = indicators;
            //TODO: display the indicators
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            IndicatorsRecyclerAdapter adapter = new IndicatorsRecyclerAdapter(getContext(), referralIndicators);
            indicatorsRecycler.setAdapter(adapter);
        }
    }

    class getFacilitiesAndServices extends AsyncTask<Void, Void, Void>{

        List<HealthFacilities> hfs = new ArrayList<>();
        List<ReferralServiceIndicators> serviceIndicators = new ArrayList<>();
        Context context;

        getFacilitiesAndServices(Context ctx){
            this.context = ctx;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            healthFacilitiesAdapter = new HealthFacilitiesAdapter(context,R.layout.subscription_plan_items_drop_down, hfs);
            servicesAdapter = new ServicesAdapter(context, R.layout.subscription_plan_items_drop_down ,serviceIndicators);
            spinnerService.setAdapter(servicesAdapter);
            spinnerToHealthFacility.setAdapter(healthFacilitiesAdapter);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            hfs = database.healthFacilitiesModelDao().getAllHealthFacilities();
            serviceIndicators = database.referralServiceIndicatorsDao().getAllServices();

            return null;
        }
    }

    class SaveReferral extends AsyncTask<Referral, Void, Void>{

        AppDatabase database;

        SaveReferral(AppDatabase db){
            this.database = db;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            toastThis("Referral Stored Successfully");
            dismiss();
        }

        @Override
        protected Void doInBackground(Referral... referrals) {

            database.referalModel().addReferal(referrals[0]);
            List<Referral> x = database.referalModel().getAllReferralsOfThisFacility(BaseActivity.session.getKeyHfid());
            Log.d("AllReferrals", "All referrals : "+x.size());

            PostOffice postOffice = new PostOffice();
            postOffice.setPost_id(referrals[0].getReferral_id());
            postOffice.setPost_data_type(POST_DATA_TYPE_REFERRAL);
            postOffice.setSyncStatus(ENTRY_NOT_SYNCED);

            database.postOfficeModelDao().addPostEntry(postOffice);

            return null;
        }
    }

    class mAdapter extends ArrayAdapter<String> {

        List<String> items = new ArrayList<>();
        Context act;

        public mAdapter(Context context, int resource, List<String> mItems) {
            super(context, resource, mItems);
            this.items = mItems;
            act = context;
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            LayoutInflater vi = (LayoutInflater) act.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(R.layout.subscription_plan_items_drop_down, null);

            TextView tvTitle =(TextView)rowView.findViewById(R.id.rowtext);
            tvTitle.setText(items.get(position));

            return rowView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View rowView = convertView;
            LayoutInflater vi = (LayoutInflater) act.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(R.layout.single_text_spinner_view_item, null);

            TextView tvTitle = (TextView)rowView.findViewById(R.id.rowtext);
            tvTitle.setText(items.get(position));

            return rowView;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        public void updateItems(List<String> newItems){
            this.items = null;
            this.items = newItems;
            this.notifyDataSetChanged();
        }

    }

}
