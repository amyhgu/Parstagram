package me.amyhgu.parstagram;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import me.amyhgu.parstagram.model.Post;

public class HomeActivity extends AppCompatActivity
        implements CaptureFragment.OnCameraSelectedListener, ProfileFragment.OnItemSelectedListener, FeedFragment.OnPostSelectedListener {

    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    Context context;

    public final String APP_TAG = "Parstagram";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int CAPTURE_PROFILE_PICTURE_REQUEST_CODE = 1024;
    public String photoFileName = "photo.jpg";
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        fragment1 = new FeedFragment();
        fragment2 = new CaptureFragment();
        fragment3 = new ProfileFragment();
        context = getApplicationContext();

        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
                        fragmentTransaction1.replace(R.id.flContainer, fragment1).commit();
                        return true;
                    case R.id.action_camera:
                        FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                        fragmentTransaction2.replace(R.id.flContainer, fragment2).commit();
                        return true;
                    case R.id.action_profile:
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ProfileFragment profileFragment = ProfileFragment.newInstance(ParseUser.getCurrentUser());
                        ft.replace(R.id.flContainer, profileFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    // Now we can define the action to take in the activity when the fragment event fires
    // This is implementing the `OnItemSelectedListener` interface methods
    @Override
    public void onCameraButtonSelected(View view) {
        onLaunchCamera(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void onLogoutButtonSelected() {
        final Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onCreateButtonSelected() {
        FragmentTransaction fragmentTransactionFeed = getSupportFragmentManager().beginTransaction();
        fragmentTransactionFeed.replace(R.id.flContainer, new FeedFragment()).commit();
    }

    public void onPostPropicSelected(ParseUser user) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ProfileFragment profileFragment = ProfileFragment.newInstance(user);
        ft.replace(R.id.flContainer, profileFragment).commit();
    }

    public void onProfilePictureSelected() {
        onLaunchCamera(CAPTURE_PROFILE_PICTURE_REQUEST_CODE);
    }

    public void onLaunchCamera(int requestCode) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = PhotoHelper.getPhotoFileUri(photoFileName, context);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(HomeActivity.this, "me.amyhgu.parstagram.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                CaptureFragment fragment2 = (CaptureFragment) getSupportFragmentManager().findFragmentById(R.id.flContainer);
                Bitmap previewBitmap = PhotoHelper.resizePhoto(photoFile, context);
                fragment2.setPreviewImage(previewBitmap, PhotoHelper.imagePath);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAPTURE_PROFILE_PICTURE_REQUEST_CODE) {
            ProfileFragment fragment3 = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.flContainer);
            Bitmap propicBitmap = PhotoHelper.resizePhoto(photoFile, context);
            fragment3.setProfilePicture(propicBitmap, PhotoHelper.imagePath);
        }
    }
}
