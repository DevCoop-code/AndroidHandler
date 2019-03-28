package com.nexstreaming.app.handlerexample;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int SEND_INFORMATION = 0;
    public static final int SEND_STOP = 1;

    TextView textView;
    Button startButton;
    Button stopButton;

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.text);
        startButton = (Button)findViewById(R.id.btn1);
        stopButton = (Button)findViewById(R.id.btn2);

        //Start to Thread when click the start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread = new Thread();
                thread.start();
            }
        });

        //Stop the Thread and send 'SEND_STOP' message to handler
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(SEND_STOP);
            }
        });
    }

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case SEND_INFORMATION:
                    textView.setText(Integer.toString(msg.arg1) + msg.obj);
                    break;

                case SEND_STOP:
                    thread.stopThread();
                    textView.setText("Stop the Thread");
                    break;
            }
        }
    };

    class Thread extends java.lang.Thread{
        boolean stopped = false;
        int i = 0;

        public Thread(){
            stopped = false;
        }

        public void stopThread(){
            stopped = true;
        }

        @Override
        public void run(){
            super.run();

            while(stopped == false){
                i++;

                //Obtain the message
                Message message = handler.obtainMessage();

                //Setting Message ID
                message.what = SEND_INFORMATION;

                //Setting Message content(int)
                message.arg1 = i;

                //Setting Message content(Object)
                String information = new String("Working seconds.");
                message.obj = information;

                //Send to Message
                handler.sendMessage(message);

                try{
                    sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }

            }
        }
    }
}
