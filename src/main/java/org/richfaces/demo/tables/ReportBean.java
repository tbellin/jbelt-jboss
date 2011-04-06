package org.richfaces.demo.tables;

import javax.inject.Named;
import javax.faces.bean.RequestScoped;

import org.richfaces.demo.tables.model.expenses.ExpenseReport;

@Named
@RequestScoped
public class ReportBean {
    ExpenseReport expReport;

    public ExpenseReport getExpReport() {
        if (expReport == null) {
            expReport = new ExpenseReport();
        }
        return expReport;
    }

    public void setExpReport(ExpenseReport expReport) {
        this.expReport = expReport;
    }
}
