package id.progmoblanjut.modul1.model;

public class CartModel {
    private int cart_id;
    private FoodModel food;
    private int cust_id;
    private int qty;

    public CartModel(int cart_id, FoodModel food, int cust_id, int qty) {
        this.cart_id = cart_id;
        this.food = food;
        this.cust_id = cust_id;
        this.qty = qty;
    }

    public int getCart_id(){
        return cart_id;
    }

    public void setCart_id(int cart_id){
        this.cart_id = cart_id;
    }

    public FoodModel getFood(){
        return food;
    }

    public void setFood(FoodModel food){
        this.food = food;
    }

    public int getCust_id(){
        return cust_id;
    }

    public void setCust_id(int cust_id){
        this.cust_id = cust_id;
    }

    public int getQty(){
        return qty;
    }

    public void setQty(int qty){
        this.qty = qty;
    }
    @Override
    public String toString() {
        return "cart_id"+ this.cart_id + "\n" + "qty : " + this.qty + "\nfood : "+ this.food;
    }
}
