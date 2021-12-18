package id.progmoblanjut.modul1.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class PaymentModel {
    private int id_payment;
    private String code;
    private Double total;
    private int id_customer;
    private String payment_status;

    public PaymentModel() {
    }

    public PaymentModel(int id_payment, String code, Double total, int id_customer, String payment_status) {
        this.id_payment = id_payment;
        this.code = code;
        this.total = total;
        this.id_customer = id_customer;
        this.payment_status = payment_status;
    }

    public String getRpTotal(){
        String rupiah="";
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        rupiah = kursIndonesia.format(this.total);
        return rupiah;
    }
    public int getId_payment() {
        return id_payment;
    }

    public void setId_payment(int id_payment) {
        this.id_payment = id_payment;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
}
