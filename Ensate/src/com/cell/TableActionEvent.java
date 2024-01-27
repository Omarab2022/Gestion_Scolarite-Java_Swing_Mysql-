/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cell;

/**
 * Interface for handling table action events.
 */
public interface TableActionEvent {
    void Onrefuse(int row);
    
    void Onapprove(int row) ;
    
    
    void Ondownload(int row);
}
