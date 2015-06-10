package unibi.com.medicapp;

import android.test.AndroidTestCase;

import unibi.com.medicapp.controller.QueryDatabase;


/**
 * @author Lukas Pfannschmidt
 *         Date: 30.05.2015
 *         Time: 18:28
 */
public class QueryDatabaseTest extends AndroidTestCase {
    private QueryDatabase mDB;

    public void setUp() throws Exception {
        super.setUp();
        mDB = QueryDatabase.getInstance(getContext());
        assertNotNull(mDB);
    }

    public void tearDown() throws Exception {
        //mDB.close();
    }

    public void testGetInstance() throws Exception {
        assertNotNull(QueryDatabase.getInstance(getContext()));
    }

    public void testGetSubstances() throws Exception {

    }

    public void testGetSubstancesLike() throws Exception {

    }

    public void testGetAgentsLike() throws Exception {

    }

    public void testGetAllEnzymes() throws Exception {

    }

    public void testGetSubstanceforAgent() throws Exception {

    }

    public void testGetResultsforDefectiveEnzyme() throws Exception {

    }

    public void testGetMetabolism() throws Exception {

    }

    public void testGetNote() throws Exception {

    }

    public void testGetEnzymes() throws Exception {

    }

    public void testGetLiterature() throws Exception {

    }

    public void testGetSubstance() throws Exception {

    }

    public void testGetClassification() throws Exception {

    }
}