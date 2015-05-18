package unibi.com.medicapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * @author Lukas Pfannschmidt
 *         Date: 13.05.2015
 *         Time: 11:58
 */
public class QueryDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "converted.sqlite";
    private static final int DATABASE_VERSION = 2;

    private static final String BEMERKUNGEN = "p450_bemerkungen";
    private static final String INTERATKIONEN = "p450_interaktionen";
    private static final String ISOENZYME = "p450_isoenzyme";
    private static final String LITERATUR = "p450_literaturverzeichnis";
    private static final String METABOLISMUS = "p450_metabolismus";
    private static final String SUBSTANZEN = "p450_substanz";
    private static final String SUBSTANZ_WIRKSTOFF_MAPPING = "p450_substanz_wirkstoff_mapping";
    private static final String THERAPEUTISCHE_KLASSIFIKATION = "p450_therapeutische_klassifikation";

    private static QueryDatabase sInstance;
    private SQLiteDatabase db;

    private QueryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
        db = getReadableDatabase();

    }

    public static synchronized QueryDatabase getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new QueryDatabase(context.getApplicationContext());

        }
        return sInstance;
    }

    public Cursor getSubstances() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "name"};

        qb.setTables(SUBSTANZEN);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;

    }

    public Cursor getSubstancesLike(String name) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "name"};

        qb.setTables(SUBSTANZEN);
        Cursor c = qb.query(db, sqlSelect, "name like '" + name + "%'", null,
                null, null, null);
        c.moveToFirst();
        return c;

    }

    public Cursor getEnzymes() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"0 _id", "id", "name"};

        qb.setTables(ISOENZYME);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        c.moveToFirst();
        return c;
    }
}
