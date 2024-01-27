/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.form;

import com.cell.RefuseButtonhandeler;
import com.cell.TableActionCellEditor;
import com.cell.TableActionCellRender;
import com.cell.TableActionEvent;
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
import javax.swing.ImageIcon;

import javax.swing.table.DefaultTableModel;

public class FormReclamations extends javax.swing.JPanel {

    public FormReclamations() {
        initComponents();
         txt1.addEventOptionSelected(new SearchOptinEvent() {
            @Override
            public void optionSelected(SearchOption option, int index) {
                txt1.setHint("Search by " + option.getName() + "...");
            }
        });
          txt1.addOption(new SearchOption("Name", new ImageIcon(getClass().getResource("/com/images/user.png"))));
          txt1.addOption(new SearchOption("Niveau", new ImageIcon(getClass().getResource("/com/images/niveau.png"))));
          txt1.addOption(new SearchOption("Email", new ImageIcon(getClass().getResource("/com/images/email.png"))));
          txt1.addOption(new SearchOption("Type_Demande", new ImageIcon(getClass().getResource("/com/images/documents.png"))));

        table3.addTableStyle(jScrollPane1);
        conn();
        loadDataIntoTable(""); // Load data into the table
        init();
        setOpaque(false);
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void Onrefuse(int row) {
                int apogee = RefuseButtonhandeler.getApogeeFromRow(table3, row, 2);
                String typeDemande = RefuseButtonhandeler.getTypeDemandeFromRow(table3, row, 4);
                int id = RefuseButtonhandeler.getEtudiantIdFromApogee(table3, 2, row);
                RefuseButtonhandeler.updateDatabase(table3, id, typeDemande, "Refusée");

                // Prompt the user for input
                String reason = promptUserForReason();

                String toEmail = RefuseButtonhandeler.getColumnEmail(table3, row, 3);

                // Send the refusal email
                RefuseButtonhandeler.sendRefusalByEmail(toEmail, reason);

                // Remove the row from the table
                ((DefaultTableModel) table3.getModel()).removeRow(row);
            
            }

            @Override
            public void Onapprove(int row) {
               
                int etudiantId = RefuseButtonhandeler.getEtudiantIdFromApogee(table3, 2, row);
                String typeDemande = RefuseButtonhandeler.getTypeDemandeFromRow(table3, row, 4);
                int demandeIdToDelete =RefuseButtonhandeler.getIdFromreclamTable(etudiantId, typeDemande);
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
                String toEmail = RefuseButtonhandeler.getColumnEmail(table3, row, 3);
                RefuseButtonhandeler.sendApprovalByEmail(toEmail, etudiantId,iddoc);

                // Update database and remove row as before
                RefuseButtonhandeler.updateDatabase(table3, etudiantId, typeDemande, "Approuvée");
                RefuseButtonhandeler.deleteFromDatabase(demandeIdToDelete);
                ((DefaultTableModel) table3.getModel()).removeRow(row);
            }

            @Override
            public void Ondownload(int row) {
               try {
                    // Call the method to generate the PDF
                    int etudiantId = RefuseButtonhandeler.getEtudiantIdFromApogee(table3, 2, row);
                    String typeDemande = RefuseButtonhandeler.getTypeDemandeFromRow(table3, row, 4);

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
                }
            }
        };
        table3.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        table3.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(event));
      

    }
    private String promptUserForReason() {
        // Show a dialog to prompt the user for input
        return javax.swing.JOptionPane.showInputDialog(this, "Enter refusal reason:");
    }
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
    int DocumentCountci1 = 0;
    int DocumentCountci2 = 0;
    int DocumentCountci3 = 0;
    int DocumentCount2ap1 = 0;
    int DocumentCount2ap2 = 0;

   private void loadDataIntoTable(String where, Object... search) {
            DefaultTableModel model = (DefaultTableModel) table3.getModel();
    model.setRowCount(0);
        try {
            // SQL query to retrieve data from the "demandes" table
            String sql = "SELECT id_etudiant, motif_reclamation, Type, date_soumission_reclamation FROM reclamation";

            // Execute the query and get the result set
            try (
                     Connection conn = FormReclamations.conn();  PreparedStatement preparedStatement = conn.prepareStatement(sql);  ResultSet resultSet = preparedStatement.executeQuery()) {
                // Populate the table with data from the result set
                while (resultSet.next()) {

                    int etudiantId = resultSet.getInt("id_etudiant");
                    String typeDemande = resultSet.getString("Type");
                    String motif = resultSet.getString("motif_reclamation");

                    String etudiantInfoQuery = "SELECT nom_etudiant, prenom_etudiant,Niveau, Apogee, email FROM etudiants WHERE id = ?" + where;

                    // Execute the query to get additional information
                    try (
                             PreparedStatement etudiantInfoStatement = conn.prepareStatement(etudiantInfoQuery)) {
                        etudiantInfoStatement.setInt(1, etudiantId);
                         for (int i = 0; i <search.length; i++) {
                              etudiantInfoStatement.setObject(i + 2, search[i]);
            }
                        ResultSet etudiantInfoResultSet = etudiantInfoStatement.executeQuery();
             
                        // Populate the table with additional data from the "etudiants" table
                        while (etudiantInfoResultSet.next()) {

                            

                            String nom = etudiantInfoResultSet.getString("nom_etudiant");
                            String prenom = etudiantInfoResultSet.getString("prenom_etudiant");

                            String name = nom + " " + prenom;

                            String niveau = etudiantInfoResultSet.getString("Niveau");
                            int apogee = etudiantInfoResultSet.getInt("Apogee");
                            String email = etudiantInfoResultSet.getString("email");

                            switch (niveau) {
                                case "CI1" ->
                                    DocumentCountci1++; // Increment the counter
                                case "2ap1" ->
                                    DocumentCount2ap1++;
                                case "2ap2" ->
                                    DocumentCount2ap2++;
                                case "GI2", "GC2", "GSTR2", "SCM2" ->
                                    DocumentCountci2++;
                                default ->
                                    DocumentCountci3++;
                            }
                            // Add a new row to the table with the retrieved data
                            ((DefaultTableModel) table3.getModel()).addRow(new Object[]{name, niveau, apogee, email, typeDemande, motif});
                            ((DefaultTableModel) table3.getModel()).fireTableDataChanged();
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
        chart.addLegend("Reclamations", new Color(255, 0, 0), new Color(255, 0, 0));

        chart.addData(new ModelChart("2ap1", new double[]{DocumentCount2ap1, 0}));
        chart.addData(new ModelChart("2ap2", new double[]{DocumentCount2ap2, 0}));
        chart.addData(new ModelChart("CI1", new double[]{DocumentCountci1, 0}));
        chart.addData(new ModelChart("CI2", new double[]{DocumentCountci2, 0}));
        chart.addData(new ModelChart("CI3", new double[]{DocumentCountci3, 0}));
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
        table3 = new com.swing.Table();
        txt1 = new com.textfield.TextFieldSearchOption();

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
                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                .addContainerGap())
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
        );

        roundPanel2.setBackground(new java.awt.Color(60, 60, 60));

        table3.setBackground(new java.awt.Color(60, 60, 60));
        table3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nom", "Niveau", "Apogee", "Email", "Type_Demande", "motif", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table3.setSelectionBackground(new java.awt.Color(0, 0, 0));
        table3.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table3);
        if (table3.getColumnModel().getColumnCount() > 0) {
            table3.getColumnModel().getColumn(0).setPreferredWidth(60);
            table3.getColumnModel().getColumn(1).setPreferredWidth(30);
            table3.getColumnModel().getColumn(2).setPreferredWidth(30);
            table3.getColumnModel().getColumn(3).setPreferredWidth(120);
            table3.getColumnModel().getColumn(4).setPreferredWidth(120);
            table3.getColumnModel().getColumn(6).setPreferredWidth(70);
        }

        txt1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout roundPanel2Layout = new javax.swing.GroupLayout(roundPanel2);
        roundPanel2.setLayout(roundPanel2Layout);
        roundPanel2Layout.setHorizontalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel2Layout.createSequentialGroup()
                .addGroup(roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundPanel2Layout.createSequentialGroup()
                        .addContainerGap(506, Short.MAX_VALUE)
                        .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        roundPanel2Layout.setVerticalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(roundPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(roundPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt1KeyReleased
   if (txt1.isSelected()) {
    int option = txt1.getSelectedIndex();
    String text = "%" + txt1.getText().trim() + "%";
    if (option == 0) {
        loadDataIntoTable("  AND nom_etudiant LIKE ?", text);
    } else if (option == 1) {
        loadDataIntoTable("  AND Niveau LIKE ?", text);
    } else if (option == 2) {
        loadDataIntoTable("  AND Email LIKE ?", text);
    }
    else if (option == 3) {
               DefaultTableModel model = (DefaultTableModel) table3.getModel();
                model.setRowCount(0);
        try {
            // SQL query to retrieve data from the "demandes" table
            String sql = "SELECT id_etudiant, motif_reclamation, Type, date_soumission_reclamation FROM reclamation WHERE Type LIKE ?";
            
            // Execute the query and get the result set
            try (
                     Connection conn = FormReclamations.conn();  
                    PreparedStatement preparedStatement = conn.prepareStatement(sql); ){
                    
                preparedStatement.setString(1, text);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Populate the table with data from the result set
                while (resultSet.next()) {

                    int etudiantId = resultSet.getInt("id_etudiant");
                    String typeDemande = resultSet.getString("Type");
                    String motif = resultSet.getString("motif_reclamation");

                    String etudiantInfoQuery = "SELECT nom_etudiant, prenom_etudiant,Niveau, Apogee, email FROM etudiants WHERE id = ?" ;

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

                        
                            // Add a new row to the table with the retrieved data
                            ((DefaultTableModel) table3.getModel()).addRow(new Object[]{name, niveau, apogee, email, typeDemande, motif});
                            ((DefaultTableModel) table3.getModel()).fireTableDataChanged();
                        }
                    }
                }
            }
            }
        } catch (SQLException e) {
            // Handle any errors that may have occurred.
            e.printStackTrace();
        }
    
    }
   }
    }//GEN-LAST:event_txt1KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.chart.CurveChart chart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.swing.RoundPanel roundPanel1;
    private com.swing.RoundPanel roundPanel2;
    private com.swing.Table table3;
    private com.textfield.TextFieldSearchOption txt1;
    // End of variables declaration//GEN-END:variables
}
