package unibi.com.medicapp;

import android.test.AndroidTestCase;

import unibi.com.medicapp.controller.DatabaseHelperClass;


/**
 * @author Lukas Pfannschmidt
 *         Date: 30.05.2015
 *         Time: 18:28
 */
public class DatabaseHelperClassTest extends AndroidTestCase {
    private DatabaseHelperClass mDB;

    public void setUp() throws Exception {
        super.setUp();
        mDB = DatabaseHelperClass.getInstance(getContext());
        assertNotNull(mDB);
    }

    public void tearDown() throws Exception {
        //mDB.close();
    }

    public void testGetInstance() throws Exception {
        assertNotNull(DatabaseHelperClass.getInstance(getContext()));
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