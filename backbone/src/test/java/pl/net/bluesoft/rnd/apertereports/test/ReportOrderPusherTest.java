/**
 *
 */
package pl.net.bluesoft.rnd.apertereports.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.net.bluesoft.rnd.apertereports.ReportOrderPusher;
import pl.net.bluesoft.rnd.apertereports.dao.ReportOrderDAO;
import pl.net.bluesoft.rnd.apertereports.dao.ReportTemplateDAO;
import pl.net.bluesoft.rnd.apertereports.data.ReportOrder;
import pl.net.bluesoft.rnd.apertereports.data.ReportTemplate;
import pl.net.bluesoft.rnd.apertereports.util.DeCoder;
import pl.net.bluesoft.rnd.apertereports.util.ExceptionUtil;
import pl.net.bluesoft.rnd.apertereports.util.TestUtil;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author MW
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( {"/testEnvContext.xml"})
@TestExecutionListeners
public class ReportOrderPusherTest {
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpDB() throws Exception {
        TestUtil.initDB();
    }

    /**
     * Test method for
     * {@link pl.net.bluesoft.rnd.apertereports.ReportOrderPusher#buildNewOrder(pl.net.bluesoft.rnd.apertereports.data.ReportTemplate, java.util.Map, java.lang.String, java.lang.String, String, String)}
     * .
     *
     * @throws Exception
     */
    @Test
    public final void testBuildNewOrder() throws Exception {
        ReportOrder rep1 = null, rep2 = null, rep3 = null, rep4 = null, rep5 = null;
        ReportTemplate reportTemplate1 = null, reportTemplate2 = null;
        try {
            reportTemplate1 = new ReportTemplate();
            // reportTemplate1.setId(-1);
            Map<String, String> parameters1 = new HashMap<String, String>();
            parameters1.put("id1", "1");
            String format = "a", recipientEmail = "b", username = "c", replyToQ = "d";

            reportTemplate2 = new ReportTemplate();
            // reportTemplate1.setId(-2);
            Map<String, String> parameters2 = new HashMap<String, String>();
            parameters2.put("id1", "1");
            parameters2.put("id2", "2");

            ReportTemplateDAO.saveOrUpdate(reportTemplate1);
            ReportTemplateDAO.saveOrUpdate(reportTemplate2);

            int reportOrders0 = ReportOrderDAO.fetchAllReportOrders().size();

            rep1 = ReportOrderPusher.buildNewOrder(reportTemplate1, parameters1, "", "", "", "");

            int reportOrders1 = ReportOrderDAO.fetchAllReportOrders().size();

            rep2 = ReportOrderPusher.buildNewOrder(reportTemplate2, parameters1, "", "", "", "");

            int reportOrders2 = ReportOrderDAO.fetchAllReportOrders().size();

            rep3 = ReportOrderPusher.buildNewOrder(reportTemplate1, parameters1, "", "", "", "");

            int reportOrders3 = ReportOrderDAO.fetchAllReportOrders().size();

            rep4 = ReportOrderPusher.buildNewOrder(reportTemplate1, parameters2, "", "", "", "");

            int reportOrders4 = ReportOrderDAO.fetchAllReportOrders().size();

            rep5 = ReportOrderPusher.buildNewOrder(reportTemplate1, parameters1, format, recipientEmail, username,
                    replyToQ);

            int reportOrders5 = ReportOrderDAO.fetchAllReportOrders().size();

            assertNotNull("reportOrder1 not created", rep1);
            assertNotNull("reportOrder2 not created", rep2);
            assertNull("created duplicate reportOrder", rep3);
            assertNotNull("reportOrder4 not created");
            assertNotNull("reportOrder5 not created");

            assertArrayEquals("parameters1 saved incorrectly", DeCoder.serializeParameters(parameters1),
                    rep1.getParametersXml());
            assertArrayEquals("parameters2 saved incorrectly", DeCoder.serializeParameters(parameters2),
                    rep4.getParametersXml());
            assertEquals("format not saved", format, rep5.getOutputFormat());
            assertEquals("email not saved", recipientEmail, rep5.getRecipientEmail());
            assertEquals("username not saved", username, rep5.getUsername());
            assertEquals("replyToQ not saved", replyToQ, rep5.getReplyToQ());

            assertEquals("reportOrder1 not created in DB", reportOrders0 + 1, reportOrders1);
            assertEquals("reportOrder2 not created in DB", reportOrders1 + 1, reportOrders2);
            assertEquals("duplicate reportOrder3 created in DB", reportOrders2, reportOrders3);
            assertEquals("reportOrder4 not created in DB", reportOrders3 + 1, reportOrders4);
            assertEquals("reportOrder5 not created in DB", reportOrders4 + 1, reportOrders5);
        }
        catch (Exception e) {
            ExceptionUtil.logSevereException(e);
            throw e;
        }
        finally {
            try {
                ReportOrderDAO.removeReportOrder(rep1, rep2, rep3, rep4, rep5);
            }
            finally {
                try {
                    ReportTemplateDAO.remove(reportTemplate1.getId());
                }
                finally {
                    ReportTemplateDAO.remove(reportTemplate2.getId());
                }
            }
        }

    }
}