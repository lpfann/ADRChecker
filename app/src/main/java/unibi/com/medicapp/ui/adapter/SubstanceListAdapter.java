package unibi.com.medicapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.LinkedList;

import unibi.com.medicapp.R;
import unibi.com.medicapp.model.Substance;

/**
 * Adapter to display Substances by name and an Button to remove the Item from the List.
 */
public class SubstanceListAdapter extends RecyclerView.Adapter<SubstanceListAdapter.ViewHolder> {
    private LinkedList<Substance> mData;
    private RecyclerView mRecyclerView;

    public SubstanceListAdapter(LinkedList<Substance> data) {
        mData = data;
    }

    @Override
    public SubstanceListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.substancelisteditable, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);
        // Init Remove Button
        ImageButton button = (ImageButton) v.findViewById(R.id.removeButton);
        button.setImageDrawable(new IconicsDrawable(viewGroup.getContext(), GoogleMaterial.Icon.gmd_clear).color(viewGroup.getResources().getColor(R.color.accent)).sizeDp(24));
        v.findViewById(R.id.removeButton).setOnClickListener(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder parent, int position) {
        // Set Name
        parent.mTextView.setText(mData.get(position).name);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView;
        public ImageButton mButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textView);
            mButton = (ImageButton) itemView.findViewById(R.id.removeButton);
        }

        // Remove Action when Button clicked
        @Override
        public void onClick(View v) {
            mData.remove(getPosition());
            mRecyclerView.getAdapter().notifyItemRemoved(getPosition());
        }
    }


}
