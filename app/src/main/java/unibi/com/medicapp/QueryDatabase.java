package unibi.com.medicapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Lukas Pfannschmidt
 *         Date: 13.05.2015
 *         Time: 11:58
 */
public class QueryDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "converted.sqlite";
    private static final int DATABASE_VERSION = 2;

    private static final String BEMERKUNGEN = "p450_bemerkungen";
    private static final String ISOENZYME = "p450_isoenzyme";
    private static final String LITERATUR = "p450_literaturverzeichnis";
    private static final String METABOLISMUS = "p450_metabolismus";
    private static final String THERAPEUTISCHE_KLASSIFIKATION = "p450_therapeutische_klassifikation";
    public static HashMap<String, String> sSubstanceAgentProjection;
    private static QueryDatabase sInstance;

    //Setup projection maps
    static {
        sSubstanceAgentProjection = new HashMap<>();
        sSubstanceAgentProjection.put(SUBSTANZ_WIRKSTOFF_MAPPING.NAME, SUBSTANZ_WIRKSTOFF_MAPPING.FULL_NAME);
        sSubstanceAgentProjection.put(SUBSTANZEN.NAME, SUBSTANZEN.FULL_NAME);
        sSubstanceAgentProjection.put("substance_name", SUBSTANZEN.FULL_NAME + " AS " + "substance_name");
        sSubstanceAgentProjection.put("_id", SUBSTANZEN.ID);
    }

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

        qb.setTables(SUBSTANZEN.TABLENAME);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;

    }

    public Cursor getSubstancesLike(String name) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "name"};

        qb.setTables(SUBSTANZEN.TABLENAME);
        Cursor c = qb.query(db, sqlSelect, "name like '" + name + "%'", null,
                null, null, null);
        c.moveToFirst();
        return c;

    }

    public Cursor getaAgentsLike(String name) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "name"};

        qb.setTables(SUBSTANZ_WIRKSTOFF_MAPPING.TABLENAME);
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

    public Cursor getSubstanceforAgent(String agent) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"_id", SUBSTANZEN.NAME};
        //Cursor c = db.rawQuery("SELECT * FROM p450_substanz JOIN p450_substanz_wirkstoff_mapping ON p450_substanz._id = p450_substanz_wirkstoff_mapping.idxSubstanz",null);
        qb.setTables(SUBSTANZ_WIRKSTOFF_MAPPING.TABLENAME + " JOIN " + SUBSTANZEN.TABLENAME + " ON " + SUBSTANZ_WIRKSTOFF_MAPPING.TABLENAME + "." + SUBSTANZ_WIRKSTOFF_MAPPING.ID_SUBSTANCE + " = " + SUBSTANZEN.FULL_ID);
        qb.setProjectionMap(sSubstanceAgentProjection);
        Cursor c = qb.query(db, sqlSelect, SUBSTANZ_WIRKSTOFF_MAPPING.FULL_NAME + " =?", new String[]{agent},
                null, null, null);
        //TODO Überprüfen wenn richtige DB
        c.moveToFirst();
        return c;

    }

    public Cursor getResultsforDefectiveEnzyme(LinkedList<Enzyme> enzymes, LinkedList<Agent> agents) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"_id", SUBSTANZEN.NAME};
        qb.setTables(
                SUBSTANZ_WIRKSTOFF_MAPPING.TABLENAME
                        + " JOIN " + SUBSTANZEN.TABLENAME
                        + " ON "
                        + SUBSTANZ_WIRKSTOFF_MAPPING.TABLENAME + "." + SUBSTANZ_WIRKSTOFF_MAPPING.ID_SUBSTANCE
                        + " = "
                        + SUBSTANZEN.FULL_ID
                        + " JOIN "
                        + INTERAKTIONEN.TABLENAME
                        + " ON "
                        + INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.SUBSTANCE_ID
                        + " = "
                        + SUBSTANZEN.FULL_ID
        );
        String enzymeString = "(";
        String agentString = "(";
        for (Enzyme enz : enzymes) {
            enzymeString = enzymeString + "'" + enz.id + "',";
        }
        for (Agent ag : agents) {
            agentString = agentString + "'" + ag.id + "',";
        }
        enzymeString += ")";
        agentString += ")";

        qb.setProjectionMap(sSubstanceAgentProjection);
        Cursor c = qb.query(db, sqlSelect,
                INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.ENZYME_ID + " IN ? AND " + SUBSTANZ_WIRKSTOFF_MAPPING.TABLENAME + "." + SUBSTANZ_WIRKSTOFF_MAPPING.ID_AGENT + " IN ?",
                new String[]{enzymeString, agentString},
                null,
                null,
                null);
        c.moveToFirst();

        //TODO Überprüfen wenn richtige DB
        return c;
    }

    public Cursor mainEnzymeQuery(LinkedList<Enzyme> enzymes, LinkedList<Agent> agents) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"0 _id", "id", "name"};
        // Get Substances for the agents

        qb.setTables(ISOENZYME);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        c.moveToFirst();
        return c;
    }

    public static final class INTERAKTIONEN {
        public static final String TABLENAME = "p450_interaktionen";
        public static final String ENZYME_ID = "isoenzyme_id";
        public static final String SUBSTANCE_ID = "substanz_id";


    }

    public static final class SUBSTANZEN {
        public static final String TABLENAME = "p450_substanz";
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String FULL_NAME = TABLENAME + "." + NAME;
        public static final String FULL_ID = TABLENAME + "." + ID;
    }

    public static final class SUBSTANZ_WIRKSTOFF_MAPPING {

        public static final String TABLENAME = "p450_substanz_wirkstoff_mapping";
        public static final String ID_PRAEP = "idxPraeparate";
        public static final String ID_SUBSTANCE = "idxSubstanz";
        public static final String ID_AGENT = "idxWirkstoff";
        public static final String NAME = "Name";
        public static final String FULL_NAME = TABLENAME + "." + NAME;
    }
}
