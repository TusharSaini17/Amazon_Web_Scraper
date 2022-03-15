package com.example.amazonwebscrapper;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText searchText;
    Button searchButton;
    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchText = findViewById(R.id.searchEditText);
        searchButton= findViewById(R.id.searchButton);
        textView= findViewById(R.id.titleView);
        imageView= findViewById(R.id.imageView);

        searchButton.setOnClickListener(view -> {
            String actualString=searchText.getText().toString().trim();
            if(actualString.isEmpty()){
                Toast toast = Toast.makeText(getApplicationContext(), "Enter Search query", Toast.LENGTH_SHORT);
                toast.show();
            }else{
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                            Document doc=null;
                            Document doc1=null;

                            String search_1 =searchText.getText().toString();
                            String Template1="https://www.amazon.in/s?k=";
                            String Template2 ="&ref=nb_sb_noss_1";
                            String Final = search_1.trim().replace(" ","+");
                            String url= Template1+Final+Template2;

                            try {
                                doc= Jsoup.connect(url).followRedirects(true).timeout(10000).get();
                            } catch (IOException a) {
                                a.printStackTrace();
                                Log.i(TAG, "url1: "+a);
                            }
                            assert doc != null;
                            Elements links = doc.getElementsByClass("s-result-item s-asin sg-col-0-of-12 sg-col-16-of-20 sg-col s-widget-spacing-small sg-col-12-of-16");
                            String data = links.toString();
                            String link="https://www.amazon.in/"+"product-name"+"/dp/"+(data.substring(16,26))+"/ref=sr_1";

                            try {
                                doc1= Jsoup.connect(link).followRedirects(true).timeout(10000).get();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.i(TAG, "url2: "+e);
                            }
                            assert doc1 != null;
                            Elements Title = doc1.select("#productTitle");
                            String TitleTxt = Title.text();

                            Elements Image = doc1.select("#landingImage");
                            String ImageTxt = Image.attr("src");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(TitleTxt);
                            Glide.with(MainActivity.this).load(ImageTxt).into(imageView);
                        }
                    });
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });


    }


}