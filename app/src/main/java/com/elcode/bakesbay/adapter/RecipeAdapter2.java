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
import com.elcode.bakesbay.MainActivity;
import com.elcode.bakesbay.R;
import com.elcode.bakesbay.model.Recipe;
import com.elcode.bakesbay.reciep.EditReciepActivity;
import com.elcode.bakesbay.reciep.MyRecipe;
import com.elcode.bakesbay.reciep.RecipePage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class RecipeAdapter2 extends RecyclerView.Adapter<RecipeAdapter2.RecipeViewHolder> {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    DatabaseReference mRef2;
    Context context;
    public List<Recipe> recipes;

    public RecipeAdapter2(Context context, List<Recipe> courses) {
        this.context = context;
        this.recipes = courses;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recipeItems = LayoutInflater.from(context).inflate(R.layout.recipe_item2, parent, false);
        return new RecipeAdapter2.RecipeViewHolder(recipeItems);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid());
        mRef2 = FirebaseDatabase.getInstance().getReference().child("recipes");

        mRef.child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue().toString().equals(recipes.get(position).getUsername())){
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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef2.child(recipes.get(position).id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                    }
                });
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditReciepActivity.class);
                intent.putExtra("id", recipes.get(position).getId());
                intent.putExtra("recipeImage", recipes.get(position).getPhotoLink());
                intent.putExtra("recipeTitle", recipes.get(position).getTitle());
                intent.putExtra("recipeCategory", recipes.get(position).getCategory());
                intent.putExtra("recipeCookTime", recipes.get(position).getCookTime());
                intent.putExtra("recipeServes", recipes.get(position).getServes());
                intent.putExtra("recipePrepTime", recipes.get(position).getPrepTime());
                intent.putExtra("recipeAccess", recipes.get(position).getAccess());
                intent.putExtra("recipeDesc", recipes.get(position).getDescription());
                intent.putExtra("recipeIngr", recipes.get(position).getIngredients());
                intent.putExtra("recipeDire", recipes.get(position).getDirections());
                context.startActivity(intent);
            }
        });
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
        public ImageView recipeImage, delete, edit;
        TextView username, time, title;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            username = itemView.findViewById(R.id.username);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.recipeTitle);


        }
    }
}
