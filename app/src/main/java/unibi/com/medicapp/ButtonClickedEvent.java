package unibi.com.medicapp;

/**
 * Created by lukas on 5/18/15.
 */
public class ButtonClickedEvent {
    public static final int ADD_SUBSTANCE_BUTTON = 0;
    public static final int GET_DATA_SEARCH_SUBSTANCE_FRAGMENT = 1;
    public static final int BACK_TO_HOME = 2;
    public static final int DISABLE_BACK_BUTTON = 3;
    public static final int SAVE_SELECTED_ENZYMES = 4;
    public static final int ENZYME_CARD_CLICKED = 5;
    public static final int DRUG_CARD_CLICKED = 6;

    int eventtype;
    public ButtonClickedEvent(int i) {
        eventtype = i;
    }
}
