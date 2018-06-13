package com.example.xuan.celebrate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celeLinkes = new ArrayList<>();
    ArrayList<String> celeNames = new ArrayList<>();
    int choosenPerson = 0;
    String[] answer = new String[4];
    int correctAnswer = 0;
    ImageView imageView;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    public void click(View view){

        if(view.getTag().toString().equals(Integer.toString(correctAnswer))){

            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getApplicationContext(), "Wrong! It was" + celeNames.get(choosenPerson), Toast.LENGTH_SHORT).show();
        }
        tryAgain();
    }

    public class Downloadimage extends AsyncTask<String, Void, Bitmap>{


        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String>{



        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection;
            try{
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                //get the data stream into string to store in the result;
                while(data != -1){
                    char current = (char)data;

                    result += current;

                    data = reader.read();
                }

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
            return result;
        }
    }
    public void tryAgain(){
        try {
            Random rand = new Random();

            choosenPerson = rand.nextInt(celeLinkes.size());

            Downloadimage downloadimage = new Downloadimage();

            Bitmap image = downloadimage.execute(celeLinkes.get(choosenPerson)).get();

            imageView.setImageBitmap(image);

            correctAnswer = rand.nextInt(4);

            int incorrectAnswer;

            for (int i = 0; i < 4; i++) {
                if (i == correctAnswer) {
                    answer[i] = celeNames.get(choosenPerson);
                } else {
                    incorrectAnswer = rand.nextInt(celeLinkes.size());
                    while (incorrectAnswer == choosenPerson) {
                        incorrectAnswer = rand.nextInt(celeLinkes.size());
                    }
                    answer[i] = celeNames.get(incorrectAnswer);
                }
            }
            button1.setText(answer[0]);
            button2.setText(answer[1]);
            button3.setText(answer[2]);
            button4.setText(answer[3]);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        String result = null;
        imageView = findViewById(R.id.imageView);
        button1 = findViewById(R.id.A);
        button2 = findViewById(R.id.B);
        button3 = findViewById(R.id.C);
        button4 = findViewById(R.id.D);

        try{
            //get the result from download
            result = task.execute("http://www.posh24.se/kandisar").get();
           // Log.i("URL", result);
            //we only want first part;
            String[] splitResult = result.split("<div class=\"listedArticles\">");

            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);

            while(m.find()){
                //System.out.println(m.group(1));
                celeLinkes.add(m.group(1));
            }
            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitResult[0]);

            while(m.find()){
                //System.out.println(m.group(1));
                celeNames.add(m.group(1));
            }

            Random rand = new Random();

            choosenPerson = rand.nextInt(celeLinkes.size());

            Downloadimage downloadimage = new Downloadimage();

            Bitmap image = downloadimage.execute(celeLinkes.get(choosenPerson)).get();

            imageView.setImageBitmap(image);

            correctAnswer = rand.nextInt(4);

            int incorrectAnswer;

            for(int i = 0; i < 4; i++){
                if(i == correctAnswer){
                    answer[i] = celeNames.get(choosenPerson);
                }else{
                    incorrectAnswer = rand.nextInt(celeLinkes.size());
                    while(incorrectAnswer == choosenPerson) {
                        incorrectAnswer = rand.nextInt(celeLinkes.size());
                    }
                    answer[i] = celeNames.get(incorrectAnswer);
                }
            }
            button1.setText(answer[0]);
            button2.setText(answer[1]);
            button3.setText(answer[2]);
            button4.setText(answer[3]);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
