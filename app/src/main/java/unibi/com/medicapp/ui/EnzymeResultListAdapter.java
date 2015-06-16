package unibi.com.medicapp.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Bus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.BusProvider;
import unibi.com.medicapp.controller.DatabaseHelperClass;
import unibi.com.medicapp.model.ItemSelectedEvent;

/**
 * @author Lukas Pfannschmidt
 *         Date: 16.05.2015
 *         Time: 15:16
 */
public class EnzymeResultListAdapter extends RecyclerView.Adapter<EnzymeResultListAdapter.ViewHolder> {
    private final DatabaseHelperClass mDB;
    private final Context c;
    private Cursor mData;
    private RecyclerView mRecyclerView;
    private Bus mBus;

    public EnzymeResultListAdapter(Cursor data,Context c) {
        mData = data;
        mBus = BusProvider.getInstance();
        this.c = c;
        mDB = DatabaseHelperClass.getInstance(c);
    }

    @Override
    public EnzymeResultListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.enzyme_result_list_element, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder parent, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        mData.moveToPosition(position);
        parent.substanceView.setText(mData.getString(mData.getColumnIndexOrThrow("name")));
        Cursor c = mDB.getEnzymesForInteractionID(mData.getLong(mData.getColumnIndex(DatabaseHelperClass.INTERAKTIONEN.ID)));
        parent.enzymeView.setText(c.getString(c.getColumnIndex(DatabaseHelperClass.ISOENZYME.NAME)));

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
        @InjectView(R.id.substanceViewItem)
        TextView substanceView;
        @InjectView(R.id.enzymeViewItem)
        TextView enzymeView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }


        @Override
        public void onClick(View v) {
            mBus.post(new ItemSelectedEvent(getAdapterPosition()));

        }
    }


}
