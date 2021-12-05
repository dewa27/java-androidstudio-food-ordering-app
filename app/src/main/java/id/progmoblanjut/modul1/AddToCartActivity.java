package id.progmoblanjut.modul1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import id.progmoblanjut.modul1.database.DbHelper;
import id.progmoblanjut.modul1.model.CustomerModel;
import id.progmoblanjut.modul1.model.FoodModel;

public class AddToCartActivity extends AppCompatActivity {
    private TextView tv_food_name,tv_food_price,tv_description,tv_food_qty;
    private EditText et_notes;
    private ImageButton dec_btn,inc_btn;
    private Button addtocart_btn;
    private int qty;
    private DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        tv_food_name=findViewById(R.id.txtFoodName);
        tv_food_qty=findViewById(R.id.txtFoodQty);
        tv_description=findViewById(R.id.txtFoodDesc);
        tv_food_price=findViewById(R.id.txtFoodPrice);
        et_notes=findViewById(R.id.editTextNotes);
        dec_btn=findViewById(R.id.imgBtnMin);
        inc_btn=findViewById(R.id.imgBtnPlus);
        addtocart_btn=findViewById(R.id.btnAddToCart);

        db=new DbHelper(this);
        Gson gson=new Gson();
        String jsonFoodModel= getIntent().getStringExtra("food_model");
        FoodModel foodModel=gson.fromJson(jsonFoodModel,FoodModel.class);
        SharedPreferences mPrefs= this.getSharedPreferences("pref",0);
        String json = mPrefs.getString("loggedCustomerData", "");
        CustomerModel loggedCustomerData = gson.fromJson(json, CustomerModel.class);

        addtocart_btn.setText("Tambahkan ke Keranjang");
        tv_food_name.setText(foodModel.getFood_name());
        tv_description.setText(foodModel.getDescription());
        tv_food_price.setText(foodModel.getPrice(1));
        qty=1;
        dec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qty>1){
                    qty--;
                    tv_food_qty.setText(Integer.toString(qty));
                }
                if(qty==1){
                    dec_btn.setEnabled(false);
                }
            }
        });
        inc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++qty;
                tv_food_qty.setText(Integer.toString(qty));
                if(qty>1){
                    dec_btn.setEnabled(true);
                }
            }
        });

        addtocart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.insertCartData(foodModel.getIdFood(),loggedCustomerData.getId_customer(),qty,et_notes.getText().toString());
                Toast.makeText(getApplicationContext(),"Berhasil ditambahkan ke keranjang",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}