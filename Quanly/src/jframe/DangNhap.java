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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(450, 300));

        latentaikhoan.setBackground(new java.awt.Color(255, 255, 255));
        latentaikhoan.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        latentaikhoan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        latentaikhoan.setText("Tên tài khoản: ");
        latentaikhoan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lamatkhau.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lamatkhau.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lamatkhau.setText("Mật khẩu:");
        lamatkhau.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(latentaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(txttentaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(lamatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(txtmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(btndangnhap)
                        .addGap(89, 89, 89)
                        .addComponent(btndangky, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(latentaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttentaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lamatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btndangnhap)
                    .addComponent(btndangky))
                .addContainerGap(46, Short.MAX_VALUE))
        );

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
    private javax.swing.JLabel latentaikhoan;
    private javax.swing.JPasswordField txtmatkhau;
    private javax.swing.JTextField txttentaikhoan;
    // End of variables declaration//GEN-END:variables
}
