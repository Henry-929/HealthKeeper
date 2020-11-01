package comp5216.sydney.edu.au.assignment2.main;

public class al_UsersFood {
    private static int foodCount = 0;

    private String foodname,quantity,category;
//    public double d_quantity;
    private String calorie,carbs,fat,protein;
//    public double d_calorie,d_carbohydrate,d_fat,d_protein;

    public al_UsersFood(){}

    public al_UsersFood(String calorie,String carbs,String fat,String protein){
        this.calorie =calorie;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }

    public al_UsersFood(String foodname,String quantity,String category,String calorie,String carbs,String fat,String protein){
        this.foodname = foodname;
        this.quantity = quantity;
        this.category = category;

        this.calorie =calorie;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;

        //convertTodouble();

    }

    public void clear_al_UsersFood(){
        this.foodname = null;
        this.quantity = null;
        this.category = null;

        this.calorie =null;
        this.carbs = null;
        this.fat = null;
        this.protein = null;

    }

    public static void incrementFoodCount() {
        foodCount = foodCount +1;
    }

    public static int getFoodCount() {
        return foodCount;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public void setCarbohydrate(String carbs) {
        this.carbs = carbs;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getCalorie() {
        return calorie;
    }

    public String getCarbs() {
        return carbs;
    }

    public String getFat() {
        return fat;
    }

    public String getFoodname() {
        return foodname;
    }

    public String getProtein() {
        return protein;
    }

    public String getQuantity() {
        return quantity;
    }
}
