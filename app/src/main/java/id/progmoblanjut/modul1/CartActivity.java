package id.progmoblanjut.modul1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import id.progmoblanjut.modul1.adapter.CartAdapter;
import id.progmoblanjut.modul1.database.DbHelper;
import id.progmoblanjut.modul1.database.FoodOrderingAPI;
import id.progmoblanjut.modul1.model.CartModel;
import id.progmoblanjut.modul1.model.CustomerModel;
import id.progmoblanjut.modul1.model.PaymentModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter cartAdapter;
    private RecyclerView cartRecyclerView;
    private BottomNavigationView btnNavView;
    private DbHelper db;
    private ArrayList<CartModel> cartData;
    private LinearLayout linearLayout;
    private TextView cartEmpty;
    private Button checkoutBtn;
    RecyclerView.LayoutManager mLayoutManager;
    private String baseUrl="http://192.168.0.102:8000/api/";
    private FoodOrderingAPI foodOrderingAPI;
    private Call<ResponseBody> call,call2;
    private PaymentModel paymentModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        btnNavView = findViewById(R.id.btnNavItem);
        btnNavView.setSelectedItemId(R.id.cart);
        cartRecyclerView = findViewById(R.id.recyclerView3);
        linearLayout = findViewById(R.id.linearCart);
        cartEmpty = findViewById(R.id.textView12);
        checkoutBtn=findViewById(R.id.button3);
        db = new DbHelper(this);

        Gson gson = new Gson();
        SharedPreferences mPrefs = this.getSharedPreferences("pref", 0);
        String json = mPrefs.getString("loggedCustomerData", "");
        CustomerModel loggedCustomerData = gson.fromJson(json, CustomerModel.class);
        Retrofit retrofit= new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        foodOrderingAPI=retrofit.create(FoodOrderingAPI.class);
        cartData = db.getCartData(loggedCustomerData.getId_customer(),"pending");

        btnNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.fnb:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
//                        overridePendingTransition(0,0);
                        btnNavView.setSelectedItemId(R.id.cart);
                        return true;
                    case R.id.cart:
                        return true;
                    case R.id.your_food:
                        startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
//                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cartData.isEmpty()||cartData==null) {
                    String shortId = getSaltString();
                    int index = 0;
                    Double total_price = 0.0;
                    while (cartData.size() > index) {
                        total_price += (double) cartData.get(index).getFood().getRawprice() * cartData.get(index).getCart_qty();
                        index++;
                    }
                    String strCartData=gson.toJson(cartData);
                    db.insertPayment(shortId, loggedCustomerData.getId_customer(), total_price);
                    call=foodOrderingAPI.createPayment(shortId,total_price,loggedCustomerData.getUuid(),strCartData);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(!response.isSuccessful()){
                                Log.d("dewa27",String.valueOf(response.code()));
//                                Toast.makeText(getApplicationContext(), "Code : " + Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                                return;
                            }
                            try{
                                String json_res=response.body().string();
                                paymentModel=gson.fromJson(json_res, PaymentModel.class);
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH::mm::ss");
                                LocalDateTime now = LocalDateTime.now();
                                String tgl_hari_ini=dtf.format(now);
                                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                                startActivity(intent);
                                finish();
                            }catch(IOException ex)
                            {
                                Log.e("retrofit_error", ex.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        if (cartData.isEmpty() || cartData==null) {
//            Toast.makeText(getApplicationContext(), "Data koosng", Toast.LENGTH_SHORT).show();
            cartEmpty.setVisibility(View.VISIBLE);
            checkoutBtn.setVisibility(View.GONE);
        } else {
//            Toast.makeText(getApplicationContext(), "Data berisi", Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(), cartData.get(0).toString(),Toast.LENGTH_SHORT).show();
            cartEmpty.setVisibility(View.GONE);
            checkoutBtn.setVisibility(View.VISIBLE);
//            Toast.makeText(getApplicationContext(), Integer.toString(loggedCustomerData.getId_customer()), Toast.LENGTH_SHORT).show();
             mLayoutManager = new GridLayoutManager(this, 1);
            cartAdapter = new CartAdapter(this, cartData, this);
            cartRecyclerView.setLayoutManager(mLayoutManager);
            cartRecyclerView.setAdapter(cartAdapter);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        btnNavView.setSelectedItemId(R.id.cart);
        if(cartAdapter!=null){
            cartAdapter.notifyDataSetChanged();
        }
        if(cartAdapter==null && !cartData.isEmpty()){
            mLayoutManager = new GridLayoutManager(this, 1);
            cartAdapter = new CartAdapter(this, cartData, this);
            cartRecyclerView.setLayoutManager(mLayoutManager);
            cartRecyclerView.setAdapter(cartAdapter);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(0, 0);
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}