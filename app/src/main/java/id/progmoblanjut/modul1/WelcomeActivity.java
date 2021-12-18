package id.progmoblanjut.modul1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import id.progmoblanjut.modul1.database.DbHelper;
import id.progmoblanjut.modul1.database.FoodOrderingAPI;
import id.progmoblanjut.modul1.database.Result;
import id.progmoblanjut.modul1.model.CustomerModel;
import id.progmoblanjut.modul1.model.FoodModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WelcomeActivity extends AppCompatActivity {
    SharedPreferences mPrefs;
    CustomerModel loggedCustomerData;
    EditText et_namaPelanggan;
    SeekBar sb_jmlOrang;
    TextView tv_jmlOrang;
    RadioGroup rg_dineOrBungkus;
    RadioButton rb_dineOrBungkus;
    int[] cb_id;
    CheckBox cb_temp;
    String cb_temp_id_str="";
    Button btn_submit;
    int jmlOrang;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Intent intent;
    CustomerModel loggedCustData,customerFromAPI;
    DbHelper db;
    private String baseUrl="http://192.168.0.102:8000/api/";
    private FoodOrderingAPI foodOrderingAPI;
    private Call<ResponseBody> call;
    String nama,chosenDineOrBungkus,rekomendasiMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        et_namaPelanggan=(EditText)findViewById(R.id.namaPelanggan);
        sb_jmlOrang=(SeekBar) findViewById(R.id.seekBar);
        tv_jmlOrang=findViewById(R.id.jmlOrang);
        rg_dineOrBungkus=(RadioGroup)findViewById(R.id.radioGroup);
        cb_id=new int[]{R.id.cb_ayam,R.id.cb_sapi,R.id.cb_seafood,R.id.cb_veg,R.id.cb_ffood,R.id.cb_lainnya};

        btn_submit=(Button)findViewById(R.id.button2);
        Gson gson = new Gson();
        mPrefs= this.getSharedPreferences("pref",0);
        String json = mPrefs.getString("loggedCustomerData", "");
        loggedCustomerData = gson.fromJson(json, CustomerModel.class);
        if(loggedCustomerData!=null){
            intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        builder= new AlertDialog.Builder(WelcomeActivity.this);
        db=new DbHelper(this);
        sb_jmlOrang.setProgress(1);
        jmlOrang=1;
        sb_jmlOrang.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                jmlOrang=(int)Math.ceil(progress/11.0)==0 ? 1 : (int)Math.ceil(progress/11.0);
                tv_jmlOrang.setText(String.valueOf(jmlOrang) + " Orang");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int selectedId = rg_dineOrBungkus.getCheckedRadioButtonId();
                rb_dineOrBungkus=(RadioButton) findViewById(selectedId);
                nama=et_namaPelanggan.getText().toString().trim();
                chosenDineOrBungkus=rb_dineOrBungkus.getText().toString();
                rekomendasiMenu="";
                for(int i=0;i<cb_id.length;i++){
                    cb_temp=(CheckBox)findViewById(cb_id[i]);
                    if(cb_temp.isChecked()){
                        rekomendasiMenu=rekomendasiMenu + cb_temp.getText().toString() + ",";
                        cb_temp_id_str+=Integer.toString(i+1) + ",";
                    }
                }
                if(nama.isEmpty() || rekomendasiMenu.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Harap isi nama dan pilihan rekomendasi menu", Toast.LENGTH_SHORT).show();
                    return;
                }
                builder.setMessage("Nama : " + nama + "\nJumlah Orang : " + jmlOrang + " Orang \n" + chosenDineOrBungkus + "\nRekomendasi Menu :\n" + rekomendasiMenu).setTitle("Isi Form");
                dialog=builder.create();
                dialog.show();

                Retrofit retrofit= new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
                foodOrderingAPI=retrofit.create(FoodOrderingAPI.class);

                call=foodOrderingAPI.createUser(nama,jmlOrang,chosenDineOrBungkus,cb_temp_id_str);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(!response.isSuccessful()){
                            Log.d("dewa27",String.valueOf(response.code()));
                            Toast.makeText(getApplicationContext(), "Code : " + Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                            return;
                        }
                        try{
                            String json_res=response.body().string();
                            customerFromAPI=gson.fromJson(json_res,CustomerModel.class);
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH::mm::ss");
                            LocalDateTime now = LocalDateTime.now();
                            String tgl_hari_ini=dtf.format(now);
                            long resultInsert=db.insertCustData(nama,jmlOrang,chosenDineOrBungkus,cb_temp_id_str,customerFromAPI.getUuid(),tgl_hari_ini,tgl_hari_ini);

                            loggedCustData=new CustomerModel((int)resultInsert,nama,jmlOrang,chosenDineOrBungkus,cb_temp_id_str,customerFromAPI.getCreated_at(),customerFromAPI.getUpdated_at());
                            loggedCustData.setRekomendasi_id(cb_temp_id_str);
                            loggedCustData.setUuid(customerFromAPI.getUuid());
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            String json = gson.toJson(loggedCustData);
                            prefsEditor.putString("loggedCustomerData", json);
                            prefsEditor.commit();
                            intent = new Intent(WelcomeActivity.this, MainActivity.class);
                            intent.putExtra("nama", nama);
                            intent.putExtra("jml_orang", jmlOrang);
                            intent.putExtra("tipe", chosenDineOrBungkus);
                            intent.putExtra("rek_menu", rekomendasiMenu);
                            intent.putExtra("rek_menu_id_str", cb_temp_id_str);
                            startActivity(intent);
                            finish();

                        }catch(IOException ex)
                        {
                            Log.e("retrofit_error", ex.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH::mm::ss");
                        LocalDateTime now = LocalDateTime.now();
                        String tgl_hari_ini=dtf.format(now);
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        long resultInsert=db.insertCustData(nama,jmlOrang,chosenDineOrBungkus,cb_temp_id_str,null,tgl_hari_ini,tgl_hari_ini);

                        loggedCustData=new CustomerModel((int)resultInsert,nama,jmlOrang,chosenDineOrBungkus,cb_temp_id_str,tgl_hari_ini,tgl_hari_ini);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        String json = gson.toJson(loggedCustData);
                        prefsEditor.putString("loggedCustomerData", json);
                        prefsEditor.commit();
                        intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        intent.putExtra("nama", nama);
                        intent.putExtra("jml_orang", jmlOrang);
                        intent.putExtra("tipe", chosenDineOrBungkus);
                        intent.putExtra("rek_menu", rekomendasiMenu);
                        intent.putExtra("rek_menu_id_str", cb_temp_id_str);
                        startActivity(intent);
                        finish();
                    }
                });

//                if(customerFromAPI!=null){
//                    long resultInsert=db.insertCustData(nama,jmlOrang,chosenDineOrBungkus,cb_temp_id_str,customerFromAPI.getUuid());
//                    loggedCustData=new CustomerModel((int)resultInsert,nama,jmlOrang,chosenDineOrBungkus,cb_temp_id_str,customerFromAPI.getCreated_at(),customerFromAPI.getUpdated_at());
//                }else{
//                    long resultInsert=db.insertCustData(nama,jmlOrang,chosenDineOrBungkus,cb_temp_id_str,null);
//                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                    LocalDateTime now = LocalDateTime.now();
//                    String tgl_hari_ini=dtf.format(now);
//                    loggedCustData=new CustomerModel((int)resultInsert,nama,jmlOrang,chosenDineOrBungkus,cb_temp_id_str,tgl_hari_ini,tgl_hari_ini);
//                }

//                intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                intent.putExtra("nama", nama);
//                intent.putExtra("jml_orang", jmlOrang);
//                intent.putExtra("tipe", chosenDineOrBungkus);
//                intent.putExtra("rek_menu", rekomendasiMenu);
//                intent.putExtra("rek_menu_id_str", cb_temp_id_str);
//                startActivity(intent);
//                finish();

//                long resultInsert=db.insertCustData(nama,jmlOrang,chosenDineOrBungkus,cb_temp_id_str);
//                if(resultInsert!=-1){
//                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                    LocalDateTime now = LocalDateTime.now();
//                    String tgl_hari_ini=dtf.format(now);
//                    loggedCustData=new CustomerModel((int)resultInsert,nama,jmlOrang,chosenDineOrBungkus,cb_temp_id_str,tgl_hari_ini,tgl_hari_ini);
//
//                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
//                    String json = gson.toJson(loggedCustData);
//                    prefsEditor.putString("loggedCustomerData", json);
//                    prefsEditor.commit();
//                    Toast.makeText(getApplicationContext(), "Data masooook "+cb_temp_id_str, Toast.LENGTH_LONG).show();
//
//                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                    intent.putExtra("nama", nama);
//                    intent.putExtra("jml_orang", jmlOrang);
//                    intent.putExtra("tipe", chosenDineOrBungkus);
//                    intent.putExtra("rek_menu", rekomendasiMenu);
//                    intent.putExtra("rek_menu_id_str", cb_temp_id_str);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    Toast.makeText(getApplicationContext(), "Data gak masuk", Toast.LENGTH_LONG).show();
//                }
            }
        });
    }
}