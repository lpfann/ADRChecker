package unibi.com.medicapp.model;

/**
 * Event Class for the OTTO Bus.
 * Identifies the Query selected in the QueryListFragment
 * @author Lukas Pfannschmidt
 *         Date: 30.05.2015
 *         Time: 15:27
 */
public class QuerySelectedEvent {
    public int position;

    public QuerySelectedEvent(int pos) {
        position = pos;
    }
}
