package unibi.com.medicapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.LinkedList;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.contentLayout)
    FrameLayout contentFrame;


    private LinkedList<Substance> selectedSubstances;
    private QueryDatabase db;
    private Drawer.Result result = null;
    private Bus bus;
    FragmentManager supportFragmentManager;
    Fragment mainSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        bus = BusProvider.getInstance();
        bus.register(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.search));

        supportFragmentManager = getSupportFragmentManager();
        mainSearchFragment = MainSearchFragment.newInstance(null, null);
        supportFragmentManager.beginTransaction().replace(R.id.contentLayout, mainSearchFragment).commit();

        // init Gui Elements
        initDrawer();


//        searchButton.setButtonColor(getResources().getColor(R.color.accent));
//        searchButton.setRippleEffectEnabled(true);
//        searchButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_search).color(getResources().getColor(R.color.icons)).sizeDp(50));
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Start search);
//            }
//        });


    }

   @Subscribe public void addSubstanceButtonClicked(ButtonClickedEvent event) {
       if (event.eventtype == 1) {
        SearchSubstanceFragment searchSubstanceFragment  = SearchSubstanceFragment.newInstance(null);
           supportFragmentManager.beginTransaction().add(R.id.contentLayout,searchSubstanceFragment).commit();
       }

   }

    private void initDrawer() {
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
                        //TODO Menu Switcher Logik
                    }

                })
                .build();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchbutton = menu.findItem(R.id.action_search);
        searchbutton.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_search).sizeDp(24).color(getResources().getColor(R.color.icons)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
