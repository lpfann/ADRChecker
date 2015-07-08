package unibi.com.medicapp.ui;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.squareup.otto.Bus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.BusProvider;
import unibi.com.medicapp.controller.DatabaseHelperClass;

/**
 * Fragment to display Details about a single Interaction requested from the DB.
 * Is embedded in
 *
 * @see DetailActivity
 */
public class Detail_Fragment_Single extends android.support.v4.app.Fragment {

    // Items which get displayed
    // uses Constants in DB Class
    String[] presentedData = {DatabaseHelperClass.THERAPEUTISCHE_KLASSIFIKATION.TABLENAME,
            DatabaseHelperClass.METABOLISMUS.TABLENAME,
            DatabaseHelperClass.BEMERKUNGEN.TABLENAME, DatabaseHelperClass.ISOENZYME.TABLENAME};
    private final int length = presentedData.length;

    Cursor[] dataCursor = new Cursor[length];

    @InjectView(R.id.linearcardLayout)
    LinearLayout linearcardLayout;


    private DatabaseHelperClass mDB;
    private Bus mBus;
    /**
     * Interaction ID which gets presented in this Fragment
     */
    private long mInteractionID;

    public Detail_Fragment_Single() {
        // Required empty public constructor
    }

    public static Detail_Fragment_Single newInstance(long interactionID) {
        Detail_Fragment_Single fragment = new Detail_Fragment_Single();
        Bundle args = new Bundle();
        args.putLong("INTERACTION_ID", interactionID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fragment is useless without an ID
        if (getArguments() != null) {
            mInteractionID = getArguments().getLong("INTERACTION_ID");
        } else {
            throw new NullPointerException("No InteractionID for Detail Fragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_single, container, false);
        ButterKnife.inject(this, v);
        initCards();
        return v;
    }

    /**
     * This method fills Views with Data from Cursor
     * Could/Should be replaced with dynamic List if used for more data points.
     */
    private void initCards() {
        TextView header;
        TextView content;
        ImageView image;
        int col;

        // Normal Cards added to linear layout
        for (int i = 0; i < length; i++) {
            String name = presentedData[i];
            FrameLayout card = (FrameLayout) getLayoutInflater(null).inflate(R.layout.normal_card, null);
            content = (TextView) card.findViewById(R.id.contentTextView);
            header = (TextView) card.findViewById(R.id.headerTextView);
            image = (ImageView) card.findViewById(R.id.imageView);

            switch (name) {
                case (DatabaseHelperClass.THERAPEUTISCHE_KLASSIFIKATION.TABLENAME):

                    dataCursor[i] = mDB.getClassificationForInteractionID(mInteractionID);
                    if (dataCursor[i].getCount() > 0) {
                        col = dataCursor[i].getColumnIndex(DatabaseHelperClass.THERAPEUTISCHE_KLASSIFIKATION.NAME);
                        content.setText(dataCursor[i].getString(col));
                        header.setText(R.string.therapeutic_classification);
                        image.setImageDrawable(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_database).sizeDp(32).color(getResources().getColor(R.color.accent)));
                        linearcardLayout.addView(card);
                    }
                    break;
                case (DatabaseHelperClass.METABOLISMUS.TABLENAME):
                    dataCursor[i] = mDB.getMetabolismForInteractionID(mInteractionID);
                    if (dataCursor[i].getCount() > 0) {
                        col = dataCursor[i].getColumnIndex(DatabaseHelperClass.METABOLISMUS.NAME);
                        content.setText(dataCursor[i].getString(col));
                        header.setText(R.string.metabolism);
                        image.setImageDrawable(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_refresh).sizeDp(32).color(getResources().getColor(R.color.accent)));
                        linearcardLayout.addView(card);
                    }
                    break;

                case (DatabaseHelperClass.BEMERKUNGEN.TABLENAME):
                    dataCursor[i] = mDB.getNoteForInteractionID(mInteractionID);
                    if (dataCursor[i].getCount() > 0) {
                        col = dataCursor[i].getColumnIndex(DatabaseHelperClass.BEMERKUNGEN.BEMERKUNG);
                        content.setText(dataCursor[i].getString(col));
                        header.setText(R.string.notes);
                        image.setImageDrawable(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_comment).sizeDp(32).color(getResources().getColor(R.color.accent)));
                        linearcardLayout.addView(card);
                    }
                    break;
                case (DatabaseHelperClass.ISOENZYME.TABLENAME):
                    dataCursor[i] = mDB.getEnzymesForInteractionID(mInteractionID);
                    if (dataCursor[i].getCount() > 0) {
                        col = dataCursor[i].getColumnIndex(DatabaseHelperClass.ISOENZYME.NAME);
                        content.setText(dataCursor[i].getString(col));
                        header.setText(R.string.enzyme);
                        image.setImageDrawable(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_puzzle_piece).sizeDp(32).color(getResources().getColor(R.color.accent)));
                        linearcardLayout.addView(card);
                    }
                    break;
                default:
                    continue;

            }

        }

        // Add literature card
        Cursor c = mDB.getLiteratureForInteractionID(mInteractionID);
        if (c.getCount() > 0) {
            FrameLayout card = (FrameLayout) getLayoutInflater(null).inflate(R.layout.literature_card, null);
            TextView pubmed = (TextView) card.findViewById(R.id.pubmedView);
            TextView title = (TextView) card.findViewById(R.id.titleView);
            TableRow pubmedrow = (TableRow) card.findViewById(R.id.pubmedrow);
            image = (ImageView) card.findViewById(R.id.imageView);
            header = (TextView) card.findViewById(R.id.headerTextView);
            col = c.getColumnIndex(DatabaseHelperClass.LITERATUR.PMID);
            if (c.getInt(col) != 0) {
                pubmed.setText(c.getString(col));
            } else {
                pubmedrow.setVisibility(View.GONE);
            }
            col = c.getColumnIndex(DatabaseHelperClass.LITERATUR.SOURCE);
            title.setText(c.getString(col));
            header.setText(R.string.literature);
            image.setImageDrawable(new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_book).sizeDp(32).color(getResources().getColor(R.color.accent)));
            linearcardLayout.addView(card);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity != null) {
            mBus = BusProvider.getInstance();
            mDB = DatabaseHelperClass.getInstance(getActivity());
        }

    }
}
