package wang.patrick.smarter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public String resid;
    public String fullName;
    public String locAndPost;







    @Override




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent extras = getIntent();
        String[] resdata = extras.getStringArrayExtra("extras");




        resid = resdata[0];
        fullName = resdata[1];
        locAndPost = resdata[2];


        Log.d("resid", resid);
        Log.d("fullName", fullName);
        Log.d("locAndPost", locAndPost);

        Toast.makeText(MainActivity.this, resid + fullName +locAndPost,
                Toast.LENGTH_LONG).show();
























        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle("Navigation Drawer");

        HomeFragment defaultFrag = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("LoginResid", resid);
        bundle.putString("fullName", fullName);
        bundle.putString("locAndPost", locAndPost);
        defaultFrag.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();




        fragmentManager.beginTransaction().replace(R.id.content_frame, defaultFrag).commit();

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
        getMenuInflater().inflate(R.menu.main, menu);
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
        Bundle bundle = new Bundle();
        bundle.putString("LoginResid", resid);
        bundle.putString("fullName", fullName);
        bundle.putString("locAndPost", locAndPost);
        Fragment nextFragment = null;
        switch (id) {
            case R.id.nav_map:
                nextFragment = new MapFragment();
                nextFragment.setArguments(bundle);

                break;
            case R.id.nav_report:
                nextFragment = new ReportFragment();
                nextFragment.setArguments(bundle);
                break;
            case R.id.nav_home:
                nextFragment = new HomeFragment();
                nextFragment.setArguments(bundle);
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,
                nextFragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

