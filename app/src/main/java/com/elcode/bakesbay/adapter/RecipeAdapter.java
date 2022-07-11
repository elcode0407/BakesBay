package com.elcode.bakesbay.adapter;


import static com.elcode.bakesbay.MainActivity.recipeList3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elcode.bakesbay.MainActivity;
import com.elcode.bakesbay.R;
import com.elcode.bakesbay.autorization.LoginActivity;
import com.elcode.bakesbay.model.Recipe;
import com.elcode.bakesbay.reciep.MyFavorite;
import com.elcode.bakesbay.reciep.RecipePage;
import com.elcode.bakesbay.success.SuccessProfile;
import com.elcode.bakesbay.user.ProfileActivity;
import com.elcode.bakesbay.user.SetupActivity;
import com.elcode.bakesbay.user.UserProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    DatabaseReference mRef2;
    DatabaseReference mRef3;
    DatabaseReference mRef4;
    Context context;
    public List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> courses) {
        this.context = context;
        this.recipes = courses;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recipeItems = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        return new RecipeAdapter.RecipeViewHolder(recipeItems);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("users");
        mRef2 = FirebaseDatabase.getInstance().getReference().child("count");
        mRef4 = FirebaseDatabase.getInstance().getReference().child("count2").child(mUser.getUid());
        mRef3 = FirebaseDatabase.getInstance().getReference().child("favorite").child(mUser.getUid());

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserProfileActivity.class);

                intent.putExtra("profileId", recipes.get(position).getId2());

                context.startActivity(intent);
            }
        });



        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        try {
                            if (holder.saveBtn.getDrawable().getConstantState().equals(context.getDrawable(R.drawable.save).getConstantState())) {
                                System.out.println("g");
                                mRef3.child(recipes.get(position).id).setValue("");
                                mRef4.setValue(Integer.parseInt(snapshot1.getValue().toString()) + 1);
                                holder.saveBtn.setImageResource(R.drawable.save2);
                            } else {
                                System.out.println("b");
                                mRef3.child(recipes.get(position).id).removeValue();
                                holder.saveBtn.setImageResource(R.drawable.save);
                            }
                            if(context instanceof MyFavorite){

                            }else {
                                recipeList3.clear();
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Toast.makeText(context, "Update...", Toast.LENGTH_LONG).show();
                                context.startActivity(intent);
                            }
                            return;
                        } catch (IndexOutOfBoundsException e) {

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        mRef4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                System.out.println(snapshot1.getValue().toString());
                mRef3.child(recipes.get(position).id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() == null) {

                        } else {
                            holder.saveBtn.setImageResource(R.drawable.save2);
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

        mRef.child(mUser.getUid()).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue().toString().equals(recipes.get(position).getUsername())) {
                    holder.username.setText("you");
                } else {
                    holder.username.setText(recipes.get(position).getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Glide.with(context).load(recipes.get(position).getPhotoLink()).into(holder.recipeImage);
        if (recipes.get(position).getCookTime().length() > 7) {
            String z = recipes.get(position).getCookTime().substring(0, 7);
            holder.time.setText(z);
        } else {
            holder.time.setText(recipes.get(position).getCookTime());
        }
        holder.title.setText(recipes.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecipePage.class);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        (Activity) context,
                        new Pair<View, String>(holder.recipeImage, "recipeImage")
                );
                intent.putExtra("recipeImage", recipes.get(position).getPhotoLink());
                intent.putExtra("recipeTitle", recipes.get(position).getTitle());
                intent.putExtra("recipeCookTime", recipes.get(position).getCookTime());
                intent.putExtra("recipeServes", recipes.get(position).getServes());
                intent.putExtra("recipePrepTime", recipes.get(position).getPrepTime());
                intent.putExtra("recipeAccess", recipes.get(position).getAccess());
                intent.putExtra("recipeDesc", recipes.get(position).getDescription());
                intent.putExtra("recipeIngr", recipes.get(position).getIngredients());
                intent.putExtra("recipeDire", recipes.get(position).getDirections());
                context.startActivity(intent, options.toBundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static final class RecipeViewHolder extends RecyclerView.ViewHolder {
        public ImageView recipeImage, saveBtn;
        TextView username, time, title;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            saveBtn = itemView.findViewById(R.id.saveBtn);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            username = itemView.findViewById(R.id.username);
            username.setPaintFlags(username.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.recipeTitle);


        }
    }
}
