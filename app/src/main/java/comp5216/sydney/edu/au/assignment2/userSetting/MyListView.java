package comp5216.sydney.edu.au.assignment2.userSetting;

import android.content.Context;
import android.widget.ListView;

public class MyListView extends ListView{

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(android.content.Context context,android.util.AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

}
