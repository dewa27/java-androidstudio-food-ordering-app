package id.progmoblanjut.modul1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.util.ArrayList;

import id.progmoblanjut.modul1.adapter.FoodAdapter;
import id.progmoblanjut.modul1.database.DbHelper;
import id.progmoblanjut.modul1.model.CustomerModel;
import id.progmoblanjut.modul1.model.FoodModel;

public class MainActivity extends AppCompatActivity {
//    private RecyclerView.Adapter forYouAdapter,foodAdapter;
    private FoodAdapter foodAdapter,forYouAdapter;
    private RecyclerView forYouRecyclerView,foodRecyclerView;
    private ImageButton imgBtnFish,imgBtnChicken,imgBtnCow,imgBtnVeg,imgBtnFf,imgBtnBev;
    private BottomNavigationView btnNavView;
    private DbHelper db;
    private TextView txtGreetCust,txtStatusMakan,txtJmlOrang;
    ArrayList<FoodModel> foodDummyData= new ArrayList<FoodModel>();
    ArrayList<FoodModel> forYouDummyData= new ArrayList<FoodModel>();
    SharedPreferences mPrefs;
    CustomerModel loggedCustomerData;
//    SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String text=getIntent().getExtras().getString("nama");
        txtGreetCust=findViewById(R.id.txtGreetCust);
        txtStatusMakan=findViewById(R.id.txtStatusMakan);
        txtJmlOrang=findViewById(R.id.txtJmlOrang);
//        foodDummyData.add(new FoodModel("Tipat Cantok",10000.0,"Tipat enak bumbu kacang",R.drawable.tipatcantok));
//        foodDummyData.add(new FoodModel("Tipat Cantok",10000.0,"Tipat enak bumbu kacang",R.drawable.tipatcantok));
//        foodDummyData.add(new FoodModel("Tipat Cantok",10000.0,"Tipat enak bumbu kacang",R.drawable.tipatcantok));
//        foodDummyData.add(new FoodModel("Tipat Cantok",10000.0,"Tipat enak bumbu kacang",R.drawable.tipatcantok));
//        foodDummyData.add(new FoodModel("Tipat Cantok",10000.0,"Tipat enak bumbu kacang",R.drawable.tipatcantok));
//        foodDummyData.add(new FoodModel("Tipat Cantok",10000.0,"Tipat enak bumbu kacang",R.drawable.tipatcantok));
        forYouDummyData.add(new FoodModel(1,"Tipat Kuah",12000.0,"Tipat enak bumbu kuah",R.drawable.tipatcantok));
        forYouDummyData.add(new FoodModel(2,"Tipat Kuah",12000.0,"Tipat enak bumbu kuah",R.drawable.tipatcantok));
        foodRecyclerView=findViewById(R.id.recyclerView1);
        forYouRecyclerView=findViewById(R.id.recyclerView2);
        foodRecyclerView.setNestedScrollingEnabled(false);
        imgBtnFish=findViewById(R.id.imageButton);
        imgBtnChicken=findViewById(R.id.imageButton2);
        imgBtnCow=findViewById(R.id.imageButton3);
        imgBtnVeg=findViewById(R.id.imageButton4);
        imgBtnFf=findViewById(R.id.imageButton5);
        imgBtnBev=findViewById(R.id.imageButton6);
        btnNavView=findViewById(R.id.btnNavItem);
//        btnNavView.setSelectedItemId(R.id.fnb);
        imgBtnFish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    //Handle selected state change
                } else {
                    //Handle de-select state change
                }
            }
        });
        imgBtnChicken.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    //Handle selected state change
                } else {
                    //Handle de-select state change
                }
            }
        });
        imgBtnCow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    //Handle selected state change
                } else {
                    //Handle de-select state change
                }
            }
        });
        imgBtnVeg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    //Handle selected state change
                } else {
                    //Handle de-select state change
                }
            }
        });
        imgBtnFf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    //Handle selected state change
                } else {
                    //Handle de-select state change
                }
            }
        });
        imgBtnBev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    Toast.makeText(getApplicationContext(), String.valueOf(loggedCustomerData.getCust_id()), Toast.LENGTH_LONG).show();
                    //Handle selected state change
                } else {
                    //Handle de-select state change
                }
            }
        });
        btnNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.fnb:
                        return true;
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(),CartActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.your_food:
                        startActivity(new Intent(getApplicationContext(),YourFoodActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        db=new DbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Gson gson = new Gson();
        mPrefs= this.getSharedPreferences("pref",0);
        String json = mPrefs.getString("loggedCustomerData", "");
        loggedCustomerData = gson.fromJson(json, CustomerModel.class);
        txtGreetCust.setText("Hai " +  loggedCustomerData.getNama() +"!");
        txtJmlOrang.setText("Jumlah Orang :  " +  loggedCustomerData.getJml_orang());
        txtStatusMakan.setText(loggedCustomerData.getStatus_makan());
        foodDummyData=db.getFoodMasterData();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView.LayoutManager nLayoutManager = new GridLayoutManager(this, 2);
        foodRecyclerView.setLayoutManager(mLayoutManager);
        forYouRecyclerView.setLayoutManager(nLayoutManager);
        foodAdapter= new FoodAdapter(this,foodDummyData);
        forYouAdapter= new FoodAdapter(this,forYouDummyData);
        foodRecyclerView.setAdapter(foodAdapter);
        forYouRecyclerView.setAdapter(forYouAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnNavView.setSelectedItemId(R.id.fnb);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Toast.makeText(getApplicationContext(), "Selamat datang kembali " + String.valueOf(loggedCustomerData.getNama()) + "!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        forYouAdapter.clearData();
//        foodAdapter.clearData();
//        mPrefs.edit().clear().commit();
    }

    @Override
    protected void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(getApplicationContext(), "Sampai jumpa " + String.valueOf(loggedCustomerData.getNama()) + "!", Toast.LENGTH_LONG).show();
    }
}