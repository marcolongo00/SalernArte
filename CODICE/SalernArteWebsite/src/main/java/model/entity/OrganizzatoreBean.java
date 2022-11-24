package model.entity;

import java.sql.Date;

public class OrganizzatoreBean extends UtenteRegistratoBean{
    private int sesso;
    private String nome,cognome,biografia,iban;
    private Date dataDiNascita;

    public OrganizzatoreBean() {
    }

    public OrganizzatoreBean(int id, int sesso, String iban, String nome, String cognome, String email, String passwordHash, String biografia, Date dataDiNascita, boolean hash) {
        super(id,email,passwordHash,"organizzatore",hash);
        this.sesso = sesso;
        this.iban = iban; //aggiungere controlli
        this.nome = nome;
        this.cognome = cognome;
        this.biografia = biografia;
        this.dataDiNascita = dataDiNascita;
    }

    public OrganizzatoreBean(int sesso, String iban, String nome, String cognome, String email, String passwordHash, String biografia, Date dataDiNascita, boolean hash) {
        super(email,passwordHash,"organizzatore",hash);
        this.sesso = sesso;
        this.iban = iban;
        this.nome = nome;
        this.cognome = cognome;
        this.biografia = biografia;
        this.dataDiNascita = dataDiNascita;

    }

    public int getSesso() {
        return sesso;
    }

    public String getIban() {
        return iban;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getBiografia() {
        return biografia;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public void setSesso(int sesso) {
        this.sesso = sesso;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public void setDataDiNascita(Date dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", sesso=" + sesso +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", biografia='" + biografia + '\'' +
                ", iban='" + iban + '\'' +
                ", dataDiNascita=" + dataDiNascita +
                '}';
    }
}
