package model.entity;

public class BigliettoBean {
    private int idBiglietto,idEvento,numAcquisto;
    private double prezzo;

    public BigliettoBean() {
    }

    public BigliettoBean(int idBiglietto, int idEvento, int numAcquisto, double prezzo) {
        this.idBiglietto = idBiglietto;
        this.idEvento = idEvento;
        this.numAcquisto = numAcquisto;
        this.prezzo = prezzo;
    }

    public int getIdBiglietto() {
        return idBiglietto;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public int getNumAcquisto() {
        return numAcquisto;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setIdBiglietto(int idBiglietto) {
        this.idBiglietto = idBiglietto;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public void setNumAcquisto(int numAcquisto) {
        this.numAcquisto = numAcquisto;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    @Override
    public String toString() {
        return "BigliettoBean{" +
                "idBiglietto=" + idBiglietto +
                ", idEvento=" + idEvento +
                ", fattura=" + numAcquisto +
                ", prezzo=" + prezzo +
                '}';
    }
}
