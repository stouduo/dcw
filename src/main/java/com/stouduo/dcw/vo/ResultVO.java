package com.stouduo.dcw.vo;

import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.domain.FormValue;
import org.springframework.data.domain.Page;

public class ResultVO {
    private Form form;
    private Page<FormValue> formValues;

    public Page<FormValue> getFormValues() {
        return formValues;
    }

    public void setFormValues(Page<FormValue> formValues) {
        this.formValues = formValues;
    }

    public Form getForm() {

        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }
}
