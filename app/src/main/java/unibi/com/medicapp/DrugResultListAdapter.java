package unibi.com.medicapp;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.otto.Bus;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Lukas Pfannschmidt
 *         Date: 16.05.2015
 *         Time: 15:16
 */
public class DrugResultListAdapter extends RecyclerView.Adapter<DrugResultListAdapter.ViewHolder> {
    private Cursor mData;
    private Cursor mSubstanceCursor;
    private RecyclerView mRecyclerView;
    private Bus mBus;

    public DrugResultListAdapter(Cursor data, Cursor s) {
        mData = data;
        mBus = BusProvider.getInstance();
        mSubstanceCursor = s;
    }

    @Override
    public DrugResultListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.drug_result_list_element, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder parent, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        mData.moveToPosition(position);
        int sub_id = mData.getInt(mData.getColumnIndexOrThrow("substanz_id_a"));
        mSubstanceCursor.moveToPosition(sub_id);
        parent.nameView1.setText(mSubstanceCursor.getString(mSubstanceCursor.getColumnIndex(QueryDatabase.SUBSTANZEN.NAME)));
        sub_id = mData.getInt(mData.getColumnIndexOrThrow("substanz_id_b"));
        mSubstanceCursor.moveToPosition(sub_id);
        parent.nameView2.setText(mSubstanceCursor.getString(mSubstanceCursor.getColumnIndex(QueryDatabase.SUBSTANZEN.NAME)));

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
        @InjectView(R.id.nameView1)
        TextView nameView1;
        @InjectView(R.id.nameView2)
        TextView nameView2;
        @InjectView(R.id.seperatorView)
        ImageView seperatorIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            seperatorIcon.setImageDrawable(new IconicsDrawable(itemView.getContext(), GoogleMaterial.Icon.gmd_swap_horiz).color(itemView.getContext().getResources().getColor(R.color.accent)).sizeDp(24));
        }


        @Override
        public void onClick(View v) {
            mBus.post(new ItemSelectedEvent(getAdapterPosition()));

        }
    }


}
