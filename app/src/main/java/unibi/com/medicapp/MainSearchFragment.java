package unibi.com.medicapp;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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


public class MainSearchFragment extends Fragment {

    Bus mBus;
    SimpleCursorAdapter adapter;
    @Icicle
    ArrayList<Enzyme> selectedEnzymeIDs;
    @InjectView(R.id.enzymeListView)
    ListView enzymeListView;
    @InjectView(R.id.selectedAgentsList)
    android.support.v7.widget.RecyclerView substanceListView;
    @InjectView(R.id.empty_view)
    TextView emptyView;
    @InjectView(R.id.search_fab)
    FloatingActionButton search_fab;

    @InjectView(R.id.add_substanceButton)
    Button add_Button;

    Cursor enzymeCursor;
    SparseBooleanArray selectedItemsInList;
    @Icicle
    LinkedList<Agent> mSelectedAgents;
    private QueryDatabase db;
    private AgentsListAdapter substanceadapter;


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
        //getCheckedItems();
        mBus.post(new ButtonClickedEvent(ButtonClickedEvent.ADD_SUBSTANCE_BUTTON));
    }

    @OnClick(R.id.search_fab)
    void searchAction() {
        mBus.post(new ButtonClickedEvent(ButtonClickedEvent.START_SEARCH));
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setHasOptionsMenu(true);
        if (mSelectedAgents == null) {
            mSelectedAgents = new LinkedList<>();
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

    private void initSubstanceList() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        substanceListView.setLayoutManager(mLayoutManager);
        substanceListView.setHasFixedSize(true);
        // Custom Decorator fuer Trennlinien, funzt momentan nicht im Fragment
//        substanceListView.addItemDecoration(new DividerItemDecoration(v.getContext(), 1));
        substanceadapter = new AgentsListAdapter(mSelectedAgents);
        substanceListView.setAdapter(substanceadapter);
        if (mSelectedAgents.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            substanceListView.setVisibility(View.GONE);
            add_Button.setText(R.string.add);
        } else {
            substanceListView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            add_Button.setText(R.string.edit);
        }


    }

    public ArrayList<Enzyme> getCheckedItems() {
        SparseBooleanArray checked = enzymeListView.getCheckedItemPositions();
        selectedEnzymeIDs = new ArrayList<>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            Cursor c = adapter.getCursor();
            if (checked.valueAt(i)) {
                c.moveToPosition(position);
                Enzyme enz = new Enzyme(c.getString(1), c.getLong(0));
                selectedEnzymeIDs.add(enz);
            }
        }
        return selectedEnzymeIDs;

    }


    private void initEnzymeList() {
        if (enzymeCursor == null) {
            enzymeCursor = db.getAllEnzymes();
        }

        final int[] to = new int[]{android.R.id.text1};
        final String[] from = new String[]{"name"};
        adapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_multiple_choice,
                enzymeCursor,
                from,
                to,
                0);
        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                final int colIndex = cursor.getColumnIndexOrThrow("name");
                return cursor.getString(colIndex);
            }
        });
        if (selectedEnzymeIDs != null) {
            for (Enzyme i : selectedEnzymeIDs) {
                enzymeListView.setItemChecked(i.id.intValue(), true);

            }

        }
        enzymeListView.setAdapter(adapter);


    }

    public void setSubstances(LinkedList<Agent> agents) {
        mSelectedAgents = agents;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity != null) {
            mBus = BusProvider.getInstance();
            db = QueryDatabase.getInstance(getActivity());
        }

    }

    @Override
    public void onPause() {
        selectedItemsInList = enzymeListView.getCheckedItemPositions();
        mBus.post(new ButtonClickedEvent(ButtonClickedEvent.SAVE_SELECTED_ENZYMES));
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
/*        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchbutton = menu.findItem(R.id.action_search);
        searchbutton.setIcon(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_search).sizeDp(24).color(getResources().getColor(R.color.icons)));
        searchbutton.setTitle(getResources().getString(R.string.search));*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public ArrayList<Enzyme> getSelectedEnzymeIDs() {
        return (ArrayList<Enzyme>) selectedEnzymeIDs.clone();
    }

    public void setSelectedEnzymeIDs(ArrayList<Enzyme> selectedEnzymeIDs) {
        this.selectedEnzymeIDs = (ArrayList<Enzyme>) selectedEnzymeIDs.clone();
    }

    public RecyclerView getSubstanceListView() {
        return substanceListView;
    }
}
