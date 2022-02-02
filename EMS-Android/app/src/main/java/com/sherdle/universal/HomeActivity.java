package com.sherdle.universal;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Dash;
import com.sherdle.universal.model.AwardInformation;
import com.sherdle.universal.model.AwardsInformationLab;
import com.sherdle.universal.model.DialogDashboardListener;
import com.sherdle.universal.model.LocalUserVariable;
import com.sherdle.universal.model.SearchingInterface;
import com.sherdle.universal.model.SupportInformation;
import com.sherdle.universal.model.SupportInformationLab;
import com.sherdle.universal.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,MyDialogFragmentListener , DialogDashboardListener,SearchView.OnQueryTextListener {
    private static final String DIALOG_ADD ="add award dialog" ;
    private static final String DIALOG_SUPPORT = "support dialog" ;
    private Toolbar toolbar;
private DrawerLayout drawerLayout;
private NavigationView navigationView;
private ActionBarDrawerToggle drawerToggle;
private Fragment fragment;
private boolean isSupport=false;
private boolean isAward = false ;
public static boolean isDashboard = false ;
static Menu menu ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeViews();
        initalizeToolBar();

        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle.syncState();


        FragmentManager manager=getSupportFragmentManager();
        Fragment fragment=manager.findFragmentById(R.id.fragment);
        if(fragment==null){

            manager.beginTransaction()
                    .add(R.id.fragment_container2, DashboardFragment.getInstance())
                    .addToBackStack(null)
                    .commit();

        }else{
            return;
        }


    }


    // to initialize toolbar . . . ;
    private void initalizeToolBar() {
        toolbar = findViewById(R.id.base_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EM System Solutions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }
    // set fragment in frame layout . . . ;
    private void navigateToFragment(Fragment fragment){
        this.fragment=fragment;
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment_container2,fragment)
                .commit();
    }

    // initalize item  . . .  ;
    private void initializeViews(){
    drawerLayout=findViewById(R.id.drawer_layout);
    navigationView=findViewById(R.id.navigation_view);
    drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_awards,menu);

        // put here a condition for replace search view between award and support . . . .;
        // to create search bar in fragments . . . . ;
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();


        /* Code for changing the textcolor and hint color for the search view */

        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);


        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // this code for get fragment name from fragment container . . . ;
        Fragment fragmentInFrame = (Fragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container2);

        int id=item.getItemId();
        switch (id){
            case R.id.addSupportOrAward:

                if(fragmentInFrame instanceof SupportFragment){
                    FragmentManager fManager=getSupportFragmentManager();
                    SupportDialogFragment dialog=new SupportDialogFragment();
                    dialog.show(fManager,DIALOG_SUPPORT);

                    return true;
                }


                FragmentManager manager = getSupportFragmentManager();
                AwardDialogFragment dialog = new AwardDialogFragment();
                dialog.show(manager,DIALOG_ADD);
                return true;
        }
        return drawerToggle.onOptionsItemSelected(item);    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (isDashboard == true) {
        menu.findItem(R.id.addSupportOrAward).setVisible(false);
        menu.findItem(R.id.menu_item_search).setVisible(false);
            return super.onPrepareOptionsMenu(menu);
        }else {
            menu.findItem(R.id.addSupportOrAward).setVisible(true);
            menu.findItem(R.id.menu_item_search).setVisible(true);
        }

    // check if permission add is 0 then hide the add button . . . .;
        if (isAward == true && LocalUserVariable.permission_add.equals("0")){
            Log.i("Permissionshere","permissionhere");
            menu.findItem(R.id.addSupportOrAward).setVisible(false);
            return true;

        }else {
            menu.findItem(R.id.addSupportOrAward).setVisible(true);

        }

                // if screen in support and user type is admint then hide add support button . . . .;
            if (isSupport == true && LocalUserVariable.usertype.equals("admin")) {
                menu.findItem(R.id.addSupportOrAward).setVisible(false);
                return true;

            } else {
                menu.findItem(R.id.addSupportOrAward).setVisible(true);

            }


        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                switch (id){
                    case R.id.dashboard:
                        isSupport=false;

                        isDashboard = true ;
                        isAward = false ;
                        invalidateOptionsMenu();
                        getSupportActionBar().setTitle("Dashboard");
                        navigateToFragment(DashboardFragment.getInstance());
                        break;

                    case R.id.awards:
                        AwardsInformationLab.getInstance().deleteAwards();
                        isSupport=false;
                        isDashboard = false ;
                        isAward = true ;
                        invalidateOptionsMenu();
                        getSupportActionBar().setTitle("Awards");
                        navigateToFragment(AwardsFragment.newInstance());
                        break;

                    case R.id.support:
                        SupportInformationLab.getInstance().deleteSupport();
                        isSupport=true;
                        isDashboard = false ;
                        isAward = false ;
                        invalidateOptionsMenu();
                        getSupportActionBar().setTitle("Support");
                        navigateToFragment(SupportFragment.newInstance());
                        break;
                }
                // close drawer layout . . . ..  ;
                drawerLayout.closeDrawer(Gravity.START);
                return true;
            }

    @Override
    public void setIsSaved(boolean isSaved ) {
        Fragment fragmentInFrame = (Fragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container2);
        if(isSaved==true){
            if(fragmentInFrame instanceof AwardsFragment) {
                Toast.makeText(this, "Added Successfull !", Toast.LENGTH_SHORT).show();
                AwardsInformationLab.getInstance().deleteAwards();
                navigateToFragment(AwardsFragment.newInstance());
            }else {
                if(fragmentInFrame instanceof SupportFragment){
                    Toast.makeText(this, "Added Successfull !", Toast.LENGTH_SHORT).show();
                    SupportInformationLab.getInstance().deleteSupport();
                    navigateToFragment(SupportFragment.newInstance());
                }
            }
        }
    }

    // when query in search  submit . . . ;
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }
    // when text change in search bar . . . . ;
    @Override
    public boolean onQueryTextChange(String s) {
        // this code for get fragment name from fragment container . . . ;
        Fragment fragmentInFrame = (Fragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container2);

        if(fragmentInFrame instanceof AwardsFragment){
          SearchingInterface searching = (SearchingInterface)fragmentInFrame;
          searching.UpdateList(s);

        }


         return true;
    }

    @Override
    public void isDismiss(){
        navigateToFragment(DashboardFragment.getInstance());
    }

    @Override
    public void hideItem(boolean visible) {
        invalidateOptionsMenu();
    }
}
