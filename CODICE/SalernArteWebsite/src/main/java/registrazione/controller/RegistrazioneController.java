package registrazione.controller;

import model.entity.OrganizzatoreBean;
import model.entity.ScolarescaBean;
import model.entity.UtenteRegistratoBean;
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
        RegistrazioneServiceimpl serviceA= new RegistrazioneServiceimpl();
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

                if(tipoUtente.compareTo("utenteRegistrato")==0){
                    String nome=request.getParameter("nome");
                    String cognome=request.getParameter("cognome");
                    Date datadiNascita=Date.valueOf(request.getParameter("dataDiNascita"));
                    String gender= request.getParameter("gender");
                    UtenteRegistratoBean newUtente=serviceA.registrazioneUtente(gender,nome,cognome,email,password,datadiNascita);
                    session.setAttribute("selezionato",newUtente);

                }else
                if(tipoUtente.compareTo("scolaresca")==0){
                    String istituto=request.getParameter("istituto");
                    ScolarescaBean newUtente= serviceA.registrazioneScolaresca(email,password,istituto);
                    session.setAttribute("selezionato",newUtente);
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
                    OrganizzatoreBean newUtente=serviceA.registrazioneOrganizzatore(gender,iban,nome,cognome,email,password,biografia,azienda,datadiNascita);
                    session.setAttribute("selezionato",newUtente);

                }else{
                    //errore
                }

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
