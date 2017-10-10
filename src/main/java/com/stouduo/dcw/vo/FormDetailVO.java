package com.stouduo.dcw.vo;

import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.domain.FormProperty;
import com.stouduo.dcw.domain.FormValue;

import java.util.List;

public class FormDetailVO {
    private long formValueCount;
    private long todaySubmitCount;
    private List<FormProperty> formProperties;
    private List<FormValue> formValues;
    private Form form;

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setFormProperties(List<FormProperty> formProperties) {
        this.formProperties = formProperties;
    }

    public List<FormProperty> getFormProperties() {
        return formProperties;
    }

    public List<FormValue> getFormValues() {
        return formValues;
    }

    public void setFormValues(List<FormValue> formValues) {
        this.formValues = formValues;
    }

    public long getFormValueCount() {
        return formValueCount;
    }

    public void setFormValueCount(long formValueCount) {
        this.formValueCount = formValueCount;
    }

    public long getTodaySubmitCount() {
        return todaySubmitCount;
    }

    public void setTodaySubmitCount(long todaySubmitCount) {
        this.todaySubmitCount = todaySubmitCount;
    }

}
