package unibi.com.medicapp.model;

/**
 *   BusEvent for Otto to notify MainActivity when a Listitem in one of the ResultFragments was clicked.
 */
public class ItemSelectedEvent {
    public int position;

    public ItemSelectedEvent(int pos) {
        position = pos;
    }
}
