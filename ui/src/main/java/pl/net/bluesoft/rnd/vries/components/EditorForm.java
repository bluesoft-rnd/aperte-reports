package pl.net.bluesoft.rnd.vries.components;

import com.vaadin.data.Item;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import eu.livotov.tpt.i18n.TM;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import pl.net.bluesoft.rnd.vries.dao.ReportTemplateDAO;
import pl.net.bluesoft.rnd.vries.data.ReportTemplate;
import pl.net.bluesoft.rnd.vries.engine.ReportCache;
import pl.net.bluesoft.rnd.vries.generators.ReportBeanFieldFactory;
import pl.net.bluesoft.rnd.vries.generators.ReportBeanFieldFactory.FieldChangeNotifier;
import pl.net.bluesoft.rnd.vries.util.FileStreamer;
import pl.net.bluesoft.rnd.vries.util.NotificationUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Displays a report template generation form.
 */
public abstract class EditorForm extends Panel {
    private boolean valueChanged = false;

    private final Button reportFetchButton = new Button(TM.get("manager.form.fetch"));
    private final Button reportSaveButton = new Button(TM.get("manager.form.save"));
    private final Form reportEditorForm = new Form();

    private VriesInvokerComponent reportInvokerButton;
    private ReportUploader uploader;

    private ReportTemplate report;

    private ReportBeanFieldFactory fieldFactory;
    private Object[] visibleFields = new Object[] {
            "filename", "reportname", "description", "active", "allowOnlineDisplay", "allowBackgroundOrder"
    };

    public EditorForm() {
        super();
        initForm();
    }

    /**
     * Invoked on form commit.
     *
     * @param aNew <code>TRUE</code> if the report template was new
     * @param savedReport Saved report template
     */
    public abstract void onSaveReport(boolean aNew, ReportTemplate savedReport);

    /**
     * Initializes the form view.
     */
    public void initForm() {
        setStyleName("borderless light");

        reportSaveButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                saveReport();
            }
        });
        reportFetchButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                downloadReport();
            }
        });

        uploader = new ReportUploader(this);
        reportInvokerButton = new VriesInvokerComponent(null, false);

        addComponent(uploader);
        addComponent(reportEditorForm);

        Layout formFooter = reportEditorForm.getFooter();
        formFooter.addComponent(reportSaveButton);
        formFooter.addComponent(reportFetchButton);
        formFooter.addComponent(reportInvokerButton);

        reportEditorForm.getFooter().setVisible(false);
        reportEditorForm.setCaption(TM.get("manager.form.title"));

        fieldFactory = new ReportBeanFieldFactory(new FieldChangeNotifier() {
            @Override
            public void fieldValueChanged(Item item, Object propertyId, Field field) {
                valueChanged = true;
            }
        }, visibleFields);
        reportEditorForm.setImmediate(true);
        reportEditorForm.setWriteThrough(false);
        reportEditorForm.setFormFieldFactory(fieldFactory);
        reportEditorForm.setVisibleItemProperties(visibleFields);

        toggleFormContent(false);
    }

    /**
     * Loads the view with the data from an input report template.
     *
     * @param rt Input report template
     */
    public void loadReport(ReportTemplate rt) {
        valueChanged = false;
        report = rt;
        if (report != null) {
            uploader.setReport(report);
            reportInvokerButton.setReport(report);
            fieldFactory.setReport(report);
            reportEditorForm.setItemDataSource(new BeanItem<ReportTemplate>(report));
            reportEditorForm.setVisibleItemProperties(visibleFields);
            toggleFormContent(StringUtils.isNotEmpty(report.getFilename()));
            uploader.setVisible(true);
        }
        else {
            toggleFormContent(false);
        }
    }

    /**
     * Reloads currently edited report template view.
     */
    public void reload() {
        loadReport(report);
        valueChanged = true;
    }

    /**
     * Saves currently edited report template to database.
     */
    public void saveReport() {
        Map<Field, String> messages = new LinkedHashMap<Field, String>();
        for (Object propertyId : reportEditorForm.getItemPropertyIds()) {
            Field field = reportEditorForm.getField(propertyId);
            try {
                field.validate();
            }
            catch (InvalidValueException e) {
                messages.put(field, e.getMessage());
            }
        }

        if (messages.isEmpty()) {
            reportEditorForm.commit();
            boolean isNew = report.getId() == null;
            ReportTemplateDAO.saveOrUpdate(report);
            ReportCache.removeReport(report.getId());
            valueChanged = false;
            onSaveReport(isNew, report);
        }
        else {
            StringBuilder sb = new StringBuilder();
            for (Field field : messages.keySet()) {
                sb.append(messages.get(field)).append("<br/>");
            }
            NotificationUtil.showValidationErrors(getWindow(), sb.toString());
        }
    }

    /**
     * Shows or hides the report template details.
     *
     * @param show <code>TRUE</code> to show the report template form
     */
    public void toggleFormContent(boolean show) {
        reportEditorForm.setVisible(show);
        reportEditorForm.getFooter().setVisible(show);
        uploader.setVisible(show);
    }

    /**
     * Opens a download generated report popup.
     */
    protected void downloadReport() {
        byte[] reportContent = Base64.decodeBase64(String.valueOf(report.getContent()).getBytes());
        FileStreamer.openFileInNewWindow(getApplication(), report.getFilename(), reportContent, "application/octet-stream");
    }

    /**
     * Indicates a form field or report template was changed.
     *
     * @return <code>TRUE</code> if any of the form fields was changed
     */
    public boolean isValueChanged() {
        return valueChanged;
    }
}