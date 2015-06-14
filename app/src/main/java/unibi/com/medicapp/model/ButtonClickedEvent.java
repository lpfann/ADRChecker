package unibi.com.medicapp.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Lukas Pfannschmidt
 * Date: 5/18/15
 * Time: 15:41
 */
public class ButtonClickedEvent {


    public static final int ADD_SUBSTANCE_BUTTON = 0;
    public static final int GET_DATA_SEARCH_SUBSTANCE_FRAGMENT = 1;
    public static final int BACK_TO_HOME = 2;
    public static final int DISABLE_BACK_BUTTON = 3;
    public static final int SAVE_SELECTED_ENZYMES = 4;
    public static final int ENZYME_CARD_CLICKED = 5;
    public static final int DRUG_CARD_CLICKED = 6;
    public static final int START_SEARCH = 7;
    public static final int OPEN_RESULT_DETAILS = 8;
    public static final int SAVE_FAB_CLICKED = 9;

    public int eventtype;

    public ButtonClickedEvent(@FragmentEvent int i) {
        eventtype = i;
    }

    @IntDef({ADD_SUBSTANCE_BUTTON, GET_DATA_SEARCH_SUBSTANCE_FRAGMENT, BACK_TO_HOME, DISABLE_BACK_BUTTON, SAVE_SELECTED_ENZYMES, ENZYME_CARD_CLICKED, DRUG_CARD_CLICKED, START_SEARCH, OPEN_RESULT_DETAILS, SAVE_FAB_CLICKED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentEvent {
    }
}