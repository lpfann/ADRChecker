package unibi.com.medicapp.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.BusProvider;
import unibi.com.medicapp.controller.QueryDatabase;

public class ResultListFragment extends android.support.v4.app.Fragment {


    public Cursor mResultCursor;
    SimpleCursorAdapter cursorAdapter;
    @InjectView(R.id.mainListView)
    RecyclerView mainListView;
    private boolean isEnzyme;
    private QueryDatabase mDb;
    private Bus mBus;
    private EnzymeResultListAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResultListFragment() {
    }

    public static ResultListFragment newInstance(boolean mode) {
        ResultListFragment fragment = new ResultListFragment();
        Bundle args = new Bundle();
        args.putBoolean("MODE", mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isEnzyme = getArguments().getBoolean("MODE");

    }

    private void initList(View v) {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        mainListView.setLayoutManager(mLayoutManager);
        mainListView.setHasFixedSize(true);
        // TODO: Interface für gleichaussehende Ergebnisse verbessern
        if (isEnzyme) {
            mainListView.setAdapter(new EnzymeResultListAdapter(mResultCursor));
        } else {
            Cursor sub_cursor = mDb.getSubstances();
            mainListView.setAdapter(new DrugResultListAdapter(mResultCursor, getActivity()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result_list, container, false);
        ButterKnife.inject(this, v);
        if (mResultCursor == null) {
            throw new Error("missing Result Cursor");
        }
        initList(v);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mDb = QueryDatabase.getInstance(activity);
        mBus = BusProvider.getInstance();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}