package vangthao.app.thoitiet.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.SQLiteDatabasKeepLogin;
import vangthao.app.thoitiet.model.User;
import vangthao.app.thoitiet.model.UserLogin;
import vangthao.app.thoitiet.model.WeatherResponse;
import vangthao.app.thoitiet.viewmodel.APIUtils;
import vangthao.app.thoitiet.viewmodel.WeatherService;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private View headerView;
    private NavigationView navigationView;
    private TextView txtUsernameHeader, txtEmailHeader, txtForgotPassWord, txtSignUp;
    private Menu menu;
    private MenuItem menuItem_Login_Logout;
    private Button btnLogin;
    private EditText edtEmailLogin, edtPasswordLogin;
    private ImageView imgViewClose;
    private String emailLogin, passwordLogin;
    private Dialog dialogLogin;
    private FirebaseAuth myAuth;
    private SQLiteDatabasKeepLogin keepUserLogin;
    private ArrayList<UserLogin> mangUserLogin;
    protected ArrayList<User> mangUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Trang chủ");

        initView();
        initValue();
        events();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            setTitle("Trang chủ");

        }
    }

    private void initValue() {
        myAuth = FirebaseAuth.getInstance();
        headerView = navigationView.getHeaderView(0);
        txtUsernameHeader = headerView.findViewById(R.id.txtUsernameHeader);
        txtEmailHeader = headerView.findViewById(R.id.txtEmailHeader);
        menu = navigationView.getMenu();
        menuItem_Login_Logout = menu.findItem(R.id.nav_login_out);

        mangUserLogin = new ArrayList<>();
        mangUser = new ArrayList<>();

        //load data user
        LoadUser();

        //create databse keep user login
        keepUserLogin = new SQLiteDatabasKeepLogin(this, "user_login.sqlite", null, 1);
        //create table UserLogin
        keepUserLogin.QueryData("CREATE TABLE IF NOT EXISTS UserLogin(Id INTEGER PRIMARY KEY AUTOINCREMENT,UserName VARCHAR(200),Email VARCHAR(200))");

        GetDataUserLogin();

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
        if (mangUserLogin.size() >= 1) {
            txtUsernameHeader.setText(mangUserLogin.get(0).getUserName());
            txtEmailHeader.setText(mangUserLogin.get(0).getEmail());
            menuItem_Login_Logout.setTitle("Đăng xuất");
            menuItem_Login_Logout.setIcon(R.drawable.ic_logout);
        } else {
            txtUsernameHeader.setText("Guest");
            txtEmailHeader.setText("No Email");
            menuItem_Login_Logout.setTitle("Đăng nhập");
            menuItem_Login_Logout.setIcon(R.drawable.ic_login);
        }

    }


    private void AddUserLogin(String username, String email) {
        if (!username.equals("") && !email.equals("")) {
            //insert data
            keepUserLogin.QueryData("INSERT INTO UserLogin VALUES(1,'" + username + "','" + email + "')");
        }
    }

    private void XoaUserLogin(final int id) {
        keepUserLogin.QueryData("DELETE FROM UserLogin WHERE Id = '" + id + "'");
        mangUserLogin.clear();
    }

    private void GetDataUserLogin() {
        //select data
        Cursor dataCongViec = keepUserLogin.GetData("SELECT * FROM UserLogin");
        mangUserLogin.clear();
        while (dataCongViec.moveToNext()) {
            int id = dataCongViec.getInt(0);
            String userName = dataCongViec.getString(1);
            String email = dataCongViec.getString(2);
            mangUserLogin.add(new UserLogin(id, userName, email));
        }
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
                if (txtUsernameHeader.getText().equals("Guest") && txtEmailHeader.getText().equals("No Email")) {
                    ShowDialogLogin();
                } else {
                    GetDataUserLogin();
                    XoaUserLogin(mangUserLogin.get(0).getId());
                    mangUserLogin.clear();
                    txtUsernameHeader.setText("Guest");
                    txtEmailHeader.setText("No Email");
                    menuItem_Login_Logout.setTitle("Đăng nhập");
                    menuItem_Login_Logout.setIcon(R.drawable.ic_login);
                }
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ShowDialogLogin() {
        final Dialog dialog = new Dialog(HomeActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dang_nhap);
        btnLogin = dialog.findViewById(R.id.btnLogin);
        txtSignUp = dialog.findViewById(R.id.txtSignUp);
        txtForgotPassWord = dialog.findViewById(R.id.txtForgotPassword);
        edtEmailLogin = dialog.findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = dialog.findViewById(R.id.edtPasswordLogin);
        imgViewClose = dialog.findViewById(R.id.imgViewClose);

        dialogLogin = dialog;

        imgViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtForgotPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(HomeActivity.this);
                dialog.setTitle("Quên Mật Khẩu");
                dialog.setContentView(R.layout.dialog_resetpassword);
                dialog.setCancelable(false);
                Button btnSend_ResetPass = dialog.findViewById(R.id.btnSendReset);
                Button btnCancle_ResetPass = dialog.findViewById(R.id.btnCancleReset);
                EditText edtEmail_ResetPass = dialog.findViewById(R.id.edtEmailResetPassword);
                btnSend_ResetPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String emailResetPass = edtEmail_ResetPass.getText().toString();
                        if (!emailResetPass.equals("")) {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(emailResetPass)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(HomeActivity.this, "Email khôi phục mật khẩu sẽ được gửi đến Email:" + emailResetPass + " trong giây lát!", Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(HomeActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(HomeActivity.this, "Vui lòng nhập email của bạn!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
                btnCancle_ResetPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        btnLogin.setOnClickListener((View view) -> {
            //Toast.makeText(MainActivity.this, "Click on Dang Nhap", Toast.LENGTH_SHORT).show();
            LoadUser();
            Login();
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Click on Dang Ky", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, SigupActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void LoadUser() {
        DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myData = myDatabase.child("USER");
        myData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mangUser.clear();
                for (DataSnapshot post : snapshot.getChildren()) {
                    User user = post.getValue(User.class);
                    mangUser.add(new User(user.getUserName(), user.getEmail()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Login() {
        emailLogin = edtEmailLogin.getText().toString();
        passwordLogin = edtPasswordLogin.getText().toString();

        if (!emailLogin.equals("") && !passwordLogin.equals("")) {

            myAuth.signInWithEmailAndPassword(emailLogin, passwordLogin)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                LoadUser();
                                String userName = "";
                                Toast.makeText(HomeActivity.this, "size:" + mangUser.size(), Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < mangUser.size(); i++) {
                                    if (mangUser.get(i).getEmail().equals(emailLogin)) {
                                        userName = mangUser.get(i).getUserName();
                                        break;
                                    }
                                }
                                Toast.makeText(HomeActivity.this, "User name:" + userName, Toast.LENGTH_SHORT).show();
                                AddUserLogin(userName, emailLogin);
                                txtUsernameHeader.setText(userName);
                                txtEmailHeader.setText(emailLogin);
                                menuItem_Login_Logout.setTitle("Đăng xuất");
                                menuItem_Login_Logout.setIcon(R.drawable.ic_logout);
                                Toast.makeText(HomeActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                dialogLogin.dismiss();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Vui lòng nhập đủ Tài khoản và Mật khẩu!", Toast.LENGTH_SHORT).show();
        }
    }
}