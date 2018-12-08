package com.example.tyler.visionapitest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.FaceDetector;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.*;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.ImageProperties;
import com.google.gson.Gson;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    FaceDetector detector;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    private String api;

    @BindView(R.id.ivCam) ImageView mImageView;
    @BindView(R.id.btnSubmit) Button bSubmit;
    Feature visionFeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        api = "LANDMARK_DETECTION";
        //detector = new FaceDetector(displayMetrics.widthPixels,displayMetrics.heightPixels,2);

        visionFeature = new Feature();
        visionFeature.setType("LANDMARK_DETECTION");
        visionFeature.setMaxResults(1);

    }



    @OnClick(R.id.btnCam)
    public void startCamera(View view){
        //mAsyncTask.execute();
        final Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(camIntent, 1);
            bSubmit.setEnabled(true);
            // Create new thread
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    // TODO: Add code to turn the picture that was just taken into proper input for Vision API request
                }
                Image inputImage;
                //inputImage.encodeContent(photoData);
            });
        }
    }

    @OnClick(R.id.btnSubmit)
    public void submitRequest(View view){

    }


    private static class mAsyncTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            //InputStream iStream = getResources().openRawResource(mImageView);
            //byte[] photoData = IOUtils.toByteArray(iStream);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            onCameraPictureGet(imageBitmap);

        }
    }

    public void onCameraPictureGet(Bitmap bit){
        /* Intent intent = new Intent(getBaseContext(), ProcessActivity.class);
        intent.putExtra("picture", b);
        startActivity(intent);
        */

        final List<Feature> featureList = new ArrayList<>();
        featureList.add(visionFeature);

        final List<AnnotateImageRequest> annotateImageRequests = new ArrayList<>();

        AnnotateImageRequest annotateImageReq = new AnnotateImageRequest();
        annotateImageReq.setFeatures(featureList);
        annotateImageReq.setImage(getImageEncodeImage(bit));
        annotateImageRequests.add(annotateImageReq);

        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    String message = "";

                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
                    //Builder initializes web connections
                    Vision.Builder visionBuild = new Vision.Builder(httpTransport, jsonFactory, null);
                    //Connecting with the API key
                    visionBuild.setVisionRequestInitializer(new VisionRequestInitializer("AIzaSyBIb9TPC2CYBD93NJBu5msYjIKseWMRyqQ"));

                    Vision vision = visionBuild.build();
                    BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(annotateImageRequests);

                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    AnnotateImageResponse imageResponses = response.getResponses().get(0);
                    ImageProperties imageProperties = imageResponses.getImagePropertiesAnnotation();

                    return convertResponseToString(response);
                } catch (GoogleJsonResponseException e) {
                    Log.d("MainActivity", "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d("MainActivity", "failed to make API request because of other IOException " + e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                //Textview visionAPIData.setText(result);
                Toast resultToast = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
                resultToast.show();

            }
        }.execute();
    }

    @NonNull
    private Image getImageEncodeImage(Bitmap bitmap) {
        Image base64EncodedImage = new Image();
        // Convert the bitmap to a JPEG
        // Just in case it's a format that Android understands but Cloud Vision
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // Base64 encode the JPEG
        base64EncodedImage.encodeContent(imageBytes);
        return base64EncodedImage;
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {

        AnnotateImageResponse imageResponses = response.getResponses().get(0);

        List<EntityAnnotation> entityAnnotations;

        String message = "";
        switch (api) {
            case "LANDMARK_DETECTION":
                entityAnnotations = imageResponses.getLandmarkAnnotations();
                message = formatAnnotation(entityAnnotations);
                break;
        }
        return message;
    }

    private String formatAnnotation(List<EntityAnnotation> entityAnnotation) {
        String message = "";

        if (entityAnnotation != null) {
            for (EntityAnnotation entity : entityAnnotation) {
                message = message + "    " + entity.getDescription() + " " + entity.getScore();
                message += "\n";
            }
        } else {
            message = "Nothing Found";
        }
        return message;
    }

}
