package org.richfaces.demo.lists;

import javax.inject.Named;
import javax.faces.bean.RequestScoped;

@Named
@RequestScoped
public class ListBean {
    private String listType = "ordered";

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }
}
