package com.kayak.pms.opFlow.engine.entity;


import java.util.List;

/**
 * Created by daniel on 27/05/2017.
 */
public class BootstrapTreeview {
    private String text;
    private boolean selectable;
    List<BootstrapTreeview> nodes;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public List<BootstrapTreeview> getNodes() {
        return nodes;
    }

    public void setNodes(List<BootstrapTreeview> nodes) {
        this.nodes = nodes;
    }
}
