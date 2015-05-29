package unibi.com.medicapp;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import butterknife.InjectView;

/**
 * @author Lukas Pfannschmidt
 *         Date: 29.05.2015
 *         Time: 12:59
 */
public class EnzymeListAdapter extends SimpleCursorAdapter {
    @InjectView(R.id.text1)
    TextView text1;

    public EnzymeListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

    }


}
