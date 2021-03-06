package org.apertereports.dashboard;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import eu.livotov.tpt.gui.widgets.TPTLazyLoadingLayout;
import org.apertereports.AbstractReportingApplication;
import org.apertereports.ui.UiFactory;

/**
 * Displays the portlet view of a configured dashboard. The view may contain a
 * custom HTML and a number of generated reports.
 */
public class ViewDashboardComponent extends AbstractDashboardComponent {

    private VerticalLayout mainPanel = UiFactory.createVLayout(null);
    private Panel contentPanel = new Panel();

    public ViewDashboardComponent() {
        contentPanel.setScrollable(true);
        contentPanel.setStyleName("borderless light");
        contentPanel.setSizeFull();
        mainPanel.addComponent(contentPanel);
        initData();
        setCompositionRoot(mainPanel);
    }

    /**
     * Initiates a lazy loading component on top of a {@link ReportViewComponent}
     * which is the main workhorse that displays the dashboard.
     */
    @Override
    protected void initComponentData() {
        contentPanel.removeAllComponents();
        contentPanel.addComponent(new TPTLazyLoadingLayout(new ReportViewComponent((AbstractReportingApplication) getApplication(), cache, template, reportConfigs, true), true));
    }
}
