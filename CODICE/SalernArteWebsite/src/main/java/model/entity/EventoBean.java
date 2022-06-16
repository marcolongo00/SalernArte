package model.entity;

import java.sql.Date;

public class EventoBean {
    private int id;
    private Date dataInizio;
    private Date dataFine;
    private String nome,path,descrizione,indirizzo,sede;
    private int numBiglietti;
    private boolean tipo; //true=teatro false=mostra

    public EventoBean() {
    }

    public EventoBean(int id, Date dataInizio, Date dataFine, String nome, String path, String descrizione, String indirizzo, String sede, int numBiglietti, boolean tipo) {
        this.id = id;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.nome = nome;
        this.path = path;
        this.descrizione = descrizione;
        this.indirizzo = indirizzo;
        this.sede = sede;
        this.numBiglietti = numBiglietti;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public String getNome() {
        return nome;
    }

    public String getPath() {
        return path;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getSede() {
        return sede;
    }

    public int getNumBiglietti() {
        return numBiglietti;
    }

    public boolean isTipo() {
        return tipo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public void setNumBiglietti(int numBiglietti) {
        this.numBiglietti = numBiglietti;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "EventoBean{" +
                "id=" + id +
                ", dataInizio=" + dataInizio +
                ", dataFine=" + dataFine +
                ", nome='" + nome + '\'' +
                ", path='" + path + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", sede='" + sede + '\'' +
                ", numBiglietti=" + numBiglietti +
                ", tipo=" + tipo +
                '}';
    }

    /* metodi epr salvare la foto nel sistema, da inseire nel serviceImpl come privata
    public static void saveImage(InputStream in, String path){
        File file=new File(path);
        try {
            Files.copy(in,file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteImage(String path){
        File file=new File(path);
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}
