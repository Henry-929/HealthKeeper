package comp5216.sydney.edu.au.assignment2.addMeal;

public class CustomFood {
    public String foodname,calorie,carbs,fat,protein;

    public CustomFood(){}
    public CustomFood(String foodname,String calorie,String carbs,String fat,String protein){
        this.foodname = foodname;
        this.calorie =calorie;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }

    public String getProtein() {
        return protein;
    }

    public String getFoodname() {
        return foodname;
    }

    public String getFat() {
        return fat;
    }

    public String getCalorie() {
        return calorie;
    }

    public String getCarbs() {
        return carbs;
    }
}
