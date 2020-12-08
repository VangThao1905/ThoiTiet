package vangthao.app.thoitiet.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.users.User;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView txtUsernameHeader, txtEmailHeader;
    private MenuItem menuItemLoginLogout;
    private EditText edtEmailLogin, edtPasswordLogin;
    private String emailLogin;
    private FirebaseAuth myAuth;
    private Dialog dialogLogin;
    private SharedPreferences.Editor editor;
    private ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Trang chủ");

        loadViews();
        loadUsers();
        initValue();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
    }

    public String getUsernameLogin() {
        return txtUsernameHeader.getText().toString();
    }

    public String getEmailLogin() {
        return txtEmailHeader.getText().toString();
    }

    private void initValue() {
        myAuth = FirebaseAuth.getInstance();
        View headerView = navigationView.getHeaderView(0);
        txtUsernameHeader = headerView.findViewById(R.id.txtUsernameHeader);
        txtEmailHeader = headerView.findViewById(R.id.txtEmailHeader);
        Menu menu = navigationView.getMenu();
        menuItemLoginLogout = menu.findItem(R.id.nav_login_out);
        userArrayList = new ArrayList<>();

        loadUsers();
        loadSessionData();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        loadSessionData();
        setTextDisplayLogin_Logout();
    }

    private void setTextDisplayLogin_Logout() {
        if (txtUsernameHeader.getText().toString().equals("Guest") && txtEmailHeader.getText().toString().equals("No Email")) {
            menuItemLoginLogout.setTitle(R.string.login);
            menuItemLoginLogout.setIcon(R.drawable.ic_login);
        } else {
            menuItemLoginLogout.setTitle(R.string.logout);
            menuItemLoginLogout.setIcon(R.drawable.ic_logout);
        }
    }

    @SuppressLint("CommitPrefEdits")
    private void loadSessionData() {
        SharedPreferences sharedPreferencesSaveSession = getSharedPreferences("view", 0);
        editor = sharedPreferencesSaveSession.edit();
        String username = sharedPreferencesSaveSession.getString("username", "Guest");
        String email = sharedPreferencesSaveSession.getString("email", "No Email");
        txtUsernameHeader.setText(username);
        txtEmailHeader.setText(email);
    }

    private void loadViews() {
        toolbar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    public void resetDrawerHeader() {
        editor.clear();
        editor.putString("username", "Guest");
        editor.putString("email", "No Email");
        editor.commit();
        txtUsernameHeader.setText(R.string.guest);
        txtEmailHeader.setText(R.string.no_email);
        menuItemLoginLogout.setTitle(R.string.login);
        menuItemLoginLogout.setIcon(R.drawable.ic_login);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_see_weather_by_place:
                Intent intentSeeWeatherByPlace = new Intent(HomeActivity.this, SeeWeatherByPlaceActivity.class);
                startActivity(intentSeeWeatherByPlace);
                break;
            case R.id.nav_placesmanagement:
                if (txtUsernameHeader.getText().equals("Guest") && txtEmailHeader.getText().equals("No Email")) {
                    showDialogLogin();
                } else {
                    Intent intentPlacesManagement = new Intent(HomeActivity.this, PlacesManagementActivity.class);
                    intentPlacesManagement.putExtra("email", txtEmailHeader.getText().toString());
                    startActivity(intentPlacesManagement);
                }
                break;
            case R.id.nav_login_out:
                if (txtUsernameHeader.getText().equals("Guest") && txtEmailHeader.getText().equals("No Email")) {
                    showDialogLogin();
                } else {
                    resetDrawerHeader();
                    Toast.makeText(this, "Bạn đã đăng xuất!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showDialogLogin() {
        dialogLogin = new Dialog(HomeActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialogLogin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogin.setContentView(R.layout.dialog_login);
        Button btnLogin = dialogLogin.findViewById(R.id.btnLogin);
        TextView txtSignUp = dialogLogin.findViewById(R.id.txtSignUp);
        TextView txtForgotPassWord = dialogLogin.findViewById(R.id.txtForgotPassword);
        edtEmailLogin = dialogLogin.findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = dialogLogin.findViewById(R.id.edtPasswordLogin);
        ImageView imgViewClose = dialogLogin.findViewById(R.id.imgViewClose);

        imgViewClose.setOnClickListener(view -> dialogLogin.dismiss());

        txtForgotPassWord.setOnClickListener(view -> {
            final Dialog dialog1 = new Dialog(HomeActivity.this);
            dialog1.setTitle(R.string.forgot_password);
            dialog1.setContentView(R.layout.dialog_resetpassword);
            dialog1.setCancelable(false);
            Button btnSend_ResetPass = dialog1.findViewById(R.id.btnSendReset);
            Button btnCancle_ResetPass = dialog1.findViewById(R.id.btnCancleReset);
            EditText edtEmail_ResetPass = dialog1.findViewById(R.id.edtEmailResetPassword);
            btnSend_ResetPass.setOnClickListener(v -> {
                final String emailResetPass = edtEmail_ResetPass.getText().toString();
                if (!emailResetPass.equals("")) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailResetPass)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(HomeActivity.this, "Email khôi phục mật khẩu sẽ được gửi đến Email:" + emailResetPass + " trong giây lát!", Toast.LENGTH_LONG).show();
                                    dialog1.dismiss();
                                } else {
                                    Toast.makeText(HomeActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(HomeActivity.this, "Vui lòng nhập email của bạn!", Toast.LENGTH_SHORT).show();
                }
            });
            dialog1.show();
            btnCancle_ResetPass.setOnClickListener(v -> dialog1.dismiss());
        });

        btnLogin.setOnClickListener((View view) -> {
            loadUsers();
            login();
        });

        txtSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, SigupActivity.class);
            startActivity(intent);
        });

        dialogLogin.show();
    }

    private void loadUsers() {
        DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myData = myDatabase.child("USER");
        myData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot post : snapshot.getChildren()) {
                    User user = post.getValue(User.class);
                    assert user != null;
                    userArrayList.add(new User(user.getUserName(), user.getEmail()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void passSessionDataIntoSharedPreferences(String username, String email) {
        editor.putString("username", username);
        editor.putString("email", email);
        editor.commit();
    }

    private void login() {
        emailLogin = edtEmailLogin.getText().toString();
        String passwordLogin = edtPasswordLogin.getText().toString();

        if (!emailLogin.equals("") && !passwordLogin.equals("")) {

            myAuth.signInWithEmailAndPassword(emailLogin, passwordLogin)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            loadUsers();
                            String userName = "";
                            for (int i = 0; i < userArrayList.size(); i++) {
                                if (userArrayList.get(i).getEmail().equals(emailLogin)) {
                                    userName = userArrayList.get(i).getUserName();
                                    break;
                                }
                            }
                            passSessionDataIntoSharedPreferences(userName, emailLogin);
                            loadSessionData();
                            menuItemLoginLogout.setTitle(R.string.logout);
                            menuItemLoginLogout.setIcon(R.drawable.ic_logout);
                            Toast.makeText(HomeActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            dialogLogin.dismiss();
                        } else {
                            Toast.makeText(HomeActivity.this, "Tai khoan hoac mat khau khong dung", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Vui lòng nhập đủ Tài khoản và Mật khẩu!", Toast.LENGTH_SHORT).show();
        }
    }
}