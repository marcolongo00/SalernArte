package model.entity;

public class BigliettoBean {
    private int idBiglietto,idEvento,fattura;
    private double prezzo;

    public BigliettoBean() {
    }

    public BigliettoBean(int idBiglietto, int idEvento, int fattura, double prezzo) {
        this.idBiglietto = idBiglietto;
        this.idEvento = idEvento;
        this.fattura = fattura;
        this.prezzo = prezzo;
    }

    public int getIdBiglietto() {
        return idBiglietto;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public int getFattura() {
        return fattura;
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

    public void setFattura(int fattura) {
        this.fattura = fattura;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    @Override
    public String toString() {
        return "BigliettoBean{" +
                "idBiglietto=" + idBiglietto +
                ", idEvento=" + idEvento +
                ", fattura=" + fattura +
                ", prezzo=" + prezzo +
                '}';
    }
}
