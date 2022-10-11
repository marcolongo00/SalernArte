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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AutenticazioneService service=new AutenticazioneServiceImpl();

        if(request.getParameter("verificaOrdini") != null)
        {

        }

        if(request.getParameter("listaUtenti")!=null){
            //usa service
            String tipoUtente= (String) session.getAttribute("tipoUtente"); //retrive utente non tipoutente
            List<UtenteRegistratoBean> allUtenti= service.retriveAllUtentiSistema(tipoUtente);
            request.setAttribute("allUtenti",allUtenti);

            String address = "/WEB-INF/gestioneUtente/AllUtenti.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(address);
            dispatcher.forward(request, response);
        }
        if(request.getParameter("goToProfilo")!=null){
            //nella jsp usa JS per vedere i dati del tipo di utente giusto
            String address = "/WEB-INF/gestioneUtente/ProfiloUtente.jsp";

            RequestDispatcher dispatcher = request.getRequestDispatcher(address);
            dispatcher.forward(request, response);
        }
        if(request.getParameter("updateProfilo")!=null)
        {
            UtenteRegistratoBean utente = (UtenteRegistratoBean) session.getAttribute("selezionato");
            String tipo = utente.getTipoUtente();

            if(utente == null)
                throw new RuntimeException("Operazione non autorizzata");

            if(request.getParameter("password").compareToIgnoreCase(request.getParameter("conferma-password")) != 0)
                throw new RuntimeException("Le password non corrispondono");

            if(tipo.compareToIgnoreCase("utente") == 0)
            {
                UtenteBean bean = new UtenteBean();
                bean.setNome(request.getParameter("nome"));
                bean.setCognome(request.getParameter("cognome"));
                bean.setEmail(request.getParameter("email"));

                if(!(request.getParameter("password").equals(null)))
                    bean.setPasswordHash(request.getParameter("password"),bean.isHash());

                bean.setDataDiNascita(Date.valueOf(request.getParameter("data")));
                bean.setSesso(Integer.parseInt(request.getParameter("sesso")));
                service.updateUtente(bean);
            }
            else if(tipo.compareToIgnoreCase("organizzatore") == 0)
            {
                OrganizzatoreBean bean = new OrganizzatoreBean();
                bean.setNome(request.getParameter("nome"));
                bean.setCognome(request.getParameter("cognome"));
                bean.setEmail(request.getParameter("email"));

                if(!(request.getParameter("password").equals(null)))
                    bean.setPasswordHash(request.getParameter("password"), bean.isHash());

                bean.setBiografia(request.getParameter("bio"));
                bean.setSesso(Integer.parseInt(request.getParameter("sesso")));
                bean.setDataDiNascita(Date.valueOf(request.getParameter("data")));
                bean.setIban(request.getParameter("iban"));
                service.updateUtente(bean);
            }
            else if(tipo.compareToIgnoreCase("scolaresca") == 0)
            {
                ScolarescaBean bean = new ScolarescaBean();
                bean.setIstituto(request.getParameter("istituto"));
                bean.setEmail(request.getParameter("email"));
                if(!(request.getParameter("password").equals(null)))
                    bean.setPasswordHash(request.getParameter("password"), bean.isHash());

                service.updateUtente(bean);
            }
            else{
                AmministratoreBean bean = new AmministratoreBean();
                bean.setNome(request.getParameter("nome"));
                bean.setCognome(request.getParameter("cognome"));
                bean.setEmail(request.getParameter("email"));

                if(!(request.getParameter("password").equals(null)))
                    bean.setPasswordHash(request.getParameter("password"), bean.isHash());

                service.updateUtente(bean);
            }
        }

    }
}
