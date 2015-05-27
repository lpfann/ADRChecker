package unibi.com.medicapp;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

import com.squareup.otto.Bus;

/**
 * A fragment representing a list of Items.
 * <p>
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ResultListFragment extends android.support.v4.app.ListFragment {


    private QueryDatabase mDb;
    private Bus mBus;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResultListFragment() {
    }

    public static ResultListFragment newInstance() {
        ResultListFragment fragment = new ResultListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Cursor testCursor = mDb.getSubstanceforAgent("Acebutolol");
        final int[] to = new int[]{android.R.id.text1};
        final String[] from = new String[]{"name"};
        setListAdapter(new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, testCursor, from, to, 0));
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }


}
