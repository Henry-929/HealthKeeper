package comp5216.sydney.edu.au.assignment2.addMeal;

public class Food {
    public int calorie,carbohydrate,fat,protein;

    public Food(int calorie,int carbohydrate,int fat,int protein){
        this.calorie =calorie;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.protein = protein;
    }

    public double getCalorie() {
        return calorie;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public double getFat() {
        return fat;
    }

    public double getProtein() {
        return protein;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }
}
