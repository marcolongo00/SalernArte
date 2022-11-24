package model.entity;

import model.dao.UtenteDAOImpl;
import model.dao.UtenteRegistratoDAO;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class UtenteRegistratoBean {
    private int id;
    private String email, passwordHash, tipoUtente;
    private boolean hash;

    public UtenteRegistratoBean() {
    }

    public UtenteRegistratoBean(int id, String email, String passwordHash, String tipoUtente, boolean hash) {
        this.id = id;
        this.email = email;
        setPasswordHash(passwordHash,hash);
        this.tipoUtente = tipoUtente;
    }

    public UtenteRegistratoBean(String email, String passwordHash, String tipoUtente, boolean hash) {
        this.email = email;
        setPasswordHash(passwordHash,hash);
        this.tipoUtente = tipoUtente;
    }

    public int getId() { return id; }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isHash() {return hash; }

    public String getTipoUtente() { return tipoUtente; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String password, boolean hash) {
        if(hash){//password è già in hash
            this.passwordHash=password;
            this.hash=hash;
        }else{ //password è inserita dall'utente non in formato hash
            try{
                MessageDigest digest=MessageDigest.getInstance("SHA-1");
                digest.reset();
                digest.update(password.getBytes(StandardCharsets.UTF_8));
                setHash(true);
                this.passwordHash=String.format("%040x",new BigInteger(1,digest.digest()));
            }catch (NoSuchAlgorithmException e){
                throw new RuntimeException(e);
            }
        }

    }

    public void setHash(boolean hash) { this.hash = hash; }

    public void setTipoUtente(String tipoUtente) { this.tipoUtente = tipoUtente; }

    public void setId(int id) { this.id = id;  }

    @Override
    public String toString() {
        return "UtenteRegistratoBean{" +
                "id='" + id + '\'' +
                "email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", tipoUtente='" + tipoUtente + '\'' +
                ", hash=" + hash + '\'';
    }
}
