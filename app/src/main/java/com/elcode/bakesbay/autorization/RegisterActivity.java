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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputPasswordConfirm;
    ImageButton btnRegister;
    TextView alreadyAccount;
    FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordConfirm = findViewById(R.id.inputPasswordConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        alreadyAccount = findViewById(R.id.createAccount);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(this);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtemtRegistration();
            }
        });
        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AtemtRegistration() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputPasswordConfirm.getText().toString();

        if (email.isEmpty()) {
            inputEmail.setError("Email is required!");
            inputEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Please provide valid email");
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
        if (!confirmPassword.equals(password)) {
            inputPasswordConfirm.setError("Password did not match");
            inputPasswordConfirm.requestFocus();
            return;
        }
        mLoadingBar.setTitle("Registration");
        mLoadingBar.setMessage("Please Wait ,While Check your Credential");
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                if (task.isSuccessful()) {
                    System.out.println("good");
                    user.sendEmailVerification();
                    Toast.makeText(RegisterActivity.this, "Registration is Successfully", Toast.LENGTH_LONG).show();
                    mLoadingBar.dismiss();
                } else {
                    System.out.println(task.getException().toString());
                    task.getException();
                    System.out.println(task.getResult().toString());
                    task.getResult();
                    System.out.println("good1");
                    user.sendEmailVerification();
                    mLoadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Registration is Successfully", Toast.LENGTH_LONG).show();
                }
                System.out.println("good2");
                user.sendEmailVerification();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(RegisterActivity.this, "Registration is Successfully", Toast.LENGTH_LONG).show();
            }
        });

    }
}