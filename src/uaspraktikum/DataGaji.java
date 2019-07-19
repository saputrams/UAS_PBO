/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uaspraktikum;

import Object.DataGajiKaryawan;
import Object.DataKaryawan;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ASUS
 */
public class DataGaji extends javax.swing.JFrame {
    PreparedStatement ps;
    ResultSet rs;
    Koneksi connect;
    ArrayList<DataGajiKaryawan> listData;
    Date date;
    int gaji_perhari = 60000;
    int uang_lembur = 20000;
    int pot_sakit = 5000;
    int pot_izin = 10000;
    int pot_alpa = 15000;
    int tunjangan_jabatan = 25000;
    
    /**
     * Creates new form DataGaji
     */
    public DataGaji() {
        initComponents();
        connect = new Koneksi();
        setLocationRelativeTo(null);
        CB_KaryawanId.removeAllItems();
        LoadCBKaryawanId();
        LoadTableDataGaji();
        ClearData();
        TF_GajiPegId.setVisible(false);
        TF_PotTidakHadir.disable();
        TF_GajiBersih.disable();
        TF_GajiKotor.disable();
    }
    
    public void LoadCBKaryawanId(){
        CB_KaryawanId.addItem("--Pilih Id--");
        try{
            String sql = "SELECT karyawan_id FROM karyawan";
            ps = connect.conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                CB_KaryawanId.addItem(rs.getString("karyawan_id"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error, "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void LoadDataKaryawan(int id){
        try{
            String sql = "SELECT * FROM karyawan WHERE karyawan_id="+id;
            ps = connect.conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.first()){
                LBL_Nama.setText(rs.getString("nama_karyawan"));
                LBL_Jabatan.setText(rs.getString("jabatan"));
                LBL_Depart.setText(rs.getString("departemen"));
                
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error, "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void LoadTableDataGaji(){
        listData = new ArrayList<>();
        try {
            String sql = "SELECT a.*, b.nama_karyawan,b.jabatan,b.departemen FROM gajikaryawan a, karyawan b\n" +
                            "WHERE a.karyawan_id = b.karyawan_id";
            ps = connect.conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                DataGajiKaryawan obj = new DataGajiKaryawan();
                obj.setGajiKaryawanId(rs.getInt("gajiKaryawanId"));
                obj.setKaryawanId(rs.getInt("karyawan_id"));
                obj.setNoTransaksi(rs.getString("no_transaksi"));
                obj.setJumlahHadir(rs.getInt("jumlah_hadir_karyawan"));
                obj.setSakit(rs.getInt("sakit"));
                obj.setIzin(rs.getInt("izin"));
                obj.setAlpa(rs.getInt("alpa"));
                obj.setPotonganTidakMasuk(rs.getDouble("jumlah_potongan_tidak_hadir"));
                obj.setPotonganBpjs(rs.getDouble("jumlah_potongan_BPJS"));
                obj.setLembur(rs.getInt("lembur"));
                obj.setBonus(rs.getDouble("bonus"));
                obj.setTunjanganJabatan(rs.getDouble("tunjangan_jabatan"));
                obj.setGajiKotor(rs.getDouble("gaji_kotor"));
                obj.setGajiBersih(rs.getDouble("gaji_bersih"));
                obj.setNamaKaryawan(rs.getString("nama_karyawan"));
                obj.setJabatan(rs.getString("jabatan"));
                obj.setDepartemen(rs.getString("departemen"));
                
                listData.add(obj);
            }
            
            String[] column = {"ID Gaji Karyawan","ID Karyawan", "Nama Karyawan", "Jabatan", "Departemen","No Transaksi","Tanggal Transaksi","Jumlah Hadir","Jumlah Tidak Hadir","Total Gaji Kotor", "Total Gaji Bersih"};
            String[][] row = new String [listData.size()][11];
            for(int i =0 ; i<listData.size();i++){
                row[i][0]= String.valueOf(listData.get(i).getGajiKaryawanId());
                row[i][1]= String.valueOf(listData.get(i).getKaryawanId());
                row[i][2]= listData.get(i).getNamaKaryawan();
                row[i][3]= listData.get(i).getJabatan();
                row[i][4]= listData.get(i).getDepartemen();
                row[i][5]= listData.get(i).getNoTransaksi();
                row[i][6]= "";
                row[i][7]= String.valueOf(listData.get(i).getJumlahHadir());
                row[i][8]= String.valueOf(listData.get(i).getSakit()+ listData.get(i).getAlpa()+listData.get(i).getIzin());
                row[i][9]= String.valueOf(listData.get(i).getGajiKotor());
                row[i][9]= String.valueOf(listData.get(i).getGajiBersih());
            }

            DefaultTableModel model = new DefaultTableModel(row, column);
            TBL_Data.setModel(model);
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error, "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void DeleteData(int id){
        try{
            
            String sql = "DELETE FROM gajikaryawan "
                    + "WHERE gajiKaryawanId="+id;
            connect.conn.createStatement().executeUpdate(sql);
            connect.conn.createStatement().close();
            JOptionPane.showMessageDialog(rootPane, "SUKSES DELETE");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error, "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void LoadDataDetail(int id){
        DataGajiKaryawan obj = new DataGajiKaryawan();
        try {
            String sql = "SELECT a.*, b.nama_karyawan,b.jabatan,b.departemen FROM gajikaryawan a, karyawan b\n" +
                            "WHERE a.karyawan_id = b.karyawan_id "
                    + "AND a.gajiKaryawanId="+id;
            ps = connect.conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.first()){
                obj.setGajiKaryawanId(rs.getInt("gajiKaryawanId"));
                obj.setKaryawanId(rs.getInt("karyawan_id"));
                obj.setNoTransaksi(rs.getString("no_transaksi"));
                obj.setJumlahHadir(rs.getInt("jumlah_hadir_karyawan"));
                obj.setSakit(rs.getInt("sakit"));
                obj.setIzin(rs.getInt("izin"));
                obj.setAlpa(rs.getInt("alpa"));
                obj.setPotonganTidakMasuk(rs.getDouble("jumlah_potongan_tidak_hadir"));
                obj.setPotonganBpjs(rs.getDouble("jumlah_potongan_BPJS"));
                obj.setLembur(rs.getInt("lembur"));
                obj.setBonus(rs.getDouble("bonus"));
                obj.setTunjanganJabatan(rs.getDouble("tunjangan_jabatan"));
                obj.setGajiKotor(rs.getDouble("gaji_kotor"));
                obj.setGajiBersih(rs.getDouble("gaji_bersih"));
                obj.setNamaKaryawan(rs.getString("nama_karyawan"));
                obj.setJabatan(rs.getString("jabatan"));
                obj.setDepartemen(rs.getString("departemen"));
                
            }
            TF_GajiPegId.setText(String.valueOf(obj.getGajiKaryawanId()));
            TF_Alpa.setText(String.valueOf(obj.getAlpa()));
            TF_Bonus.setText(String.valueOf(obj.getBonus()));
            TF_GajiBersih.setText(String.valueOf(obj.getGajiBersih()));
            TF_GajiKotor.setText(String.valueOf(obj.getGajiKotor()));
            TF_Izin.setText(String.valueOf(obj.getIzin()));
            TF_JmlHadir.setText(String.valueOf(obj.getJumlahHadir()));
            TF_Lembur.setText(String.valueOf(obj.getLembur()));
            TF_NoTrans.setText(String.valueOf(obj.getNoTransaksi()));
            TF_PotTidakHadir.setText(String.valueOf(obj.getPotonganTidakMasuk()));
            TF_Sakit.setText(String.valueOf(obj.getSakit()));
            LBL_Depart.setText(String.valueOf(obj.getDepartemen()));
            LBL_Jabatan.setText(String.valueOf(obj.getJabatan()));
            LBL_Nama.setText(String.valueOf(obj.getNamaKaryawan()));
            LBL_PotBpjs.setText(String.valueOf(obj.getPotonganBpjs()));
            LBL_TglTrans.setText(String.valueOf(1));
            LBL_Tunjangan.setText(String.valueOf(tunjangan_jabatan));
            CB_KaryawanId.setSelectedItem(String.valueOf(obj.getKaryawanId()));
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error, "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void InputData(String karyawan_id, String noTrans, String jmlHadir, String sakit, String izin, String alpa, String jmlPotTidakHadir, String potBpjs, String lembur, String bonus, String tunjangan, String gakor, String gaber){
        try{
            String sql = "INSERT INTO gajikaryawan( karyawan_id, no_transaksi, jumlah_hadir_karyawan, sakit, izin, alpa, jumlah_potongan_tidak_hadir, jumlah_potongan_BPJS, lembur, bonus, tunjangan_jabatan, gaji_kotor, gaji_bersih) "
                + "VALUES ("+karyawan_id+","+noTrans+","+jmlHadir+","+sakit+","+izin+","+alpa+","+jmlPotTidakHadir+","+potBpjs+","+lembur+","+bonus+","+tunjangan+","+gakor+","+gaber+")";
            System.out.println(sql);
            connect.conn.createStatement().executeUpdate(sql);
            connect.conn.createStatement().close();
            JOptionPane.showMessageDialog(rootPane, "SUKSES SIMPAN");
        }catch(Exception e){
            
            JOptionPane.showMessageDialog(null, "Error, "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void UpdateData(String gajiKaryawanId, String karyawan_id, String noTrans, String jmlHadir, String sakit, String izin, String alpa, String jmlPotTidakHadir, String potBpjs, String lembur, String bonus, String tunjangan, String gakor, String gaber){
        try{
            String sql = "UPDATE gajikaryawan "
                    + "SET karyawan_id="+karyawan_id+",no_transaksi="+noTrans+",jumlah_hadir_karyawan="+jmlHadir+","
                    + "sakit="+sakit+",izin="+izin+",alpa="+alpa+",jumlah_potongan_tidak_hadir="+jmlPotTidakHadir+","
                    + "jumlah_potongan_BPJS="+potBpjs+",lembur="+lembur+",bonus="+bonus+","
                    + "tunjangan_jabatan="+tunjangan+",gaji_kotor="+gakor+",gaji_bersih="+gaber+" "
                    + "WHERE gajiKaryawanId="+gajiKaryawanId;
                
            connect.conn.createStatement().executeUpdate(sql);
            connect.conn.createStatement().close();
            JOptionPane.showMessageDialog(rootPane, "SUKSES UPDATE");
        }catch(Exception e){
            
            JOptionPane.showMessageDialog(null, "Error, "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    
    
    public void SetJumlahTidakMasuk(){
        int jml_sakit = 0;
        int jml_izin = 0;
        int jml_alpa = 0;
        int pot_jml_tidak_hadir = 0;
        
        if(!TF_Alpa.getText().equals("")){
            jml_alpa = Integer.parseInt(TF_Alpa.getText().toString());
        }
        if(!TF_Izin.getText().equals("")){
            jml_izin = Integer.parseInt(TF_Izin.getText().toString());
        }
        if(!TF_Sakit.getText().equals("")){
            jml_sakit = Integer.parseInt(TF_Sakit.getText().toString());
        }
        
        pot_jml_tidak_hadir = (jml_sakit*pot_sakit)+(jml_izin*pot_izin)+(jml_alpa*pot_alpa);
        TF_PotTidakHadir.setText(String.valueOf(pot_jml_tidak_hadir));
    }
    
    public void HitungGaji(){
        int jml_kehadiran = 0;
        int bonus = 0;
        int lembur = 0;
        int gajiKotor = 0;
        double gajiBersih = 0;
        double bpjs = 0;
        if(!TF_JmlHadir.getText().equals("")){
            jml_kehadiran = Integer.parseInt(TF_JmlHadir.getText());
        }
        if(!TF_Bonus.getText().equals("")){
            bonus = Integer.parseInt(TF_Bonus.getText());
        }
        if(!TF_Lembur.getText().equals("")){
            lembur = Integer.parseInt(TF_Lembur.getText());
        }
        
        gajiKotor = jml_kehadiran*gaji_perhari;
        bpjs = gajiKotor * 0.03;
        if(gajiKotor>0){
            gajiBersih = gajiKotor + (lembur*uang_lembur) + tunjangan_jabatan + bonus - (Integer.parseInt(TF_PotTidakHadir.getText())) - bpjs;
            if(gajiBersih < 0){
                gajiBersih = 0;
            }
        }
        LBL_PotBpjs.setText(String.valueOf(bpjs));
        TF_GajiKotor.setText(String.valueOf(gajiKotor));
        
        TF_GajiBersih.setText(String.valueOf(gajiBersih));
    }
    
    public void ClearData(){
        TF_GajiPegId.setText("");
        TF_Alpa.setText("");
        TF_Bonus.setText("");
        TF_GajiBersih.setText("0");
        TF_GajiKotor.setText("0");
        TF_Izin.setText("");
        TF_JmlHadir.setText("");
        TF_Lembur.setText("");
        TF_NoTrans.setText("");
        TF_PotTidakHadir.setText("0");
        TF_Sakit.setText("");
        LBL_Depart.setText("");
        LBL_Jabatan.setText("");
        LBL_Nama.setText("");
        LBL_PotBpjs.setText("0");
        LBL_TglTrans.setText("");
        LBL_Tunjangan.setText(String.valueOf(tunjangan_jabatan));
        CB_KaryawanId.setSelectedIndex(0);
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
        jLabel7 = new javax.swing.JLabel();
        CB_KaryawanId = new javax.swing.JComboBox<>();
        LBL_Nama = new javax.swing.JLabel();
        LBL_Jabatan = new javax.swing.JLabel();
        LBL_Depart = new javax.swing.JLabel();
        TF_NoTrans = new javax.swing.JTextField();
        LBL_TglTrans = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        TF_JmlHadir = new javax.swing.JTextField();
        TF_Sakit = new javax.swing.JTextField();
        TF_Izin = new javax.swing.JTextField();
        TF_Alpa = new javax.swing.JTextField();
        TF_PotTidakHadir = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        LBL_PotBpjs = new javax.swing.JLabel();
        TF_Lembur = new javax.swing.JTextField();
        TF_Bonus = new javax.swing.JTextField();
        TF_GajiKotor = new javax.swing.JTextField();
        LBL_Tunjangan = new javax.swing.JLabel();
        TF_GajiBersih = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TBL_Data = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        TF_GajiPegId = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Data Penggajian Karyawan");

        jLabel2.setText("Pilih ID Karyawan");

        jLabel3.setText("Nama Karyawan");

        jLabel4.setText("Jabatan");

        jLabel5.setText("Departemen");

        jLabel6.setText("No. Transaksi");

        jLabel7.setText("Tanggal Transaksi");

        CB_KaryawanId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CB_KaryawanId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CB_KaryawanIdItemStateChanged(evt);
            }
        });

        LBL_Nama.setText("jLabel8");

        LBL_Jabatan.setText("jLabel9");

        LBL_Depart.setText("jLabel10");

        TF_NoTrans.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TF_NoTransKeyTyped(evt);
            }
        });

        LBL_TglTrans.setText("jLabel11");

        jLabel12.setText("Jumlah Hadir Karyawan");

        jLabel13.setText("Keterangan Jumlah Tidak Hadir");

        jLabel14.setText("Sakit");

        jLabel15.setText("Izin");

        jLabel16.setText("Alpa");

        jLabel17.setText("Jumlah Potongan Tidak Hadir");

        TF_JmlHadir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TF_JmlHadirKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TF_JmlHadirKeyTyped(evt);
            }
        });

        TF_Sakit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TF_SakitKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TF_SakitKeyTyped(evt);
            }
        });

