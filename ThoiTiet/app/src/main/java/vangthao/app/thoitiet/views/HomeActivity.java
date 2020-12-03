package vangthao.app.thoitiet.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
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
import vangthao.app.thoitiet.model.users.SQLiteDatabasKeepLogin;
import vangthao.app.thoitiet.model.users.User;
import vangthao.app.thoitiet.model.users.UserLogin;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    public static TextView txtUsernameHeader, txtEmailHeader, txtForgotPassWord, txtSignUp;
    private MenuItem menuItem_Login_Logout;
    private EditText edtEmailLogin, edtPasswordLogin;
    private String emailLogin;
    private Dialog dialogLogin;
    private FirebaseAuth myAuth;
    private SQLiteDatabasKeepLogin keepUserLogin;
    private ArrayList<UserLogin> userLoginArrayList;
    protected ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Trang chủ");

        loadViews();
        initValue();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            setTitle("Trang chủ");
        }
    }

    private void initValue() {
        myAuth = FirebaseAuth.getInstance();
        View headerView = navigationView.getHeaderView(0);
        txtUsernameHeader = headerView.findViewById(R.id.txtUsernameHeader);
        txtEmailHeader = headerView.findViewById(R.id.txtEmailHeader);
        Menu menu = navigationView.getMenu();
        menuItem_Login_Logout = menu.findItem(R.id.nav_login_out);

        userLoginArrayList = new ArrayList<>();
        userArrayList = new ArrayList<>();

        //load data user
        loadUser();

        //create databse keep user login
        keepUserLogin = new SQLiteDatabasKeepLogin(this, "user_login.sqlite", null, 1);
        //create table UserLogin
        keepUserLogin.queryData("CREATE TABLE IF NOT EXISTS UserLogin(Id INTEGER PRIMARY KEY AUTOINCREMENT,UserName VARCHAR(200),Email VARCHAR(200))");

        getDataUserLogin();

        //setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //kiem tra trang thai dang nhap
        if (userLoginArrayList.size() >= 1) {
            txtUsernameHeader.setText(userLoginArrayList.get(0).getUserName());
            txtEmailHeader.setText(userLoginArrayList.get(0).getEmail());
            menuItem_Login_Logout.setTitle(R.string.logout);
            menuItem_Login_Logout.setIcon(R.drawable.ic_logout);
        } else {
            txtUsernameHeader.setText(R.string.guest);
            txtEmailHeader.setText(R.string.no_email);
            menuItem_Login_Logout.setTitle(R.string.login);
            menuItem_Login_Logout.setIcon(R.drawable.ic_login);
        }

    }


    private void addUserLogin(String username, String email) {
        if (!username.equals("") && !email.equals("")) {
            //insert data
            keepUserLogin.queryData("INSERT INTO UserLogin VALUES(1,'" + username + "','" + email + "')");
        }
    }

    private void xoaUserLogin(final int id) {
        keepUserLogin.queryData("DELETE FROM UserLogin WHERE Id = '" + id + "'");
        userLoginArrayList.clear();
    }

    private void getDataUserLogin() {
        //select data
        Cursor dataCongViec = keepUserLogin.getData("SELECT * FROM UserLogin");
        userLoginArrayList.clear();
        while (dataCongViec.moveToNext()) {
            int id = dataCongViec.getInt(0);
            String userName = dataCongViec.getString(1);
            String email = dataCongViec.getString(2);
            userLoginArrayList.add(new UserLogin(id, userName, email));
        }
    }

    private void loadViews() {
        toolbar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    public void resetDrawerHeader() {
        getDataUserLogin();
        xoaUserLogin(userLoginArrayList.get(0).getId());
        userLoginArrayList.clear();
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
                Intent intentSeeWeatherByPlace = new Intent(HomeActivity.this, SeeWeatherByPlace.class);
                startActivity(intentSeeWeatherByPlace);
                break;
            case R.id.nav_placesmanagement:
                if (txtUsernameHeader.getText().equals(String.valueOf(R.string.guest)) && txtEmailHeader.getText().equals(String.valueOf(R.string.no_email))) {
                    showDialogLogin();
                } else {
                    Intent intentPlacesManagement = new Intent(HomeActivity.this, PlacesManagement.class);
                    startActivity(intentPlacesManagement);
                }
                break;
            case R.id.nav_login_out:
                if (txtUsernameHeader.getText().equals(String.valueOf(R.string.guest)) && txtEmailHeader.getText().equals(String.valueOf(R.string.no_email))) {
                    showDialogLogin();
                } else {
                    resetDrawerHeader();
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
        txtSignUp = dialog.findViewById(R.id.txtSignUp);
        txtForgotPassWord = dialog.findViewById(R.id.txtForgotPassword);
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
                            //keep user login state
                            addUserLogin(userName, emailLogin);
                            txtUsernameHeader.setText(userName);
                            txtEmailHeader.setText(emailLogin);
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