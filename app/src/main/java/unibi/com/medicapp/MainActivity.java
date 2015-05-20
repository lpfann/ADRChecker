package unibi.com.medicapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.contentLayout)
    FrameLayout contentFrame;

    private FragmentManager supportFragmentManager;
    private MainSearchFragment mainSearchFragment;
    private SearchSubstanceFragment searchSubstanceFragment;
    private ArrayList<Integer> checkedEnzymes;
    private Drawer.Result result = null;
    private LinkedList<Substance> selectedSubstances;
    private Bus bus;
    private ResultListFragment resultListFragment;
    private boolean custom_menu_enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        bus = BusProvider.getInstance();
        bus.register(this);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setTitle(getResources().getString(R.string.search));
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();


        // Create  for ContentLayout
        supportFragmentManager = getSupportFragmentManager();
        mainSearchFragment = MainSearchFragment.newInstance();
        supportFragmentManager.beginTransaction().add(R.id.contentLayout, mainSearchFragment).commit();

        // init Gui Elements
        initDrawer(savedInstanceState);


    }

    @Subscribe
    public void addSubstanceButtonClicked(ButtonClickedEvent event) {
        switch (event.eventtype) {
            case (ButtonClickedEvent.ADD_SUBSTANCE_BUTTON):
                // Open Search Fragment
                searchSubstanceFragment = SearchSubstanceFragment.newInstance();
                if (selectedSubstances != null) {
                    searchSubstanceFragment.setSubstances(selectedSubstances);
                }
                supportFragmentManager.beginTransaction().replace(R.id.contentLayout, searchSubstanceFragment).addToBackStack(null).commit();
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(getString(R.string.add_substances));
                return;
            default:
        }

    }

    private void initDrawer(Bundle savedInstance) {
        AccountHeader.Result headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
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
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem().withName(getString(R.string.info)).withIcon(FontAwesome.Icon.faw_info)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        System.out.println();
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
            case R.id.action_search:
                // Open Result Fragment
                checkedEnzymes = mainSearchFragment.getCheckedItems();
                resultListFragment = ResultListFragment.newInstance();
                supportFragmentManager.beginTransaction().replace(R.id.contentLayout, resultListFragment).addToBackStack(null).commit();
                getSupportActionBar().setTitle(R.string.results);
                return true;
            case R.id.action_commit_selection:
                // Come back from Search Fragment
                selectedSubstances = searchSubstanceFragment.getSubstances();
                mainSearchFragment.setSubstances(selectedSubstances);
                supportFragmentManager.popBackStack();


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
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

}
