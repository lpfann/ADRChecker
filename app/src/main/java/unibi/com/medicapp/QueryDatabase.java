package unibi.com.medicapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
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
    public static HashMap<String, String> sSubstanceAgentProjection;
    private static QueryDatabase sInstance;

    //Setup projection maps
    static {
        sSubstanceAgentProjection = new HashMap<>();
        sSubstanceAgentProjection.put(SUBSTANZ_WIRKSTOFF_MAPPING.NAME, SUBSTANZ_WIRKSTOFF_MAPPING.FULL_NAME);
        sSubstanceAgentProjection.put(SUBSTANZEN.NAME, SUBSTANZEN.FULL_NAME);
        sSubstanceAgentProjection.put("substance_name", SUBSTANZEN.FULL_NAME + " AS " + "substance_name");
        sSubstanceAgentProjection.put("_id", SUBSTANZEN.FULL_ID + " AS " + "_id");
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

    public Cursor getAgentsLike(String name) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"idxWirkstoffe AS _id", "Name"};

        qb.setTables(SUBSTANZ_WIRKSTOFF_MAPPING.TABLENAME);
        Cursor c = qb.query(db, sqlSelect, "Name like '" + name + "%'", null,
                null, null, null);
        c.moveToFirst();
        return c;

    }

    public Cursor getAllEnzymes() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"id AS _id", "name"};

        qb.setTables(ISOENZYME.TABLENAME);
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

    public Cursor getSubstanceforAgent(Agent agent) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"_id", SUBSTANZEN.NAME};
        //Cursor c = db.rawQuery("SELECT * FROM p450_substanz JOIN p450_substanz_wirkstoff_mapping ON p450_substanz._id = p450_substanz_wirkstoff_mapping.idxSubstanz",null);
        qb.setTables(SUBSTANZ_WIRKSTOFF_MAPPING.TABLENAME + " JOIN " + SUBSTANZEN.TABLENAME + " ON " + SUBSTANZ_WIRKSTOFF_MAPPING.TABLENAME + "." + SUBSTANZ_WIRKSTOFF_MAPPING.ID_SUBSTANCE + " = " + SUBSTANZEN.FULL_ID);
        qb.setProjectionMap(sSubstanceAgentProjection);
        Cursor c = qb.query(db, sqlSelect, SUBSTANZ_WIRKSTOFF_MAPPING.FULL_NAME + " =?", new String[]{agent.name},
                null, null, null);
        //TODO Überprüfen wenn richtige DB
        c.moveToFirst();
        return c;

    }

    public Cursor getResultsforDefectiveEnzyme(ArrayList<Enzyme> enzymes, LinkedList<Agent> agents) {
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
        String enzymeString;
        if (enzymes.size() > 0) {
            enzymeString = "(";
            for (int i = 0; i < enzymes.size(); i++) {
                Enzyme enz = enzymes.get(i);

                enzymeString += "'" + enz.id + "'";
                if (i < enzymes.size() - 1) {
                    enzymeString += ",";
                } else {
                    enzymeString += ")";
                }
            }
        } else {
            enzymeString = "()";
        }
        String agentString;
        if (agents.size() > 0) {
            agentString = "(";
            for (int i = 0; i < agents.size(); i++) {
                Agent ag = agents.get(i);

                agentString += "'" + ag.id + "'";
                if (i < agents.size() - 1) {
                    agentString += ",";
                } else {
                    agentString += ")";
                }
            }
        } else {
            agentString = "()";
        }


        qb.setProjectionMap(sSubstanceAgentProjection);
        Cursor c = qb.query(db, sqlSelect,
                INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.ENZYME_ID + " IN " + enzymeString + " AND " + SUBSTANZ_WIRKSTOFF_MAPPING.TABLENAME + "." + SUBSTANZ_WIRKSTOFF_MAPPING.ID_AGENT + " IN " + agentString,
                null,
                null,
                null,
                null);
        c.moveToFirst();

        //TODO Überprüfen wenn richtige DB
        return c;
    }

    public Cursor getResultsForDrugDrugInteraction(LinkedList<Agent> agents) {

        LinkedList<Long> substances = new LinkedList<>();
        for (int i = 0; i < agents.size(); i++) {
            Cursor ctemp = getSubstanceforAgent(agents.get(i));
            for (int j = 0; j < ctemp.getCount(); j++) {
                substances.add(ctemp.getLong(ctemp.getColumnIndex(SUBSTANZEN.ID)));
            }
        }
        String substanceString;
        if (substances.size() > 0) {
            substanceString = "(";
            for (int i = 0; i < substances.size(); i++) {


                substanceString += "'" + substances.get(i) + "'";
                if (i < substances.size() - 1) {
                    substanceString += ",";
                } else {
                    substanceString += ")";
                }
            }
        } else {
            substanceString = "()";
        }


        Cursor c = db.rawQuery("SELECT DISTINCT a._id as ID_A, b._id as ID_B" + " FROM " +
                INTERAKTIONEN.TABLENAME + " a"
                + " JOIN " + INTERAKTIONEN.TABLENAME + "  b"
                + " ON "
                + "a." + INTERAKTIONEN.ENZYME_ID
                + " = "
                + "b." + INTERAKTIONEN.ENZYME_ID
                + " WHERE "
                + "a." + INTERAKTIONEN.SUBSTANCE_ID + " IN " + substanceString + " AND " + "b." + INTERAKTIONEN.SUBSTANCE_ID + " IN " + substanceString + " AND " + "a." + INTERAKTIONEN.ID + " < " + "b." + INTERAKTIONEN.ID
                + " GROUP BY " + " a." + INTERAKTIONEN.SUBSTANCE_ID + " , " + "b." + INTERAKTIONEN.SUBSTANCE_ID
                , null);

        c.moveToFirst();

        //TODO Überprüfen wenn richtige DB
        return c;
    }

    public Cursor getMetabolism(long id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {METABOLISMUS.TABLENAME + "." + METABOLISMUS.ID, METABOLISMUS.TABLENAME + "." + METABOLISMUS.NAME};

        qb.setTables(METABOLISMUS.TABLENAME + " JOIN " + INTERAKTIONEN.TABLENAME + " ON " + METABOLISMUS.TABLENAME + "." + METABOLISMUS.ID + " = " + INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.METABOLISM_ID);
        Cursor c = qb.query(db, sqlSelect, INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.ID + " = " + id, null,
                null, null, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getNote(long id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {BEMERKUNGEN.TABLENAME + "." + BEMERKUNGEN.ID, BEMERKUNGEN.TABLENAME + "." + BEMERKUNGEN.BEMERKUNG};

        qb.setTables(BEMERKUNGEN.TABLENAME + " JOIN " + INTERAKTIONEN.TABLENAME + " ON " + BEMERKUNGEN.TABLENAME + "." + BEMERKUNGEN.ID + " = " + INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.NOTE_ID);
        Cursor c = qb.query(db, sqlSelect, INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.ID + " = " + id, null,
                null, null, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getEnzymes(long id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {ISOENZYME.TABLENAME + "." + ISOENZYME.ID, ISOENZYME.TABLENAME + "." + ISOENZYME.NAME};

        qb.setTables(ISOENZYME.TABLENAME + " JOIN " + INTERAKTIONEN.TABLENAME + " ON " + ISOENZYME.TABLENAME + "." + ISOENZYME.ID + " = " + INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.ENZYME_ID);
        Cursor c = qb.query(db, sqlSelect, INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.ID + " = " + id, null,
                null, null, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getLiterature(long id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {LITERATUR.TABLENAME + "." + LITERATUR.ID, LITERATUR.PMID, LITERATUR.YEAR, LITERATUR.SOURCE};

        qb.setTables(LITERATUR.TABLENAME + " JOIN " + INTERAKTIONEN.TABLENAME + " ON " + LITERATUR.TABLENAME + "." + LITERATUR.ID + " = " + INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.LITERATURE_ID);
        Cursor c = qb.query(db, sqlSelect, INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.ID + " = " + id, null,
                null, null, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getSubstance(long id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {SUBSTANZEN.TABLENAME + "." + SUBSTANZEN.ID, SUBSTANZEN.TABLENAME + "." + SUBSTANZEN.NAME};

        qb.setTables(SUBSTANZEN.TABLENAME + " JOIN " + INTERAKTIONEN.TABLENAME + " ON " + SUBSTANZEN.TABLENAME + "." + SUBSTANZEN.ID + " = " + INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.SUBSTANCE_ID);
        Cursor c = qb.query(db, sqlSelect, INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.ID + " = " + id, null,
                null, null, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getClassification(long id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {THERAPEUTISCHE_KLASSIFIKATION.TABLENAME + "." + THERAPEUTISCHE_KLASSIFIKATION.ID, THERAPEUTISCHE_KLASSIFIKATION.TABLENAME + "." + THERAPEUTISCHE_KLASSIFIKATION.NAME};

        qb.setTables(THERAPEUTISCHE_KLASSIFIKATION.TABLENAME + " JOIN " + INTERAKTIONEN.TABLENAME + " ON " + THERAPEUTISCHE_KLASSIFIKATION.TABLENAME + "." + THERAPEUTISCHE_KLASSIFIKATION.ID + " = " + INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.CLASS_ID);
        Cursor c = qb.query(db, sqlSelect, INTERAKTIONEN.TABLENAME + "." + INTERAKTIONEN.ID + " = " + id, null,
                null, null, null);
        c.moveToFirst();
        return c;
    }

    public static final class BEMERKUNGEN {
        public static final String TABLENAME = "p450_bemerkungen";
        public static final String ID = "_id";
        public static final String BEMERKUNG = "bemerkung";
    }

    public static final class ISOENZYME {
        public static final String TABLENAME = "p450_isoenzyme";
        public static final String ID = "id";
        public static final String NAME = "name";

    }

    public static final class LITERATUR {
        public static final String TABLENAME = "p450_literaturverzeichnis";
        public static final String ID = "_lit_id";
        public static final String PMID = "pmid";
        public static final String YEAR = "Erscheinungsjahr";
        public static final String SOURCE = "literatur";
    }

    public static final class METABOLISMUS {
        public static final String TABLENAME = "p450_metabolismus";
        public static final String ID = "_id";
        public static final String NAME = "name";
    }

    public static final class THERAPEUTISCHE_KLASSIFIKATION {
        public static final String TABLENAME = "p450_therapeutische_klassifikation";
        public static final String ID = "_id";
        public static final String NAME = "name";
    }

    public static final class INTERAKTIONEN {
        public static final String TABLENAME = "p450_interaktionen";
        public static final String ID = "_id";
        public static final String ENZYME_ID = "isoenzyme_id";
        public static final String SUBSTANCE_ID = "substanz_id";
        public static final String METABOLISM_ID = "metabolismus_id";
        public static final String LITERATURE_ID = "literatur_id";
        public static final String NOTE_ID = "bemerkung";
        public static final String CLASS_ID = "tk_id";


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
        public static final String ID_AGENT = "idxWirkstoffe";
        public static final String NAME = "Name";
        public static final String FULL_NAME = TABLENAME + "." + NAME;
    }
}
