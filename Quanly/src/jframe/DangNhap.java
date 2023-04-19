package jframe;

import dao.KetNoisql;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
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
    
    private void dangNhap(){
        // Lấy kết nối CSDL thông qua phương thức ketNoi() của đối tượng ketnoisql.
        conn = cn.ketNoi();
        //Lấy giá trị tài khoản và mật khẩu được nhập vào từ hai đối tượng txtusername và txtpassword.
        String tentaikhoan = txttentaikhoan.getText().toString().trim();
        String matkhau = txtmatkhau.getText().toString().trim();
        //Kiểm tra xem tài khoản và mật khẩu được nhập vào có trống hay không, nếu có thì sẽ được thêm vào đối tượng StringBuffer sb.
        if(tentaikhoan.equals("")||matkhau.equals("")){
            JOptionPane.showMessageDialog(null,"Vui lòng nhập đầy đủ");
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

        jPanel1 = new javax.swing.JPanel();
        lalogo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btndangky = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lashowhide = new javax.swing.JLabel();
        btndangnhap = new javax.swing.JButton();
        lamatkhau = new javax.swing.JLabel();
        txtmatkhau = new javax.swing.JPasswordField();
        txttentaikhoan = new javax.swing.JTextField();
        latentaikhoan = new javax.swing.JLabel();
        lalogin = new javax.swing.JLabel();
        laicontaikhoan = new javax.swing.JLabel();
        laiconmatkhau = new javax.swing.JLabel();
        laexit = new javax.swing.JLabel();
        lamini = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(0, 204, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 0, new java.awt.Color(153, 153, 153)));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 400));

        lalogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Nhà hàng TiTi");

        btndangky.setBackground(new java.awt.Color(153, 255, 153));
        btndangky.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btndangky.setForeground(new java.awt.Color(204, 0, 0));
        btndangky.setText("Register");
        btndangky.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndangkyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lalogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(btndangky)))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(lalogo)
                .addGap(0, 0, 0)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btndangky, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(153, 255, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 400));

        lashowhide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/eye_hide.png"))); // NOI18N
        lashowhide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lashowhideMouseClicked(evt);
            }
        });

        btndangnhap.setBackground(new java.awt.Color(0, 204, 102));
        btndangnhap.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btndangnhap.setForeground(new java.awt.Color(255, 255, 255));
        btndangnhap.setText("Login");
        btndangnhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndangnhapActionPerformed(evt);
            }
        });

        lamatkhau.setBackground(new java.awt.Color(255, 255, 255));
        lamatkhau.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lamatkhau.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lamatkhau.setText("Mật khẩu: ");
        lamatkhau.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        txtmatkhau.setBackground(new java.awt.Color(153, 255, 153));
        txtmatkhau.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txtmatkhau.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtmatkhauKeyPressed(evt);
            }
        });

        txttentaikhoan.setBackground(new java.awt.Color(153, 255, 153));
        txttentaikhoan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        latentaikhoan.setBackground(new java.awt.Color(255, 255, 255));
        latentaikhoan.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        latentaikhoan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        latentaikhoan.setText("Tên tài khoản: ");
        latentaikhoan.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lalogin.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lalogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lalogin.setText("Login");

        laicontaikhoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/taikhoan.png"))); // NOI18N
        laicontaikhoan.setText("jLabel4");

        laiconmatkhau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/matkhau.png"))); // NOI18N

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(250, 250, 250)
                .addComponent(lamini)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(laexit, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(lalogin, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(laicontaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(latentaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttentaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(laiconmatkhau)
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lamatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(lashowhide, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(btndangnhap))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lamini, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(laexit, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(48, 48, 48)
                .addComponent(lalogin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(laicontaikhoan))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(latentaikhoan)
                        .addGap(0, 0, 0)
                        .addComponent(txttentaikhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(laiconmatkhau))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(lamatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(lashowhide)))
                .addGap(18, 18, 18)
                .addComponent(btndangnhap, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btndangkyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndangkyActionPerformed
        new DangKy().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btndangkyActionPerformed

    private void btndangnhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndangnhapActionPerformed
        dangNhap();
    }//GEN-LAST:event_btndangnhapActionPerformed
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

    private void txtmatkhauKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmatkhauKeyPressed
        if(evt.getKeyCode()==evt.VK_ENTER){
            dangNhap();
        }
    }//GEN-LAST:event_txtmatkhauKeyPressed

    private void laexitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_laexitMouseClicked

    private void laminiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseClicked
        setExtendedState(ICONIFIED);
    }//GEN-LAST:event_laminiMouseClicked

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
                new DangNhap().setVisible(true);
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
    private javax.swing.JLabel laiconmatkhau;
    private javax.swing.JLabel laicontaikhoan;
    private javax.swing.JLabel lalogin;
    private javax.swing.JLabel lalogo;
    private javax.swing.JLabel lamatkhau;
    private javax.swing.JLabel lamini;
    private javax.swing.JLabel lashowhide;
    private javax.swing.JLabel latentaikhoan;
    private javax.swing.JPasswordField txtmatkhau;
    private javax.swing.JTextField txttentaikhoan;
    // End of variables declaration//GEN-END:variables
}
