package com.elcode.bakesbay.reciep;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class RecipePage extends AppCompatActivity {

    public List<Comment> commentList1 = new ArrayList<>();
    static public RecyclerView commentRecycler;
    static public TextView show;
    static public CommentAdapter commentAdapter;
    static public int i = 0;

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);
        //Обьявляю перемменые
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid());
        DatabaseReference mRef3 = FirebaseDatabase.getInstance().getReference().child("commentCount").child(getIntent().getStringExtra("id")).child("count");
        DatabaseReference mRef4 = FirebaseDatabase.getInstance().getReference().child("comment").child(getIntent().getStringExtra("id"));
        commentRecycler = findViewById(R.id.commentR);

        ImageView pageImage = findViewById(R.id.recipePageImage);
        Button add = findViewById(R.id.add);
        CheckBox anonymBox = findViewById(R.id.anonymBox);
        CheckBox notAnonymBox = findViewById(R.id.notAnonymBox);
        TextView title = findViewById(R.id.pageTitle);
        EditText comment = findViewById(R.id.comment);
        TextView cookTime = findViewById(R.id.pageCookTime);
        TextView serves = findViewById(R.id.pageServes);
        TextView prepTime = findViewById(R.id.pagePrepTime);
        TextView access = findViewById(R.id.pageAccess);
        TextView description = findViewById(R.id.pageDescription);
        TextView ingredients = findViewById(R.id.pageIngredients);
        TextView directions = findViewById(R.id.pageDirectons);
        show = findViewById(R.id.show);
        TextView addC = findViewById(R.id.addC);
        TextView sentAs = findViewById(R.id.sentAs);
        show.setPaintFlags(show.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        notAnonymBox.setChecked(true);
        anonymBox.setChecked(false);
        anonymBox.setSelected(false);
        anonymBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anonymBox.setChecked(true);
                anonymBox.setSelected(true);
                notAnonymBox.setChecked(false);
                notAnonymBox.setSelected(false);
            }
        });
        notAnonymBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notAnonymBox.setChecked(true);
                notAnonymBox.setSelected(true);
                anonymBox.setChecked(false);
                anonymBox.setSelected(false);
            }
        });

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
        if (getIntent().getStringExtra("id2").equals(mUser.getUid())) {
            addC.setVisibility(View.INVISIBLE);
            comment.setVisibility(View.INVISIBLE);
            sentAs.setVisibility(View.INVISIBLE);
            anonymBox.setVisibility(View.INVISIBLE);
            notAnonymBox.setVisibility(View.INVISIBLE);
            add.setVisibility(View.INVISIBLE);
        }
        add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                String comment2 = comment.getText().toString();
                if (comment2.isEmpty()) {
                    comment.setError("Comment is empty!");
                    comment.requestFocus();
                    return;
                } else {
                    mRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mRef.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                    HashMap<String, String> map = new HashMap<>();
                                    long millis = System.currentTimeMillis();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    java.sql.Date date = new java.sql.Date(millis);
                                    String frmtdDate = dateFormat.format(date);
                                    map.put("id", mUser.getUid());
                                    map.put("date", frmtdDate);
                                    map.put("id2", getIntent().getStringExtra("id"));
                                    map.put("text", comment2);
                                    if (anonymBox.isChecked()) {
                                        map.put("anonymous", "Yes");
                                    } else {
                                        map.put("anonymous", "No");
                                    }
                                    mRef3.setValue(Integer.parseInt(snapshot.getValue().toString()) + 1);

                                    mRef4.child("comment" + snapshot.getValue().toString()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            setList();
                                            setCommentRecycler(commentList1);
                                            commentAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    comment.setText("");
                                    notAnonymBox.setChecked(true);
                                    anonymBox.setChecked(false);
                                    anonymBox.setSelected(false);

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
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 0) {
                    commentRecycler.setVisibility(View.VISIBLE);
                    setCommentRecycler(commentList1);
                    show.setText("Hide comments");
                    i = 1;
                } else {
                    setList();
                    List<Comment> commentList2 = new ArrayList<>();
                    setCommentRecycler(commentList2);
                    commentRecycler.setVisibility(View.INVISIBLE);
                    show.setText("Show comments");
                    i = 0;
                }
            }
        });
        setList();
    }

    public synchronized void setList() {
        commentList1.clear();
        System.out.println("setList");
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
                    mRef4.child("comment" + z).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() == null) {
                                System.out.println("s");
                            } else {
                                commentList1.add(new Comment(snapshot.child("anonymous").getValue().toString(), snapshot.child("id").getValue().toString(), snapshot.child("id2").getValue().toString(), snapshot.child("text").getValue().toString(), snapshot.child("date").getValue().toString()));
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

    public void setCommentRecycler(List<Comment> commentList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        commentRecycler.setLayoutManager(layoutManager);

        commentAdapter = new CommentAdapter(getApplicationContext(), commentList);
        commentRecycler.setAdapter(commentAdapter);

    }
}