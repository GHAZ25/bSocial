package uniftec.bsocial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;

import uniftec.bsocial.fragments.CategoryChooserFragment;
import uniftec.bsocial.fragments.ContactsFragment;
import uniftec.bsocial.fragments.LikeChooserFragment;
import uniftec.bsocial.fragments.MessageFragment;
import uniftec.bsocial.fragments.ProfileFragment;
import uniftec.bsocial.fragments.SearchFragment;
import uniftec.bsocial.fragments.SettingsFragment;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.OnFragmentInteractionListener,
                    SearchFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener, MessageFragment.OnFragmentInteractionListener,
        LikeChooserFragment.OnFragmentInteractionListener, CategoryChooserFragment.OnFragmentInteractionListener {

    FragmentManager fragmentManager;
    ProfileFragment profileFragment;
    SearchFragment searchFragment;
    ContactsFragment contactsFragment;
    SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setTitle("bSocial");

       NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        profileFragment = new ProfileFragment();
        fragmentManager.beginTransaction().replace(R.id.content_navigation_drawer, profileFragment,
                profileFragment.getTag()).commit();
        navigationView.setCheckedItem(R.id.nav_profile);
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
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            profileFragment = new ProfileFragment();
            fragmentManager.beginTransaction().replace(R.id.content_navigation_drawer, profileFragment,
                    profileFragment.getTag()).commit();
        } else if (id == R.id.nav_search) {
            searchFragment = new SearchFragment();
            fragmentManager.beginTransaction().replace(R.id.content_navigation_drawer, searchFragment,
                    searchFragment.getTag()).commit();
        } else if (id == R.id.nav_contacts) {
            contactsFragment= new ContactsFragment();
            fragmentManager.beginTransaction().replace(R.id.content_navigation_drawer, contactsFragment,
                    contactsFragment.getTag()).commit();
        } else if (id == R.id.nav_settings) {
            settingsFragment = new SettingsFragment();
            fragmentManager.beginTransaction().replace(R.id.content_navigation_drawer, settingsFragment,
                    settingsFragment.getTag()).commit();
        } else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            startActivity(new Intent(NavigationDrawerActivity.this, MainActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
