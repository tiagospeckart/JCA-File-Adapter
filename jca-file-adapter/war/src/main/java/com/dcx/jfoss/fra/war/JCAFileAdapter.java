package com.dcx.jfoss.fra.war;

import com.dcx.jfoss.fra.api.JCAFileAdapterConnection;
import com.dcx.jfoss.fra.api.JCAFileAdapterConnectionFactory;
import com.dcx.jfoss.fra.war.exceptions.TDOCBusinessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.InitialContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Class is used in to be able to handle files using jca file
 *
 */
public class JCAFileAdapter {

    private JCAFileAdapter() {
    }

    private static final Logger logger = LogManager.getLogger(JCAFileAdapter.class);
    private static final int MAX_RETRY_ATTEMPTS = 5; // retry connection
    private static final int MILL_SECONDS_THREAD_SLEEP = 5000; // 1s between each retry
    private static Map<String, JCAFileAdapterConnection> cache = new HashMap<>();

    /**
     * Get JCAFileAdapterConnection for project in order to access attachments.
     *
     * @return connection - the JCA FileAdapter connection.
     */
    public static JCAFileAdapterConnection getConnection(String jndiName) {
        validateJndiName(jndiName);

        JCAFileAdapterConnection result = getCachedConnection(jndiName);

        if (result == null) {
          result = findAndCacheConnection(jndiName);
        }
        return result;
    }

    private static void validateJndiName(String jndiName) {
        if (jndiName == null) {
            logger.error("wspdok.jcaadapter.connection.jndi.null; JNDI is null!");
            throw new IllegalArgumentException("JNDI is null!");
        }
    }

    // TODO
    private static JCAFileAdapterConnection getCachedConnection(String jndiName) {
        JCAFileAdapterConnection result = cache.get(jndiName);
        if (result != null && (result.isClosed() || result.isFileAccessLocked())) {
            cache.remove(jndiName);
            result = null;
        }
        return result;
    }

    private static JCAFileAdapterConnection findAndCacheConnection(String jndiName) {
        int retry = MAX_RETRY_ATTEMPTS;

        while (retry >= 0) {
            try {
                InitialContext initialContext = new InitialContext();
                JCAFileAdapterConnectionFactory connectionFactory = (JCAFileAdapterConnectionFactory) initialContext.lookup(jndiName);
                JCAFileAdapterConnection connection = connectionFactory.getConnection();
                if (connection != null) {
                    cache.put(jndiName, connection);
                    return connection;
                }
            } catch (Exception fae) {
                logger.error("wspdoku.jcaadapter.connection.error; Error on JCA adapter", fae);
            }

            retry = handleRetry(retry);
        }

        throw new TDOCBusinessException("JCA ERROR - Find and cache error for JNDI name: "+ jndiName);
    }

    private static int handleRetry(int retry) {
        retry--;

        if (retry >= 0) {
            try {
                Thread.sleep(MILL_SECONDS_THREAD_SLEEP);
            } catch (InterruptedException e) {
                logger.error("wspdoku.jcaadapter.connection.error.sleep; Error on jca sleep retry func: {}", e.getMessage());
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }

        return retry;
    }
}