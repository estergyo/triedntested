package com.example.estergyofanny.triedntested;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.example.estergyofanny.triedntested.EditProfileActivity.decodeBase64;

public class InventoryFragment extends Fragment {
    EditText editTextUpdateNama;
    EditText editTextUpdateNIM;
    EditText editTextUpdateNoHP;
    EditText editTextUpdateTTL;
    ImageView imageEditItem;
    Button buttonUpdateEI;
    Button buttonKameraEI;

    Profile prof;
    Intent intent;

    Bitmap img;
    String uname;
    String noim;
    String noHP;
    String ttl;
    ByteArrayOutputStream byteArrayOutputStream ;
    byte[] byteArray ;
    String strimg ;

    HttpURLConnection httpURLConnection ;
    URL url; OutputStream outputStream;
    BufferedWriter bufferedWriter ;
    int RC ;
    BufferedReader bufferedReader ;
    StringBuilder stringBuilder;
    boolean check = true;

    String ImageTag = "image_tag" ;
    String ImageName = "image_data" ;
    String Nim = "nim" ;
    String NoHp = "nohp";
    String Tanggal = "tanggal";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        intent = getActivity().getIntent();

        byteArrayOutputStream = new ByteArrayOutputStream();

        editTextUpdateNama = (EditText) getActivity().findViewById(R.id.editTextUpdateNama);
        editTextUpdateNIM = (EditText) getActivity().findViewById(R.id.editTextUpdateNIM);
        editTextUpdateNoHP = (EditText) getActivity().findViewById(R.id.editTextUpdateNoHP);
        editTextUpdateTTL = (EditText) getActivity().findViewById(R.id.editTextUpdateTTL);
        buttonUpdateEI = (Button) getActivity().findViewById(R.id.buttonUpdateEI);
        buttonKameraEI = (Button) getActivity().findViewById(R.id.buttonKameraEI);
        imageEditItem = (ImageView) getActivity().findViewById(R.id.imageEditItem);

        imageEditItem.setImageDrawable(getResources().getDrawable(R.drawable.picture));

        prof = intent.getParcelableExtra("prof_object");
        //String prof = intent.getExtras().getString("prof_object");

        Log.d("name from parcellable", prof.getTextNama());

        editTextUpdateNama.setText(prof.getTextNama());

        buttonKameraEI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        buttonUpdateEI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = editTextUpdateNama.getText().toString();
                noim = editTextUpdateNIM.getText().toString();
                noHP = editTextUpdateNoHP.getText().toString();
                ttl = editTextUpdateTTL.getText().toString();
                UploadImageToServer();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            img = (Bitmap) data.getExtras().get("data");
            imageEditItem.setImageBitmap(img);
        }
    }

    public void UploadImageToServer(){
        img.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        strimg = Base64.encodeToString(byteArray, Base64.DEFAULT);
        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {
            @Override
            protected void  onPostExecute(String string1){
                super.onPostExecute(string1);
                Toast.makeText(getActivity(), string1, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void...Params){
                ImageProccessClass imageProcessClass = new ImageProccessClass();
                HashMap<String,String> HashMapParams = new HashMap<String,String>();
                HashMapParams.put(ImageTag, uname);
                HashMapParams.put(Nim, noim);
                HashMapParams.put(NoHp, noHP);
                HashMapParams.put(Tanggal, ttl);
                HashMapParams.put(ImageName, strimg);
                String FinalData = imageProcessClass.ImageHttpRequest("http://sasmitoh.nitarahmawati.my.id/upload-image-to-server.php/", HashMapParams);
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ =new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProccessClass {
        public String ImageHttpRequest(String requestURL,HashMap<String,String>PData){
            StringBuilder stringBuilder = new StringBuilder();
            try{
                url = new URL(requestURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(20000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                outputStream = httpURLConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(bufferedWriterDataFN(PData));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                RC = httpURLConnection.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK){
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReader.readLine()) != null){
                        stringBuilder.append(RC2);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }
        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()){
                if (check) check = false;
                else
                    stringBuilder.append("&");
                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF8"));


            }return stringBuilder.toString();

        }
    }
}
