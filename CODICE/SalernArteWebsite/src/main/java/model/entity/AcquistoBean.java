package model.entity;

import java.sql.Date;

public class AcquistoBean {

    private int numOrdine,idUtente;
    private Date  data;
    private double totale;
    private String prodotti; //string e non list eprchè se i pordotti vengono rimossi dal sito, la lista di prodotti nell'acquisto rimane

    public AcquistoBean() {
    }

    public AcquistoBean(int numOrdine, int idUtente, Date data, double totale, String prodotti) {
        this.numOrdine = numOrdine;
        this.idUtente = idUtente;
        this.data = data;
        this.totale = totale;
        this.prodotti = prodotti;
    }

    public AcquistoBean(int idUtente, Date data, double totale, String prodotti) {
        this.idUtente = idUtente;
        this.data = data;
        this.totale = totale;
        this.prodotti = prodotti;
    }

    public int getNumOrdine() {
        return numOrdine;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public Date getData() {
        return data;
    }

    public double getTotale() {
        return totale;
    }

    public String getProdotti() {
        return prodotti;
    }

    public void setNumOrdine(int numOrdine) {
        this.numOrdine = numOrdine;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

    public void setProdotti(String prodotti) {
        this.prodotti = prodotti;
    }

    @Override
    public String toString() {
        return "FatturaBean{" +
                "numOrdine=" + numOrdine +
                ", idUtente=" + idUtente +
                ", data=" + data +
                ", totale=" + totale +
                ", prodotti='" + prodotti + '\'' +
                '}';
    }
}
