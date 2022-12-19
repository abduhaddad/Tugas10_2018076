package com.example.a2018076_tugas4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import com.example.a2018076_tugas4.databinding.ActivityLoginBinding;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {
    MyDbHelper myDbHelper;
    private ActivityLoginBinding binding;
    private SessionManager session;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup view binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        session = new SessionManager(getApplicationContext());
        myDbHelper = new MyDbHelper(this);
        userDetails = new UserDetails();
        binding.btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = binding.inpSerialNumber.getText().toString();
        if (v.getId() == R.id.btnSignIn) {
            Boolean result = myDbHelper.findPassword(username);
            if (result == true) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                session.setLogin(true);
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Serial Number didn`t match\nApakah anda ingin membuat baru?")
                        .setPositiveButton(R.string.YA, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                userDetails.setName("Abdullah");
                                userDetails.setEmail("alhaddadabdullah832@gmail.com");
                                userDetails.setSerialNumber(username);
                                long rowid = myDbHelper.insertData(userDetails);
                                if (rowid > 0) {
                                    Toast.makeText(getApplicationContext(), "Row " + rowid + "is successfully inserted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    session.setLogin(true);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Row not inserted ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.TIDAK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }
}
