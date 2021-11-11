package id.progmoblanjut.modul1.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import java.util.ArrayList;

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
        holder.food_price_ph.setText(foodData.get(position).getFood_price(1));
        holder.food_pic_ph.setImageResource(foodData.get(position).getFood_pic());
        holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.insertCartData(foodData.get(position).getFood_id(),loggedCustomerData.getCust_id());
                Toast.makeText(v.getContext(), "Data masooook", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return foodData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView food_name_ph,food_price_ph;
        ImageView food_pic_ph;
        Button add_to_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.food_name_ph = itemView.findViewById(R.id.food_name);
            this.food_price_ph = itemView.findViewById(R.id.food_price);
            this.food_pic_ph = itemView.findViewById(R.id.food_pic);
            this.add_to_cart=itemView.findViewById(R.id.button);
        }
    }
    public void clearData() {
        int size = foodData.size();
        foodData.clear();
        notifyItemRangeRemoved(0, size);
    }
}
