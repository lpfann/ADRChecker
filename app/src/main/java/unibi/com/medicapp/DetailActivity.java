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




    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    private DetailsListAdapter mAdapter;
    private long mInteractionID;
    private QueryDatabase mDB;
    private boolean isEnzymeInteraction;
    private String mSubstance;
    private String mSubstance2;

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
            if (isEnzymeInteraction) {
                mSubstance = extras.getString("SUBSTANCE");
            }
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
            Cursor sub_cursor = mDB.getSubstances();

            collapsingToolbar.setTitle("placeholder1" + "  —  " + "placeholder2");
        }
        // TODO: TAB VIEW einbauen für zwei elemente


    }


}
