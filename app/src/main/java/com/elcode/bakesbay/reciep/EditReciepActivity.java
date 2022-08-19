package com.elcode.bakesbay.reciep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elcode.bakesbay.MainActivity;
import com.elcode.bakesbay.R;
import com.elcode.bakesbay.success.SuccesEditRecipe;
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

import java.util.Arrays;
import java.util.Locale;

public class EditReciepActivity extends AppCompatActivity {
    Button next, edit;
    ImageButton cancel;
    CheckBox privateBox, publicBox;
    EditText title, description, serves, ingredients, directions;
    Spinner category;
    static String title2, description2, serves2, ingredients2, directions2, category2, privateBox2, publicBox2;
    private int REQUEST_CODE = 101;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    static String[] autotype3 = new String[1];
    ProgressDialog mLoad;
    DatabaseReference mRef;
    DatabaseReference mRef3;
    StorageReference sRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reciep);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("recipes").child(getIntent().getStringExtra("id"));
        mRef3 = FirebaseDatabase.getInstance().getReference().child("users");
        sRef = FirebaseStorage.getInstance().getReference().child("recipeImage");

        next = findViewById(R.id.next);
        cancel = findViewById(R.id.cancel);
        title = findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("recipeTitle"));
        mLoad = new ProgressDialog(this);
        description = findViewById(R.id.description);
        description.setText(getIntent().getStringExtra("recipeDesc"));
        serves = findViewById(R.id.serves);
        serves.setText(getIntent().getStringExtra("recipeServes"));
        String[] time2 = new String[]{"Appetizer", "Beverage", "Breads", "Breakfast", "Dessert", "Lunch", "Main Dish", "Salad", "Side Dish", "Snacks", "Soups", "Other"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_items, time2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category = findViewById(R.id.category);
        category.setAdapter(adapter2);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                autotype3[0] = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        int spinnerPosition = Arrays.asList(time2).indexOf(getIntent().getStringExtra("recipeCategory"));
        category.setSelection(spinnerPosition);
        privateBox = findViewById(R.id.privateBox);
        publicBox = findViewById(R.id.publicBox);
        mRef.child("access").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue().toString().equals("Public")) {
                    publicBox.setChecked(true);
                } else {
                    privateBox.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        privateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicBox.setChecked(false);
                publicBox.setSelected(false);
            }
        });
        publicBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privateBox.setChecked(false);
                privateBox.setSelected(false);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title2 = title.getText().toString();
                description2 = description.getText().toString();
                serves2 = serves.getText().toString();
                category2 = autotype3[0];
                privateBox2 = privateBox.getText().toString();
                publicBox2 = publicBox.getText().toString();
                System.out.println("1" + privateBox2);
                System.out.println("1" + publicBox2);
                if (title2.isEmpty()) {
                    title.setError("Title is require!");
                    title.requestFocus();
                    return;
                } else if (title2.length() > 16) {
                    title.setError("Title max length 10!");
                    title.requestFocus();
                    return;
                } else if (description2.isEmpty()) {
                    description.setError("Description is require!");
                    description.requestFocus();
                    return;
                } else if (description.length() < 10) {
                    description.setError("Description min length 10!");
                    description.requestFocus();
                    return;
                } else if (serves2.isEmpty()) {
                    serves.setError("Serves is require!");
                    serves.requestFocus();
                    return;
                } else if (serves2.charAt(0) == '0') {
                    serves.setError("Serves mustn't start with 0");
                    serves.requestFocus();
                    return;
                } else {
                    setContentView(R.layout.activity_edit_reciep_2);
                    ingredients = findViewById(R.id.ingredients);
                    ingredients.setText(getIntent().getStringExtra("recipeIngr"));
                    directions = findViewById(R.id.directions);
                    directions.setText(getIntent().getStringExtra("recipeDire"));
                    edit = findViewById(R.id.next2);
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ingredients2 = ingredients.getText().toString();
                            directions2 = directions.getText().toString();

                            if (ingredients2.isEmpty()) {
                                ingredients.setError("Ingredients is require!");
                                ingredients.requestFocus();
                                return;
                            } else if (ingredients.length() < 10) {
                                ingredients.setError("Ingredients min length 10");
                                ingredients.requestFocus();
                                return;
                            } else if (directions2.isEmpty()) {
                                directions.setError("Directions is require!");
                                directions.requestFocus();
                                return;
                            } else if (directions.length() < 10) {
                                directions.setError("Directions min length 10");
                                directions.requestFocus();
                                return;
                            } else {
                                EditData();
                                Toast.makeText(EditReciepActivity.this, "Uploaded!!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(EditReciepActivity.this, SuccesEditRecipe.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                return;
                            }
                        }
                    });
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditReciepActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private synchronized void EditData() {
        try {
            System.out.println("NNOOOOOOO");
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("recipePhoto").child(getIntent().getStringExtra("id"));
            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    mRef3.child(mUser.getUid()).child("username").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (publicBox.isChecked()) {
                                System.out.println(publicBox2);
                                mRef.child("access").setValue(publicBox2);

                            } else {
                                System.out.println(privateBox2);
                                mRef.child("access").setValue(privateBox2);
                            }
                            System.out.println("2 " + category2);
                            mRef.child("category").setValue(category2);
                            mRef.child("title").setValue(title2.toUpperCase(Locale.ROOT).replaceAll("[\\s]{2,}", " "));
                            mRef.child("description").setValue(description2);
                            mRef.child("serves").setValue(serves2);
                            mRef.child("ingredients").setValue(ingredients2);
                            mRef.child("directions").setValue(directions2);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

        } catch (SecurityException e) {

        }

    }

}