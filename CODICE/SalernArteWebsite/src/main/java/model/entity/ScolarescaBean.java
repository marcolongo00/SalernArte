package model.entity;

public class ScolarescaBean extends UtenteRegistratoBean{
    private String istituto;

    public ScolarescaBean() {
    }

    public ScolarescaBean(int id, String email, String passwordHash, String istituto, boolean hash) {
        super(id,email,passwordHash,"Scolaresca",hash);
        this.istituto = istituto;
    }

    public ScolarescaBean(String email, String passwordHash, String istituto, boolean hash) {
        super(email,passwordHash,"Scolaresca",hash);
        this.istituto = istituto;
    }


    public String getIstituto() {
        return istituto;
    }


    public void setIstituto(String istituto) {
        this.istituto = istituto;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", istituto='" + istituto + '\'' +
                '}';
    }
}
