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
                //عند الضغط على زر "تسجيل"، ينفذ هذا الكود:

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");
               // الحصول على مرجع لقاعدة بيانات Firebase، تحديدًا إلى مسار users.

                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();
                // استخراج القيم المدخلة من الحقول وتحويلها إلى نصوص.

                HelperClass helperClass = new HelperClass(name, email, username, password);
                reference.child(username).setValue(helperClass);
               // تخزين بيانات المستخدم في قاعدة البيانات تحت اسم المستخدم كـ "مفتاح".

                Toast.makeText(SignupActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
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
}