package com.example.firebase_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // create object
    private FirebaseFirestore objectFirebaseFirestore;
    private Dialog objectDialog;
    private EditText documentET,cityNameET,cityDetailsET;
    private DocumentReference objectDocumentReference;
    private EditText valuetextbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize firebase
        objectFirebaseFirestore=FirebaseFirestore.getInstance();
        objectDialog=new Dialog(this);

        objectDialog.setContentView(R.layout.activity_please_wait);
        documentET=findViewById(R.id.documentIDET);

        cityNameET=findViewById(R.id.cityNameET);
        cityDetailsET=findViewById(R.id.cityDetailsET);
    }
    private void addValues(View v)
    {
        try {

            if(!documentET.getText().toString().isEmpty() && !cityNameET.getText().toString().isEmpty()
                    && !cityDetailsET.getText().toString().isEmpty()) {
                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();

                objectMap.put("city_name", cityNameET.getText().toString());
                objectMap.put("city_details", cityDetailsET.getText().toString());

                objectFirebaseFirestore.collection("NewCities")
                        .document(documentET.getText().toString()).set(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to add data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else
            {
                Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "addValues:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void getData(View view) {
        try {
            if (!documentET.getText().toString().isEmpty()) {
                objectDocumentReference = objectFirebaseFirestore.collection("NewCities").document(documentET.getText().toString()
                );
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String City = documentSnapshot.getString("city_name");
                        String Details = documentSnapshot.getString("city_details");
                        valuetextbox.setText("\nCity Name: " + City + "\n\n" + "City Details: " + Details);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed To Get Values Back", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Get Data Error" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getValues(View view) {
    }
}
