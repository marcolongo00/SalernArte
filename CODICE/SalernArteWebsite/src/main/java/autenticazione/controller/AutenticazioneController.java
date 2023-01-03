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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AutenticazioneService serviceA= new AutenticazioneServiceImpl();
        /*
        if(request.getParameter("goToLogin")!=null){
            //credo non venga mia usata. contorllare
            String address="WEB-INF/autenticazione/login.jsp";
            callDispatcher(request,response,address);
        }
         */

        if(request.getParameter("Accedi")!=null){
            String email=request.getParameter("email");
            String password=request.getParameter("password");
            String tipoUtente=UtenteRegistratoDAOImpl.doRetriveTipoUtenteByEmail(email);

            UtenteRegistratoBean utente=serviceA.loginUtente(email,password,tipoUtente);
            if(utente!=null){
                CarrelloBean carrello= (CarrelloBean) session.getAttribute("carrello");
                if(utente.getTipoUtente().compareTo("utente")!=0 && utente.getTipoUtente().compareTo("scolaresca")!=0 ) {
                    if(carrello!=null)
                        session.removeAttribute("carrello");
                }else{
                    carrello=serviceA.mergeCarrelloSessioneAndCarrelloDBAfterLogin(utente,carrello);
                    if (utente.getTipoUtente().compareToIgnoreCase("scolaresca")==0){
                        serviceA.applicaScontoScuola(carrello);
                    }
                    session.setAttribute("carrello",carrello);
                }
            }else{
                new RuntimeException("errore Login");
            }

            session.setAttribute("selezionato",utente);
            callReferer(request,response);
        }
        if(request.getParameter("logout")!=null) {
            session.removeAttribute("selezionato");
            session.removeAttribute("carrello");

            callDispatcher(request,response,"/index.html");
        }
        if(request.getParameter("ConfermaCambioPwd")!=null){
            //DA FARE
            String emailTo=request.getParameter("email");
            //contorlal formato email e controlla che esista in db
            //retriene by email
            //genera stringa per password casuale
            //cambia la pwd nel db e invia la mail
            callDispatcher(request,response,"/index.html");        }
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

