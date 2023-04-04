package jframe;

import dao.KetNoisql;
import static dao.KiemTrasdt.isPhoneNumber;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class GiaoDienAdmin extends javax.swing.JFrame {
    //khởi tạo một đối tượng kết nối CSDL thông qua class ketnoisql
    KetNoisql cn = new KetNoisql();
    //Khai báo biến kết nối CSDL
    Connection conn;
    // Tạo một đối tượng DefaultTableModel để hiển thị dữ liệu từ CSDL lên bảng trong giao diện người dùng.
    DefaultTableModel model = new DefaultTableModel();
    public GiaoDienAdmin() {
        initComponents();
        // hiển thị dữ liệu từ CSDL lên bảng
        loadBang();
        // Thay đổi logo và tiêu đề
        ImageIcon icon = new ImageIcon(getClass().getResource("/image/logo.png"));
        setIconImage(icon.getImage());
        setTitle("Admin");
        //Màn hình xuất hiện ở trung tâm màn hình
        setLocationRelativeTo(null);
        //Giao diện cố định
        setResizable(false);
    }
    public void Reset(){
        txttentaikhoan.setText("");
        txtmatkhau.setText("");
        txtsodienthoai.setText("");
        txthovaten.setText("");
        datengaytao.setDate(null);
    }
    public void loadBang(){
        Object col[] = {"STT","Tên tài khoản","Mật khẩu","Họ và tên","Số điện thoại","vai trò","ngày tạo"};
        // đặt tên cho các cột của bảng
        model.setColumnIdentifiers(col);
        //đặt mô hình dữ liệu cho bảng
        tableuser.setModel(model);
        // tải dữ liệu từ CSDL vào JTable
        updateTable();
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
                    model.addRow(dulieucot);
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
    public void addTable(){
        conn = cn.ketNoi();
//        //Khởi tạo một đối tượng Calendar với thời gian hiên tại
//        Calendar cal = Calendar.getInstance();
        //định dạng ngày tháng theo mẫu "dd-MM-yyyy"
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyy");
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
//        if(!cal.getTime().equals(datengaytao.getDate())){
//            sb.append("Ngày tạo không đúng\n");
//        }
        if(txtsodienthoai.getText().trim().equals("")){
            sb.append("Số điện thoại không được trống\n");
        }
        if(!isPhoneNumber(txtsodienthoai.getText())){
            sb.append("Số điện thoại không hợp lệ\n");
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
                JOptionPane.showMessageDialog(this,"Thêm thành công, ngày tạo được thêm tự động");
                //thêm thông tin người dùng mới vào cơ sở dữ liệu
                String sql = "insert into nguoidung(tennguoidung,matkhau,hovaten,sodienthoai,vaitro) values(?,?,?,?,?)";
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
                //thực hiện cật nhật dữ liệu
                pst1.executeUpdate();
                //xóa hàng
                model.setRowCount(0);
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
        lanhap = new javax.swing.JLabel();
        btnthem = new javax.swing.JButton();
        btnsua = new javax.swing.JButton();
        btnxoa = new javax.swing.JButton();
        btntimkiem = new javax.swing.JButton();
        txtnhap = new javax.swing.JTextField();
        btnreset = new javax.swing.JButton();
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

        lanhap.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lanhap.setText("Nhập:");
        lanhap.setToolTipText("Nhập tên tài khoản hoặc số điện thoại để tìm!");
        lanhap.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnthem.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnthem.setText("Thêm");
        btnthem.setToolTipText("Ngày tạo được thêm tự động");
        btnthem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthemActionPerformed(evt);
            }
        });

        btnsua.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnsua.setText("Sửa");

        btnxoa.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnxoa.setText("Xóa");

        btntimkiem.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btntimkiem.setText("Tìm kiếm");

        btnreset.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnreset.setText("Reset");
        btnreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnresetActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addComponent(lanhap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtnhap, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btntimkiem)
                .addGap(20, 20, 20))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lanhap, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnthem, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnsua, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnxoa, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btntimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnhap, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnreset, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71))
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
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addGap(0, 709, Short.MAX_VALUE)
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
            .addGap(0, 709, Short.MAX_VALUE)
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
            .addGap(0, 709, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 51, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
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

    private void btnthemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthemActionPerformed
        addTable();
    }//GEN-LAST:event_btnthemActionPerformed

    private void btnresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnresetActionPerformed
        Reset();
    }//GEN-LAST:event_btnresetActionPerformed

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
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JScrollPane jScrollPane2;
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
    private javax.swing.JLabel lahovaten;
    private javax.swing.JLabel lamatkhau;
    private javax.swing.JLabel langaytao;
    private javax.swing.JLabel lanhap;
    private javax.swing.JLabel lasodienthoai;
    private javax.swing.JLabel latentaikhoan;
    private javax.swing.JLabel lavaitro;
    private javax.swing.JTable tableuser;
    private javax.swing.JTextField txthovaten;
    private javax.swing.JTextField txtmatkhau;
    private javax.swing.JTextField txtnhap;
    private javax.swing.JTextField txtsodienthoai;
    private javax.swing.JTextField txttentaikhoan;
    // End of variables declaration//GEN-END:variables
}
