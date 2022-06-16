package model.entity;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

public class UtenteRegistratoBean {
    private int id,sesso;
    private String nome,cognome,email, passwordHash;
    private Date dataDiNascita;

    public UtenteRegistratoBean() {
    }

    public UtenteRegistratoBean(int id, int sesso, String nome, String cognome, String email, String passwordHash, Date dataDiNascita) {
        this.id = id;
        this.sesso = sesso; //controllo sul range
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        setPasswordHash(passwordHash);
        this.dataDiNascita = dataDiNascita;
    }

    public int getId() {
        return id;
    }

    public int getSesso() {
        //
        return sesso;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String password) {
        //password è inserita dall'utente
        try{
            MessageDigest digest=MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            this.passwordHash=String.format("%040x",new BigInteger(1,digest.digest()));
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    public void setDataDiNascita(Date dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    @Override
    public String toString() {
        return "UtenteRegistratoBean{" +
                "id=" + id +
                ", sesso=" + sesso +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", dataDiNascita=" + dataDiNascita +
                '}';
    }
}
