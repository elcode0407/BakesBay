package com.elcode.bakesbay.reciep;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elcode.bakesbay.R;
import com.elcode.bakesbay.adapter.CommentAdapter;
import com.elcode.bakesbay.model.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipePage2 extends AppCompatActivity {

    public List<Comment> commentList = new ArrayList<>();
    RecyclerView commentRecycler;
    static CommentAdapter commentAdapter;
    int i = 0;

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page2);
        //Обьявляю перемменые

        DatabaseReference mRef3 = FirebaseDatabase.getInstance().getReference().child("commentCount").child(getIntent().getStringExtra("id")).child("count");
        DatabaseReference mRef4 = FirebaseDatabase.getInstance().getReference().child("comment").child(getIntent().getStringExtra("id"));
        commentRecycler = findViewById(R.id.commentR);
        setList();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        ImageView pageImage = findViewById(R.id.recipePageImage);
        TextView title = findViewById(R.id.pageTitle);
        TextView cookTime = findViewById(R.id.pageCookTime);
        TextView serves = findViewById(R.id.pageServes);
        TextView prepTime = findViewById(R.id.pagePrepTime);
        TextView access = findViewById(R.id.pageAccess);
        TextView description = findViewById(R.id.pageDescription);
        TextView ingredients = findViewById(R.id.pageIngredients);
        TextView directions = findViewById(R.id.pageDirectons);
        TextView show = findViewById(R.id.show);
        show.setPaintFlags(show.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //Беру данные про рецепт с адаптера
        Glide.with(this).load(getIntent().getStringExtra("recipeImage")).into(pageImage);
        title.setText(getIntent().getStringExtra("recipeTitle"));
        cookTime.setText(getIntent().getStringExtra("recipeCookTime"));
        serves.setText(getIntent().getStringExtra("recipeServes"));
        prepTime.setText(getIntent().getStringExtra("recipePrepTime"));
        access.setText(getIntent().getStringExtra("recipeAccess"));
        description.setText(getIntent().getStringExtra("recipeDesc"));
        ingredients.setText(getIntent().getStringExtra("recipeIngr"));
        directions.setText(getIntent().getStringExtra("recipeDire"));

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 0) {
                    commentRecycler.setVisibility(View.VISIBLE);
                    setCommentRecycler(commentList);
                    show.setText("Hide comments");
                    i = 1;
                } else {
                    List<Comment> commentList2 = new ArrayList<>();
                    setCommentRecycler(commentList2);
                    commentRecycler.setVisibility(View.INVISIBLE);
                    show.setText("Show comments");
                    i = 0;
                }
            }
        });


    }

    public void setList() {
        commentList.clear();
        DatabaseReference mRef3 = FirebaseDatabase.getInstance().getReference().child("commentCount").child(getIntent().getStringExtra("id")).child("count");
        DatabaseReference mRef4 = FirebaseDatabase.getInstance().getReference().child("comment").child(getIntent().getStringExtra("id"));
        int[] s = new int[1];
        mRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String b = snapshot.getValue().toString();
                s[0] = Integer.parseInt(b);
                int z = s[0] - 1;
                for (int i = 1; i <= s[0] - 1; i++) {
                    System.out.println("i: " + i);
                    System.out.println("count: " + s[0]);
                    System.out.println(z);
                    mRef4.child("comment" + z).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() == null) {
                                System.out.println("s");
                            } else {
                                commentList.add(new Comment(snapshot.child("anonymous").getValue().toString(), snapshot.child("id").getValue().toString(), snapshot.child("id2").getValue().toString(), snapshot.child("text").getValue().toString(), snapshot.child("date").getValue().toString()));
                                System.out.println(snapshot.getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    z--;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCommentRecycler(List<Comment> commentList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        commentRecycler.setLayoutManager(layoutManager);

        commentAdapter = new CommentAdapter(getApplicationContext(), commentList);
        commentRecycler.setAdapter(commentAdapter);

    }
}