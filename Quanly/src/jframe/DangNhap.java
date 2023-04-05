package jframe;

import dao.KetNoisql;
import java.sql.*;
import javax.swing.*;
import javax.swing.JOptionPane;

public class DangNhap extends javax.swing.JFrame {
    //khởi tạo một đối tượng kết nối CSDL thông qua class ketnoisql
    KetNoisql cn = new KetNoisql();
    //Khai báo biến kết nối CSDL
    Connection conn;
    
    public DangNhap() {
        initComponents();
        // Thay đổi logo và tiêu đề
        ImageIcon icon = new ImageIcon(getClass().getResource("/image/logo.png"));
        setIconImage(icon.getImage());
        setTitle("Đăng nhập");
        //đặt hiển thị của  mật khẩu
        txtmatkhau.setEchoChar('\u25cf');
        //Màn hình xuất hiện ở trung tâm màn hình
        setLocationRelativeTo(null);
        //Giao diện cố định
        setResizable(false);
    }
    private void reset(){
        txttentaikhoan.setText("");
        txtmatkhau.setText("");
    }
    private void dangNhap(){
        // Lấy kết nối CSDL thông qua phương thức ketNoi() của đối tượng ketnoisql.
        conn = cn.ketNoi();
        //Lấy giá trị tài khoản và mật khẩu được nhập vào từ hai đối tượng txtusername và txtpassword.
        String tentaikhoan = txttentaikhoan.getText().toString().trim();
        String matkhau = txtmatkhau.getText().toString().trim();
        //khai báo một đối tượng StringBuffer để xây dựng thông báo lỗi nếu có.
        StringBuffer sb = new StringBuffer();
        //Kiểm tra xem tài khoản và mật khẩu được nhập vào có trống hay không, nếu có thì sẽ được thêm vào đối tượng StringBuffer sb.
        if(tentaikhoan.equals("")){
            sb.append("Tên tài khoản không được trống\n");
        }
        if(matkhau.equals("")){
            sb.append("Mật khẩu không được trống");
        }
        if(sb.length()>0){
            JOptionPane.showMessageDialog(this,sb.toString());
            return;
        }
        //tạo câu lệnh để kiểm tra tài khoản và mật khẩu trong CSDL.
        String sql_dangnhap = "Select * from nguoidung where tennguoidung =? and matkhau=?";
        try {
            //Tạo một PreparedStatement để truy vấn CSDL với câu lệnh SQL đã được khai báo trước đó.
            PreparedStatement pst = conn.prepareStatement(sql_dangnhap);
            //truyền giá trị tài khoản và mật khẩu vào PreparedStatement để thực hiện truy vấn CSDL.
            pst.setString(1, tentaikhoan);
            pst.setString(2, matkhau);
            //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getInt("vaitro")==0){
                    // tạo lựa chọn
                    String[] chon = {"Admin","User"};
                    // hiển thị lựa chọn
                    int luachon = JOptionPane.showOptionDialog(null, "Phiên bản", "Admin đăng nhập thành công", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, chon, chon[0]);
                    if (luachon == JOptionPane.YES_OPTION) {
                        new GiaoDienAdmin().setVisible(true);
                        //ẩn màn hình đăng nhập
                        this.setVisible(false);
                    } else if (luachon == JOptionPane.NO_OPTION) {
                        new GiaoDienChinh().setVisible(true);
                        //ẩn màn hình đăng nhập
                        this.setVisible(false);
                    } else {
                        // Xử lý khi không chọn gì cả hoặc đóng cửa sổ
                    }
                    
                }
                else{
                    //hiển thị một thông báo thành công
                    JOptionPane.showMessageDialog(this,"Đăng nhập thành công");
                    //mở một cửa sổ mới (lớp manhinhchinh) 
                    new GiaoDienChinh(tentaikhoan,matkhau).setVisible(true);
                    //ẩn màn hình đăng nhập
                    this.setVisible(false);
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu");
            }
            //giải phóng bộ nhớ
            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();  
        }   
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        latentaikhoan = new javax.swing.JLabel();
        lamatkhau = new javax.swing.JLabel();
        txttentaikhoan = new javax.swing.JTextField();
        txtmatkhau = new javax.swing.JPasswordField();
        btndangnhap = new javax.swing.JButton();
        btndangky = new javax.swing.JButton();
        txthienthimatkhau = new javax.swing.JTextField();
        lashowhide = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(450, 300));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        latentaikhoan.setBackground(new java.awt.Color(255, 255, 255));
        latentaikhoan.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        latentaikhoan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        latentaikhoan.setText("Tên tài khoản: ");
        latentaikhoan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(latentaikhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(61, 52, 108, 30));

        lamatkhau.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lamatkhau.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lamatkhau.setText("Mật khẩu:");
        lamatkhau.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lamatkhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 108, 31));
        getContentPane().add(txttentaikhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(219, 52, 162, 30));
        getContentPane().add(txtmatkhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 110, 162, 31));

        btndangnhap.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btndangnhap.setText("Đăng nhập");
        btndangnhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndangnhapActionPerformed(evt);
            }
        });
        getContentPane().add(btndangnhap, new org.netbeans.lib.awtextra.AbsoluteConstraints(81, 199, -1, -1));

        btndangky.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btndangky.setText("Đăng ký");
        btndangky.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndangkyActionPerformed(evt);
            }
        });
        getContentPane().add(btndangky, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 199, 88, -1));
        getContentPane().add(txthienthimatkhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 110, 162, 30));

        lashowhide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/eye_hide.png"))); // NOI18N
        lashowhide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lashowhideMouseClicked(evt);
            }
        });
        getContentPane().add(lashowhide, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 116, -1, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //sự kiện nút đăng nhập
    private void btndangnhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndangnhapActionPerformed
        dangNhap();
    }//GEN-LAST:event_btndangnhapActionPerformed

    private void btndangkyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndangkyActionPerformed
        new DangKy().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btndangkyActionPerformed
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

   public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DangNhap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btndangky;
    private javax.swing.JButton btndangnhap;
    private javax.swing.JLabel lamatkhau;
    private javax.swing.JLabel lashowhide;
    private javax.swing.JLabel latentaikhoan;
    private javax.swing.JTextField txthienthimatkhau;
    private javax.swing.JPasswordField txtmatkhau;
    private javax.swing.JTextField txttentaikhoan;
    // End of variables declaration//GEN-END:variables
}
