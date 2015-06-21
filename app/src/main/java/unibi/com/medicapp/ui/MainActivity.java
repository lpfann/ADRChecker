package unibi.com.medicapp.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
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
import unibi.com.medicapp.controller.DatabaseHelperClass;
import unibi.com.medicapp.model.ButtonClickedEvent;
import unibi.com.medicapp.model.Enzyme;
import unibi.com.medicapp.model.ItemSelectedEvent;
import unibi.com.medicapp.model.Query;
import unibi.com.medicapp.model.QuerySelectedEvent;
import unibi.com.medicapp.model.Substance;

/**
 * Main Activity which hosts most of the APP
 * Consists of Navigation Drawer,Toolbar, FrameLayout for most Fragments
 * and some Controller Stuff, by receiving Bus Events through EventBus
 */
public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    // Data Cursors for Lists
    public Cursor enzymeCursor;
    /**
     * Top Toolbar (ActionBar)
     */
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    /**
     * Field for all checked Enzymes
     */
    @Icicle
    ArrayList<Enzyme> checkedEnzymes;
    /**
     * Field for added Substances by the user
     */
    @Icicle
    LinkedList<Substance> mSelectedSubstances = new LinkedList<>();
    /**
     * Event Bus, receives Data from Fragments
     */
    private Bus bus;
    /**
     * DB instance
     */
    private DatabaseHelperClass mDb;
    // Different Fragments which are displayed inside the FrameLayout
    private WelcomeFragment welcomeFragment;
    private MainSearchFragment mainSearchFragment;
    private AutoCompleteSearchFragment mAutoCompleteSearchFragment;
    private ResultOverviewFragment resultOverviewFragment;
    private QueryListFragment mQueryFragment;
    private ResultListFragment mResultListFragment;
    // Navigation Drawer and Header of it
    private Drawer.Result drawer = null;
    private AccountHeader.Result drawer_header;
    private Cursor drugCursor;

    /**
     * Used to determine ResultListFragment Type
     */
    private boolean isEnzymeInteraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Restore fields with Icepick Library
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
            welcomeFragment = WelcomeFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.content_layout, welcomeFragment).commit();
            /*mainSearchFragment = MainSearchFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.contentLayout, mainSearchFragment, "main").commit();
*/

        } else {
            mainSearchFragment = (MainSearchFragment) getSupportFragmentManager().findFragmentByTag("main");
        }




    }


    /**
     * Inits drawer which is used in the app
     * uses 3rd Party library
     *
     * @param savedInstance
     */
    private void initDrawer(Bundle savedInstance) {
        // Header which is on top the Drawer
        drawer_header = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSavedInstance(savedInstance)
                .build();


        drawer = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(drawer_header)
                        // Add Drawer Items
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(getString(R.string.drawer_item_home)).withIcon(FontAwesome.Icon.faw_home),
                        new PrimaryDrawerItem().withName(getString(R.string.new_search)).withIcon(FontAwesome.Icon.faw_exchange),
                        new PrimaryDrawerItem().withName(getString(R.string.saved_queries)).withIcon(FontAwesome.Icon.faw_save)

                )
                        // Add Listener logic, switches Fragments
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
                            if (((Nameable) drawerItem).getName() == getString(R.string.new_search)) {
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
                            if (((Nameable) drawerItem).getName() == getString(R.string.drawer_item_home)) {
                                {
                                    welcomeFragment = WelcomeFragment.newInstance();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, welcomeFragment).commit();
                                }
                            }


                        }
                    }

                })
                        // Back Arrow action
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        // Only called when back button is shown - handlers back action inside fragment
                        getSupportFragmentManager().popBackStack();
                        return true;
                    }
                })
                        // Close keyboard when drawer open
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
        // Handle Menu Item selection
        switch (item.getItemId()) {
            // Substances where selected and commited inside AutoCompleteSearchFragment
            case R.id.action_commit_selection:
                // Come back from Search Fragment
                mAutoCompleteSearchFragment = (AutoCompleteSearchFragment) getSupportFragmentManager().findFragmentByTag("search");
                mainSearchFragment = (MainSearchFragment) getSupportFragmentManager().findFragmentByTag("main");
                mSelectedSubstances = mAutoCompleteSearchFragment.getSubstances();
                //mainSearchFragment.setSelectedEnzymeIDs(checkedEnzymes);
                mainSearchFragment.setSubstances(mSelectedSubstances);
                getSupportFragmentManager().popBackStack();
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                return true;
            // MainSearchFragment Clear Button was clicked
            case R.id.action_clear:
                if (mainSearchFragment != null) {
                    mainSearchFragment.clearForms();
                }
                mSelectedSubstances = new LinkedList<>();
                checkedEnzymes = new ArrayList<>();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Receiver for Bus Events for ButtonClicks inside fragments and lists
     *
     * @param event Button Event
     */
    @Subscribe
    public void onBusEvent(ButtonClickedEvent event) {
        switch (event.eventtype) {
            // Start Button inside Welcome Fragment is clicked

            case (ButtonClickedEvent.START_BUTTON_CLICKED):
                // Replace welcome fragment with MainFragment
                getSupportFragmentManager().beginTransaction().remove(welcomeFragment).commit();
                if (mainSearchFragment == null) {
                    mainSearchFragment = MainSearchFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().add(R.id.contentLayout, mainSearchFragment).commit();

                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, mainSearchFragment, "main").commit();
                }
                return;
            // Open AutoCompleteSearchFragment when clicking Add Button
            case (ButtonClickedEvent.ADD_SUBSTANCE_BUTTON):
                // Use fancy Element Transition Animations when using Lollipop
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    mainSearchFragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));
                    mainSearchFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.change_agent_list));
                    mainSearchFragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.change_agent_list));

                    // Create new fragment to add (Fragment B)
                    mAutoCompleteSearchFragment = AutoCompleteSearchFragment.newInstance();
                    mAutoCompleteSearchFragment.setAgents(mSelectedSubstances);

                    mAutoCompleteSearchFragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));
                    mAutoCompleteSearchFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.change_agent_list));
                    mAutoCompleteSearchFragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.change_agent_list));

                    // Our shared element (in Fragment A)
                    View agent_list = mainSearchFragment.getSubstanceListView();
                    agent_list.setTransitionName("TransitionToSearch");

                    // Add Fragment B
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, mAutoCompleteSearchFragment, "search").addToBackStack(null).addSharedElement(agent_list, "TransitionToSearch").commit();
                    assert getSupportActionBar() != null;
                    getSupportActionBar().setTitle(getString(R.string.add_substance));

                } else {
                    // Code to run on older devices
                    // Open Search Fragment
                    mAutoCompleteSearchFragment = AutoCompleteSearchFragment.newInstance();
                    mAutoCompleteSearchFragment.setAgents(mSelectedSubstances);
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, mAutoCompleteSearchFragment, "search").addToBackStack(null).commit();
                    assert getSupportActionBar() != null;
                    getSupportActionBar().setTitle(getString(R.string.add_substance));
                }
                return;
            // Open Drug-Drug INTERACTION Result list
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
            // Open Enzyme Interaction result list
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
            // Start search and open overview fragment
            case ButtonClickedEvent.START_SEARCH:
                // Open Result Fragment
                checkedEnzymes = mainSearchFragment.getCheckedEnzymes();
                enzymeCursor = mDb.getResultsforDefectiveEnzyme(checkedEnzymes, mSelectedSubstances);
                drugCursor = mDb.getResultsForDrugDrugInteraction(mSelectedSubstances);
                resultOverviewFragment = ResultOverviewFragment.newInstance(enzymeCursor.getCount(), drugCursor.getCount());
                getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, resultOverviewFragment).addToBackStack(null).commit();
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(R.string.results);
                return;
            // Save Query
            case ButtonClickedEvent.SAVE_FAB_CLICKED:
                final EditText input = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("Enter Name")
                        .setMessage("Name for the Query")
                        .setView(input)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String text = input.getText().toString();
                                LinkedList<Enzyme> enzymes = new LinkedList<>();
                                for (int i = 0; i < checkedEnzymes.size(); i++) {
                                    enzymes.add(new Enzyme("", checkedEnzymes.get(i).id));
                                }
                                Query q = new Query(mSelectedSubstances, enzymes, text);
                                mDb.saveQuery(q);
                                Toast.makeText(getBaseContext(), "Search saved", Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();

                return;
            default:
        }

    }

    /**
     * Bus Receiver for List Click Events in Result list
     * @param event List item Event
     */
    @Subscribe
    public void onSelectItem(ItemSelectedEvent event) {
        // Open Result Activity
        Intent i = new Intent(this, DetailActivity.class);
        int col;
        // Drug-Drug interactions need two IDs
        if (isEnzymeInteraction) {
            enzymeCursor.moveToPosition(event.position);
            col = enzymeCursor.getColumnIndexOrThrow(DatabaseHelperClass.INTERAKTIONEN.ID);
            i.putExtra("INTERACTION_ID", enzymeCursor.getLong(col));
            i.putExtra("SUBSTANCE", enzymeCursor.getString(enzymeCursor.getColumnIndex(DatabaseHelperClass.SUBSTANZEN.NAME)));
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

    /**
     * Receiver for Bus Events for Query Load Events
     * @param event Query Load Event
     */
    @Subscribe
    public void onQueryLoad(QuerySelectedEvent event) {
        // Load MainFragment and use Query item
        int queryid = event.position;
        Query q = mDb.getQuery(queryid);
        mainSearchFragment = MainSearchFragment.newInstance();
        mSelectedSubstances = q.substances;
        // Fill data with fields from query
        mainSearchFragment.setSelectedEnzymeIDs(new ArrayList<>(q.enzymes));
        mainSearchFragment.setSubstances(mSelectedSubstances);
        // Open Fragment with saved Items
        getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, mainSearchFragment, "main").commit();
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.search);
        drawer.setSelectionByIdentifier(R.string.new_search);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save state for drawer
        outState = drawer.saveInstanceState(outState);
        outState = drawer_header.saveInstanceState(outState);

        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);

    }

    /**
     * Determine if back button should be shown are Hamburger Icon
     */
    public void shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        int canback = getSupportFragmentManager().getBackStackEntryCount();
        assert getSupportActionBar() != null;
        if (drawer != null) {
            if (canback > 0) {
                drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
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
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
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
        mDb = DatabaseHelperClass.getInstance(this);
    }


}
