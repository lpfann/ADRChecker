package unibi.com.medicapp.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.otto.Bus;

import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icepick;
import icepick.Icicle;
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.BusProvider;
import unibi.com.medicapp.controller.QueryDatabase;
import unibi.com.medicapp.model.Agent;


public class AutoCompleteSearchFragment extends android.support.v4.app.Fragment {

    @InjectView(R.id.wirkstoffAutoComplete1)
    AutoCompleteTextView autocompleteWirkstoffView;
    @InjectView(R.id.wirkstoffListe)
    android.support.v7.widget.RecyclerView wirkstoffListeView;

    @Icicle
    LinkedList<Agent> mAgents;
    private QueryDatabase db;
    private AgentsListAdapter mAdapter;
    private Bus mBus;


    public AutoCompleteSearchFragment() {
        // Required empty public constructor
    }

    public static AutoCompleteSearchFragment newInstance() {
        AutoCompleteSearchFragment fragment = new AutoCompleteSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void initList(View v) {
        if (mAgents == null) {
            mAgents = new LinkedList<>();
        }
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        wirkstoffListeView.setLayoutManager(mLayoutManager);
        wirkstoffListeView.setHasFixedSize(true);
        // Custom Decorator fuer Trennlinien, funzt momentan nicht im Fragment
//        wirkstoffListeView.addItemDecoration(new DividerItemDecoration(v.getContext(), 1));
        mAdapter = new AgentsListAdapter(mAgents);
        wirkstoffListeView.setAdapter(mAdapter);
    }


    private void initializeAutoComplete(Context c) {

        final int[] to = new int[]{android.R.id.text1};
        final String[] from = new String[]{"Name"};
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(c,
                android.R.layout.simple_dropdown_item_1line,
                null,
                from,
                to,
                0);

        // This will provide the labels for the choices to be displayed in the AutoCompleteTextView
        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                final int colIndex = cursor.getColumnIndexOrThrow("Name");
                return cursor.getString(colIndex);
            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence name) {
                return db.getAgentsLike((String) name);
            }
        });
        autocompleteWirkstoffView.setAdapter(adapter);
        autocompleteWirkstoffView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapter.getCursor().getString(adapter.getCursor().getColumnIndexOrThrow("Name"));
                Agent selAgent = new Agent(id, name);
                mAgents.add(selAgent);
                mAdapter.notifyItemInserted(mAgents.size() - 1);
                autocompleteWirkstoffView.setText("");
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_autocomplete_search, container, false);
        ButterKnife.inject(this, v);
        initList(v);
        initializeAutoComplete(getActivity());
        autocompleteWirkstoffView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_substance_search, menu);
        menu.findItem(R.id.action_commit_selection).setIcon(new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_check).sizeDp(24).color(getResources().getColor(R.color.icons)));


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        db = QueryDatabase.getInstance(activity);
        mBus = BusProvider.getInstance();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_commit_selection) {
//            mBus.post(new ButtonClickedEvent(ButtonClickedEvent.GET_DATA_SEARCH_SUBSTANCE_FRAGMENT));
//        }
        return super.onOptionsItemSelected(item);
    }

    public LinkedList<Agent> getAgents() {
        return (LinkedList<Agent>) mAgents.clone();
    }

    public void setAgents(LinkedList<Agent> agents) {
        this.mAgents = (LinkedList<Agent>) agents.clone();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

}
