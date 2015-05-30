package unibi.com.medicapp;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class DetailActivity extends AppCompatActivity {


    String[] presentedData = {QueryDatabase.THERAPEUTISCHE_KLASSIFIKATION.TABLENAME, QueryDatabase.METABOLISMUS.TABLENAME, QueryDatabase.LITERATUR.TABLENAME, QueryDatabase.BEMERKUNGEN.TABLENAME};
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
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    private DetailsListAdapter mAdapter;
    private long mInteractionID;
    private QueryDatabase mDB;
    private boolean isEnzymeInteraction;
    private String mSubstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_detail);
        ButterKnife.inject(this);

        mDB = QueryDatabase.getInstance(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mInteractionID = extras.getLong("INTERACTION_ID");
            isEnzymeInteraction = extras.getBoolean("MODE");
            mSubstance = extras.getString("SUBSTANCE");
        } else {
            throw new Error("No Data vor DetailView passed!");
        }

        // init Gui Elements
        assert getSupportActionBar() != null;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.icons));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.icons));
        if (isEnzymeInteraction) {
            Cursor c = mDB.getEnzymes(mInteractionID);
            int i = c.getColumnIndex(QueryDatabase.ISOENZYME.NAME);
            String s1 = c.getString(i);
            collapsingToolbar.setTitle(mSubstance + "  —  " + s1);
            //part2.setText(c.getString(i));
        } else {
            // TODO: Substance Substance Header
        }

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


}
