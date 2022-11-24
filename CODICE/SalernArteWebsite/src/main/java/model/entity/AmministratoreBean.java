package model.entity;

public class AmministratoreBean extends UtenteRegistratoBean{
    private String nome,cognome;
    public AmministratoreBean() {
    }

    public AmministratoreBean(int id, String nome, String cognome, String email, String passwordHash,boolean hash) {
        super(id,email,passwordHash,"amministratore",hash);
        this.nome = nome;
        this.cognome = cognome;
    }
    public AmministratoreBean(String nome, String cognome, String email, String passwordHash, boolean hash) {
        super(email,passwordHash,"amministratore",hash);
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                '}';
    }
}
