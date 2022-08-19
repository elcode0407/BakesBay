package com.elcode.bakesbay.adapter;


import static com.elcode.bakesbay.reciep.RecipePage.commentAdapter;
import static com.elcode.bakesbay.reciep.RecipePage.commentRecycler;
import static com.elcode.bakesbay.reciep.RecipePage.i;
import static com.elcode.bakesbay.reciep.RecipePage.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elcode.bakesbay.R;
import com.elcode.bakesbay.model.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    int i1 = 0;
    String text2;

    Context context;
    public List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View commentItems = LayoutInflater.from(context).inflate(R.layout.comment_text, parent, false);
        return new CommentViewHolder(commentItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseReference mRef4 = FirebaseDatabase.getInstance().getReference().child("comment").child(commentList.get(position).getId2());
        holder.edit.setImageResource(R.drawable.ic_icons8_edit__1_);
        holder.delete.setImageResource(R.drawable.ic_icons8_delete);
        holder.text.setEnabled(false);
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text2 = commentList.get(position).getDate();
                String text = holder.text.getText().toString();
                if (text.isEmpty()) {
                    holder.text.setError("Text is required");
                    holder.text.requestFocus();
                    return;
                } else if (text2.equals(text)) {
                    return;
                } else {
                    mRef4.addValueEventListener(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot s :
                                    snapshot.getChildren()) {
                                System.out.println(text2);
                                if (s.child("date").getValue().toString().equals(text2)) {
                                    mRef4.child(s.getKey().toString()).child("text").setValue(text).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            i1 = 0;
                                            holder.editBtn.setVisibility(View.INVISIBLE);
                                            holder.text.setEnabled(false);
                                            Drawable buttonDrawable = holder.text.getBackground();
                                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                                            DrawableCompat.setTint(buttonDrawable, Color.BLACK);
                                            holder.text.setBackground(buttonDrawable);
                                            List<Comment> commentList2 = new ArrayList<>();
                                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                            commentRecycler.setLayoutManager(layoutManager);
                                            commentAdapter = new CommentAdapter(context, commentList2);
                                            commentRecycler.setAdapter(commentAdapter);
                                            commentRecycler.setVisibility(View.INVISIBLE);
                                            show.setText("Show comments");
                                            i = 0;

                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            i1 = 0;
                                            holder.editBtn.setVisibility(View.INVISIBLE);
                                            holder.text.setEnabled(false);
                                            Drawable buttonDrawable = holder.text.getBackground();
                                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                                            DrawableCompat.setTint(buttonDrawable, Color.BLACK);
                                            holder.text.setBackground(buttonDrawable);
                                            List<Comment> commentList2 = new ArrayList<>();
                                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                            commentRecycler.setLayoutManager(layoutManager);
                                            commentAdapter = new CommentAdapter(context, commentList2);
                                            commentRecycler.setAdapter(commentAdapter);
                                            commentRecycler.setVisibility(View.INVISIBLE);
                                            show.setText("Show comments");
                                            i = 0;
                                        }
                                    });
                                } else {
                                    System.out.println("no");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    holder.text.setText(text);
                    commentList.get(position).setText(text);
                }
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (i1 == 0) {
                    holder.text.setEnabled(true);
                    Drawable buttonDrawable = holder.text.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.BLACK);
                    holder.text.setBackground(buttonDrawable);
                    holder.editBtn.setVisibility(View.VISIBLE);
                    i1 = 1;
                } else {
                    holder.text.setEnabled(false);
                    Drawable buttonDrawable = holder.text.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.TRANSPARENT);
                    holder.text.setBackground(buttonDrawable);
                    holder.text.setText(commentList.get(position).getText());
                    holder.editBtn.setVisibility(View.INVISIBLE);
                    i1 = 0;
                }

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef4.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            text2 = commentList.get(position).getDate();
                        } catch (IndexOutOfBoundsException e) {

                        }
                        for (DataSnapshot s :
                                snapshot.getChildren()) {
                            System.out.println(text2);
                            if (s.child("date").getValue().toString().equals(text2)) {
                                mRef4.child(s.getKey().toString()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        List<Comment> commentList2 = new ArrayList<>();
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                        commentRecycler.setLayoutManager(layoutManager);
                                        commentAdapter = new CommentAdapter(context, commentList2);
                                        commentRecycler.setAdapter(commentAdapter);
                                        commentRecycler.setVisibility(View.INVISIBLE);
                                        show.setText("Show comments");
                                        i = 0;
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        List<Comment> commentList2 = new ArrayList<>();
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                        commentRecycler.setLayoutManager(layoutManager);
                                        commentAdapter = new CommentAdapter(context, commentList2);
                                        commentRecycler.setAdapter(commentAdapter);
                                        commentRecycler.setVisibility(View.INVISIBLE);
                                        show.setText("Show comments");
                                        i = 0;
                                    }
                                });

                            } else {
                                System.out.println("no");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        mRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mRef4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        System.out.println("3 " + commentList.get(position).getId());
                        mRef.child(commentList.get(position).getId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                System.out.println("1 " + snapshot.child("username").getValue().toString());
                                System.out.println("2 " + snapshot2.child("username").getValue().toString());
                                if (snapshot.child("username").getValue().toString().equals(snapshot2.child("username").getValue().toString())) {
                                    System.out.println("no");
                                } else {
                                    System.out.println("yes");
                                    holder.edit.setVisibility(View.INVISIBLE);
                                    holder.delete.setVisibility(View.INVISIBLE);
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
        if (commentList.get(position).getAnonymous().equals("No")) {
            mRef.child(commentList.get(position).getId()).child("profileImage").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Glide.with(context).load(snapshot.getValue().toString()).into(holder.imageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            mRef.child(commentList.get(position).getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    holder.username.setText(snapshot.child("username").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            holder.text.setText(commentList.get(position).getText());

            holder.date.setText(commentList.get(position).getDate());
        } else {
            holder.date.setText(commentList.get(position).getDate());
            holder.text.setText(commentList.get(position).getText());
        }

    }

    @Override
    public int getItemCount() {

        return commentList.size();
    }

    public static final class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        ImageView edit, delete;
        TextView username, date;
        EditText text;
        Button editBtn;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.commentUsername);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            text = itemView.findViewById(R.id.commentText);
            imageView = itemView.findViewById(R.id.commentProfile);
            date = itemView.findViewById(R.id.date);
            editBtn = itemView.findViewById(R.id.editBtn);

        }
    }
}
