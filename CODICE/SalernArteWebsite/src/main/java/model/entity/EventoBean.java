package model.entity;

import java.sql.Date;

public class EventoBean {
    private int id,idOrganizzatore;
    private Date dataInizio;
    private Date dataFine;
    private String nome,path,descrizione,indirizzo,sede;
    private int numBiglietti;
    private boolean tipo; //true=teatro false=mostra
    private  boolean attivo=false;

    public EventoBean() {
    }

    public EventoBean(int idOrganizzatore, Date dataInizio, Date dataFine, String nome, String path, String descrizione, String indirizzo, String sede, int numBiglietti, boolean tipo) {
        this.idOrganizzatore = idOrganizzatore;
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

    public EventoBean(int id, int idOrganizzatore, Date dataInizio, Date dataFine, String nome, String path, String descrizione, String indirizzo, String sede, int numBiglietti, boolean tipo) {
        this.id = id;
        this.idOrganizzatore = idOrganizzatore;
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

    public EventoBean(int id, int idOrganizzatore, Date dataInizio, Date dataFine, String nome, String path, String descrizione, String indirizzo, String sede, int numBiglietti, boolean tipo, boolean attivo) {
        this.id = id;
        this.idOrganizzatore = idOrganizzatore;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.nome = nome;
        this.path = path;
        this.descrizione = descrizione;
        this.indirizzo = indirizzo;
        this.sede = sede;
        this.numBiglietti = numBiglietti;
        this.tipo = tipo;
        this.attivo = attivo;
    }

    public int getId() {
        return id;
    }

    public int getIdOrganizzatore() {
        return idOrganizzatore;
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

    public boolean isAttivo() {
        return attivo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdOrganizzatore(int idOrganizzatore) {
        this.idOrganizzatore = idOrganizzatore;
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

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    @Override
    public String toString() {
        return "EventoBean{" +
                "id=" + id +
                ", idOrganizzatore=" + idOrganizzatore +
                ", dataInizio=" + dataInizio +
                ", dataFine=" + dataFine +
                ", nome='" + nome + '\'' +
                ", path='" + path + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", sede='" + sede + '\'' +
                ", numBiglietti=" + numBiglietti +
                ", tipo=" + tipo +
                ", attivo=" + attivo +
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
