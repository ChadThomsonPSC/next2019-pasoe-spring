package psc.services.pasoescheduler;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progress.appserv.services.logging.Slf4jAppLogger;
import com.progress.common.ehnlog.LogException;
import com.progress.open4gl.ConnectException;
import com.progress.open4gl.Open4GLException;
import com.progress.open4gl.RunTime4GLException;
import com.progress.open4gl.SystemErrorException;
import com.progress.open4gl.javaproxy.Connection;
import com.progress.open4gl.javaproxy.OpenAppObject;
import com.progress.open4gl.javaproxy.OpenClassObject;

public class PasOEClient {
    protected final Logger log           = LoggerFactory.getLogger(getClass());

    private String         m_asURL;
    private String         m_clsName;
    private Properties     m_connProps;

    private boolean        m_isConnected = Boolean.FALSE;

    private Connection     m_connection;

    private OpenAppObject  m_client;
    private OpenClassObject m_cls;
    
    /**
     * At minimum, we need to know the URL and which class to exec. Properties is
     * optional - connection properties.
     * 
     * @param asURL
     * @param clsName
     * @param connProps
     */
    public PasOEClient(String asURL, String clsName, Properties connProps) {
        // TODO: Assert that required vars are populated
        this.m_asURL     = asURL;
        this.m_clsName   = clsName;
        this.m_connProps = connProps;
    }

    public boolean isConnected() {
        return this.m_isConnected;
    }
    
    public void testConnect() {
        if (!isConnected()) {
            
            try {
                connect();
            } catch (Open4GLException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                log.error("Error during connect", e);
            }
        }
    }
    
    protected synchronized boolean connect() throws Open4GLException {
        boolean retval = Boolean.FALSE;
        String connretstr = null;

        log.trace("Attempting to Connect: isConnected: {}", this.m_isConnected);

        if (this.m_isConnected) {
            return Boolean.TRUE;
        }

        // connections can be persistent -- they're just properties
        this.m_connection = new Connection();
        this.m_connection.setUrl(this.m_asURL);

        if (null != this.m_connProps) {
            this.m_connection.setProperties(this.m_connProps);
        }

        // always session-free(1) - session-managed(0)
        this.m_connection.setSessionModel(1);

        // create the as client object (OpenAppObject)
        try {
            log.debug("Creating new Connection to: {}", this.m_asURL);

            /*
             * the 3rd parameter is the logging mechanism. Slf4jAppLogger directs OE log
             * information to the appropriate PASOE log
             */
            this.m_client = new OpenAppObject(this.m_connection, "OEScheduler", new Slf4jAppLogger("O4gl"));

            // return string is returned from as.srvconnectproc, session-managed
            connretstr    = this.m_client._getProcReturnString();
            if (null != connretstr)
                log.debug("Connection return: {}", connretstr);

            retval             = Boolean.TRUE;
            this.m_isConnected = retval;

        } catch (ConnectException e) {
            throw new Open4GLException(e.getMessage(), new Object[] { e });
        } catch (SystemErrorException e) {
            throw new Open4GLException(e.getMessage(), new Object[] { e });
        } catch (IOException e) {
            throw new Open4GLException(e.getMessage(), new Object[] { e });
        } catch (LogException e) {
            throw new Open4GLException(e.getMessage(), new Object[] { e });
        }

        return retval;
    }

    public void disconnect() throws Open4GLException {
        if (null != m_cls) {
            this.m_cls._release();
            this.m_cls = null;
        }

        this.m_client._release();
        this.m_client = null;

        this.m_connection.releaseConnection();
        this.m_connection  = null;

        this.m_isConnected = Boolean.FALSE;
    }
    
    public void releaseClassObject() {
        if (null != this.m_cls) {
            try {
                this.m_cls._release();
                this.m_cls = null;
            } catch (SystemErrorException e) {
                e.printStackTrace();
            } catch (Open4GLException e) {
                e.printStackTrace();
            }
        }
    }

    public OpenClassObject getOpenClassObject() {
        if (null == this.m_cls) {
            try {
                if (!isConnected()) {
                    connect();
                }

                this.m_cls = this.m_client.createCO(this.m_clsName);
            } catch (RunTime4GLException e) {
                e.printStackTrace();
            } catch (SystemErrorException e) {
                e.printStackTrace();
            } catch (Open4GLException e) {
                e.printStackTrace();
            }

        }
        return this.m_cls;
    }
}
