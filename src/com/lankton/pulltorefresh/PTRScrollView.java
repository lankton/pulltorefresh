package com.lankton.pulltorefresh;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

public class PTRScrollView extends ScrollView{
    
    private String tag = "PTRScrollView";
    private Context context;
    private OnPullListener onPullListener;
    private boolean isInit = true;
    private int count = 0;
    private int screenHeight = 0;

    public PTRScrollView(Context context) {
        super(context);
        Log.i(tag, "construct with 1 param");
        this.context = context;
        // TODO Auto-generated constructor stub
    }
    public PTRScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(tag, "construct with 2 params");
        this.context = context;
        // TODO Auto-generated constructor stub
    }
    public PTRScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.i(tag, "construct with 3 params");
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public ListView lv = null; 
    public View headView = null;
    public int headViewHeight;
    
    public int getScreenHeight()
    {
        if(0 == this.screenHeight)
        {
            WindowManager wm = (WindowManager)context
                    .getSystemService(Context.WINDOW_SERVICE);
            screenHeight = wm.getDefaultDisplay().getHeight();
        }
        return this.screenHeight;
    }
    
    public void initView()
    {
        LinearLayout wrapper = (LinearLayout) this.getChildAt(0);
        headView = wrapper.getChildAt(0);
        lv = (ListView) wrapper.getChildAt(1);
    }
    
    public void setOnPullListener(OnPullListener opl)
    {
        this.onPullListener = opl;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);        
        initView();
        headViewHeight = headView.getLayoutParams().height;
        
        isInit = false;
    }
    
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        int scrollY = this.getScrollY();
        int progress = (headViewHeight - scrollY) * 100 / headViewHeight;
        if(null != this.onPullListener) {
            if(progress >= 0)
            {
                this.onPullListener.onPull(progress, ev.getAction());
            }
        }
        return super.onTouchEvent(ev);
    }
    
    public void reSize(ListView lv) {  
        ListAdapter listAdapter = lv.getAdapter();  
        if (listAdapter == null) {  
         return;  
        }  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, lv);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();  
        }  
        
        ViewGroup.LayoutParams params = lv.getLayoutParams();  
        
        params.height = totalHeight  
          + (lv.getDividerHeight() * (listAdapter.getCount() - 1));  
        
        if(params.height < this.getScreenHeight())
        {
            params.height = this.screenHeight;
        }
        lv.setLayoutParams(params);  
       }
    
    public void smoothHide()
    {
        this.post(new Runnable() {@Override public void run() {PTRScrollView.this.smoothScrollTo(0, PTRScrollView.this.headViewHeight);}});
    }
    public void quickHide()
    {
        this.scrollTo(0, PTRScrollView.this.headViewHeight);
    }

    interface OnPullListener{
        /*参数progress为下拉的从程度，<=0表示完全隐藏，100为完全拉下
         * action为动作 */
        void onPull(int progress, int action);
    }

}
