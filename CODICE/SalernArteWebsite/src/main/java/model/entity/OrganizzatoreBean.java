package model.entity;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

public class OrganizzatoreBean {
    private int id,sesso,iban;
    private String nome,cognome,email, passwordHash,biografia,azienda;
    private Date dataDiNascita;
    private boolean hash;

    public OrganizzatoreBean() {
    }

    public OrganizzatoreBean(int id, int sesso, int iban, String nome, String cognome, String email, String passwordHash, String biografia, String azienda, Date dataDiNascita, boolean hash) {
        this.id = id;
        this.sesso = sesso;
        this.iban = iban; //aggiungere controlli
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        if(hash){
            this.passwordHash=passwordHash;
        }else{
            setPasswordHash(passwordHash);
            setHash(true);
        }
        this.biografia = biografia;
        this.azienda = azienda;
        this.dataDiNascita = dataDiNascita;
    }

    public OrganizzatoreBean(int sesso, int iban, String nome, String cognome, String email, String passwordHash, String biografia, String azienda, Date dataDiNascita, boolean hash) {
        this.sesso = sesso;
        this.iban = iban;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        if(hash){
            this.passwordHash=passwordHash;
            this.hash=hash;
        }else{
            setPasswordHash(passwordHash);
            setHash(true);
        }
        this.biografia = biografia;
        this.azienda = azienda;
        this.dataDiNascita = dataDiNascita;

    }

    public int getId() {
        return id;
    }

    public int getSesso() {
        return sesso;
    }

    public int getIban() {
        return iban;
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

    public String getBiografia() {
        return biografia;
    }

    public String getAzienda() {
        return azienda;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public boolean isHash() {
        return hash;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSesso(int sesso) {
        this.sesso = sesso;
    }

    public void setIban(int iban) {
        this.iban = iban;
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

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public void setAzienda(String azienda) {
        this.azienda = azienda;
    }

    public void setDataDiNascita(Date dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public void setHash(boolean hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "OrganizzatoreBean{" +
                "id=" + id +
                ", sesso=" + sesso +
                ", iban=" + iban +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", biografia='" + biografia + '\'' +
                ", azienda='" + azienda + '\'' +
                ", dataDiNascita=" + dataDiNascita +
                '}';
    }
}
