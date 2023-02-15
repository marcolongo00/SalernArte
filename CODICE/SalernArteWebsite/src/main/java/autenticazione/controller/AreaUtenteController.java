package autenticazione.controller;

import autenticazione.service.AutenticazioneService;
import autenticazione.service.AutenticazioneServiceImpl;
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
import java.util.List;

@WebServlet(name = "AreaUtenteController",urlPatterns = "/area-utente")
public class AreaUtenteController extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AutenticazioneService service=new AutenticazioneServiceImpl();
        UtenteRegistratoBean utenteLoggato= (UtenteRegistratoBean) session.getAttribute("selezionato");

        if(request.getParameter("goToListaAcquisti") != null){
            int idUtente;
            if(utenteLoggato.getTipoUtente().compareToIgnoreCase("amministratore")==0){
                idUtente=Integer.parseInt(request.getParameter("id"));
            }else{
                idUtente=utenteLoggato.getId();
            }
            List<AcquistoBean>ordiniUtente= service.retrieveListaOrdiniUtente(utenteLoggato,idUtente);
            request.setAttribute("ordini",ordiniUtente);
            callDispatcher(request,response,"/WEB-INF/gestioneUtente/OrdiniUtente.jsp");
        }

        if(request.getParameter("listaUtenti")!=null){
             if(utenteLoggato==null) throw new RuntimeException("operazione non autorizzata");
            List<UtenteRegistratoBean> allUtenti= service.retrieveAllUtentiSistema(utenteLoggato);
            request.setAttribute("allUtenti",allUtenti);

            String address = "/WEB-INF/gestioneUtente/AllUtenti.jsp";
            callDispatcher(request,response,address);
        }
        if(request.getParameter("goToProfilo")!=null){
            if(utenteLoggato==null) throw new RuntimeException("Devi essere loggato epr accedere al tuo profilo");
            String address = "/WEB-INF/gestioneUtente/ProfiloUtente.jsp";
            callDispatcher(request,response,address);
        }
        if(request.getParameter("updateProfilo")!=null){
            //controllare autorizzazioni
            if(utenteLoggato == null)
                throw new RuntimeException("Operazione non autorizzata");

            String tipo = utenteLoggato.getTipoUtente();
            String password= request.getParameter("password");
            String passwordConferma= request.getParameter("passwordConferma");

            if(!password.isEmpty() && !passwordConferma.isEmpty() && password.compareToIgnoreCase(passwordConferma)!=0)
                throw new RuntimeException("Le password inserite non corrispondono");
            // il null delle string viene controllato nel service
            String email=request.getParameter("email");
            String nome=request.getParameter("nome");
            String cognome=request.getParameter("cognome");
            String istituto=request.getParameter("istituto");
            String biografia=request.getParameter("biografia");
            String iban=request.getParameter("iban");
            UtenteRegistratoBean utenteAggiornato=null;
            if(tipo.compareToIgnoreCase("utente") == 0) {
                Date dataDiNascita= Date.valueOf(request.getParameter("dataDiNascita"));
                int gender= Integer.parseInt(request.getParameter("gender"));

                utenteAggiornato=service.updateUtente(utenteLoggato,email,password,nome,cognome,dataDiNascita,gender);

            }else
                if(tipo.compareToIgnoreCase("organizzatore") == 0){
                    Date dataDiNascita= Date.valueOf(request.getParameter("dataDiNascita"));
                    int gender= Integer.parseInt(request.getParameter("gender"));

                    utenteAggiornato=service.updateOrganizzatore(utenteLoggato,email,password,nome,cognome,dataDiNascita,gender,biografia,iban);
                }
                else if(tipo.compareToIgnoreCase("scolaresca") == 0) {
                    utenteAggiornato=service.updateScolaresca(utenteLoggato,email,password,istituto);
                }
                else{
                    utenteAggiornato=service.updateAmministratore(utenteLoggato,email,password,nome,cognome);
                }
            session.setAttribute("selezionato",utenteAggiornato);
            String address = "/WEB-INF/gestioneUtente/ProfiloUtente.jsp";
            callDispatcher(request,response,address);
        }
        if(request.getParameter("eliminaProfilo")!=null){
            service.eliminaProfiloUtente(utenteLoggato);
            session.removeAttribute("selezionato");
            callDispatcher(request,response,"/index.html");
        }

    }
    private void callDispatcher(HttpServletRequest request, HttpServletResponse response,String address) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
    private void callReferer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String address=request.getHeader("referer"); //gli da fastidio, devi completamente separare dispatcher e referer
        if(address==null || address.contains("/area-utente") || address.trim().isEmpty()){
            address=".";
        }
        response.sendRedirect(address);
    }
}
