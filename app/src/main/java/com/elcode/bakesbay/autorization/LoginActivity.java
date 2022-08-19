package com.elcode.bakesbay.autorization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Обьявляю перемменые
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        forgotPassword = findViewById(R.id.forgotPassword);
        createAccount = findViewById(R.id.createAccount);
        mLoadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("users");

        //ставлю праслущиватель на нажатие кнопки
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

    //Функция входа пользователя в аккаунт
    private void AttemptLogin() {
        String usernameEmail = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        //Проверяю ввод на наличие каких-то проблем
        if (usernameEmail.isEmpty()) {
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
        //Включаю Загрузочное Окно
        mLoadingBar.setTitle("Login");
        mLoadingBar.setMessage("Please Wait ,While Check your Credential");
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();
        //Вход пользователя в аккаунт
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot s :
                            snapshot.getChildren()) {
                        System.out.println(s.getValue().toString());
                        String email = s.child("email").getValue().toString();
                        System.out.println(email);
                        System.out.println(s.child("username").getValue().toString());
                        System.out.println(usernameEmail);
                        //Делаю возможность входа через емаил и имя пользователя
                        //Проверяю на наличие имени пользователя если есть то взожу через
                        //имя пользователя если нет кидаю ошибку на неналичие имени пользователя
                        if (usernameEmail.equals(s.child("username").getValue().toString())) {
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                                        //Проверяю потвердил ли узер свой емаил
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
                //Вхожу через емаил если пользователь так захотел

                mAuth.signInWithEmailAndPassword(usernameEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                            if (user.isEmailVerified()) {
                                mLoadingBar.dismiss();

                                Toast.makeText(LoginActivity.this, "Login is Successfully", Toast.LENGTH_SHORT).show();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                mLoadingBar.dismiss();
                                user.sendEmailVerification();
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle("Alert!")
                                        .setMessage("Check your email to verify your account!Look in spam folder in your email if you don`t receive verification email")
                                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // Закрываем окно
                                                dialog.cancel();
                                            }
                                        });
                                builder.show();
                                builder.create();
                                Toast.makeText(LoginActivity.this, "Check your email to verify your account!Look in spam folder in your email if you don`t receive verification email", Toast.LENGTH_LONG).show();
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