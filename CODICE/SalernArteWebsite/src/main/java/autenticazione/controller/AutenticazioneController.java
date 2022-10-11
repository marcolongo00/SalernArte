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
import java.sql.Date;

@WebServlet(name = "AutenticazioneController",urlPatterns = "/autenticazione-controller")
public class AutenticazioneController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AutenticazioneService serviceA= new AutenticazioneServiceImpl();

        if(request.getParameter("goToLogin")!=null){
            String address="WEB-INF/autenticazione/login.jsp";
            RequestDispatcher dispatcher=request.getRequestDispatcher(address);
            dispatcher.forward(request,response);
        }

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
                    session.setAttribute("carrello",carrello);
                }
            }else{
                new RuntimeException("errore Login"); //myException
            }

            session.setAttribute("selezionato",utente);
            session.setAttribute("tipoUtente",tipoUtente);

           //aggiungi dati carrello appena disponibili
            String address=request.getHeader("referer");

            if(address==null || address.contains("/autenticazione-controller") || address.trim().isEmpty()){
                address=".";
            }

            response.sendRedirect(address);
        }
        if(request.getParameter("logout")!=null) {
            session.removeAttribute("selezionato");
            session.removeAttribute("tipoUtente");
            session.removeAttribute("carrello");


            String address = request.getHeader("referer");
            //attenzione alle pagine in cui un utente non ha il permesso di stare
            //a questo punto andrei alla homepage
            if (address == null || address.contains("/autenticazione-controller") || address.trim().isEmpty()) {
                address = ".";
            }

            response.sendRedirect(address);
        }
    }
}
