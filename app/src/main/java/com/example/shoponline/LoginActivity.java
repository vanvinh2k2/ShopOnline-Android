package com.example.shoponline;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.annotations.Until;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.internal.Util;

public class LoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {
    TextView register, forgetpasswordtxt, googletxt;
    EditText emailedt, passwordedt;
    Button loginbtn;
    ImageView eye;
    CheckBox show;
    ReferenceManager manager;
    LoginButton loginButton;
    APIShop apiShop;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        process();
        login();
        google();
        facebook();
    }

    private void google() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        googletxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signIntent = gsc.getSignInIntent();
        startActivityForResult(signIntent, Ustils.CODE_GOOGLE);
    }
    private void login() {
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput()){
                    String email1 = emailedt.getText().toString().trim();
                    String password1 = passwordedt.getText().toString().trim();
                    compositeDisposable.add(apiShop.login(email1, password1)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    modelUser -> {
                                        if(modelUser.isSuccess()){
                                            text(modelUser.getMessage());
                                            User user = modelUser.getData();
                                            saveUser(user);
                                        }
                                    },
                                    throwable -> {
                                        text(throwable.getMessage());
                                        Log.d("loi", throwable.getMessage());
                                    }
                            ));
                }
            }
        });
    }

    private void saveUser(User user){
        manager.putString("id", user.getId());
        manager.putString("username", user.getUsername());
        manager.putString("email", user.getEmail());
        manager.putString("avatar", user.getAvatar());
        manager.putString("access", user.getToken().getAccess());
        manager.putString("refresh", user.getToken().getRefresh());
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void facebook() {
        loginButton = findViewById(R.id.login_button);
        loginButton.setLoginText("Continue with Facebook");
        loginButton.setLogoutText("Continue with Facebook");
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, this);
    }
    private void process() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show.isChecked()) {
                    show.setChecked(false);
                    eye.setImageResource(R.drawable.eye);
                    passwordedt.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    show.setChecked(true);
                    eye.setImageResource(R.drawable.hidden);
                    passwordedt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });
    }

    boolean checkInput(){
        if(emailedt.getText().toString().trim().isEmpty()){
            text("Please input Email!");
            emailedt.requestFocus();
            return false;
        }else if(passwordedt.getText().toString().trim().isEmpty()){
            text("Please input Password!");
            passwordedt.requestFocus();
            return false;
        }else return true;
    }

    void text(String v){
        Toast.makeText(this, v+"", Toast.LENGTH_SHORT).show();
    }
    public void init(){
        register = findViewById(R.id.registertxt);
        forgetpasswordtxt = findViewById(R.id.forgetpassword);
        googletxt = findViewById(R.id.login_google);
        emailedt = findViewById(R.id.email);
        passwordedt = findViewById(R.id.password);
        loginbtn = findViewById(R.id.loginaccount);
        show = findViewById(R.id.show);
        eye = findViewById(R.id.eye);
        loginButton = findViewById(R.id.login_button);
        apiShop = RetrofitClient.getInstance(Ustils.BASE_URL).create(APIShop.class);
        manager = new ReferenceManager(LoginActivity.this);
    }

    @Override
    protected void onStart() {
        if(manager.getString("username") != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        super.onStart();
    }

    private void registerGoogle(String name1, String email1, String id1, String avatar1) {
        compositeDisposable.add(apiShop.loginGoogle(id1, name1, avatar1, email1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                text(userModel.getMessage());
                                User user = userModel.getData();
                                saveUser(user);
                            }else{
                                text(userModel.getMessage());
                            }
                        },
                        throwable -> {
                            text(throwable.getMessage());
                        }
                ));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Ustils.CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                if (acct != null) {
                    String person = acct.getDisplayName();
                    String email = acct.getEmail();
                    String id = acct.getId();
                    String photoUrl = String.valueOf(acct.getPhotoUrl());
                    //Log.e("kq_google", "Name: " + person + "\tEmail: " + email + "\tIdGoogle: " + id + "\t Avatar: " + photoUrl);
                    registerGoogle(person, email, id, photoUrl);
                }
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        text("ok luon");
        Log.d("kyu", "kol");
    }

    @Override
    public void onCancel() {
        text("ok ca");
        Log.d("kyu", "kol");
    }

    @Override
    public void onError(FacebookException error) {
        text("ok er");
        Log.d("kyu", "kol");
    }
}