package unibi.com.medicapp.ui;

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
 * @author Lukas Pfannschmidt
 *         Date: 16.05.2015
 *         Time: 15:16
 */
public class QueryListAdapter extends RecyclerView.Adapter<QueryListAdapter.ViewHolder> {
    private final DatabaseHelperClass mDB;
    private Cursor mData;
    private Context c;
    private RecyclerView mRecyclerView;
    private Bus mBus;

    public QueryListAdapter(Cursor data, Context c) {
        mData = data;
        this.c = c;
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

    @Override
    public void onBindViewHolder(ViewHolder parent, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        mData.moveToPosition(position);
        long id = mData.getLong(mData.getColumnIndexOrThrow("queryid"));
        Query query = mDB.getQuery(id);
        parent.idView.setText(Long.toString(id));
        TextView child;
        for (int i = 0; i < query.substances.size(); i++) {
            if (i < parent.substancesize) {
                child = parent.substanceViewArray.get(i);
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
        for (TextView tv : holder.enzymeViewArray) {
            tv.setVisibility(View.GONE);
        }
        for (TextView tv : holder.substanceViewArray) {
            tv.setVisibility(View.GONE);
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
            mBus.post(new QuerySelectedEvent(getAdapterPosition() + 1)); // Rowid from Sqlite starts at 1 so we  need to offset the request from the adapter
        }

    }



}
