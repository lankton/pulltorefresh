package com.lankton.pulltorefresh;

import java.util.ArrayList;

import com.lankton.pulltorefresh.R;
import com.lankton.pulltorefresh.PTRScrollView.OnPullListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
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
    private ImageView head_img2;
    private MHandler handler;
    private Animation anim;
    private Animation anim_arrow;
    private Animation anim_arrow_reverse;
    
    private int nextIndex = 0;
    
    private static ArrayList<Character> charList = new ArrayList<Character>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        head_text = (TextView) this.findViewById(R.id.head_text);
        head_img = (ImageView) this.findViewById(R.id.head_img);
        head_img2 = (ImageView) this.findViewById(R.id.head_img2);
        
        anim = AnimationUtils.loadAnimation(this, R.anim.retate);
        anim.setFillAfter(true);
        anim_arrow = AnimationUtils.loadAnimation(this, R.anim.retate_arrow);
        anim_arrow.setFillAfter(true);
        anim_arrow_reverse = AnimationUtils.loadAnimation(this, R.anim.retate_arrow_reverse);
        anim_arrow_reverse.setFillAfter(true);
        
        sv = (PTRScrollView) this.findViewById(R.id.mPTRScrollView);
        sv.setSmoothScrollingEnabled(true);
        lv = (ListView) this.findViewById(R.id.listview);
        handler = new MHandler();
        for(int i = 0; i < 3; i++)
        {
            charList.add((char) ('a' + i));
        }
        nextIndex = 3;
        
        lv.setAdapter(new ArrayAdapter<Character>(this,
                android.R.layout.simple_list_item_1, charList));
        sv.reSize(lv);
        
        sv.setOnPullListener(new OnPullListener(){
            boolean isFull = false;
            boolean isLoading = false;

            @Override
            public void onPull(int progress, int action) {
                // TODO Auto-generated method stub
                if(isLoading)
                {
                    return;
                }
                if(progress == 100)
                {
                    if(MotionEvent.ACTION_UP == action)
                    {
                            head_img.clearAnimation();
                            head_img.setVisibility(View.INVISIBLE);
                            head_img2.setVisibility(View.VISIBLE);
                            head_img2.startAnimation(anim);
                            head_text.setText("loading...");
                            isLoading = true;
                            new Thread(new Runnable(){

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    try {
                                        Thread.sleep(3000);
                                        charList.add((char) ('a' + nextIndex));
                                        nextIndex ++;
                                        handler.sendEmptyMessage(0);
                                        isLoading = false;
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    
                                }
                                
                            }).start();
                            
                    } 
                    else
                    {
                        head_text.setText("release to refresh");
                                                    
                    }
                    isFull = true;
                }
                else
                {
                    if(MotionEvent.ACTION_UP == action)
                    {
                        sv.smoothHide();
                    }
                    else if(progress > 0)
                    {
                        if(isFull)
                        {
                            head_img.startAnimation(anim_arrow_reverse);
                        }
                        head_text.setText("pull to refresh (" + progress + "%)");
                    }
                    isFull = false;
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
                sv.quickHide();
                ((ArrayAdapter)lv.getAdapter()).notifyDataSetChanged();
                sv.reSize(lv); 
                head_img2.clearAnimation();
                head_img2.setVisibility(View.INVISIBLE);
                head_img.setVisibility(View.VISIBLE);
                head_text.setText("");
                
                Toast.makeText(MainActivity.this, "load successful", Toast.LENGTH_LONG).show();
            }
        }
        
        
    }
    
    
}
