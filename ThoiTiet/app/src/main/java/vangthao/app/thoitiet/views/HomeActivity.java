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
    private MenuItem menuItem_Login_Logout;
    private EditText edtEmailLogin, edtPasswordLogin;
    private String emailLogin;
    private Dialog dialogLogin;
    private FirebaseAuth myAuth;
    private SharedPreferences.Editor editor;
    private ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Trang chủ");

        loadViews();
        loadUser();
        initValue();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            //HomeActivity.this.setTitle("Trang chủ");

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
        menuItem_Login_Logout = menu.findItem(R.id.nav_login_out);

        userArrayList = new ArrayList<>();

        //load data user
        loadUser();
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
            menuItem_Login_Logout.setTitle(R.string.login);
            menuItem_Login_Logout.setIcon(R.drawable.ic_login);
        } else {
            menuItem_Login_Logout.setTitle(R.string.logout);
            menuItem_Login_Logout.setIcon(R.drawable.ic_logout);
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
        menuItem_Login_Logout.setTitle(R.string.login);
        menuItem_Login_Logout.setIcon(R.drawable.ic_login);
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
        final Dialog dialog = new Dialog(HomeActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
        Button btnLogin = dialog.findViewById(R.id.btnLogin);
        TextView txtSignUp = dialog.findViewById(R.id.txtSignUp);
        TextView txtForgotPassWord = dialog.findViewById(R.id.txtForgotPassword);
        edtEmailLogin = dialog.findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = dialog.findViewById(R.id.edtPasswordLogin);
        ImageView imgViewClose = dialog.findViewById(R.id.imgViewClose);

        dialogLogin = dialog;

        imgViewClose.setOnClickListener(view -> dialog.dismiss());

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
            loadUser();
            login();
        });

        txtSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, SigupActivity.class);
            startActivity(intent);
        });

        dialog.show();
    }

    private void loadUser() {
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
                            loadUser();
                            String userName = "";
                            //get username
                            for (int i = 0; i < userArrayList.size(); i++) {
                                if (userArrayList.get(i).getEmail().equals(emailLogin)) {
                                    userName = userArrayList.get(i).getUserName();
                                    break;
                                }
                            }
                            passSessionDataIntoSharedPreferences(userName, emailLogin);
                            loadSessionData();
                            menuItem_Login_Logout.setTitle(R.string.logout);
                            menuItem_Login_Logout.setIcon(R.drawable.ic_logout);
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