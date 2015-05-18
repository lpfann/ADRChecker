package unibi.com.medicapp;

import com.squareup.otto.Bus;

/**
 * Created by lukas on 5/18/15.
 */
public class BusProvider {
    private static final Bus BUS = new Bus();
    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
