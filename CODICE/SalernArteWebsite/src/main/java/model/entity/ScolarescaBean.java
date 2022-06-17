package model.entity;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ScolarescaBean {
    private int id;
    private String email, passwordHash, istituto;
    private boolean hash;

    public ScolarescaBean() {
    }

    public ScolarescaBean(int id, String email, String passwordHash, String istituto, boolean hash) {
        this.id = id;
        this.email = email;
        if(hash){
            this.passwordHash=passwordHash;
            this.hash=hash;
        }else{
            setPasswordHash(passwordHash);
            setHash(true);
        }
        this.istituto = istituto;
    }

    public ScolarescaBean(String email, String passwordHash, String istituto, boolean hash) {
        this.email = email;
        if(hash){
            this.passwordHash=passwordHash;
            this.hash=hash;
        }else{
            setPasswordHash(passwordHash);
            setHash(true);
        }
        this.istituto = istituto;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getIstituto() {
        return istituto;
    }

    public boolean isHash() {
        return hash;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setIstituto(String istituto) {
        this.istituto = istituto;
    }

    public void setHash(boolean hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "ScolarescaBean{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", istituto='" + istituto + '\'' +
                '}';
    }
}
