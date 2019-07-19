/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uaspraktikum;

import Object.DataKaryawan;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ASUS
 */
public class MasterData extends javax.swing.JFrame {

    Koneksi connect;
    PreparedStatement ps;
    ResultSet rs;
    
    ArrayList<DataKaryawan> listData;
    /**
     * Creates new form MasterData
     */
    public MasterData() {
        initComponents();
        connect = new Koneksi();
        setLocationRelativeTo(null);
        TF_Id.disable();
        LoadJenisKelamin();
        LoadDataTable();
    }
    
    public void LoadJenisKelamin(){
        CB_JenisKelamin.removeAllItems();
        CB_JenisKelamin.addItem("--Pilih--");
        CB_JenisKelamin.addItem("Laki-laki");
        CB_JenisKelamin.addItem("Perempuan");
    }
    
    public void LoadDataTable(){
        listData = new ArrayList<>();
        try {
            String sql = "SELECT * FROM karyawan";
            ps = connect.conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                DataKaryawan obj = new DataKaryawan();
                obj.setKaryawan_id(rs.getInt("karyawan_id"));
                obj.setNama_karyawan(rs.getString("nama_karyawan"));
                obj.setJabatan(rs.getString("jabatan"));
                obj.setDepartemen(rs.getString("departemen"));
                obj.setJenis_kelamin(rs.getString("jenis_kelamin"));
                obj.setNo_telp(rs.getString("no_telepon"));
                obj.setAlamat(rs.getString("alamat"));
                obj.setMasa_kerja(rs.getInt("masa_kerja"));
                obj.setRate_kerja(rs.getInt("rate_kerja"));
                
                listData.add(obj);
            }
            
            String[] column = {"ID Karyawan", "Nama Karyawan", "Jabatan", "Departemen","No Telepon","Alamat","Masa Kerja","Rate Gaji"};
            String[][] row = new String [listData.size()][8];
            for(int i =0 ; i<listData.size();i++){
                row[i][0]= String.valueOf(listData.get(i).getKaryawan_id());
                row[i][1]= listData.get(i).getNama_karyawan();
                row[i][2]= listData.get(i).getJabatan();
                row[i][3]= listData.get(i).getDepartemen();
                row[i][4]= listData.get(i).getNo_telp();
                row[i][5]= listData.get(i).getAlamat();
                row[i][6]= String.valueOf(listData.get(i).getMasa_kerja());
                row[i][7]= String.valueOf(listData.get(i).getRate_kerja());
            }

            DefaultTableModel model = new DefaultTableModel(row, column);
            TBL_Data.setModel(model);
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error, "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void LoadDataDetail(int id){
        DataKaryawan obj = new DataKaryawan();
        try {
            String sql = "SELECT * FROM karyawan where karyawan_id="+id;
            ps = connect.conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.first()){
                obj.setKaryawan_id(rs.getInt("karyawan_id"));
                obj.setNama_karyawan(rs.getString("nama_karyawan"));
                obj.setJabatan(rs.getString("jabatan"));
                obj.setDepartemen(rs.getString("departemen"));
                obj.setJenis_kelamin(rs.getString("jenis_kelamin"));
                obj.setNo_telp(rs.getString("no_telepon"));
                obj.setAlamat(rs.getString("alamat"));
                obj.setMasa_kerja(rs.getInt("masa_kerja"));
                obj.setRate_kerja(rs.getInt("rate_kerja"));
                
            }
            TF_Id.setText(String.valueOf(obj.getKaryawan_id()));
            TF_Nama.setText(obj.getNama_karyawan());
            TF_Jabatan.setText(obj.getJabatan());
            TF_Departemen.setText(obj.getDepartemen());
            if(obj.getJenis_kelamin().equals("L")){
                CB_JenisKelamin.setSelectedIndex(1);
            }else{
                CB_JenisKelamin.setSelectedIndex(2);
            }
            TF_Notelp.setText(obj.getNo_telp());
            TA_Alamat.setText(obj.getAlamat());
            TF_MasaKerja.setText(String.valueOf(obj.getMasa_kerja()));
            TF_Rate.setText(String.valueOf(obj.getRate_kerja()));
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error, "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void InputData(String nama, String jabatan, String departemen, String jenis_kelamin, String no_telp, String alamat, String masa_kerja, String rate){
        String jk = "";
        if(jenis_kelamin.equals("Laki-laki")){
            jk = "L";
        }else{
            jk = "P";
        }
        try{
            
            String sql = "INSERT INTO karyawan(nama_karyawan, jabatan, departemen, jenis_kelamin, no_telepon, alamat, masa_kerja, rate_kerja) "
                    + "VALUES ('"+nama+"','"+jabatan+"','"+departemen+"','"+jk+"','"+no_telp+"','"+alamat+"',"+masa_kerja+","+rate+")";
            connect.conn.createStatement().executeUpdate(sql);
            connect.conn.createStatement().close();
            JOptionPane.showMessageDialog(rootPane, "SUKSES SIMPAN");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error, "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void UpdateData(String id, String nama, String jabatan, String departemen, String jenis_kelamin, String no_telp, String alamat, String masa_kerja, String rate){
        String jk = "";
        if(jenis_kelamin.equals("Laki-laki")){
            jk = "L";
        }else{
            jk = "P";
        }
        try{
            
            String sql = "UPDATE karyawan "
                    + "SET nama_karyawan='"+nama+"',jabatan='"+jabatan+"',departemen='"+departemen+"',"
                    + "jenis_kelamin='"+jk+"',no_telepon='"+no_telp+"',alamat='"+alamat+"',masa_kerja="+masa_kerja+",rate_kerja="+rate+" "
                    + "WHERE karyawan_id="+id;
            connect.conn.createStatement().executeUpdate(sql);
            connect.conn.createStatement().close();
            JOptionPane.showMessageDialog(rootPane, "SUKSES UPDATE");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error, "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void DeleteData(int id){
        try{
            
            String sql = "DELETE FROM karyawan "
                    + "WHERE karyawan_id="+id;
            connect.conn.createStatement().executeUpdate(sql);
            connect.conn.createStatement().close();
            JOptionPane.showMessageDialog(rootPane, "SUKSES DELETE");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error, "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ClearData(){
        TF_Id.setText("");
        TF_Nama.setText("");
        TF_Jabatan.setText("");
        TF_Departemen.setText("");
        CB_JenisKelamin.setSelectedIndex(0);
        TF_Notelp.setText("");
        TA_Alamat.setText("");
        TF_MasaKerja.setText("");
        TF_Rate.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        TF_Id = new javax.swing.JTextField();
        TF_Nama = new javax.swing.JTextField();
        TF_Jabatan = new javax.swing.JTextField();
        TF_Departemen = new javax.swing.JTextField();
        CB_JenisKelamin = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        TF_Notelp = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TA_Alamat = new javax.swing.JTextArea();
        TF_MasaKerja = new javax.swing.JTextField();
        TF_Rate = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TBL_Data = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Master Data Karyawan");

        jLabel2.setText("ID Karyawan");

        jLabel3.setText("Nama Karyawan");

        jLabel4.setText("Jabatan");

        jLabel5.setText("Departemen");

        jLabel6.setText("Jenis Kelamin");

        CB_JenisKelamin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("No. Telepon");

        jLabel8.setText("Alamat");

        jLabel9.setText("Masa Kerja");

        jLabel10.setText("Rate Gaji");

        TA_Alamat.setColumns(20);
        TA_Alamat.setRows(5);
        jScrollPane1.setViewportView(TA_Alamat);

        TF_MasaKerja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_MasaKerjaActionPerformed(evt);
            }
        });

        TBL_Data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8"
            }
        ));
        TBL_Data.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TBL_DataMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TBL_Data);

        jButton1.setText("TAMBAH");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("SIMPAN");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("EDIT");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("HAPUS");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("INPUT PENGGAJIAN");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("LOG OUT");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addComponent(jLabel4))
                                        .addGap(31, 31, 31)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(TF_Jabatan)
                                            .addComponent(TF_Nama)
                                            .addComponent(TF_Id, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addGap(47, 47, 47)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(TF_Departemen, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(CB_JenisKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane1)
                                    .addComponent(TF_Notelp)
                                    .addComponent(TF_MasaKerja)
                                    .addComponent(TF_Rate, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(231, 231, 231)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(22, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton5))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jButton6))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(TF_Id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(TF_Notelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(TF_MasaKerja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(TF_Nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(TF_Jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TF_Departemen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(CB_JenisKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(TF_Rate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TF_MasaKerjaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_MasaKerjaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_MasaKerjaActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if(TF_Id.getText().equals("")){
            if(TF_Nama.getText().equals("")||TF_Jabatan.getText().equals("")||TF_Departemen.getText().equals("")||CB_JenisKelamin.getSelectedItem().equals("")|| TF_Notelp.getText().equals("")||TA_Alamat.getText().equals("")||TF_MasaKerja.getText().equals("")||TF_Rate.getText().equals("")){
                JOptionPane.showMessageDialog(null, "PERINGATAN, SEMUA FATA HARUS TERISI !!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }else{
                InputData(TF_Nama.getText().toString(),TF_Jabatan.getText().toString(),TF_Departemen.getText().toString(),CB_JenisKelamin.getSelectedItem().toString(), TF_Notelp.getText().toString(),TA_Alamat.getText().toString(),TF_MasaKerja.getText().toString(),TF_Rate.getText().toString());
                LoadDataTable();
                ClearData();
            }
        }else{
           
            JOptionPane.showMessageDialog(null, "PERINGATAN, SILAHKAN PILIH TOMBOL EDIT !!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void TBL_DataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TBL_DataMouseClicked
        // TODO add your handling code here:
        int row = TBL_Data.getSelectedRow();
        int id = Integer.parseInt(TBL_Data.getModel().getValueAt(row, 0).toString());
        System.out.println("id = "+id);
        LoadDataDetail(id);
    }//GEN-LAST:event_TBL_DataMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ClearData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if(TF_Id.getText().equals("")){
            JOptionPane.showMessageDialog(null, "PERINGATAN, PILIH DATA TERLEBIH DAHULU !!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }else{
            if(TF_Nama.getText().equals("")||TF_Jabatan.getText().equals("")||TF_Departemen.getText().equals("")||CB_JenisKelamin.getSelectedItem().equals("")|| TF_Notelp.getText().equals("")||TA_Alamat.getText().equals("")||TF_MasaKerja.getText().equals("")||TF_Rate.getText().equals("")){
                JOptionPane.showMessageDialog(null, "PERINGATAN, SEMUA FATA HARUS TERISI !!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }else{
                UpdateData(TF_Id.getText().toString(),TF_Nama.getText().toString(),TF_Jabatan.getText().toString(),TF_Departemen.getText().toString(),CB_JenisKelamin.getSelectedItem().toString(), TF_Notelp.getText().toString(),TA_Alamat.getText().toString(),TF_MasaKerja.getText().toString(),TF_Rate.getText().toString());
                LoadDataTable();
                ClearData();
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if(TF_Id.getText().equals("")){
            JOptionPane.showMessageDialog(null, "PERINGATAN, PILIH DATA TERLEBIH DAHULU !!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }else{
            DeleteData(Integer.parseInt(TF_Id.getText().toString()));
            LoadDataTable();
            ClearData();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

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
            java.util.logging.Logger.getLogger(MasterData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MasterData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MasterData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MasterData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MasterData().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CB_JenisKelamin;
    private javax.swing.JTextArea TA_Alamat;
    private javax.swing.JTable TBL_Data;
    private javax.swing.JTextField TF_Departemen;
    private javax.swing.JTextField TF_Id;
    private javax.swing.JTextField TF_Jabatan;
    private javax.swing.JTextField TF_MasaKerja;
    private javax.swing.JTextField TF_Nama;
    private javax.swing.JTextField TF_Notelp;
    private javax.swing.JTextField TF_Rate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
