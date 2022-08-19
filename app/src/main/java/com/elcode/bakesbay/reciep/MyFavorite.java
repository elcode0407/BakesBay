package com.elcode.bakesbay.reciep;

import static com.elcode.bakesbay.MainActivity.recipeList;
import static com.elcode.bakesbay.MainActivity.recipeList3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.elcode.bakesbay.MainActivity;
import com.elcode.bakesbay.R;
import com.elcode.bakesbay.adapter.RecipeAdapter;
import com.elcode.bakesbay.adapter.RecipeAdapter2;
import com.elcode.bakesbay.model.Recipe;
import com.elcode.bakesbay.user.ProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFavorite extends AppCompatActivity {
    ImageView btnAdd, btnHome, btnMyRecipe;
    CircleImageView btnProfile;
    RecyclerView recipeRecycler;
    static RecipeAdapter recipeAdapter;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    DatabaseReference mRef2;
    StorageReference sRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid());
        mRef2 = FirebaseDatabase.getInstance().getReference().child("recipes");
        sRef = FirebaseStorage.getInstance().getReference().child("profileImage");
        btnMyRecipe = findViewById(R.id.myRecipe);
        btnAdd = findViewById(R.id.addRecipeBtn);
        btnHome = findViewById(R.id.homePageBtn);
        btnProfile = findViewById(R.id.profile_image);

        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(recipeList3.toString());
                setRecipeRecycler(recipeList3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRef.child("profileImage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println(uri.toString());
                        Glide.with(getApplicationContext()).load(uri).into(btnProfile);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyFavorite.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyFavorite.this, AddReciepActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyFavorite.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        btnMyRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyFavorite.this, MyRecipe.class);
                startActivity(intent);
            }
        });
    }
    private void setRecipeRecycler(List<Recipe> recipeList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        recipeRecycler = findViewById(R.id.myRecipe1);
        recipeRecycler.setLayoutManager(layoutManager);

        recipeAdapter = new RecipeAdapter(this, recipeList);
        recipeRecycler.setAdapter(recipeAdapter);

    }
    public Context getContext(){
        return getApplicationContext();
    }
}