package es.caib.bantel.persistence.plugins;

import java.util.List;
import java.util.Properties;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.caib.bantel.model.ReferenciaTramiteBandeja;
import es.caib.bantel.model.Tramite;
import es.caib.bantel.persistence.delegate.DelegateUtil;
import es.caib.bantel.persistence.intf.BteConectorFacade;
import es.caib.bantel.persistence.intf.BteConectorFacadeHome;
import es.caib.bantel.wsClient.v1.client.ClienteWS;
import es.caib.sistra.plugins.PluginFactory;
import es.caib.sistra.plugins.login.AutenticacionExplicitaInfo;
import es.caib.util.CifradoUtil;

/**
 * Plugin que permite el acceso al BackOffice
 * para realizar avisos
 */
public class PluginBackOffice {

    private static Log log = LogFactory.getLog(PluginBackOffice.class);

    /**
	 * Configuraci�n del tr�mite con informaci�n del acceso al BackOffice
	 */
    private Tramite tramite;

    /**
	 * Crea plugin a partir configuraci�n tr�mite
	 * @param tramite
	 */
    public PluginBackOffice(Tramite tramite) {
        this.tramite = tramite;
    }

    /**
	 * Realiza el aviso de las nuevas entradas al BackOffice
	 * @param entradas
	 * @throws Exception
	 */
    public void avisarEntradas(List entradas, String usuAuto, String passAuto) throws Exception {
        log.debug("[" + tramite.getIdentificador() + "] - Aviso entradas a BackOffice para tramite " + tramite.getIdentificador());
        switch(tramite.getTipoAcceso()) {
            case Tramite.ACCESO_EJB:
                avisarEntradasEJB(entradas);
                break;
            case Tramite.ACCESO_WEBSERVICE:
                avisarEntradasWS(entradas, usuAuto, passAuto);
                break;
            default:
                throw new Exception("Tipo de acceso a BackOffice no soportado: " + tramite.getTipoAcceso());
        }
    }

    /**
	 * Realiza el aviso de las nuevas entradas al BackOffice mediante EJB
	 * @param entradas
	 * @throws Exception
	 */
    private void avisarEntradasEJB(List entradas) throws Exception {
        LoginContext lc = null;
        try {
            CallbackHandler handler = null;
            switch(tramite.getAutenticacionEJB()) {
                case Tramite.AUTENTICACION_SIN:
                    break;
                case Tramite.AUTENTICACION_ESTANDAR:
                    log.debug("Autenticacion explicita con usuario/password");
                    String claveCifrada = (String) DelegateUtil.getConfiguracionDelegate().obtenerConfiguracion().get("clave.cifrado");
                    String user = CifradoUtil.descifrar(claveCifrada, tramite.getUsr());
                    String pass = CifradoUtil.descifrar(claveCifrada, tramite.getPwd());
                    handler = new UsernamePasswordCallbackHandler(user, pass);
                    break;
                case Tramite.AUTENTICACION_ORGANISMO:
                    log.debug("Autenticacion explicita con plugin organismo");
                    AutenticacionExplicitaInfo authInfo = null;
                    try {
                        authInfo = PluginFactory.getInstance().getPluginAutenticacionExplicita().getAutenticacionInfo();
                        log.debug("Usuario plugin autenticacion organismo: " + authInfo.getUser());
                    } catch (Exception ex) {
                        throw new Exception("Excepcion obteniendo informacion autenticacion explicita a traves de plugin organismo", ex);
                    }
                    handler = new UsernamePasswordCallbackHandler(authInfo.getUser(), authInfo.getPassword());
                    break;
            }
            if (handler != null) {
                lc = new LoginContext("client-login", handler);
                lc.login();
            }
            log.debug("[" + tramite.getIdentificador() + "] -Aviso a backoffice a trav�s de EJB - Version original");
            String ents[] = new String[entradas.size()];
            for (int i = 0; i < entradas.size(); i++) {
                ReferenciaTramiteBandeja ref = (ReferenciaTramiteBandeja) entradas.get(i);
                ents[i] = new String(ref.getNumeroEntrada());
            }
            BteConectorFacadeHome homeCF = (BteConectorFacadeHome) EjbBackOfficeFactory.getInstance().getHome(tramite.getJndiEJB(), (tramite.getLocalizacionEJB() == Tramite.EJB_LOCAL ? "LOCAL" : tramite.getUrl()));
            BteConectorFacade ejbCF = homeCF.create();
            ejbCF.avisoEntradas(ents);
            log.debug("[" + tramite.getIdentificador() + "] - Aviso entradas completado");
        } catch (Exception exc) {
            log.error("[" + tramite.getIdentificador() + "] - " + exc);
            throw exc;
        } finally {
            if (lc != null) {
                lc.logout();
            }
        }
    }

    /**
	 * Realiza el aviso de las nuevas entradas al BackOffice mediante Webservice
	 * @param entradas
	 * @throws Exception
	 */
    private void avisarEntradasWS(List entradas, String usuAuto, String passAuto) throws Exception {
        log.debug("[" + tramite.getIdentificador() + "] -Aviso a backoffice a trav�s de WS");
        Properties config = DelegateUtil.getConfiguracionDelegate().obtenerConfiguracion();
        String user = null, pass = null;
        switch(tramite.getAutenticacionEJB()) {
            case Tramite.AUTENTICACION_SIN:
                log.debug("Autenticacion implicita con usuario auto");
                user = usuAuto;
                pass = passAuto;
                break;
            case Tramite.AUTENTICACION_ESTANDAR:
                log.debug("Autenticacion explicita con usuario/password");
                String claveCifrada = (String) config.get("clave.cifrado");
                user = CifradoUtil.descifrar(claveCifrada, tramite.getUsr());
                pass = CifradoUtil.descifrar(claveCifrada, tramite.getPwd());
                break;
            case Tramite.AUTENTICACION_ORGANISMO:
                log.debug("Autenticacion explicita con plugin organismo");
                AutenticacionExplicitaInfo authInfo = null;
                try {
                    authInfo = PluginFactory.getInstance().getPluginAutenticacionExplicita().getAutenticacionInfo();
                    log.debug("Usuario plugin autenticacion organismo: " + authInfo.getUser());
                } catch (Exception ex) {
                    throw new Exception("Excepcion obteniendo informacion autenticacion explicita a traves de plugin organismo", ex);
                }
                user = authInfo.getUser();
                pass = authInfo.getPassword();
                break;
        }
        String prop = config.getProperty("webService.cliente.asincrono");
        if (prop == null) prop = "true";
        ClienteWS.avisarEntradasWS(entradas, tramite.getUrl(), user, pass, Boolean.valueOf(prop).booleanValue());
    }
}
