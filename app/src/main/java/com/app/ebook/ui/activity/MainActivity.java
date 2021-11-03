package com.app.ebook.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityMainBinding;
import com.app.ebook.models.LogoutResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.util.AppUtilities;
import com.app.ebook.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements RetrofitListener {

    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    private Toolbar toolbar;
    private Menu menu;
    private RetroClient retroClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        retroClient = new RetroClient(this, this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_my_library, R.id.navigation_wishlist, R.id.navigation_subscription,
                R.id.navigation_authors_zone, R.id.navigation_settings, R.id.navigation_help_and_support,
                R.id.navigation_rating_and_reviews, R.id.navigation_profile)
                .setDrawerLayout(binding.drawerLayout)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_profile) {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    toolbar.setNavigationIcon(R.drawable.ic_action_navigation_icon_white);
                    if (menu != null) {
                        menu.getItem(0).setIcon(R.drawable.ic_action_notification_white);
                        menu.getItem(1).setVisible(false);
                    }
                    toolbar.setTitle("Profile");
                    toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                } else {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.white));
                    toolbar.setNavigationIcon(R.drawable.ic_action_navigation_drawer_blue);
                    if (menu != null) {
                        menu.getItem(0).setIcon(R.drawable.ic_action_notification_blue);
                        menu.getItem(1).setVisible(true);
                    }

                    toolbar.setTitleTextColor(getResources().getColor(R.color.black));
                    String title = "";
                    switch (destination.getId()) {
                        case R.id.navigation_my_library:
                            title = "My Library";
                            break;
                        case R.id.navigation_wishlist:
                            title = "WishList";
                            break;
                        case R.id.navigation_subscription:
                            title = "Subscription List";
                            break;
                        default:
                            title = getString(R.string.app_name);
                            break;
                    }
                    toolbar.setTitle(title);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        View headerLayout = binding.navView.getHeaderView(0);
        TextView textViewName = headerLayout.findViewById(R.id.textViewName);
        textViewName.setText("Hi, " + mUser.name);
    }

    public void onClickLogout(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mSessionManager.setSession(Constants.IS_LOGGEDIN, false);
                mSessionManager.setSession(Constants.USER_TOKEN, "");
                startTargetActivityNewTask(LoginActivity.class);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        if (method_name.equals(UrlConstants.URL_LOGOUT)) {
            LogoutResponse logoutResponse = (LogoutResponse) response.body();
            mProgressDialog.showProgressDialog();
            if (logoutResponse != null) {
                AppUtilities.showSnackBar(binding.drawerLayout, logoutResponse.responseMessage);
                if (logoutResponse.responseCode == 200) {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
            } else {
                AppUtilities.showSnackBar(binding.drawerLayout, "Something Went Wrong!");
            }
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        AppUtilities.showSnackBar(binding.drawerLayout, "Something Went Wrong!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_notification) {
            return true;
        } else if (item.getItemId() == R.id.menu_cart) {
            goToCartActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}