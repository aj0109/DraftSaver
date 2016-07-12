package com.surroundsync.ashrayjoshi.draftsaver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,LocationListener {
    private static String currentTimeStamp;
    public EditText etDraftString, etRetrieveString;
    File file;
    Button btnGenerate, btnList,btnCamera;
    DatabaseHandler db;
    String ret = "";
    ListView listView;
    ArrayList<Drafts> drafts;
    ImageView ivCamera;
    private Uri fileUri;
    boolean cam;
    private LocationManager locationManager;
    private Location location;
    private String provider;
    private String stringLatitude = null;
    private String stringLongitude = null;
    private Criteria criteria;
    private double latitude;
    private double longitude;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        currentTimeStamp = dateFormat.format(new Date());
        db = new DatabaseHandler(MainActivity.this);
        etDraftString = (EditText) findViewById(R.id.activity_et_string);
        etRetrieveString = (EditText) findViewById(R.id.activity_et_string2);
        btnGenerate = (Button) findViewById(R.id.button);
        drafts = new ArrayList<>();
        btnList = (Button) findViewById(R.id.button3);
        listView = (ListView) findViewById(R.id.list_view);
        btnCamera=(Button) findViewById(R.id.button2);
        getLocation();
        btnCamera.setOnClickListener(this);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                etRetrieveString.setText(CustomAdapter.request.get(i).getDrafts());
            }
        });
       /// ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, );



        btnGenerate.setOnClickListener(this);
        btnList.setOnClickListener(this);


    }

    private String readFile() {
        Log.d("READ", "reading from db");
        drafts = db.getAllContacts();
        final CustomAdapter adapter = new CustomAdapter(this, drafts);

       listView.setAdapter(adapter);
        Log.d("read", "COUNT : "+drafts.size());

       try {
            FileInputStream inputStream = new FileInputStream(String.valueOf(getLatestFilefromDir(Environment.getExternalStorageDirectory() + File.separator + "Music_Folder/Report Files")));

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();

                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;



    }

    private File getLatestFilefromDir(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final String draftString = etDraftString.getText().toString();
        try {
            File root = new File(Environment.getExternalStorageDirectory() + File.separator
                    + "Music_Folder", "Report Files");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, currentTimeStamp + ".txt");


            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(draftString + "\n\n");
            writer.flush();
            writer.close();
            Log.d("Valuesall",""+cam);
            db.addDraft(new Drafts(currentTimeStamp, draftString,cam,stringLatitude,stringLongitude));
            Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();

        }


        //FileOutputStream outputStream = null;

 /*       try {
            file = new File("/sdcard/SurroundSync/");
            if (!file.exists()) {
                file.mkdir();
            }

            outputStream = new FileOutputStream(file.toString()+"drafts.txt");
            outputStream.write(draftString.getBytes());
            outputStream.close();

            *//*outputStream = openFileOutput(file, Context.MODE_PRIVATE);
            outputStream.write(draftString.getBytes());
            outputStream.close();*//*
            Toast.makeText(MainActivity.this,"File Created",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"File Not Created",Toast.LENGTH_SHORT).show();
            Log.e("Files"," "+e);

        }


    }*/

    }

  /*  public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Drafts");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                etRetrieveString.setText(readFile());
                break;
            case R.id.button2:
                 cameraIntent();
            case R.id.button3:
                listView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


       /* Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ivCamera.setImageBitmap(thumbnail);*/

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0)
                onSelectFromGalleryResult(data);
            else if (requestCode == 1)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory() + File.separator
                + "Music_Folder", "Image_Files");
        if (!destination.exists()) {
            destination.mkdirs();
        }
        File gpxfile = new File(destination, currentTimeStamp + ".jpg");

        Log.d("TAG", "path : "+gpxfile);
        FileOutputStream fo;
        try {
            gpxfile.createNewFile();
            fo = new FileOutputStream(gpxfile);
            fo.write(bytes.toByteArray());
            fo.close();
            if (gpxfile == null) {
                cam = false;
            } else {
                cam = true;
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       // ivCamera.setImageBitmap(thumbnail);
    }
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //ivCamera.setImageBitmap(bm);

        }


    public void getLocation() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);
        Log.d("locationlog", "location :" + location);
        if (location != null) {
            location = locationManager.getLastKnownLocation(provider);
            onLocationChanged(location);
        } else {
            List<String> providers = locationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {

                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null
                        || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;
                }
            }
            if (bestLocation == null) {
                bestLocation = null;
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            }
            location = bestLocation;
            Log.d("locationlog", "location :" + location);
            latitude = location.getLatitude();
            stringLatitude = String.valueOf(latitude);
            longitude = location.getLongitude();
            stringLongitude = String.valueOf(longitude);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        stringLatitude = String.valueOf(latitude);
        longitude = location.getLongitude();
        stringLongitude = String.valueOf(longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
