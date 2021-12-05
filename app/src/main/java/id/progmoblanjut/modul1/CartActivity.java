package id.progmoblanjut.modul1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.util.ArrayList;

import id.progmoblanjut.modul1.adapter.CartAdapter;
import id.progmoblanjut.modul1.adapter.FoodAdapter;
import id.progmoblanjut.modul1.database.DbHelper;
import id.progmoblanjut.modul1.model.CartModel;
import id.progmoblanjut.modul1.model.CustomerModel;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter cartAdapter;
    private RecyclerView cartRecyclerView;
    private BottomNavigationView btnNavView;
    private DbHelper db;
    private ArrayList<CartModel> cartData;
    private LinearLayout linearLayout;
    private TextView cartEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        btnNavView=findViewById(R.id.btnNavItem);
        btnNavView.setSelectedItemId(R.id.fnb);
        cartRecyclerView=findViewById(R.id.recyclerView3);
        linearLayout=findViewById(R.id.linearCart);
        cartEmpty=findViewById(R.id.textView12);
        db=new DbHelper(this);

        Gson gson=new Gson();
        SharedPreferences mPrefs= this.getSharedPreferences("pref",0);
        String json = mPrefs.getString("loggedCustomerData", "");
        CustomerModel loggedCustomerData = gson.fromJson(json, CustomerModel.class);

        cartData=db.getCartData(loggedCustomerData.getId_customer());
        btnNavView.setSelectedItemId(R.id.cart);
        btnNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch (item.getItemId()){
                    case R.id.fnb:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cart:
                        return true;
                    case R.id.your_food:
                        startActivity(new Intent(getApplicationContext(),YourFoodActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        if(cartData.isEmpty()){
            Toast.makeText(getApplicationContext(), "Data koosng", Toast.LENGTH_SHORT).show();
            cartEmpty.setVisibility(View.VISIBLE);
        }else{
//            Toast.makeText(getApplicationContext(), "Data berisi", Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(), cartData.get(0).toString(),Toast.LENGTH_SHORT).show();
            cartEmpty.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), Integer.toString(loggedCustomerData.getId_customer()),Toast.LENGTH_SHORT).show();
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
            cartAdapter= new CartAdapter(this,cartData,this);
            cartRecyclerView.setLayoutManager(mLayoutManager);
            cartRecyclerView.setAdapter(cartAdapter);
        }
    }

    public void showCartTextStatus(){
        if(cartData.isEmpty()){
            cartEmpty.setVisibility(View.VISIBLE);
        }else{
            cartEmpty.setVisibility(View.GONE);
        }
    }
}
