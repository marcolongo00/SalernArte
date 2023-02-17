package gestioneAcquisti.controller;

import gestioneAcquisti.service.GestioneAcquistiService;
import gestioneAcquisti.service.GestioneAcquistiServiceImpl;
import model.entity.CarrelloBean;
import model.entity.UtenteRegistratoBean;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "GestioneAcquistiController",urlPatterns = "/gestione-acquisti")
public class GestioneAcquistiController extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GestioneAcquistiService service= new GestioneAcquistiServiceImpl();
        HttpSession session = request.getSession();
        UtenteRegistratoBean utente= (UtenteRegistratoBean) session.getAttribute("selezionato");
        CarrelloBean carrello=(CarrelloBean) session.getAttribute("carrello");
        session.removeAttribute("messaggio");
        try{
        if(request.getParameter("goToCarrello")!=null){
            boolean alertCarrello=false;
            if(carrello==null && utente==null){
                carrello=new CarrelloBean();

            }else if(carrello==null && utente!=null){
                // non si può mai verificare perchè se ho fatto il login il carrello non è null ma al più vuoto
                carrello=service.retrieveCarrelloUtente(utente);
                //contorllo elementi per eventi non attivi
                alertCarrello=service.controlloEventiNonAttivi(carrello);
            }else if(carrello!=null && utente!=null){
                alertCarrello=service.controlloElementiCarrello(carrello,utente);
            }

            session.setAttribute("carrello",carrello);
            request.setAttribute("alertCarrello",alertCarrello);
            String address="WEB-INF/gestioneAcquisti/Carrello.jsp";
            callDispatcher(request,response,address);
        }
        if(request.getParameter("svuotaCarrello")!=null){
            if(utente==null){
                carrello=new CarrelloBean();
            }else{
                service.svuotaCarrello(carrello,utente);
                carrello=new CarrelloBean(utente.getId());
            }

            session.setAttribute("carrello",carrello);
            String address="WEB-INF/gestioneAcquisti/Carrello.jsp";
            callDispatcher(request,response,address);
        }
        if(request.getParameter("removeEventoFromCarrello")!=null){
            int idE= Integer.parseInt(request.getParameter("idE"));
            service.removeEventoFromCarrello(idE,carrello,utente);
            session.setAttribute("carrello", carrello);
            session.setAttribute("messaggio", "Rimozione evento dal carrello avvenuta con successo");
            String address="WEB-INF/gestioneAcquisti/Carrello.jsp";
            callDispatcher(request,response,address);
        }
        if(request.getParameter("datiCartaAcquisto")!=null){
            /** controllo su dati carta di credito non effettuato
             * per mancanza di budget
             * */
            if(utente==null || service.controlloElementiCarrello(carrello,utente)){
                throw new RuntimeException("operazione non autorizzata");
            } else if (utente.getTipoUtente().compareToIgnoreCase("utente")!=0 && utente.getTipoUtente().compareToIgnoreCase("scolaresca")!=0) {
                throw new RuntimeException("operazione non autorizzata");
            }
            //go to dettagli carta first
            String address="WEB-INF/gestioneAcquisti/DettagliPagamentoCarta.jsp";
            callDispatcher(request,response,address);
        }
        String finalizzaAcquisto=request.getParameter("finalizzaAcquisto");
        if(finalizzaAcquisto!=null){
            service.acquistaProdotti(carrello,utente);
            session.setAttribute("carrello", new CarrelloBean(utente.getId()));
            //se utente fosse null non si sarebbe potuto fare l'acquisto
            session.setAttribute("messaggio", "Acquisto avvenuto con successo");
            callDispatcher(request,response,"/index.html");
        }
        }catch (RuntimeException e){
            session.setAttribute("messaggio",e.getMessage());
            callDispatcher(request,response,"/index.html");
        }
    }
    private void callDispatcher(HttpServletRequest request, HttpServletResponse response,String address) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
    private void callReferer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String address=request.getHeader("referer"); //gli da fastidio, devi completamente separare dispatcher e referer
        if(address==null || address.contains("/gestione-acquisti") || address.trim().isEmpty()){
            address=".";
        }
        response.sendRedirect(address);
    }
}
