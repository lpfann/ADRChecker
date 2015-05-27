package unibi.com.medicapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.FontAwesome;

import java.util.LinkedList;

/**
 * @author Lukas Pfannschmidt
 *         Date: 16.05.2015
 *         Time: 15:16
 */
public class AgentsListAdapter extends RecyclerView.Adapter<AgentsListAdapter.ViewHolder> {
    private LinkedList<Agent> mData;
    private RecyclerView mRecyclerView;

    public AgentsListAdapter(LinkedList<Agent> data) {
        mData = data;
    }

    @Override
    public AgentsListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.substancelisteditable, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);
        ImageButton button = (ImageButton) v.findViewById(R.id.removeButton);
        button.setImageDrawable(new IconicsDrawable(viewGroup.getContext(), FontAwesome.Icon.faw_minus_square).color(R.color.accent).sizeDp(24));
        v.findViewById(R.id.removeButton).setOnClickListener(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder parent, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
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


        @Override
        public void onClick(View v) {
            mData.remove(getPosition());
            mRecyclerView.getAdapter().notifyItemRemoved(getPosition());
        }
    }


}
