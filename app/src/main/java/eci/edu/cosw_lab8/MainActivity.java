package eci.edu.cosw_lab8;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URI;

import eci.edu.cosw_lab8.Model.Post;

public class MainActivity extends AppCompatActivity {

    private String APP_DIRECTORY = "myPictureApp/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY+"media";
    private String TEMPORAL_PICTURE_NAME = "photo.jpg";
    private String location ="";
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
    }

    public void addPhoto(View view) {
        final CharSequence[] options = {"Camera", "Galeria","Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose an option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                location="";
                if(options[which]=="Camera"){
                    openCamera();
                }else if(options[which]=="Galeria"){
                    Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 200);
                }else if (options[which]=="Cancel"){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100:
                if(resultCode== RESULT_OK){
                    String dir = Environment.getExternalStorageDirectory()+File.separator+MEDIA_DIRECTORY+File.separator+TEMPORAL_PICTURE_NAME;
                    location = dir+"";
                    System.out.println(location+" Con camara queda aqui.");
                    decodeBitmap(dir);
                }
                break;
            case 200:
                if(resultCode==RESULT_OK){
                    Uri path = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(path,filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    location = cursor.getString(columnIndex)+"";
                    cursor.close();
                    Bitmap bmp = null;
                    try {
                    bmp = getBitmapFromUri(path);
                    } catch (IOException e) {
                    e.printStackTrace();
                    }
                    imageView.setImageBitmap(bmp);
                }
                break;
        }
    }

    private void decodeBitmap(String dir) {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(dir);
        imageView.setImageBitmap(bitmap);
    }

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(),MEDIA_DIRECTORY);
        file.mkdirs();
        String path = Environment.getExternalStorageDirectory()+File.separator+MEDIA_DIRECTORY+File.separator+TEMPORAL_PICTURE_NAME;
        File newFile = new File(path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        startActivityForResult(intent,100);

    }

    public void save(View view) {
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        String message = autoCompleteTextView.getText().toString();
        if(message.isEmpty() && location.isEmpty()){
            autoCompleteTextView.setError("Please enter either a message or select an image");
        }else{
            Post postObj = new Post(message,location);
            Intent intent = new Intent(this, PostActivity.class);
            intent.putExtra("postObject",postObj);
            startActivity(intent);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
