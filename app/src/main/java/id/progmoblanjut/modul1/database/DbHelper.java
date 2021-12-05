package id.progmoblanjut.modul1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import id.progmoblanjut.modul1.R;
import id.progmoblanjut.modul1.adapter.FoodAdapter;
import id.progmoblanjut.modul1.model.CartModel;
import id.progmoblanjut.modul1.model.FoodModel;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TB_CART="tb_cart";
    private static final String ID_CART="id_cart";
    private static final String ID_FOOD="id_food";
    private static final String CART_QTY="cart_qty";
    private static final String ID_CUSTOMER="id_customer";
    private static final String NOTES="notes";
    private static final String CREATED_AT="created_at";
    private static final String UPDATED_AT="updated_at";

    private static final String TB_CUSTOMER="tb_customer";
    private static final String CUSTOMER_NAME="customer_name";
    private static final String CUSTOMER_QTY="customer_qty";
    private static final String STATUS_MAKAN="status_makan";
    private static final String REKOMENDASI="rekomendasi";

    private static final String TB_FOOD="tb_food";
    private static final String FOOD_NAME="food_name";
    private static final String PRICE="price";
    private static final String DESCRIPTION="description";
    private static final String FOOD_FILENAME="food_filename";

    private static final String TB_FOOD_CATEGORY="tb_food_category";
    private static final String ID_FOOD_CATEGORY="id_food_category";
    private static final String CATEGORY_NAME="category_name";
    private static final String CATEGORY_DESCRIPTION="category_description";

    public DbHelper(Context context) {
        super(context,"Customerdata.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR(30),%s INTEGER,%s VARCHAR(15),%s TEXT,%s TEXT,%s TEXT)",TB_CUSTOMER,ID_CUSTOMER,CUSTOMER_NAME,CUSTOMER_QTY,STATUS_MAKAN,REKOMENDASI,CREATED_AT,UPDATED_AT));

        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)",TB_FOOD_CATEGORY,ID_FOOD_CATEGORY,CATEGORY_NAME,CATEGORY_DESCRIPTION));

        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR(30),%s DOUBLE,%s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE NO ACTION ON UPDATE NO ACTION)",TB_FOOD,ID_FOOD,FOOD_NAME,PRICE,DESCRIPTION,FOOD_FILENAME,ID_FOOD_CATEGORY,CREATED_AT,UPDATED_AT,ID_FOOD_CATEGORY,TB_FOOD_CATEGORY,ID_FOOD_CATEGORY));

        db.execSQL(String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER,%s INTEGER,%s INTEGER, %s TEXT, FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE ON UPDATE NO ACTION, FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE ON UPDATE NO ACTION)",TB_CART,ID_CART,ID_FOOD,CART_QTY,ID_CUSTOMER,NOTES,ID_FOOD,TB_FOOD,ID_FOOD,ID_CUSTOMER,TB_CUSTOMER,ID_CUSTOMER));

        makeFoodMasterData(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS customer_data");
        db.execSQL("DROP TABLE IF EXISTS food_master");
        db.execSQL("DROP TABLE IF EXISTS cart");
    }
    public long insertCustData(String nama,int jml_orang,String status_makan,String rekomendasi){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(CUSTOMER_NAME,nama);
        contentValues.put(CUSTOMER_QTY,jml_orang);
        contentValues.put(STATUS_MAKAN,status_makan);
        contentValues.put(REKOMENDASI,rekomendasi);
        long result=DB.insert(TB_CUSTOMER,null,contentValues);
        return result;
    }
    public Boolean updateCustData(String nama,int jml_orang,String status_makan,String rekomendasi){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(CUSTOMER_NAME,nama);
        contentValues.put(CUSTOMER_QTY,jml_orang);
        contentValues.put(STATUS_MAKAN,status_makan);
        contentValues.put(REKOMENDASI,rekomendasi);
        Cursor cursor=DB.rawQuery("Select * from "+TB_CUSTOMER+" where "+CUSTOMER_NAME+"=?",new String[]{nama});
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String tgl_hari_ini=dtf.format(now);
        ArrayList<FoodModel> dummyFood=new ArrayList<FoodModel>();
        dummyFood.add(new FoodModel(1,"Tipat Cantok",17000.0,"Tipat enak berisi bumbu kacang",Integer.toString(R.drawable.tipatcantok),1));
        dummyFood.add(new FoodModel(2,"Tipat Plecing",15000.0,"Tipat enak berisi bumbu pedas", Integer.toString(R.drawable.tipatcantok),1));
        dummyFood.add(new FoodModel(3,"Ayam Bakarr",20000.0,"Ayam bakar diisi bumbu kecap pedas", Integer.toString(R.drawable.tipatcantok),1));
        dummyFood.add(new FoodModel(4,"Steak Sapiderman",50000.0,"Steak sapiderman lengkap dengan jaringnya", Integer.toString(R.drawable.tipatcantok),1));
        for (int i=0;i<dummyFood.size();i++){
            ContentValues contentValues=new ContentValues();
            contentValues.put(FOOD_NAME,dummyFood.get(i).getFood_name());
            contentValues.put(PRICE,dummyFood.get(i).getRawprice());
            contentValues.put(DESCRIPTION,dummyFood.get(i).getDescription());
            contentValues.put(FOOD_FILENAME,dummyFood.get(i).getFood_filename());
            contentValues.put(ID_FOOD_CATEGORY,dummyFood.get(i).getId_food_category());
            contentValues.put(CREATED_AT,tgl_hari_ini);
            contentValues.put(UPDATED_AT,tgl_hari_ini);
            long result=db.insert(TB_FOOD,null,contentValues);
        }
    }
    public ArrayList<FoodModel> getFoodMasterData(){
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery("SELECT * FROM "+TB_FOOD, null);
        ArrayList<FoodModel> data      = new ArrayList<FoodModel>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new FoodModel(cursor.getInt(cursor.getColumnIndex(ID_FOOD)),cursor.getString(cursor.getColumnIndex(FOOD_NAME)),cursor.getDouble(cursor.getColumnIndex(PRICE)),cursor.getString(cursor.getColumnIndex(DESCRIPTION)),cursor.getString(cursor.getColumnIndex(FOOD_FILENAME)),cursor.getInt(cursor.getColumnIndex(ID_FOOD_CATEGORY))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }
    public Boolean insertCartData(int id_food,int cust_id,int cart_qty,String notes){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(ID_FOOD,id_food);
        contentValues.put(CART_QTY,cart_qty);
        contentValues.put(ID_CUSTOMER,cust_id);
        contentValues.put(NOTES,notes);
        long result=DB.insert(TB_CART,null,contentValues);
        if (result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Boolean updateCartData(int cart_id, int qty){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
//        contentValues.put("id_food",id_food);
        contentValues.put(CART_QTY,qty);
//        contentValues.put("cust_id",cust_id);
        Cursor cursor=DB.rawQuery("Select * from "+TB_CART+" where "+ID_CART+"="+ cart_id,null);
        if(cursor.getCount()>0){
            if(cursor.moveToLast()){
                //name = cursor.getString(column_index);//to get other values
                int id = cursor.getInt(0);//to get id, 0 is the column index
                long result=DB.update(TB_CART,contentValues,ID_CART+"=?",new String[]{String.valueOf(id)});
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
        long result=DB.delete(TB_CART,ID_CART+"=?",new String[]{Integer.toString(cart_id)});
        if (result==-1){
            return false;
        }else{
            return true;
        }
    }
    public ArrayList<CartModel> getCartData(int cust_id){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM "+TB_CART+" JOIN "+TB_FOOD+" ON "+TB_FOOD+"."+ID_FOOD+"="+TB_CART+"."+ID_FOOD+" JOIN "+TB_CUSTOMER+" ON "+TB_CUSTOMER+"."+ID_CUSTOMER+"="+TB_CART+"."+ID_CUSTOMER+" WHERE "+TB_CART+"."+ID_CUSTOMER+" = ?", new String[] {Integer.toString(cust_id)});
        ArrayList<CartModel> data = new ArrayList<CartModel>();
        if (cursor.moveToFirst()) {
            do{
                data.add(new CartModel(cursor.getInt(cursor.getColumnIndex(ID_CART)),new FoodModel(cursor.getInt(cursor.getColumnIndex(ID_FOOD)),cursor.getString(cursor.getColumnIndex(FOOD_NAME)),cursor.getDouble(cursor.getColumnIndex(PRICE)),cursor.getString(cursor.getColumnIndex(DESCRIPTION)),cursor.getString(cursor.getColumnIndex(FOOD_FILENAME)),cursor.getInt(cursor.getColumnIndex(ID_FOOD_CATEGORY))),(cursor.getInt(cursor.getColumnIndex(ID_CUSTOMER))),(cursor.getInt(cursor.getColumnIndex(CART_QTY))),cursor.getString(cursor.getColumnIndex(NOTES))));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return data;
    }
}
