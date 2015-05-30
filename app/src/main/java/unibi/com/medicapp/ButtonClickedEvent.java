package unibi.com.medicapp;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lukas on 5/18/15.
 */
public class ButtonClickedEvent {

    @IntDef({ADD_SUBSTANCE_BUTTON, GET_DATA_SEARCH_SUBSTANCE_FRAGMENT, BACK_TO_HOME, DISABLE_BACK_BUTTON, SAVE_SELECTED_ENZYMES, ENZYME_CARD_CLICKED, DRUG_CARD_CLICKED, START_SEARCH, OPEN_RESULT_DETAILS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentEvent {
    }
    public static final int ADD_SUBSTANCE_BUTTON = 0;
    public static final int GET_DATA_SEARCH_SUBSTANCE_FRAGMENT = 1;
    public static final int BACK_TO_HOME = 2;
    public static final int DISABLE_BACK_BUTTON = 3;
    public static final int SAVE_SELECTED_ENZYMES = 4;
    public static final int ENZYME_CARD_CLICKED = 5;
    public static final int DRUG_CARD_CLICKED = 6;
    public static final int START_SEARCH = 7;
    public static final int OPEN_RESULT_DETAILS = 8;

    int eventtype;

    @IntDef({ADD_SUBSTANCE_BUTTON, GET_DATA_SEARCH_SUBSTANCE_FRAGMENT, BACK_TO_HOME, DISABLE_BACK_BUTTON, SAVE_SELECTED_ENZYMES, ENZYME_CARD_CLICKED, DRUG_CARD_CLICKED, START_SEARCH, OPEN_RESULT_DETAILS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentEvent {
    }

    public ButtonClickedEvent(@FragmentEvent int i) {
        eventtype = i;
    }
}
