package com.cell;

import com.form.FormHome;
import java.io.IOException;
import com.form.FormDocument;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JTable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.time.Year;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.time.Clock;

public class RefuseButtonhandeler {

    public static int getApogeeFromRow(JTable table, int row, int column) {
        return Integer.parseInt(table.getValueAt(row, column).toString());
    }

    public static int getEtudiantIdFromApogee(JTable table, int apogeeColumnIndex, int row) {
        try {
            int apogee = getApogeeFromRow(table, row, apogeeColumnIndex);
            // SQL query to retrieve the ID from the "etudiants" table based on the apogee
            String sql = "SELECT id FROM etudiants WHERE Apogee = ?";
            try (
                     Connection conn = FormDocument.conn();  PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, apogee);
                try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getIdFromDemandesTable(int etudiantId, String typeDemande) {
        try {
            // SQL query to retrieve the ID from the "demandes" table based on etudiantId and typeDemande
            String sql = "SELECT id FROM demandes WHERE etudiant_id = ? AND type_document = ?";
            try (
                     Connection conn = FormDocument.conn();  PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, etudiantId);
                preparedStatement.setString(2, typeDemande);
                try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no matching row is found
    }

    public static int getIdFromreclamTable(int etudiantId, String typeDemande) {
        try {
            // SQL query to retrieve the ID from the "demandes" table based on etudiantId and typeDemande
            String sql = "SELECT id FROM reclamation WHERE id_etudiant = ? AND type = ?";
            try (
                     Connection conn = FormDocument.conn();  PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, etudiantId);
                preparedStatement.setString(2, typeDemande);
                try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no matching row is found
    }

    public static String getTypeDemandeFromRow(JTable table, int row, int column) {
        return table.getValueAt(row, column).toString();
    }

    public static void updateDatabase(JTable table, int id, String typeDemande, String newState) {
        try {
            int etudiantIdColumnIndex = 2;  // Replace with the actual column index
            int typeDocumentColumnIndex = 4;  // Replace with the actual column index

            // SQL query to update the "demandes" table with the new state
            String sql = "UPDATE demandes SET etat_demande = ? WHERE etudiant_id = ? AND type_document = ?";
            try (
                     Connection conn = FormDocument.conn();  PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, newState);
                preparedStatement.setInt(2, id);
                preparedStatement.setString(3, typeDemande);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFromDatabase(int id) {
        try {
            // SQL query to delete a row from the "demandes" table based on the ID
            String sql = "DELETE FROM reclamation WHERE id = ?";
            try (
                     Connection conn = FormDocument.conn();  PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getColumnEmail(JTable table, int row, int column) {
        Object emailValue = table.getValueAt(row, column);

        return emailValue != null ? emailValue.toString() : "";
    }

    public static void sendRefusalByEmail(String toEmail, String reason) {
        // Configure properties for the SMTP connection (replace with your SMTP server information)
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Replace with your SMTP server
        props.put("mail.smtp.port", "587"); // SMTP Port
        props.put("mail.smtp.auth", "true"); // SMTP Authentication
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS encryption

        // Create a session with authentication information
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ensatetouann@gmail.com", "mvlxanwobpomazqb");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ensatetouann@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Votre demande a été refusée");
            message.setText(reason);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendApprovalByEmail(String toEmail, int etudiantId, int iddoc) {
        // Configure properties for the SMTP connection (replace with your SMTP server information)
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Replace with your SMTP server
        props.put("mail.smtp.port", "587"); // SMTP Port
        props.put("mail.smtp.auth", "true"); // SMTP Authentication
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS encryption

        // Create a session with authentication information
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ensatetouann@gmail.com", "mvlxanwobpomazqb");
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ensatetouann@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Votre demande a été approuvée");
            message.setText("Votre demande a été approuvée. Veuillez trouver le document attaché.");

            // Attach the generated PDF to the email
            String filePath = "/Users/user/Desktop/projet_GL/Ensate/src/com/files/" + etudiantId + iddoc + "_Document.pdf";
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(filePath);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void generatePDF(int etudiantId, String typeDemande, String nom, String prenom, String Niveau, int apogee, String email, String nomEntreprise, Date dateDebut, Date dateFin, String typeStage, String cin, String cne, String date_naissance, String lieu_naissance, String encadrantAcademique, String encadrantProfessionnel, int iddoc) {
        try {
           
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();

           
            String filePath = "/Users/user/Desktop/projet_GL/Ensate/src/com/files/" + etudiantId + iddoc + "_Document.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(filePath));


            // Customize the content based on the type of document
            if (typeDemande.equals("ATTESTATION DE SCOLARITE")) {
                document.open();

                try {
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date date = new java.util.Date();
                    LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
                   
                    Image img = Image.getInstance("/Users/user/Desktop/projet_GL/Ensate/src/com/images/ENSA2.PNG");
                    img.scaleAbsolute(400, 70);
                    img.setAlignment(Element.ALIGN_CENTER);

                    Font f1 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD | Font.UNDERLINE);
                    Font f2 = new Font(Font.FontFamily.UNDEFINED, 11);
                    Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
                    Font f4 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.RED);
                    Font f5 = new Font(Font.FontFamily.TIMES_ROMAN, 11);
                    Paragraph p1, p2, p3, p4, p5, p6, p7;
                    p4 = (new Paragraph("\n"));
                    p1 = (new Paragraph("ATTESTATION DE SCOLARITE", f1));
                    p1.setAlignment(Element.ALIGN_CENTER);
                    p2 = (new Paragraph("\n\nLe Directeur de l'Ecole Nationale des Sciences Appliquées atteste que l'étudiant(e):\n\n " + nom + " " + prenom + "\n\nNuméro de la carte d'identité nationale : " + cin + "\n\nCode national de l'étudiant : " + cne + "\n\nNé(e) le : " + date_naissance + " à " + lieu_naissance + "\n\nPoursuit ses études à l'Ecole Nationale des Sciences Appliquées pour l'année universitaire : " + Year.now().minusYears(1) + "/" + Year.now() + ".\n\n\n", f2));
                    p3 = (new Paragraph("                                 Fait à Tétouan le : " + formatter1.format(date) + "\n\n\nLe directeur", f3));
                    p3.setAlignment(Element.ALIGN_CENTER);
                    p6 = (new Paragraph("  Adresse : M'HANNECH || B.P.2222 Tétouan\n\n                  Tél : 0539968802,FAX:0539994624", f5));
                    p5 = (new Paragraph("\n\n"));
                    Image img2 = Image.getInstance("/Users/user/Desktop/projet_GL/Ensate/src/com/images/cachet.PNG");
                    img2.setAlignment(Element.ALIGN_CENTER);
                    p7 = (new Paragraph("Le présent document n'est délivré qu'en un seul exemplaire.\nIl appartient à l'étudiant d'en faire des photocopies certifiées conformes.", f4));
                    p7.setAlignment(Element.ALIGN_CENTER);
                    img2.scaleAbsolute(250, 110);
                    img2.setAlignment(Element.ALIGN_CENTER);
                    document.add(img);
                    document.add(p1);
                    document.add(p2);
                    document.add(p5);
                    document.add(p3);
                    document.add(img2);
                    document.add(new Chunk(line));
                    document.add(p6);
                    document.add(new Chunk(line));
                    document.add(p7);
                    com.itextpdf.text.Rectangle rect = new com.itextpdf.text.Rectangle(577, 825, 18, 15);
                    rect.enableBorderSide(1);
                    rect.enableBorderSide(2);
                    rect.enableBorderSide(4);
                    rect.enableBorderSide(8);
                    rect.setBorderColor(BaseColor.BLACK);
                    rect.setBorderWidth(1);
                    document.add(rect);
                    document.close();
                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                } catch (DocumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

                // Add more content specific to Attestation if needed
            } else if (typeDemande.equals("Attestation De Réussite")) {
                document.open();
                try {

                    java.util.Date date = new java.util.Date();
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                    LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
                    Image img = Image.getInstance("/Users/user/Desktop/projet_GL/Ensate/src/com/images/at.png");
                    img.scaleAbsolute(400, 70);
                    img.setAlignment(Element.ALIGN_CENTER);
                    Font f1 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
                    Font f2 = new Font(Font.FontFamily.UNDEFINED, 12);
                    Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
                    Font f4 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.RED);
                    Paragraph p1, p2, p3, p4, p5;
                    p1 = (new Paragraph("\n\n\n ATTESTATION DE REUSSITE", f1));
                    p1.setAlignment(Element.ALIGN_CENTER);
                    p2 = (new Paragraph("\n\n\n           Le Directeur de l'Ecole Nationale des Sciences Appliquées atteste que l'étudiant(e):\n\n           " + prenom + " " + nom + " de Numero Apoge : " + apogee + " a été déclaré(e) admis(e) au niveau : \n\n           "
                            + Niveau + " au titre de l'année universitaire :\n\n           " + "2022" + "-" + ("2023   ") + ".", f2));
                    p3 = (new Paragraph("\n\nFait à Tétouan le " + formatter1.format(date) + "\n\nDirecteur\n\n\n Kamal Reklaoui", f3));
                    p5 = (new Paragraph("\n"));
                    Image img2 = Image.getInstance("/Users/user/Desktop/projet_GL/Ensate/src/com/images/cachet.png");
                    img2.scaleAbsolute(150, 100);
                    img2.setAlignment(Element.ALIGN_CENTER);
                    p4 = (new Paragraph("Avis important: Il ne peut être délivré qu'un seul exemplaire. Aucun duplicate ne sera fourni", f4));
                    p3.setAlignment(Element.ALIGN_CENTER);
                    p4.setAlignment(Element.ALIGN_CENTER);
                    document.add(img);
                    document.add(p1);
                    document.add(p2);
                    document.add(p3);
                    document.add(p5);
                    document.add(img2);
                    document.add(p5);
                    document.add(p5);
                    document.add(new Chunk(line));
                    document.add(p4);
                    com.itextpdf.text.Rectangle rect = new com.itextpdf.text.Rectangle(577, 825, 18, 15);
                    rect.enableBorderSide(1);
                    rect.enableBorderSide(2);
                    rect.enableBorderSide(4);
                    rect.enableBorderSide(8);
                    rect.setBorderColor(BaseColor.BLACK);
                    rect.setBorderWidth(1);
                    document.add(rect);
                    document.close();

                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                } catch (DocumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            } else if (typeDemande.equals("Convention de Stage")) {

                try {
                    document.open();
                    Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date date = new java.util.Date();
                    Clock clock = Clock.systemDefaultZone();
                    Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 10);
                    com.itextpdf.text.Rectangle rect = new com.itextpdf.text.Rectangle(577, 825, 18, 15);
                    rect.enableBorderSide(1);
                    rect.enableBorderSide(2);
                    rect.enableBorderSide(4);
                    rect.enableBorderSide(8);
                    rect.setBorderColor(BaseColor.BLACK);
                    rect.setBorderWidth(1);
                    document.add(rect);
                    Image img = Image.getInstance("/Users/user/Desktop/projet_GL/Ensate/src/com/images/at.png");
                    img.scaleAbsolute(550, 80);
                    img.setAlignment(Image.ALIGN_CENTER);
                    document.add(img);
                    LineSeparator ls3 = new LineSeparator();
                    document.add(new Chunk(ls3));
                    document.add(new Paragraph("\n"));
                    document.add(new Paragraph("                                                                         CONVENTION DE STAGE ", boldFont));
                    document.add(new Paragraph("                                             " + nomEntreprise + " / Ecole Nationale  des Sciences Appliquées de Tétouan\n\n\n", boldFont));
                    document.add(new Paragraph("  Entreprise :                  " + nomEntreprise + "\n", boldFont));
                    document.add(new Paragraph("  Représentée par son Directeur :\n"));
                    document.add(new Paragraph("                                                                                                                                                                  D'une part\n\n", boldFont));
                    document.add(new Paragraph(" l’Ecole Nationale  des Sciences Appliquées de Tétouan.", boldFont));
                    document.add(new Paragraph(" Représentée par : " + "Mr. Mostafa KAMAL REKLAOUI de l’école\n", boldFont));
                    document.add(new Paragraph("                                                                                                                                                                  D'autre part\n\n", boldFont));
                    document.add(new Paragraph(" Il a été convenu ce qui suit : \n\n"));
                    document.add(new Paragraph(" ARTICLE 1", boldFont));
                    document.add(new Paragraph("     La présente convention régit les rapports des deux parties, dans le cadre de l’organisation de stages d‘ entreprises qui s’inscrivent dans le programme de formation de l’établissement de formation cité ci-dessus.\n\n"));
                    document.add(new Paragraph(" ARTICLE 2", boldFont));
                    document.add(new Paragraph("     Le stage de formation a pour but essentiel d'assurer l‘ application pratique de l’enseignement donné par I’ établissement, et d’élaborer un projet sur un thème proposé par l’entreprise.\n\n"));
                    document.add(new Paragraph(" ARTICLE 3", boldFont));
                    document.add(new Paragraph("     Le programme du stage est élaboré par le personnel charge de l’encadrement du stagiaire. En tenant compte du programme et de la spécialité des études de l’élève, ainsi que  de la disponibilité des moyens humains et matériel de l’entreprise. Cette dernière se réserve le droit de réorienter l’apprentissage en fonction des qualifications du stagiaire et du rythme de ses activités professionnelles.\n\n"));
                    document.add(new Paragraph(" ARTICLE 4", boldFont));
                    document.add(new Paragraph("     Pendant le stage, I ’élève est soumis aux usages et règlements de l’entreprise, notamment en matière de discipline, des horaires et des congés. En cas de manquement à ces règles, le chef d’entreprise se réserve le droit de mettre fin au stage, après avoir prévenu le Directeur de l'EcoIe.\n"));

                    LineSeparator ls = new LineSeparator();
                    document.add(new Chunk(ls));
                    document.add(new Paragraph("                              Ecole Nationale des Sciences Appliquées de Tétouan. B.P 2222 M’hannech II — Tétouan \n"
                            + "                                                           Tél. 053688027; Fax. 0539994624; www.ensate.uae.ma\n\n", footerFont));
                    document.add(img);
                    LineSeparator ls4 = new LineSeparator();
                    document.add(new Chunk(ls4));
                    document.add(new Paragraph(" \nARTICLE 5", boldFont));
                    document.add(new Paragraph("     Au terme de son stage, le stagiaire est dans l’obligation de remettre une copie de son rapport de stage à l’entreprise.\n\n"));
                    document.add(new Paragraph(" ARTICLE 6", boldFont));
                    document.add(new Paragraph("     L’élève ingénieur s’engage à  garder confidentielle toute information recueillie dans l’entreprise, et à n’utiliser en aucun cas ces informations pour faire l’objet d’une publication, communication à des tiers, conférences, …, sans l’accord de l’entreprise.\n\n"));
                    document.add(new Paragraph(" ARTICLE 7", boldFont));
                    document.add(new Paragraph("     En cas de non-respect de l’une des clauses de cette convention, aussi bien par l’élève stagiaire que son établissement d’origine, l’entreprise se réserve le droit de mettre fin à ce stage.\n\n"));
                    document.add(new Paragraph(" ARTICLE 8", boldFont));
                    document.add(new Paragraph("     Dans le cadre de la présente convention, l’entreprise " + nomEntreprise + "  accepte de recevoir l’élève ingénieur - stagiaire : " + nom + " " + prenom + ", inscrit à l'ENSA de Tétouan pendant l’année universitaire " + Year.now().minusYears(1) + "-" + Year.now(clock) + " en " + Niveau + ". Pour la période allant du " + dateDebut + " au " + dateFin + ".\n\n"));

                    document.add(new Paragraph("ARTICLE 9 : Encadrement\n\n", boldFont));

                    document.add(new Paragraph("L'encadrement académique sera assuré par M. / Mme " + encadrantAcademique + ", enseignant à l'ENSA de Tétouan.\n\n"));

                    document.add(new Paragraph("L'encadrement professionnel sera assuré par M. / Mme " + encadrantProfessionnel + ", représentant de l'entreprise " + nomEntreprise + ".\n\n"));
                    document.add(new Paragraph("Fait à Tétouan, le " + formatter.format(date) + "\n\n"));
                    document.add(new Paragraph("Le Directeur de l’Entreprise                                    Le Directeur de l’Ecole\n\n"));

                    Image img1 = Image.getInstance("/Users/user/Desktop/projet_GL/Ensate/src/com/images/cachet.png");
                    img1.scaleAbsolute(190, 60);
                    img1.setAlignment(Image.ALIGN_RIGHT);
                    document.add(img1);
                    Paragraph p = new Paragraph();

                    LineSeparator ls1 = new LineSeparator();
                    document.add(new Chunk(ls1));
                    document.add(new Paragraph("                              Ecole Nationale des Sciences Appliquées de Tétouan. B.P 2222 M’hannech II — Tétouan \n"
                            + "                                                          Tél. 053688027; Fax. 0539994624; www.ensate.uae.ma", footerFont));
                    document.add(rect);
                    document.close();

                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                } catch (DocumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());;

                }
            } else if (typeDemande.equals("Relevé de note")) {

                Connection conn = FormDocument.conn();

                try {
                    String sqlQuery = "select module_1,module_2,module_3,module_4,module_5,module_6,module_7,module_8,module_9,module_10,module_11,module_12 from nouvelles_notes "
                            + "where nouvelles_notes.id_etudiant in (select demandes.etudiant_id from demandes where demandes.etudiant_id=" + etudiantId + "); ";

                    PreparedStatement ps;
                    ResultSet rs;
                    ps = conn.prepareStatement(sqlQuery);
                    rs = ps.executeQuery();
//        	rs.next();

//    		PreparedStatement prestmt2 = ConnectionDB.getConnnection().prepareStatement(sqlQuery2);
//    		ResultSet rs2= prestmt2.executeQuery();
//    		rs2.next();
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                    java.util.Date date = new java.util.Date();
                    String path = "static\\Document\\Releve_de_note\\Releve_de_note__" + prenom + "_" + nom + "_" + formatter.format(date) + ".pdf";
                    PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(path));
                    document.open();
                    Image img = Image.getInstance("/Users/user/Desktop/projet_GL/Ensate/src/com/images/ENSA2.PNG");
                    img.scaleAbsolute(400, 70);
                    img.setAlignment(Element.ALIGN_CENTER);
                    document.add(img);
                    LineSeparator ls = new LineSeparator();
                    Font f1 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE | Font.BOLD, BaseColor.RED);
                    Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE);
                    Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
                    Font f4 = new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD);
                    Font f5 = new Font(Font.FontFamily.UNDEFINED, 11);
                    Font f6 = new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD, BaseColor.BLUE);
                    Font f7 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.RED);
                    Font f8 = new Font(Font.FontFamily.UNDEFINED, 14, Font.BOLD);
                    Font f9 = new Font(Font.FontFamily.UNDEFINED, 10);
                    Paragraph p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12;
                    p1 = null;
                    p2 = null;
                    p3 = null;
                    p4 = null;
                    p5 = null;
                    p6 = null;
                    p7 = null;
                    p8 = null;
                    p9 = null;
                    p10 = null;
                    p11 = null;
                    p12 = null;

                    PdfPTable pdfPTable1 = new PdfPTable(3);

                    PdfPTable pdfPTable2 = new PdfPTable(2);
                    PdfPCell pdfPCell1 = null;
                    PdfPCell pdfPCell2 = null;
                    PdfPCell pdfPCell3 = null;
                    PdfPCell pdfPCell4 = null;
                    PdfPCell pdfPCell5 = null;
                    PdfPCell pdfPCell6 = null;
                    PdfPCell pdfPCell7 = null;
                    PdfPCell pdfPCell8 = null;
                    PdfPCell pdfPCell9 = null;
                    PdfPCell pdfPCell10 = null;
                    PdfPCell pdfPCell11 = null;
                    PdfPCell pdfPCell12 = null;

                    PdfPCell pdfPCell13 = null;
                    PdfPCell pdfPCell14 = null;
                    PdfPCell pdfPCell15 = null;

                    PdfPCell pdfPCell16 = null;
                    PdfPCell pdfPCell17 = null;
                    PdfPCell pdfPCell18 = null;

                    PdfPCell pdfPCell19 = null;
                    PdfPCell pdfPCell20 = null;
                    PdfPCell pdfPCell21 = null;

                    PdfPCell pdfPCell22 = null;
                    PdfPCell pdfPCell23 = null;
                    PdfPCell pdfPCell24 = null;
                    //LIGNE2
                    PdfPCell pdfPCell25 = null;
                    PdfPCell pdfPCell26 = null;
                    PdfPCell pdfPCell27 = null;

                    PdfPCell pdfPCell28 = null;
                    PdfPCell pdfPCell29 = null;
                    PdfPCell pdfPCell30 = null;

                    PdfPCell pdfPCell31 = null;
                    PdfPCell pdfPCell32 = null;
                    PdfPCell pdfPCell33 = null;

                    PdfPCell pdfPCell34 = null;
                    PdfPCell pdfPCell35 = null;
                    PdfPCell pdfPCell36 = null;

                    PdfPCell pdfPCell37 = null;
                    PdfPCell pdfPCell38 = null;
                    PdfPCell pdfPCell39 = null;

                    PdfPCell pdfPCellN01 = null;
                    PdfPCell pdfPCellN02 = null;
                    PdfPCell pdfPCellN12 = null;

                    PdfPCell pdfPCellN11 = null;

                    PdfPCell pdfPCellN21 = null;
                    PdfPCell pdfPCellN22 = null;

                    while (rs.next()) {
                        if (Niveau.equals("2ap1") || Niveau.equals("2AP1")) {
                            p1 = (new Paragraph(" Relevé des notes de la 1ère année", f2));
                        }
                        if (Niveau.equals("2AP2") || Niveau.equals("2ap2")) {
                            p1 = (new Paragraph(" Relevé des notes de la 2ème année", f2));
                        }
                        if (Niveau.equals("GI1") || Niveau.equals("GC1") || Niveau.equals("GM1") || Niveau.equals("GSTR1") || Niveau.equals("SCM1")) {
                            p1 = (new Paragraph(" Relevé des notes de la 1ère année cycle d'ingénieur", f2));
                        }
                        if (Niveau.equals("GI2") || Niveau.equals("GC2") || Niveau.equals("GM2") || Niveau.equals("GSTR2") || Niveau.equals("SCM2")) {
                            p1 = (new Paragraph(" Relevé des notes de la 2ème année cycle d'ingénieur", f2));
                        }
                        if (Niveau.equals("GI3") || Niveau.equals("GC3") || Niveau.equals("GM3") || Niveau.equals("GSTR3") || Niveau.equals("SCM3")) {
                            p1 = (new Paragraph(" Relevé des notes de la 3ème année cycle d'ingénieur", f2));
                        }
                        p2 = (new Paragraph(" Filière : " + Niveau, f1));
                        p3 = (new Paragraph(" Année universitaire : " + "2022" + "/" + "2023", f3));
                        p4 = (new Paragraph("\n L’élève Ingénieur :", f5));
                        p5 = (new Paragraph(" Nom et Prénom : " + nom + " " + prenom, f5));
                        p6 = (new Paragraph(" CNE           : " + cne, f5));
                        p7 = (new Paragraph(" Code Apogée     : " + apogee, f5));
                        p8 = (new Paragraph(" A obtenu les résultats suivants :", f5));
                        p9 = (new Paragraph("\n"));
                        p10 = (new Paragraph(" La présente attestation est délivrée à l’intéressé(e) pour servir et valoir ce que de droit.", f5));
                        p11 = (new Paragraph(" Fait à Tétouan, le : " + formatter1.format(date), f5));
                        p12 = (new Paragraph(" AC : Acquis par Compensation                                                           NV : Non Validé\n N.B. Le présent document n’est délivré qu’en un seul exemplaire. Il appartient à l’étudiant d’en faire des photocopies certifiées conformes.", f5));

                        p1.setAlignment(Element.ALIGN_CENTER);
                        p2.setAlignment(Element.ALIGN_CENTER);
                        p3.setAlignment(Element.ALIGN_CENTER);
                        //specify column widths
                        //Create Table object, Here 4 specify the no. of columns

                        //Create cells
                        int moyenne;
                        if (Niveau.equals("2AP1") || Niveau.equals("2AP2")) {
                            moyenne = 10;
                        } else {
                            moyenne = 12;
                        }
                        pdfPCell1 = new PdfPCell(new Paragraph("       Intitulé du Module\n", f6));
                        pdfPCell2 = new PdfPCell(new Paragraph("                Note/20", f6));
                        pdfPCell3 = new PdfPCell(new Paragraph("                Résultat", f6));
                        //LIGNE2
                        pdfPCell4 = new PdfPCell(new Paragraph("Module 1" + " \n", f4));
                        pdfPCell5 = new PdfPCell(new Paragraph("               " + rs.getDouble("module_1"), f9));
                        pdfPCell6 = new PdfPCell(new Paragraph("               " + ((rs.getDouble("module_1") < moyenne) ? "NV" : "V"), f9));

                        pdfPCell7 = new PdfPCell(new Paragraph("Module 2" + " \n", f4));
                        pdfPCell8 = new PdfPCell(new Paragraph("               " + rs.getDouble("module_2"), f9));
                        pdfPCell9 = new PdfPCell(new Paragraph("               " + ((rs.getDouble("module_2") < moyenne) ? "NV" : "V"), f9));

                        pdfPCell10 = new PdfPCell(new Paragraph("Module 3" + " \n", f4));
                        pdfPCell11 = new PdfPCell(new Paragraph("               " + rs.getDouble("module_3"), f9));
                        pdfPCell12 = new PdfPCell(new Paragraph("               " + ((rs.getDouble("module_3") < moyenne) ? "NV" : "V"), f9));

                        pdfPCell13 = new PdfPCell(new Paragraph("Module 4" + " \n", f4));
                        pdfPCell14 = new PdfPCell(new Paragraph("               " + rs.getDouble("module_4"), f9));
                        pdfPCell15 = new PdfPCell(new Paragraph("               " + ((rs.getDouble("module_4") < moyenne) ? "NV" : "V"), f9));

                        pdfPCell16 = new PdfPCell(new Paragraph("Module 5" + " \n", f4));
                        pdfPCell17 = new PdfPCell(new Paragraph("               " + rs.getDouble("module_5"), f9));
                        pdfPCell18 = new PdfPCell(new Paragraph("               " + ((rs.getDouble("module_5") < moyenne) ? "NV" : "V"), f9));

                        pdfPCell19 = new PdfPCell(new Paragraph("Module 6" + " \n", f4));
                        pdfPCell20 = new PdfPCell(new Paragraph("               " + rs.getDouble("module_6"), f9));
                        pdfPCell21 = new PdfPCell(new Paragraph("               " + ((rs.getDouble("module_6") < moyenne) ? "NV" : "V"), f9));

                        pdfPCell22 = new PdfPCell(new Paragraph("Module 7" + " \n", f4));
                        pdfPCell23 = new PdfPCell(new Paragraph("               " + rs.getDouble("module_7"), f9));
                        pdfPCell24 = new PdfPCell(new Paragraph("               " + ((rs.getDouble("module_7") < moyenne) ? "NV" : "V"), f9));
                        //LIGNE2
                        pdfPCell25 = new PdfPCell(new Paragraph("Module 8" + " \n", f4));
                        pdfPCell26 = new PdfPCell(new Paragraph("               " + rs.getDouble("module_8"), f9));
                        pdfPCell27 = new PdfPCell(new Paragraph("               " + ((rs.getDouble("module_8") < moyenne) ? "NV" : "V"), f9));

                        pdfPCell28 = new PdfPCell(new Paragraph("Module 9" + " \n", f4));
                        pdfPCell29 = new PdfPCell(new Paragraph("               " + rs.getDouble("module_9"), f9));
                        pdfPCell30 = new PdfPCell(new Paragraph("               " + ((rs.getDouble("module_9") < moyenne) ? "NV" : "V"), f9));

                        pdfPCell31 = new PdfPCell(new Paragraph("Module 10" + " \n", f4));
                        pdfPCell32 = new PdfPCell(new Paragraph("               " + rs.getDouble("module_10"), f9));
                        pdfPCell33 = new PdfPCell(new Paragraph("               " + ((rs.getDouble("module_10") < moyenne) ? "NV" : "V"), f9));

                        pdfPCell34 = new PdfPCell(new Paragraph("Module 11" + " \n", f4));
                        pdfPCell35 = new PdfPCell(new Paragraph("               " + rs.getDouble("module_11"), f9));
                        pdfPCell36 = new PdfPCell(new Paragraph("               " + ((rs.getDouble("module_11") < moyenne) ? "NV" : "V"), f9));

                        pdfPCell37 = new PdfPCell(new Paragraph("Module 12" + " \n", f4));
                        pdfPCell38 = new PdfPCell(new Paragraph("               " + rs.getDouble("module_12"), f9));
                        pdfPCell39 = new PdfPCell(new Paragraph("               " + ((rs.getDouble("module_12") < moyenne) ? "NV" : "V"), f9));

                        pdfPCellN01 = new PdfPCell(new Paragraph("                 Points du jury\n\n", f7));
                        pdfPCellN02 = new PdfPCell(new Paragraph("                       "));

                        Double sum_notes = (rs.getDouble("module_1") + rs.getDouble("module_2")
                                + rs.getDouble("module_3") + rs.getDouble("module_4")
                                + rs.getDouble("module_5") + rs.getDouble("module_6")
                                + rs.getDouble("module_7") + rs.getDouble("module_8")
                                + rs.getDouble("module_9") + rs.getDouble("module_10")
                                + rs.getDouble("module_11") + rs.getDouble("module_12"));
                        Double moy_notes = sum_notes / 12;

                        pdfPCellN12 = new PdfPCell(new Paragraph("                 " + String.format("%.2f", moy_notes), f8));

                        pdfPCellN11 = new PdfPCell(new Paragraph("                 Moyenne du l’année\n\n", f7));

                        pdfPCellN21 = new PdfPCell(new Paragraph("                 Résultat de l’année\n\n", f7));
                        pdfPCellN22 = new PdfPCell(new Paragraph("                 " + ((moy_notes < moyenne) ? "Non admis" : "admis"), f8));
//           
                    }
                    //Add cells to table
                    document.add(p9);
                    document.add(new Chunk(ls));
                    document.add(p1);
                    document.add(p2);
                    document.add(p3);
                    document.add(p4);
                    document.add(p5);
                    document.add(p6);
                    document.add(p7);
                    document.add(p8);
                    pdfPTable1.addCell(pdfPCell1);
                    pdfPTable1.addCell(pdfPCell2);
                    pdfPTable1.addCell(pdfPCell3);
                    pdfPTable1.addCell(pdfPCell4);
                    pdfPTable1.addCell(pdfPCell5);
                    pdfPTable1.addCell(pdfPCell6);
                    pdfPTable1.addCell(pdfPCell7);
                    pdfPTable1.addCell(pdfPCell8);
                    pdfPTable1.addCell(pdfPCell9);
                    pdfPTable1.addCell(pdfPCell10);
                    pdfPTable1.addCell(pdfPCell11);
                    pdfPTable1.addCell(pdfPCell12);
                    pdfPTable1.addCell(pdfPCell13);
                    pdfPTable1.addCell(pdfPCell14);
                    pdfPTable1.addCell(pdfPCell15);
                    pdfPTable1.addCell(pdfPCell16);
                    pdfPTable1.addCell(pdfPCell17);
                    pdfPTable1.addCell(pdfPCell18);
                    pdfPTable1.addCell(pdfPCell19);
                    pdfPTable1.addCell(pdfPCell20);
                    pdfPTable1.addCell(pdfPCell21);
                    pdfPTable1.addCell(pdfPCell22);
                    pdfPTable1.addCell(pdfPCell23);
                    pdfPTable1.addCell(pdfPCell24);
                    pdfPTable1.addCell(pdfPCell25);
                    pdfPTable1.addCell(pdfPCell26);
                    pdfPTable1.addCell(pdfPCell27);
                    pdfPTable1.addCell(pdfPCell28);
                    pdfPTable1.addCell(pdfPCell29);
                    pdfPTable1.addCell(pdfPCell30);
                    pdfPTable1.addCell(pdfPCell31);
                    pdfPTable1.addCell(pdfPCell32);
                    pdfPTable1.addCell(pdfPCell33);
                    pdfPTable1.addCell(pdfPCell34);
                    pdfPTable1.addCell(pdfPCell35);
                    pdfPTable1.addCell(pdfPCell36);
                    pdfPTable1.addCell(pdfPCell37);
                    pdfPTable1.addCell(pdfPCell38);
                    pdfPTable1.addCell(pdfPCell39);
                    document.add(p9);
                    document.add(p9);
                    pdfPTable2.addCell(pdfPCellN01);
                    pdfPTable2.addCell(pdfPCellN02);
                    pdfPTable2.addCell(pdfPCellN11);
                    pdfPTable2.addCell(pdfPCellN12);
                    pdfPTable2.addCell(pdfPCellN21);
                    pdfPTable2.addCell(pdfPCellN22);
                    document.add(pdfPTable1);
                    document.add(p9);
                    document.add(p9);
                    document.add(pdfPTable2);
                    document.add(p9);
                    document.add(p9);
                    document.add(p10);
                    document.add(p9);
                    document.add(p11);
                    document.add(p9);
                    document.add(p9);
                    document.add(p12);

                    com.itextpdf.text.Rectangle rect = new com.itextpdf.text.Rectangle(577, 825, 18, 15);
                    rect.enableBorderSide(1);
                    rect.enableBorderSide(2);
                    rect.enableBorderSide(4);
                    rect.enableBorderSide(8);
                    rect.setBorderColor(BaseColor.BLACK);
                    rect.setBorderWidth(1);
                    document.add(rect);
                    document.close();

                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                } catch (DocumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

            }

            // Now, you can use the 'filePath' to attach the PDF to the email
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
