/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.component;

import com.swing.MenuButton;
import com.swing.split;
import event.Menuevent;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import raven.alerts.MessageAlerts;
import raven.popup.GlassPanePopup;
import raven.popup.component.PopupCallbackAction;

import com.interfaces.LogoutListener;
import javax.swing.JOptionPane;
import raven.popup.component.PopupController;


/**
 *
 * @author Administrator
 */
public class Menu extends javax.swing.JPanel {

    /**
     * Creates new form Menu
     */
    private MenuButton selectedMenu;
    private MenuButton unSelectedMenu;
    private Animator animator;
    private Menuevent event;

    private LogoutListener logoutListener;
    
    

    public Menu() {
        initComponents();

        // Add the following lines to set the scroll bar policies
        scroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        setOpaque(false);
        scroll.setViewportBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        panelMenu.setLayout(new MigLayout("wrap, fillx", "[fill]"));
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                selectedMenu.setAnimate(fraction);
                if (unSelectedMenu != null) {
                    unSelectedMenu.setAnimate(1f - fraction);
                }
            }
        };
        animator = new Animator(300, target);
        animator.setAcceleration(.5f);
        animator.setDeceleration(.5f);
        animator.setResolution(0);

    }

    public void initMenu(Menuevent event, LogoutListener logoutListener) {
        this.event = event;
        this.logoutListener = logoutListener;
        addMenu("1", "demandes", 0);
        addMenu("2", "Historique", 1);
        Split("Re-send");
        addMenu("3", "Reclamations", 2);
        space();
        addMenu("key", "Logout", 4);

    }

    private void addMenu(String icon, String text, int index) {
        MenuButton menu = new MenuButton(index);
        setFont(menu.getFont().deriveFont(Font.PLAIN, 14));
        String imagePath = "/com/icon/" + icon + ".png";
        java.net.URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            menu.setIcon(new ImageIcon(imageUrl));
        } else {
            System.err.println("Image not found: " + imagePath);
        }

        menu.setText(" " + text);
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!animator.isRunning()) {
                    if (menu != selectedMenu) {
                        unSelectedMenu = selectedMenu;
                        selectedMenu = menu;
                        animator.start();
                        event.menuSelected(index);
                    }
                }
            }
        });

        if ("Logout".equals(text.trim())) { // Check if the menu item is "Logout"
            menu.addActionListener(new ActionListener() {
                
                
//                     @Override
//                public void actionPerformed(ActionEvent e) {
//
//                    MessageAlerts.getInstance().showMessage("Logout Confirmation", "Are you sure you want to logout?",
//                            MessageAlerts.MessageType.SUCCESS, MessageAlerts.YES_NO_OPTION, new PopupCallbackAction() {
//                        @Override
//                        public void action(PopupController pc, int i) {
//                            if (i == MessageAlerts.YES_OPTION) {
//                                if (logoutListener != null) {
//                                    logoutListener.onLogout(); // Notify the listener
//                                    // Add code here to navigate to the specific form ("1", "DEMANDE", 0)
//                                    event.menuSelected(0); // Assuming the index for "1", "DEMANDE", 0 is 0
//                                }
//                            }
//                        }
//                    });
//
//                }
                
                
                    
                @Override
                public void actionPerformed(ActionEvent e) {
                    

                    int choice = JOptionPane.showConfirmDialog(
                            null, // Use null as the parent component
                            "Are you sure you want to logout?",
                            "Logout Confirmation",
                            JOptionPane.YES_NO_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        if (logoutListener != null) {
                            logoutListener.onLogout(); // Notify the listener
                            // Add code here to navigate to the specific form ("1", "DEMANDE", 0)
                            event.menuSelected(0); // Assuming the index for "1", "DEMANDE", 0 is 0
                        }
                    }
                }
            });
        }

        panelMenu.add(menu);
    }

    private void Split(String name) {
        panelMenu.add(new split(name));
    }

    private void space() {
        panelMenu.add(new JLabel(), "push");
    }

    public void setSelected(int index) {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuButton) {
                MenuButton menu = (MenuButton) com;
                if (menu.getIndex() == index) {
                    if (menu != selectedMenu) {
                        unSelectedMenu = selectedMenu;
                        selectedMenu = menu;
                        animator.start();
                        event.menuSelected(index);

                    }
                    break;
                }

            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        imageAvatar1 = new com.swing.imageAvatar();
        scroll = new javax.swing.JScrollPane();
        panelMenu = new javax.swing.JPanel();

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/icon/apps.png"))); // NOI18N
        jLabel2.setText("Ent Ensate");

        imageAvatar1.setBorderSize(3);
        imageAvatar1.setBorderSpace(1);
        imageAvatar1.setGradientColor1(new java.awt.Color(255, 255, 255));
        imageAvatar1.setGradientColor2(new java.awt.Color(0, 102, 255));
        imageAvatar1.setImage(new javax.swing.ImageIcon(getClass().getResource("/com/icon/logo_ensate_n.png"))); // NOI18N

        panelMenu.setForeground(new java.awt.Color(255, 255, 255));
        panelMenu.setOpaque(false);
        scroll.setViewportView(panelMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroll)
                    .addComponent(imageAvatar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imageAvatar1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.swing.imageAvatar imageAvatar1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JScrollPane scroll;
    // End of variables declaration//GEN-END:variables

}
