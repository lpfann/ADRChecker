package unibi.com.medicapp.model;

import java.util.LinkedList;

/**
 * @author Lukas Pfannschmidt
 *         Date: 10.06.2015
 *         Time: 19:12
 */
public class Query {
    public LinkedList<Substance> substances;
    public LinkedList<Enzyme> enzymes;
    public String name;



    public Query(LinkedList<Substance> substances, LinkedList<Enzyme> enzymes) {
        this.substances = substances;
        this.enzymes = enzymes;
    }

    public Query(LinkedList<Substance> substances, LinkedList<Enzyme> enzymes, String name) {
        this.substances = substances;
        this.enzymes = enzymes;
        this.name = name;
    }
}
