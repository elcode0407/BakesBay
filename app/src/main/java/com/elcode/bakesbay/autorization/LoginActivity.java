package com.elcode.bakesbay.autorization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.elcode.bakesbay.R;
import com.elcode.bakesbay.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    ImageButton btnLogin;
    TextView forgotPassword, createAccount;
    ProgressDialog mLoadingBar;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    static boolean f = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        forgotPassword = findViewById(R.id.forgotPassword);
        createAccount = findViewById(R.id.createAccount);
        mLoadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("users");

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptLogin();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    private void AttemptLogin() {
        String username = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (username.isEmpty()) {
            inputEmail.setError("Required line!");
            inputEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            inputPassword.setError("Password is required!");
            inputPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            inputPassword.setError("Min password length should be 6 characters");
            inputPassword.requestFocus();
            return;
        }
        mLoadingBar.setTitle("Login");
        mLoadingBar.setMessage("Please Wait ,While Check your Credential");
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot s :
                            snapshot.getChildren()) {
                        System.out.println(s.getValue().toString());
                        String email = s.child("email").getValue().toString();
                        System.out.println(email);
                        System.out.println(s.child("username").getValue().toString());
                        System.out.println(username);
                        if (username.equals(s.child("username").getValue().toString())) {
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                                        if (user.isEmailVerified()) {
                                            mLoadingBar.dismiss();
                                            Toast.makeText(LoginActivity.this, "Login is Successfully", Toast.LENGTH_SHORT);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            mLoadingBar.dismiss();
                                            Toast.makeText(LoginActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                                            mAuth.signOut();
                                        }
                                    } else {
                                        mLoadingBar.dismiss();
                                        Toast.makeText(LoginActivity.this,"This username,email or password is incorrect!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            return;
                        }
                    }
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                            if (user.isEmailVerified()) {
                                mLoadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Login is Successfully", Toast.LENGTH_SHORT);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                mLoadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                            }
                        } else {
                            mLoadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "This username,email or password is incorrect!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}