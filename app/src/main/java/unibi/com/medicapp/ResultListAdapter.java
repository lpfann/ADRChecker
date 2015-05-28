package unibi.com.medicapp;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Lukas Pfannschmidt
 *         Date: 16.05.2015
 *         Time: 15:16
 */
public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ViewHolder> {
    private Cursor mData;
    private RecyclerView mRecyclerView;

    public ResultListAdapter(Cursor data) {
        mData = data;
    }

    @Override
    public ResultListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.result_list_element, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder parent, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        mData.moveToPosition(position);
        parent.nameView.setText(mData.getString(mData.getColumnIndexOrThrow("name")));

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
        @InjectView(R.id.nameView)
        TextView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }


        @Override
        public void onClick(View v) {
            // TODO: aktion implementieren wenn ergebnis gedrückt
        }
    }


}
