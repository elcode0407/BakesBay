package com.elcode.bakesbay.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView profile_image;
    AutoCompleteTextView inputCountry;
    EditText inputUsername, inputNameSurname;
    Spinner autocomplete_type, autocomplete_level;
    private int REQUEST_CODE = 101;
    static Uri imageUri;
    ImageButton btnSave;
    static String[] autotype = new String[1];
    static String[] autolevel = new String[1];
    static String[] level;
    static String[] type;
    static List<String> list = new ArrayList<>();
    ProgressDialog mLoad;


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
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef2 = FirebaseDatabase.getInstance().getReference().child("users");
        mRef3 = FirebaseDatabase.getInstance().getReference().child("recipes");
        mRef4 = FirebaseDatabase.getInstance().getReference().child("count").child(mUser.getUid());
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid());
        sRef = FirebaseStorage.getInstance().getReference().child("profileImage");
        mLoad = new ProgressDialog(this);
        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s :
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
            type = new String[]{"I'm Vegetarian", "I love Meat meal", "I am on a diet", "I'm omnivore)"};
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
            level = new String[]{"I'm new", "I'm an amateur", "I'm a chef"};
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
        mRef.child("profileImage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).into(profile_image);
                        System.out.println(uri.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRef.child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                inputUsername.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRef.child("level").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int spinnerPosition = Arrays.asList(level).indexOf(snapshot.getValue().toString());
                autocomplete_level.setSelection(spinnerPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRef.child("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int spinnerPosition = Arrays.asList(type).indexOf(snapshot.getValue().toString());
                autocomplete_type.setSelection(spinnerPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRef.child("country").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                inputCountry.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRef.child("surnameName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                inputNameSurname.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                int[] a = {0};
                mRef.child("username").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!WordUtils.uncapitalize(inputUsername.getText().toString()).equals(snapshot.getValue().toString())) {
                            for (String s1 :
                                    list.subList(0, list.size())) {
                                System.out.println(s1);
                                if (Objects.equals(s1, WordUtils.uncapitalize(inputUsername.getText().toString()))) {
                                    a[0] = 1;
                                }
                            }
                        } else {
                            a[0] = 0;
                        }
                        System.out.println(a[0]);
                        if (a[0] != 1) {
                            SaveData();
                            Toast.makeText(EditProfileActivity.this, "Profile update", Toast.LENGTH_SHORT);
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Username is Exist", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
        } else if (country.isEmpty()) {
            inputCountry.setError("Country is required!");
            inputCountry.requestFocus();
            return;
        } else if (ns.isEmpty()) {
            inputNameSurname.setError("Name and Surname is required!");
            inputNameSurname.requestFocus();
            return;
        } else {
            if (imageUri == null) {
                if (username == null) {
                    mRef.child("username").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mRef.child("username").setValue(snapshot.getValue().toString());
                            int[] s = new int[1];
                            mRef4.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String b = snapshot.child("count").getValue().toString();
                                    s[0] = Integer.parseInt(b);
                                    int z = 1;
                                    for (int i = 1; i <= s[0] - 1; i++) {
                                        System.out.println("i: " + i);
                                        System.out.println("count: " + s[0]);
                                        System.out.println(z);
                                        String firstLower = WordUtils.uncapitalize(username);
                                        mRef3.child(mUser.getUid() + z).child("username").setValue(firstLower);
                                        z++;
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
                } else {

                    String firstLower = WordUtils.uncapitalize(username);
                    mRef.child("username").setValue(firstLower);
                    int[] s = new int[1];
                    mRef4.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String b = snapshot.child("count").getValue().toString();
                            s[0] = Integer.parseInt(b);
                            int z = 1;
                            for (int i = 1; i <= s[0] - 1; i++) {
                                System.out.println("i: " + i);
                                System.out.println("count: " + s[0]);
                                System.out.println(z);
                                mRef3.child(mUser.getUid() + z).child("username").setValue(firstLower);
                                z++;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if (ns == null) {
                    mRef.child("surnameName").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mRef.child("surnameName").setValue(snapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    mRef.child("surnameName").setValue(capitalizeWord(ns));
                }
                if (country == null) {
                    mRef.child("country").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mRef.child("country").setValue(snapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    mRef.child("country").setValue(capitalizeWord(country.replaceAll("[\\s]{2,}", " ")));
                }
                mRef.child("level").setValue(level);
                mRef.child("type").setValue(type);
                Toast.makeText(EditProfileActivity.this, "Profile update", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfileActivity.this, SuccessProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                sRef.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            sRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mRef.child("profileImage").setValue(uri.toString());
                                    if (username == null) {
                                        mRef.child("username").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                mRef.child("username").setValue(snapshot.getValue().toString());
                                                int[] s = new int[1];
                                                mRef4.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String b = snapshot.child("count").getValue().toString();
                                                        s[0] = Integer.parseInt(b);
                                                        int z = 1;
                                                        for (int i = 1; i <= s[0] - 1; i++) {
                                                            System.out.println("i: " + i);
                                                            System.out.println("count: " + s[0]);
                                                            System.out.println(z);
                                                            String firstLower = WordUtils.uncapitalize(username);
                                                            mRef3.child(mUser.getUid() + z).child("username").setValue(firstLower);
                                                            z++;
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
                                    } else {
                                        String firstLower = WordUtils.uncapitalize(username);
                                        mRef.child("username").setValue(firstLower);
                                        int[] s = new int[1];
                                        mRef4.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String b = snapshot.child("count").getValue().toString();
                                                s[0] = Integer.parseInt(b);
                                                int z = 1;
                                                for (int i = 1; i <= s[0] - 1; i++) {
                                                    System.out.println("i: " + i);
                                                    System.out.println("count: " + s[0]);
                                                    System.out.println(z);
                                                    mRef3.child(mUser.getUid() + z).child("username").setValue(firstLower);
                                                    z++;
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    if (ns == null) {
                                        mRef.child("surnameName").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                mRef.child("surnameName").setValue(snapshot.getValue().toString());
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        mRef.child("surnameName").setValue(capitalizeWord(ns));
                                    }
                                    if (country == null) {
                                        mRef.child("country").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                mRef.child("country").setValue(snapshot.getValue().toString());
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        mRef.child("country").setValue(capitalizeWord(country));
                                    }
                                    mRef.child("level").setValue(level);
                                    mRef.child("type").setValue(type);
                                    Toast.makeText(EditProfileActivity.this, "Profile Update", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EditProfileActivity.this, SuccessProfile.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
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
