package id.progmoblanjut.modul1.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;

import id.progmoblanjut.modul1.AddToCartActivity;
import id.progmoblanjut.modul1.DownloadImageTask;
import id.progmoblanjut.modul1.MainActivity;
import id.progmoblanjut.modul1.WelcomeActivity;
import id.progmoblanjut.modul1.database.DbHelper;
import id.progmoblanjut.modul1.model.CustomerModel;
import id.progmoblanjut.modul1.model.FoodModel;
import id.progmoblanjut.modul1.R;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    ArrayList<FoodModel> foodData;
    DbHelper db;
    Context context;
    CustomerModel loggedCustomerData;
    SharedPreferences mPrefs;
    public FoodAdapter(Context context, ArrayList<FoodModel> foodData) {
        this.context=context;
        this.foodData = foodData;
        db=new DbHelper(context);
        mPrefs= context.getSharedPreferences("pref",0);
        String json = mPrefs.getString("loggedCustomerData", "");
        Gson gson = new Gson();
        loggedCustomerData = gson.fromJson(json, CustomerModel.class);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_food,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.food_name_ph.setText(foodData.get(position).getFood_name());
        holder.price_ph.setText(foodData.get(position).getPrice(1));
//        holder.food_filename_ph.setImageResource(Integer.parseInt(foodData.get(position).getFood_filename()));
//        holder.food_filename_ph.setImageResource(R.drawable.tipatcantok);
        new DownloadImageTask((ImageView) holder.food_filename_ph).execute(DownloadImageTask.imageUrl+foodData.get(position).getFood_filename());

        holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Gson gson = new Gson();
                String json=gson.toJson(foodData.get(position));
                Intent intent = new Intent(holder.add_to_cart.getContext(), AddToCartActivity.class);
                intent.putExtra("food_model", json);
                holder.add_to_cart.getContext().startActivity(intent);
//                db.insertCartData(foodData.get(position).getIdFood(),loggedCustomerData.getId_customer());
//                Toast.makeText(v.getContext(), "Data masooook", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return foodData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView food_name_ph,price_ph;
        ImageView food_filename_ph;
        Button add_to_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.food_name_ph = itemView.findViewById(R.id.food_name);
            this.price_ph = itemView.findViewById(R.id.price);
            this.food_filename_ph = itemView.findViewById(R.id.food_filename);
            this.add_to_cart=itemView.findViewById(R.id.button);
        }
    }
    public void clearData() {
        int size = foodData.size();
        foodData.clear();
        notifyItemRangeRemoved(0, size);
    }
}
