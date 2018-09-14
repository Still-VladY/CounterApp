package com.example.vgubarev.testapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FireBaseOaut extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView; //объявляем поля и авторизацию
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_oaut);

        mStatusTextView = findViewById(R.id.status);  //определяем поля по id
        mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        findViewById(R.id.emailSignInButton).setOnClickListener(this);  //определяем кнопки, добавляем слушателей
        findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();   //инициализируем авторизацию
    }

    @Override
    public void onStart() {
        super.onStart();
        // Проверяем зареган ли юзер
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) { //Проверка на заполненность полей
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)    //создаем нвоого пользователя, если успешно - кидаем на форму с подтверждением входа, не успешно - ошибка
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("StringFormatMatches")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                                    user.getEmail()));

                            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
                            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
                            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(FireBaseOaut.this, "Ошибка регистрации",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        hideProgressDialog();
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password) //Авторизация по логину и паролю, если успешно, перекидываем на главную активити, не успешно - ошибка
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(FireBaseOaut.this, "Ошибка авторизации",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void signOut() {  //метод выхода
        mAuth.signOut();
        updateUI(null);
    }

    private boolean validateForm() {  //проверка на заполненность полей
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Введите вашу почту");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Введите пароль");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(final FirebaseUser user) { //обновление инфы о зареганом юзере, перекидывание на активити со счетчиками
        hideProgressDialog();

        if (user != null) {

            DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");
            db.addValueEventListener(new ValueEventListener() {
                @SuppressLint("StringFormatMatches")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    showProgressDialog();
                    String val = dataSnapshot.child(user.getUid()).child("access").getValue(String.class);
                    hideProgressDialog();
                    if (val != null) {
                        Intent intent = new Intent(getApplicationContext(), MainFrame.class);
                        startActivity(intent);
                    } else {
                        FirebaseUser user = mAuth.getCurrentUser();
                        mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                                user.getEmail()));

                        findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
                        findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
                        findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.emailCreateAccountButton) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.emailSignInButton) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.signOutButton) {
            signOut();
        }
    }
}
