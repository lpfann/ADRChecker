package unibi.com.medicapp.controller;

import com.squareup.otto.Bus;

/**
 * Created by lukas on 5/18/15.
 */
public class BusProvider {
    private static final Bus BUS = new Bus();
    private BusProvider() {
        // No instances.
    }

    public static Bus getInstance() {
        return BUS;
    }
}
