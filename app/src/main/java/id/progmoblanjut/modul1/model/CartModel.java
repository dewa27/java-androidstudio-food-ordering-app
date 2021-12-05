package id.progmoblanjut.modul1.model;

public class CartModel {
    private int id_cart;
    private FoodModel food;
    private int id_customer;
    private int cart_qty;
    private String notes;

    public CartModel(int id_cart, FoodModel food, int id_customer, int cart_qty,String notes) {
        this.id_cart = id_cart;
        this.food = food;
        this.id_customer = id_customer;
        this.cart_qty = cart_qty;
        this.notes=notes;
    }

    public int getId_cart() {
        return id_cart;
    }

    public void setId_cart(int id_cart) {
        this.id_cart = id_cart;
    }

    public FoodModel getFood() {
        return food;
    }

    public void setFood(FoodModel food) {
        this.food = food;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public int getCart_qty() {
        return cart_qty;
    }

    public void setCart_qty(int cart_qty) {
        this.cart_qty = cart_qty;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "cart_id"+ this.id_cart + "\n" + "qty : " + this.cart_qty + "\nfood : "+ this.food;
    }
}
