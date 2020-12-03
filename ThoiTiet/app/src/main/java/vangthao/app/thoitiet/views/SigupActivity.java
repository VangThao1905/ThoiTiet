package vangthao.app.thoitiet.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.users.User;

public class SigupActivity extends AppCompatActivity {

    private EditText edtUsernameSigup, edtEmailSigup, edtPasswordSigup, edtRetypePasswordSigup;
    private Button btnSigup, btnCancleSigup;
    private TextView txtHaveAccount;
    private String userName;
    private String email;
    private String password;
    private FirebaseAuth myAuth;

    public static DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup);

        initView();
        intitValue();
        events();

    }

    private void events() {
        txtHaveAccount.setOnClickListener(v -> finish());

        btnSigup.setOnClickListener(v -> Sigup());

        btnCancleSigup.setOnClickListener(v -> finish());
    }

    private void Sigup() {
        userName = edtUsernameSigup.getText().toString();
        email = edtEmailSigup.getText().toString();
        password = edtPasswordSigup.getText().toString();
        String retypePassword = edtRetypePasswordSigup.getText().toString();

        if (!userName.equals("") && !email.equals("") && !password.equals("") && !retypePassword.equals("")) {
            if (retypePassword.equals(password)) {
                //Toast.makeText(this, ""+userName + ","+passWord, Toast.LENGTH_SHORT).show();
                AddUser();
            } else {
                Toast.makeText(SigupActivity.this, "Mật khẩu và Nhập lại mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin đăng ký!", Toast.LENGTH_SHORT).show();
        }

    }

    private void AddUser() {
        //Toast.makeText(this, "This is function AddUser", Toast.LENGTH_SHORT).show();
        myAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        User user = new User(userName, email);
                        Toast.makeText(SigupActivity.this, "" + user.getUserName() + "," + user.getEmail(), Toast.LENGTH_SHORT).show();
                        myDatabase.child("USER").push().setValue(user, (databaseError, databaseReference) -> {
                            if (!(databaseError == null)) {
                                Toast.makeText(SigupActivity.this, "Lỗi đăng ký!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Toast.makeText(SigupActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        edtUsernameSigup.setText("");
                        edtEmailSigup.setText("");
                        edtPasswordSigup.setText("");
                        edtRetypePasswordSigup.setText("");
                        finish();
                    } else {
                        Toast.makeText(SigupActivity.this, "Email không hợp lệ hoặc đã được sử dụng!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void intitValue() {
        edtUsernameSigup.requestFocus();
        myAuth = FirebaseAuth.getInstance();
    }

    private void initView() {
        edtUsernameSigup = findViewById(R.id.edtUsernameSigup);
        edtEmailSigup = findViewById(R.id.edtEmailSigup);
        edtPasswordSigup = findViewById(R.id.edtPasswordSigup);
        edtRetypePasswordSigup = findViewById(R.id.edtRetypePasswordSigup);
        txtHaveAccount = findViewById(R.id.txtHaveAccount);
        btnSigup = findViewById(R.id.btnSigup);
        btnCancleSigup = findViewById(R.id.btnCancleSigup);
    }
}