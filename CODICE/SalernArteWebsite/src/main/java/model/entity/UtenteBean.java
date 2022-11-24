package model.entity;

import java.sql.Date;

public class UtenteBean extends UtenteRegistratoBean{
    private int sesso;
    private String nome,cognome;
    private Date dataDiNascita;

    public UtenteBean() {
    }

    public UtenteBean(int sesso, String nome, String cognome, String email, String passwordHash, Date dataDiNascita,boolean hash) {
        super(email,passwordHash,"utente",hash);
        this.sesso = sesso;
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
    }

    public UtenteBean(int id, int sesso, String nome, String cognome, String email, String passwordHash, Date dataDiNascita, boolean hash) {
        super(id,email,passwordHash,"utente",hash);
        this.sesso = sesso;
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
    }


    public int getSesso() {
        return sesso;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public void setSesso(int sesso) {
        //controllo sul range
        this.sesso = sesso;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
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
                ", dataDiNascita=" + dataDiNascita +
                '}';
    }
}