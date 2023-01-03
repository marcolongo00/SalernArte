package registrazione.controller;

import model.entity.CarrelloBean;
import model.entity.UtenteRegistratoBean;
import registrazione.service.RegistrazioneService;
import registrazione.service.RegistrazioneServiceimpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "RegistrazioneController",urlPatterns = "/registrazione-controller")
public class RegistrazioneController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        RegistrazioneService serviceA= new RegistrazioneServiceimpl();
        /*
        if(request.getParameter("goToRegistrazione")!=null){ //sposta in registrazione service
            String address="WEB-INF/autenticazione/registrazione.jsp";
            RequestDispatcher dispatcher=request.getRequestDispatcher(address);
            dispatcher.forward(request,response);
        }
         */
        if(request.getParameter("registrazione")!= null){ //controllo sui dati e su password conferma

            String password=request.getParameter("password");
            String passConferma=request.getParameter("passwordConferma");
            if(password.compareTo(passConferma)!=0){
                throw new RuntimeException("Le due password inserite non corrispondono, riprovare");
            }
            //tutti altri controlli
                String email=request.getParameter("email");
                String tipoUtente=request.getParameter("tipoUtente");
                UtenteRegistratoBean utenteResult=null;

                if(tipoUtente.compareToIgnoreCase("utente")==0){
                    String nome=request.getParameter("nome");
                    String cognome=request.getParameter("cognome");
                    Date datadiNascita=Date.valueOf(request.getParameter("dataDiNascita"));
                    int gender= Integer.parseInt(request.getParameter("gender"));
                    utenteResult=serviceA.registrazioneUtente(gender,nome,cognome,email,password,datadiNascita);
                    CarrelloBean carrelloSessione= (CarrelloBean) session.getAttribute("carrello");
                    serviceA.salvaCarrelloSessione(utenteResult,carrelloSessione);
                }else
                if(tipoUtente.compareToIgnoreCase("scolaresca")==0){
                    String istituto=request.getParameter("istituto");
                    utenteResult= serviceA.registrazioneScolaresca(email,password,istituto);
                    CarrelloBean carrelloSessione= (CarrelloBean) session.getAttribute("carrello");
                    serviceA.salvaCarrelloSessione(utenteResult,carrelloSessione);
                    serviceA.applicaScontoScuola(carrelloSessione);
                    session.setAttribute("carrello",carrelloSessione);
                }
                else
                if(tipoUtente.compareToIgnoreCase("organizzatore")==0){
                    String nome=request.getParameter("nome");
                    String cognome=request.getParameter("cognome");
                    Date datadiNascita=Date.valueOf(request.getParameter("dataDiNascita"));
                    int gender= Integer.parseInt(request.getParameter("gender"));
                    String biografia=request.getParameter("biografia");
                    String azienda=request.getParameter("azienda");
                    String iban=request.getParameter("iban");
                    utenteResult=serviceA.registrazioneOrganizzatore(gender,iban,nome,cognome,email,password,biografia,azienda,datadiNascita);
                    session.removeAttribute("carrello");

                }else{ //admin?????
                    //errore
                }
                session.setAttribute("selezionato",utenteResult);
                callReferer(request,response);

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    private void callDispatcher(HttpServletRequest request, HttpServletResponse response,String address) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
    private void callReferer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String address=request.getHeader("referer"); //gli da fastidio, devi completamente separare dispatcher e referer
        if(address==null || address.contains("/registrazione-controller") || address.trim().isEmpty()){
            address=".";
        }
        response.sendRedirect(address);
    }
}
