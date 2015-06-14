package unibi.com.medicapp.ui;

import android.content.Context;
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
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.BusProvider;
import unibi.com.medicapp.controller.DatabaseHelperClass;
import unibi.com.medicapp.model.ItemSelectedEvent;

/**
 * @author Lukas Pfannschmidt
 *         Date: 16.05.2015
 *         Time: 15:16
 */
public class DrugResultListAdapter extends RecyclerView.Adapter<DrugResultListAdapter.ViewHolder> {
    String[][] mSubstanceNames;
    private Cursor mData;
    private RecyclerView mRecyclerView;
    private Bus mBus;

    public DrugResultListAdapter(Cursor data, Context c) {
        mData = data;
        mBus = BusProvider.getInstance();

        data.moveToFirst();
        mSubstanceNames = new String[data.getCount()][2];
        DatabaseHelperClass db = DatabaseHelperClass.getInstance(c);
        int i = 0;
        mData.moveToFirst();
        while (!mData.isAfterLast()) {
            // Substance A
            long id = mData.getLong(mData.getColumnIndex("ID_A"));
            Cursor substance = db.getSubstanceForInteractionID(id);
            mSubstanceNames[i][0] = substance.getString(substance.getColumnIndex(DatabaseHelperClass.SUBSTANZEN.NAME));
            // Substance B
            id = mData.getLong(mData.getColumnIndex("ID_B"));
            substance = db.getSubstanceForInteractionID(id);
            mSubstanceNames[i][1] = substance.getString(substance.getColumnIndex(DatabaseHelperClass.SUBSTANZEN.NAME));
            i++;
            mData.moveToNext();
        }

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
        parent.nameView1.setText(mSubstanceNames[position][0]);
        parent.nameView2.setText(mSubstanceNames[position][1]);

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
