package java_files;

import py4j.GatewayServer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.*;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

public class JasperEntryPoint {
    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new JasperEntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started");
    }

    public void generate_report(String jrxmlPath, String outputPath,
    List<Map<String, Object>> dynamicParams, String exportFormat) throws Exception {
        
        // Set up parameters
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        List<Map<String, ?>> safeList = new ArrayList<>();
        for (Map<String, Object> record : dynamicParams) {
            safeList.add(record);
        }
        JRDataSource dataSource = new JRMapCollectionDataSource(safeList);


        // 3. Compile and fill the report
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlPath);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        if ("PDF".equalsIgnoreCase(exportFormat)) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
        } else if ("XLSX".equalsIgnoreCase(exportFormat)) {
            exportToExcel(jasperPrint, outputPath);
        } else if ("DOCX".equalsIgnoreCase(exportFormat)) {
            exportToWord(jasperPrint, outputPath);
        } else {
            throw new IllegalArgumentException("Unsupported export format: " + exportFormat);
        }
    }

    private void exportToExcel(JasperPrint jasperPrint, String outputPath) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputPath));

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
