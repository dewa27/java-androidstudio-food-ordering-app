package id.progmoblanjut.modul1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class PaymentItemAdapter extends RecyclerView.Adapter<PaymentItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CartModel> cartData;
    private DbHelper db;
    private Activity paymentActivity;
//    Gson gson = new Gson();
//    SharedPreferences mPrefs;

    public PaymentItemAdapter(Context context, ArrayList<CartModel> cartData, Activity activity) {
        this.context = context;
        this.paymentActivity = activity;
        this.cartData = cartData;
        db = new DbHelper(context);
    }

    @NonNull
    @Override
    public PaymentItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_payment_item, parent, false);
        return new PaymentItemAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentItemAdapter.ViewHolder holder, int position) {
        holder.nama.setText(cartData.get(position).getFood().getFood_name());
        holder.qty.setText(Integer.toString(cartData.get(position).getCart_qty())+" pcs");
        holder.harga.setText(cartData.get(position).getFood().getPrice(cartData.get(position).getCart_qty()));
    }

    @Override
    public int getItemCount() {
        return cartData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView harga, qty, nama;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.harga = itemView.findViewById(R.id.textView21);
            this.qty = itemView.findViewById(R.id.textView20);
            this.nama = itemView.findViewById(R.id.textView19);
        }
    }
}
