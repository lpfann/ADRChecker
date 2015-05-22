package unibi.com.medicapp;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.FontAwesome;
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
    ArrayList<Integer> selectedEnzymeIDs;
    @InjectView(R.id.enzymeListView)
    ListView enzymeListView;
    @InjectView(R.id.selectedSubstanceList)
    android.support.v7.widget.RecyclerView substanceListView;
    @InjectView(R.id.empty_view)
    TextView emptyView;

    Cursor enzymeCursor;
    SparseBooleanArray selectedItemsInList;
    @Icicle
    LinkedList<Substance> selectedSubstances;
    private QueryDatabase db;
    private SubstanceListAdapter substanceadapter;


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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setHasOptionsMenu(true);
        if (selectedSubstances == null) {
            selectedSubstances = new LinkedList<>();
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
        return v;
    }

    private void initSubstanceList() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        substanceListView.setLayoutManager(mLayoutManager);
        substanceListView.setHasFixedSize(true);
        // Custom Decorator fuer Trennlinien, funzt momentan nicht im Fragment
//        substanceListView.addItemDecoration(new DividerItemDecoration(v.getContext(), 1));
        substanceadapter = new SubstanceListAdapter(selectedSubstances);
        substanceListView.setAdapter(substanceadapter);
        if (selectedSubstances.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            substanceListView.setVisibility(View.GONE);
        } else {
            substanceListView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }


    }

    public ArrayList<Integer> getCheckedItems() {
        SparseBooleanArray checked = enzymeListView.getCheckedItemPositions();
        selectedEnzymeIDs = new ArrayList<>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            Cursor c = adapter.getCursor();
            if (checked.valueAt(i)) {
                c.moveToPosition(i);
                selectedEnzymeIDs.add(c.getInt(1));
            }
        }
        return selectedEnzymeIDs;

    }


    private void initEnzymeList() {
        if (enzymeCursor == null) {
            enzymeCursor = db.getEnzymes();
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
            for (int i : selectedEnzymeIDs) {
                enzymeListView.setItemChecked(i, true);

            }

        }
        enzymeListView.setAdapter(adapter);


    }

    public void setSubstances(LinkedList<Substance> substances) {
        selectedSubstances = substances;

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
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchbutton = menu.findItem(R.id.action_search);
        searchbutton.setIcon(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_search).sizeDp(24).color(getResources().getColor(R.color.icons)));
        searchbutton.setTitle(getResources().getString(R.string.search));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public ArrayList<Integer> getSelectedEnzymeIDs() {
        return (ArrayList<Integer>) selectedEnzymeIDs.clone();
    }

    public void setSelectedEnzymeIDs(ArrayList<Integer> selectedEnzymeIDs) {
        this.selectedEnzymeIDs = (ArrayList<Integer>) selectedEnzymeIDs.clone();
    }

}
