package unibi.com.medicapp;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.squareup.otto.Bus;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Detail_Fragment_Single extends android.support.v4.app.Fragment {

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
    private long mInteractionID;

    public Detail_Fragment_Single() {
        // Required empty public constructor
    }

    public static Detail_Fragment_Single newInstance(long interactionID) {
        Detail_Fragment_Single fragment = new Detail_Fragment_Single();
        Bundle args = new Bundle();
        args.putLong("INTERACTION_ID",interactionID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           mInteractionID = getArguments().getLong("INTERACTION_ID");
        } else {
            throw new NullPointerException("No InteractionID for Detail Fragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_single,container,false);
        ButterKnife.inject(this, v);
        initCards();
        return v;
    }

private void initCards(){
    TextView header;
    TextView content;
    ImageView image;
    int col;
    for (int i = 0; i < length; i++) {
        String name = presentedData[i];
        switch (name) {
            case (QueryDatabase.THERAPEUTISCHE_KLASSIFIKATION.TABLENAME):
                dataCursor[i] = mDB.getClassification(mInteractionID);
                if (dataCursor[i].getCount() > 0) {

                    content = (TextView) class_card.findViewById(R.id.contentTextView);
                    col = dataCursor[i].getColumnIndex(QueryDatabase.THERAPEUTISCHE_KLASSIFIKATION.NAME);
                    content.setText(dataCursor[i].getString(col));
                }
                header = (TextView) class_card.findViewById(R.id.headerTextView);
                header.setText("Therapeutic Classification");
                image = (ImageView) class_card.findViewById(R.id.imageView);
                image.setImageDrawable(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_database).sizeDp(32).color(getResources().getColor(R.color.accent)));
                continue;
            case (QueryDatabase.METABOLISMUS.TABLENAME):
                dataCursor[i] = mDB.getMetabolism(mInteractionID);
                if (dataCursor[i].getCount() > 0) {

                    content = (TextView) metabolism_card.findViewById(R.id.contentTextView);
                    col = dataCursor[i].getColumnIndex(QueryDatabase.METABOLISMUS.NAME);
                    content.setText(dataCursor[i].getString(col));
                }
                header = (TextView) metabolism_card.findViewById(R.id.headerTextView);
                header.setText("Metabolism");
                image = (ImageView) metabolism_card.findViewById(R.id.imageView);
                image.setImageDrawable(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_refresh).sizeDp(32).color(getResources().getColor(R.color.accent)));
                continue;
            case (QueryDatabase.LITERATUR.TABLENAME):
                dataCursor[i] = mDB.getLiterature(mInteractionID);
                if (dataCursor[i].getCount() > 0) {

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
                header = (TextView) literature_card.findViewById(R.id.headerTextView);
                header.setText("Literature");
                image = (ImageView) literature_card.findViewById(R.id.imageView);
                image.setImageDrawable(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_book).sizeDp(32).color(getResources().getColor(R.color.accent)));
                continue;
            case (QueryDatabase.BEMERKUNGEN.TABLENAME):
                dataCursor[i] = mDB.getNote(mInteractionID);
                if (dataCursor[i].getCount() > 0) {
                    content = (TextView) note_card.findViewById(R.id.contentTextView);
                    col = dataCursor[i].getColumnIndex(QueryDatabase.BEMERKUNGEN.BEMERKUNG);
                    content.setText(dataCursor[i].getString(col));
                }
                header = (TextView) note_card.findViewById(R.id.headerTextView);
                header.setText("Notes");
                image = (ImageView) note_card.findViewById(R.id.imageView);
                image.setImageDrawable(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_comment).sizeDp(32).color(getResources().getColor(R.color.accent)));
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
