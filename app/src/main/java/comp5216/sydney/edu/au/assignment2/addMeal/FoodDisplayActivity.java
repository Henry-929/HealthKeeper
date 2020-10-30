package comp5216.sydney.edu.au.assignment2.addMeal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;

public class FoodDisplayActivity extends Activity implements AdapterView.OnItemClickListener {

    private Context mContext;

    private LinearLayout ll_quit,progress_bar;
    private TextView calorieIntake,calorieTotal,calorieLeft;
    private String userIntake,userTotal,userLeft;
    private int leftInt;
    private float bar_width;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        mContext = this;

        //1.获取新闻数据用list封装
        ArrayList<FoodBean> allFood = FoodUtils.getAllFood(mContext);
        //2.找到控件
        ListView listView_breakfast = (ListView) findViewById(R.id.listView_breakfast);
        //3.创建一个adapter设置给listview
        FoodAdapter foodAdapter = new FoodAdapter(mContext, allFood);
        listView_breakfast.setAdapter(foodAdapter);
        //4.设置listview条目的点击事件
        listView_breakfast.setOnItemClickListener(this);

        ll_quit = (LinearLayout)findViewById(R.id.ll_display_food_cancel);
        ll_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDisplayActivity.this, MainActivity.class);
                if (intent != null) {
                    FoodDisplayActivity.this.startActivity(intent);
                }
            }
        });

        calorieIntake = (TextView)findViewById(R.id.meal_display_already_intake);
        calorieTotal = (TextView)findViewById(R.id.meal_display_total_intake);
        calorieLeft= (TextView)findViewById(R.id.meal_display_left_intake);
        userIntake = "1250";
        userTotal="2500";
        calorieIntake.setText(userIntake);
        calorieTotal.setText(userTotal);
        leftInt = Integer.parseInt(userTotal)-Integer.parseInt(userIntake);
        userLeft = String.valueOf(leftInt);
        calorieLeft.setText(userLeft);

        progress_bar = (LinearLayout)findViewById(R.id.calorie_progress_bar);
        bar_width = (float)Integer.parseInt(userIntake)/(float)Integer.parseInt(userTotal)*250;
        System.out.println(bar_width+"111111111111111111111111111111111");
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) progress_bar.getLayoutParams();
        Resources r = getResources();
        linearParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,bar_width,r.getDisplayMetrics());
        progress_bar.setLayoutParams(linearParams);
    }

    //listview的条目点击时会调用该方法   parent:代表listviw  view:点击的条目上的那个view对象   position:条目的位置  id： 条目的id
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        //需要获取条目上bean对象中url做跳转
        FoodBean bean = (FoodBean) parent.getItemAtPosition(position);

        String url = bean.news_url;

        //跳转浏览器
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }
}
