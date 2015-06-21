package unibi.com.medicapp.controller;

import com.squareup.otto.Bus;

/**
 * Provides all UI Classes with a single Instance of the Event Bus.
 * THe Bus is used to notify the MainActivity from everywhere without Interfaces.
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
