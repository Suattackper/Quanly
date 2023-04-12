package jframe;

import dao.KetNoisql;
import static dao.KiemTrasdt.isPhoneNumber;
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

        latentaikhoan = new javax.swing.JLabel();
        lasodienthoai = new javax.swing.JLabel();
        lamatkhau = new javax.swing.JLabel();
        laxacnhanmatkhau = new javax.swing.JLabel();
        txttentaikhoan = new javax.swing.JTextField();
        txtsodienthoai = new javax.swing.JTextField();
        txtxacnhanmatkhau = new javax.swing.JPasswordField();
        txtmatkhau = new javax.swing.JPasswordField();
        lahovaten = new javax.swing.JLabel();
        txthovaten = new javax.swing.JTextField();
        btndangnhap = new javax.swing.JButton();
        btndangky = new javax.swing.JButton();
        laxacnhanshowhide = new javax.swing.JLabel();
        lashowhide = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(375, 400));

        latentaikhoan.setBackground(new java.awt.Color(255, 255, 255));
        latentaikhoan.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        latentaikhoan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        latentaikhoan.setText("Tên tài khoản: ");
        latentaikhoan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lasodienthoai.setBackground(new java.awt.Color(255, 255, 255));
        lasodienthoai.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lasodienthoai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lasodienthoai.setText("Số điện thoại:");
        lasodienthoai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lamatkhau.setBackground(new java.awt.Color(255, 255, 255));
        lamatkhau.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lamatkhau.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lamatkhau.setText("Mật khẩu:");
        lamatkhau.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        laxacnhanmatkhau.setBackground(new java.awt.Color(255, 255, 255));
        laxacnhanmatkhau.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        laxacnhanmatkhau.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        laxacnhanmatkhau.setText("Xác nhận mật khẩu:");
        laxacnhanmatkhau.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtxacnhanmatkhau.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtxacnhanmatkhauKeyPressed(evt);
            }
        });

        lahovaten.setBackground(new java.awt.Color(255, 255, 255));
        lahovaten.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lahovaten.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lahovaten.setText("Họ và tên:");
        lahovaten.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btndangnhap.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btndangnhap.setText("Đăng nhập");
        btndangnhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndangnhapActionPerformed(evt);
            }
        });

        btndangky.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btndangky.setText("Đăng ký");
        btndangky.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndangkyActionPerformed(evt);
            }
        });

        laxacnhanshowhide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/eye_hide.png"))); // NOI18N
        laxacnhanshowhide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                laxacnhanshowhideMouseClicked(evt);
            }
        });

        lashowhide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/eye_hide.png"))); // NOI18N
        lashowhide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lashowhideMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(latentaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(txttentaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lasodienthoai, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(txtsodienthoai, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lahovaten, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(txthovaten, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(laxacnhanmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(txtxacnhanmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(laxacnhanshowhide, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(btndangnhap)
                        .addGap(69, 69, 69)
                        .addComponent(btndangky, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lamatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(txtmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lashowhide, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(latentaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttentaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lasodienthoai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtsodienthoai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lahovaten, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txthovaten, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lamatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lashowhide, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(laxacnhanmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtxacnhanmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btndangnhap)
                            .addComponent(btndangky)))
                    .addComponent(laxacnhanshowhide, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
        );

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
    private javax.swing.JLabel lahovaten;
    private javax.swing.JLabel lamatkhau;
    private javax.swing.JLabel lashowhide;
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
