package unibi.com.medicapp.ui;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.BusProvider;
import unibi.com.medicapp.model.ButtonClickedEvent;

/**
 * Fragment to display a little welcome Text to introduce the function of the app.
 */
public class WelcomeFragment extends android.support.v4.app.Fragment {
    private Bus mBus;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();

        return fragment;
    }

    @OnClick(R.id.startbutton)
    void start() {
        mBus.post(new ButtonClickedEvent(ButtonClickedEvent.START_BUTTON_CLICKED));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity != null) {
            mBus = BusProvider.getInstance();
        }
    }
}
