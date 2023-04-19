package jframe;

import dao.KetNoisql;
import static dao.KiemTrasdt.isPhoneNumber;
import java.awt.Color;
import java.sql.*;
import javax.swing.*;
import javax.swing.JOptionPane;

public class DangKy extends javax.swing.JFrame {
    //khởi tạo một đối tượng kết nối CSDL thông qua class ketnoisql
    KetNoisql cn = new KetNoisql();
    //Khai báo biến kết nối CSDL
    Connection conn;
    public DangKy() {
        initComponents();
        //Đặt lại các đối tượng
        reset();
        // Thay đổi logo và tiêu đề
        ImageIcon icon = new ImageIcon(getClass().getResource("/image/logo.png"));
        setIconImage(icon.getImage());
        setTitle("Đăng ký");
        //đặt hiển thị của  mật khẩu
        txtmatkhau.setEchoChar('\u25cf');
        txtxacnhanmatkhau.setEchoChar('\u25cf');
        //Màn hình xuất hiện ở trung tâm màn hình
        setLocationRelativeTo(null);
        //Giao diện cố định
        setResizable(false);
    }
    //hàm đặt lại giá trị các đối tượng
    private void reset(){
        txttentaikhoan.setText("");
        txtsodienthoai.setText("");
        txthovaten.setText("");
        txtmatkhau.setText("");
        txtxacnhanmatkhau.setText("");
    }
    private void dangKy(){
        // Lấy kết nối CSDL thông qua phương thức ketNoi() của đối tượng ketnoisql.
        conn = cn.ketNoi();
        //Lấy các giá trị được nhập vào các đối tượng.trim() loại bỏ khoảng trắng ở đầu và cuối chuỗi
        String tentaikhoan = txttentaikhoan.getText().toString().trim();
        String sodienthoai = txtsodienthoai.getText().toString().trim();
        String hovaten = txthovaten.getText().toString().trim();
        String matkhau = txtmatkhau.getText().toString().trim();
        String xacnhanmatkhau = txtxacnhanmatkhau.getText().toString().trim();
        //Kiểm tra xem các đối tượng được nhập vào có trống hay không, nếu có thì sẽ được thêm vào đối tượng StringBuffer sb.
        if(tentaikhoan.equals("")||sodienthoai.equals("")||hovaten.equals("")
                ||matkhau.equals("")||xacnhanmatkhau.equals("")){
            JOptionPane.showMessageDialog(null,"Vui lòng nhập đầy đủ");
            return;
        }
        if(!isPhoneNumber(txtsodienthoai.getText())){
            JOptionPane.showMessageDialog(null,"Số điện thoại không hợp lệ");
            return;
        }
        if(!xacnhanmatkhau.equals("")){
            if(!xacnhanmatkhau.equals(matkhau)){
                JOptionPane.showMessageDialog(null,"Mật khẩu khác xác nhận mật khẩu");
                return;
            }
        }
        //tạo câu lệnh để kiểm tra các đối tượng trong CSDL.
        String sql_dangky = "Select * from nguoidung where tennguoidung =? or sodienthoai=?";
        try {
            //Tạo một PreparedStatement để truy vấn CSDL với câu lệnh SQL đã được khai báo trước đó.
            PreparedStatement pst = conn.prepareStatement(sql_dangky);
            //truyền giá trị đối tượng cần kiểm tra vào PreparedStatement để thực hiện truy vấn CSDL.
            pst.setString(1, tentaikhoan);
            pst.setString(2, sodienthoai);
            //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getString("tennguoidung").equals(tentaikhoan)){
                    JOptionPane.showMessageDialog(null, "Tên tài khoản đã tồn tại");
                    return;
                }
                if(rs.getString("sodienthoai").equals(sodienthoai)){
                    JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại");
                    return;
                }
                //giải phóng bộ nhớ
                pst.close();
                rs.close();
                conn.close();
            }
            else{
                //hiển thị một thông báo thành công
                JOptionPane.showMessageDialog(this,"Đăng ký thành công");
                //thêm thông tin người dùng mới vào cơ sở dữ liệu
                PreparedStatement pst1 = conn.prepareStatement("insert into nguoidung(tennguoidung,matkhau,hovaten,sodienthoai) values(?,?,?,?)");
                pst1.setString(1, txttentaikhoan.getText());
                pst1.setString(2, txtmatkhau.getText());
                pst1.setString(3, txthovaten.getText());
                pst1.setString(4, txtsodienthoai.getText());
                //thực hiện cật nhật dữ liệu
                pst1.executeUpdate();
                //mở một cửa sổ mới (lớp manhinhchinh) 
                new DangNhap().setVisible(true);
                this.setVisible(false);
                //giải phóng bộ nhớ
                pst1.close();
                pst.close();
                rs.close();
                conn.close();
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }   
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        txtxacnhanmatkhau = new javax.swing.JPasswordField();
        txtmatkhau = new javax.swing.JPasswordField();
        txthovaten = new javax.swing.JTextField();
        txtsodienthoai = new javax.swing.JTextField();
        txttentaikhoan = new javax.swing.JTextField();
        latentaikhoan = new javax.swing.JLabel();
        lasodienthoai = new javax.swing.JLabel();
        lahovaten = new javax.swing.JLabel();
        lamatkhau = new javax.swing.JLabel();
        laxacnhanmatkhau = new javax.swing.JLabel();
        lasignup = new javax.swing.JLabel();
        lashowhide = new javax.swing.JLabel();
        laxacnhanshowhide = new javax.swing.JLabel();
        btndangky = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lalogo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lamini = new javax.swing.JLabel();
        laexit = new javax.swing.JLabel();
        btndangnhap = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setBackground(new java.awt.Color(153, 255, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 0, new java.awt.Color(153, 153, 153)));
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 400));

        txtxacnhanmatkhau.setBackground(new java.awt.Color(153, 255, 153));
        txtxacnhanmatkhau.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txtxacnhanmatkhau.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtxacnhanmatkhauKeyPressed(evt);
            }
        });

        txtmatkhau.setBackground(new java.awt.Color(153, 255, 153));
        txtmatkhau.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        txthovaten.setBackground(new java.awt.Color(153, 255, 153));
        txthovaten.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        txtsodienthoai.setBackground(new java.awt.Color(153, 255, 153));
        txtsodienthoai.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        txttentaikhoan.setBackground(new java.awt.Color(153, 255, 153));
        txttentaikhoan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        latentaikhoan.setBackground(new java.awt.Color(255, 255, 255));
        latentaikhoan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        latentaikhoan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        latentaikhoan.setText("Tên tài khoản: ");
        latentaikhoan.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lasodienthoai.setBackground(new java.awt.Color(255, 255, 255));
        lasodienthoai.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lasodienthoai.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lasodienthoai.setText("Số điện thoại:");
        lasodienthoai.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lahovaten.setBackground(new java.awt.Color(255, 255, 255));
        lahovaten.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lahovaten.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lahovaten.setText("Họ và tên:");
        lahovaten.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lamatkhau.setBackground(new java.awt.Color(255, 255, 255));
        lamatkhau.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lamatkhau.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lamatkhau.setText("Mật khẩu:");
        lamatkhau.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        laxacnhanmatkhau.setBackground(new java.awt.Color(255, 255, 255));
        laxacnhanmatkhau.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        laxacnhanmatkhau.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        laxacnhanmatkhau.setText("Xác nhận mật khẩu:");
        laxacnhanmatkhau.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lasignup.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lasignup.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lasignup.setText("Sign Up");

        lashowhide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/eye_hide.png"))); // NOI18N
        lashowhide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lashowhideMouseClicked(evt);
            }
        });

        laxacnhanshowhide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/eye_hide.png"))); // NOI18N
        laxacnhanshowhide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                laxacnhanshowhideMouseClicked(evt);
            }
        });

        btndangky.setBackground(new java.awt.Color(0, 204, 102));
        btndangky.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btndangky.setForeground(new java.awt.Color(255, 255, 255));
        btndangky.setText("Register");
        btndangky.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndangkyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(lasignup))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(33, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btndangky)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(laxacnhanmatkhau)
                                    .addComponent(lamatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lahovaten)
                                    .addComponent(lasodienthoai)
                                    .addComponent(txttentaikhoan)
                                    .addComponent(latentaikhoan)
                                    .addComponent(txtsodienthoai)
                                    .addComponent(txthovaten)
                                    .addComponent(txtmatkhau)
                                    .addComponent(txtxacnhanmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(laxacnhanshowhide, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lashowhide, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lasignup, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(latentaikhoan)
                .addGap(0, 0, 0)
                .addComponent(txttentaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lasodienthoai)
                .addGap(0, 0, 0)
                .addComponent(txtsodienthoai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lahovaten)
                .addGap(0, 0, 0)
                .addComponent(txthovaten, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lamatkhau)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lashowhide, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addComponent(laxacnhanshowhide, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addComponent(laxacnhanmatkhau)
                        .addGap(0, 0, 0)
                        .addComponent(txtxacnhanmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btndangky, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2);

        jPanel1.setBackground(new java.awt.Color(0, 204, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 400));

        lalogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Nhà hàng TiTi");

        lamini.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lamini.setForeground(new java.awt.Color(255, 51, 51));
        lamini.setText("-");
        lamini.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                laminiMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                laminiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                laminiMouseExited(evt);
            }
        });

        laexit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        laexit.setForeground(new java.awt.Color(255, 51, 51));
        laexit.setText("X");
        laexit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                laexitMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                laexitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                laexitMouseExited(evt);
            }
        });

        btndangnhap.setBackground(new java.awt.Color(153, 255, 153));
        btndangnhap.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btndangnhap.setForeground(new java.awt.Color(204, 0, 0));
        btndangnhap.setText("Login");
        btndangnhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndangnhapActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lalogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(78, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lamini)
                        .addGap(16, 16, 16)
                        .addComponent(laexit)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btndangnhap)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lamini, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(laexit, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(lalogo)
                .addGap(0, 0, 0)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE)
                .addComponent(btndangnhap, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btndangnhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndangnhapActionPerformed
        new DangNhap().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btndangnhapActionPerformed

    private void btndangkyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndangkyActionPerformed
        dangKy();
    }//GEN-LAST:event_btndangkyActionPerformed
    //ẩn, hiện xác nhận mật khẩu
    private void laxacnhanshowhideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laxacnhanshowhideMouseClicked
        if (txtxacnhanmatkhau.getEchoChar() == '\u25cf') { //kiểm tra xem mật khẩu có đang được ẩn hay không
            txtxacnhanmatkhau.setEchoChar((char) 0); // hiển thị mật khẩu
            laxacnhanshowhide.setIcon(new ImageIcon(getClass().getResource("/image/eye.png")));
        } else {
            txtxacnhanmatkhau.setEchoChar('\u25cf'); // ẩn mật khẩu
            laxacnhanshowhide.setIcon(new ImageIcon(getClass().getResource("/image/eye_hide.png")));
        }
    }//GEN-LAST:event_laxacnhanshowhideMouseClicked
    //ẩn, hiện mật khẩu
    private void lashowhideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lashowhideMouseClicked
        if (txtmatkhau.getEchoChar() == '\u25cf') { //kiểm tra xem mật khẩu có đang được ẩn hay không
            txtmatkhau.setEchoChar((char) 0); // hiển thị mật khẩu
            lashowhide.setIcon(new ImageIcon(getClass().getResource("/image/eye.png")));
        } else {
            txtmatkhau.setEchoChar('\u25cf'); // ẩn mật khẩu
            lashowhide.setIcon(new ImageIcon(getClass().getResource("/image/eye_hide.png")));
        }
    }//GEN-LAST:event_lashowhideMouseClicked

    private void txtxacnhanmatkhauKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtxacnhanmatkhauKeyPressed
        if(evt.getKeyCode()==evt.VK_ENTER){
            dangKy();
        }
    }//GEN-LAST:event_txtxacnhanmatkhauKeyPressed

    private void laminiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseClicked
        setExtendedState(ICONIFIED);
    }//GEN-LAST:event_laminiMouseClicked

    private void laexitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_laexitMouseClicked

    private void laexitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseEntered
        laexit.setForeground(Color.ORANGE);
    }//GEN-LAST:event_laexitMouseEntered

    private void laexitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseExited
        laexit.setForeground(Color.red);
    }//GEN-LAST:event_laexitMouseExited

    private void laminiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseEntered
        lamini.setForeground(Color.ORANGE);
    }//GEN-LAST:event_laminiMouseEntered

    private void laminiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseExited
        lamini.setForeground(Color.red);
    }//GEN-LAST:event_laminiMouseExited

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DangKy().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btndangky;
    private javax.swing.JButton btndangnhap;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel laexit;
    private javax.swing.JLabel lahovaten;
    private javax.swing.JLabel lalogo;
    private javax.swing.JLabel lamatkhau;
    private javax.swing.JLabel lamini;
    private javax.swing.JLabel lashowhide;
    private javax.swing.JLabel lasignup;
    private javax.swing.JLabel lasodienthoai;
    private javax.swing.JLabel latentaikhoan;
    private javax.swing.JLabel laxacnhanmatkhau;
    private javax.swing.JLabel laxacnhanshowhide;
    private javax.swing.JTextField txthovaten;
    private javax.swing.JPasswordField txtmatkhau;
    private javax.swing.JTextField txtsodienthoai;
    private javax.swing.JTextField txttentaikhoan;
    private javax.swing.JPasswordField txtxacnhanmatkhau;
    // End of variables declaration//GEN-END:variables
}
