package comp5216.sydney.edu.au.assignment2.addMeal;

public class Food {
    public String calorie,carbohydrate,fat,protein;

    public Food(String calorie,String carbohydrate,String fat,String protein){
        this.calorie =calorie;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.protein = protein;
    }

    public String getCalorie() {
        return calorie;
    }

    public String getCarbohydrate() {
        return carbohydrate;
    }

    public String getFat() {
        return fat;
    }

    public String getProtein() {
        return protein;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public void setCarbohydrate(String carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }
}
