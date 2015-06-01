package unibi.com.medicapp;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Detail_Fragment_Single extends Fragment {

    String[] presentedData = {QueryDatabase.THERAPEUTISCHE_KLASSIFIKATION.TABLENAME,
            QueryDatabase.METABOLISMUS.TABLENAME, QueryDatabase.LITERATUR.TABLENAME,
            QueryDatabase.BEMERKUNGEN.TABLENAME};
    private final int length = presentedData.length;
    Cursor[] dataCursor = new Cursor[length];
    @InjectView(R.id.classification_card)
    FrameLayout class_card;
    @InjectView(R.id.literature_card)
    FrameLayout literature_card;
    @InjectView(R.id.metabolism_card)
    FrameLayout metabolism_card;
    @InjectView(R.id.note_card)
    FrameLayout note_card;
    private QueryDatabase mDB;
    private Bus mBus;

    public static Detail_Fragment_Single newInstance() {
        Detail_Fragment_Single fragment = new Detail_Fragment_Single();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Detail_Fragment_Single() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_single,container,false);
        ButterKnife.inject(this, v);

        return v;
    }

private void initCards(){
    TextView header;
    TextView content;
    int col;
    for (int i = 0; i < length; i++) {
        String name = presentedData[i];
        switch (name) {
            case (QueryDatabase.THERAPEUTISCHE_KLASSIFIKATION.TABLENAME):
                dataCursor[i] = mDB.getClassification(mInteractionID);
                if (dataCursor[i].getCount() > 0) {

                    header = (TextView) class_card.findViewById(R.id.headerTextView);
                    content = (TextView) class_card.findViewById(R.id.contentTextView);
                    header.setText("Therapy Classification");
                    col = dataCursor[i].getColumnIndex(QueryDatabase.THERAPEUTISCHE_KLASSIFIKATION.NAME);
                    content.setText(dataCursor[i].getString(col));
                }
                continue;
            case (QueryDatabase.METABOLISMUS.TABLENAME):
                dataCursor[i] = mDB.getMetabolism(mInteractionID);
                if (dataCursor[i].getCount() > 0) {

                    header = (TextView) metabolism_card.findViewById(R.id.headerTextView);
                    content = (TextView) metabolism_card.findViewById(R.id.contentTextView);
                    header.setText("Metabolism");
                    col = dataCursor[i].getColumnIndex(QueryDatabase.METABOLISMUS.NAME);
                    content.setText(dataCursor[i].getString(col));
                }
                continue;
            case (QueryDatabase.LITERATUR.TABLENAME):
                dataCursor[i] = mDB.getLiterature(mInteractionID);
                if (dataCursor[i].getCount() > 0) {

                    header = (TextView) literature_card.findViewById(R.id.headerTextView);
                    header.setText("Literature");
                    TextView year = (TextView) literature_card.findViewById(R.id.yearView);
                    TextView pubmed = (TextView) literature_card.findViewById(R.id.pubmedView);
                    TextView title = (TextView) literature_card.findViewById(R.id.titleView);
                    col = dataCursor[i].getColumnIndex(QueryDatabase.LITERATUR.YEAR);
                    year.setText(dataCursor[i].getString(col));
                    col = dataCursor[i].getColumnIndex(QueryDatabase.LITERATUR.PMID);
                    pubmed.setText(dataCursor[i].getString(col));
                    col = dataCursor[i].getColumnIndex(QueryDatabase.LITERATUR.SOURCE);
                    title.setText(dataCursor[i].getString(col));
                }
                continue;
            case (QueryDatabase.BEMERKUNGEN.TABLENAME):
                dataCursor[i] = mDB.getNote(mInteractionID);
                if (dataCursor[i].getCount() > 0) {

                    header = (TextView) note_card.findViewById(R.id.headerTextView);
                    content = (TextView) note_card.findViewById(R.id.contentTextView);
                    header.setText("Notes");
                    col = dataCursor[i].getColumnIndex(QueryDatabase.BEMERKUNGEN.BEMERKUNG);
                    content.setText(dataCursor[i].getString(col));
                }
                continue;
            default:
                continue;

        }
    }
}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity != null) {
            mBus = BusProvider.getInstance();
            mDB = QueryDatabase.getInstance(getActivity());
        }

    }
}
