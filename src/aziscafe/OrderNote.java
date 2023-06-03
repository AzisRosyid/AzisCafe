/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package aziscafe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
/**
 *
 * @author azisr
 */
public class OrderNote extends javax.swing.JFrame {

    private CafeDB db;
    private String cashierNote;
    
    /**
     * Creates new form PurchaseOrder
     */
    public OrderNote() {
        initComponents();
        orderNote.setAlignmentX(JTextPane.CENTER_ALIGNMENT);
        StyledDocument doc = orderNote.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        loadForm();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void loadForm() {
        try {
            db = new CafeDB();
            Statement s = db.getCon().createStatement();
            ResultSet rs = s.executeQuery("SELECT purchase_note FROM order_headers WHERE id='" + Method.orderId + "' LIMIT 1;");
            if (rs.next())
                cashierNote = rs.getString(1);
            orderNote.setText(cashierNote);
        } catch (SQLException ex) {
            Logger.getLogger(OrderNote.class.getName()).log(Level.SEVERE, null, ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        orderNote = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        orderClose = new javax.swing.JButton();
        orderPrint = new javax.swing.JButton();
        orderSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Azis Cafe | Order Note");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        orderNote.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(orderNote);

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Order Note");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Azis Cafe");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(26, 26, 26))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addComponent(jLabel2)
                    .addContainerGap(751, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel2)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        orderClose.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        orderClose.setText("Close");
        orderClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderCloseActionPerformed(evt);
            }
        });

        orderPrint.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        orderPrint.setText("Print");
        orderPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderPrintActionPerformed(evt);
            }
        });

        orderSave.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        orderSave.setText("Save");
        orderSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(orderClose)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(orderPrint)
                        .addGap(18, 18, 18)
                        .addComponent(orderSave)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(orderClose)
                    .addComponent(orderPrint)
                    .addComponent(orderSave))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        switch (Method.formIndex) {
            case 0 -> {
                Method.cashier.setEnabled(true);
                setVisible(false);
                Method.cashier.loadForm();
            }
            case 1 -> {
                Method.orderHistory.setEnabled(true);
                setVisible(false);
                Method.orderHistory.loadData();
            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void orderCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderCloseActionPerformed
        switch (Method.formIndex) {
            case 0 -> {
                Method.cashier.setEnabled(true);
                setVisible(false);
                Method.cashier.loadForm();
            }
            case 1 -> {
                Method.orderHistory.setEnabled(true);
                setVisible(false);
                Method.orderHistory.loadData();
            }
        }
    }//GEN-LAST:event_orderCloseActionPerformed

    private void orderSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderSaveActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Do you want to save the Order Note as txt file?", "Confirm Message", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        int no = 0;
        File dir = new File(Method.savePath);
        String outputPath = "";
        if (!dir.exists())
            dir.mkdirs();
        while(true) {
            outputPath = Method.savePath + "note" + (no == 0 ? "" : no) + ".txt";
            File f = new File(outputPath);
            if (!f.exists() && !f.isDirectory())
                break;
            no++;
        }
        try (PrintWriter pw = new PrintWriter(outputPath)) {  
            pw.print(cashierNote);
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OrderNote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_orderSaveActionPerformed

    private void orderPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderPrintActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Do you want to print the Order Note as pdf file?", "Confirm Message", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        
        int no = 0;
        File dir = new File(Method.printPath);
        String outputPath = "";
        if (!dir.exists())
            dir.mkdirs();
        while(true) {
            outputPath = Method.printPath + "file" + (no == 0 ? "" : no) + ".pdf";
            File f = new File(outputPath);
            if (!f.exists() && !f.isDirectory())
                break;
            no++;
        }

        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont( PDType1Font.COURIER_BOLD, 12);
            contentStream.setLeading(15f);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            
            String[] lines = cashierNote.split("\n");
            for (String line : lines) {
                contentStream.showText(line);
                contentStream.newLine();
            }
            
            contentStream.endText();
            contentStream.close();

            document.save(outputPath);
            document.close();

            System.out.println("PDF printed successfully!");
        } catch (Exception e) {
           
        }
    }//GEN-LAST:event_orderPrintActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OrderNote.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderNote.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderNote.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderNote.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrderNote().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton orderClose;
    private javax.swing.JTextPane orderNote;
    private javax.swing.JButton orderPrint;
    private javax.swing.JButton orderSave;
    // End of variables declaration//GEN-END:variables
}
