package application;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.JREmptyDataSource;


public class ReceiptGenerator {

    private static final String REPORT_TEMPLATE_PATH = "/path/to/your/jasper/template.jasper";
    private static final String PDF_OUTPUT_PATH = "/path/to/output/receipt.pdf";

    public static void generateReceipt(OrderNow order) {
        try {
            // Load JasperReport template
            InputStream inputStream = ReceiptGenerator.class.getResourceAsStream(REPORT_TEMPLATE_PATH);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);

            // Prepare parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("OrderNum", order.getOnum());
            parameters.put("OrderDate", order.getOdate());
            parameters.put("CustomerName", order.getCname());
            

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            // Export to PDF
            File pdfOutputFile = new File(PDF_OUTPUT_PATH);
            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfFile(jasperPrint, pdfOutputFile.getAbsolutePath());

            System.out.println("Receipt generated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
