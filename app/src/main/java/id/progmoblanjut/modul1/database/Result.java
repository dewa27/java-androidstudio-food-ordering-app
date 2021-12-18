package id.progmoblanjut.modul1.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id_payment")
    @Expose
    private int id_payment;
    @SerializedName("notes")
    @Expose
    private String notes;

    /**
     *
     * @return
     * The id
     */
    public Integer getId_payment() {
        return id_payment;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id_payment) {
        this.id_payment = id_payment;
    }

    /**
     *
     * @return
     * The username
     */
    public String getNotes() {
        return notes;
    }

    /**
     *
     * @param username
     * The Username
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     *
     * @return
     * The level
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param level
     * The Level
     */
    public void setLevel(String status) {
        this.status = status;
    }

}