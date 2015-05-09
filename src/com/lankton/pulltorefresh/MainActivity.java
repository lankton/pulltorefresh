package com.lankton.pulltorefresh;

import java.util.ArrayList;

import com.lankton.pulltorefresh.R;
import com.lankton.pulltorefresh.PTRScrollView.OnPullListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {

    private ListView lv;
    private PTRScrollView sv;
    private TextView head_text;
    private ImageView head_img;
    private MHandler handler;
    private Animation anim;
    private int nextIndex = 0;
    
    private static ArrayList<Character> charList = new ArrayList<Character>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        head_text = (TextView) this.findViewById(R.id.head_text);
        head_img = (ImageView) this.findViewById(R.id.head_img);
        
        anim = AnimationUtils.loadAnimation(this, R.anim.retate);
        anim.setFillAfter(true);
        
        sv = (PTRScrollView) this.findViewById(R.id.mPTRScrollView);
        sv.setSmoothScrollingEnabled(true);
        lv = (ListView) this.findViewById(R.id.listview);
        handler = new MHandler();
        for(int i = 0; i < 26; i++)
        {
            charList.add((char) ('a' + i));
        }
        nextIndex = 3;
        
        lv.setAdapter(new ArrayAdapter<Character>(this,
                android.R.layout.simple_list_item_1, charList));
        lv.setSelection(0);
        sv.reSize(lv);
        
        sv.setOnPullListener(new OnPullListener(){

            @Override
            public void onPull(int progress, int action) {
                // TODO Auto-generated method stub
                
                if(progress == 100)
                {
                    if(MotionEvent.ACTION_UP == action || MotionEvent.ACTION_HOVER_EXIT == action)
                    {
                            head_img.startAnimation(anim);
                            head_text.setText("loading...");
                            new Thread(new Runnable(){

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    try {
                                        charList.add((char) ('a' + nextIndex));
                                        nextIndex ++;
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    handler.sendEmptyMessage(0);
                                }
                                
                            }).start();
                            
                    } 
                    else
                    {
                             head_text.setText("release to refresh");
                    }
                }
                else
                {
                    if(MotionEvent.ACTION_UP == action)
                    {
                        sv.smoothHide();
                    }
                    else if(progress > 0)
                    {
                        {
                            head_text.setText("pulled: " + progress + "%");
                        }
                    }
                }
                
                
            }
            
        });
    }

    
    public class MHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(0 == msg.what) {
                ((ArrayAdapter)lv.getAdapter()).notifyDataSetChanged();
                sv.reSize(lv);
                head_img.clearAnimation();
                sv.smoothHide();
            }
        }
        
        
    }
    
    
}
