package aziscafe;

import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author azisr
 */
public class OrderHistory extends javax.swing.JFrame {

    private CafeDB db;
    DefaultTableModel dt;
    DefaultComboBoxModel dc;
    private Integer orderId;
    /**
     * Creates new form OrderHistory
     */
    public OrderHistory() {
        initComponents();
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
            //all cells false
                return false;
            }
        };
        historyTable.setModel(tableModel);
        
        dc = (DefaultComboBoxModel) historyOrder.getModel();
        dt = (DefaultTableModel) historyTable.getModel();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Method.orderHistory = this;
        loadForm();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private TableColumnModelListener columnModelListener = new TableColumnModelListener() {
        @Override
        public void columnMarginChanged(ChangeEvent e) {
            adjustRowHeight();
        }

        @Override
        public void columnMoved(TableColumnModelEvent e) {
            adjustRowHeight();
        }

        @Override
        public void columnAdded(TableColumnModelEvent e) {
        }

        @Override
        public void columnRemoved(TableColumnModelEvent e) {
        }

        @Override
        public void columnSelectionChanged(ListSelectionEvent e) {
        }
    };
    
    private void loadForm() {
        dc.removeAllElements();
        dt.setColumnCount(0);
        
        String[] colNames = {"No", "Id", "Cashier", "Date", "Information"};
        for (String st: colNames) {
            if (!st.equals("No")) 
                dc.addElement(st);
            dt.addColumn(st);
        }
        TableColumn informationColumn = historyTable.getColumn("Information");
        informationColumn.setCellRenderer(new AutoHeightCellRenderer());
        historyTable.getColumnModel().addColumnModelListener(columnModelListener);

        historyTable.setRowHeight(30);
        historyTable.getColumn("Id").setMinWidth(0);
        historyTable.getColumn("Id").setMaxWidth(0);
        historyTable.getColumn("Id").setWidth(0);
        
        loadData();
    }
    
    public void loadData() {
        orderId = null;
        db = new CafeDB();
        dt.setRowCount(0);
        try {
            Statement s = db.getCon().createStatement();
            String ss = "oh.id LIKE '%" + historySearch.getText() + "%' || em.name LIKE '%" + historySearch.getText() + "%' || oh.date LIKE '%" + historySearch.getText() + "%' || oh.information LIKE '%" + historySearch.getText() + "%'";
            ResultSet rs = s.executeQuery("SELECT oh.id, em.name, oh.date, oh.information FROM order_headers as oh JOIN employees as em ON oh.employee_id = em.id WHERE " + ss + " ORDER BY " + (historyOrder.getSelectedItem().equals("Cashier") ? "em.Name" : "oh." + historyOrder.getSelectedItem()) + " " + historyOrderMethod.getSelectedItem());
            
            int no = 1;
            while (rs.next()) {
                Vector v = new Vector();
                v.add(no);
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                dt.addRow(v);
                no++;
            }
            adjustRowHeight();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void adjustRowHeight() {
        int maxRowHeight = 80; // Maximum row height in pixels

        for (int row = 0; row < historyTable.getRowCount(); row++) {
            int rowHeight = historyTable.getRowHeight();

            for (int column = 0; column < historyTable.getColumnCount(); column++) {
                Component comp = historyTable.prepareRenderer(historyTable.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }

            // Set the row height, considering the maximum row height
            historyTable.setRowHeight(row, Math.min(rowHeight, maxRowHeight));
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

        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        historyTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        historySearch = new javax.swing.JTextField();
        historyShowNote = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        historyOrder = new javax.swing.JComboBox<>();
        historyOrderMethod = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Azis Cafe | Order History");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Order History");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Azis Cafe");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(26, 26, 26))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addComponent(jLabel4)
                    .addContainerGap(246, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel4)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        historyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        historyTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                historyTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(historyTable);

        jLabel5.setText("Search :");

        historySearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historySearchActionPerformed(evt);
            }
        });
        historySearch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                historySearchPropertyChange(evt);
            }
        });
        historySearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                historySearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                historySearchKeyTyped(evt);
            }
        });

        historyShowNote.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        historyShowNote.setText("Show Note");
        historyShowNote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historyShowNoteActionPerformed(evt);
            }
        });

        jLabel6.setText("Order By :");

        historyOrder.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        historyOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historyOrderActionPerformed(evt);
            }
        });

        historyOrderMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ASC", "DESC" }));
        historyOrderMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historyOrderMethodActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(historySearch, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(historyOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(historyOrderMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                        .addComponent(historyShowNote)
                        .addGap(9, 9, 9)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(historySearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(historyShowNote)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(historyOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(historyOrderMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void historySearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historySearchActionPerformed

    }//GEN-LAST:event_historySearchActionPerformed

    private void historySearchPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_historySearchPropertyChange

    }//GEN-LAST:event_historySearchPropertyChange

    private void historySearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_historySearchKeyReleased
        loadData();
    }//GEN-LAST:event_historySearchKeyReleased

    private void historySearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_historySearchKeyTyped

    }//GEN-LAST:event_historySearchKeyTyped

    private void historyOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historyOrderActionPerformed
        if (historyOrder.getSelectedItem() != null)
            loadData();
    }//GEN-LAST:event_historyOrderActionPerformed

    private void historyOrderMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historyOrderMethodActionPerformed
        loadData();
    }//GEN-LAST:event_historyOrderMethodActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Method.cashier.setEnabled(true);
        setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void historyShowNoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historyShowNoteActionPerformed
        if (orderId != null) {
            Method.orderId = orderId;
            Method.formIndex = 1;
            OrderNote po = new OrderNote();
            po.setVisible(true);
            this.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(null, "Select order first!", "Error Table", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }//GEN-LAST:event_historyShowNoteActionPerformed

    private void historyTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_historyTableMouseClicked
        int row = historyTable.getSelectedRow();
        orderId = Integer.valueOf(historyTable.getValueAt(row, 1).toString());
    }//GEN-LAST:event_historyTableMouseClicked

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
            java.util.logging.Logger.getLogger(OrderHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> historyOrder;
    private javax.swing.JComboBox<String> historyOrderMethod;
    private javax.swing.JTextField historySearch;
    private javax.swing.JButton historyShowNote;
    private javax.swing.JTable historyTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}