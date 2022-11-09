package samples.speech.cognitiveservices.microsoft.web;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import java.lang.ref.WeakReference;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    String url = "http://192.168.0.4:8088/";
    TextView textView;
    String msg;
    final Bundle bundle = new Bundle();
    private final MyHandler handler = new MyHandler(this);
    private class MyHandler extends Handler {
        private final WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity activity) {
            this.weakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            weakReference.get().textView.setText(bundle.getString("message"));
            /*if (bundle.equals("1")) {
                setContentView(R.layout.popup1);
            } else if (bundle.equals("0")) {
                setContentView(R.layout.popup2);
            } else {
                Toast.makeText(getApplicationContext(), "탐지가 되지 않았습니다. 다시 시도해주세요." ,Toast.LENGTH_SHORT).show();
            }*/
        }
    }
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textview);


      new Thread(() -> {
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
                Element elements = doc.select("body").first();
                String str = elements.text();
                if (str.equals("1")) {
                    setContentView(R.layout.popup1);
                } else if (str.equals("0")) {
                    setContentView(R.layout.popup2);
                } else {
                    Toast.makeText(getApplicationContext(), "탐지가 되지 않았습니다. 다시 시도해주세요." ,Toast.LENGTH_SHORT).show();
                }
                msg = elements.text(); //body 값 불러와 저장
                bundle.putString("message",msg);
                Message msg = handler.obtainMessage();
                msg.setData(bundle);
                handler.sendMessage(msg);

            } catch (IOException e){
                e.printStackTrace();
            }
        }).start();

    }

}

