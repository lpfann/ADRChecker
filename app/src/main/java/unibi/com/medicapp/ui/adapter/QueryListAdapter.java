package unibi.com.medicapp.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.BusProvider;
import unibi.com.medicapp.controller.DatabaseHelperClass;
import unibi.com.medicapp.model.Query;
import unibi.com.medicapp.model.QuerySelectedEvent;

/**
 * ListAdapter to handle saved Search-Querys from the DB.
 * @author Lukas Pfannschmidt
 *         Date: 16.05.2015
 *         Time: 15:16
 */
public class QueryListAdapter extends RecyclerView.Adapter<QueryListAdapter.ViewHolder> {
    private final DatabaseHelperClass mDB;
    private Cursor mData;
    private Bus mBus;
    private QueryListAdapter adapter;

    public QueryListAdapter(Cursor data, Context c) {
        mData = data;
        adapter = this;
        mBus = BusProvider.getInstance();
        mDB = DatabaseHelperClass.getInstance(c);
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

    /**
     * Fills views with content from the cursor.
     */
    @Override
    public void onBindViewHolder(ViewHolder parent, int position) {
        // Get data
        mData.moveToPosition(position);
        long id = mData.getLong(mData.getColumnIndexOrThrow("queryid"));
        Query query = mDB.getQuery(id);

        // Set Id and Name
        parent.idView.setText(Long.toString(id));
        parent.nameView.setText(query.name);

        // Create views to form a dynamic list of items (Substances, Enzymes)
        TextView child;
        for (int i = 0; i < query.substances.size(); i++) {
            if (i < parent.substancesize) {
                child = parent.substanceViewArray.get(i);
                // reuse existing views when created before in another item
            } else {
                child = new TextView(parent.substanceList.getContext());
                parent.substanceList.addView(child);
                parent.substanceViewArray.add(child);
                parent.substancesize++;
            }
            child.setText(query.substances.get(i).name);
            child.setVisibility(View.VISIBLE);

        }
        for (int i = 0; i < query.enzymes.size(); i++) {
            if (i < parent.enzymesize) {
                child = parent.enzymeViewArray.get(i);
                // reuse existing views when created before in another item
            } else {
                child = new TextView(parent.enzymeList.getContext());
                parent.enzymeList.addView(child);
                parent.enzymeViewArray.add(child);
                parent.enzymesize++;
            }
            child.setText(query.enzymes.get(i).name);
            child.setVisibility(View.VISIBLE);

        }

    }

    public void refreshCursor() {
        mData = mDB.getAllQueries();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        // Hide list items when not in view - for recycling created views
        for (TextView tv : holder.enzymeViewArray) {
            tv.setVisibility(View.GONE);
        }
        for (TextView tv : holder.substanceViewArray) {
            tv.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mData.getCount();
    }

    /**
     * @return Row id from the cursor
     */
    @Override
    public long getItemId(int position) {
        mData.moveToPosition(position);

        return mData.getLong(mData.getColumnIndex("queryid"));
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.idView)
        TextView idView;
        @InjectView(R.id.list1)
        LinearLayout substanceList;
        @InjectView(R.id.list2)
        LinearLayout enzymeList;
        @InjectView(R.id.nameView)
        TextView nameView;
        // Size if each list, is used for recycling existing views
        int substancesize = 0;
        int enzymesize = 0;
        ArrayList<TextView> enzymeViewArray = new ArrayList<>();
        ArrayList<TextView> substanceViewArray = new ArrayList<>();

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }


        @Override
        public void onClick(View v) {
            // Send event to MainActivity to load Query data into MainSearchFragment
            mBus.post(new QuerySelectedEvent((int) adapter.getItemId(getAdapterPosition())));
        }

    }



}
