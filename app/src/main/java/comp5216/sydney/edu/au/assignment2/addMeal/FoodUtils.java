package comp5216.sydney.edu.au.assignment2.addMeal;

import android.content.Context;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;

public class FoodUtils {
    public static ArrayList<FoodBean> getAllFood(Context context) {
        ArrayList<FoodBean> arrayList = new ArrayList<FoodBean>();
        for(int i = 0 ;i <1;i++)
        {
            FoodBean  usersFood = new FoodBean();
            usersFood.foodname = "Hamburger";
            usersFood.calorie = "100";
            usersFood.icon = ContextCompat.getDrawable(context, R.drawable.examplefood_burger);
            //通过context对象将一个资源id转换成一个Drawable对象。;
            usersFood.news_url= "http://www.sina.cn";
            arrayList.add(usersFood);

        }
        return arrayList;
    }
}
