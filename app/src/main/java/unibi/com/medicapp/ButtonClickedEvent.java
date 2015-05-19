package unibi.com.medicapp;

/**
 * Created by lukas on 5/18/15.
 */
public class ButtonClickedEvent {
    public static final int GET_DATA_SEARCH_SUBSTANCE_FRAGMENT = 1;
    public static final int ADD_SUBSTANCE_BUTTON = 0;
    int eventtype;
    public ButtonClickedEvent(int i) {
        eventtype = i;
    }
}
