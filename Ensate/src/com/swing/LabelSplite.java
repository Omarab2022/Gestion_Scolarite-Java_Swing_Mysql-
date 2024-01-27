/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

/**
 *
 * @author Administrator
 */
public class LabelSplite extends JLabel {

    public boolean isGradiet() {
        return gradiet;
    }

   
    public void setGradiet(boolean gradiet) {
        this.gradiet = gradiet;
    }
    private boolean gradiet;
    
    @Override
    public void paint(Graphics grphcs){
    Graphics2D g2 = (Graphics2D)grphcs.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
    if (isGradiet()){
        g2.setPaint(new GradientPaint(0,0,new Color(115,115,115), getWidth(),0,new Color(115,115,115,0)));
    }
    else {
        g2.setColor(new Color(115,115,155));
    
    }
    g2.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
    g2.dispose();
     super.paint(grphcs);
    }
}
