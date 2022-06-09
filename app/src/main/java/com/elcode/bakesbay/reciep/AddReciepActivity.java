package com.elcode.bakesbay.reciep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.elcode.bakesbay.MainActivity;
import com.elcode.bakesbay.R;
import com.elcode.bakesbay.user.EditProfileActivity;

public class AddReciepActivity extends AppCompatActivity {
    Button btn,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reciep);
        btn = findViewById(R.id.button2);
        btn2= findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("ResourceType") Intent intent = new Intent(AddReciepActivity.this, findViewById(R.layout.activity_add_reciep_2).getClass());
                startActivity(intent);
            }
        });
    }
}