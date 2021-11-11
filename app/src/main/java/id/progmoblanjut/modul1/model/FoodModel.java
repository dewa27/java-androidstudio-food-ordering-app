package id.progmoblanjut.modul1.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class FoodModel implements Serializable {
    private int food_id;
    private String food_name;
    private Double food_price;
    private String food_description;
    private int food_pic;
    public FoodModel(int food_id,String food_name, Double food_price, String food_description, int food_pic) {
        this.food_id=food_id;
        this.food_name = food_name;
        this.food_price = food_price;
        this.food_description = food_description;
        this.food_pic = food_pic;
    }
    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_price(int qty) {
        String rupiah="";
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        rupiah = kursIndonesia.format(this.food_price*qty);
        return rupiah;
    }
    public Double getRawFood_price() {
        return this.food_price;
    }

    public void setFood_price(Double food_price) {
        this.food_price = food_price;
    }

    public String getFood_description() {
        return food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
    }

    public int getFood_pic() {
        return food_pic;
    }

    public void setFood_pic(int food_pic) {
        this.food_pic = food_pic;
    }
}
