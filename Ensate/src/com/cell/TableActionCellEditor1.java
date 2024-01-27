/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cell;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author Administrator
 */
public class TableActionCellEditor1 extends DefaultCellEditor {
    private TableActionEvent1 event;
    public TableActionCellEditor1(TableActionEvent1 event) {
        super(new JCheckBox());
        this.event=event;
    }


    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int i, int i1) {
        ActionPanel1 action = new ActionPanel1();
        action.initEvent(event,i);
       action.setBackground(jtable.getSelectionBackground());
        return action;
    }
}
