package com.example.morgane.sharemiamapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword,inputPhone, inputPseudo;
    private Button btnSignIn, btnSignUp, btnResetPassword, btnTakePicture,btnChoosePicture,btnSupprImage;
    private ImageView imageViewProfil;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private static final int RESULT_LOAD_IMG = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputPhone = (EditText) findViewById(R.id.phoneNumber);
        inputPseudo = (EditText) findViewById(R.id.pseudonyme);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        btnTakePicture = ( Button) findViewById(R.id.btnTakePictureSignup);
        btnChoosePicture = (Button) findViewById(R.id.btnChoosePictureSignUp);
        btnSupprImage = (Button)findViewById(R.id.btnDeleteImgProfil);
        imageViewProfil = (ImageView) findViewById(R.id.imageProfile);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();
                String pseudo = inputPseudo.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Entrez une adresse email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Entrez un mot de passe!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Mot de passe trop faible, minimum 6 carractères!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(getApplicationContext(), "Entrez un numero de cellulaire", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pseudo)){
                    Toast.makeText(getApplicationContext(), "Entrez un pseudonyme", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference("Users");

                                    String imageProfil = saveImage();

                                    User s = new User(auth.getCurrentUser().getUid(), auth.getCurrentUser().getEmail(),inputPhone.getText().toString().trim(),inputPseudo.getText().toString().trim(),imageProfil,3);
                                    String id= mDatabaseReference.push().getKey();
                                    mDatabaseReference.child(id).setValue(s);
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));

                                    finish();
                                }
                            }
                        });




            }
        });



        btnTakePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
                    takePictureIntent.getExtras();


                }
            }
        });

        btnChoosePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        btnSupprImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewProfil.setVisibility(View.GONE);
                btnChoosePicture.setEnabled(true);
                btnTakePicture.setEnabled(true);
                btnSupprImage.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            BitmapDrawable imageDraw = new BitmapDrawable(imageBitmap);
            imageViewProfil.setBackground(imageDraw);

        }else if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK){
            Bitmap bm=null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            BitmapDrawable imageDraw = new BitmapDrawable(bm);
            imageViewProfil.setBackground(imageDraw);



        }
        imageViewProfil.setVisibility(View.VISIBLE);
        btnSupprImage.setVisibility(View.VISIBLE);
        btnTakePicture.setEnabled(false);
        btnChoosePicture.setEnabled(false);



    }


    private String saveImage() {
        imageViewProfil.setDrawingCacheEnabled(true);
        imageViewProfil.buildDrawingCache();
        Bitmap bitmap = imageViewProfil.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        String imageB64 = Base64.encodeToString(data, Base64.DEFAULT);
        return imageB64;
    }
}