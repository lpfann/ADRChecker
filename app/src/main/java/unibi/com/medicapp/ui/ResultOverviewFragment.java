package unibi.com.medicapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.otto.Bus;

import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.BusProvider;
import unibi.com.medicapp.controller.QueryDatabase;
import unibi.com.medicapp.model.ButtonClickedEvent;
import unibi.com.medicapp.model.Substance;


public class ResultOverviewFragment extends android.support.v4.app.Fragment {
    public static final String ENZ_RESULT = "ENZ_RESULT";
    private static final String DRUG_RESULT = "DRUG_RESULT";
    @InjectView(R.id.enzymecard)
    FrameLayout enzymecardView;
    @InjectView(R.id.drugcard)
    FrameLayout drugcardView;
    @InjectView(R.id.enzymResultNumTextView)
    TextView enzymeResultView;
    @InjectView(R.id.drugResultTextView)
    TextView drugResultView;
    @InjectView(R.id.save_fab)
    FloatingActionButton save_fab;

    private int enz_result;

    private int drug_result;
    private LinkedList<Substance> mSubstances;
    private QueryDatabase db;
    private SubstanceListAdapter mAdapter;
    private Bus mBus;
    public ResultOverviewFragment() {
        // Required empty public constructor
    }

    public static ResultOverviewFragment newInstance(int enz_result, int drug_result) {
        ResultOverviewFragment fragment = new ResultOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ENZ_RESULT, enz_result);
        args.putInt(DRUG_RESULT, drug_result);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.enzymecard)
    void showEnzymeResults() {
        mBus.post(new ButtonClickedEvent(ButtonClickedEvent.ENZYME_CARD_CLICKED));
    }

    @OnClick(R.id.drugcard)
    void showDrugResults() {
        mBus.post(new ButtonClickedEvent(ButtonClickedEvent.DRUG_CARD_CLICKED));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        enz_result = bundle.getInt(ENZ_RESULT, 0);
        drug_result = bundle.getInt(DRUG_RESULT, 0);

        mBus = BusProvider.getInstance();
        setHasOptionsMenu(true);

    }

    @OnClick(R.id.save_fab)
    void saveFabClicked() {
        mBus.post(new ButtonClickedEvent(ButtonClickedEvent.SAVE_FAB_CLICKED));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.result_overview_layout, container, false);
        ButterKnife.inject(this, v);
        enzymeResultView.setText(Integer.toOctalString(enz_result));
        drugResultView.setText(Integer.toOctalString(drug_result));
        save_fab.setImageDrawable(new IconicsDrawable(v.getContext(), GoogleMaterial.Icon.gmd_save).color(v.getContext().getResources().getColor(R.color.icons)).sizeDp(42));
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        db = QueryDatabase.getInstance(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_commit_selection) {
            mBus.post(new ButtonClickedEvent(ButtonClickedEvent.GET_DATA_SEARCH_SUBSTANCE_FRAGMENT));
        }
        return super.onOptionsItemSelected(item);
    }

    public LinkedList<Substance> getAgents() {
        return (LinkedList<Substance>) mSubstances.clone();
    }

    public void setAgents(LinkedList<Substance> substances) {
        this.mSubstances = (LinkedList<Substance>) substances.clone();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
