/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.form;

import com.cell.RefuseButtonhandeler;
import com.cell.TableActionCellEditor1;
import com.cell.TableActionCellRender1;
import com.cell.TableActionEvent1;
import com.chart.ModelChart;
import com.textfield.SearchOptinEvent;
import com.textfield.SearchOption;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;


public class FormDocument extends javax.swing.JPanel {

    public FormDocument() {
         initComponents();
             txt3.addEventOptionSelected(new SearchOptinEvent() {
            @Override
            public void optionSelected(SearchOption option, int index) {
                txt3.setHint("Search by " + option.getName() + "...");
            }
        });
     
           txt3.addOption(new SearchOption("Name", new ImageIcon(getClass().getResource("/com/images/user.png"))));
           txt3.addOption(new SearchOption("Niveau", new ImageIcon(getClass().getResource("/com/images/niveau.png"))));
           txt3.addOption(new SearchOption("Email", new ImageIcon(getClass().getResource("/com/images/email.png"))));
           txt3.addOption(new SearchOption("Type_Demande", new ImageIcon(getClass().getResource("/com/images/documents.png"))));

   TableActionEvent1 event = new TableActionEvent1() {
            
            @Override
          public void Ondownload(int row) {
                      try {
                    // Call the method to generate the PDF
                    int etudiantId = RefuseButtonhandeler.getEtudiantIdFromApogee(table2, 2, row);
                    String typeDemande = RefuseButtonhandeler.getTypeDemandeFromRow(table2, row, 4);

                    // Récupérer le nom, l'apogée et l'email de l'étudiant depuis la base de données
                    String nom = "";
                    int apogee = 0;
                    int iddoc = 0;
                    String email = "";
                    String prenom = "";
                    String Niveau = "";
                    String nomEntreprise = "";
                    Date dateDebut = new Date();
                    Date dateFin = new Date();
                    String typeStage = "";
                    String cin1 = "";
                    String cne1 = "";
                    String dateDeNaissance1 = ""; 
                    String lieuDeNaissance1= ""; 
                    String encadrantAcademique = "";
                    String encadrantProfessionnel = "";
                    try {
                        // Utiliser la connexion à la base de données existante
                        Connection conn = FormHome.conn();

                        // Exécuter la requête pour récupérer les informations de l'étudiant
                       String query = "SELECT nom_etudiant, prenom_etudiant, Niveau, Apogee, Email, date_de_naissance, cin, cne, lieu_de_naissance FROM etudiants WHERE id = ?";
                        try ( PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                            preparedStatement.setInt(1, etudiantId);
                            ResultSet resultSet = preparedStatement.executeQuery();

                            // Vérifier s'il y a des résultats
                            if (resultSet.next()) {
                                nom = resultSet.getString("nom_etudiant");
                                prenom = resultSet.getString("prenom_etudiant");
                                Niveau = resultSet.getString("Niveau");
                                apogee = resultSet.getInt("Apogee");
                               
                                email = resultSet.getString("Email");
                                
                                Date dateDeNaissance = resultSet.getDate("date_de_naissance");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                dateDeNaissance1 = dateFormat.format(dateDeNaissance);
                              
                                cin1 = resultSet.getString("cin");
                                cne1 = resultSet.getString("cne");
                                lieuDeNaissance1 = resultSet.getString("lieu_de_naissance");

                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Gérer les erreurs de la base de données
                    }

                    try {
                        // Utiliser la connexion à la base de données existante
                        Connection conn = FormHome.conn();

                        // Exécuter la requête pour récupérer les informations de l'étudiant
                       String query = "SELECT id,nom_entreprise, date_debut_stage, date_fin_stage, type_stage, encadrant_academique, encadrant_professionnel\n"
        + "FROM demandes\n"
        + "WHERE etudiant_id = ? AND type_document = ?;";



                        try ( PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                            preparedStatement.setInt(1, etudiantId);
                             preparedStatement.setString(2, typeDemande);
                            ResultSet rs = preparedStatement.executeQuery();

                            // Vérifier s'il y a des résultats
                            if (rs.next()) {
                                 iddoc = rs.getInt("id");
                                nomEntreprise = rs.getString("nom_entreprise");
                                dateDebut = rs.getDate("date_debut_stage");
                                dateFin = rs.getDate("date_fin_stage");
                                typeStage = rs.getString("type_stage");
                                encadrantAcademique =rs.getString("encadrant_academique");
                                encadrantProfessionnel = rs.getString("encadrant_professionnel");

                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Gérer les erreurs de la base de données
                    }

                    // Générer le PDF avec toutes les informations récupérées
                    
                     RefuseButtonhandeler.generatePDF(etudiantId, typeDemande, nom, prenom, Niveau, apogee, email, nomEntreprise, dateDebut, dateFin, typeStage,cin1,cne1, dateDeNaissance1, lieuDeNaissance1, encadrantAcademique , encadrantProfessionnel,iddoc);

                    // Specify the file path of the generated PDF
                    String filePath = "/Users/user/Desktop/projet_GL/Ensate/src/com/files/" + etudiantId +iddoc+ "_Document.pdf";

                    // Create a File object with the specified file path
                    File pdfFile = new File(filePath);

                    // Check if the file exists before attempting to open it
                    if (pdfFile.exists()) {
                        // Use the Desktop class to open the PDF file
                        Desktop.getDesktop().open(pdfFile);
                    } else {
                        System.out.println("File not found: " + filePath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                };
         
           
}

                      @Override
                      public void Onresend(int row) {
                   int etudiantId = RefuseButtonhandeler.getEtudiantIdFromApogee(table2, 2, row);
                String typeDemande = RefuseButtonhandeler.getTypeDemandeFromRow(table2, row, 4);

                // Récupérer le nom, l'apogée et l'email de l'étudiant depuis la base de données
                String nom = "";
                int apogee = 0;
                String email = "";
                String prenom = "";
                String Niveau = "";
                String nomEntreprise = "";
                Date dateDebut = new Date();
                Date dateFin = new Date();
                String typeStage = "";
                String cin1 = "";
                String cne1 = "";
                int iddoc = 0;
                String dateDeNaissance1 = ""; 
                String lieuDeNaissance1= ""; 
                String encadrantAcademique = "";
                String encadrantProfessionnel = "";

                try {
                    // Utiliser la connexion à la base de données existante
                    Connection conn = FormHome.conn();

                    // Exécuter la requête pour récupérer les informations de l'étudiant
                    String query = "SELECT nom_etudiant, prenom_etudiant, Niveau, Apogee, Email, date_de_naissance, cin, cne, lieu_de_naissance FROM etudiants WHERE id = ?";
                    try ( PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                        preparedStatement.setInt(1, etudiantId);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        // Vérifier s'il y a des résultats
                        if (resultSet.next()) {
                            nom = resultSet.getString("nom_etudiant");
                            prenom = resultSet.getString("prenom_etudiant");
                            Niveau = resultSet.getString("Niveau");
                            apogee = resultSet.getInt("Apogee");
                            email = resultSet.getString("Email");
                            Date dateDeNaissance = resultSet.getDate("date_de_naissance");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            dateDeNaissance1 = dateFormat.format(dateDeNaissance);
                              
                            cin1 = resultSet.getString("cin");
                            cne1 = resultSet.getString("cne");
                            lieuDeNaissance1 = resultSet.getString("lieu_de_naissance");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Gérer les erreurs de la base de données
                }

                try {
                    // Utiliser la connexion à la base de données existante
                    Connection conn = FormHome.conn();

                    // Exécuter la requête pour récupérer les informations de l'étudiant
                  String query = "SELECT id,nom_entreprise, date_debut_stage, date_fin_stage, type_stage, encadrant_academique, encadrant_professionnel\n"
        + "FROM demandes\n"
        + "WHERE etudiant_id = ? AND type_document = ?;";


                    try ( PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                        preparedStatement.setInt(1, etudiantId);
                        preparedStatement.setString(2, typeDemande);
                        ResultSet rs = preparedStatement.executeQuery();

                        // Vérifier s'il y a des résultats
                        if (rs.next()) {
                            iddoc = rs.getInt("id");
                            nomEntreprise = rs.getString("nom_entreprise");
                            dateDebut = rs.getDate("date_debut_stage");
                            dateFin = rs.getDate("date_fin_stage");
                            typeStage = rs.getString("type_stage");
                            encadrantAcademique =rs.getString("encadrant_academique");
                            encadrantProfessionnel = rs.getString("encadrant_professionnel");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Gérer les erreurs de la base de données
                }

                // Générer le PDF avec toutes les informations récupérées
                RefuseButtonhandeler.generatePDF(etudiantId, typeDemande, nom, prenom, Niveau, apogee, email, nomEntreprise, dateDebut, dateFin, typeStage,cin1,cne1, dateDeNaissance1, lieuDeNaissance1, encadrantAcademique , encadrantProfessionnel,iddoc);

                // Send an email with the generated PDF
                String toEmail = RefuseButtonhandeler.getColumnEmail(table2, row, 3);
                RefuseButtonhandeler.sendApprovalByEmail(toEmail, etudiantId,iddoc);

                // Update database and remove row as before
                RefuseButtonhandeler.updateDatabase(table2 , etudiantId, typeDemande, "Approuvée");
   
    // Add your resend logic here
}

        };
       

        table2.addTableStyle(jScrollPane1);
        conn();
        loadDataIntoTable(""); // Load data into the table
        init();
        setOpaque(false);
        table2.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender1());
        table2.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor1(event));

    }
    
    int DocumentCountci1r = 0;
    int DocumentCountci2r = 0;
    int DocumentCountci3r = 0;
    int DocumentCount2ap1r = 0;
    int DocumentCount2ap2r = 0;

    int DocumentCountci1a = 0;
    int DocumentCountci2a = 0;
    int DocumentCountci3a = 0;
    int DocumentCount2ap1a = 0;
    int DocumentCount2ap2a = 0;

    public static Connection conn() {
        try {
            // JDBC URL, username, and password of MySQL server
            String jdbcURL = "jdbc:mysql://localhost:3308/ensaspace";
            String username = "root";
            String password = "";

            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            Connection conn = DriverManager.getConnection(jdbcURL, username, password);
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            // Handle any errors that may have occurred.
            e.printStackTrace();
            System.err.println("Error: Unable to establish the database connection.");
        }
        return null;
    }

    private void loadDataIntoTable(String where, Object... search) {
    DefaultTableModel model = (DefaultTableModel) table2.getModel();
    model.setRowCount(0);
        try {
            // SQL query to retrieve data from the "demandes" table
            String sql = "SELECT type_document,etudiant_id,etat_demande FROM demandes WHERE etat_demande!='En Attente'";

            // Execute the query and get the result set
            try (
                     Connection conn = FormDocument.conn();  PreparedStatement preparedStatement = conn.prepareStatement(sql);  ResultSet resultSet = preparedStatement.executeQuery()) {
                // Populate the table with data from the result set
                while (resultSet.next()) {

                    int etudiantId = resultSet.getInt("etudiant_id");
                    String typeDemande = resultSet.getString("type_document");
                    String etatDemande = resultSet.getString("etat_demande");
                    // Query to retrieve additional information from the "etudiants" table
                    String etudiantInfoQuery = "SELECT nom_etudiant,prenom_etudiant, Niveau, Apogee, email FROM etudiants WHERE id = ?"  + where;

                    // Execute the query to get additional information
                    try (
                            
                             PreparedStatement etudiantInfoStatement = conn.prepareStatement(etudiantInfoQuery)) {
                          for (int i = 0; i <search.length; i++) {
                              etudiantInfoStatement.setObject(i + 2, search[i]);
            }
                        etudiantInfoStatement.setInt(1, etudiantId);
                        ResultSet etudiantInfoResultSet = etudiantInfoStatement.executeQuery();

                        // Populate the table with additional data from the "etudiants" table
                        while (etudiantInfoResultSet.next()) {

                            String nom = etudiantInfoResultSet.getString("nom_etudiant");
                            String prenom = etudiantInfoResultSet.getString("prenom_etudiant");

                            String name = nom + " " + prenom;

                            String niveau = etudiantInfoResultSet.getString("Niveau");
                            int apogee = etudiantInfoResultSet.getInt("Apogee");
                            String email = etudiantInfoResultSet.getString("email");

                            if (etatDemande.equals("Refusée")) {

                                switch (niveau) {
                                    case "CI1" ->
                                        DocumentCountci1r++; // Increment the counter
                                    case "2ap1" ->
                                        DocumentCount2ap1r++;
                                    case "2ap2" ->
                                        DocumentCount2ap2r++;
                                    case "GI2", "GC2", "GSTR2", "SCM2" ->
                                        DocumentCountci2r++;
                                    default ->
                                        DocumentCountci3r++;
                                }
                            } else {
                                switch (niveau) {
                                    case "CI1" ->
                                        DocumentCountci1a++; // Increment the counter
                                    case "2ap1" ->
                                        DocumentCount2ap1a++;
                                    case "2ap2" ->
                                        DocumentCount2ap2a++;
                                    case "GI2", "GC2", "GSTR2", "SCM2" ->
                                        DocumentCountci2a++;
                                    default ->
                                        DocumentCountci3a++;

                                }
                            }

                            // Add a new row to the table with the retrieved data
                            ((DefaultTableModel) table2.getModel()).addRow(new Object[]{name, niveau, apogee, email, typeDemande, etatDemande});
                            ((DefaultTableModel) table2.getModel()).fireTableDataChanged();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            // Handle any errors that may have occurred.
            e.printStackTrace();
        }
    }

    private void init() {
        chart.addLegend("Refusée", new Color(12, 84, 175), new Color(0, 108, 247));
        chart.addLegend("Approuver", new Color(200, 200, 200), new Color(100, 100, 100));

        chart.addData(new ModelChart("2ap1", new double[]{DocumentCount2ap1r, DocumentCount2ap1a}));
        chart.addData(new ModelChart("2ap2", new double[]{DocumentCount2ap2r, DocumentCount2ap2a}));
        chart.addData(new ModelChart("CI1", new double[]{DocumentCountci1r, DocumentCountci1a}));
        chart.addData(new ModelChart("CI2", new double[]{DocumentCountci2r, DocumentCountci2a}));
        chart.addData(new ModelChart("CI3", new double[]{DocumentCountci3r, DocumentCountci3a}));
        chart.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        roundPanel1 = new com.swing.RoundPanel();
        chart = new com.chart.CurveChart();
        roundPanel2 = new com.swing.RoundPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table2 = new com.swing.Table();
        txt3 = new com.textfield.TextFieldSearchOption();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(234, 234, 234));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        roundPanel1.setBackground(new java.awt.Color(60, 60, 60));

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
                .addContainerGap())
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
        );

        roundPanel2.setBackground(new java.awt.Color(60, 60, 60));

        table2.setBackground(new java.awt.Color(60, 60, 60));
        table2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nom", "Niveau", "Apogee", "Email", "Type_Demande", "Etat_demande", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table2.setSelectionBackground(new java.awt.Color(0, 0, 0));
        table2.getTableHeader().setReorderingAllowed(false);
        table2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                table2KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(table2);
        if (table2.getColumnModel().getColumnCount() > 0) {
            table2.getColumnModel().getColumn(0).setResizable(false);
            table2.getColumnModel().getColumn(0).setPreferredWidth(80);
            table2.getColumnModel().getColumn(1).setResizable(false);
            table2.getColumnModel().getColumn(2).setResizable(false);
            table2.getColumnModel().getColumn(3).setResizable(false);
            table2.getColumnModel().getColumn(3).setPreferredWidth(110);
            table2.getColumnModel().getColumn(4).setResizable(false);
            table2.getColumnModel().getColumn(4).setPreferredWidth(140);
            table2.getColumnModel().getColumn(5).setResizable(false);
            table2.getColumnModel().getColumn(6).setPreferredWidth(70);
        }

        txt3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt3ActionPerformed(evt);
            }
        });
        txt3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt3KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout roundPanel2Layout = new javax.swing.GroupLayout(roundPanel2);
        roundPanel2.setLayout(roundPanel2Layout);
        roundPanel2Layout.setHorizontalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
                    .addGroup(roundPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        roundPanel2Layout.setVerticalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(roundPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(roundPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void table2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_table2KeyReleased
        // TODO add your hand if (txt.isSelected()) {
  
    }//GEN-LAST:event_table2KeyReleased

    private void txt3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt3ActionPerformed

    private void txt3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt3KeyReleased
  int option = txt3.getSelectedIndex();
    String text = "%" + txt3.getText().trim() + "%";
    if (option == 0) {
       loadDataIntoTable("  AND nom_etudiant LIKE ?", text);
   } else if (option == 1) {
        loadDataIntoTable("  AND Niveau LIKE ?", text);
   } else if (option == 2) {
       loadDataIntoTable("  AND Email LIKE ?", text);
   }  
    
 else if (option == 3) {
    DefaultTableModel model = (DefaultTableModel) table2.getModel();
    model.setRowCount(0);
        try {
            // SQL query to retrieve data from the "demandes" table
            String sql = "SELECT type_document,etudiant_id,etat_demande FROM demandes WHERE etat_demande!='En Attente' AND type_document LIKE ? ";

            // Execute the query and get the result set
            try (
                     Connection conn = FormDocument.conn();  PreparedStatement preparedStatement = conn.prepareStatement(sql);  
                   ) {
                
                preparedStatement.setString(1, text);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                
                // Populate the table with data from the result set
                while (resultSet.next()) {

                    int etudiantId = resultSet.getInt("etudiant_id");
                    String typeDemande = resultSet.getString("type_document");
                    String etatDemande = resultSet.getString("etat_demande");
                    // Query to retrieve additional information from the "etudiants" table
                    String etudiantInfoQuery = "SELECT nom_etudiant,prenom_etudiant, Niveau, Apogee, email FROM etudiants WHERE id = ? " ;

                    // Execute the query to get additional information
                    try (
                            
                             PreparedStatement etudiantInfoStatement = conn.prepareStatement(etudiantInfoQuery)) {
              
                        etudiantInfoStatement.setInt(1, etudiantId);
                        ResultSet etudiantInfoResultSet = etudiantInfoStatement.executeQuery();

                        // Populate the table with additional data from the "etudiants" table
                        while (etudiantInfoResultSet.next()) {

                            String nom = etudiantInfoResultSet.getString("nom_etudiant");
                            String prenom = etudiantInfoResultSet.getString("prenom_etudiant");

                            String name = nom + " " + prenom;

                            String niveau = etudiantInfoResultSet.getString("Niveau");
                            int apogee = etudiantInfoResultSet.getInt("Apogee");
                            String email = etudiantInfoResultSet.getString("email");

                            if (etatDemande.equals("Refusée")) {

                                switch (niveau) {
                                    case "CI1" ->
                                        DocumentCountci1r++; // Increment the counter
                                    case "2ap1" ->
                                        DocumentCount2ap1r++;
                                    case "2ap2" ->
                                        DocumentCount2ap2r++;
                                    case "GI2", "GC2", "GSTR2", "SCM2" ->
                                        DocumentCountci2r++;
                                    default ->
                                        DocumentCountci3r++;
                                }
                            } else {
                                switch (niveau) {
                                    case "CI1" ->
                                        DocumentCountci1a++; // Increment the counter
                                    case "2ap1" ->
                                        DocumentCount2ap1a++;
                                    case "2ap2" ->
                                        DocumentCount2ap2a++;
                                    case "GI2", "GC2", "GSTR2", "SCM2" ->
                                        DocumentCountci2a++;
                                    default ->
                                        DocumentCountci3a++;

                                }
                            }

                            // Add a new row to the table with the retrieved data
                            ((DefaultTableModel) table2.getModel()).addRow(new Object[]{name, niveau, apogee, email, typeDemande, etatDemande});
                            ((DefaultTableModel) table2.getModel()).fireTableDataChanged();
                        }
                    }
                }
            }
            }
        } catch (SQLException e) {
            // Handle any errors that may have occurred.
            e.printStackTrace();
        }}// TODO add your handling code here:
    }//GEN-LAST:event_txt3KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.chart.CurveChart chart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.swing.RoundPanel roundPanel1;
    private com.swing.RoundPanel roundPanel2;
    private com.swing.Table table2;
    private com.textfield.TextFieldSearchOption txt3;
    // End of variables declaration//GEN-END:variables
}
