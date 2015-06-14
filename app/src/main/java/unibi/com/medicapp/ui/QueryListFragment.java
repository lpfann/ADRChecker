package unibi.com.medicapp.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.BusProvider;
import unibi.com.medicapp.controller.DatabaseHelperClass;

public class QueryListFragment extends android.support.v4.app.Fragment {


    @InjectView(R.id.mainListView)
    RecyclerView mainListView;
    Cursor mSavedQueriesCursor;
    private DatabaseHelperClass mDb;
    private Bus mBus;
    private QueryListAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QueryListFragment() {
    }

    public static QueryListFragment newInstance() {
        QueryListFragment fragment = new QueryListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSavedQueriesCursor = mDb.getAllQueries();

    }

    private void initList(View v) {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        mainListView.setLayoutManager(mLayoutManager);
        mainListView.setHasFixedSize(false);
        final QueryListAdapter adapter = new QueryListAdapter(mSavedQueriesCursor, getActivity());
        mainListView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int adapterPosition = viewHolder.getAdapterPosition();
                long id = adapter.getItemId(adapterPosition);
                Log.d("Swipe", "item  " + id);
                mDb.removeQuery(id);
                adapter.notifyItemRemoved(adapterPosition);
                adapter.refreshCursor();


            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mainListView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result_list, container, false);
        ButterKnife.inject(this, v);

        initList(v);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mDb = DatabaseHelperClass.getInstance(activity);
        mBus = BusProvider.getInstance();

    }


}
