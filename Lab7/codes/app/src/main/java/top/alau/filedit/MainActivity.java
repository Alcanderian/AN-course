package top.alau.filedit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout til_new_pwd;
    private TextInputLayout til_confirm_pwd;
    private Button btn_ok;
    private Button btn_clear;
    private Boolean has_pwd;
    private SharedPreferences sp_pwd = null;
    private MessageDigest md5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        til_confirm_pwd = findViewById(R.id.til_confirm_pwd);
        til_new_pwd = findViewById(R.id.til_new_pwd);
        btn_ok = findViewById(R.id.btn_ok);
        btn_clear = findViewById(R.id.btn_clear);

        sp_pwd = getSharedPreferences("alau_file_editor", MODE_PRIVATE);
        has_pwd = sp_pwd.getString("pwd", null) != null;

        try {
            md5 = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                til_confirm_pwd.getEditText().setText("");
                til_new_pwd.getEditText().setText("");
            }
        });

        if (has_pwd) {
            til_new_pwd.setVisibility(View.GONE);
            til_confirm_pwd.setHint("Password");
            til_confirm_pwd.getEditText().setHint("Password");
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String r_pwd = sp_pwd.getString("pwd", null);
                    String l_pwd = til_confirm_pwd.getEditText().getText().toString();
                    l_pwd = new String(md5.digest(l_pwd.getBytes()));
                    if (r_pwd.equals(l_pwd)) {
                        Intent it = new Intent(MainActivity.this, EditActivity.class);
                        startActivity(it);
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Password mismatch", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        } else {
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String n_pwd = til_new_pwd.getEditText().getText().toString();
                    String c_pwd = til_confirm_pwd.getEditText().getText().toString();
                    if (n_pwd.isEmpty()) {
                        Toast.makeText(MainActivity.this,
                                "Password cannot be empty", Toast.LENGTH_SHORT)
                                .show();
                    } else if (n_pwd.equals(c_pwd)) {
                        String r_pwd = new String(md5.digest(c_pwd.getBytes()));
                        sp_pwd.edit().putString("pwd", r_pwd).commit();
                        Intent it = new Intent(MainActivity.this, EditActivity.class);
                        startActivity(it);
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Password mismatch", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        }
    }
}
