package com.elcode.bakesbay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.elcode.bakesbay.adapter.CategoryAdapter;
import com.elcode.bakesbay.adapter.RecipeAdapter;
import com.elcode.bakesbay.model.Category;
import com.elcode.bakesbay.model.Recipe;
import com.elcode.bakesbay.reciep.AddReciepActivity;
import com.elcode.bakesbay.reciep.MyFavorite;
import com.elcode.bakesbay.reciep.MyRecipe;
import com.elcode.bakesbay.user.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ImageView btnAdd, btnHome, btnMyRecipe, btnFavorite;
    CircleImageView btnProfile;
    RecyclerView recipeRecycler;
    static RecipeAdapter recipeAdapter;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    DatabaseReference mRef2;
    DatabaseReference mRef3;
    DatabaseReference mRef4;

    StorageReference sRef;
    RecyclerView categoryRecycler;
    CategoryAdapter categoryAdapter;
    public static List<Recipe> recipeList = new ArrayList<>();
    public static List<Recipe> recipeList2 = new ArrayList<>();
    public static List<Recipe> recipeList3 = new ArrayList<>();
    public static List<Recipe> fullList = new ArrayList<>();
    public static List<Recipe> fullList2 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipeList.clear();
        recipeList2.clear();
        recipeList3.clear();
        fullList2.clear();
        fullList.clear();

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("Appetizer"));
        categoryList.add(new Category("Beverage"));
        categoryList.add(new Category("Breads"));
        categoryList.add(new Category("Breakfast"));
        categoryList.add(new Category("Dessert"));
        categoryList.add(new Category("Lunch"));
        categoryList.add(new Category("Main Dish"));
        categoryList.add(new Category("Salad"));
        categoryList.add(new Category("Side Dish"));
        categoryList.add(new Category("Snacks"));
        categoryList.add(new Category("Soups"));
        categoryList.add(new Category("Other"));
        setCategoryRecycler(categoryList);

        {
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid());
            mRef2 = FirebaseDatabase.getInstance().getReference().child("recipes");
            mRef4 = FirebaseDatabase.getInstance().getReference().child("favorite").child(mUser.getUid());
            mRef3 = FirebaseDatabase.getInstance().getReference().child("count").child(mUser.getUid());
            sRef = FirebaseStorage.getInstance().getReference().child("profileImage");
            btnFavorite = findViewById(R.id.myFavorite);
            btnAdd = findViewById(R.id.addRecieptBtn);
            btnHome = findViewById(R.id.homePageBtn);
            btnMyRecipe = findViewById(R.id.myRecipe);
            btnProfile = findViewById(R.id.profile_image);

        }

        int[] s = new int[1];
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyFavorite.class);
                startActivity(intent);
            }
        });
        mRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String b = snapshot.child("count").getValue().toString();
                s[0] = Integer.parseInt(b);
                int z = s[0] - 1;
                for (int i = 1; i <= s[0] - 1; i++) {
                    System.out.println("i: " + i);
                    System.out.println("count: " + s[0]);
                    System.out.println(z);
                    mRef2.child(mUser.getUid() + z).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() == null) {

                            } else {
                                recipeList.add(snapshot.getValue(Recipe.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    z--;
                }
                mRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        mRef4.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                for (DataSnapshot s :
                                        snapshot2.getChildren()) {
                                    System.out.println(s.child("id").getValue().toString());
                                    mRef4.child(s.child("id").getValue().toString()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.getValue() == null) {

                                            } else {
                                                System.out.println("8884" + s.getValue().toString());
                                                recipeList3.add(s.getValue(Recipe.class));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                fullList.clear();
                mRef.child("username").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot s1 : snapshot1.getChildren()) {
                            System.out.println(s1.child("username").getValue().toString());
                            System.out.println(4 + snapshot.getValue().toString());
                            if (s1.child("username").getValue().toString().equals(snapshot.getValue().toString())) {
                                System.out.println("equal");
                            } else {
                                System.out.println("Not equal");
                                recipeList2.add(s1.getValue(Recipe.class));
                            }
                            fullList2.add(s1.getValue(Recipe.class));
                        }
                        System.out.println(4940);
                        fullList.addAll(recipeList2);
                        setRecipeRecycler(fullList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRef.child("profileImage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(getApplicationContext()).load(snapshot.getValue().toString()).into(btnProfile);
                System.out.println(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        btnMyRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyRecipe.class);
                startActivity(intent);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddReciepActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setRecipeRecycler(List<Recipe> recipeList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        recipeRecycler = findViewById(R.id.myRecipe1);
        recipeRecycler.setLayoutManager(layoutManager);

        recipeAdapter = new RecipeAdapter(MainActivity.this, recipeList);
        recipeRecycler.setAdapter(recipeAdapter);

    }

    private void setCategoryRecycler(List<Category> categoryList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        categoryRecycler = findViewById(R.id.categoryRecycler);
        categoryRecycler.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryRecycler.setAdapter(categoryAdapter);

    }

    @SuppressLint("NotifyDataSetChanged")
    public void openMain(View view) {
        recipeList2.clear();
        System.out.println(11);
        categoryRecycler.setVisibility(View.VISIBLE);

        for (Recipe c : fullList2) {
            recipeList2.add(c);
        }

        setRecipeRecycler(recipeList2);
        recipeAdapter.notifyDataSetChanged();

    }

    @SuppressLint("NotifyDataSetChanged")
    public static void showCoursesByCategory(String category) {
        recipeList2.clear();
        System.out.println(11);
        recipeList2.addAll(fullList2);


        List<Recipe> filterCourses = new ArrayList<>();

        for (Recipe c : recipeList2) {
            System.out.println(c.getCategory());
            System.out.println(category);
            if (c.getCategory().equals(category)) {
                filterCourses.add(c);
            }
        }

        recipeList2.clear();
        recipeList2.addAll(filterCourses);
        recipeAdapter.notifyDataSetChanged();
    }
}