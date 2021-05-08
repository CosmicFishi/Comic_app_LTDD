package com.example.comic_app;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

enum EnumPage {
    LOGIN, REGISTER;

    public EnumPage switchPage() {
        if (this == EnumPage.LOGIN)
            return EnumPage.REGISTER;
        else
            return EnumPage.LOGIN;
    }
}

public class LoginSignupActivity extends Activity {
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;
    private EnumPage currentPage = EnumPage.LOGIN;

    private final String emailRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private final String passRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

    TextView textViewTitle;
    EditText editTextUsername, editTextPassword, editTextDisplayName, editTextPhone, editTextConfirmPassword;
    Button btnLogin, btnLoginGoogle, btnSignUp;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);

        bidingUI();
        switchPage(EnumPage.LOGIN);
        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignUp.setOnClickListener(v -> {
            if (currentPage != EnumPage.REGISTER){
                switchPage(EnumPage.REGISTER);
                return;
            }

            String email = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            if(email.isEmpty() && password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter the form.", Toast.LENGTH_SHORT).show();
            } else if (editTextPassword.getText().toString() != editTextConfirmPassword.getText().toString()){
                Toast.makeText(getApplicationContext(), "Password confirm not correct", Toast.LENGTH_SHORT).show();
            } else if (checkValidEmailAndPassword(email,password)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    adduserDetail(editTextPhone.getText().toString());
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(editTextDisplayName.getText().toString())
                                            .build();
                                    auth.getCurrentUser().updateProfile(profileUpdates);
                                    changeMainActivity();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed: create account, please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        });

        btnLogin.setOnClickListener(v -> {
            if (currentPage != EnumPage.LOGIN){
                switchPage(EnumPage.LOGIN);
                return;
            }

            String email = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            if(email.isEmpty() && password.isEmpty() ) {
                Toast.makeText(getApplicationContext(), "Please enter the correct information", Toast.LENGTH_SHORT).show();
            } else if (checkValidEmailAndPassword(email, password)) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    changeMainActivity();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        btnLoginGoogle.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null){
                changeMainActivity();
            }
            signIn(); // dùng để hiện lên ds tài khoản Google để kết nối đến.
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void bidingUI(){
        editTextUsername = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextDisplayName = findViewById(R.id.editTextDisplayName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        textViewTitle = findViewById(R.id.textViewTitle);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (auth.getCurrentUser().getPhoneNumber() != "")
                                    adduserDetail(auth.getCurrentUser().getPhoneNumber());
                                else
                                    adduserDetail("Not set");

                                changeMainActivity();
                            } else {
                                Toast.makeText(getApplicationContext(), "Can't get the access Token.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (ApiException e) {
            Log.w("ERROR==============", "signInResult:failed code=" + e.getStatusCode());
            e.printStackTrace();
        }
    }

    public void changeMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private boolean checkValidEmailAndPassword(String email, String password) {
        if(Utils.checkValidRegex(email, emailRegex) && Utils.checkValidRegex(password, passRegex))
            return true;
        Toast.makeText(getApplicationContext(), "Input incorrect email or password format. Password must be strong", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void adduserDetail(String phone){
        Map<String, Object> user = new HashMap<>();
        user.put("comicHistory", new ArrayList<String>());
        user.put("favouriteComic", new ArrayList<String>());
        user.put("phone", phone);

        fireStore.collection("user").document(mAuth.getUid())
                .set(user, SetOptions.merge())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "ERROR create profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void switchPage(EnumPage page){
        if (page == EnumPage.LOGIN){
            textViewTitle.setText("LOGIN");
            editTextDisplayName.setVisibility(View.GONE);
            editTextPhone.setVisibility(View.GONE);
            editTextConfirmPassword.setVisibility(View.GONE);
            currentPage = EnumPage.LOGIN;
            editTextUsername.requestFocus();
        }
        else {
            textViewTitle.setText("RESIGTER");
            editTextDisplayName.setVisibility(View.VISIBLE);
            editTextPhone.setVisibility(View.VISIBLE);
            editTextConfirmPassword.setVisibility(View.VISIBLE);
            currentPage = EnumPage.REGISTER;
            editTextDisplayName.requestFocus();
        }
    }

}