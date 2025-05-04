package com.example.signuploginrealtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ربط هذه الشاشة بواجهة activity_login.xml.
        setContentView(R.layout.activity_login);
        // يربط المتغيرات بعناصر الواجهة الرسومية في XML.
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);

        //parti button
        loginButton.setOnClickListener(new View.OnClickListener() {
            //يتم الانتقال إلى
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()){
                    System.out.println("ERROR LOGIN");
                } else {
                    checkUser();
                }
            }
        });
        //parti lien signup
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            //عند الضغط على النص "لا تملك حساب؟ سجل"، يتم الانتقال إلى SignupActivity.
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateUsername(){
        String val = loginUsername.getText().toString();
        if (val.isEmpty()){
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()){
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){
        //trim() لحذف أي فراغات في البداية أو النهاية.
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        //يتم الوصول إلى جدول users في Firebase.
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
        //يتم إجراء بحث (Query) عن المستخدم الذي يطابق username.

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
        //الاستماع للنتيجة من Firebase:


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)){


                        //جلب البيانات من Firebase:

                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);

                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        // تمرير بيانات المستخدم من صفحة تسجيل الدخول (LoginActivity) إلى صفحة الملف الشخصي (ProfileActivity)
                        //(Intent) للتنقل من LoginActivity إلى ProfileActivity.
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);
                        // إرفاق البيانات مع الـ Intent:
                        //putExtra تُستخدم لإرسال بيانات إلى النشاط (Activity) الآخر.

                        startActivity(intent);
                        //يتم الآن فتح ProfileActivity مع البيانات المرسلة.

                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                        //يتم عرض رسالة خطأ تحت حقل كلمة المرور.
                    }
                } else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                    //يتم عرض رسالة بأن اسم المستخدم غير موجود.
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            // في حال فشل القراءة من Firebase (نادرًا):
            }
        });
    }

}