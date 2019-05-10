package com.multilingual.rupali.accesspoints.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.multilingual.rupali.accesspoints.Constants.BundleArg;
import com.multilingual.rupali.accesspoints.Constants.LoginSharedPref;
import com.multilingual.rupali.accesspoints.Constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.api.UserApi;
import com.multilingual.rupali.accesspoints.config.APIClient;
import com.multilingual.rupali.accesspoints.fragments.AccountDetailsFragment;
import com.multilingual.rupali.accesspoints.fragments.CreateOrderFragment;
import com.multilingual.rupali.accesspoints.fragments.EditAccountDetailsFragment;
import com.multilingual.rupali.accesspoints.fragments.HomeFragment;
import com.multilingual.rupali.accesspoints.fragments.OrdersFragment;
import com.multilingual.rupali.accesspoints.models.Address;
import com.multilingual.rupali.accesspoints.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CreateOrderFragment.ProgressListener,EditAccountDetailsFragment.ProgressListener, OrdersFragment.ProgressListener {
    TextView nameTextView;
    TextView logInOrSignUp;
    CircleImageView loginImageView;
    SharedPreferences sharedPreferences;
    Boolean loggedIn;
    String email;
    Retrofit retrofit;
    ProgressBar progressBar;
    LinearLayout contentHome;
    TextView toolbarTextView;
    boolean exit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTextView=findViewById(R.id.home_act_toolbar);
        setSupportActionBar(toolbar);
        toolbarTextView.setText("Home");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        progressBar=findViewById(R.id.home_act_progress_bar);
        contentHome=findViewById(R.id.content_home);
        nameTextView=(TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_name);
        logInOrSignUp=(TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_login_text);
        logInOrSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButtonClick(view);
            }
        });
        loginImageView=navigationView.getHeaderView(0).findViewById(R.id.nav_header_profile_image);
        sharedPreferences=getSharedPreferences(LoginSharedPref.SHARED_PREF_NAME,MODE_PRIVATE);
        retrofit= APIClient.getClient();
        HomeFragment homeFragment= new HomeFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_main,homeFragment,"HOME").commit();
    }

    @Override
    protected void onResume() {
        populateDataFromSharedPreferences();
        super.onResume();
    }

    private void loginButtonClick(View view) {
        if(!loggedIn){
            Intent intent=new Intent(HomeActivity.this,AccountActivity.class);
            startActivity(intent);
        }else{
            logout();
        }
    }

    private void logout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Logout?");
        builder.setTitle("Confirm Logout !");
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                endUserSession();
                dialogInterface.dismiss();

            }
        });
        builder.show();
    }

    private void endUserSession() {
        String x_auth=sharedPreferences.getString(LoginSharedPref.USER_TOKEN,"");
        Log.d("xAuth",x_auth);
        UserApi userApi=retrofit.create(UserApi.class);
        Call<Void> call=userApi.logoutCurrentUser(x_auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    populateDataFromSharedPreferences();
                    Toast.makeText(HomeActivity.this,"Logged Out Successfully.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"Log out success: Body: "+response.body()+"");
                }else{
                    Toast.makeText(HomeActivity.this,"Please check your network connection or the User does not Exist.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG, "log out post submitted to API failed.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"Please check your network connection or the User does not Exist.", Toast.LENGTH_SHORT).show();
                Log.d(Tag.MY_TAG, "Failure: log out post submitted to API failed."+t.getMessage());
            }
        });

    }

    private void populateDataFromSharedPreferences() {
        loggedIn=sharedPreferences.getBoolean(LoginSharedPref.LOGGED_IN,false);
        if(loggedIn){
            email=sharedPreferences.getString(LoginSharedPref.USER_EMAIL,"");
           String x_auth =sharedPreferences.getString(LoginSharedPref.USER_TOKEN,"");
            Log.d("xAuth",x_auth);
            nameTextView.setText(email);
            logInOrSignUp.setText("Logout");
        }else{
            nameTextView.setText("Welcome!");
            logInOrSignUp.setText("Log In Or Sign Up");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            if (exit) {
                super.onBackPressed();
                return;
            }

            try {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("HOME");
                if (fragment != null) {
                    if (fragment.isVisible()) {
                        this.exit = true;
                        Toast.makeText(this, "Press Back again to Exit", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    fragment = HomeFragment.class.newInstance();
                    getFragmentManager().popBackStack();
//                    toolbarTitle.setText("Home");
                    fragmentManager.beginTransaction().replace(R.id.container_main, fragment, "HOME").commit();
                }
            } catch (Exception e) {

            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2000);
        }
//else {
//            super.onBackPressed();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_account) {
            editAccountOnClick();
            return true;
        }else if(id==R.id.action_delete_account){
            deleteAccountOnClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editAccount = menu.findItem(R.id.action_edit_account);
        MenuItem deleteAccount=menu.findItem(R.id.action_delete_account);

        if(loggedIn){
            editAccount.setVisible(true);
            deleteAccount.setVisible(true);
        }else{
            editAccount.setVisible(false);
            deleteAccount.setVisible(false);
        }
        return true;
    }

    private void editAccountOnClick() {
        if(loggedIn){
            EditAccountDetailsFragment editAccountDetailsFragment=new EditAccountDetailsFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_main,editAccountDetailsFragment);
            transaction.commit();
            toolbarTextView.setText("Edit Account Details");
            exit=false;
        }else{
            Toast.makeText(HomeActivity.this,"You need to be logged in to continue.",Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAccountOnClick() {
        if(loggedIn){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);

            builder.setMessage("Are you sure you want to Delete your account?");
            builder.setTitle("Confirm Deletion!");
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteAccount();
                    dialogInterface.dismiss();
                }
            });
            builder.show();

        }else{
            Toast.makeText(HomeActivity.this,"You need to be logged in to continue.",Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAccount() {
        showProgress();
        String xAuth=sharedPreferences.getString(LoginSharedPref.USER_TOKEN,"");
        UserApi userApi=retrofit.create(UserApi.class);
        Call<User> userResponse=userApi.deleteCurrentUser(xAuth);
        userResponse.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    populateDataFromSharedPreferences();
                    Toast.makeText(HomeActivity.this,"User Deleted Successfully.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"User delete Success: Body: "+response.body()+" code: "+response.code());
                    hideProgress();
                }else{
                    hideProgress();
                    Toast.makeText(HomeActivity.this,"Please check your network connection.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG, "delete post submitted to API failed."+response.body()+" code: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgress();
                Toast.makeText(HomeActivity.this,"Please check your network connection.", Toast.LENGTH_SHORT).show();
                Log.d(Tag.MY_TAG, "delete post submitted to API failed." +t.getMessage());
            }

        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            homeOnClick();
            toolbarTextView.setText("Home");
            exit=false;
        }else if (id == R.id.nav_create_order) {
           createOrderOnClick();
           toolbarTextView.setText("Order");
            exit=false;
        } else if (id == R.id.nav_my_orders) {
            myOrderOnClick();
            toolbarTextView.setText("My Orders");

        } else if (id == R.id.nav_orders) {
            allOrderOnClick();
            toolbarTextView.setText("All Orders");

        } else if (id == R.id.nav_account) {
            accountOnClick();
            toolbarTextView.setText("Account");

        } else if (id == R.id.nav_about_us) {

        } else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Access Points");
                String sAux = "\nAccess points\n";
                sAux = sAux + "https://play.google.com/store/apps/details?1234=the.AccessPoints.1234 \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }

        } else if (id == R.id.nav_contact) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","rupalichawla186@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact: Access Points");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void allOrderOnClick() {
        Bundle bundle = new Bundle();
        bundle.putInt(BundleArg.FETCH_ORDERS_TYPE,BundleArg.ALL_ORDERS);
        OrdersFragment ordersFragment=new OrdersFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_main,ordersFragment);
        ordersFragment.setArguments(bundle);
        transaction.commit();
    }

    private void homeOnClick() {
        HomeFragment homeFragment= new HomeFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container_main,homeFragment,"HOME").commit();
    }

    private void myOrderOnClick() {
        if(loggedIn){
            Bundle bundle = new Bundle();
            bundle.putInt(BundleArg.FETCH_ORDERS_TYPE,BundleArg.MY_ORDERS);
            OrdersFragment ordersFragment=new OrdersFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_main,ordersFragment);
            ordersFragment.setArguments(bundle);
            transaction.commit();
        }else{
            Toast.makeText(HomeActivity.this,"You need to be logged in to continue.",Toast.LENGTH_SHORT).show();
        }
    }

    private void createOrderOnClick() {
       if(loggedIn){
           CreateOrderFragment createOrderFragment = new CreateOrderFragment();
           android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
           FragmentTransaction transaction = fragmentManager.beginTransaction();
           transaction.replace(R.id.container_main,createOrderFragment).commit();
       }else{
           Toast.makeText(HomeActivity.this,"You need to be logged in to continue.",Toast.LENGTH_SHORT).show();
       }
    }

    private void accountOnClick() {
        if(!loggedIn){
            Intent intent=new Intent(HomeActivity.this,AccountActivity.class);
            startActivity(intent);
        }else{
            AccountDetailsFragment accountDetailsFragment = new AccountDetailsFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_main,accountDetailsFragment).commit();
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        contentHome.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        contentHome.setVisibility(View.VISIBLE);
    }
}
