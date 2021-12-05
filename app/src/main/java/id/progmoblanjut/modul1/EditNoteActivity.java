package id.progmoblanjut.modul1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import id.progmoblanjut.modul1.database.DbHelper;
import id.progmoblanjut.modul1.model.CartModel;
import id.progmoblanjut.modul1.model.CustomerModel;
import id.progmoblanjut.modul1.model.FoodModel;

public class EditNoteActivity extends AppCompatActivity {
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
        tv_food_name = findViewById(R.id.txtFoodName);
        tv_food_qty = findViewById(R.id.txtFoodQty);
        tv_description = findViewById(R.id.txtFoodDesc);
        tv_food_price = findViewById(R.id.txtFoodPrice);
        et_notes = findViewById(R.id.editTextNotes);
        dec_btn = findViewById(R.id.imgBtnMin);
        inc_btn = findViewById(R.id.imgBtnPlus);
        addtocart_btn = findViewById(R.id.btnAddToCart);

        db = new DbHelper(this);
        Gson gson = new Gson();
        String jsonCartModel = getIntent().getStringExtra("cart_model");
        CartModel cartModel = gson.fromJson(jsonCartModel, CartModel.class);
        SharedPreferences mPrefs = this.getSharedPreferences("pref", 0);
        String json = mPrefs.getString("loggedCustomerData", "");
        CustomerModel loggedCustomerData = gson.fromJson(json, CustomerModel.class);

        qty = cartModel.getCart_qty();
        addtocart_btn.setText("Simpan Perubahan");
        tv_food_name.setText(cartModel.getFood().getFood_name());
        tv_description.setText(cartModel.getFood().getDescription());
        tv_food_price.setText(cartModel.getFood().getPrice(1));
        tv_food_qty.setText(Integer.toString(qty));
        et_notes.setText(cartModel.getNotes());
        dec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qty > 1) {
                    qty--;
                    tv_food_qty.setText(Integer.toString(qty));
                }
                if (qty == 1) {
                    dec_btn.setEnabled(false);
                }
            }
        });
        inc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++qty;
                tv_food_qty.setText(Integer.toString(qty));
                if (qty > 1) {
                    dec_btn.setEnabled(true);
                }
            }
        });

        addtocart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Berhasil ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
