package id.progmoblanjut.modul1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import id.progmoblanjut.modul1.CartActivity;
import id.progmoblanjut.modul1.R;
import id.progmoblanjut.modul1.database.DbHelper;
import id.progmoblanjut.modul1.model.CartModel;
import id.progmoblanjut.modul1.model.CustomerModel;
import id.progmoblanjut.modul1.model.FoodModel;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private Context context;
    private ArrayList<CartModel> cartData;
    private DbHelper db;
    private Activity cartActivity;
    SharedPreferences mPrefs;
    public CartAdapter(Context context, ArrayList<CartModel> cartData,Activity activity) {
        this.context=context;
        this.cartActivity=activity;
        this.cartData = cartData;
//        this.cartActivity= CartActivity;
        db=new DbHelper(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_food_cart,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        holder.nama.setText(cartData.get(position).getFood().getFood_name());

        holder.harga.setText(cartData.get(position).getFood().getFood_price(1));
        holder.qty.setText(Integer.toString(cartData.get(position).getQty()));
        holder.total.setText(cartData.get(position).getFood().getFood_price(cartData.get(position).getQty()));
        holder.imgFood.setImageResource(cartData.get(position).getFood().getFood_pic());
        holder.incBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updateCartData(cartData.get(position).getCart_id(),cartData.get(position).getQty()+1);
                cartData.get(position).setQty(cartData.get(position).getQty()+1);
                holder.qty.setText(""+cartData.get(position).getQty());
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartData.get(position).getQty()==1){
                    if(db.deleteCartData(cartData.get(position).getCart_id())){
                        cartData.remove(position);
                        notifyItemRemoved(position);
                    };
                }else{
                    db.updateCartData(cartData.get(position).getCart_id(),cartData.get(position).getQty()-1);
                    cartData.get(position).setQty(cartData.get(position).getQty()-1);
                    holder.qty.setText(""+cartData.get(position).getQty());
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return cartData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView harga,qty,note,nama,total;
        private Button editBtn;
        private ImageButton incBtn,decBtn;
        private ImageView imgFood;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.harga = itemView.findViewById(R.id.textView7);
            this.qty = itemView.findViewById(R.id.textView11);
            this.note=itemView.findViewById(R.id.textView7);
            this.total = itemView.findViewById(R.id.textView9);
            this.nama = itemView.findViewById(R.id.textView6);
            this.editBtn=itemView.findViewById(R.id.button4);
            this.incBtn=itemView.findViewById(R.id.imageButton9);
            this.decBtn=itemView.findViewById(R.id.imageButton8);
            this.imgFood=itemView.findViewById(R.id.imageView);
        }
    }
}
