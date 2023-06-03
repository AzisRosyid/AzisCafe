/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aziscafe;

import java.sql.*;
/**
 *
 * @author azisr
 */
public class CafeDB {
   
    public Connection getCon() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_cafe?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "root");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
