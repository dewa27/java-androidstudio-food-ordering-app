package id.progmoblanjut.modul1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import id.progmoblanjut.modul1.adapter.PaymentItemAdapter;
import id.progmoblanjut.modul1.database.DbHelper;
import id.progmoblanjut.modul1.database.FoodOrderingAPI;
import id.progmoblanjut.modul1.database.Result;
import id.progmoblanjut.modul1.model.CartModel;
import id.progmoblanjut.modul1.model.CustomerModel;
import id.progmoblanjut.modul1.model.PaymentModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends AppCompatActivity {
    private BottomNavigationView btnNavView;
    private DbHelper db;
    private ArrayList<CartModel> cartData;
    private TextView tv_kode_payment,tv_total_payment,tv_notif;
    private PaymentModel paymentModel;
    private CustomerModel loggedCustomerData;
    private RecyclerView paymentRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PaymentItemAdapter paymentItemAdapter;
    private Button btn_show_qr;
    private Bitmap bitmap;
    private String baseUrl="http://192.168.0.102:8000/api/";
    private ImageView qrImage;
    private int dimen;
    private Boolean isQRGenerated=false;
    private FoodOrderingAPI foodOrderingAPI;
    private Retrofit retrofit;
    private Call<Result> call;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_food);
        btnNavView=findViewById(R.id.btnNavItem);
        btnNavView.setSelectedItemId(R.id.your_food);
        btn_show_qr=findViewById(R.id.btnQrMenu);
        tv_kode_payment=findViewById(R.id.tv_kode_payment);
        tv_total_payment=findViewById(R.id.tv_total_payment);
        paymentRecyclerView=findViewById(R.id.rv_payment_item);
        tv_notif=findViewById(R.id.textView15);
        qrImage=findViewById(R.id.qrImage);
        db=new DbHelper(this);
        Gson gson = new Gson();
        SharedPreferences mPrefs = this.getSharedPreferences("pref", 0);
        String json = mPrefs.getString("loggedCustomerData", "");
        loggedCustomerData = gson.fromJson(json, CustomerModel.class);
        cartData = db.getCartData(loggedCustomerData.getId_customer(),"checkout");
        paymentModel=db.getPayment(loggedCustomerData.getId_customer());


        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;
        dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        retrofit= new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        foodOrderingAPI=retrofit.create(FoodOrderingAPI.class);
        call=foodOrderingAPI.checkPayment(paymentModel.getCode());
        gson= new Gson();

        if(cartData!=null){
            mLayoutManager = new LinearLayoutManager(this);
            paymentItemAdapter = new PaymentItemAdapter(this, cartData, this);
            paymentRecyclerView.setLayoutManager(mLayoutManager);
            paymentRecyclerView.setAdapter(paymentItemAdapter);
        }
        if(paymentModel!=null) {
            tv_kode_payment.setText(paymentModel.getCode());
            tv_total_payment.setText(paymentModel.getRpTotal());
            if(paymentModel.getPayment_status().equals("paid")){
                tv_notif.setText("Pesanan Anda sudah dibayar !");
                btn_show_qr.setVisibility(View.GONE);
            }else{
                tv_notif.setText("Tunjukkan QR Code ke Kasir untuk melakukan pembayaran!");
                btn_show_qr.setVisibility(View.VISIBLE);
            }
        }


        btn_show_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                builder.setTitle("Tampilkan QR Code");
                builder.setMessage(Html.fromHtml("<b>Pembayaran harus dilakukan maksimal 5 menit setelah QRCode diaktifkan</b><br>Apakah anda yakin ingin mengaktifkan QR COde ?"));
                builder.setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(!isQRGenerated){
                            QRGEncoder qrgEncoder = new QRGEncoder(paymentModel.getCode(), null, QRGContents.Type.TEXT, dimen);
                            qrgEncoder.setColorBlack(Color.parseColor(getResources().getString(R.color.orange_primary)));
                            try {
                                // Getting QR-Code as Bitmap
                                bitmap = qrgEncoder.getBitmap();
                                // Setting Bitmap to ImageView
                                qrImage.setImageBitmap(bitmap);
                                updatePaymentHandler();
                            } catch (Exception e) {
                                Log.v("qr error", e.toString());
                            }
                        }
                    }
                });
                builder.setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        btnNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch (item.getItemId()){
                    case R.id.fnb:
                        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
//                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cart:
                        Intent intent2= new Intent(getApplicationContext(), CartActivity.class);
                        startActivity(intent2);
                    case R.id.your_food:
//                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        cartData = db.getCartData(loggedCustomerData.getId_customer(),"pending");
        paymentModel=db.getPayment(loggedCustomerData.getId_customer());
        if(paymentModel!=null) {
            tv_kode_payment.setText(paymentModel.getCode());
            tv_total_payment.setText(paymentModel.getRpTotal());
        }
    }
    public void updatePaymentHandler(){
        Toast.makeText(getApplicationContext(), "Update payment jalan", Toast.LENGTH_SHORT).show();
        long startTime = System.currentTimeMillis(); //fetch starting time
        final Handler handler = new Handler();
        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            public void run() {
                if(System.currentTimeMillis()-startTime>300000)
                {
                    timer.cancel();
                }
                handler.post(new Runnable() {
                    public void run() {
                        call.clone().enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {
                                if(!response.isSuccessful()){
                                    Log.d("dewa27",String.valueOf(response.code()));
                                    Toast.makeText(getApplicationContext(), "Code : " + Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                try{
                                    response.body();
                                    Toast.makeText(getApplicationContext(),response.body().getStatus(),Toast.LENGTH_SHORT).show();
                                    if(response.body().getStatus().equals("success")){
                                        timer.cancel();
                                        Toast.makeText(getApplicationContext(),"Pembayaran berhasil dilakukan ",Toast.LENGTH_SHORT).show();
                                        db.updatePayment(paymentModel.getCode());
                                        finish();
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                    }
                                }catch(Exception ex)
                                {
                                    Log.e("retrofit_error", ex.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

            }
        };
        timer.schedule(task, 10000,15000);
    }
}