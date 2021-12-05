package id.progmoblanjut.modul1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import id.progmoblanjut.modul1.adapter.FoodAdapter;
import id.progmoblanjut.modul1.database.DbHelper;
import id.progmoblanjut.modul1.model.CustomerModel;
import id.progmoblanjut.modul1.model.FoodModel;
import id.progmoblanjut.modul1.model.PostModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
//    private RecyclerView.Adapter forYouAdapter,foodAdapter;
    private FoodAdapter foodAdapter,forYouAdapter;
    private RecyclerView forYouRecyclerView,foodRecyclerView;
    private ImageButton imgBtnFish,imgBtnChicken,imgBtnCow,imgBtnVeg,imgBtnFf,imgBtnBev;
    private BottomNavigationView btnNavView;
    private DbHelper db;
    private TextView txtGreetCust,txtStatusMakan,txtJmlOrang;
    private EditText searchText;
    private FoodOrderingAPI foodOrderingAPI;
    private ArrayList<String> category_arr = new ArrayList<>();
    private String keyword="";
    private ProgressBar loadingProgressBar;
    private Call<ArrayList<FoodModel>> call;
    private Boolean loadAPIStatus;
    ArrayList<FoodModel> foodDummyData= new ArrayList<FoodModel>();
    ArrayList<FoodModel> forYouDummyData= new ArrayList<FoodModel>();
    ArrayList<PostModel> postData=new ArrayList<PostModel>();
    SharedPreferences mPrefs;
    CustomerModel loggedCustomerData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String text=getIntent().getExtras().getString("nama");
        txtGreetCust=findViewById(R.id.txtGreetCust);
        txtStatusMakan=findViewById(R.id.txtStatusMakan);
        txtJmlOrang=findViewById(R.id.txtJmlOrang);
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
        searchText=findViewById(R.id.searchText);
        loadingProgressBar=findViewById(R.id.progressBar4);

        Retrofit retrofit= new Retrofit.Builder().baseUrl("http://192.168.0.102:8000/api/").addConverterFactory(GsonConverterFactory.create()).build();
        foodOrderingAPI=retrofit.create(FoodOrderingAPI.class);
        Gson gson = new Gson();
        mPrefs= this.getSharedPreferences("pref",0);
        String json = mPrefs.getString("loggedCustomerData", "");
        loggedCustomerData = gson.fromJson(json, CustomerModel.class);
        txtGreetCust.setText("Hello " +  loggedCustomerData.getCustomer_name() +"!");
        txtJmlOrang.setText(Integer.toString(loggedCustomerData.getCustomer_qty()));
        txtStatusMakan.setText(loggedCustomerData.getStatus_makan());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView.LayoutManager nLayoutManager = new GridLayoutManager(this, 2);
        foodRecyclerView.setLayoutManager(mLayoutManager);
        forYouRecyclerView.setLayoutManager(nLayoutManager);

        imgBtnFish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    //Handle selected state change
                    category_arr.add("4");
                } else {
                    //Handle de-select state change
                    category_arr.remove(category_arr.indexOf("4"));
                }
                getFood();
            }
        });
        imgBtnChicken.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    //Handle selected state change
                    category_arr.add("1");
                } else {
                    //Handle de-select state change
                    category_arr.remove(category_arr.indexOf("1"));
                }
                getFood();
            }
        });
        imgBtnCow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    //Handle selected state change
                    category_arr.add("2");
                } else {
                    //Handle de-select state change
                    category_arr.remove(category_arr.indexOf("2"));
                }
                getFood();
            }
        });
        imgBtnVeg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    //Handle selected state change
                    category_arr.add("3");
                } else {
                    //Handle de-select state change
                    category_arr.remove(category_arr.indexOf("3"));
                }
                getFood();
            }
        });
        imgBtnFf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    //Handle selected state change
                    category_arr.add("6");
                } else {
                    //Handle de-select state change
                    category_arr.remove(category_arr.indexOf("6"));
                }
                getFood();
            }
        });
        imgBtnBev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    //Handle selected state change
                    category_arr.add("5");
                } else {
                    //Handle de-select state change
                    category_arr.remove(category_arr.indexOf("5"));
                }
                getFood();
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

        searchText.addTextChangedListener(new TextWatcher() {
            private Timer timer = new Timer();
            private final long DELAY = 1000;
            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                timer= new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                if (s.toString().trim().length() == 0) {
                                    keyword="";
                                }else{
                                    keyword=s.toString();
                                }
                                loadAPIStatus=true;
                                getFood();
                            }
                        }, DELAY
                );
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        db=new DbHelper(this);
//        db.makeFoodMasterData();
//        String rec_category_id_str= getIntent().getStringExtra("rek_menu_id_str");
//        Toast.makeText(getApplicationContext(), "Arr Str : " + rec_category_id_str, Toast.LENGTH_LONG).show();
        foodDummyData=db.getFoodMasterData();
        forYouDummyData=db.getFoodMasterData();
        getFood();
        getRecommendedFood(loggedCustomerData.getRekomendasi());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    protected void getFood(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!loadAPIStatus) {
                    foodRecyclerView.setVisibility(View.GONE);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        StringBuilder str = new StringBuilder("");
        // Traversing the ArrayList
        category_arr.add("1");
        for (String eachstring : category_arr) {

            // Each element in ArrayList is appended
            // followed by comma
            str.append(eachstring).append(",");
        }

        // StringBuffer to String conversion
        String commaseparatedlist = str.toString();

        // Condition check to remove the last comma
        if (commaseparatedlist.length() > 0)
            commaseparatedlist= commaseparatedlist.substring(
                    0, commaseparatedlist.length() - 1);
        if(category_arr.isEmpty() && keyword.trim().length()==0){
            call= foodOrderingAPI.getFood();
        }else if(!category_arr.isEmpty() && keyword.trim().length()!=0){
            call= foodOrderingAPI.getFoodByNameAndCategory(commaseparatedlist,keyword);
        }else if(!category_arr.isEmpty() && keyword.trim().length()==0){
            call= foodOrderingAPI.getFoodByCategory(commaseparatedlist);
        }else{
            call= foodOrderingAPI.getFoodByName(keyword);
        }
        call.enqueue(new Callback<ArrayList<FoodModel>>() {
            @Override
            public void onResponse(Call<ArrayList<FoodModel>> call, Response<ArrayList<FoodModel>> response) {
                if(!response.isSuccessful()){
                    Log.d("dewa27",String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "Code : " + Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        foodRecyclerView.setVisibility(View.VISIBLE);
                        loadingProgressBar.setVisibility(View.GONE);
                    }
                });
                foodDummyData=response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<FoodModel>> call, Throwable t) {
                Log.d("api",t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        foodRecyclerView.setVisibility(View.VISIBLE);
                        loadingProgressBar.setVisibility(View.GONE);
                    }
                });
                foodDummyData=db.getFoodMasterData();
            }
        });
        foodAdapter= new FoodAdapter(getApplicationContext(),foodDummyData);
        foodRecyclerView.setAdapter(foodAdapter);
    }
    protected void getRecommendedFood(String recommendedCategoryIdStr){
        // Condition check to remove the last comma
        if (recommendedCategoryIdStr.length() > 0)
            recommendedCategoryIdStr= recommendedCategoryIdStr.substring(
                    0, recommendedCategoryIdStr.length() - 1);
        call= foodOrderingAPI.getFoodByCategory(recommendedCategoryIdStr);
        call.enqueue(new Callback<ArrayList<FoodModel>>() {
            @Override
            public void onResponse(Call<ArrayList<FoodModel>> call, Response<ArrayList<FoodModel>> response) {
                if(!response.isSuccessful()){
//                    Log.d("dewa27",String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "Code : " + Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                    return;
                }
                forYouRecyclerView.setVisibility(View.VISIBLE);
                forYouDummyData=response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<FoodModel>> call, Throwable t) {
                Log.d("api",t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                forYouRecyclerView.setVisibility(View.VISIBLE);
                forYouDummyData=db.getFoodMasterData();
            }
        });
        forYouAdapter= new FoodAdapter(getApplicationContext(),forYouDummyData);
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

//    @Override
//    protected void onDestroy() {
////        super.onDestroy();
////        Toast.makeText(getApplicationContext(), "Sampai jumpa " + String.valueOf(loggedCustomerData.getNama()) + "!", Toast.LENGTH_LONG).show();
//    }
}