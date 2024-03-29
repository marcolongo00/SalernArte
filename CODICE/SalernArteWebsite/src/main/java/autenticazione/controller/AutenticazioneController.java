package autenticazione.controller;

import autenticazione.service.AutenticazioneService;
import autenticazione.service.AutenticazioneServiceImpl;
import model.dao.*;
import model.entity.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AutenticazioneController",urlPatterns = "/autenticazione-controller")
public class AutenticazioneController extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AutenticazioneService serviceA= new AutenticazioneServiceImpl();
        session.removeAttribute("messaggio");
        try{
        if(request.getParameter("Accedi")!=null){
            String email=request.getParameter("email");
            String password=request.getParameter("password");
            String tipoUtente=UtenteRegistratoDAOImpl.doRetriveTipoUtenteByEmail(email);

            UtenteRegistratoBean utente=serviceA.loginUtente(email,password,tipoUtente);
            if(utente!=null){
                CarrelloBean carrello= (CarrelloBean) session.getAttribute("carrello");
                if(utente.getTipoUtente().compareToIgnoreCase("utente")!=0 && utente.getTipoUtente().compareToIgnoreCase("scolaresca")!=0 ) {
                    if(carrello!=null)
                        session.removeAttribute("carrello");
                }else{
                    carrello=serviceA.mergeCarrelloSessioneAndCarrelloDBAfterLogin(utente,carrello);
                    if (utente.getTipoUtente().compareToIgnoreCase("scolaresca")==0){
                        serviceA.applicaScontoScuola(carrello);
                    }
                    session.setAttribute("carrello",carrello);
                }

                if(utente.getTipoUtente().compareToIgnoreCase("utente")==0){
                    session.setAttribute("messaggio","login utente andato a buon fine");

                }else if(utente.getTipoUtente().compareToIgnoreCase("organizzatore")==0){
                    session.setAttribute("messaggio","login organizzatore andato a buon fine");

                }else if(utente.getTipoUtente().compareToIgnoreCase("scolaresca")==0){
                    session.setAttribute("messaggio","login scolaresca andato a buon fine");

                }else if(utente.getTipoUtente().compareToIgnoreCase("amministratore")==0){
                    session.setAttribute("messaggio","login amministratore andato a buon fine");
                }

            }else{
                session.setAttribute("messaggio", "dati login inseriti errati, riprovare");
            }

            session.setAttribute("selezionato",utente);
            callDispatcher(request,response,"/index.html");
        }
        if(request.getParameter("logout")!=null) {
            session.removeAttribute("selezionato");
            session.removeAttribute("carrello");

            callDispatcher(request,response,"/index.html");
        }
        if(request.getParameter("ConfermaCambioPwd")!=null){
            /** operazione non implementata
             * */
            String emailTo=request.getParameter("email");
            callDispatcher(request,response,"/index.html");        }
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
        if(address==null || address.contains("/autenticazione-controller") || address.trim().isEmpty()){
            address=".";
        }
        response.sendRedirect(address);
    }
}

