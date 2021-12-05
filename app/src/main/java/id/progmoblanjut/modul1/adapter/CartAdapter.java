package id.progmoblanjut.modul1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import id.progmoblanjut.modul1.EditNoteActivity;
import id.progmoblanjut.modul1.R;
import id.progmoblanjut.modul1.database.DbHelper;
import id.progmoblanjut.modul1.model.CartModel;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private Context context;
    private ArrayList<CartModel> cartData;
    private DbHelper db;
    private Activity cartActivity;
    Gson gson = new Gson();
    SharedPreferences mPrefs;
    public CartAdapter(Context context, ArrayList<CartModel> cartData,Activity activity) {
        this.context=context;
        this.cartActivity=activity;
        this.cartData = cartData;
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
        String jsonCartModel = gson.toJson(cartData.get(position));
        holder.nama.setText(cartData.get(position).getFood().getFood_name());
        holder.harga.setText(cartData.get(position).getFood().getPrice(1));
        holder.qty.setText(Integer.toString(cartData.get(position).getCart_qty()));
        holder.total.setText(cartData.get(position).getFood().getPrice(cartData.get(position).getCart_qty()));
        holder.note.setText(cartData.get(position).getNotes());
        holder.imgFood.setImageResource(Integer.parseInt(cartData.get(position).getFood().getFood_filename()));
        holder.incBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updateCartData(cartData.get(position).getId_cart(),cartData.get(position).getCart_qty()+1);
                cartData.get(position).setCart_qty(cartData.get(position).getCart_qty()+1);
                holder.qty.setText(""+cartData.get(position).getCart_qty());
                holder.total.setText(cartData.get(position).getFood().getPrice(cartData.get(position).getCart_qty()));
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
                intent.putExtra("cart_model",jsonCartModel);
                v.getContext().startActivity(intent);
            }
        });
        holder.decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartData.get(position).getCart_qty()==1){
                    if(db.deleteCartData(cartData.get(position).getId_cart())){
                        cartData.remove(position);
                        notifyItemRemoved(position);
                    };
                }else{
                    db.updateCartData(cartData.get(position).getId_cart(),cartData.get(position).getCart_qty()-1);
                    cartData.get(position).setCart_qty(cartData.get(position).getCart_qty()-1);
                    holder.qty.setText(""+cartData.get(position).getCart_qty());
                    holder.total.setText(cartData.get(position).getFood().getPrice(cartData.get(position).getCart_qty()));
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
        private ImageButton incBtn,decBtn,editBtn;
        private ImageView imgFood;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.harga = itemView.findViewById(R.id.textView7);
            this.qty = itemView.findViewById(R.id.txtFoodQty);
            this.note=itemView.findViewById(R.id.textView13);
            this.total = itemView.findViewById(R.id.textView9);
            this.nama = itemView.findViewById(R.id.textView6);
            this.editBtn=itemView.findViewById(R.id.imageButton7);
            this.incBtn=itemView.findViewById(R.id.imgBtnPlus);
            this.decBtn=itemView.findViewById(R.id.imgBtnMin);
            this.imgFood=itemView.findViewById(R.id.imageView);
        }
    }
}
