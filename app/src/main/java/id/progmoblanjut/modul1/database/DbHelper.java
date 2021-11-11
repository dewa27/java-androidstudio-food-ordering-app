package id.progmoblanjut.modul1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import id.progmoblanjut.modul1.R;
import id.progmoblanjut.modul1.adapter.FoodAdapter;
import id.progmoblanjut.modul1.model.CartModel;
import id.progmoblanjut.modul1.model.FoodModel;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context) {
        super(context,"Customerdata.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE customer_data(cust_id INTEGER PRIMARY KEY AUTOINCREMENT, nama VARCHAR(30),jml_orang INTEGER,status_makan VARCHAR(15),rekomendasi TEXT)");
        db.execSQL("CREATE TABLE food_master(food_id INTEGER PRIMARY KEY AUTOINCREMENT, food_nama VARCHAR(30),harga DOUBLE,deskripsi TEXT, pic INTEGER)");
        db.execSQL("CREATE TABLE cart(cart_id INTEGER PRIMARY KEY AUTOINCREMENT, food_id INTEGER,qty INTEGER,cust_id INTEGER,FOREIGN KEY (food_id) REFERENCES food_master(food_id) ON DELETE CASCADE ON UPDATE NO ACTION, FOREIGN KEY (cust_id) REFERENCES customer_data(cust_id) ON DELETE CASCADE ON UPDATE NO ACTION)");
        makeFoodMasterData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS customer_data");
        db.execSQL("DROP TABLE IF EXISTS food_master");
    }
    public long insertCustData(String nama,int jml_orang,String status_makan,String rekomendasi){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("nama",nama);
        contentValues.put("jml_orang",jml_orang);
        contentValues.put("status_makan",status_makan);
        contentValues.put("rekomendasi",rekomendasi);
        long result=DB.insert("customer_data",null,contentValues);
        return result;
    }
    public Boolean updateCustData(String nama,int jml_orang,String status_makan,String rekomendasi){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("nama",nama);
        contentValues.put("jml_orang",jml_orang);
        contentValues.put("status_makan",status_makan);
        contentValues.put("rekomendasi",rekomendasi);
        Cursor cursor=DB.rawQuery("Select * from customer_data where name=?",new String[]{nama});
        if(cursor.getCount()>0){
            if(cursor.moveToLast()){
                //name = cursor.getString(column_index);//to get other values
                int id = cursor.getInt(0);//to get id, 0 is the column index
                long result=DB.update("customer_data",contentValues,"cust_id=?",new String[]{String.valueOf(id)});
                if (result==-1){
                    return false;
                }else{
                    return true;
                }
            }
            return true;
        }else{
            return false;
        }
    }
//    public Boolean deleteCustData(String nama){
//        SQLiteDatabase DB=this.getWritableDatabase();
//        Cursor cursor=DB.rawQuery("Select * from customer_data where name=?",new String[]{nama});
//        if(cursor.getCount()>0){
//            if(cursor.moveToLast()){
//                //name = cursor.getString(column_index);//to get other values
//                int id = cursor.getInt(0);//to get id, 0 is the column index
//                long result=DB.delete("customer_data","cust_id=?",new String[]{Integer.toString(id)});
//                if (result==-1){
//                    return false;
//                }else{
//                    return true;
//                }
//            }
//            return true;
//        }else{
//            return false;
//        }
//    }
    public void makeFoodMasterData (SQLiteDatabase db){
        ArrayList<FoodModel> dummyFood=new ArrayList<FoodModel>();
        dummyFood.add(new FoodModel(1,"Tipat Cantok",17000.0,"Tipat enak berisi bumbu kacang", R.drawable.tipatcantok));
        dummyFood.add(new FoodModel(2,"Tipat Plecing",15000.0,"Tipat enak berisi bumbu pedas", R.drawable.tipatcantok));
        dummyFood.add(new FoodModel(3,"Ayam Bakarr",20000.0,"Ayam bakar diisi bumbu kecap pedas", R.drawable.tipatcantok));
        dummyFood.add(new FoodModel(4,"Steak Sapiderman",50000.0,"Steak sapiderman lengkap dengan jaringnya", R.drawable.tipatcantok));
        for (int i=0;i<dummyFood.size();i++){
            ContentValues contentValues=new ContentValues();
            contentValues.put("food_nama",dummyFood.get(i).getFood_name());
            contentValues.put("harga",dummyFood.get(i).getRawFood_price());
            contentValues.put("deskripsi",dummyFood.get(i).getFood_description());
            contentValues.put("pic",dummyFood.get(i).getFood_pic());
            long result=db.insert("food_master",null,contentValues);
        }
    }
    public ArrayList<FoodModel> getFoodMasterData(){
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery("SELECT * FROM food_master", null);
        ArrayList<FoodModel> data      = new ArrayList<FoodModel>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new FoodModel(cursor.getInt(cursor.getColumnIndex("food_id")),cursor.getString(cursor.getColumnIndex("food_nama")),cursor.getDouble(cursor.getColumnIndex("harga")),cursor.getString(cursor.getColumnIndex("deskripsi")),cursor.getInt(cursor.getColumnIndex("pic"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }
    public Boolean insertCartData(int food_id,int cust_id){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("food_id",food_id);
        contentValues.put("qty",1);
        contentValues.put("cust_id",cust_id);
        long result=DB.insert("cart",null,contentValues);
        if (result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Boolean updateCartData(int cart_id, int qty){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
//        contentValues.put("food_id",food_id);
        contentValues.put("qty",qty);
//        contentValues.put("cust_id",cust_id);
        Cursor cursor=DB.rawQuery("Select * from cart where cart_id="+ cart_id,null);
        if(cursor.getCount()>0){
            if(cursor.moveToLast()){
                //name = cursor.getString(column_index);//to get other values
                int id = cursor.getInt(0);//to get id, 0 is the column index
                long result=DB.update("cart",contentValues,"cart_id=?",new String[]{String.valueOf(id)});
                if (result==-1){
                    return false;
                }else{
                    return true;
                }
            }
            return true;
        }else{
            return false;
        }
    }
    public Boolean deleteCartData(int cart_id){
        SQLiteDatabase DB=this.getWritableDatabase();
        long result=DB.delete("cart","cart_id=?",new String[]{Integer.toString(cart_id)});
        if (result==-1){
            return false;
        }else{
            return true;
        }
    }
    public ArrayList<CartModel> getCartData(int cust_id){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM cart JOIN food_master ON food_master.food_id=cart.food_id JOIN customer_data ON customer_data.cust_id=cart.cust_id WHERE cart.cust_id = ?", new String[] {Integer.toString(cust_id)});
        ArrayList<CartModel> data = new ArrayList<CartModel>();
        if (cursor.moveToFirst()) {
            do{
                data.add(new CartModel(cursor.getInt(cursor.getColumnIndex("cart_id")),new FoodModel(cursor.getInt(cursor.getColumnIndex("food_id")),cursor.getString(cursor.getColumnIndex("food_nama")),cursor.getDouble(cursor.getColumnIndex("harga")),cursor.getString(cursor.getColumnIndex("deskripsi")),cursor.getInt(cursor.getColumnIndex("pic"))),(cursor.getInt(cursor.getColumnIndex("cust_id"))),(cursor.getInt(cursor.getColumnIndex("qty")))));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return data;
    }
}
