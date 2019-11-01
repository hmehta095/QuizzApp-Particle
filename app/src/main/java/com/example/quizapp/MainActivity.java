package com.example.quizapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.ParticleEvent;
import io.particle.android.sdk.cloud.ParticleEventHandler;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;

public class MainActivity extends AppCompatActivity {
    // MARK: Debug info
    private final String TAG="QUIZ";

    TextView tv,tv1;
    ImageView img;
    Button btn;
    String[] Key = new String[] {"A","B"};

    // MARK: Particle Account Info
    private final String PARTICLE_USERNAME = "hmehta095@gmail.com";
    private final String PARTICLE_PASSWORD = "Hmehta_095@";

    // MARK: Particle device-specific info
    private final String DEVICE_ID = "31002e000447363333343435";

    // MARK: Particle Publish / Subscribe variables
    private long subscriptionId;

    // MARK: Particle device
    private ParticleDevice mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.result);
        tv1 = findViewById(R.id.textView);
        img = findViewById(R.id.imageView);
        btn = findViewById(R.id.score);

        // 1. Initialize your connection to the Particle API
        ParticleCloudSDK.init(this.getApplicationContext());

        // 2. Setup your device variable
        getDeviceFromCloud();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {



                    @Override
                    public void run() {
                        tv.setText("" + scoreCount);
                        showScore("" + scoreCount);

                    }
                });


            }
        });

    }


    /**
     * Custom function to connect to the Particle Cloud and get the device
     */
    public void getDeviceFromCloud() {
        // This function runs in the background
        // It tries to connect to the Particle Cloud and get your device
        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                particleCloud.logIn(PARTICLE_USERNAME, PARTICLE_PASSWORD);
                mDevice = particleCloud.getDevice(DEVICE_ID);
                return -1;

            }

            @Override
            public void onSuccess(Object o) {

                Log.d(TAG, "Successfully got device from Cloud");

                // if you get the device, then go subscribe to events
                subscribeToParticleEvents();
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }


    int i = 0;
    int j = 1;
    int k = 1;
    int scoreCount = 0;
    public void result(String answer){
        runOnUiThread(new Runnable() {



            @Override
            public void run() {

                try{
                    if (answer.equals(Key[i])){
                        tv.setText("Correct");

                        if (Key[1].equals(Key[i])){
                            turnParticleGreen("circle");


                            if (j == 1) {
                                scoreCount = scoreCount + 1;
                                j++;
                            }

                        }
                        else if (Key[0].equals(Key[i])){
                            turnParticleGreen("triangle");

                            if (k == 1) {
                                scoreCount = scoreCount + 1;
                                k++;
                            }
                        }


                    }
                    else {
                        tv.setText("Wrong");
                        turnParticleRed();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
                }

            }
        });



        Log.d(TAG, "I'm inside the result() function");

    }
    public void nextQue(){


        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if(i<1){
                    i++;
                    img.setImageResource(R.drawable.circle);
                    tv1.setText("How many Sides 0 or 1? " +
                            "\n A. 1 \n B. 0\n\n" +
                            "Enter your response using the Particle.\n\n" +
                            "(A = Button 1, B = Button 0)");
                    tv.setText("Answer");

                }else{
                    i++;
                    img.setImageResource(R.drawable.coder);
                    tv1.setText("Quiz Finished");
                    tv.setText("");
                }

            }
        });



    }


    public void turnParticleGreen(String green) {

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                // put your logic here to talk to the particle
                // --------------------------------------------
                List<String> functionParameters = new ArrayList<String>();
                functionParameters.add(green);
                try {
                    mDevice.callFunction("answer", functionParameters);

                } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                    e1.printStackTrace();
                }


                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                // put your success message here
                Log.d(TAG, "Success: Turned light green!!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                // put your error handling code here
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }


    public void showScore(String score) {

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                // put your logic here to talk to the particle
                // --------------------------------------------
                List<String> functionParameters = new ArrayList<String>();
                functionParameters.add(score);
                try {
                    mDevice.callFunction("score", functionParameters);

                } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                    e1.printStackTrace();
                }


                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                // put your success message here
                Log.d(TAG, "Success: Turned light green!!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                // put your error handling code here
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }



    public void turnParticleRed() {

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                // put your logic here to talk to the particle
                // --------------------------------------------
                List<String> functionParameters = new ArrayList<String>();
                functionParameters.add("red");
                try {
                    mDevice.callFunction("answer", functionParameters);

                } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                    e1.printStackTrace();
                }


                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                // put your success message here
                Log.d(TAG, "Success: Turned lights red!!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                // put your error handling code here
                Log.d(TAG, exception.getBestMessage());
            }
        });



    }




    public void subscribeToParticleEvents() {
        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                subscriptionId = ParticleCloudSDK.getCloud().subscribeToAllEvents(
                        "playerChoice",  // the first argument, "eventNamePrefix", is optional
                        new ParticleEventHandler() {
                            public void onEvent(String eventName, ParticleEvent event) {
                                Log.i(TAG, "Received event with payload: " + event.dataPayload);
                                String choice = event.dataPayload;
                                if (choice.contentEquals("A")) {
                                    Log.d(TAG, "Trying to go inside result");
                                    result("A");


                                }
                                else if (choice.contentEquals("B")) {
                                    Log.d(TAG, "Trying to go inside result");
                                    result("B");


                                }
                                else if(choice.contentEquals("true")) {
                                    nextQue();
                                    Log.d(TAG, "I am in true condition");
                                }
                                else if(choice.contentEquals("C")){
                                    showScore(""+ scoreCount);
                                }

                            }

                            public void onEventError(Exception e) {
                                Log.e(TAG, "Event error: ", e);
                            }
                        });


                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "Success: Subscribed to events!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }
}
