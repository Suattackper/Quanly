package jframe;

import dao.KetNoisql;
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
        //Lấy các giá trị được nhập vào các đối tượng.
        String tentaikhoan = txttentaikhoan.getText().toString().trim();
        String sodienthoai = txtsodienthoai.getText().toString().trim();
        String hovaten = txthovaten.getText().toString().trim();
        String matkhau = txtmatkhau.getText().toString().trim();
        String xacnhanmatkhau = txtxacnhanmatkhau.getText().toString().trim();
        //khai báo một đối tượng StringBuffer để xây dựng thông báo lỗi nếu có.
        StringBuffer sb = new StringBuffer();
        //Kiểm tra xem các đối tượng được nhập vào có trống hay không, nếu có thì sẽ được thêm vào đối tượng StringBuffer sb.
        if(tentaikhoan.equals("")){
            sb.append("Tên tài khoản không được trống\n");
        }
        else if(sodienthoai.equals("")){
            sb.append("Số điện thoại không được trống\n");
        }
        else if(hovaten.equals("")){
            sb.append("Họ và tên không được trống\n");
        }
        else if(matkhau.equals("")){
            sb.append("Mật khẩu không được trống\n");
        }
        else if(xacnhanmatkhau.equals("")){
            sb.append("Vui lòng xác nhân mật khẩu\n");
        }
        if(!xacnhanmatkhau.equals(matkhau)){
            sb.append("Mật khẩu khác xác nhận mật khẩu");
            txtmatkhau.setText("");
            txtxacnhanmatkhau.setText("");
        }
        if(sb.length()>0){
            JOptionPane.showMessageDialog(this,sb.toString());
            return;
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
                String message = "";
                if(rs.getString("tennguoidung").equals(tentaikhoan)){
                message += "Tên tài khoản đã tồn tại.\n";
                }
                if(rs.getString("sodienthoai").equals(sodienthoai)){
                    message += "Số điện thoại đã tồn tại.";
                }
                JOptionPane.showMessageDialog(this, message);
                //giải phóng bộ nhớ
                pst.close();
                rs.close();
            }
            else{
                //hiển thị một thông báo thành công
                JOptionPane.showMessageDialog(this,"Đăng ký thành công");
                //thêm thông tin người dùng mới vào cơ sowrw dữ liệu
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(375, 400));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        latentaikhoan.setBackground(new java.awt.Color(255, 255, 255));
        latentaikhoan.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        latentaikhoan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        latentaikhoan.setText("Tên tài khoản: ");
        latentaikhoan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(latentaikhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 42, 134, 30));

        lasodienthoai.setBackground(new java.awt.Color(255, 255, 255));
        lasodienthoai.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lasodienthoai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lasodienthoai.setText("Số điện thoại:");
        lasodienthoai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lasodienthoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 90, 134, 30));

        lamatkhau.setBackground(new java.awt.Color(255, 255, 255));
        lamatkhau.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lamatkhau.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lamatkhau.setText("Mật khẩu:");
        lamatkhau.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lamatkhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 186, 134, 30));

        laxacnhanmatkhau.setBackground(new java.awt.Color(255, 255, 255));
        laxacnhanmatkhau.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        laxacnhanmatkhau.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        laxacnhanmatkhau.setText("Xác nhận mật khẩu:");
        laxacnhanmatkhau.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(laxacnhanmatkhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 234, 134, 30));
        getContentPane().add(txttentaikhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 42, 135, 30));
        getContentPane().add(txtsodienthoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, 135, 30));
        getContentPane().add(txtxacnhanmatkhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 234, 135, 30));
        getContentPane().add(txtmatkhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 186, 135, 30));

        lahovaten.setBackground(new java.awt.Color(255, 255, 255));
        lahovaten.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lahovaten.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lahovaten.setText("Họ và tên:");
        lahovaten.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lahovaten, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 138, 134, 30));
        getContentPane().add(txthovaten, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 138, 135, 30));

        btndangnhap.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btndangnhap.setText("Đăng nhập");
        btndangnhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndangnhapActionPerformed(evt);
            }
        });
        getContentPane().add(btndangnhap, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, -1, -1));

        btndangky.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btndangky.setText("Đăng ký");
        btndangky.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndangkyActionPerformed(evt);
            }
        });
        getContentPane().add(btndangky, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 300, 87, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btndangnhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndangnhapActionPerformed
        new DangNhap().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btndangnhapActionPerformed

    private void btndangkyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndangkyActionPerformed
        dangKy();
    }//GEN-LAST:event_btndangkyActionPerformed

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
    private javax.swing.JLabel lasodienthoai;
    private javax.swing.JLabel latentaikhoan;
    private javax.swing.JLabel laxacnhanmatkhau;
    private javax.swing.JTextField txthovaten;
    private javax.swing.JPasswordField txtmatkhau;
    private javax.swing.JTextField txtsodienthoai;
    private javax.swing.JTextField txttentaikhoan;
    private javax.swing.JPasswordField txtxacnhanmatkhau;
    // End of variables declaration//GEN-END:variables
}
