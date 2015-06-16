package unibi.com.medicapp.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import unibi.com.medicapp.model.Enzyme;

/**
 * Created by mirek on 16.06.15.
 */
public class EnzymeCursorAdapter extends SimpleCursorAdapter {
    HashMap<Long, Boolean> checked;
    private Cursor c;
    private ArrayList<Enzyme> checkedArray;

    public EnzymeCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, ArrayList<Enzyme> checkedList) {
        super(context, layout, c, from, to, flags);
        this.c = c;
        this.checkedArray = checkedList;
        checked = new HashMap<>();
        for (Enzyme e : checkedList) {
            checked.put(e.id, true);
        }
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        final int colIndex = cursor.getColumnIndexOrThrow("name");
        return cursor.getString(colIndex);
    }

/*
    @Override
    public long getItemId(int position) {
        c.moveToPosition(position);
        return c.getLong(0);
    }
*/

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (checked.containsKey(getItemId(position))) {
            ((ListView) parent).setItemChecked(position, true);
        }
        return super.getView(position, convertView, parent);
    }
}
