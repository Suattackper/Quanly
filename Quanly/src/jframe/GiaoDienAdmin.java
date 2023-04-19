package jframe;

import com.microsoft.sqlserver.jdbc.StringUtils;
import dao.KetNoisql;
import static dao.KiemTrasdt.isPhoneNumber;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class GiaoDienAdmin extends javax.swing.JFrame {
    //khởi tạo một đối tượng kết nối CSDL thông qua class ketnoisql
    KetNoisql cn = new KetNoisql();
    //Khai báo biến kết nối CSDL
    Connection conn;
    // Tạo một đối tượng DefaultTableModel để hiển thị dữ liệu từ CSDL lên bảng trong giao diện người dùng và sản phẩm.
    DefaultTableModel modeluser = new DefaultTableModel();
    DefaultTableModel modelproduct = new DefaultTableModel();
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
        modelproduct.setRowCount(0);
        modeluser.setRowCount(0);
        loadBang();
    }
    public void resetProduct(){
        txtmasanpham.setText("");
        txttensanpham.setText("");
        txtgia.setText("");
        txtmota.setText("");
        lachonanh.setIcon(null);
        //xóa hàng
        modeluser.setRowCount(0);
        modelproduct.setRowCount(0);
        loadBang();
    }
    public void loadBang(){
        Object coluser[] = {"STT","Tên tài khoản","Mật khẩu","Họ và tên","Số điện thoại","Vai trò","Ngày tạo"};
        Object colproduct[] = {"STT","Mã sản phẩm","Tên sản phẩm","Loại","Giá","Mô tả","Ngày tạo"};
        // đặt tên cho các cột của bảng
        modeluser.setColumnIdentifiers(coluser);
        modelproduct.setColumnIdentifiers(colproduct);
        //đặt mô hình dữ liệu cho bảng
        tableuser.setModel(modeluser);
        tableproduct.setModel(modelproduct);
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
        tableproduct.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = tableproduct.getSelectedRow();
                    if (selectedRow != -1) {
                        conn = cn.ketNoi();
                        // Lấy giá trị từ các cột của hàng được chọn
                        String colmasanpham = tableproduct.getModel().getValueAt(selectedRow, 1).toString();
                        String coltensanpham = tableproduct.getModel().getValueAt(selectedRow, 2).toString();
                        String colloai = tableproduct.getModel().getValueAt(selectedRow, 3).toString();
                        String colgia = tableproduct.getModel().getValueAt(selectedRow, 4).toString();
                        String colmota = tableproduct.getModel().getValueAt(selectedRow, 5).toString();
                        // Hiển thị các giá trị lên các JTextField
                        txtmasanpham.setText(colmasanpham);
                        txttensanpham.setText(coltensanpham);
                        cbbloai.setSelectedItem(colloai);
                        txtgia.setText(colgia);
                        txtmota.setText(colmota);
                        try {
                            String sql = "select anh from sanpham where masanpham=?";
                            // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                            PreparedStatement pst = conn.prepareStatement(sql);
                            pst.setString(1, colmasanpham);
                            // thực thi câu truy vấn và lấy kết quả trả về vào đối tượng ResultSet
                            ResultSet rs = pst.executeQuery();
                            if(rs.next()){
                                if(rs.getBytes("anh")!=null){
                                    byte[] anh = rs.getBytes("anh");
                                    // tạo đối tượng ImageIcon từ mảng byte
                                    ImageIcon icon = new ImageIcon(anh);
                                    //Thiết lập kích thước mới cho hình ảnh 
                                    Image newImg = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                                    //Tạo một đối tượng ImageIcon mới từ đối tượng Image đã thu nhỏ để có thể hiển thị hình ảnh thu nhỏ trên giao diện người dùng
                                    icon = new ImageIcon(newImg);
                                    //hiển thị trên giao diện người dùng tại vị trí của lachonanh
                                    lachonanh.setIcon(icon);
                                }
                                else lachonanh.setIcon(null);
                                //giải phóng bộ nhớ
                                pst.close();
                                rs.close();
                                conn.close();
                            }
                        } catch (Exception e) {
                            
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
            String sqlsp = "Select masanpham,tensanpham,loai,gia,mota,format(ngaytao,'dd/MM/yyyy') as 'ngaytao' from sanpham";
            try {
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst = conn.prepareStatement(sql);
                PreparedStatement pstsp = conn.prepareStatement(sqlsp);
                // thực thi câu truy vấn và lấy kết quả trả về vào đối tượng ResultSet
                ResultSet rs = pst.executeQuery();
                ResultSet rssp = pstsp.executeQuery();
                // tạo một mảng để chứa dữ liệu của từng hàng trong bảng
                Object[] dulieucot = new Object[7];
                Object[] dulieucotsp = new Object[7];
                int i=0,j=0;
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
                while(rssp.next()){
                    j++;
                    dulieucotsp[0] = j;
                    dulieucotsp[1] = rssp.getString("masanpham");
                    dulieucotsp[2] = rssp.getString("tensanpham");
                    if(rssp.getInt("loai")==0){
                        dulieucotsp[3] = "Đồ ăn";
                    }
                    else if(rssp.getInt("loai")==1){
                        dulieucotsp[3] = "Đồ tráng miệng";
                    }
                    else dulieucotsp[3] = "Nước uống";
                    dulieucotsp[4] = rssp.getString("gia");
                    dulieucotsp[5] = rssp.getString("mota");
                    dulieucotsp[6] = rssp.getString("ngaytao");
                    modelproduct.addRow(dulieucotsp);
                }
                //giải phóng bộ nhớ
                pst.close();
                rs.close();
                pstsp.close();
                rssp.close();
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
        //Kiểm tra xem các đối tượng được nhập vào có trống hay không
        if(txttentaikhoan.getText().trim().equals("")||txthovaten.getText().trim().equals("")
                ||txtmatkhau.getText().trim().equals("")||txtsodienthoai.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Vui lòng nhập đầy đủ");
            //Dừng và không tiếp tục thực hiện các lệnh bên dưới
            return;
        }
        if(!isPhoneNumber(txtsodienthoai.getText())){
            JOptionPane.showMessageDialog(null,"Số điện thoại không hợp lệ");
            //Dừng và không tiếp tục thực hiện các lệnh bên dưới
            return;
        }
        if(datengaytao.getDate()==null||!formattedngayhientai.equals(dateformat.format(datengaytao.getDate()))){
            JOptionPane.showMessageDialog(null,"Ngày tạo không đúng");
            //Dừng và không tiếp tục thực hiện các lệnh bên dưới
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
                if(rs.getString("tennguoidung").equals(txttentaikhoan.getText())){
                    JOptionPane.showMessageDialog(null, "Tên tài khoản đã tồn tại");
                    //Dừng và không tiếp tục thực hiện các lệnh bên dưới
                    return;
                }
                if(rs.getString("sodienthoai").equals(txtsodienthoai.getText())){
                    JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại");
                    //Dừng và không tiếp tục thực hiện các lệnh bên dưới
                    return;
                }
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
                pst1.close();
            }
            //giải phóng bộ nhớ
            pst.close();
            rs.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }
    public void addRowsp(){
        conn = cn.ketNoi();
        //Kiểm tra xem các đối tượng được nhập vào có trống hay không
        if(txtmasanpham.getText().trim().equals("")||txttensanpham.getText().trim().equals("")
                ||txtgia.getText().trim().equals("")||txtmota.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Vui lòng nhập đầy đủ");
            //Dừng và không tiếp tục thực hiện các lệnh bên dưới
            return;
        }
        if(!StringUtils.isNumeric(txtgia.getText())){
            JOptionPane.showMessageDialog(null,"Giá không hợp lệ");
            //Dừng và không tiếp tục thực hiện các lệnh bên dưới
            return;
        }
        //tạo câu lệnh để kiểm tra các đối tượng trong CSDL.
        String sql_kiemtrasp = "Select * from sanpham where masanpham=?";
        try {
            //Tạo một PreparedStatement để truy vấn CSDL với câu lệnh SQL đã được khai báo trước đó.
            PreparedStatement pstsp = conn.prepareStatement(sql_kiemtrasp);
            //truyền giá trị đối tượng cần kiểm tra vào PreparedStatement để thực hiện truy vấn CSDL.
            pstsp.setString(1, txtmasanpham.getText());
            //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
            ResultSet rssp = pstsp.executeQuery();

            if(rssp.next()){
                if(rssp.getString("masanpham").equals(txtmasanpham.getText())){
                    JOptionPane.showMessageDialog(null, "Mã sản phẩm đã tồn tại");
                    //Dừng và không tiếp tục thực hiện các lệnh bên dưới
                    return;
                }
            }
            else{
                //hiển thị một thông báo thành công
                JOptionPane.showMessageDialog(this,"Thêm thành công");
                //thêm thông tin người dùng mới vào cơ sở dữ liệu
                String sql = "insert into sanpham(masanpham,tensanpham,loai,gia,mota,anh,ngaytao) values(?,?,?,?,?,?,GETDATE())";
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst1 = conn.prepareStatement(sql);
                //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
                pst1.setString(1, txtmasanpham.getText());
                pst1.setString(2, txttensanpham.getText());
                if(cbbloai.getSelectedItem().toString().equals("Đồ ăn")){
                    pst1.setInt(3, 0);
                }
                else if(cbbloai.getSelectedItem().toString().equals("Đồ tráng miệng")){
                    pst1.setInt(3, 1);
                }
                else pst1.setInt(3, 2);
                pst1.setFloat(4, Float.parseFloat(txtgia.getText()));
                pst1.setString(5, txtmota.getText());
                if(lachonanh.getIcon()!=null){
                    //lấy ảnh
                    Icon icon = lachonanh.getIcon();
                    //Tạo một đối tượng BufferedImage với kích thước bằng với kích thước của icon, sử dụng loại định dạng màu RGB.
                    BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                    //vẽ icon lên bufferedImage
                    Graphics g = bufferedImage.createGraphics();
                    icon.paintIcon(null, g, 0, 0);
                    //giải phóng
                    g.dispose();
                    //ByteArrayOutputStream để lưu trữ dữ liệu ảnh.
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //ImageIO.write để chuyển đổi BufferedImage thành một mảng byte với định dạng jpg và lưu vào baos.
                    ImageIO.write(bufferedImage, "jpg", baos);
                    byte[] anh = baos.toByteArray();
                    pst1.setBytes(6, anh);
                }
                else pst1.setBytes(6, null);
                //thực hiện cật nhật dữ liệu
                pst1.executeUpdate();
                //xóa hàng
                modelproduct.setRowCount(0);
                //hiển thị dữ liệu từ CSDL lên bảng
                loadBang();
                //giải phóng bộ nhớ
                pst1.close();
            }
            //giải phóng bộ nhớ
            pstsp.close();
            rssp.close();
            conn.close();
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
    public void deleteRowsp(){
        conn = cn.ketNoi();
        try {
            //xóa thông tin người dùng trong cơ sở dữ liệu
            String sql = "delete from sanpham where masanpham=?";
            int rowIndex = tableproduct.getSelectedRow(); // lấy chỉ số hàng đang được chọn
            // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
            PreparedStatement pst = conn.prepareStatement(sql);
            //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
            pst.setString(1, tableproduct.getValueAt(rowIndex, 1).toString());
            //thực hiện cật nhật dữ liệu
            pst.executeUpdate();
            //xóa hàng
            modelproduct.setRowCount(0);
            //hiển thị dữ liệu từ CSDL lên bảng
            loadBang();
            //giải phóng bộ nhớ
            pst.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }
    public void fixRow(){
        conn = cn.ketNoi();
        if(txttentaikhoan.getText().equals("")||txtmatkhau.getText().equals("")||txthovaten.getText().equals("")
                ||txtsodienthoai.getText().equals("")||datengaytao.getDate()==null){
            JOptionPane.showMessageDialog(null, "Không được bỏ trống");
            return;
        }
        //tạo câu lệnh để kiểm tra các đối tượng trong CSDL.
        String sql_kiemtra = "Select * from nguoidung where tennguoidung =?";
        try {
            int selectedRow = tableuser.getSelectedRow();
            String coltennguoidung = null;
            if (selectedRow != -1) {
                coltennguoidung = tableuser.getValueAt(selectedRow, 1).toString(); // lấy giá trị cột tên người dùng của hàng được chọn
            }
            //Tạo một PreparedStatement để truy vấn CSDL với câu lệnh SQL đã được khai báo trước đó.
            PreparedStatement pst = conn.prepareStatement(sql_kiemtra);
            //truyền giá trị đối tượng cần kiểm tra vào PreparedStatement để thực hiện truy vấn CSDL.
            pst.setString(1, coltennguoidung);
            //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                String sodienthoaihientai = rs.getString("sodienthoai");
                String sodienthoaimoi = txtsodienthoai.getText();
                if(!sodienthoaihientai.equals(sodienthoaimoi)) {
                    // Kiểm tra số điện thoại mới đã tồn tại chưa
                    String sql_checkPhone = "SELECT * FROM nguoidung WHERE sodienthoai = ?";
                    PreparedStatement pst_checkPhone = conn.prepareStatement(sql_checkPhone);
                    pst_checkPhone.setString(1, sodienthoaimoi);
                    ResultSet rs_checkPhone = pst_checkPhone.executeQuery();
                    if(rs_checkPhone.next()) {
                        JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại");
                        return;
                    }
                }
                if(!txttentaikhoan.getText().equals(coltennguoidung)){
                    JOptionPane.showMessageDialog(null, "Không được sửa tên tài khoản");
                    return;
                }
                if(!isPhoneNumber(txtsodienthoai.getText())){
                    JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ");
                    return;
                }
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
                modelproduct.setRowCount(0);
                //hiển thị dữ liệu từ CSDL lên bảng
                loadBang();
                //giải phóng bộ nhớ
                pst1.close();
            }
            else{
                JOptionPane.showMessageDialog(null, "Không được sửa tên tài khoản");
            }
            //giải phóng bộ nhớ
            rs.close();
            pst.close();
            conn.close();
        } 
        catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }  
    }
    public void fixRowsp(){
        conn = cn.ketNoi();
        //tạo câu lệnh để kiểm tra các đối tượng trong CSDL.
        String sql_kiemtra = "Select * from sanpham where masanpham =?";
        try {
            //Tạo một PreparedStatement để truy vấn CSDL với câu lệnh SQL đã được khai báo trước đó.
            PreparedStatement pst = conn.prepareStatement(sql_kiemtra);
            //truyền giá trị đối tượng cần kiểm tra vào PreparedStatement để thực hiện truy vấn CSDL.
            pst.setString(1, txtmasanpham.getText().trim());
            //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                if(txtmasanpham.getText().equals("")||txttensanpham.getText().equals("")||txtmota.getText().equals("")
                        ||txtgia.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Không được bỏ trống");
                }
                else if(!StringUtils.isNumeric(txtgia.getText())){
                    JOptionPane.showMessageDialog(null, "Giá không hợp lệ");
                }
                else{
                    //cật nhật thông tin người dùng trong cơ sở dữ liệu
                    String sql = "update sanpham set masanpham=?, tensanpham=?, loai=?,gia=?,mota=?,anh=? where masanpham=?";
                    // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                    PreparedStatement pst1 = conn.prepareStatement(sql);
                    //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
                    pst1.setString(1, txtmasanpham.getText());
                    pst1.setString(2, txttensanpham.getText());
                    if(cbbloai.getSelectedItem().toString().equals("Đồ ăn")){
                        pst1.setInt(3, 0);
                    }
                    else if(cbbloai.getSelectedItem().toString().equals("Đồ tráng miệng")){
                        pst1.setInt(3, 1);
                    }
                    else pst1.setInt(3, 2);
                    pst1.setFloat(4,Float.parseFloat(txtgia.getText()));
                    pst1.setString(5, txtmota.getText());
                    if(lachonanh.getIcon()!=null){
                        //lấy ảnh
                        Icon icon = lachonanh.getIcon();
                        //Tạo một đối tượng BufferedImage với kích thước bằng với kích thước của icon, sử dụng loại định dạng màu RGB.
                        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                        //vẽ icon lên bufferedImage
                        Graphics g = bufferedImage.createGraphics();
                        icon.paintIcon(null, g, 0, 0);
                        //giải phóng
                        g.dispose();
                        //ByteArrayOutputStream để lưu trữ dữ liệu ảnh.
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        //ImageIO.write để chuyển đổi BufferedImage thành một mảng byte với định dạng jpg và lưu vào baos.
                        ImageIO.write(bufferedImage, "jpg", baos);
                        byte[] anh = baos.toByteArray();
                        pst1.setBytes(6, anh);
                    }
                    else pst1.setBytes(6, null);
                    pst1.setString(7, txtmasanpham.getText());
                    //thực hiện cật nhật dữ liệu
                    pst1.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Sửa thành công");
                    //xóa hàng
                    modeluser.setRowCount(0);
                    modelproduct.setRowCount(0);
                    //hiển thị dữ liệu từ CSDL lên bảng
                    loadBang();
                    //giải phóng bộ nhớ
                    pst1.close();
                }
                //giải phóng bộ nhớ
                rs.close();
                pst.close();
                conn.close();
            }
            else{
                JOptionPane.showMessageDialog(null, "Không được sửa mã sản phẩm");
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
    public void timKiemsp(){
        conn = cn.ketNoi();
        if(conn!=null){
            String sql = "Select masanpham,tensanpham,loai,gia,mota,format(ngaytao,'dd/MM/yyyy') as 'ngaytao' from sanpham";
            try {
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst = conn.prepareStatement(sql);
                // thực thi câu truy vấn và lấy kết quả trả về vào đối tượng ResultSet
                ResultSet rs = pst.executeQuery();
                // tạo một mảng để chứa dữ liệu của từng hàng trong bảng
                Object[] dulieucot = new Object[7];
                int i=0;
                while(rs.next()){
                    if(rs.getString("masanpham").equals(txttimkiemsanpham.getText())){
                        i++;
                        // lấy dữ liệu các cột trong bảng nguoidung và đưa vào mảng dulieucot
                        dulieucot[0] = i;
                        dulieucot[1] = rs.getString("masanpham");
                        dulieucot[2] = rs.getString("tensanpham");
                        if(rs.getInt("loai")==0){
                            dulieucot[3] = "Đồ ăn";
                        } 
                        else if(rs.getInt("loai")==1) dulieucot[3] = "Đồ tráng miệng";
                        else dulieucot[3] = "Nước uống";
                        dulieucot[4] = rs.getFloat("gia");
                        dulieucot[5] = rs.getString("mota");
                        dulieucot[6] = rs.getString("ngaytao");
                         //xóa hàng
                        modelproduct.setRowCount(0);
                        modelproduct.addRow(dulieucot);
                    }
                     
                }
                if(txttimkiemsanpham.getText().trim().equals("")){
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

        jPanel3 = new javax.swing.JPanel();
        laexit = new javax.swing.JLabel();
        lamini = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        nguoidung = new javax.swing.JPanel();
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
        jPanel10 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        lamasanpham = new javax.swing.JLabel();
        txtmasanpham = new javax.swing.JTextField();
        txttensanpham = new javax.swing.JTextField();
        laloai = new javax.swing.JLabel();
        latensanpham = new javax.swing.JLabel();
        cbbloai = new javax.swing.JComboBox<>();
        lachonanh = new javax.swing.JLabel();
        btnxoaanh = new javax.swing.JButton();
        lagia = new javax.swing.JLabel();
        txtgia = new javax.swing.JTextField();
        lamota = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtmota = new javax.swing.JTextArea();
        btnchonanh = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableproduct = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        btnthemsp = new javax.swing.JButton();
        btnsuasp = new javax.swing.JButton();
        btnxoasp = new javax.swing.JButton();
        btntimkiemsp = new javax.swing.JButton();
        txttimkiemsanpham = new javax.swing.JTextField();
        btnresetsp = new javax.swing.JButton();
        latimkiemmasanpham = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(915, 605));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(153, 153, 153)));

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

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Giao diện Admin");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lamini)
                .addGap(16, 16, 16)
                .addComponent(laexit)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lamini, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(laexit, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jTabbedPane1.setBackground(new java.awt.Color(204, 204, 204));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setInheritsPopupMenu(true);
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(915, 600));

        nguoidung.setToolTipText("");

        jPanel1.setBackground(new java.awt.Color(153, 255, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        latentaikhoan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        latentaikhoan.setText("Tên tài khoản:");
        latentaikhoan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(latentaikhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 31));
        jPanel1.add(txttentaikhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 143, 31));
        jPanel1.add(txtmatkhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 143, 31));

        lasodienthoai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lasodienthoai.setText("Số điện thoại:");
        lasodienthoai.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(lasodienthoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 100, -1, 31));

        lamatkhau.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lamatkhau.setText("Mật khẩu:");
        lamatkhau.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(lamatkhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 77, 31));
        jPanel1.add(txthovaten, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 30, 127, 31));
        jPanel1.add(txtsodienthoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 100, 127, 31));

        langaytao.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        langaytao.setText("Ngày tạo:");
        langaytao.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(langaytao, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, 77, 31));

        lavaitro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lavaitro.setText("Vai trò:");
        lavaitro.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(lavaitro, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 30, 77, 31));

        datengaytao.setBackground(new java.awt.Color(153, 255, 204));
        jPanel1.add(datengaytao, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 100, 129, 32));

        lahovaten.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lahovaten.setText("Họ và tên:");
        lahovaten.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(lahovaten, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 94, 31));
        jPanel1.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(-64, -22, -1, -1));

        cbbvaitro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "User", "Admin" }));
        jPanel1.add(cbbvaitro, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, -1, 30));

        jPanel6.setBackground(new java.awt.Color(153, 255, 204));

        jScrollPane4.setViewportView(null);

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
            .addComponent(jScrollPane4)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
        );

        jPanel13.setBackground(new java.awt.Color(153, 255, 204));

        btnthem.setBackground(new java.awt.Color(204, 204, 204));
        btnthem.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnthem.setForeground(new java.awt.Color(102, 102, 102));
        btnthem.setText("Thêm");
        btnthem.setToolTipText("");
        btnthem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthemActionPerformed(evt);
            }
        });

        btnsua.setBackground(new java.awt.Color(204, 204, 204));
        btnsua.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnsua.setForeground(new java.awt.Color(102, 102, 102));
        btnsua.setText("Sửa");
        btnsua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsuaActionPerformed(evt);
            }
        });

        btnxoa.setBackground(new java.awt.Color(204, 204, 204));
        btnxoa.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnxoa.setForeground(new java.awt.Color(102, 102, 102));
        btnxoa.setText("Xóa");
        btnxoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxoaActionPerformed(evt);
            }
        });

        btntimkiem.setBackground(new java.awt.Color(204, 204, 204));
        btntimkiem.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btntimkiem.setForeground(new java.awt.Color(102, 102, 102));
        btntimkiem.setText("Tìm kiếm");
        btntimkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntimkiemActionPerformed(evt);
            }
        });

        txttimkiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txttimkiemKeyPressed(evt);
            }
        });

        btnreset.setBackground(new java.awt.Color(204, 204, 204));
        btnreset.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnreset.setForeground(new java.awt.Color(102, 102, 102));
        btnreset.setText("Reset");
        btnreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnresetActionPerformed(evt);
            }
        });

        radtentaikhoan.setBackground(new java.awt.Color(153, 255, 204));
        radtentaikhoan.setText("Tên tài khoản");
        radtentaikhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radtentaikhoanActionPerformed(evt);
            }
        });

        radsodienthoai.setBackground(new java.awt.Color(153, 255, 204));
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
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
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout nguoidungLayout = new javax.swing.GroupLayout(nguoidung);
        nguoidung.setLayout(nguoidungLayout);
        nguoidungLayout.setHorizontalGroup(
            nguoidungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        nguoidungLayout.setVerticalGroup(
            nguoidungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nguoidungLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Người dùng", new javax.swing.ImageIcon(getClass().getResource("/image/nhanvien.png")), nguoidung, ""); // NOI18N

        jPanel10.setToolTipText("");

        jPanel15.setBackground(new java.awt.Color(204, 204, 255));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lamasanpham.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lamasanpham.setText("Mã sản phẩm:");
        lamasanpham.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel15.add(lamasanpham, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 20, 100, 30));
        jPanel15.add(txtmasanpham, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 20, 131, 30));
        jPanel15.add(txttensanpham, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 63, 131, 30));

        laloai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        laloai.setText("Loại:");
        laloai.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel15.add(laloai, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 105, 100, -1));

        latensanpham.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        latensanpham.setText("Tên sản phẩm:");
        latensanpham.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel15.add(latensanpham, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 63, 100, 30));

        cbbloai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đồ ăn", "Đồ tráng miệng", "Nước uống" }));
        jPanel15.add(cbbloai, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 106, 131, 30));

        lachonanh.setToolTipText("Có thể không thêm");
        lachonanh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel15.add(lachonanh, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, 150, 147));

        btnxoaanh.setBackground(new java.awt.Color(153, 153, 255));
        btnxoaanh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnxoaanh.setForeground(new java.awt.Color(51, 51, 51));
        btnxoaanh.setText("Xóa ảnh");
        btnxoaanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxoaanhActionPerformed(evt);
            }
        });
        jPanel15.add(btnxoaanh, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 160, 80, -1));

        lagia.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lagia.setText("Giá:");
        lagia.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel15.add(lagia, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 148, 100, 30));
        jPanel15.add(txtgia, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 148, 131, 30));

        lamota.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lamota.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lamota.setText("Mô tả:");
        lamota.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel15.add(lamota, new org.netbeans.lib.awtextra.AbsoluteConstraints(361, 20, 131, 30));

        txtmota.setColumns(20);
        txtmota.setRows(5);
        jScrollPane1.setViewportView(txtmota);

        jPanel15.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(309, 56, -1, 122));

        btnchonanh.setBackground(new java.awt.Color(153, 153, 255));
        btnchonanh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnchonanh.setForeground(new java.awt.Color(51, 51, 51));
        btnchonanh.setText("Chọn ảnh");
        btnchonanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnchonanhActionPerformed(evt);
            }
        });
        jPanel15.add(btnchonanh, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 160, -1, -1));

        jPanel16.setBackground(new java.awt.Color(204, 204, 255));

        jScrollPane5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tableproduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Loại", "Giá", "Mô tả", "Ngày tạo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tableproduct);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 756, Short.MAX_VALUE)
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 324, Short.MAX_VALUE)
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel17.setBackground(new java.awt.Color(204, 204, 255));

        btnthemsp.setBackground(new java.awt.Color(204, 204, 204));
        btnthemsp.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnthemsp.setForeground(new java.awt.Color(102, 102, 102));
        btnthemsp.setText("Thêm");
        btnthemsp.setToolTipText("");
        btnthemsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthemspActionPerformed(evt);
            }
        });

        btnsuasp.setBackground(new java.awt.Color(204, 204, 204));
        btnsuasp.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnsuasp.setForeground(new java.awt.Color(102, 102, 102));
        btnsuasp.setText("Sửa");
        btnsuasp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsuaspActionPerformed(evt);
            }
        });

        btnxoasp.setBackground(new java.awt.Color(204, 204, 204));
        btnxoasp.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnxoasp.setForeground(new java.awt.Color(102, 102, 102));
        btnxoasp.setText("Xóa");
        btnxoasp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxoaspActionPerformed(evt);
            }
        });

        btntimkiemsp.setBackground(new java.awt.Color(204, 204, 204));
        btntimkiemsp.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btntimkiemsp.setForeground(new java.awt.Color(102, 102, 102));
        btntimkiemsp.setText("Tìm kiếm");
        btntimkiemsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntimkiemspActionPerformed(evt);
            }
        });

        btnresetsp.setBackground(new java.awt.Color(204, 204, 204));
        btnresetsp.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnresetsp.setForeground(new java.awt.Color(102, 102, 102));
        btnresetsp.setText("Reset");
        btnresetsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnresetspActionPerformed(evt);
            }
        });

        latimkiemmasanpham.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        latimkiemmasanpham.setText("Mã sản phẩm:");
        latimkiemmasanpham.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnthemsp)
                .addGap(18, 18, 18)
                .addComponent(btnsuasp)
                .addGap(18, 18, 18)
                .addComponent(btnxoasp)
                .addGap(18, 18, 18)
                .addComponent(btnresetsp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(latimkiemmasanpham)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txttimkiemsanpham, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btntimkiemsp)
                .addGap(14, 14, 14))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnthemsp, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnsuasp, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnxoasp, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btntimkiemsp, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttimkiemsanpham, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnresetsp, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(latimkiemmasanpham, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jTabbedPane1.addTab("   Sản phẩm", new javax.swing.ImageIcon(getClass().getResource("/image/sanpham.png")), jPanel10, ""); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 756, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 577, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("     Danh thu", new javax.swing.ImageIcon(getClass().getResource("/image/doanhthu.png")), jPanel9, ""); // NOI18N

        jPanel2.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel2ComponentShown(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 756, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 577, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("  Đăng xuất", new javax.swing.ImageIcon(getClass().getResource("/image/thoat.png")), jPanel2); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void radsodienthoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radsodienthoaiActionPerformed
        radtentaikhoan.setSelected(false);
    }//GEN-LAST:event_radsodienthoaiActionPerformed

    private void radtentaikhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radtentaikhoanActionPerformed
        radsodienthoai.setSelected(false);
    }//GEN-LAST:event_radtentaikhoanActionPerformed

    private void btnresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnresetActionPerformed
        resetUser();
    }//GEN-LAST:event_btnresetActionPerformed

    private void txttimkiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttimkiemKeyPressed
        if(evt.getKeyCode()==evt.VK_ENTER){
            timKiem();
        }
    }//GEN-LAST:event_txttimkiemKeyPressed

    private void btntimkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntimkiemActionPerformed
        timKiem();
    }//GEN-LAST:event_btntimkiemActionPerformed

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

    private void btnresetspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnresetspActionPerformed
        resetProduct();
    }//GEN-LAST:event_btnresetspActionPerformed

    private void btntimkiemspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntimkiemspActionPerformed
        timKiemsp();
    }//GEN-LAST:event_btntimkiemspActionPerformed

    private void btnxoaspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxoaspActionPerformed
        deleteRowsp();
    }//GEN-LAST:event_btnxoaspActionPerformed

    private void btnsuaspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsuaspActionPerformed
        fixRowsp();
    }//GEN-LAST:event_btnsuaspActionPerformed

    private void btnthemspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthemspActionPerformed
        addRowsp();
    }//GEN-LAST:event_btnthemspActionPerformed

    private void btnchonanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnchonanhActionPerformed
        //tạo hộp thoại chọn file trên màn hình
        JFileChooser chonanh = new JFileChooser();
        //chỉ hiển thị file
        chonanh.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //Hiển thị hộp thoại chọn tập tin và lưu kết quả trả về vào biến returnValue
        int returnValue = chonanh.showOpenDialog(this);
        //Kiểm tra nếu người dùng đã chọn một tập tin.
        if(returnValue==JFileChooser.APPROVE_OPTION){
            //Lấy tập tin được chọn bởi người dùng.
            File file = chonanh.getSelectedFile();
            //lấy đường dẫn file để lưu vào 1 trường trong csdl
            String duongdan = file.getAbsolutePath();
            if(file!=null){
                try {
                    // Hiển thị hình ảnh trên giao diện
                    //Tạo một đối tượng BufferedImage và đọc hình ảnh từ tệp tin đã chọn
                    BufferedImage img = ImageIO.read(file);
                    //Tạo một đối tượng ImageIcon từ đối tượng BufferedImage để có thể hiển thị hình ảnh trên giao diện người dùng.
                    ImageIcon icon = new ImageIcon(img);
                    //Thiết lập kích thước mới cho hình ảnh và tạo một đối tượng Image mới được thu nhỏ bằng phương thức getScaledInstance
                    //SCALE_SMOOTH để tạo ra hình ảnh nhỏ màu mịn và đẹp mắt
                    Image newImg = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    //Tạo một đối tượng ImageIcon mới từ đối tượng Image đã thu nhỏ để có thể hiển thị hình ảnh thu nhỏ trên giao diện người dùng
                    icon = new ImageIcon(newImg);
                    //hiển thị trên giao diện người dùng tại vị trí của lachonanh
                    lachonanh.setIcon(icon);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Không thể đọc tệp tin: " + ex.getMessage());
                }
            }
        }
    }//GEN-LAST:event_btnchonanhActionPerformed

    private void btnxoaanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxoaanhActionPerformed
        lachonanh.setIcon(null);
    }//GEN-LAST:event_btnxoaanhActionPerformed

    private void jPanel2ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel2ComponentShown
        new DangNhap().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jPanel2ComponentShown

    private void laexitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_laexitMouseClicked

    private void laexitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseEntered
        laexit.setForeground(Color.ORANGE);
    }//GEN-LAST:event_laexitMouseEntered

    private void laexitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseExited
        laexit.setForeground(Color.red);
    }//GEN-LAST:event_laexitMouseExited

    private void laminiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseClicked
        setExtendedState(ICONIFIED);
    }//GEN-LAST:event_laminiMouseClicked

    private void laminiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseEntered
        lamini.setForeground(Color.ORANGE);
    }//GEN-LAST:event_laminiMouseEntered

    private void laminiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseExited
        lamini.setForeground(Color.red);
    }//GEN-LAST:event_laminiMouseExited
    
    
    
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
    private javax.swing.JButton btnchonanh;
    private javax.swing.JButton btnreset;
    private javax.swing.JButton btnresetsp;
    private javax.swing.JButton btnsua;
    private javax.swing.JButton btnsuasp;
    private javax.swing.JButton btnthem;
    private javax.swing.JButton btnthemsp;
    private javax.swing.JButton btntimkiem;
    private javax.swing.JButton btntimkiemsp;
    private javax.swing.JButton btnxoa;
    private javax.swing.JButton btnxoaanh;
    private javax.swing.JButton btnxoasp;
    private javax.swing.JComboBox<String> cbbloai;
    private javax.swing.JComboBox<String> cbbvaitro;
    private com.toedter.calendar.JDateChooser datengaytao;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JLabel lachonanh;
    private javax.swing.JLabel laexit;
    private javax.swing.JLabel lagia;
    private javax.swing.JLabel lahovaten;
    private javax.swing.JLabel laloai;
    private javax.swing.JLabel lamasanpham;
    private javax.swing.JLabel lamatkhau;
    private javax.swing.JLabel lamini;
    private javax.swing.JLabel lamota;
    private javax.swing.JLabel langaytao;
    private javax.swing.JLabel lasodienthoai;
    private javax.swing.JLabel latensanpham;
    private javax.swing.JLabel latentaikhoan;
    private javax.swing.JLabel latimkiemmasanpham;
    private javax.swing.JLabel lavaitro;
    private javax.swing.JPanel nguoidung;
    private javax.swing.JRadioButton radsodienthoai;
    private javax.swing.JRadioButton radtentaikhoan;
    private javax.swing.JTable tableproduct;
    private javax.swing.JTable tableuser;
    private javax.swing.JTextField txtgia;
    private javax.swing.JTextField txthovaten;
    private javax.swing.JTextField txtmasanpham;
    private javax.swing.JTextField txtmatkhau;
    private javax.swing.JTextArea txtmota;
    private javax.swing.JTextField txtsodienthoai;
    private javax.swing.JTextField txttensanpham;
    private javax.swing.JTextField txttentaikhoan;
    private javax.swing.JTextField txttimkiem;
    private javax.swing.JTextField txttimkiemsanpham;
    // End of variables declaration//GEN-END:variables
}
