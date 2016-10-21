package com.github.nmcardoso.latexmanual;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   MainContentFragment.CallbackInterface {

    private AutoCompleteFragment autoCompleteFragment;
    private List<Fragment> navigationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verifyFirstRun();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MainContentFragment mainFragment = new MainContentFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frame_container, mainFragment).commit();

        navigationList = new ArrayList<>();
        navigationList.add(mainFragment);
        autoCompleteFragment = new AutoCompleteFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (navigationList.size() > 1) {
                Fragment f = navigationList.get(navigationList.size() - 2);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, f)
                        .commit();
                navigationList.remove(navigationList.size() - 1);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                expandAutoComplete();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                collapseAutoComplete();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null) {
                    autoCompleteFragment.updateListView(newText);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            MainContentFragment mainFragment = new MainContentFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, mainFragment)
                    .commit();
            navigationList.add(mainFragment);
        } else if (id == R.id.nav_favorites) {
            FavoriteFragment favoriteFragment = new FavoriteFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, favoriteFragment)
                    .commit();
            navigationList.add(favoriteFragment);
        } else if (id == R.id.nav_historic) {
            HistoricFragment historicFragment = new HistoricFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, historicFragment)
                    .commit();
            navigationList.add(historicFragment);
        } else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void swapFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void expandAutoComplete() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, autoCompleteFragment)
                .commit();
    }

    private void collapseAutoComplete() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, navigationList.get(navigationList.size() - 1))
                .commit();
    }

    private void verifyFirstRun() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREF_DEFAULT), MODE_PRIVATE);
        boolean firstRun = prefs.getBoolean(getString(R.string.KEY_FIRST_RUN), true);
    }
}
