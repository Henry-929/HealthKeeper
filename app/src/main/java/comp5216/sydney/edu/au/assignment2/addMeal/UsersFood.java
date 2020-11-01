package comp5216.sydney.edu.au.assignment2.addMeal;

import android.graphics.drawable.Drawable;

public class UsersFood {

    public String foodname,quantity,category;
    public String calorie;
    public Drawable icon;

    public UsersFood(){}

    public UsersFood(String foodname,String calorie,Drawable icon){
        this.foodname = foodname;
        this.calorie = calorie;
        this.icon = icon;
    }

    public String getFoodname() {
        return foodname;
    }

    public String getCalorie() {
        return calorie;
    }

    public Drawable getIcon() {
        return icon;
    }

    public UsersFood(String foodname, String quantity, String category){
        this.foodname = foodname;
        this.quantity = quantity;
        this.category = category;

    }
}
