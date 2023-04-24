package jframe;

import java.awt.Color;
import javax.swing.ImageIcon;

public class DanhGia extends javax.swing.JFrame {
    int danhgia;
    public DanhGia() {
        initComponents();
        // Thay đổi logo và tiêu đề
        ImageIcon icon = new ImageIcon(getClass().getResource("/image/logo.png"));
        setIconImage(icon.getImage());
        setTitle("Đánh giá");
        //Màn hình xuất hiện ở trung tâm màn hình
        setLocationRelativeTo(null);
        //Giao diện cố định
        setResizable(false);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        langoisao1 = new javax.swing.JLabel();
        langoisao2 = new javax.swing.JLabel();
        langoisao3 = new javax.swing.JLabel();
        langoisao4 = new javax.swing.JLabel();
        langoisao5 = new javax.swing.JLabel();
        lamini = new javax.swing.JLabel();
        laexit = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtbinhluan = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        btngui = new javax.swing.JButton();
        ladanhgia = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(204, 255, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 0, 5, new java.awt.Color(153, 153, 153)));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 100));

        jLabel2.setBackground(new java.awt.Color(204, 255, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Đánh giá");
        jLabel2.setOpaque(true);

        jPanel3.setBackground(new java.awt.Color(204, 255, 102));

        langoisao1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ngoisaotrong.png"))); // NOI18N
        langoisao1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                langoisao1MouseClicked(evt);
            }
        });
        jPanel3.add(langoisao1);

        langoisao2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ngoisaotrong.png"))); // NOI18N
        langoisao2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                langoisao2MouseClicked(evt);
            }
        });
        jPanel3.add(langoisao2);

        langoisao3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ngoisaotrong.png"))); // NOI18N
        langoisao3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                langoisao3MouseClicked(evt);
            }
        });
        jPanel3.add(langoisao3);

        langoisao4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ngoisaotrong.png"))); // NOI18N
        langoisao4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                langoisao4MouseClicked(evt);
            }
        });
        jPanel3.add(langoisao4);

        langoisao5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ngoisaotrong.png"))); // NOI18N
        langoisao5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                langoisao5MouseClicked(evt);
            }
        });
        jPanel3.add(langoisao5);

        lamini.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lamini.setForeground(new java.awt.Color(242, 242, 242));
        lamini.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lamini.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/minimize.png"))); // NOI18N
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
        laexit.setForeground(new java.awt.Color(242, 242, 242));
        laexit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        laexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/close.png"))); // NOI18N
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(lamini, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(laexit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lamini, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(laexit))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 255, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 5, 5, new java.awt.Color(153, 153, 153)));

        txtbinhluan.setColumns(20);
        txtbinhluan.setRows(5);
        jScrollPane1.setViewportView(txtbinhluan);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Bình luận:");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        btngui.setBackground(new java.awt.Color(255, 153, 0));
        btngui.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btngui.setForeground(new java.awt.Color(255, 255, 255));
        btngui.setText("Gửi");
        btngui.setPreferredSize(new java.awt.Dimension(72, 30));

        ladanhgia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ladanhgia.setForeground(new java.awt.Color(255, 0, 0));
        ladanhgia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(btngui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(ladanhgia, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ladanhgia, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btngui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void langoisao1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_langoisao1MouseClicked
        danhgia=1;
        ladanhgia.setText("Rất tệ!");
        langoisao1.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao2.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaotrong.png")));
        langoisao3.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaotrong.png")));
        langoisao4.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaotrong.png")));
        langoisao5.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaotrong.png")));
    }//GEN-LAST:event_langoisao1MouseClicked

    private void langoisao2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_langoisao2MouseClicked
        danhgia=2;
        ladanhgia.setText("Tệ!");
        langoisao1.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao2.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao3.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaotrong.png")));
        langoisao4.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaotrong.png")));
        langoisao5.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaotrong.png")));
    }//GEN-LAST:event_langoisao2MouseClicked

    private void langoisao3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_langoisao3MouseClicked
        danhgia=3;
        ladanhgia.setText("Bình thường!");
        langoisao1.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao2.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao3.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao4.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaotrong.png")));
        langoisao5.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaotrong.png")));
    }//GEN-LAST:event_langoisao3MouseClicked

    private void langoisao4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_langoisao4MouseClicked
        danhgia=4;
        ladanhgia.setText("Ngon!");
        langoisao1.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao2.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao3.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao4.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao5.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaotrong.png")));
    }//GEN-LAST:event_langoisao4MouseClicked

    private void langoisao5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_langoisao5MouseClicked
        danhgia=5;
        ladanhgia.setText("Rất ngon!");
        langoisao1.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao2.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao3.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao4.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
        langoisao5.setIcon(new ImageIcon(getClass().getResource("/image/ngoisaovang.png")));
    }//GEN-LAST:event_langoisao5MouseClicked

    private void laminiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseClicked
        setExtendedState(ICONIFIED);
    }//GEN-LAST:event_laminiMouseClicked

    private void laminiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseEntered
        lamini.setOpaque(true);
        lamini.setBackground(new Color(255,153,0));
    }//GEN-LAST:event_laminiMouseEntered

    private void laminiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseExited
        lamini.setOpaque(false);
        lamini.setBackground(new Color(153,255,153));
    }//GEN-LAST:event_laminiMouseExited

    private void laexitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_laexitMouseClicked

    private void laexitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseEntered
        laexit.setBackground(new Color(255,153,0));
        //dùng để hiển thị background label
        laexit.setOpaque(true);
    }//GEN-LAST:event_laexitMouseEntered

    private void laexitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseExited
        laexit.setBackground(new Color(153,255,153));
        laexit.setOpaque(false);
    }//GEN-LAST:event_laexitMouseExited

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DanhGia().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btngui;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel ladanhgia;
    private javax.swing.JLabel laexit;
    private javax.swing.JLabel lamini;
    private javax.swing.JLabel langoisao1;
    private javax.swing.JLabel langoisao2;
    private javax.swing.JLabel langoisao3;
    private javax.swing.JLabel langoisao4;
    private javax.swing.JLabel langoisao5;
    private javax.swing.JTextArea txtbinhluan;
    // End of variables declaration//GEN-END:variables
}
