package eci.edu.cosw_lab8;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import eci.edu.cosw_lab8.Model.Post;

public class PostActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Bundle bundle = getIntent().getExtras();
        Post post = (Post)bundle.get("postObject");
        imageView = (ImageView)findViewById(R.id.imageView2);
        textView = (TextView)findViewById(R.id.textView);
        textView.setText(post.message);
        decodeBitmap(post.imageUri);
    }

    private void decodeBitmap(String dir) {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(dir);
        imageView.setImageBitmap(bitmap);
    }
}
