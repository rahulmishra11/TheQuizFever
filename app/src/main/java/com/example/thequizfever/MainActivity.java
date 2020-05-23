package com.example.thequizfever;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thequizfever.Model.Users;
import com.example.thequizfever.User_Info.User_Info;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.BindView;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.btn_login)
    Button login;

    @BindView(R.id.input_phone)
    EditText inputphone;

    @BindView(R.id.input_password)
    EditText inputPass;

    @BindView(R.id.link_signup)
    TextView signup;

    @BindView(R.id.text_forgot)
    TextView forgot_psswd;

    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loadingBar = new ProgressDialog(this);
        Paper.init(this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this ,SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loginUser();
            }
        });

        forgot_psswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent =new Intent(MainActivity.this,OTPActivity.class);
            startActivity(intent);
            }
        });

        String UserPhone = Paper.book().read(User_Info.UserPhoneKey);
        String UserPassword = Paper.book().read(User_Info.UserPasswordKey);

        if (UserPhone != "" && UserPassword != "")
        {
            if (!TextUtils.isEmpty(UserPhone)  &&  !TextUtils.isEmpty(UserPassword))
            {
                AllowAccess(UserPhone, UserPassword);
                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }

    private void AllowAccess(final String phone, final String pass) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child(phone).exists()){
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPass().equals(pass)){

                            Toast.makeText(MainActivity.this,"Logged in Succesfully.. ",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(MainActivity.this,homepage.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(MainActivity.this,"Wrong Password..",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                { Toast.makeText(MainActivity.this,"Account With this Phone Number does not exists",Toast.LENGTH_SHORT).show(); }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loginUser() {
      String phone = inputphone.getText().toString();
      String Pass = inputPass.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pass))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            UserAccount(phone, Pass);
        }


    }

    private void UserAccount(final String phone, final String pass) {
        Paper.book().write(User_Info.UserPhoneKey, phone);
        Paper.book().write(User_Info.UserPasswordKey, pass);
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child(phone).exists()){
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPass().equals(pass)){

                            Toast.makeText(MainActivity.this,"Logged in Succesfully.. ",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,homepage.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Wrong Password..", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                { loadingBar.dismiss();
                    Toast.makeText(MainActivity.this,"Account With this Phone Number does not exists",Toast.LENGTH_SHORT).show(); }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
