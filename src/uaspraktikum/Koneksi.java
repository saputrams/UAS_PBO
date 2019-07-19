/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uaspraktikum;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author ASUS
 */
public class Koneksi {
    String pesan;
    Connection conn;

    public Koneksi() {
        pesan ="";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/uaspraktikum","root","");
            pesan="SUKSES";
            
        }catch (Exception e){
           pesan = "ERROR, "+e.getMessage();
        }
        
    }
    
}
