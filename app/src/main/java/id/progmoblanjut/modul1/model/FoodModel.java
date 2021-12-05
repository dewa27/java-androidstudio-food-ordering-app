package id.progmoblanjut.modul1.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class FoodModel implements Serializable {
    private int id_food;
    private String food_name;
    private Double price;
    private String description;
    private String food_filename;
    private int id_food_category;


    public FoodModel(int id_food, String food_name, Double price, String description, String food_filename,int id_food_category) {
        this.id_food=id_food;
        this.food_name = food_name;
        this.price = price;
        this.description = description;
        this.food_filename = food_filename;
        this.id_food_category = id_food_category;
    }
    public int getIdFood() {
        return id_food;
    }

    public void setId_food(int id_food) {
        this.id_food = id_food;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getPrice(int qty) {
        String rupiah="";
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        rupiah = kursIndonesia.format(this.price*qty);
        return rupiah;
    }
    public Double getRawprice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFood_filename() {
        return food_filename;
    }

    public void setfood_filename(String food_filename) {
        this.food_filename = food_filename;
    }

    public int getId_food_category() {
        return id_food_category;
    }

    public void setId_food_category(int id_food_category) {
        this.id_food_category = id_food_category;
    }
}
