package com.github.nmcardoso.latexmanual;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   MainContentFragment.CallbackInterface {

    private AutoCompleteFragment autoCompleteFragment;

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

        autoCompleteFragment = new AutoCompleteFragment();

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        changeNavSelectedItem();
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_favorites) {
            FavoriteFragment favoriteFragment = new FavoriteFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, favoriteFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_history) {
            HistoryFragment historyFragment = new HistoryFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, historyFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
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

    private void changeNavSelectedItem() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Fragment currFrag = getSupportFragmentManager().findFragmentById(R.id.frame_container);

        if (currFrag != null && navigationView != null) {
            if (currFrag instanceof MainContentFragment) {
                navigationView.setCheckedItem(R.id.nav_home);
            } else if (currFrag instanceof FavoriteFragment) {
                navigationView.setCheckedItem(R.id.nav_favorites);
            } else if (currFrag instanceof HistoryFragment) {
                navigationView.setCheckedItem(R.id.nav_history);
            }
        }
    }

    private void expandAutoComplete() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, autoCompleteFragment)
                .addToBackStack(null)
                .commit();
    }

    private void collapseAutoComplete() {
        onBackPressed();
    }

    private void verifyFirstRun() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.PREF_DEFAULT), MODE_PRIVATE);
        boolean firstRun = prefs.getBoolean(getString(R.string.KEY_FIRST_RUN), true);
    }
}
