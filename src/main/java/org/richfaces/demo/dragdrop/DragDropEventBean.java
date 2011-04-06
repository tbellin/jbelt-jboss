package org.richfaces.demo.dragdrop;

import javax.inject.Named;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.richfaces.event.DropEvent;
import org.richfaces.event.DropListener;

@Named
@RequestScoped
public class DragDropEventBean implements DropListener {

    @ManagedProperty(value = "#{dragDropBean}")
    private DragDropBean dragDropBean;

    public void setDragDropBean(DragDropBean dragDropBean) {
        this.dragDropBean = dragDropBean;
    }

    public void processDrop(DropEvent event) {
        dragDropBean.moveFramework((Framework) event.getDragValue());
    }
}