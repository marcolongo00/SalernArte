package model.entity;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AmministratoreBean {
    private int id;
    private String nome,cognome,email, passwordHash;
    private boolean hash;

    public AmministratoreBean() {
    }

    public AmministratoreBean(int id, String nome, String cognome, String email, String passwordHash,boolean hash) {
        this.id = id;
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
    }

    public AmministratoreBean(String nome, String cognome, String email, String passwordHash, boolean hash) {
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
    }

    public int getId() {
        return id;
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

    public boolean isHash() {
        return hash;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setHash(boolean hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "AmministratoreBean{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
