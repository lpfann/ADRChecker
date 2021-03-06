package unibi.com.medicapp.ui;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.DatabaseHelperClass;

/**
 * Activty which displays single Results.
 * Results can contain one or two Interaction Fragments.
 * which are displayed in
 * see @Detail_Fragment_Single
 * Utilizes ViewPager to display two Interaction Fragments side by side.
 */
public class DetailActivity extends AppCompatActivity {




    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    ViewPager mViewPager;
    TabLayout mTabLayout;

    private MyAdapter mAdapter;
    private long mInteractionID;
    private long mInteractionID2;
    private DatabaseHelperClass mDB;
    private boolean isEnzymeInteraction;
    private String mSubstance;
    private String mSubstance2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mDB = DatabaseHelperClass.getInstance(this);
        Bundle extras = getIntent().getExtras();
        // Activity sseless without IDs
        if (extras != null) {
            mInteractionID = extras.getLong("INTERACTION_ID");
            isEnzymeInteraction = extras.getBoolean("MODE");
            // Different branches depending on result type.
            if (isEnzymeInteraction) {
                mSubstance = extras.getString("SUBSTANCE");
            } else {
                mInteractionID2 = extras.getLong("INTERACTION_ID2");
            }
        } else {
            throw new Error("No Data for DetailView passed!");
        }

        // Set different Views depending on result type
        // ViewPager or Single FrameLayout
        if (isEnzymeInteraction) {
            setContentView(R.layout.activity_detail_enzyme);
            ButterKnife.inject(this);
            FragmentManager fm = getSupportFragmentManager();
            Detail_Fragment_Single enzyme_fragment = Detail_Fragment_Single.newInstance(mInteractionID);
            fm.beginTransaction().add(R.id.fragment_frame, enzyme_fragment).commit();

            // Set Title
            Cursor c = mDB.getEnzymesForInteractionID(mInteractionID);
            int i = c.getColumnIndex(DatabaseHelperClass.ISOENZYME.NAME);
            String s1 = c.getString(i);
            collapsingToolbar.setTitle(mSubstance + "  —  " + s1);
        } else {
            setContentView(R.layout.activity_detail);
            ButterKnife.inject(this);

            // Substance A
            Cursor c = mDB.getSubstanceForInteractionID(mInteractionID);
            mSubstance = c.getString(c.getColumnIndex(DatabaseHelperClass.SUBSTANZEN.NAME));
            // Substance B
            c = mDB.getSubstanceForInteractionID(mInteractionID2);
            mSubstance2 = c.getString(c.getColumnIndex(DatabaseHelperClass.SUBSTANZEN.NAME));
            mAdapter = new MyAdapter(getSupportFragmentManager(), this, mSubstance, mSubstance2);
            mViewPager = (ViewPager) findViewById(R.id.viewpager);
            mViewPager.setAdapter(mAdapter);
            mTabLayout = (TabLayout) findViewById(R.id.tabs);
            mTabLayout.setupWithViewPager(mViewPager);

            // Should change Color to match app theme
            // Library Bug? is not working atm
            mTabLayout.setTabTextColors(getResources().getColor(R.color.icons), getResources().getColor(R.color.primary_light));

        }
        // Change back arrow
        assert getSupportActionBar() != null;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        // Set titlebar colors
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.icons));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.icons));


    }

    /**
     * Adapter for the ViewPager to create Fragments to display
     */
    public static class MyAdapter extends FragmentPagerAdapter {
        long interactionID;
        long interactionID2;
        private String mSubstance;
        private String mSubstance2;

        public MyAdapter(FragmentManager fm, DetailActivity detailActivity, String substance, String substance2) {
            super(fm);
            mSubstance = substance;
            mSubstance2 = substance2;
            interactionID = detailActivity.mInteractionID;
            interactionID2 = detailActivity.mInteractionID2;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return Detail_Fragment_Single.newInstance(interactionID);
            } else {
                return Detail_Fragment_Single.newInstance(interactionID2);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mSubstance;
                case 1:
                    return mSubstance2;
                default:
                    return null;
            }


        }

    }
}
