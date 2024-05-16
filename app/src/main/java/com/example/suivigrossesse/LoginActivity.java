package com.example.suivigrossesse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "Suivi Grossesse";
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private TextView forgotpassowrd,signup;
    private EditText email,password;
    private Button login;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

//        //todo pause de 2s avant de continuer
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Continuer avec d'autres opérations après la pause
//            }
//        }, 2000); // 2000 millisecondes = 2 secondes

        initViews();

        //todo to check with the data base
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valideInputs()) {
                    String emailAddress = email.getText().toString().trim();
                    String givenPassword = password.getText().toString().trim();

                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(emailAddress, givenPassword)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("email", emailAddress);

                                    startActivity(intent);
//                                    Toast.makeText(LoginActivity.this, "Welcome Back", Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        //todo send mail authentification
        forgotpassowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                //TODO make a validation if the use entered an email before start email sending process
                String emailaddress = email.getText().toString().trim();
                progressDialog.setTitle("Sending Mail");
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(emailaddress)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.cancel();
                                Toast.makeText(LoginActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initViews() {

        forgotpassowrd = findViewById(R.id.forgotPassword);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

    }

    private boolean valideInputs(){

        String emailAddress = email.getText().toString().trim();
        String givenPassword = password.getText().toString().trim();

        // Regular expression to validate email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailAddress);

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

        return true;
    }

}


