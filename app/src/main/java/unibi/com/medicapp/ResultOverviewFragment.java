package unibi.com.medicapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.squareup.otto.Bus;

import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ResultOverviewFragment extends android.support.v4.app.Fragment {

    @InjectView(R.id.enzymecard)
    FrameLayout enzymecardView;
    @InjectView(R.id.drugcard)
    FrameLayout drugcardView;
    private LinkedList<Substance> mSubstances;
    private QueryDatabase db;
    private SubstanceListAdapter mAdapter;
    private Bus mBus;
    public ResultOverviewFragment() {
        // Required empty public constructor
    }

    public static ResultOverviewFragment newInstance() {
        ResultOverviewFragment fragment = new ResultOverviewFragment();
        Bundle args = new Bundle();
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

        mBus = BusProvider.getInstance();
        setHasOptionsMenu(true);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.result_overview_layout, container, false);
        ButterKnife.inject(this, v);

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_substance_search, menu);
        //menu.findItem(R.id.action_search).setVisible(false);
        //menu.findItem(R.id.action_commit_selection).setIcon(new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_save).sizeDp(24).color(getResources().getColor(R.color.icons)));

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

    public LinkedList<Substance> getSubstances() {
        return (LinkedList<Substance>) mSubstances.clone();
    }

    public void setSubstances(LinkedList<Substance> substances) {
        this.mSubstances = (LinkedList<Substance>) substances.clone();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
