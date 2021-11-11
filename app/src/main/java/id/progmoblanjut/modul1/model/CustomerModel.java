package id.progmoblanjut.modul1.model;

import java.io.Serializable;

public class CustomerModel implements Serializable {
    private int cust_id;
    private String nama;
    private int jml_orang;
    private String status_makan;
    private String rekomendasi;

    public CustomerModel(String nama, int jml_orang, String status_makan, String rekomendasi) {
        this.nama = nama;
        this.jml_orang = jml_orang;
        this.status_makan = status_makan;
        this.rekomendasi = rekomendasi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getJml_orang() {
        return jml_orang;
    }

    public void setJml_orang(int jml_orang) {
        this.jml_orang = jml_orang;
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

    public int getCust_id() {
        return cust_id;
    }
    public void setCust_id(int cust_id) {
        this.cust_id = cust_id;
    }
}
