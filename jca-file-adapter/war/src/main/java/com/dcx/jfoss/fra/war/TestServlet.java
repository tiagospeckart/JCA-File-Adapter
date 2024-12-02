package com.dcx.jfoss.fra.war;

import com.dcx.jfoss.fra.api.JCAFileAdapterConnection;
import com.dcx.jfoss.fra.war.constants.TDOCDbConstants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(TestServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String filename = "test.txt";
        String content = "test content";

        writeDMUImportFile(filename, content);
    }

    public boolean writeDMUImportFile(String fileName, String content) {
        boolean result = false;
        JCAFileAdapterConnection con = null;

        try {
            con = JCAFileAdapter.getConnection(TDOCDbConstants.JNDI_JCARESOURCE_DMU);

            if (con.isFile(fileName)) {
                logger.info("{} Existing file will be deleted: {}", TDOCDbConstants.INFO_MSG, fileName);
                con.delete(fileName);
            }

            logger.info("{} Creating new file: {}", TDOCDbConstants.INFO_MSG, fileName);
            con.createNewFile(fileName);

            try (OutputStream outputStream = con.getOutputStream(fileName, true)) {
                byte[] strToBytes = content.getBytes();
                outputStream.write(strToBytes);
                outputStream.flush();
            }

            result = con.isFile(fileName);
            logger.info("{} File {} created successfully", TDOCDbConstants.INFO_MSG, fileName);

        } catch (Exception e) {
            logger.error("An error occurred while writing DMU import file", e);
            logger.warn("code.scheduler.tdoc.exception");
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    logger.error("Error closing JCAFileAdapterConnection", e);
                }
            }
        }

        return result;
    }
}
/*

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // Handle the POST request
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        // Implement your testing logic here, interact with ResourceAccessBean
        try {
            resourceAccessBean.connect();
            // Perform testing operations with the connection or any other logic
            response.getWriter().write("Connection successful! Testing logic executed.");
            resourceAccessBean.disconnect();
        } catch (Exception e) {
            // Handle exceptions appropriately
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();  // Log the exception or handle it according to your application's requirements
        }
    }

 */

