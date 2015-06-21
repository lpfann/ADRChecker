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
 * Adapter to display Enzymes from the DB.
 * Enzymes can be selected which is handled here.
 */
public class EnzymeCursorAdapter extends SimpleCursorAdapter {
    HashMap<Long, Boolean> checked;
    private Cursor c;

    public EnzymeCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, ArrayList<Enzyme> checkedList) {
        super(context, layout, c, from, to, flags);
        this.c = c;

        checked = new HashMap<>();
        // Set local field to selected items
        for (Enzyme e : checkedList) {
            checked.put(e.id, true);
        }

    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        // Display enzyme name
        final int colIndex = cursor.getColumnIndexOrThrow("name");
        return cursor.getString(colIndex);
    }

    /**
     * Ids are rowids in the DB -> stable
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
