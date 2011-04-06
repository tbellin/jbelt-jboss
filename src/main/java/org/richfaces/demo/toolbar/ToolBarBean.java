package org.richfaces.demo.toolbar;

import java.io.Serializable;

import javax.inject.Named;
import javax.faces.bean.ViewScoped;

@Named
@ViewScoped
public class ToolBarBean implements Serializable{
    private String groupSeparator;
    private String groupItemSeparator;

    public String getGroupItemSeparator() {
        return groupItemSeparator;
    }

    public void setGroupItemSeparator(String groupItemSeparator) {
        this.groupItemSeparator = groupItemSeparator;
    }

    public String getGroupSeparator() {
        return groupSeparator;
    }

    public void setGroupSeparator(String groupSeparator) {
        this.groupSeparator = groupSeparator;
    }

}
