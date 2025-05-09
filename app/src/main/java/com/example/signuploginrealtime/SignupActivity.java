package com.example.signuploginrealtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
// استيراد الأدوات الضرورية للتعامل مع الشاشة والعناصر (أزرار، حقول نص، تنقل بين الشاشات،

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
// استيراد Firebase للتعامل مع قاعدة البيانات في الوقت الحقيقي (Realtime Database).

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    // تعريف مكونات الشاشة (حقول إدخال، زر، رابط نصي)، ومراجع لقاعدة البيانات.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //دالة onCreate تُنفذ عند فتح الشاشة، وتربط التصميم (XML) بالـ Java.
        setContentView(R.layout.activity_signup);


        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
       // ربط عناصر الواجهة الرسومية (من ملف XML) بالمتغيرات في الكود.

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signupName.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String username = signupUsername.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();

                if (!validateName() | !validateEmail() | !validateUsername() | !validatePassword()) {
                    Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                HelperClass helperClass = new HelperClass(name, email, username, password);
                reference.child(username).setValue(helperClass);
               // تخزين بيانات المستخدم في قاعدة البيانات تحت اسم المستخدم كـ "مفتاح".

                Toast.makeText(SignupActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);



            }

        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    public Boolean validateName() {
        String val = signupName.getText().toString();
        if (val.isEmpty()) {
            signupName.setError("Name cannot be empty");
            return false;
        } else {
            signupName.setError(null);
            return true;
        }
    }

    public Boolean validateEmail() {
        String val = signupEmail.getText().toString();
        if (val.isEmpty()) {
            signupEmail.setError("Email cannot be empty");
            return false;
        } else {
            signupEmail.setError(null);
            return true;
        }
    }

    public Boolean validateUsername() {
        String val = signupUsername.getText().toString();
        if (val.isEmpty()) {
            signupUsername.setError("Username cannot be empty");
            return false;
        } else {
            signupUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = signupPassword.getText().toString();
        if (val.isEmpty()) {
            signupPassword.setError("Password cannot be empty");
            return false;
        } else {
            signupPassword.setError(null);
            return true;
        }
    }

}