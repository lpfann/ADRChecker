package unibi.com.medicapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icepick;
import icepick.Icicle;
import unibi.com.medicapp.R;
import unibi.com.medicapp.controller.BusProvider;
import unibi.com.medicapp.controller.QueryDatabase;
import unibi.com.medicapp.model.Agent;
import unibi.com.medicapp.model.ButtonClickedEvent;
import unibi.com.medicapp.model.Enzyme;
import unibi.com.medicapp.model.ItemSelectedEvent;
import unibi.com.medicapp.model.Query;


public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {


    public Cursor enzymeCursor;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    MainSearchFragment mainSearchFragment;
    AutoCompleteSearchFragment mAutoCompleteSearchFragment;

    @Icicle
    ArrayList<Enzyme> checkedEnzymes;
    @Icicle
    LinkedList<Agent> mSelectedAgents = new LinkedList<>();
    @Icicle
    ArrayList<Integer> checkedItems;

    Drawer.Result result = null;
    AccountHeader.Result headerResult;
    boolean isEnzymeInteraction;
    private ResultOverviewFragment resultOverviewFragment;
    private Bus bus;
    private QueryDatabase mDb;
    private ResultListFragment mResultListFragment;
    private DetailActivity mDetailActivity;
    private Cursor drugCursor;
    private QueryListFragment mQueryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);

        // init Gui Elements
        assert getSupportActionBar() != null;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setTitle(getResources().getString(R.string.search));
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        initDrawer(savedInstanceState);

        if (savedInstanceState == null) {

            // Create  for ContentLayout

            mainSearchFragment = MainSearchFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.contentLayout, mainSearchFragment, "main").commit();


        } else {
            mainSearchFragment = (MainSearchFragment) getSupportFragmentManager().findFragmentByTag("main");
        }




    }



    private void initDrawer(Bundle savedInstance) {
        headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSavedInstance(savedInstance)
                .build();


        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home),
                        new PrimaryDrawerItem().withName(getString(R.string.enzyme_interaction)).withIcon(FontAwesome.Icon.faw_exchange),
                        new PrimaryDrawerItem().withName(getString(R.string.saved_queries)).withIcon(FontAwesome.Icon.faw_save),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem().withName(getString(R.string.info)).withIcon(FontAwesome.Icon.faw_info)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (((Nameable) drawerItem).getName() == getString(R.string.saved_queries)) {
                                {
                                    mQueryFragment = QueryListFragment.newInstance();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, mQueryFragment, "querylist").commit();
                                    assert getSupportActionBar() != null;
                                    getSupportActionBar().setTitle(getString(R.string.saved_queries));

                                }
                            }
                            if (((Nameable) drawerItem).getName() == getString(R.string.enzyme_interaction)) {
                                {
                                    if (mainSearchFragment != null) {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, mainSearchFragment, "main").commit();
                                        assert getSupportActionBar() != null;
                                        getSupportActionBar().setTitle(getString(R.string.search));
                                    } else {
                                        mainSearchFragment = MainSearchFragment.newInstance();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, mainSearchFragment, "main").commit();
                                        assert getSupportActionBar() != null;
                                        getSupportActionBar().setTitle(getString(R.string.search));
                                    }
                                }
                            }


                        }
                    }

                })
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        // Only called when back button is shown - handlers back action inside fragment
                        getSupportFragmentManager().popBackStack();
                        return true;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        KeyboardUtil.hideKeyboard(MainActivity.this);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .withSavedInstance(savedInstance)
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_commit_selection:
                // Come back from Search Fragment
                mAutoCompleteSearchFragment = (AutoCompleteSearchFragment) getSupportFragmentManager().findFragmentByTag("search");
                mainSearchFragment = (MainSearchFragment) getSupportFragmentManager().findFragmentByTag("main");
                mSelectedAgents = mAutoCompleteSearchFragment.getAgents();
                //mainSearchFragment.setSelectedEnzymeIDs(checkedEnzymes);
                mainSearchFragment.setSubstances(mSelectedAgents);
                getSupportFragmentManager().popBackStack();
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Subscribe
    public void onBusEvent(ButtonClickedEvent event) {
        switch (event.eventtype) {
            case (ButtonClickedEvent.ADD_SUBSTANCE_BUTTON):
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    mainSearchFragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));
                    mainSearchFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.change_agent_list));
                    mainSearchFragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.change_agent_list));

                    // Create new fragment to add (Fragment B)
                    mAutoCompleteSearchFragment = AutoCompleteSearchFragment.newInstance();
                    mAutoCompleteSearchFragment.setAgents(mSelectedAgents);

                    mAutoCompleteSearchFragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));
                    mAutoCompleteSearchFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.change_agent_list));
                    mAutoCompleteSearchFragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.change_agent_list));

                    // Our shared element (in Fragment A)
                    View agent_list = mainSearchFragment.getSubstanceListView();
                    agent_list.setTransitionName("TransitionToSearch");

                    // Add Fragment B
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, mAutoCompleteSearchFragment, "search").addToBackStack(null).addSharedElement(agent_list, "TransitionToSearch").commit();
                    assert getSupportActionBar() != null;
                    getSupportActionBar().setTitle(getString(R.string.add_agents));

                } else {
                    // Code to run on older devices
                    // Open Search Fragment
                    mAutoCompleteSearchFragment = AutoCompleteSearchFragment.newInstance();
                    mAutoCompleteSearchFragment.setAgents(mSelectedAgents);
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, mAutoCompleteSearchFragment, "search").addToBackStack(null).commit();
                    assert getSupportActionBar() != null;
                    getSupportActionBar().setTitle(getString(R.string.add_agents));
                }
                return;
            case ButtonClickedEvent.DRUG_CARD_CLICKED:
                // Set Result Mode
                isEnzymeInteraction = false;
                // Open List Fragment
                mResultListFragment = ResultListFragment.newInstance(isEnzymeInteraction);
                mResultListFragment.mResultCursor = drugCursor;
                getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, mResultListFragment, "resultlist").addToBackStack(null).commitAllowingStateLoss();
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(getString(R.string.drugdruginteractions));
                return;
            case ButtonClickedEvent.ENZYME_CARD_CLICKED:
                // Set Result Mode
                isEnzymeInteraction = true;
                // Open List Fragment
                mResultListFragment = ResultListFragment.newInstance(isEnzymeInteraction);
                mResultListFragment.mResultCursor = enzymeCursor;
                getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, mResultListFragment, "resultlist").addToBackStack(null).commitAllowingStateLoss();
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(getString(R.string.enzyme_interaction));
                return;
            case ButtonClickedEvent.START_SEARCH:
                // Open Result Fragment
                checkedEnzymes = mainSearchFragment.getCheckedItems();
                enzymeCursor = mDb.getResultsforDefectiveEnzyme(checkedEnzymes, mSelectedAgents);
                drugCursor = mDb.getResultsForDrugDrugInteraction(mSelectedAgents);
                resultOverviewFragment = ResultOverviewFragment.newInstance(enzymeCursor.getCount(), drugCursor.getCount());
                getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, resultOverviewFragment).addToBackStack(null).commit();
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(R.string.results);
                return;
            case ButtonClickedEvent.SAVE_FAB_CLICKED:
                LinkedList<Enzyme> enzymes = new LinkedList<>();
                for (int i = 0; i < checkedEnzymes.size(); i++) {
                    enzymes.add(new Enzyme("", checkedEnzymes.get(i).id));
                }
                Query q = new Query(mSelectedAgents, enzymes);
                mDb.saveQuery(q);
                Toast.makeText(this, "Search saved", Toast.LENGTH_LONG).show();
                return;
            default:
        }

    }

    @Subscribe
    public void onSelectItem(ItemSelectedEvent event) {
        // Open Result Fragment
        Intent i = new Intent(this, DetailActivity.class);
        int col;
        if (isEnzymeInteraction) {
            enzymeCursor.moveToPosition(event.position);
            col = enzymeCursor.getColumnIndexOrThrow(QueryDatabase.INTERAKTIONEN.ID);
            i.putExtra("INTERACTION_ID", enzymeCursor.getLong(col));
            i.putExtra("SUBSTANCE", enzymeCursor.getString(enzymeCursor.getColumnIndex(QueryDatabase.SUBSTANZEN.NAME)));
        } else {
            drugCursor.moveToPosition(event.position);
            col = drugCursor.getColumnIndexOrThrow("ID_A");
            i.putExtra("INTERACTION_ID", drugCursor.getLong(col));
            col = drugCursor.getColumnIndexOrThrow("ID_B");
            i.putExtra("INTERACTION_ID2", drugCursor.getLong(col));
        }
        i.putExtra("MODE", isEnzymeInteraction);
        startActivity(i);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);

    }


    public void shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        int canback = getSupportFragmentManager().getBackStackEntryCount();
        assert getSupportActionBar() != null;
        if (result != null) {
            if (canback > 0) {
                result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                getSupportActionBar().setTitle(R.string.search);
            }

        }
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus = BusProvider.getInstance();
        bus.register(this);
        mDb = QueryDatabase.getInstance(this);
    }


}
