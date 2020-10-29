package comp5216.sydney.edu.au.assignment2.main;

public class al_UsersFood {

    public String foodname,quantity,category;
    public double d_quantity;
    public String calorie,carbohydrate,fat,protein;
    public double d_calorie,d_carbohydrate,d_fat,d_protein;

    public al_UsersFood(String foodname,String quantity,String category,String calorie,String carbohydrate,String fat,String protein){
        this.foodname = foodname;
        this.quantity = quantity;
        this.category = category;

        this.calorie =calorie;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.protein = protein;

        //convertTodouble();

    }

    public void convertTodouble(){

        d_quantity = Double.parseDouble(quantity);

        d_calorie = Double.parseDouble(calorie);
        d_carbohydrate = Double.parseDouble(carbohydrate);
        d_fat = Double.parseDouble(fat);
        d_protein = Double.parseDouble(protein);

        d_calorie = d_calorie * d_quantity;
        d_carbohydrate = d_carbohydrate * d_quantity;
        d_fat = d_fat * d_quantity;
        d_protein = d_protein * d_quantity;

    }

    public String getCategory() {
        return category;
    }

    public double getD_calorie(){
        return d_calorie;
    }

    public double getD_carbohydrate() {
        return d_carbohydrate;
    }

    public double getD_fat() {
        return d_fat;
    }

    public double getD_protein() {
        return d_protein;
    }

    public double getD_quantity() {
        return d_quantity;
    }
}
