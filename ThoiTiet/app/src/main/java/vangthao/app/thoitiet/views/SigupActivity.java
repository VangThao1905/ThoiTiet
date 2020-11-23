package vangthao.app.thoitiet.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.User;

public class SigupActivity extends AppCompatActivity {

    private EditText edtUsernameSigup, edtEmailSigup, edtPasswordSigup, edtRetypePasswordSigup;
    private Button btnSigup, btnCancleSigup;
    private TextView txtHaveAccount;
    private String userName,email,passWord,retypePassword;
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
        txtHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sigup();
            }
        });

        btnCancleSigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Sigup() {
        userName = edtUsernameSigup.getText().toString();
        email = edtEmailSigup.getText().toString();
        passWord = edtPasswordSigup.getText().toString();
        retypePassword = edtRetypePasswordSigup.getText().toString();

        if (!userName.equals("") && !email.equals("") && !passWord.equals("") && !retypePassword.equals("")) {
            if (retypePassword.equals(passWord)) {
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
        myAuth.createUserWithEmailAndPassword(email, passWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(SigupActivity.this, "OK", Toast.LENGTH_SHORT).show();
                            User user = new User(userName, email);
                            Toast.makeText(SigupActivity.this, ""+user.getUserName() + "," + user.getEmail(), Toast.LENGTH_SHORT).show();
                            if (user != null) {
                                myDatabase.child("USER").push().setValue(user, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            //Toast.makeText(SigupActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SigupActivity.this, "Lỗi đăng ký!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                            Toast.makeText(SigupActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            edtUsernameSigup.setText("");
                            edtEmailSigup.setText("");
                            edtPasswordSigup.setText("");
                            edtRetypePasswordSigup.setText("");
                            finish();
                        } else {
                            Toast.makeText(SigupActivity.this, "Email không hợp lệ hoặc đã được sử dụng!", Toast.LENGTH_SHORT).show();
                        }
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