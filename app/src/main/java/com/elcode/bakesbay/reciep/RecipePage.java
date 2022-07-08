package com.elcode.bakesbay.reciep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.elcode.bakesbay.R;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class RecipePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

        ImageView pageImage = findViewById(R.id.recipePageImage);

        TextView title = findViewById(R.id.pageTitle);
        TextView cookTime = findViewById(R.id.pageCookTime);
        TextView serves = findViewById(R.id.pageServes);
        TextView prepTime = findViewById(R.id.pagePrepTime);
        TextView access = findViewById(R.id.pageAccess);
        TextView description = findViewById(R.id.pageDescription);
        TextView ingredients = findViewById(R.id.pageIngredients);
        TextView directions = findViewById(R.id.pageDirectons);

        Glide.with(this).load(getIntent().getStringExtra("recipeImage")).into(pageImage);
        title.setText(getIntent().getStringExtra("recipeTitle"));
        cookTime.setText(getIntent().getStringExtra("recipeCookTime"));
        serves.setText(getIntent().getStringExtra("recipeServes"));
        prepTime.setText(getIntent().getStringExtra("recipePrepTime"));
        access.setText(getIntent().getStringExtra("recipeAccess"));
        description.setText(getIntent().getStringExtra("recipeDesc"));
        ingredients.setText(getIntent().getStringExtra("recipeIngr"));
        directions.setText(getIntent().getStringExtra("recipeDire"));


    }
}