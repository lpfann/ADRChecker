package unibi.com.medicapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static unibi.com.medicapp.QueryDatabase.BEMERKUNGEN;
import static unibi.com.medicapp.QueryDatabase.LITERATUR;
import static unibi.com.medicapp.QueryDatabase.METABOLISMUS;
import static unibi.com.medicapp.QueryDatabase.THERAPEUTISCHE_KLASSIFIKATION;

/**
 * @author Lukas Pfannschmidt
 *         Date: 29.05.2015
 *         Time: 22:35
 */
public class DetailsListAdapter extends RecyclerView.Adapter<DetailsListAdapter.ViewHolder> {
    String[] presentedData = {THERAPEUTISCHE_KLASSIFIKATION.TABLENAME, METABOLISMUS.TABLENAME, LITERATUR.TABLENAME, BEMERKUNGEN.TABLENAME};
    private final int length = presentedData.length;
    Cursor[] dataCursor = new Cursor[length];
    private RecyclerView mRecyclerView;
    private long dataID;
    private QueryDatabase mDB;

    public DetailsListAdapter(long id, Context c) {
        dataID = id;
        mDB = QueryDatabase.getInstance(c);
        for (int i = 0; i < length; i++) {
            String name = presentedData[i];
            switch (name) {
                case (THERAPEUTISCHE_KLASSIFIKATION.TABLENAME):
                    dataCursor[i] = mDB.getClassification(dataID);
                    continue;
                case (METABOLISMUS.TABLENAME):
                    dataCursor[i] = mDB.getMetabolism(dataID);
                    continue;
                case (LITERATUR.TABLENAME):
                    dataCursor[i] = mDB.getLiterature(dataID);
                    continue;
                case (BEMERKUNGEN.TABLENAME):
                    dataCursor[i] = mDB.getNote(dataID);
                    continue;
                default:
                    continue;

            }
        }

    }

    @Override
    public DetailsListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.normal_card, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(vh);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder parent, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        switch (presentedData[position]) {
            case (THERAPEUTISCHE_KLASSIFIKATION.TABLENAME):
                Cursor c = dataCursor[position];

                return;
            default:

        }


        parent.headerView.setText(presentedData[position]);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.contentTextView)
        TextView contentView;
        @InjectView(R.id.headerTextView)
        TextView headerView;
        @InjectView(R.id.detailCard)
        CardView cardView;


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
