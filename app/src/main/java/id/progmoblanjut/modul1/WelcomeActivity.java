package id.progmoblanjut.modul1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import id.progmoblanjut.modul1.database.DbHelper;
import id.progmoblanjut.modul1.model.CustomerModel;

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
    Button btn_submit;
    int jmlOrang;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    DbHelper db;
    Intent intent;
    CustomerModel loggedCustData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        et_namaPelanggan=(EditText)findViewById(R.id.namaPelanggan);
        sb_jmlOrang=(SeekBar) findViewById(R.id.seekBar);
        tv_jmlOrang=findViewById(R.id.jmlOrang);
        rg_dineOrBungkus=(RadioGroup)findViewById(R.id.radioGroup);
        cb_id=new int[]{R.id.cb_ayam,R.id.cb_sapi,R.id.cb_seafood,R.id.cb_veg,R.id.cb_ffood,R.id.cb_lainnya};
//        cb_ayam=findViewById(R.id.cb_ayam);
//        cb_sapi=findViewById(R.id.cb_sapi);
//        cb_seafood=findViewById(R.id.cb_seafood);
//        cb_fastFood=findViewById(R.id.cb_seafood);
//        cb_lainnya=findViewById(R.id.cb_lainnya);
        btn_submit=(Button)findViewById(R.id.button2);
        Gson gson = new Gson();
        mPrefs= this.getSharedPreferences("pref",0);
        String json = mPrefs.getString("loggedCustomerData", "");
        loggedCustomerData = gson.fromJson(json, CustomerModel.class);
        if(loggedCustomerData!=null){
            intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                    intent.putExtra("nama", nama);
            startActivity(intent);
        }
        builder= new AlertDialog.Builder(WelcomeActivity.this);
        db=new DbHelper(this);
        sb_jmlOrang.setProgress(1);
        jmlOrang=1;
        sb_jmlOrang.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                jmlOrang=(int)Math.ceil(progress/11.0)==0 ? 1 : (int)Math.ceil(progress/11.0);
//                if(progress/10==0){
//                    jmlOrang=String.valueOf((progress/10)+1);
//                }else{
//                    jmlOrang=String.valueOf(progress/10);
//                }
                tv_jmlOrang.setText(String.valueOf(jmlOrang) + " Orang");
//                Toast.makeText(getApplicationContext(), String.valueOf(progress), Toast.LENGTH_LONG).show();
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
                String nama=et_namaPelanggan.getText().toString().trim();
                String chosenDineOrBungkus=rb_dineOrBungkus.getText().toString();
                String rekomendasiMenu="";
                for(int i=0;i<cb_id.length;i++){
                    cb_temp=(CheckBox)findViewById(cb_id[i]);
                    if(cb_temp.isChecked()){
//                        rekomendasiMenu=rekomendasiMenu + cb_temp.getText().toString() + "\n";
                        rekomendasiMenu=rekomendasiMenu + cb_temp.getText().toString() + ",";
                    }
                }
                if(nama.isEmpty() || rekomendasiMenu.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Harap isi nama dan pilihan rekomendasi menu", Toast.LENGTH_SHORT).show();
                    return;
                }
                builder.setMessage("Nama : " + nama + "\nJumlah Orang : " + jmlOrang + " Orang \n" + chosenDineOrBungkus + "\nRekomendasi Menu :\n" + rekomendasiMenu).setTitle("Isi Form");
                dialog=builder.create();
                dialog.show();
//                Toast.makeText(getApplicationContext(), et_namaPelanggan.getText(), Toast.LENGTH_LONG).show();
                long resultInsert=db.insertCustData(nama,jmlOrang,chosenDineOrBungkus,rekomendasiMenu);
                if(resultInsert!=-1){
                    loggedCustData=new CustomerModel(nama,jmlOrang,chosenDineOrBungkus,rekomendasiMenu);
                    loggedCustData.setCust_id((int)resultInsert);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(loggedCustData);
                    prefsEditor.putString("loggedCustomerData", json);
                    prefsEditor.commit();
                    Toast.makeText(getApplicationContext(), "Data masooook", Toast.LENGTH_LONG).show();
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    intent.putExtra("nama", nama);
                    intent.putExtra("jml_orang", jmlOrang);
                    intent.putExtra("tipe", chosenDineOrBungkus);
                    intent.putExtra("rek_menu", rekomendasiMenu);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Data gak masuk", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}