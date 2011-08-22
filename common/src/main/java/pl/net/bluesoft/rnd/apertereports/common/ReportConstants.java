package pl.net.bluesoft.rnd.apertereports.common;

import java.text.SimpleDateFormat;

/**
 * Contains constant fields used in the report generation process.
 */
public interface ReportConstants {
    /**
     * Web service namespace.
     */
    public static final String WS_NAMESPACE = "http://bluesoft.net.pl/rnd/apertereports/schemas";
    /**
     * Web service request local part.
     */
    public static final String WS_REQUEST_LOCAL_PART = "GenerateReportRequest";
    /**
     * File prefix for log purposes.
     */
    public static final String FILE_PREFIX = "AperteReports_";
    /**
     * A date format for writing in log files.
     */
    public static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("ddMMyyyy_hhmmss");
    /**
     * CSV field delimiter.
     */
    public static final String FIELD_DELIMITER = ";";
    /**
     * CSV record delimiter.
     */
    public static final String RECORD_DELIMITER = "\n\r";
    /**
     * Cache manager's thread interval between each cache analysis. Set to 1 second (1000 milliseconds).
     */
    public static final Integer CACHE_MANAGER_CHECK_INTERVAL = 1000;
    /**
     * A simple datetime pattern.
     */
    public static final String DATETIME_PATTERN = "dd-MM-yyyy HH:mm";
    /**
     * An instance of a default date formatter.
     */
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat(DATETIME_PATTERN);
    /**
     * Cyclic reports JMS queue name.
     */
    public static final String CYCLIC_REPORT_ORDER_RESPONSE_Q = "jms/queue/CyclicOrderResponse";
    /**
     * Report generation JMS queue name.
     */
    public static final String GENERATE_REPORT_Q = "jms/queue/GenerateReport";

    /**
     * Report order id property name.
     */
    public static final String REPORT_ORDER_ID = "reportOrderId";
    
    /**
     * Key used to pass map with compiled subreports to JasperFiller
     */
    public static final String SUBREPORT_MAP_PARAMETER_NAME = "SUBREPORT_MAP";

    /**
     * Represents a boolean report property.
     */
    public static enum BooleanValues {
        FALSE, TRUE
    }

    /**
     * Report generation error codes.
     */
    public static enum ErrorCodes {
        INVALID_REPORT_TYPE, JASPER_REPORTS_EXCEPTION, INVALID_EXPORTER_PARAMETER, REPORT_SOURCE_EXCEPTION, SERIALIZATION_EXCEPTION
    }

    /**
     * Represents report parameter input types. This is transformed into an adequate Vaadin input widget.
     */
    public static enum InputTypes {
        TEXT, DATE, TEXTAREA, SELECT, MULTISELECT, RADIOBUTTONS, CHECKBOXES, CHECKBOX, SPECIAL_CONTROL, FILTER, FILTERED_SELECT
    }

    /**
     * Different types of report parameters.
     */
    public static enum Keys {
        INPUT_TYPE, DICT_QUERY, WIDTH, MAXCHARS, REQUIRED, REQUIRED_ERROR, REGEXP, REGEXP_ERROR, ORDER, LABEL, SCRIPT_LANGUAGE, SPECIAL_CONTROL_CODE, SPECIAL_VALIDATION_CODE, SPECIAL_VALIDATION_ERROR, SPECIAL_DATA_QUERY_CODE, LEVEL, FILTER_GROUP, MULTIPLE_CHOICE, SELECT_ALL, DICT_ITEM_LIST
    }

    /**
     * Datasource parameter.
     */
    public static enum Parameter {
        DATASOURCE
    }

    /**
     * Allowed report formats.
     */
    public static enum ReportType {
        CSV, HTML, PDF, XLS
    }

    /**
     * Simple enum for report type conversion to a mime type.
     */
    public static enum ReportMimeType {
        CSV("application/csv"), HTML("text/html"), PDF("application/pdf"), XLS("application/vnd.ms-excel");

        private final String mimeType;

        ReportMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public String mimeType() {
            return mimeType;
        }
    }

}