package com.example.suivigrossesse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.suivigrossesse.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private Button signup;
    private TextView login;
    private EditText email,Name,Address,Phone,password,confirmPassword;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        initViews();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString();
                String emailAddress = email.getText().toString();
                String address = Address.getText().toString();
                String phone = Phone.getText().toString();
                String givenPassword = password.getText().toString();
                String ConfirmPassword = confirmPassword.getText().toString();
                if(valideInputs()){

                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(emailAddress,givenPassword)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                intent.putExtra("email", emailAddress);
                                startActivity(intent);
                                progressDialog.cancel();

                                firebaseFirestore.collection("User")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .set(new User(name,emailAddress,address,phone));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        });
               }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean valideInputs(){

        String emailAddress = email.getText().toString().trim();
        String givenPassword = password.getText().toString().trim();
        String givenConfirmPassword = confirmPassword.getText().toString().trim();
        String name = Name.getText().toString().trim();


        // Regular expression to validate email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailAddress);

        if(name.isEmpty()){
            Toast.makeText(this, "Enter your email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(emailAddress.isEmpty()){
            Toast.makeText(this, "Enter your email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(matcher.matches()==false){
            Toast.makeText(this, "Invalid Email Format!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(givenPassword.isEmpty()){
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(givenConfirmPassword.isEmpty()){
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!givenConfirmPassword.equals(givenPassword)){
            Toast.makeText(this, "Check password!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void initViews() {
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);

        Name = findViewById(R.id.Name);
        email = findViewById(R.id.email);
        Address = findViewById(R.id.address);
        Phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

    }
}