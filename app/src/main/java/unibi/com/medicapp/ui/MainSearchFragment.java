package unibi.com.medicapp.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.Icicle;
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.BusProvider;
import unibi.com.medicapp.controller.DatabaseHelperClass;
import unibi.com.medicapp.model.ButtonClickedEvent;
import unibi.com.medicapp.model.Enzyme;
import unibi.com.medicapp.model.Substance;

/**
 * Most important Fragment!
 * Displays List for Substances and Enzymes and enables the user to create his query.
 * Delegates tasks to AutoCompleteSearchFragment to fill the Substance list
 * and starts the Search by passing the data to the MainActivity
 */
public class MainSearchFragment extends Fragment {

    Bus mBus;
    EnzymeCursorAdapter adapter;
    /**
     * Enzyme IDs which the user selected
     */
    @Icicle
    ArrayList<Enzyme> selectedEnzymeIDs;
    /**
     * Enzyme List
     */
    @InjectView(R.id.enzymeListView)
    ListView enzymeListView;
    /**
     * Substance List
     */
    @InjectView(R.id.selectedAgentsList)
    android.support.v7.widget.RecyclerView substanceListView;
    /**
     * View to show message when no Substance was added.
     */
    @InjectView(R.id.empty_view)
    TextView emptyView;
    /**
     * Floating Action Button for start of the search which is prominently displayed as the main action of this fragment.
     */
    @InjectView(R.id.search_fab)
    FloatingActionButton search_fab;
    /**
     * Button which is associated with the Substance List to open the AutoComplete Fragment
     * see @AutoCompleteSearchFragment
     */
    @InjectView(R.id.add_substanceButton)
    Button add_Button;
    Cursor enzymeCursor;
    SparseBooleanArray selectedItemsInList;
    /**
     * selected Substances which are retrieved from the AutoComplete Fragment
     */
    @Icicle
    LinkedList<Substance> mSelectedSubstances;
    private SubstanceListAdapter substanceadapter;
    private DatabaseHelperClass db;


    public MainSearchFragment() {
        // Required empty public constructor
    }

    public static MainSearchFragment newInstance() {
        MainSearchFragment fragment = new MainSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.add_substanceButton)
    void addSubstanceButtonClick() {
        // Delegate Bus event to MainActivity to start the SearchFragment from there
        mBus.post(new ButtonClickedEvent(ButtonClickedEvent.ADD_SUBSTANCE_BUTTON));
    }

    @OnClick(R.id.search_fab)
    void searchAction() {
        // Delegate Bus event to MainActivity to start the Search
        mBus.post(new ButtonClickedEvent(ButtonClickedEvent.START_SEARCH));
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setHasOptionsMenu(true);
        // Check if there are existing lists (eg. when loading a saved Query)
        if (mSelectedSubstances == null) {
            mSelectedSubstances = new LinkedList<>();
        }
        if (selectedEnzymeIDs == null) {
            selectedEnzymeIDs = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_search, container, false);
        ButterKnife.inject(this, v);

        initEnzymeList();
        initSubstanceList();
        search_fab.setImageDrawable(new IconicsDrawable(v.getContext(), GoogleMaterial.Icon.gmd_search).color(v.getContext().getResources().getColor(R.color.icons)).sizeDp(34));
        return v;
    }

    /**
     * Init RecyclerView for the Substance List
     */
    private void initSubstanceList() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        substanceListView.setLayoutManager(mLayoutManager);
        substanceListView.setHasFixedSize(true);
        substanceadapter = new SubstanceListAdapter(mSelectedSubstances);
        substanceListView.setAdapter(substanceadapter);
        // Show empty view with a message when nothing is in the list
        if (mSelectedSubstances.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            substanceListView.setVisibility(View.GONE);
            add_Button.setText(R.string.add);
        } else {
            substanceListView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            add_Button.setText(R.string.edit);
        }


    }

    /**
     * Retrieves the selected Enzymes in the list for the main search
     *
     * @return checked Enzyme IDs
     */
    public ArrayList<Enzyme> getCheckedEnzymes() {
        long[] checkedItemIds = enzymeListView.getCheckedItemIds();
        selectedEnzymeIDs = new ArrayList<>();
        for (long checkedItemId : checkedItemIds) {
            Cursor c = db.getEnzyme(checkedItemId);
            Enzyme enz = new Enzyme(c.getString(c.getColumnIndex(DatabaseHelperClass.ISOENZYME.NAME)), checkedItemId);
            selectedEnzymeIDs.add(enz);
        }
        return selectedEnzymeIDs;

    }

    /**
     * Inits List View for the Enzymes
     */
    private void initEnzymeList() {
        if (enzymeCursor == null) {
            enzymeCursor = db.getAllEnzymes();
        }

        final int[] to = new int[]{android.R.id.text1};
        final String[] from = new String[]{"name"};

        // Using Adapter for a Multiple Choice List
        adapter = new EnzymeCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_multiple_choice,
                enzymeCursor,
                from,
                to,
                0, selectedEnzymeIDs);
        enzymeListView.setAdapter(adapter);


    }

    /**
     * Clear all InputData
     */
    public void clearForms() {
        mSelectedSubstances = new LinkedList<>();
        selectedEnzymeIDs = new ArrayList<>();
        initEnzymeList();
        initSubstanceList();
    }

    public void setSubstances(LinkedList<Substance> substances) {
        mSelectedSubstances = substances;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Retrieve Bus and DB instances when deleted
        if (activity != null) {
            mBus = BusProvider.getInstance();
            db = DatabaseHelperClass.getInstance(getActivity());
        }

    }

    @Override
    public void onPause() {
        selectedItemsInList = enzymeListView.getCheckedItemPositions();
        mBus.post(new ButtonClickedEvent(ButtonClickedEvent.SAVE_SELECTED_ENZYMES));
        super.onPause();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        // Add Item for the Clear Action to remove all Input data
        MenuItem clearMenuAction = menu.findItem(R.id.action_clear);
        clearMenuAction.setIcon(new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_receipt).sizeDp(24).color(getResources().getColor(R.color.icons)));
        clearMenuAction.setTitle(getResources().getString(R.string.clear));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }


    public void setSelectedEnzymeIDs(ArrayList<Enzyme> selectedEnzymeIDs) {
        this.selectedEnzymeIDs = (ArrayList<Enzyme>) selectedEnzymeIDs.clone();
    }

    /**
     * Getter for the Substance List View.
     * Is used to have a shared Element for a Fragment Transition in the new Lollipop Design Framework.
     *
     * @return SubstanceListview
     */
    public RecyclerView getSubstanceListView() {
        return substanceListView;
    }
}
