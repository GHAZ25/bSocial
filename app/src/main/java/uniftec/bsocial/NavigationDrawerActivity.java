package uniftec.bsocial;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import uniftec.bsocial.cache.UserCache;
import uniftec.bsocial.fragments.CategoryChooserFragment;
import uniftec.bsocial.fragments.ContactsFragment;
import uniftec.bsocial.fragments.LikeChooserFragment;
import uniftec.bsocial.fragments.MessageFragment;
import uniftec.bsocial.fragments.NotificationsFragment;
import uniftec.bsocial.fragments.ProfileFragment;
import uniftec.bsocial.fragments.SearchFragment;
import uniftec.bsocial.fragments.SettingsFragment;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.OnFragmentInteractionListener,
                    SearchFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener, MessageFragment.OnFragmentInteractionListener,
        LikeChooserFragment.OnFragmentInteractionListener, CategoryChooserFragment.OnFragmentInteractionListener {

    private FragmentManager fragmentManager;
    private ProfileFragment profileFragment;
    private SearchFragment searchFragment;
    private ContactsFragment contactsFragment;
    private NotificationsFragment notificationsFragment;
    private SettingsFragment settingsFragment;
    private DecimalFormat decimalFormat = null;
    private Profile profile = null;
    private UserCache userCache = null;
    private Double latitude = null;
    private Double longitude = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switch (Locale.getDefault().getLanguage()) {
            case "en":
                decimalFormat = new DecimalFormat("##0.00000000");
            break;
            case "pt":
                decimalFormat = new DecimalFormat("##0,00000000");
            break;
        }

        profile = Profile.getCurrentProfile();
        userCache = new UserCache(profile.getId(), getApplicationContext());
        userCache.initialize();

        int delay = 1000;
        int interval = 60000;

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) { }

            @Override
            public void onProviderEnabled(String arg0) { }

            @Override
            public void onProviderDisabled(String arg0) { }

            @Override
            public void onLocationChanged(Location location) {
                latitude = Double.parseDouble(decimalFormat.format(location.getLatitude() - userCache.getUser().getLatitude()));
                longitude = Double.parseDouble(decimalFormat.format(location.getLongitude() - userCache.getUser().getLongitude()));

                if ((latitude > 0.00000000) || (latitude < -0.00000000) || (longitude > 0.00000000) || (longitude < -0.00000000)) {
                    userCache.getUser().setLatitude(location.getLatitude());
                    userCache.getUser().setLongitude(location.getLongitude());

                    userCache.updateLocation();
                    Toast.makeText(getApplicationContext(), "Mudou o local...", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Ainda no mesmo local...", Toast.LENGTH_LONG).show();
                }}
        };

        Timer gpsStart = new Timer();
        Timer gpsStop = new Timer();

        gpsStart.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER ,locationListener , null);
                        } catch (SecurityException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }, delay, interval);


        gpsStop.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    locationManager.removeUpdates(locationListener);
                } catch (SecurityException e) { }
            }
        }, (delay + 5000), interval);
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
        /*if (id == R.id.action_settings) {
            return true;
        }*/

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
        } else if (id == R.id.nav_notifications) {
            notificationsFragment = new NotificationsFragment();
            fragmentManager.beginTransaction().replace(R.id.content_navigation_drawer, notificationsFragment,
                    notificationsFragment.getTag()).commit();
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
