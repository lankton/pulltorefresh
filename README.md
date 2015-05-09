# pulltorefresh
An android project that implements a pull-to-refresh ListVIew with a ScrollView

#Use it in XML
I created a class called 'PTRScrollView' that extends ScrollView . 
You can use PTRScrollView in layout file like this：
<com.lankton.pulltorefresh.PTRScrollView
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:id="@+id/mPTRScrollView"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <LinearLayout
            android:orientation="vertical"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
<!--this is the head space that'll be displayed when you pull to refresh. u can use your own view here -->
            <include
                layout="@layout/header"
                android:layout_width="match_parent"
                android:layout_height="100dp"/>
<!--this is the ListView that show data -->
            <ListView
                android:id="@+id/listview" 
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>
        </LinearLayout>
    </com.lankton.pulltorefresh.PTRScrollView>
    
#OnPullListener    
PTRScrollView provides interface OnPullListener what has a callback function OnPull(int, int). The 1st arguments represents the number of the percentage points shows how much you pull the head space out， ranging 0 - 100. The 2nd arg represents the MotionEvent， such as MotionEvent.ACTION_UP, MotionEvent.ACTION_MOVE, etc.  The callback function will be excuted when you touching on the screen, so you can use these 2 args do what you want , like making some animations, or someting else.
You can use the function setOnPullListener provided by PTRScrollView to set your OnPullListener.

#Other
Directly using a ListView in a ScrollView may cause some displaying error. To avoid such a situation, you should use the resize(ListView ) function provided by PTRScrollView after initing you listview or the excuting of function notifyDataSetChanged of your listview adapter . 

specific using are referenced in this sample project .^ ^ 

    

