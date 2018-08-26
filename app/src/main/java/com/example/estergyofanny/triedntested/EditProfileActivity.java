package com.example.estergyofanny.triedntested;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class EditProfileActivity extends AppCompatActivity {
    EditText editTextUpdateNama;
    EditText editTextUpdateNIM;
    EditText editTextUpdateNoHP;
    EditText editTextUpdateTTL;
    ImageView imageEditItem;
    Button buttonUpdateEI;
    Button buttonKameraEI;

    SQLiteDatabase dbEditProfile;
    Profile prof;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        intent = getIntent();

        editTextUpdateNama = (EditText) findViewById(R.id.editTextUpdateNama);
        editTextUpdateNIM = (EditText) findViewById(R.id.editTextUpdateNIM);
        editTextUpdateNoHP = (EditText) findViewById(R.id.editTextUpdateNoHP);
        editTextUpdateTTL = (EditText) findViewById(R.id.editTextUpdateTTL);
        buttonUpdateEI = (Button) findViewById(R.id.buttonUpdateEI);
        buttonKameraEI = (Button) findViewById(R.id.buttonKameraEI);
        imageEditItem = (ImageView) findViewById(R.id.imageEditItem);

        prof = intent.getParcelableExtra("Profile");

        buttonKameraEI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        editTextUpdateNama.setText(prof.getTextNama());
        editTextUpdateNIM.setText(prof.getTextNIM());
        editTextUpdateNoHP.setText(prof.getTextNoHP());
        editTextUpdateTTL.setText(prof.getTextTTL());
        imageEditItem.setImageBitmap(decodeBase64(prof.getTextImage()));

        dbEditProfile = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbEditProfile.execSQL("Create table if not exists profil(nama VARCHAR, nim VARCHAR, NoHP VARCHAR, ttl date, image VARCHAR);");

        buttonUpdateEI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            imageEditItem.setImageBitmap(bp);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void checkInput() {
        if (prof.getTextNama().toString().equals("") || prof.getTextNIM().toString().equals("") || prof.getTextNoHP().toString().equals("") ||prof.getTextTTL().toString().equals("") || imageEditItem.getDrawable()== null) {
            Toast.makeText(EditProfileActivity.this, "data Profile tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        else {
            Cursor nim = dbEditProfile.rawQuery("select * from profil where NIM = '" + prof.getTextNIM() + "' ", null);
            if (nim.getCount()==1) {
                dbEditProfile.execSQL("update profil set nama = '" + editTextUpdateNama.getText() + "', ttl = " + editTextUpdateTTL.getText() + ", kelas = "+editTextUpdateNoHP.getText()+", image = '"+ encodeTobase64(((BitmapDrawable)imageEditItem.getDrawable()).getBitmap()) +"' where nim = '" + prof.getTextNIM() + "' ");
                Intent returnIntent = new Intent();
                //Profile newProfile = new Profile(editTextUpdateNama.getText().toString(), prof.getTextNIM().toString(), editTextUpdateKelas.getText().toString(), encodeTobase64(((BitmapDrawable)imageEditItem.getDrawable()).getBitmap()));
                //returnIntent.putExtra("UPDATEDPROFIL", newProfile);
                setResult(RESULT_OK,returnIntent);
                Toast.makeText(EditProfileActivity.this, "data tersimpan", Toast.LENGTH_SHORT).show();
            }
//          else{
//                Toast.makeText(TambahItemActivity.this,"produk telah terdaftar",Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
