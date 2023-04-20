package com.example.norman_lee.comicapp;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    EditText editTextComicNo;
    Button buttonGetComic;
    TextView textViewTitle;
    ImageView imageViewComic;

    String comicNo;
    public static final String TAG = "MyLog";
    final String ERROR_NO_NETWORK = "No Network";
    final String ERROR_NOT_VALID = "Comic No Not Valid";
    final String ERROR_MALFORMED_URL = "Malformed URL";
    final String ERROR_BAD_JSON = "Bad JSON Response";
    final String ERROR_HTTPS_ERROR = "HTTPS Error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO 6.0 Study the Utils class and see what methods are available for you
        //TODO 6.1 Ensure that Android Manifest has permissions for internet and has orientation fixed
        //TODO 6.2 Get references to widgets
        //TODO 6.3 Set up setOnClickListener for the button
        editTextComicNo = findViewById(R.id.editTextComicNo);
        buttonGetComic = findViewById(R.id.buttonGetComic);
        imageViewComic = findViewById(R.id.imageViewComic);
        textViewTitle = findViewById(R.id.textViewTitle);

        buttonGetComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comicNumber = editTextComicNo.getText().toString();
                //TODO 6.9
                if (Utils.isNetworkAvailable(MainActivity.this)){
                    getComic(comicNumber);
                }
                else{
                    Toast.makeText(MainActivity.this, ERROR_NO_NETWORK,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //TODO 6.4 Retrieve the user input from the EditText
        //TODO 6.5 - 6.9 Modify getComic below
        //TODO 6.10 If network is active, call the getComic method with the userInput

    }


    //TODO 6.5 - 6.9 ****************
    //TODO you are reminded that new Runnable{} is an anonymous inner class
    //TODO 6.5 Make sure getComic has the signature getComic(final String userInput); make sure an executor and a handler are instantiated
    //TODO 6.6 (background work) create a final Container<Bitmap> cBitmap object which will be used for commmunication between the main thread and the child thread
    //TODO 6.7 (background work) Call Utils.getImageURLFromXkcdApi to get the image URL from comicNo
    //TODO 6.8 (background work)Call Utils.getBitmap using the URL to get the bitmap
    //TODO 6.9 (UI thread work)Assign the Bitmap downloaded to imageView. The bitmap may be null.


    void getComic(final String userInput) {
        // Executor -- to execute the background
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // Handler -- to talk to the UI
        final Handler handler = new Handler(Looper.getMainLooper());


        //execute a background class
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //the code below must be inside the run class because this is the background thread
                // you need to run the background thread inside a runnable object
                //TODO 6.7
                try {
                    String urlString = Utils.getImageURLFromXkcdApi(userInput);
                    //TODO 6.8
                    //convert url string into url object
                    URL url = new URL(urlString);
                    /** Bitmap is Android's datatype to store images */
                    Bitmap bitmap = Utils.getBitmap(url);   //returns a Bitmap
                    // YOU ARE IN BACKGROUND THREAD
                    // YOU NEED TO TALK TO THE UI THREAD
                    //you need container datatype to pass messages
                    Container<Bitmap> bitmapContainer = new Container<>();
                    bitmapContainer.set(bitmap);
                    // U NEED HANDLER.POST WITH RUNNABLE
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //what u want to do in the UI thread?
                            //imageViewComic.setImageBitmap(bitmap);

                            //can access the bitmap directly
                            imageViewComic.setImageBitmap(bitmapContainer.get());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();    //Display a Toast
                } catch (JSONException e) {
                    e.printStackTrace();    //Display a Toast
                }
            }
        });

    }

    //TODO 6.6
    final static class Container<T>{
        //instance variable t of type T
        private T t;

        T get(){
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }
        //declare input variable as t and input type as T
        void set(T t){
            this.t = t;
        }
    }

}
