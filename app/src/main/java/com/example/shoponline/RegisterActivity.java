package com.example.shoponline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoponline.constant.ReferenceManager;
import com.example.shoponline.constant.Ustils;
import com.example.shoponline.model.User;
import com.example.shoponline.retrofit.APIShop;
import com.example.shoponline.retrofit.RetrofitClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {
    TextView logintxt;
    EditText nameedt, emailedt, passwordedt, copasswordedt;
    Button createbtn;
    ImageView eye1, eye2;
    CheckBox show1, show2;
    APIShop apiShop;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ReferenceManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        process();
        register();
    }

    private void register() {
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput()){
                    String name1 = nameedt.getText().toString().trim();
                    String email1 = emailedt.getText().toString().trim();
                    String password1 = passwordedt.getText().toString().trim();
                    compositeDisposable.add(apiShop.register(email1, name1, password1)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    modelUser -> {
                                        if(modelUser.isSuccess()){
                                            text(modelUser.getMessage());
                                            User user = modelUser.getData();
                                            manager.putString("id", user.getId());
                                            manager.putString("username", user.getUsername());
                                            manager.putString("email", user.getEmail());
                                            manager.putString("avatar", user.getAvatar());
                                            manager.putString("access", user.getToken().getAccess());
                                            manager.putString("refresh", user.getToken().getRefresh());
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }else{
                                            text(modelUser.getMessage());
                                        }
                                    },
                                    throwable -> {
                                        text(throwable.getMessage());
                                        Log.e("loi", throwable.getMessage());
                                    }
                            ));
                }
            }
        });

    }

    private void process() {
        logintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        eye1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show1.isChecked()) {
                    show1.setChecked(false);
                    eye1.setImageResource(R.drawable.eye);
                    passwordedt.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    show1.setChecked(true);
                    eye1.setImageResource(R.drawable.hidden);
                    passwordedt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });
        eye2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show2.isChecked()) {
                    show2.setChecked(false);
                    eye2.setImageResource(R.drawable.eye);
                    copasswordedt.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    show2.setChecked(true);
                    eye2.setImageResource(R.drawable.hidden);
                    copasswordedt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });
    }

    boolean checkInput(){
        if(nameedt.getText().toString().trim().isEmpty()){
            text("Please input Name!");
            nameedt.requestFocus();
            return false;
        }else if(emailedt.getText().toString().trim().isEmpty()){
            text("Please input Email!");
            emailedt.requestFocus();
            return false;
        }else if(passwordedt.getText().toString().trim().isEmpty()){
            text("Please input Password!");
            passwordedt.requestFocus();
            return false;
        }else if(copasswordedt.getText().toString().trim().isEmpty()){
            text("Please input Password Confirmation!");
            passwordedt.requestFocus();
            return false;
        }else if(copasswordedt.getText().toString().compareTo(passwordedt.getText().toString())!=0){
            text("Password Confirmation and Passwrod is not same!");
            copasswordedt.requestFocus();
            return false;
        }else return true;
    }

    void text(String v){
        Toast.makeText(this, v+"", Toast.LENGTH_SHORT).show();
    }
    public void init(){
        logintxt = findViewById(R.id.logintxt);
        nameedt = findViewById(R.id.name);
        emailedt = findViewById(R.id.email);
        passwordedt = findViewById(R.id.password);
        copasswordedt = findViewById(R.id.passwordcf);
        createbtn = findViewById(R.id.create);
        eye1 = findViewById(R.id.eyeimg);
        eye2 = findViewById(R.id.eyeimg2);
        show1 = findViewById(R.id.show1);
        show2 = findViewById(R.id.show2);
        apiShop = RetrofitClient.getInstance(Ustils.BASE_URL).create(APIShop.class);
        manager = new ReferenceManager(RegisterActivity.this);
    }
}