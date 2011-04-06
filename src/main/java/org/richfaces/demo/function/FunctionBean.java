package org.richfaces.demo.function;

import javax.inject.Named;
import javax.faces.bean.RequestScoped;

@Named("functionBean")
@RequestScoped
public class FunctionBean {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
