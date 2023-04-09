package jframe;

import dao.KetNoisql;
import static dao.KiemTrasdt.isPhoneNumber;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class GiaoDienAdmin extends javax.swing.JFrame {
    //khởi tạo một đối tượng kết nối CSDL thông qua class ketnoisql
    KetNoisql cn = new KetNoisql();
    //Khai báo biến kết nối CSDL
    Connection conn;
    // Tạo một đối tượng DefaultTableModel để hiển thị dữ liệu từ CSDL lên bảng trong giao diện người dùng.
    DefaultTableModel modeluser = new DefaultTableModel();
    public GiaoDienAdmin() {
        initComponents();
        // hiển thị dữ liệu từ CSDL lên bảng
        loadBang();
        hienThi();
        // Thay đổi logo và tiêu đề
        ImageIcon icon = new ImageIcon(getClass().getResource("/image/logo.png"));
        setIconImage(icon.getImage());
        setTitle("Admin");
        //Màn hình xuất hiện ở trung tâm màn hình
        setLocationRelativeTo(null);
        //Giao diện cố định
        setResizable(false);
    }
    public void resetUser(){
        txttentaikhoan.setText("");
        txtmatkhau.setText("");
        txtsodienthoai.setText("");
        txthovaten.setText("");
        txttimkiem.setText("");
        datengaytao.setDate(null);
        radtentaikhoan.setSelected(false);
        radsodienthoai.setSelected(false);
        //xóa hàng
        modeluser.setRowCount(0);
        loadBang();
    }
    public void loadBang(){
        Object col[] = {"STT","Tên tài khoản","Mật khẩu","Họ và tên","Số điện thoại","vai trò","ngày tạo"};
        // đặt tên cho các cột của bảng
        modeluser.setColumnIdentifiers(col);
        //đặt mô hình dữ liệu cho bảng
        tableuser.setModel(modeluser);
        // tải dữ liệu từ CSDL vào JTable
        updateTable();
    }
    public void hienThi(){
        tableuser.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = tableuser.getSelectedRow();
                    if (selectedRow != -1) {
                        // Lấy giá trị từ các cột của hàng được chọn
                        String coltentaikhoan = tableuser.getModel().getValueAt(selectedRow, 1).toString();
                        String colmatkhau = tableuser.getModel().getValueAt(selectedRow, 2).toString();
                        String colhovaten = tableuser.getModel().getValueAt(selectedRow, 3).toString();
                        String colsodienthoai = tableuser.getModel().getValueAt(selectedRow, 4).toString();
                        String colvaitro = tableuser.getModel().getValueAt(selectedRow, 5).toString();
                        String colngaytao = tableuser.getModel().getValueAt(selectedRow, 6).toString();
                        // Hiển thị các giá trị lên các JTextField
                        txttentaikhoan.setText(coltentaikhoan);
                        txtmatkhau.setText(colmatkhau);
                        txthovaten.setText(colhovaten);
                        txtsodienthoai.setText(colsodienthoai);
                        cbbvaitro.setSelectedItem(colvaitro);
                        //định dạng ngày tháng theo mẫu "dd/MM/yyyy"
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            //formatter.parse(colngaytao) chuyển date thành string
                            datengaytao.setDate(formatter.parse(colngaytao));
                        } catch (ParseException ex) {
                            Logger.getLogger(GiaoDienAdmin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }
    public void updateTable(){
        conn = cn.ketNoi();
        if(conn!=null){
            String sql = "Select tennguoidung,matkhau,hovaten,sodienthoai,vaitro,ngaytao from nguoidung";
            try {
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst = conn.prepareStatement(sql);
                // thực thi câu truy vấn và lấy kết quả trả về vào đối tượng ResultSet
                ResultSet rs = pst.executeQuery();
                // tạo một mảng để chứa dữ liệu của từng hàng trong bảng
                Object[] dulieucot = new Object[7];
                int i=0;
                while(rs.next()){
                    i++;
                    // lấy dữ liệu các cột trong bảng nguoidung và đưa vào mảng dulieucot
                    dulieucot[0] = i;
                    dulieucot[1] = rs.getString("tennguoidung");
                    dulieucot[2] = rs.getString("matkhau");
                    dulieucot[3] = rs.getString("hovaten");
                    dulieucot[4] = rs.getString("sodienthoai");
                    if(rs.getInt("vaitro")==0){
                        dulieucot[5] = "Admin";
                    } 
                    else dulieucot[5] = "User";
                    dulieucot[6] = rs.getString("ngaytao");
                    modeluser.addRow(dulieucot);
                }
                //giải phóng bộ nhớ
                pst.close();
                rs.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showConfirmDialog(null, e);
            }
        }
    }
    public void addRow(){
        conn = cn.ketNoi();
        Date ngayhientai = new Date();
        //định dạng ngày tháng theo mẫu "dd/MM/yyyy"
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        //chuyển date sang string
        String formattedngayhientai = dateformat.format(ngayhientai);
        //khai báo một đối tượng StringBuffer để xây dựng thông báo lỗi nếu có.
        StringBuffer sb = new StringBuffer();
        //Kiểm tra xem các đối tượng được nhập vào có trống hay không, nếu có thì sẽ được thêm vào đối tượng StringBuffer sb.
        if(txttentaikhoan.getText().trim().equals("")){
            sb.append("Tên tài khoản không được trống\n");
        }
        if(txthovaten.getText().trim().equals("")){
            sb.append("Họ và tên không được trống\n");
        }
        if(txtmatkhau.getText().trim().equals("")){
            sb.append("Mật khẩu không được trống\n");
        }
        if(datengaytao.getDate()==null||!formattedngayhientai.equals(dateformat.format(datengaytao.getDate()))){
            sb.append("Ngày tạo không đúng\n");
        }
        if(txtsodienthoai.getText().trim().equals("")){
            sb.append("Số điện thoại không được trống\n");
        }
        if(!isPhoneNumber(txtsodienthoai.getText())){
            sb.append("Số điện thoại không hợp lệ");
        }
        if(cbbvaitro.getSelectedIndex()==-1){
            sb.append("Chưa chọn vai trò\n");
        }
        if(sb.length()>0){
            JOptionPane.showMessageDialog(this,sb.toString());
            return;
        }
        //tạo câu lệnh để kiểm tra các đối tượng trong CSDL.
        String sql_kiemtra = "Select * from nguoidung where tennguoidung =? or sodienthoai=?";
        try {
            //Tạo một PreparedStatement để truy vấn CSDL với câu lệnh SQL đã được khai báo trước đó.
            PreparedStatement pst = conn.prepareStatement(sql_kiemtra);
            //truyền giá trị đối tượng cần kiểm tra vào PreparedStatement để thực hiện truy vấn CSDL.
            pst.setString(1, txttentaikhoan.getText());
            pst.setString(2, txtsodienthoai.getText());
            //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                String message = "";
                if(rs.getString("tennguoidung").equals(txttentaikhoan.getText())){
                message += "Tên tài khoản đã tồn tại.\n";
                }
                if(rs.getString("sodienthoai").equals(txtsodienthoai.getText())){
                    message += "Số điện thoại đã tồn tại.";
                }
                JOptionPane.showMessageDialog(this, message);
                //giải phóng bộ nhớ
                pst.close();
                rs.close();
                conn.close();
            }
            else{
                //hiển thị một thông báo thành công
                JOptionPane.showMessageDialog(this,"Thêm thành công");
                //thêm thông tin người dùng mới vào cơ sở dữ liệu
                String sql = "insert into nguoidung(tennguoidung,matkhau,hovaten,sodienthoai,vaitro,ngaytao) values(?,?,?,?,?,?)";
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst1 = conn.prepareStatement(sql);
                //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
                pst1.setString(1, txttentaikhoan.getText());
                pst1.setString(2, txtmatkhau.getText());
                pst1.setString(3, txthovaten.getText());
                pst1.setString(4, txtsodienthoai.getText());
                if(cbbvaitro.getSelectedItem().toString().equals("Admin")){
                    pst1.setInt(5,0 );
                }
                else pst1.setInt(5,1 );
                pst1.setString(6, dateformat.format(datengaytao.getDate()));
                //thực hiện cật nhật dữ liệu
                pst1.executeUpdate();
                //xóa hàng
                modeluser.setRowCount(0);
                //hiển thị dữ liệu từ CSDL lên bảng
                loadBang();
                //giải phóng bộ nhớ
                pst.close();
                rs.close();
                conn.close();
            }
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }
    public void deleteRow(){
        conn = cn.ketNoi();
        try {
            //xóa thông tin người dùng trong cơ sở dữ liệu
            String sql = "delete from nguoidung where tennguoidung=?";
            int rowIndex = tableuser.getSelectedRow(); // lấy chỉ số hàng đang được chọn
            // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
            PreparedStatement pst1 = conn.prepareStatement(sql);
            //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
            pst1.setString(1, tableuser.getValueAt(rowIndex, 1).toString());
            //thực hiện cật nhật dữ liệu
            pst1.executeUpdate();
            //xóa hàng
            modeluser.setRowCount(0);
            //hiển thị dữ liệu từ CSDL lên bảng
            loadBang();
            //giải phóng bộ nhớ
            pst1.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }
    public void fixRow(){
        conn = cn.ketNoi();
        //tạo câu lệnh để kiểm tra các đối tượng trong CSDL.
        String sql_kiemtra = "Select * from nguoidung where tennguoidung =? or sodienthoai=?";
        try {
            //Tạo một PreparedStatement để truy vấn CSDL với câu lệnh SQL đã được khai báo trước đó.
            PreparedStatement pst = conn.prepareStatement(sql_kiemtra);
            //truyền giá trị đối tượng cần kiểm tra vào PreparedStatement để thực hiện truy vấn CSDL.
            pst.setString(1, txttentaikhoan.getText().trim());
            pst.setString(2, txtsodienthoai.getText().trim());
            //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
//                if(!rs.getString("tennguoidung").equals(txttentaikhoan.getText().trim())){
//                    JOptionPane.showMessageDialog(this, "Tên tài khoản không được đổi");
//                }
                //cật nhật thông tin người dùng trong cơ sở dữ liệu
                String sql = "update nguoidung set tennguoidung=?, matkhau=?, hovaten=?,sodienthoai=?,vaitro=?,ngaytao=? where tennguoidung=?";
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst1 = conn.prepareStatement(sql);
                //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
                pst1.setString(1, txttentaikhoan.getText());
                pst1.setString(2, txtmatkhau.getText());
                pst1.setString(3, txthovaten.getText());
                pst1.setString(4, txtsodienthoai.getText());
                if(cbbvaitro.getSelectedItem().toString().equals("Admin")){
                    pst1.setInt(5, 0);
                }
                else pst1.setInt(5, 1);
                //định dạng ngày tháng theo mẫu "dd/MM/yyyy"
                SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
                pst1.setString(6, dateformat.format(datengaytao.getDate()));
                pst1.setString(7, txttentaikhoan.getText());
                //thực hiện cật nhật dữ liệu
                pst1.executeUpdate();
                JOptionPane.showMessageDialog(this, "Sửa thành công");
                //xóa hàng
                modeluser.setRowCount(0);
                //hiển thị dữ liệu từ CSDL lên bảng
                loadBang();
                //giải phóng bộ nhớ
                rs.close();
                pst.close();
                pst1.close();
                conn.close();
            }
        } 
        catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }  
    }
    
    public void timKiem(){
        conn = cn.ketNoi();
        if(conn!=null){
            String sql = "Select tennguoidung,matkhau,hovaten,sodienthoai,vaitro,ngaytao from nguoidung";
            try {
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst = conn.prepareStatement(sql);
                // thực thi câu truy vấn và lấy kết quả trả về vào đối tượng ResultSet
                ResultSet rs = pst.executeQuery();
                // tạo một mảng để chứa dữ liệu của từng hàng trong bảng
                Object[] dulieucot = new Object[7];
                int i=0;
                while(rs.next()){
                    if(radtentaikhoan.isSelected()&&rs.getString("tennguoidung").equals(txttimkiem.getText())){
                        i++;
                        // lấy dữ liệu các cột trong bảng nguoidung và đưa vào mảng dulieucot
                        dulieucot[0] = i;
                        dulieucot[1] = rs.getString("tennguoidung");
                        dulieucot[2] = rs.getString("matkhau");
                        dulieucot[3] = rs.getString("hovaten");
                        dulieucot[4] = rs.getString("sodienthoai");
                        if(rs.getInt("vaitro")==0){
                            dulieucot[5] = "Admin";
                        } 
                        else dulieucot[5] = "User";
                        dulieucot[6] = rs.getString("ngaytao");
                         //xóa hàng
                        modeluser.setRowCount(0);
                        modeluser.addRow(dulieucot); 
                    }
                    else if(radsodienthoai.isSelected()&&rs.getString("sodienthoai").equals(txttimkiem.getText())){
                        i++;
                        // lấy dữ liệu các cột trong bảng nguoidung và đưa vào mảng dulieucot
                        dulieucot[0] = i;
                        dulieucot[1] = rs.getString("tennguoidung");
                        dulieucot[2] = rs.getString("matkhau");
                        dulieucot[3] = rs.getString("hovaten");
                        dulieucot[4] = rs.getString("sodienthoai");
                        if(rs.getInt("vaitro")==0){
                            dulieucot[5] = "Admin";
                        } 
                        else dulieucot[5] = "User";
                        dulieucot[6] = rs.getString("ngaytao");
                         //xóa hàng
                        modeluser.setRowCount(0);
                        modeluser.addRow(dulieucot); 
                    }
                }
                if(radtentaikhoan.isSelected()==false&&radsodienthoai.isSelected()==false){
                    JOptionPane.showMessageDialog(null, "Bạn chưa chọn gì cả");
                }
                else if(txttimkiem.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null, "Bạn chưa nhập gì cả");
                }
                else if(i==0){
                    JOptionPane.showMessageDialog(null, "Không tìm thấy");
                }
                //giải phóng bộ nhớ
                pst.close();
                rs.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showConfirmDialog(null, e);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        latentaikhoan = new javax.swing.JLabel();
        txttentaikhoan = new javax.swing.JTextField();
        txtmatkhau = new javax.swing.JTextField();
        lasodienthoai = new javax.swing.JLabel();
        lamatkhau = new javax.swing.JLabel();
        txthovaten = new javax.swing.JTextField();
        txtsodienthoai = new javax.swing.JTextField();
        langaytao = new javax.swing.JLabel();
        lavaitro = new javax.swing.JLabel();
        datengaytao = new com.toedter.calendar.JDateChooser();
        lahovaten = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        cbbvaitro = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableuser = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        btnthem = new javax.swing.JButton();
        btnsua = new javax.swing.JButton();
        btnxoa = new javax.swing.JButton();
        btntimkiem = new javax.swing.JButton();
        txttimkiem = new javax.swing.JTextField();
        btnreset = new javax.swing.JButton();
        radtentaikhoan = new javax.swing.JRadioButton();
        radsodienthoai = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jTextField11 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jToggleButton1 = new javax.swing.JToggleButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(915, 600));

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jPanel3.setToolTipText("");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        latentaikhoan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        latentaikhoan.setText("Tên tài khoản:");
        latentaikhoan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(latentaikhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 52, -1, 31));
        jPanel1.add(txttentaikhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 53, 143, 31));
        jPanel1.add(txtmatkhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 130, 143, 31));

        lasodienthoai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lasodienthoai.setText("Số điện thoại:");
        lasodienthoai.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(lasodienthoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 129, -1, 31));

        lamatkhau.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lamatkhau.setText("Mật khẩu:");
        lamatkhau.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(lamatkhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 129, 77, 31));
        jPanel1.add(txthovaten, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 53, 127, 31));
        jPanel1.add(txtsodienthoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 130, 127, 31));

        langaytao.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        langaytao.setText("Ngày tạo:");
        langaytao.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(langaytao, new org.netbeans.lib.awtextra.AbsoluteConstraints(515, 129, 77, 31));

        lavaitro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lavaitro.setText("Vai trò:");
        lavaitro.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(lavaitro, new org.netbeans.lib.awtextra.AbsoluteConstraints(515, 52, 77, 31));
        jPanel1.add(datengaytao, new org.netbeans.lib.awtextra.AbsoluteConstraints(598, 129, 129, 32));

        lahovaten.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lahovaten.setText("Họ và tên:");
        lahovaten.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(lahovaten, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 52, 94, 31));
        jPanel1.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(-64, -22, -1, -1));

        cbbvaitro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "User", "Admin" }));
        jPanel1.add(cbbvaitro, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 52, -1, 30));

        tableuser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên tài khoản", "Mật khẩu", "Họ và tên", "Số điện thoại", "Vai trò", "Ngày tạo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tableuser);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
        );

        btnthem.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnthem.setText("Thêm");
        btnthem.setToolTipText("");
        btnthem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthemActionPerformed(evt);
            }
        });

        btnsua.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnsua.setText("Sửa");
        btnsua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsuaActionPerformed(evt);
            }
        });

        btnxoa.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnxoa.setText("Xóa");
        btnxoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxoaActionPerformed(evt);
            }
        });

        btntimkiem.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btntimkiem.setText("Tìm kiếm");
        btntimkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntimkiemActionPerformed(evt);
            }
        });

        txttimkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttimkiemActionPerformed(evt);
            }
        });

        btnreset.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnreset.setText("Reset");
        btnreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnresetActionPerformed(evt);
            }
        });

        radtentaikhoan.setText("Tên tài khoản");
        radtentaikhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radtentaikhoanActionPerformed(evt);
            }
        });

        radsodienthoai.setText("Số điện thoại");
        radsodienthoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radsodienthoaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnthem)
                .addGap(18, 18, 18)
                .addComponent(btnsua)
                .addGap(18, 18, 18)
                .addComponent(btnxoa)
                .addGap(18, 18, 18)
                .addComponent(btnreset)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(radtentaikhoan, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(radsodienthoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txttimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btntimkiem)
                .addGap(14, 14, 14))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnthem, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnsua, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnxoa, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btntimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnreset, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(radtentaikhoan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radsodienthoai)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("      User    ", new javax.swing.ImageIcon(getClass().getResource("/image/nhanvien.png")), jPanel3); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 751, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Đồ ăn", jPanel4);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 751, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Đồ uống", jPanel7);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 751, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Tráng miệng", jPanel8);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        jTabbedPane2.getAccessibleContext().setAccessibleName("");

        jTabbedPane1.addTab("Sản phẩm", new javax.swing.ImageIcon(getClass().getResource("/image/sanpham.png")), jPanel2); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Mật khẩu:");
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Tên tài khoản:");
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Vai trò:");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Ngày tạo:");
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Họ và tên:");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Số điện thoại:");
        jLabel13.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(jTextField4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField6)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                .addGap(23, 23, 23))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên tài khoản", "Mật khẩu", "Họ và tên", "Số điện thoại", "Vai trò", "Ngày tạo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setHeaderValue("STT");
        }

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jScrollPane3.setViewportView(jTree1);

        jToggleButton1.setText("jToggleButton1");

        jRadioButton1.setText("jRadioButton1");

        jRadioButton2.setText("jRadioButton2");

        jCheckBox1.setText("jCheckBox1");

        jCheckBox2.setText("jCheckBox2");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(65, 65, 65)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jToggleButton1)
                            .addComponent(jCheckBox1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton1)
                            .addComponent(jCheckBox2))
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("      User    ", new javax.swing.ImageIcon(getClass().getResource("/image/nhanvien.png")), jPanel5); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    private void btnresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnresetActionPerformed
        resetUser();
    }//GEN-LAST:event_btnresetActionPerformed

    private void btnxoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxoaActionPerformed
        //Tạo một đối tượng DefaultTableModel để quản lý dữ liệu của bảng được lấy từ bảng tableuser
        DefaultTableModel model = (DefaultTableModel) tableuser.getModel();
        //iểm tra xem có hàng nào trong bảng hay không, ko có  = -1
        if(tableuser.getSelectedRow()==-1){
            if(tableuser.getRowCount()==0){ //Kiểm tra xem bảng có hàng nào không
                JOptionPane.showMessageDialog(null, "Không có gì để xóa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
            else{
                JOptionPane.showMessageDialog(null, "Chọn hàng để xóa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
        }
        else{
            //Nếu một hàng trong bảng được chọn, nó sẽ được xóa
            deleteRow();
//            model.removeRow(tableuser.getSelectedRow());
        }
    }//GEN-LAST:event_btnxoaActionPerformed

    private void btnsuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsuaActionPerformed
        //Tạo một đối tượng DefaultTableModel để quản lý dữ liệu của bảng được lấy từ bảng tableuser
        DefaultTableModel model = (DefaultTableModel) tableuser.getModel();
        //kiểm tra xem có hàng nào trong bảng hay không, ko có  = -1
        if(tableuser.getSelectedRow()==-1){
            if(tableuser.getRowCount()==0){ //Kiểm tra xem bảng có hàng nào không
                JOptionPane.showMessageDialog(null, "Không có gì để sửa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
            else{
                JOptionPane.showMessageDialog(null, "Chọn hàng để sửa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
        }
        else{
            //Nếu một hàng trong bảng được chọn, nó sẽ được xóa
            fixRow();
//            model.removeRow(tableuser.getSelectedRow());
        }
    }//GEN-LAST:event_btnsuaActionPerformed

    private void btnthemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthemActionPerformed
        addRow();
    }//GEN-LAST:event_btnthemActionPerformed

    private void radtentaikhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radtentaikhoanActionPerformed
        radsodienthoai.setSelected(false);
    }//GEN-LAST:event_radtentaikhoanActionPerformed

    private void radsodienthoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radsodienthoaiActionPerformed
        radtentaikhoan.setSelected(false);
    }//GEN-LAST:event_radsodienthoaiActionPerformed

    private void btntimkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntimkiemActionPerformed
        timKiem();
    }//GEN-LAST:event_btntimkiemActionPerformed

    private void txttimkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttimkiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttimkiemActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GiaoDienAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GiaoDienAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GiaoDienAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GiaoDienAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GiaoDienAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnreset;
    private javax.swing.JButton btnsua;
    private javax.swing.JButton btnthem;
    private javax.swing.JButton btntimkiem;
    private javax.swing.JButton btnxoa;
    private javax.swing.JComboBox<String> cbbvaitro;
    private com.toedter.calendar.JDateChooser datengaytao;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTree jTree1;
    private javax.swing.JLabel lahovaten;
    private javax.swing.JLabel lamatkhau;
    private javax.swing.JLabel langaytao;
    private javax.swing.JLabel lasodienthoai;
    private javax.swing.JLabel latentaikhoan;
    private javax.swing.JLabel lavaitro;
    private javax.swing.JRadioButton radsodienthoai;
    private javax.swing.JRadioButton radtentaikhoan;
    private javax.swing.JTable tableuser;
    private javax.swing.JTextField txthovaten;
    private javax.swing.JTextField txtmatkhau;
    private javax.swing.JTextField txtsodienthoai;
    private javax.swing.JTextField txttentaikhoan;
    private javax.swing.JTextField txttimkiem;
    // End of variables declaration//GEN-END:variables
}
