package registrazione.controller;

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

        if(request.getParameter("goToRegistrazione")!=null){ //sposta in registrazione service
            String address="WEB-INF/autenticazione/registrazione.jsp";
            RequestDispatcher dispatcher=request.getRequestDispatcher(address);
            dispatcher.forward(request,response);
        }
        if(request.getParameter("registrazione")!= null){ //controllo sui dati e su password conferma

            String password=request.getParameter("password");
            String passConferma=request.getParameter("passwordConferma");
            if(password.compareTo(passConferma)==0){ //tutti altri controlli
                String email=request.getParameter("email");
                String tipoUtente=request.getParameter("tipoUtente");
                UtenteRegistratoBean utenteResult=null;

                if(tipoUtente.compareTo("utente")==0){
                    String nome=request.getParameter("nome");
                    String cognome=request.getParameter("cognome");
                    Date datadiNascita=Date.valueOf(request.getParameter("dataDiNascita"));
                    String gender= request.getParameter("gender");
                    //utenteResult=new UtenteBean();
                    utenteResult=serviceA.registrazioneUtente(gender,nome,cognome,email,password,datadiNascita);

                }else
                if(tipoUtente.compareTo("scolaresca")==0){
                    String istituto=request.getParameter("istituto");
                    utenteResult= serviceA.registrazioneScolaresca(email,password,istituto);
                }
                else
                if(tipoUtente.compareTo("organizzatore")==0){
                    String nome=request.getParameter("nome");
                    String cognome=request.getParameter("cognome");
                    Date datadiNascita=Date.valueOf(request.getParameter("dataDiNascita"));
                    String gender= request.getParameter("gender");
                    String biografia=request.getParameter("biografia");
                    String azienda=request.getParameter("azienda");
                    String iban=request.getParameter("iban");
                    utenteResult=serviceA.registrazioneOrganizzatore(gender,iban,nome,cognome,email,password,biografia,azienda,datadiNascita);


                }else{ //admin?????
                    //errore
                }
                session.setAttribute("selezionato",utenteResult);
                session.setAttribute("tipoUtente",tipoUtente);

                //aggiungi dati carrello appena disponibili
                String address=request.getHeader("referer");

                if(address==null || address.contains("/registrazione-controller") || address.trim().isEmpty()){
                    address=".";
                }

                response.sendRedirect(address);
            }

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
