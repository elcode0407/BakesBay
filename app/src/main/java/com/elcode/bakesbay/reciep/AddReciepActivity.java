package com.elcode.bakesbay.reciep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.elcode.bakesbay.MainActivity;
import com.elcode.bakesbay.R;
import com.elcode.bakesbay.SplashActivity;
import com.elcode.bakesbay.SuccessRecipe;
import com.elcode.bakesbay.user.EditProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Locale;

public class AddReciepActivity extends AppCompatActivity {
    Button next, next2, save;
    ImageButton cancel;
    EditText title, description, prepTime, cookTime, serves, ingredients, directions;
    Spinner prepTime2, cookTime2, category;
    CheckBox privateBox, publicBox;
    VideoView videoBtn;
    ImageView photoBtn;
    static String title2, description2, prepTime3, cookTime3, serves2, ingredients2, directions2, type, type2, privateBox2, publicBox2, category2;
    private int REQUEST_CODE = 101;
    static Uri imageUri;
    static Uri videouri;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    static String[] autotype = new String[1];
    static String[] autotype2 = new String[1];
    static String[] autotype3 = new String[1];
    ProgressDialog mLoad;
    DatabaseReference mRef;
    DatabaseReference mRef2;
    DatabaseReference mRef3;
    StorageReference sRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reciep);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("recipes");
        mRef3 = FirebaseDatabase.getInstance().getReference().child("users");
        mRef2 = FirebaseDatabase.getInstance().getReference().child("count").child(mUser.getUid());
        sRef = FirebaseStorage.getInstance().getReference().child("recipeImage");

        next = findViewById(R.id.next);
        cancel = findViewById(R.id.cancel);
        title = findViewById(R.id.title);
        mLoad = new ProgressDialog(this);
        description = findViewById(R.id.description);
        prepTime = findViewById(R.id.prepTime);
        cookTime = findViewById(R.id.cookTime);
        serves = findViewById(R.id.serves);
        String[] time = {"MINUTES", "HOURS", "DAYS"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_items, time);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String[] time2 = new String[] {"Appetizer", "Beverage", "Breads", "Breakfast", "Dessert", "Lunch", "Main Dish", "Salad", "Side Dish", "Snacks", "Soups", "Other"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_items, time2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category = findViewById(R.id.category);
        prepTime2 = findViewById(R.id.prepTime2);
        cookTime2 = findViewById(R.id.cookTime2);
        prepTime2.setAdapter(adapter);
        cookTime2.setAdapter(adapter);
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
        prepTime2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                autotype[0] = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        cookTime2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                autotype2[0] = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title2 = title.getText().toString();
                description2 = description.getText().toString();
                prepTime3 = prepTime.getText().toString();
                cookTime3 = cookTime.getText().toString();
                serves2 = serves.getText().toString();
                type = autotype[0];
                type2 = autotype2[0];
                category2 = autotype3[0];
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
                } else if (prepTime3.isEmpty() || type.isEmpty()) {
                    prepTime.setError("Prep Time is require!");
                    prepTime.requestFocus();
                    return;
                } else if (cookTime3.isEmpty() || type2.isEmpty()) {
                    cookTime.setError("Cook Time is require!");
                    cookTime.requestFocus();
                    return;
                } else if (prepTime3.charAt(0) == '0') {
                    prepTime.setError("Prep Time mustn't start with 0");
                    prepTime.requestFocus();
                    return;
                } else if (cookTime3.charAt(0) == '0') {
                    cookTime.setError("Cook Time mustn't start with 0");
                    cookTime.requestFocus();
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
                    setContentView(R.layout.activity_add_reciep_2);
                    ingredients = findViewById(R.id.ingredients);
                    directions = findViewById(R.id.directions);
                    privateBox = findViewById(R.id.privateBox);
                    publicBox = findViewById(R.id.publicBox);
                    publicBox.setChecked(true);
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
                    next2 = findViewById(R.id.next2);
                    next2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ingredients2 = ingredients.getText().toString();
                            directions2 = directions.getText().toString();
                            privateBox2 = privateBox.getText().toString();
                            publicBox2 = publicBox.getText().toString();
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
                                setContentView(R.layout.activity_add_reciep_3);
                                photoBtn = findViewById(R.id.imageBtn);
                                photoBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        intent.setType("image/");
                                        startActivityForResult(intent, REQUEST_CODE);
                                    }

                                });
                                save = findViewById(R.id.save);
                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        SaveData();
                                        Toast.makeText(AddReciepActivity.this, "Uploaded!!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(AddReciepActivity.this, SuccessRecipe.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        return;
                                    }
                                });

                            }

                        }
                    });
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddReciepActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private void SaveData() {
        int[] s = new int[1];
        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                String b = snapshot1.child("count").getValue().toString();
                s[0] = Integer.parseInt(b);
                System.out.println(s[0]);
                if (imageUri == null) {
                    Toast.makeText(AddReciepActivity.this, "Please select an profile image", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        System.out.println("NNOOOOOOO");
                        StorageReference reference2 = FirebaseStorage.getInstance().getReference().child("recipe").child(mUser.getUid() + s[0]);
                        StorageReference reference = FirebaseStorage.getInstance().getReference().child("recipePhoto").child(mUser.getUid() + s[0]);
                        reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mRef3.child(mUser.getUid()).child("username").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                HashMap<String, String> map = new HashMap<>();
                                                HashMap<String, Integer> map2 = new HashMap<>();
                                                map.put("id", mUser.getUid() + s[0]);
                                                System.out.println("1 " + snapshot.getValue().toString());
                                                map.put("username", snapshot.getValue().toString());
                                                System.out.println("2 " + category2);
                                                map.put("category", category2);
                                                map.put("photoLink", uri.toString());
                                                map.put("title", title2.toUpperCase(Locale.ROOT).replaceAll("[\\s]{2,}", " "));
                                                map.put("description", description2);
                                                map.put("prepTime", prepTime3 + " " + type);
                                                if (type2.length() == 7) {
                                                    String z = type2.substring(0, 3);
                                                    map.put("cookTime", cookTime3 + " " + z);
                                                } else {
                                                    map.put("cookTime", cookTime3 + " " + type2);
                                                }
                                                if (type.length() == 7) {
                                                    String z = type.substring(0, 3);
                                                    map.put("prepTime", prepTime3 + " " + z);
                                                } else {
                                                    map.put("prepTime", prepTime3 + " " + type);
                                                }
                                                map.put("serves", serves2);
                                                map.put("ingredients", ingredients2);
                                                map.put("directions", directions2);
                                                if (publicBox2.isEmpty()) {
                                                    map.put("access", privateBox2);
                                                } else {
                                                    map.put("access", publicBox2);
                                                }
                                                map2.put("count", s[0] + 1);
                                                System.out.println("3" + map.toString());
                                                mRef.child(mUser.getUid() + s[0]).setValue(map);
                                                mRef2.setValue(map2);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                });
                            }
                        });
                    } catch (SecurityException e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            photoBtn.setImageURI(imageUri);
        }
        if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videouri = data.getData();
            videoBtn.setVideoURI(videouri);
            videoBtn.start();
        }
    }

}