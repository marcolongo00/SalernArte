package model.entity;

import java.sql.Date;

public class FatturaBean {

    private int numOrdine,idUtente;
    private Date  data;
    private double totale;
    private String prodotti;
    private boolean tipoUtente; //true= utente registrato, false=scolaresca

    public FatturaBean() {
    }

    public FatturaBean(int numOrdine, int idUtente, Date data, double totale, String prodotti, boolean tipoUtente) {
        this.numOrdine = numOrdine;
        this.idUtente = idUtente;
        this.data = data;
        this.totale = totale;
        this.prodotti = prodotti;
        this.tipoUtente = tipoUtente;
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

    public boolean isTipoUtente() {
        return tipoUtente;
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

    public void setTipoUtente(boolean tipoUtente) {
        this.tipoUtente = tipoUtente;
    }

    @Override
    public String toString() {
        return "FatturaBean{" +
                "numOrdine=" + numOrdine +
                ", idUtente=" + idUtente +
                ", data=" + data +
                ", totale=" + totale +
                ", prodotti='" + prodotti + '\'' +
                ", tipoUtente=" + tipoUtente +
                '}';
    }
}
