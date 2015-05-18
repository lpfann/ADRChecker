package unibi.com.medicapp;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.squareup.otto.Bus;

import java.util.ArrayList;


public class MainSearchFragment extends Fragment {

    Bus mBus;
    SimpleCursorAdapter adapter;
    ArrayList<Integer> selectedEnzymeIDs;
    @InjectView(R.id.enzymeListView)
    ListView enzymeListView;
    @InjectView(R.id.textView3)
    TextView test;
    private OnFragmentInteractionListener mListener;
    private QueryDatabase db;
    public MainSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainSearchFragment newInstance(String param1, String param2) {
        MainSearchFragment fragment = new MainSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.add_substanceButton)
    void addSubstanceButtonClick() {
        getCheckedItems();
        mBus.post(new ButtonClickedEvent(1));
    }

    private void getCheckedItems() {
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


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_search, container, false);
        ButterKnife.inject(this, v);
        initEnzymeList();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mBus = BusProvider.getInstance();
        db = QueryDatabase.getInstance(getActivity());

    }

    private void initEnzymeList() {
        Cursor enzymeCursor = db.getEnzymes();
        final int[] to = new int[]{android.R.id.text1};
        final String[] from = new String[]{"name"};
        adapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_multiple_choice,
                enzymeCursor,
                from,
                to,
                0);
        enzymeListView.setAdapter(adapter);
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
