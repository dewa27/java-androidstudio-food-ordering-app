package id.progmoblanjut.modul1.model;

import java.io.Serializable;

public class CustomerModel implements Serializable {
    private int id_customer;
    private String customer_name;
    private int customer_qty;
    private String status_makan;
    private String rekomendasi;
    private String rekomendasi_id;
    private String created_at;
    private String updated_at;
    private String uuid;

    public CustomerModel(int id_customer, String customer_name, int customer_qty, String status_makan, String rekomendasi, String created_at, String updated_at) {
        this.id_customer = id_customer;
        this.customer_name = customer_name;
        this.customer_qty = customer_qty;
        this.status_makan = status_makan;
        this.rekomendasi = rekomendasi;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getCustomer_qty() {
        return customer_qty;
    }

    public void setCustomer_qty(int customer_qty) {
        this.customer_qty = customer_qty;
    }

    public String getStatus_makan() {
        return status_makan;
    }

    public void setStatus_makan(String status_makan) {
        this.status_makan = status_makan;
    }

    public String getRekomendasi() {
        return rekomendasi;
    }

    public void setRekomendasi(String rekomendasi) {
        this.rekomendasi = rekomendasi;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRekomendasi_id() {
        return rekomendasi_id;
    }

    public void setRekomendasi_id(String rekomendasi_id) {
        this.rekomendasi_id = rekomendasi_id;
    }
}
