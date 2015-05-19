package unibi.com.medicapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.mikepenz.iconics.IconicsDrawable;
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

import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.contentLayout)
    FrameLayout contentFrame;

    FragmentManager supportFragmentManager;
    MainSearchFragment mainSearchFragment;
    SearchSubstanceFragment searchSubstanceFragment;
    private LinkedList<Substance> selectedSubstances;
    private Drawer.Result result = null;
    private Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        bus = BusProvider.getInstance();
        bus.register(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setTitle(getResources().getString(R.string.search));
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();


        // Create  for ContentLayout
        supportFragmentManager = getSupportFragmentManager();
        mainSearchFragment = MainSearchFragment.newInstance(null, null);
        supportFragmentManager.beginTransaction().add(R.id.contentLayout, mainSearchFragment).commit();

        // init Gui Elements
        initDrawer(savedInstanceState);


    }

    @Subscribe
    public void addSubstanceButtonClicked(ButtonClickedEvent event) {
        switch (event.eventtype) {
            case (ButtonClickedEvent.ADD_SUBSTANCE_BUTTON):
                searchSubstanceFragment = SearchSubstanceFragment.newInstance(null);
                if (selectedSubstances != null) {
                    searchSubstanceFragment.setSubstances(selectedSubstances);
                }
                supportFragmentManager.beginTransaction().replace(R.id.contentLayout, searchSubstanceFragment).addToBackStack(null).commit();
                result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                return;
            default:
                return;
        }

    }

    public void shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
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

                    }

                })
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        // Only called when back button is shown - handlers back action inside fragment
                        getSupportFragmentManager().popBackStack();
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchbutton = menu.findItem(R.id.action_search);
        searchbutton.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_search).sizeDp(24).color(getResources().getColor(R.color.icons)));
        searchbutton.setTitle(getResources().getString(R.string.search));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_commit_selection:
                selectedSubstances = searchSubstanceFragment.getSubstances();
                mainSearchFragment.setSubstances(selectedSubstances);
                supportFragmentManager.popBackStack();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
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
