package unibi.com.medicapp.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;

import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.BusProvider;
import unibi.com.medicapp.controller.QueryDatabase;
import unibi.com.medicapp.model.Enzyme;
import unibi.com.medicapp.model.Query;
import unibi.com.medicapp.model.Substance;

/**
 * @author Lukas Pfannschmidt
 *         Date: 16.05.2015
 *         Time: 15:16
 */
public class QueryListAdapter extends RecyclerView.Adapter<QueryListAdapter.ViewHolder> {
    private final QueryDatabase mDB;
    private Cursor mData;
    private Context c;
    private RecyclerView mRecyclerView;
    private Bus mBus;

    public QueryListAdapter(Cursor data, Context c) {
        mData = data;
        this.c = c;
        mBus = BusProvider.getInstance();
        mDB = QueryDatabase.getInstance(c);
    }

    @Override
    public QueryListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.query_item, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder parent, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        mData.moveToPosition(position);
        long id = mData.getLong(mData.getColumnIndexOrThrow("queryid"));
        Query query = mDB.getQuery(id);
        parent.idView.setText(Long.toString(id));
        for (int i = 0; i < query.substances.size(); i++) {
            TextView child = new TextView(parent.agentList.getContext());
            child.setText(query.substances.get(i).name);
            parent.agentList.addView(child);
        }
        for (int i = 0; i < query.enzymes.size(); i++) {
            TextView child = new TextView(parent.enzymeList.getContext());
            child.setText(query.enzymes.get(i).name);
            parent.enzymeList.addView(child);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return mData.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.idView)
        TextView idView;
        @InjectView(R.id.list1)
        LinearLayout agentList;
        @InjectView(R.id.list2)
        LinearLayout enzymeList;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }


        @Override
        public void onClick(View v) {
            // TODO Query laden implementieren
        }
    }

    public class AgentsAdapter extends ArrayAdapter<Substance> {
        public AgentsAdapter(Context context, LinkedList<Substance> substances) {
            super(context, 0, substances);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Substance substance = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            // Lookup view for data population
            TextView name = (TextView) convertView.findViewById(android.R.id.text1);
            // Populate the data into the template view using the data object
            name.setText(substance.name);
            // Return the completed view to render on screen
            return convertView;
        }
    }

    public class EnzymeAdapter extends ArrayAdapter<Enzyme> {
        public EnzymeAdapter(Context context, LinkedList<Enzyme> enzymes) {
            super(context, 0, enzymes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Enzyme enzyme = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            // Lookup view for data population
            TextView name = (TextView) convertView.findViewById(android.R.id.text1);
            // Populate the data into the template view using the data object
            name.setText(enzyme.name);
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
