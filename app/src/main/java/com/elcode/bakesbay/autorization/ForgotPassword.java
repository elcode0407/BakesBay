package com.elcode.bakesbay.autorization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elcode.bakesbay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //здесь я обьявляю свою кнопку и место ввода текста
        emailEditText = findViewById(R.id.email);
        resetPasswordButton = findViewById(R.id.resetPassword);

        auth = FirebaseAuth.getInstance();

        //ставлю праслущиватель на нажатие кнопки
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //вызываю функцию сброситьПароль()
                resetPassword();
                Toast.makeText(getApplicationContext(), "Check your email to reset your password!", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    //Функция сброса пароля
    private void resetPassword() {
        String email = emailEditText.getText().toString();

        //Проверяю ввод на наличие каких-то проблем
        if (email.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide valid email");
            emailEditText.requestFocus();
            return;
        }

        //Отправляю на емаил ссылку на смену пароля
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(ForgotPassword.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(ForgotPassword.this,"Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}