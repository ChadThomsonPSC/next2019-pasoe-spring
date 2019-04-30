package psc.services.pasoescheduler;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progress.open4gl.Parameter;
import com.progress.open4gl.javaproxy.ParamArray;

//@formatter:off
/* AppServer URL format, see: PASOEInst - oeablSecurity.properties, authManagers.xml: 
 * (1) "internal://nxgas" for local PAS for OE Server
 * (2) "http[s]://<host>[:<port>]/[oeabl-app-name/]apsv" for remote PAS for OE server 
 * (3) "http[s]://<host>[:<port>]/<aia-app-nam>/aia" for remote classic AppServer via AIA 
 * (4) "AppServer[s]://<name-server-host>:<name-server-port>/<service-name>" for remote classic AppServer using NameServer 
 * (5) "AppServerDC[s]://<AppServer-host>:<AppServer-port>/<service-name>" for remote classic AppServer using Direct Connect
 */
//@formatter:on        

public class PasOEScheduler {
    protected final Logger log     = LoggerFactory.getLogger(getClass());

    private PasOEClient    m_client;
    private String         m_asURL;
    private String         m_clsName;
    private Properties     m_props = null;

    public PasOEScheduler() {
        log.debug("New PasOEScheduler created");
    }

    /*
     * Getters and setters for each configuration variable
     */
    public String getAsURL() {
        return m_asURL;
    }

    public void setAsURL(String AsURL) {
        this.m_asURL = AsURL;
    }

    public String getOeClsName() {
        return m_clsName;
    }

    public void setOeClsName(String OeClsName) {
        this.m_clsName = OeClsName;
    }

    /**
     * Any method can be used as a Task, as long as there are no parameters
     */
    public void printMessages() {
        log.info("Hello Next2019");
    }

    /**
     * This method will exec OE Tasks on the AppServer
     * 
     * @throws Exception
     */
    public void runOETasks() throws Exception {
        // TODO: Assert that all required variables have values

        log.debug("Running with ASURL:[{}], clsName:[{}]", m_asURL, m_clsName);

        /*
         * Call O4glrt/Proxy code
         **/
        if (null == m_client) {
            log.trace("Building Client");
            createClient();
        }

        // collect parameters : if any
        ParamArray params = new ParamArray(0);

        // our implementation returns how many tasks were executed
        params.setReturnType(Parameter.PRO_INTEGER);

        // create classObject and invoke "runAllScheduled" method
        this.m_client.getOpenClassObject().invokeMethod("runAllScheduled", params);

        // collect return value
        Integer retval = (Integer) params.getReturnValue();
        log.debug("Returnvalue from runAllSchedule: {}", retval);

        // release class object
        this.m_client.releaseClassObject();
    }

    private synchronized void createClient() {
        if (null != this.m_client) {
            return;
        }
        m_client = new PasOEClient(this.m_asURL, this.m_clsName, m_props);
    }
}
