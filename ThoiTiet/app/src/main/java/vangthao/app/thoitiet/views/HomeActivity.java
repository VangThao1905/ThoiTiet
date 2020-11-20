package vangthao.app.thoitiet.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import vangthao.app.thoitiet.R;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private View headerView;
    private NavigationView navigationView;
    private TextView txtUsernameHeader, txtEmailHeader;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initValue();
        events();
    }

    private void initValue() {
        headerView = navigationView.getHeaderView(0);
        txtUsernameHeader = headerView.findViewById(R.id.txtUsernameHeader);
        txtEmailHeader = headerView.findViewById(R.id.txtEmailHeader);

        //setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        menu = navigationView.getMenu();

    }

    private void events() {
    }

    private void initView() {
        toolbar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_myaccount:
                Toast.makeText(this, "tai khoan cua toi", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_placesmanagement:
                Toast.makeText(this, "Quan ly dia diem", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_login_out:
                Toast.makeText(this, "dang nhap", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}