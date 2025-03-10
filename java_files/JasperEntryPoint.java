package jasper4py;

import py4j.GatewayServer;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.*;
import net.sf.jasperreports.export.*;

// ? Commands - 
// ? 1. javac -cp "jar/*" java_files/JasperEntryPoint.java
// ? 2. java -cp "jar/*;." java_files.JasperEntryPoint [For windows]
// ? 2. java -cp "jar/*:." java_files.JasperEntryPoint [For linux/ubuntu]

public class JasperEntryPoint {
    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new JasperEntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }

    public void generate_report(String jrxmlPath, String outputPath,
            Map<String, Object> dynamicParams, String exportFormat) throws Exception {
        // Add this before the connection

        // Load the JDBC driver
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // Retrieve database connection parameters from dynamicParams
        String dbHost = (String) dynamicParams.get("dbHost");
        String dbName = (String) dynamicParams.get("dbName");
        String dbUser = (String) dynamicParams.get("dbUser");
        String dbPassword = (String) dynamicParams.get("dbPassword");

        // Database connection
        String connectionUrl = "jdbc:sqlserver://" + dbHost + ";"
                + "database=" + dbName + ";"
                + "user=" + dbUser + ";"
                + "password=" + dbPassword + ";"
                + "encrypt=false;";

        Connection conn = DriverManager.getConnection(connectionUrl);

        // Set up parameters
        Map<String, Object> parameters = new HashMap<String, Object>();

        for (Map.Entry<String, Object> entry : dynamicParams.entrySet()) {
            parameters.put(entry.getKey(), entry.getValue());
        }

        // Compile and fill report
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlPath);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

        if ("PDF".equalsIgnoreCase(exportFormat)) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
        } else if ("XLSX".equalsIgnoreCase(exportFormat)) {
            exportToExcel(jasperPrint, outputPath);
        } else if ("DOCX".equalsIgnoreCase(exportFormat)) {
            exportToWord(jasperPrint, outputPath);
        } else {
            throw new IllegalArgumentException("Unsupported export format: " + exportFormat);
        }

        conn.close();
    }

    private void exportToExcel(JasperPrint jasperPrint, String outputPath) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputPath));

        // Optionally set some export configurations (like removing page numbers)
        SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
        reportConfig.setRemoveEmptySpaceBetweenRows(true);
        reportConfig.setWhitePageBackground(false);
        exporter.setConfiguration(reportConfig);

        exporter.exportReport();
    }

    private void exportToWord(JasperPrint jasperPrint, String outputPath) throws JRException {
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputPath));

        exporter.exportReport();
    }
}
