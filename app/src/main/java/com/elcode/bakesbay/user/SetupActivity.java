package com.elcode.bakesbay.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.elcode.bakesbay.R;
import com.elcode.bakesbay.success.SuccessProfile;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    CircleImageView profile_image;
    AutoCompleteTextView inputCountry;
    EditText inputUsername, inputNameSurname;
    Spinner autocomplete_type, autocomplete_level;
    private int REQUEST_CODE = 101;
    static Uri imageUri;
    ImageButton btnSave;
    static String[] autotype = new String[1];
    static String[] autolevel = new String[1];
    ProgressDialog mLoad;
    static List<String> list = new ArrayList<>();


    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    DatabaseReference mRef2;
    DatabaseReference mRef3;
    DatabaseReference mRef4;
    StorageReference sRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("users");
        mRef2 = FirebaseDatabase.getInstance().getReference().child("usernames");
        mRef3 = FirebaseDatabase.getInstance().getReference().child("count").child(mUser.getUid());
        mRef3 = FirebaseDatabase.getInstance().getReference().child("count2").child(mUser.getUid());
        sRef = FirebaseStorage.getInstance().getReference().child("profileImage");
        mLoad = new ProgressDialog(this);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s:
                     snapshot.getChildren()) {
                    System.out.println(s.child("username").getValue().toString());
                    list.add(s.child("username").getValue().toString());
                    //                    mRef.child(s.getValue().toString()).child("username").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            System.out.println("s"+snapshot.getValue().toString());
//                            list.add(snapshot.getValue().toString());
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        {
            profile_image = findViewById(R.id.profile_image);
            inputUsername = findViewById(R.id.inputUsername);
            String[] countries = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine"};
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, countries);
            inputCountry = findViewById(R.id.inputCountry);
            inputCountry.setThreshold(1);
            inputCountry.setAdapter(adapter1);
            inputNameSurname = findViewById(R.id.inputNameSurname);
            btnSave = findViewById(R.id.btnSave);
            String[] type = {"I'm Vegetarian", "I love Meat meal", "I am on a diet", "I'm omnivore)"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            autocomplete_type = findViewById(R.id.autocomplete_type);
            autocomplete_type.setAdapter(adapter);
            autocomplete_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    autotype[0] = adapterView.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            String[] level = {"I'm new", "I'm an amateur", "I'm a chef"};
            ArrayAdapter<String> adapter2 =
                    new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, level);
            autocomplete_level = findViewById(R.id.autocomplete_level);
            autocomplete_level.setAdapter(adapter2);
            autocomplete_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    autolevel[0] = adapterView.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                startActivityForResult(intent, REQUEST_CODE);
            }

        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = 0;
                for (String s1 :
                        list.subList(0, list.size())) {
                    System.out.println(s1);
                    if (Objects.equals(s1, WordUtils.uncapitalize(inputUsername.getText().toString()))) {
                        a = 1;
                    }
                }
                System.out.println(a);
                if (a != 1) {
                    SaveData();
                } else {
                    Toast.makeText(SetupActivity.this, "Username is Exist", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void SaveData() {
        String username = inputUsername.getText().toString();
        String country = inputCountry.getText().toString();
        String ns = inputNameSurname.getText().toString();
        String level = autolevel[0].toString();
        String type = autotype[0].toString();

        if (username.isEmpty()) {
            inputUsername.setError("Username is required!");
            inputUsername.requestFocus();
            return;
        } else if (username.length() < 3) {
            inputUsername.setError("Min username length should be 3 characters ");
            inputUsername.requestFocus();
            return;
        }else if (username.length() >16) {
            inputUsername.setError("Max username length should be 15 characters ");
            inputUsername.requestFocus();
            return;
        } else if (country.isEmpty()) {
            inputCountry.setError("Country is required!");
            inputCountry.requestFocus();
            return;
        } else if (ns.isEmpty()) {
            inputNameSurname.setError("Name and Surname is required!");
            inputNameSurname.requestFocus();
            return;
        } else if (imageUri == null) {
            Toast.makeText(this, "Please select an profile image", Toast.LENGTH_SHORT).show();
        } else {
            sRef.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        sRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap = new HashMap();
                                HashMap hashMap3 = new HashMap();
                                String firstLower = WordUtils.uncapitalize(username);
                                HashMap hashMap2 = new HashMap();
                                hashMap.put("username", firstLower);
                                hashMap.put("surnameName", capitalizeWord(ns));
                                hashMap.put("country", capitalizeWord(country.replaceAll("[\\s]{2,}", " ")));
                                hashMap.put("level", level);
                                hashMap.put("type", type);
                                hashMap.put("email", mUser.getEmail());
                                hashMap.put("profileImage", uri.toString());
                                hashMap2.put("count", 1);
                                hashMap3.put("count2", 2);

                                mRef3.setValue(hashMap2);
                                mRef.child(mUser.getUid()).updateChildren(hashMap);
                            }
                        });
                        Toast.makeText(SetupActivity.this, "Setup profile completed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SetupActivity.this, SuccessProfile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profile_image.setImageURI(imageUri);

        }
    }
    public static String capitalizeWord(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }

}