/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package aziscafe;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author azisr
 */
public class Menu extends javax.swing.JFrame {

    private CafeDB db;
    private String name, image;
    private Integer id, carbo, protein;
    private Double price;
    DefaultComboBoxModel dc;
    DefaultTableModel dt;
    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
            //all cells false
                return false;
            }
        };
        menuTable.setModel(tableModel);
        
        dc = (DefaultComboBoxModel) menuOrder.getModel();
        dt = (DefaultTableModel) menuTable.getModel();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        loadForm();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void loadForm() {
        db = new CafeDB();
        dc.removeAllElements();
        dt.setColumnCount(0);
        
        String[] colNames = {"No", "Id", "Name", "Price", "Carbo", "Protein", "Image"};
        for (String st: colNames) {
            if (!st.equals("No") && !st.equals("Image")) 
                dc.addElement(st);
            dt.addColumn(st);
        }
        menuTable.getColumn("Image").setMinWidth(0);
        menuTable.getColumn("Image").setMaxWidth(0);
        menuTable.getColumn("Image").setWidth(0);
        menuTable.getColumn("Id").setMinWidth(0);
        menuTable.getColumn("Id").setMaxWidth(0);
        menuTable.getColumn("Id").setWidth(0);
        name = ""; image = ""; id = null; carbo = null; protein = null; price = null;
        
        loadData();
        loadDescription();
    }
    
     private void loadData2() {
        dt.setRowCount(0);
        try {
            Statement s = db.getCon().createStatement();
            String ss = "id LIKE '%" + menuSearch.getText() + "%' || name LIKE '%" + menuSearch.getText() + "%'";
            ResultSet rs = s.executeQuery("SELECT * FROM menus WHERE " + ss + " ORDER BY " + menuOrder.getSelectedItem() + " " + menuOrderMethod.getSelectedItem());
            
            int no = 1;
            while (rs.next()) {
                Vector v = new Vector();
                v.add(no);
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));
                v.add(rs.getString(6));
                dt.addRow(v);
                no++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadData() {
        dt.setRowCount(0);
        try {
            Statement s = db.getCon().createStatement();
            String ss = "id LIKE '%" + menuSearch.getText() + "%' || name LIKE '%" + menuSearch.getText() + "%'";
            ResultSet rs = s.executeQuery("SELECT * FROM menus WHERE " + ss);

            List<Vector<String>> data = new ArrayList<>();

            int no = 1;
            while (rs.next()) {
                Vector<String> v = new Vector();
                v.add(Integer.toString(no));
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));
                v.add(rs.getString(6));
                data.add(v);
                no++;
            }
            
            System.out.println("Before Sorting");
            for (Vector<String> row : data) {
                System.out.println(row);
            }

            if ("ASC".equals(menuOrderMethod.getSelectedItem())) {
                insertionSort(data);
            } else if ("DESC".equals(menuOrderMethod.getSelectedItem())) {
                shellSort(data);
            }

            System.out.println("After Sorting | Order by index: "  + (menuOrder.getSelectedIndex() + 1) + " with method: " + menuOrderMethod.getSelectedItem());
            for (Vector<String> row : data) {
                System.out.println(row);
                dt.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertionSort(List<Vector<String>> data) {
        int n = data.size();
        int sortByIndex = menuOrder.getSelectedIndex() + 1;

        for (int i = 1; i < n; ++i) {
            Vector<String> key = new Vector<>(data.get(i));
            int j = i - 1;

            while (j >= 0 && compareValues(data.get(j).get(sortByIndex), key.get(sortByIndex)) > 0) {
                data.set(j + 1, new Vector<>(data.get(j)));
                j = j - 1;
            }
            data.set(j + 1, key);
        }
    }

    private void shellSort(List<Vector<String>> data) {
        int n = data.size();
        int sortByIndex = menuOrder.getSelectedIndex() + 1;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Vector<String> temp = new Vector<>(data.get(i));
                int j;
                for (j = i; j >= gap && compareValues(data.get(j - gap).get(sortByIndex), temp.get(sortByIndex)) < 0; j -= gap) {
                    data.set(j, new Vector<>(data.get(j - gap)));
                }
                data.set(j, temp);
            }
        }
    }

    private int compareValues(String value1, String value2) {
        try {
            Double num1 = Double.parseDouble(value1);
            Double num2 = Double.parseDouble(value2);
            return num1.compareTo(num2);
        } catch (NumberFormatException e) {
            // Handle non-numeric comparison
            return value1.compareToIgnoreCase(value2);
        }
    }

    private void loadDescription() {
        menuName.setText(name);
        menuPrice.setText(price == null ? "" : Method.curr(price));
        menuCarbo.setText(carbo == null ? "" : carbo.toString());
        menuProtein.setText(carbo == null ? "" : protein.toString());
        
        try {
            BufferedImage imageFile = ImageIO.read(ClassLoader.getSystemResource(Method.imagePath + image));
            Image scaledImage = imageFile.getScaledInstance(menuImage.getWidth(),menuImage.getHeight(),Image.SCALE_SMOOTH);
            menuImage.setIcon(new ImageIcon(scaledImage));
        } catch (Exception ex) {
            menuImage.setIcon(new ImageIcon(ClassLoader.getSystemResource(Method.imagePath + "nopict.png")));
        }
    }
    
    private boolean selectMenu() {
        for (OrderMenu st: Method.orderMenus) {
            if (st.getId() == id) {
                st.setQty(st.getQty() + 1);
                return false;
            }
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        menuTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        menuSearch = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        menuImage = new javax.swing.JLabel();
        menuName = new javax.swing.JTextField();
        menuPrice = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        menuCarbo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        menuProtein = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        menuOrder = new javax.swing.JComboBox<>();
        menuOrderMethod = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Azis Cafe | Select Menu");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Select Menu");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Azis Cafe");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(28, 28, 28))
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

        menuTable.setModel(new javax.swing.table.DefaultTableModel(
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
        menuTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(menuTable);

        jLabel3.setText("Order By :");

        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        menuSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSearchActionPerformed(evt);
            }
        });
        menuSearch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                menuSearchPropertyChange(evt);
            }
        });
        menuSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                menuSearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                menuSearchKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Description :");

        jLabel5.setText("Search :");

        jLabel6.setText("Name :");

        jButton2.setText("Select");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        menuName.setEditable(false);

        menuPrice.setEditable(false);

        jLabel7.setText("Price");

        menuCarbo.setEditable(false);

        jLabel8.setText("Carbo :");

        menuProtein.setEditable(false);

        jLabel9.setText("Protein :");

        jLabel10.setText("Image :");

        menuOrder.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        menuOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOrderActionPerformed(evt);
            }
        });

        menuOrderMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ASC", "DESC" }));
        menuOrderMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOrderMethodActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(38, 38, 38)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(102, 102, 102)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(30, 30, 30)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(menuName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(menuPrice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(menuCarbo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9)
                                            .addComponent(menuProtein, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel10))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(105, 105, 105)
                                .addComponent(menuImage, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(32, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(menuOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(menuOrderMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(menuSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(menuSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(menuOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(menuOrderMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(menuName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(menuPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(menuCarbo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(menuProtein, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(menuImage, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuTableMouseClicked
        // TODO add your handling code here:
        int row = menuTable.getSelectedRow();
        id = Integer.valueOf(menuTable.getValueAt(row, 1).toString());
        name = menuTable.getValueAt(row, 2).toString();
        price = Double.valueOf(menuTable.getValueAt(row, 3).toString());
        carbo = Integer.valueOf(menuTable.getValueAt(row, 4).toString());
        protein = Integer.valueOf(menuTable.getValueAt(row, 5).toString());
        image = menuTable.getValueAt(row, 6).toString();
        loadDescription();
    }//GEN-LAST:event_menuTableMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Method.cashier.setEnabled(true);
        setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (id == null) {
            return;
        }
        if (selectMenu()){
            Method.orderMenus.add(new OrderMenu(id, name, 1, carbo, protein, price, image));
        }
        Method.cashier.setEnabled(true);
        Method.cashier.loadData();
        setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void menuOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOrderActionPerformed
        loadData();
    }//GEN-LAST:event_menuOrderActionPerformed

    private void menuOrderMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOrderMethodActionPerformed
        loadData();
    }//GEN-LAST:event_menuOrderMethodActionPerformed

    private void menuSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_menuSearchKeyTyped
        
    }//GEN-LAST:event_menuSearchKeyTyped

    private void menuSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSearchActionPerformed
        
    }//GEN-LAST:event_menuSearchActionPerformed

    private void menuSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_menuSearchKeyReleased
        loadData();
    }//GEN-LAST:event_menuSearchKeyReleased

    private void menuSearchPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_menuSearchPropertyChange
       
    }//GEN-LAST:event_menuSearchPropertyChange

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Method.cashier.setEnabled(true);
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField menuCarbo;
    private javax.swing.JLabel menuImage;
    private javax.swing.JTextField menuName;
    private javax.swing.JComboBox<String> menuOrder;
    private javax.swing.JComboBox<String> menuOrderMethod;
    private javax.swing.JTextField menuPrice;
    private javax.swing.JTextField menuProtein;
    private javax.swing.JTextField menuSearch;
    private javax.swing.JTable menuTable;
    // End of variables declaration//GEN-END:variables
}
