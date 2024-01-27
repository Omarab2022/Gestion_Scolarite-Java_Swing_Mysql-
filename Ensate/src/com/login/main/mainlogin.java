package com.login.main;

import com.login.Connector.ConnectDB;
import java.awt.Color;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import raven.alerts.MessageAlerts;
import raven.popup.GlassPanePopup;
import com.ensate.MainDashboard;

public class mainlogin extends javax.swing.JFrame {

    private Animator animatorLogin;

    public mainlogin() {
        
        GlassPanePopup.install(this);

        initComponents();

        getContentPane().setBackground(new Color(255, 255, 255));

        TimingTarget targetLogin = new TimingTargetAdapter() {

            @Override
            public void timingEvent(float fraction) {
                background1.setAnimate(fraction);
            }

            public void end() {
                panellogin.setVisible(false);
                background1.setShowPaint(true);
                // panelbody.setVisible(true);
            }

        };

        animatorLogin = new Animator(1400, targetLogin);
        animatorLogin.setResolution(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background1 = new com.login.swing.Background();
        panellogin = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        loginbutton = new com.admin.swing.Button();
        emailtextfiled = new com.admin.swing.TextField();
        passwordtextfield = new com.admin.swing.PasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        background1.setLayout(new java.awt.CardLayout());

        panellogin.setBackground(java.awt.Color.white);
        panellogin.setOpaque(false);

        jPanel1.setOpaque(false);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/login/icons/PNG6-1024x383 (1).png"))); // NOI18N

        loginbutton.setBackground(new java.awt.Color(151, 218, 252));
        loginbutton.setText("Login");
        loginbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginbuttonActionPerformed(evt);
            }
        });

        emailtextfiled.setBackground(new java.awt.Color(245, 245, 245));
        emailtextfiled.setLabelText("Email");

        passwordtextfield.setLabelText("Password");
        passwordtextfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordtextfieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(emailtextfiled, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordtextfield, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loginbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(emailtextfiled, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(passwordtextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(loginbutton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelloginLayout = new javax.swing.GroupLayout(panellogin);
        panellogin.setLayout(panelloginLayout);
        panelloginLayout.setHorizontalGroup(
            panelloginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelloginLayout.createSequentialGroup()
                .addGap(260, 260, 260)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(260, Short.MAX_VALUE))
        );
        panelloginLayout.setVerticalGroup(
            panelloginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelloginLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(134, Short.MAX_VALUE))
        );

        background1.add(panellogin, "card2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void loginbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginbuttonActionPerformed
        String email = emailtextfiled.getText();
        String password = String.valueOf(passwordtextfield.getPassword());

        if (!animatorLogin.isRunning()) {

            boolean action = true;
            if (email.isEmpty()) {
                emailtextfiled.setHelperText("Please input user name");
                emailtextfiled.grabFocus();
                action = false;

                // Ajout de l'écouteur pour effacer le message d'aide lorsque l'utilisateur commence à saisir
                emailtextfiled.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        emailtextfiled.setHelperText(null); // Efface le message d'aide
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        // Ne rien faire ici
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        // Ne rien faire ici
                    }
                });
            }

            if (password.isEmpty()) {
                passwordtextfield.setHelperText("Please input password");
                if (action) {
                    passwordtextfield.grabFocus();
                }
                action = false;

                // Ajout de l'écouteur pour effacer le message d'aide lorsque l'utilisateur commence à saisir
                passwordtextfield.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        passwordtextfield.setHelperText(null); // Efface le message d'aide
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        // Ne rien faire ici
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        // Ne rien faire ici
                    }
                });
            }

            if (action) {
                try {
                    java.sql.Connection connection = ConnectDB.ConnectToDB();
                    java.sql.PreparedStatement ps = connection.prepareStatement("SELECT * FROM admin WHERE email=? AND password=?");
                    ps.setString(1, email);
                    ps.setString(2, password);

                    java.sql.ResultSet rs = ps.executeQuery();

                    if (rs.next()) {

                        // Les informations d'identification sont correctes, fermez la fenêtre de connexion
                    dispose();

                    // Affichez la fenêtre du tableau de bord
                    MainDashboard mainDashboard = new MainDashboard();
                    mainDashboard.setVisible(true);
                    } else {

                        //JOptionPane.showMessageDialog(null, " Try Again !!! ", "error", JOptionPane.ERROR_MESSAGE);
                        MessageAlerts.getInstance().showMessage("Echec", "Please Try Again !!", MessageAlerts.MessageType.ERROR);

                    }

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }//GEN-LAST:event_loginbuttonActionPerformed

    private void passwordtextfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordtextfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordtextfieldActionPerformed

    public static void main(String args[]) {

          try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainlogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.login.swing.Background background1;
    private com.admin.swing.TextField emailtextfiled;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private com.admin.swing.Button loginbutton;
    private javax.swing.JPanel panellogin;
    private com.admin.swing.PasswordField passwordtextfield;
    // End of variables declaration//GEN-END:variables
}
