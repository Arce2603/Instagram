package com.example.arce.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.arce.instagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    //private static final String imageUrl= "/storage/emulated/0/DCIM/Camera/IMG_20180710_131547.jpg";
/*    private EditText descriptionInput;
    private Button createBtn;
    private Button refreshBtn;*/

    private Button cameraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cameraBtn = findViewById(R.id.btnCamera);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this,PhotoActivity.class);
                startActivity(i);
            }
        });

/*        descriptionInput = findViewById(R.id.etDesc);
        createBtn = findViewById(R.id.btnCreate);
        refreshBtn = findViewById(R.id.btnRefresh);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
                //final ParseFile img =

                final File file = new File(imageUrl);
                final ParseFile parseFile =  new ParseFile(file);

                createPost(description,parseFile,user);
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTopPost();
            }
        });*/

    }

    private void createPost(String description, ParseFile image, ParseUser user){
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(image);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e== null){
                        Log.d("HomeActivity","Create post success");
                }
                else{
                    e.printStackTrace();
                    Log.d("HomeActivity","Create post FAIL");
                }

            }
        });

    }

    private  void loadTopPost (){
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e== null){
                    for(int i = 0; i<objects.size(); i++)
                        Log.d("HomeActivity","Post("+i+") "+ objects.get(i).getDescription()
                                + "\n username =  " + objects.get(i).getUser().getUsername());
                }
                else{
                    e.printStackTrace();;
                }

            }
        });
    }
}
