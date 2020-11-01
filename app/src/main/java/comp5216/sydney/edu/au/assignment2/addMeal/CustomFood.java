package comp5216.sydney.edu.au.assignment2.addMeal;

public class CustomFood {
    private static int CustomfoodCount = 0;

    public String foodname,calorie,carbs,fat,protein;

    public CustomFood(){}
    public CustomFood(String foodname,String calorie,String carbs,String fat,String protein){
        this.foodname = foodname;
        this.calorie =calorie;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }

    public static void incrementFoodCount() {
        CustomfoodCount = CustomfoodCount +1;
    }

    public static int getFoodCount() {
        return CustomfoodCount;
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

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }
}
