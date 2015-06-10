package unibi.com.medicapp.model;

import java.util.LinkedList;

/**
 * @author Lukas Pfannschmidt
 *         Date: 10.06.2015
 *         Time: 19:12
 */
public class Query {
    public LinkedList<Agent> agents;
    public LinkedList<Enzyme> enzymes;

    public Query(LinkedList<Agent> agents, LinkedList<Enzyme> enzymes) {
        this.agents = agents;
        this.enzymes = enzymes;
    }
}
