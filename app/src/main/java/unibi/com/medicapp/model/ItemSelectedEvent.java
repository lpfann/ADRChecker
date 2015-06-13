package unibi.com.medicapp.model;

/**
 * @author Lukas Pfannschmidt
 *         Date: 30.05.2015
 *         Time: 15:27
 */
public class ItemSelectedEvent {
    public int position;

    public ItemSelectedEvent(int pos) {
        position = pos;
    }
}
