package com.elcode.bakesbay.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elcode.bakesbay.R;
import com.elcode.bakesbay.model.Recipe;
import com.elcode.bakesbay.reciep.RecipePage;
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
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid());

        mRef.child("username").addValueEventListener(new ValueEventListener() {
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
        public ImageView recipeImage;
        TextView username, time, title;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            username = itemView.findViewById(R.id.username);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.recipeTitle);


        }
    }
}
