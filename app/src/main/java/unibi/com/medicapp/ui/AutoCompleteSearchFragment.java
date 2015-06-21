package unibi.com.medicapp.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import unibi.com.medicapp.controller.DatabaseHelperClass;
import unibi.com.medicapp.model.Substance;


/**
 * Displays the AutoCompleteSearch Field
 * Is used to search for Substances and add them to a list which is later used to query the DB.
 * opened in
 *
 * @see MainSearchFragment
 */
public class AutoCompleteSearchFragment extends android.support.v4.app.Fragment {

    @InjectView(R.id.wirkstoffAutoComplete1)
    AutoCompleteTextView autocompleteWirkstoffView;
    @InjectView(R.id.wirkstoffListe)
    android.support.v7.widget.RecyclerView wirkstoffListeView;

    /**
     * List of substances which are chosen by the user.
     * Can be notnull if the user edits a former created list.
     */
    @Icicle
    LinkedList<Substance> mSubstances;

    private DatabaseHelperClass db;
    /**
     * Adapter for the Recyler view.
     */
    private SubstanceListAdapter mAdapter;
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

    /**
     * Recycler view gets init.
     *
     * @param v Root view the list is added to.
     */
    private void initList(View v) {
        if (mSubstances == null) {
            mSubstances = new LinkedList<>();
        }
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        wirkstoffListeView.setLayoutManager(mLayoutManager);
        wirkstoffListeView.setHasFixedSize(true);
        mAdapter = new SubstanceListAdapter(mSubstances);
        wirkstoffListeView.setAdapter(mAdapter);
    }

    /**
     * Init. of AutoCompleteTextView
     * @param c
     */
    private void initializeAutoComplete(Context c) {

        final int[] to = new int[]{android.R.id.text1};
        final String[] from = new String[]{"name"};
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
                final int colIndex = cursor.getColumnIndexOrThrow("name");
                return cursor.getString(colIndex);
            }
        });
        // Filters the DB for rows which start with the entered characters in the view
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence name) {
                return db.getSubstancesLike((String) name);
            }
        });
        autocompleteWirkstoffView.setAdapter(adapter);
        // Add selected Substances to the RecyclerView
        autocompleteWirkstoffView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapter.getCursor().getString(adapter.getCursor().getColumnIndexOrThrow("name"));
                Substance selSubstance = new Substance(id, name);
                mSubstances.add(selSubstance);
                mAdapter.notifyItemInserted(mSubstances.size() - 1);
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
        // Request focus and open keyboard
        autocompleteWirkstoffView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Add Commit Menu Item
        inflater.inflate(R.menu.menu_substance_search, menu);
        menu.findItem(R.id.action_commit_selection).setIcon(new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_check).sizeDp(24).color(getResources().getColor(R.color.icons)));


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        db = DatabaseHelperClass.getInstance(activity);
        mBus = BusProvider.getInstance();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Getter for the parent activity to retrieve selected substances.
     * @return Selected Substances
     */
    public LinkedList<Substance> getSubstances() {
        return (LinkedList<Substance>) mSubstances.clone();
    }

    /**
     * Fill list with items user added previously.
     * @param substances
     */
    public void setAgents(LinkedList<Substance> substances) {
        this.mSubstances = (LinkedList<Substance>) substances.clone();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

}
