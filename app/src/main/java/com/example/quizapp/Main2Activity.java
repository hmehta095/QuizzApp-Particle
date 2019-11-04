package com.example.quizapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.mukesh.DrawingView;

import java.util.UUID;

public class Main2Activity extends AppCompatActivity {
    String imgSaved;
    TextView descriptionText;
    private DrawingView drawingView;
    private float size = (float) 20.00;
    private Button saveBtn;

    //public VisionServiceClient visionServiceClient = new VisionServiceRestClient("ac585835001b490a941d07984f938e77");


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        drawingView = findViewById(R.id.drawing);
        saveBtn = findViewById(R.id.button);
        drawingView.initializePen();
        drawingView.setPenColor(Color.RED);
        drawingView.setPenSize(size);
        LinearLayout paintLayout = findViewById(R.id.paintColor);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawingView.setDrawingCacheEnabled(true);
                imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(),drawingView.getDrawingCache(),
                        UUID.randomUUID().toString()+".png","drawing"
                );
                if (imgSaved != null)
                {
                    Toast.makeText(getApplicationContext(), "SAVED", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "OOps, not saved", Toast.LENGTH_LONG).show();
                }
                drawingView.destroyDrawingCache();
            }
        });//        AsyncTask<InputStream,String,String> visionTask = new AsyncTask<InputStream, String, String>() {
//           ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
//            @Override
//            protected String doInBackground(InputStream... inputStreams) {
//                try
//                {publishProgress("Recognizing....");
//                String[] features = {"Description"};
//                String[] details = {};
//
//                    AnalysisResult result = visionServiceClient.analyzeImage(inputStreams[0], features, details);
//                    String strResults = new Gson().toJson(result);
//                    return strResults;
//
//                }
//                catch (Exception e)
//                {
//return null;
//                }
//            }
//
//            @Override
//            protected void onPreExecute() {
//               mDialog.show();
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//               mDialog.dismiss();
//               AnalysisResult result = new Gson().fromJson(s,AnalysisResult.class);
//                descriptionText = findViewById(R.id.showDescription);
//                StringBuilder stringBuilder = new StringBuilder();
//                for (Caption caption : result.description.captions)
//                {
//                    descriptionText.setText(stringBuilder);
//                }
//            }
//
//            @Override
//            protected void onProgressUpdate(String... values) {
//               mDialog.setMessage(values[0]);
//            }
//
//        };
//        Bitmap bmImg = BitmapFactory.decodeFile("imageSaved");
//         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bmImg.compress(Bitmap.CompressFormat.PNG,100,outputStream);
//
//        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
//
//
//        visionTask.execute(inputStream);
    }


}