        TF_Izin.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        TF_Izin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TF_IzinKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TF_IzinKeyTyped(evt);
            }
        });

        TF_Alpa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TF_AlpaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TF_AlpaKeyTyped(evt);
            }
        });

        jLabel18.setText("Potongan BPJS 3%");

        jLabel19.setText("Lembur");

        jLabel20.setText("Bonus Gaji");

        jLabel21.setText("Tunjangan Jabatan");

        jLabel22.setText("Total Gaji Kotor");

        jLabel23.setText("Total Gaji Bersih");

        LBL_PotBpjs.setText("jLabel24");

        TF_Lembur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TF_LemburKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TF_LemburKeyTyped(evt);
            }
        });

        TF_Bonus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TF_BonusKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TF_BonusKeyTyped(evt);
            }
        });

        TF_GajiKotor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TF_GajiKotorKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TF_GajiKotorKeyReleased(evt);
            }
        });

        LBL_Tunjangan.setText("jLabel25");

        TBL_Data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TBL_Data.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TBL_DataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TBL_Data);
        if (TBL_Data.getColumnModel().getColumnCount() > 0) {
            TBL_Data.getColumnModel().getColumn(0).setResizable(false);
            TBL_Data.getColumnModel().getColumn(1).setResizable(false);
            TBL_Data.getColumnModel().getColumn(2).setResizable(false);
            TBL_Data.getColumnModel().getColumn(3).setResizable(false);
            TBL_Data.getColumnModel().getColumn(4).setResizable(false);
            TBL_Data.getColumnModel().getColumn(5).setResizable(false);
            TBL_Data.getColumnModel().getColumn(6).setResizable(false);
            TBL_Data.getColumnModel().getColumn(7).setResizable(false);
            TBL_Data.getColumnModel().getColumn(8).setResizable(false);
            TBL_Data.getColumnModel().getColumn(9).setResizable(false);
            TBL_Data.getColumnModel().getColumn(10).setResizable(false);
        }

        jButton1.setText("TAMBAH DATA");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("EDIT DATA");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("HAPUS DATA");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("SIMPAN DATA");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("KEMBALI KE MASTER KARYAWAN");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        TF_GajiPegId.setText("jTextField1");

        jButton6.setText("LOG OUT");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(TF_GajiPegId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton6))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton5)
                                .addGap(64, 64, 64))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addGap(55, 55, 55)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(CB_KaryawanId, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(LBL_Nama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(LBL_Jabatan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(LBL_Depart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(TF_NoTrans, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(31, 31, 31)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel13)
                                                    .addComponent(jLabel17)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel12)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(TF_JmlHadir, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(56, 56, 56)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel14)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel15)
                                                            .addComponent(jLabel16))
                                                        .addGap(95, 95, 95)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(TF_Izin)
                                                            .addComponent(TF_Sakit)
                                                            .addComponent(TF_Alpa, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addComponent(TF_PotTidakHadir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel1))))
                                    .addComponent(LBL_TglTrans))
                                .addGap(72, 72, 72)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel23))
                                .addGap(36, 36, 36)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TF_GajiBersih)
                                    .addComponent(TF_Bonus)
                                    .addComponent(LBL_Tunjangan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(LBL_PotBpjs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(TF_Lembur)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(TF_GajiKotor, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addGap(46, 46, 46))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(8, 8, 8)
                .addComponent(TF_GajiPegId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(CB_KaryawanId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(TF_JmlHadir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(LBL_PotBpjs))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(LBL_Nama)
                    .addComponent(jLabel13)
                    .addComponent(jLabel19)
                    .addComponent(TF_Lembur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(LBL_Jabatan)
                    .addComponent(jLabel14)
                    .addComponent(TF_Sakit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(TF_Bonus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(LBL_Depart)
                    .addComponent(jLabel15)
                    .addComponent(TF_Izin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(LBL_Tunjangan))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(TF_NoTrans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(TF_Alpa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(TF_GajiKotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(LBL_TglTrans)
                    .addComponent(jLabel17)
                    .addComponent(jLabel23)
                    .addComponent(TF_GajiBersih, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TF_PotTidakHadir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addContainerGap())
        );

        TF_Izin.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CB_KaryawanIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CB_KaryawanIdItemStateChanged
        // TODO add your handling code here:
        if(CB_KaryawanId.getSelectedIndex() > 0){
            LoadDataKaryawan(Integer.parseInt(CB_KaryawanId.getSelectedItem().toString()));
        }else{
            LBL_Nama.setText("");
            LBL_Jabatan.setText("");
            LBL_Depart.setText("");
        }
    }//GEN-LAST:event_CB_KaryawanIdItemStateChanged

    private void TF_GajiKotorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_GajiKotorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_GajiKotorKeyPressed

    private void TF_GajiKotorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_GajiKotorKeyReleased
        // TODO add your handling code here:
        
        System.out.println("a = "+TF_GajiKotor.getText());
    }//GEN-LAST:event_TF_GajiKotorKeyReleased

    private void TF_JmlHadirKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_JmlHadirKeyTyped
        // TODO add your handling code here:
        char input = evt.getKeyChar();
        if (input < '0' || input > '9') {
            evt.consume();
            getToolkit().beep();
        }
    }//GEN-LAST:event_TF_JmlHadirKeyTyped

    private void TF_SakitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_SakitKeyTyped
        // TODO add your handling code here:
        char input = evt.getKeyChar();
        if (input < '0' || input > '9') {
            evt.consume();
            getToolkit().beep();
        }
    }//GEN-LAST:event_TF_SakitKeyTyped

    private void TF_IzinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_IzinKeyTyped
        // TODO add your handling code here:
        char input = evt.getKeyChar();
        if (input < '0' || input > '9') {
            evt.consume();
            getToolkit().beep();
        }
    }//GEN-LAST:event_TF_IzinKeyTyped

    private void TF_AlpaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_AlpaKeyTyped
        // TODO add your handling code here:
        char input = evt.getKeyChar();
        if (input < '0' || input > '9') {
            evt.consume();
            getToolkit().beep();
        }
    }//GEN-LAST:event_TF_AlpaKeyTyped

    private void TF_LemburKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_LemburKeyTyped
        char input = evt.getKeyChar();
        if (input < '0' || input > '9') {
            evt.consume();
            getToolkit().beep();
        }
    }//GEN-LAST:event_TF_LemburKeyTyped

    private void TF_BonusKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_BonusKeyTyped
        // TODO add your handling code here:
        char input = evt.getKeyChar();
        if (input < '0' || input > '9') {
            evt.consume();
            getToolkit().beep();
        }
    }//GEN-LAST:event_TF_BonusKeyTyped

    private void TF_AlpaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_AlpaKeyReleased
        // TODO add your handling code here:
        SetJumlahTidakMasuk();
        HitungGaji();
    }//GEN-LAST:event_TF_AlpaKeyReleased

    private void TF_IzinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_IzinKeyReleased
        // TODO add your handling code here:
        SetJumlahTidakMasuk();
        HitungGaji();
    }//GEN-LAST:event_TF_IzinKeyReleased

    private void TF_SakitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_SakitKeyReleased
        // TODO add your handling code here:
        SetJumlahTidakMasuk();
        HitungGaji();
    }//GEN-LAST:event_TF_SakitKeyReleased

    private void TF_JmlHadirKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_JmlHadirKeyReleased
        // TODO add your handling code here:
        HitungGaji();
    }//GEN-LAST:event_TF_JmlHadirKeyReleased

    private void TF_LemburKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_LemburKeyReleased
        // TODO add your handling code here:
        HitungGaji();
    }//GEN-LAST:event_TF_LemburKeyReleased

    private void TF_BonusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_BonusKeyReleased
        // TODO add your handling code here:
        HitungGaji();
    }//GEN-LAST:event_TF_BonusKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ClearData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if(TF_GajiPegId.getText().equals("")){
            if(CB_KaryawanId.getSelectedIndex() == 0 || TF_NoTrans.getText().equals("") || TF_JmlHadir.getText().equals("")){
                JOptionPane.showMessageDialog(null, "ID KARYAWAN, NO TRANSAKSI, DAN JUMLAH KEHADIRAN HARUS TERISI !!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }else{

                String jml_sakit = "0";
                String jml_izin = "0";
                String jml_alpa = "0";
                String lembur = "0";
                String bonus = "0";

                if(!TF_Sakit.getText().equals("")){
                    jml_sakit = TF_Sakit.getText().toString();
                }if(!TF_Alpa.getText().equals("")){
                    jml_alpa = TF_Alpa.getText().toString();
                }if(!TF_Izin.getText().equals("")){
                    jml_izin = TF_Izin.getText().toString();
                }if(!TF_Lembur.getText().equals("")){
                    lembur = TF_Lembur.getText().toString();
                }if(!TF_Bonus.getText().equals("")){
                    bonus = TF_Bonus.getText().toString();
                }

                InputData(CB_KaryawanId.getSelectedItem().toString(), TF_NoTrans.getText(),TF_JmlHadir.getText() , jml_sakit, jml_izin, jml_alpa, TF_PotTidakHadir.getText(), LBL_PotBpjs.getText(), lembur, bonus, String.valueOf(tunjangan_jabatan), TF_GajiKotor.getText(), TF_GajiBersih.getText());
                LoadTableDataGaji();
                ClearData();
            }
        }else{
           
            JOptionPane.showMessageDialog(null, "PERINGATAN, SILAHKAN PILIH TOMBOL EDIT !!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if(TF_GajiPegId.getText().equals("")){
            JOptionPane.showMessageDialog(null, "PERINGATAN, PILIH DATA TERLEBIH DAHULU !!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }else{
            if(CB_KaryawanId.getSelectedIndex() == 0 || TF_NoTrans.getText().equals("") || TF_JmlHadir.getText().equals("")){
                JOptionPane.showMessageDialog(null, "ID KARYAWAN, NO TRANSAKSI, DAN JUMLAH KEHADIRAN HARUS TERISI !!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }else{

                String jml_sakit = "0";
                String jml_izin = "0";
                String jml_alpa = "0";
                String lembur = "0";
                String bonus = "0";

                if(!TF_Sakit.getText().equals("")){
                    jml_sakit = TF_Sakit.getText().toString();
                }if(!TF_Alpa.getText().equals("")){
                    jml_alpa = TF_Alpa.getText().toString();
                }if(!TF_Izin.getText().equals("")){
                    jml_izin = TF_Izin.getText().toString();
                }if(!TF_Lembur.getText().equals("")){
                    lembur = TF_Lembur.getText().toString();
                }if(!TF_Bonus.getText().equals("")){
                    bonus = TF_Bonus.getText().toString();
                }

                UpdateData(TF_GajiPegId.getText(),CB_KaryawanId.getSelectedItem().toString(), TF_NoTrans.getText(),TF_JmlHadir.getText() , jml_sakit, jml_izin, jml_alpa, TF_PotTidakHadir.getText(), LBL_PotBpjs.getText(), lembur, bonus, String.valueOf(tunjangan_jabatan), TF_GajiKotor.getText(), TF_GajiBersih.getText());
                LoadTableDataGaji();
                ClearData();
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void TBL_DataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TBL_DataMouseClicked
        // TODO add your handling code here:
        int row = TBL_Data.getSelectedRow();
        int id = Integer.parseInt(TBL_Data.getModel().getValueAt(row, 0).toString());
        LoadDataDetail(id);
    }//GEN-LAST:event_TBL_DataMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if(TF_GajiPegId.getText().equals("")){
            JOptionPane.showMessageDialog(null, "PERINGATAN, PILIH DATA TERLEBIH DAHULU !!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }else{
            DeleteData(Integer.parseInt(TF_GajiPegId.getText().toString()));
            LoadTableDataGaji();
            ClearData();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void TF_NoTransKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TF_NoTransKeyTyped
        // TODO add your handling code here:
        char input = evt.getKeyChar();
        if (input < '0' || input > '9') {
            evt.consume();
            getToolkit().beep();
        }
    }//GEN-LAST:event_TF_NoTransKeyTyped

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        MasterData obj = new MasterData();
        obj.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(DataGaji.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataGaji.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataGaji.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataGaji.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DataGaji().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CB_KaryawanId;
    private javax.swing.JLabel LBL_Depart;
    private javax.swing.JLabel LBL_Jabatan;
    private javax.swing.JLabel LBL_Nama;
    private javax.swing.JLabel LBL_PotBpjs;
    private javax.swing.JLabel LBL_TglTrans;
    private javax.swing.JLabel LBL_Tunjangan;
    private javax.swing.JTable TBL_Data;
    private javax.swing.JTextField TF_Alpa;
    private javax.swing.JTextField TF_Bonus;
    private javax.swing.JTextField TF_GajiBersih;
    private javax.swing.JTextField TF_GajiKotor;
    private javax.swing.JTextField TF_GajiPegId;
    private javax.swing.JTextField TF_Izin;
    private javax.swing.JTextField TF_JmlHadir;
    private javax.swing.JTextField TF_Lembur;
    private javax.swing.JTextField TF_NoTrans;
    private javax.swing.JTextField TF_PotTidakHadir;
    private javax.swing.JTextField TF_Sakit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
