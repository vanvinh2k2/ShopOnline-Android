package com.example.shoponline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    FloatingActionButton callphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        process();
    }

    private void process() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new HomeFragment());
        transaction.commit();
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(item.getItemId() == R.id.home){
                    transaction.replace(R.id.container, new HomeFragment());
                    item.setChecked(true);
                }else if(item.getItemId() == R.id.vendor){
                    transaction.replace(R.id.container, new VendorFragment());
                    item.setChecked(true);
                }else if(item.getItemId() == R.id.notify){
                    transaction.replace(R.id.container, new HomeFragment());
                    item.setChecked(true);
                }else if(item.getItemId() == R.id.profile){
                    transaction.replace(R.id.container, new HomeFragment());
                    item.setChecked(true);
                }else if(item.getItemId() == R.id.contact){
                    transaction.replace(R.id.container, new HomeFragment());
                    item.setChecked(true);
                }
                transaction.commit();
                return false;
            }
        });
        callphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {android.Manifest.permission.CALL_PHONE}, 123);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==123 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:0354342295"));
            startActivity(intent);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void init(){
        bottomNavigation = findViewById(R.id.bottomNavigationView);
        callphone = findViewById(R.id.callphone);
    }
}