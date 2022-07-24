package model.entity;

import java.util.Collection;
import java.util.LinkedHashMap;

public class CarrelloBean { //un attimo n pausa, rivedere bean, dao e daoimpl quando si sarà sviluppata la parte servlet
    public static class BigliettoQuantita {
        private EventoBean evento;
        private int quantita;
        private double prezzoBigl;

        public BigliettoQuantita() {
        }

        public BigliettoQuantita(EventoBean evento, int quantita, double prezzoBigl) {
            this.evento = evento;
            this.quantita = quantita;
            this.prezzoBigl = prezzoBigl;
        }

        public int getQuantita() {
            return quantita;
        }

        public void setQuantita(int quantita) {
            this.quantita = quantita;
        }

        public double getPrezzoBigl() {return prezzoBigl;  }

        public void setPrezzoBigl(double prezzoBigl) { this.prezzoBigl = prezzoBigl; }

        public EventoBean getProdotto() {
            return evento;
        }

        public void setProdotto(EventoBean evento){ this.evento=evento;   }

        public double getPrezzoTot(){ return  this.prezzoBigl*this.quantita; }
    }

    private LinkedHashMap<Integer, BigliettoQuantita> prodotti = new LinkedHashMap<>();
    private int idUtente;

    public CarrelloBean() {
    }

    public CarrelloBean(int idUtente) {
        this.idUtente = idUtente;
    }

    public int getIdUtente() { return idUtente; }

    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public Collection<BigliettoQuantita> getProdotti() {
        return prodotti.values();
    }

    public BigliettoQuantita get(int prodId) {
        return prodotti.get(prodId);
    }

    public void put(EventoBean evento, int quantita, double prezzoBigl) {
        prodotti.put(evento.getId(), new BigliettoQuantita(evento, quantita,prezzoBigl));
    }
    public boolean contains(BigliettoQuantita x){
        return prodotti.containsKey(x.getProdotto().getId());
    }

    public BigliettoQuantita remove(int prodId) {
        return prodotti.remove(prodId);
    }

    public double getPrezzoTot(){ return prodotti.values().stream().mapToDouble(p -> p.getPrezzoTot()).sum(); }


}
