package jframe;

import com.microsoft.sqlserver.jdbc.StringUtils;
import dao.KetNoisql;
import static dao.KiemTrasdt.isPhoneNumber;
import java.util.Date;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    // Tạo một đối tượng DefaultTableModel để hiển thị dữ liệu từ CSDL lên bảng trong giao diện.
    DefaultTableModel modelnguoidung = new DefaultTableModel();
    DefaultTableModel modelsanpham = new DefaultTableModel();
    DefaultTableModel modelkhuyenmai = new DefaultTableModel();
    //tạo điều kiện lưu
    int nd=1,sp,km;
    public GiaoDienAdmin() {
        initComponents();
        //cài đặt cbb
        setCbb();
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
    public void resetNguoidung(){
        txttentaikhoan.setText("");
        txtmatkhau.setText("");
        txtsodienthoai.setText("");
        txthovaten.setText("");
        txttimkiem.setText("");
        radtentaikhoan.setSelected(false);
        radsodienthoai.setSelected(false);
        //xóa hàng
        modelsanpham.setRowCount(0);
        modelnguoidung.setRowCount(0);
        modelkhuyenmai.setRowCount(0);
        loadBang();
    }
    public void resetSanpham(){
        txtmasanpham.setText("");
        txttensanpham.setText("");
        txtgia.setText("");
        txtmota.setText("");
        lachonanh.setIcon(null);
        //xóa hàng
        modelnguoidung.setRowCount(0);
        modelsanpham.setRowCount(0);
        modelkhuyenmai.setRowCount(0);
        loadBang();
    }
    public void resetKhuyenmai(){
        txtmakhuyenmai.setText("");
        txttenkhuyenmai.setText("");
        txtmasanphamkm.setText("");
        txtgiam.setText("");
        spindieukien.setValue(0);
        spinsoluong.setValue(0);
        datengaybatdau.setDate(null);
        datengayketthuc.setDate(null);
        radmakhuyenmai.setSelected(false);
        radmasanphamkm.setSelected(false);
        //xóa hàng
        modelnguoidung.setRowCount(0);
        modelsanpham.setRowCount(0);
        modelkhuyenmai.setRowCount(0);
        loadBang();
    }
    public void setCbb(){
        conn = cn.ketNoi();
        String sqlnguoidung = "Select tenquyen from quyen";
        String sqlsanpham = "Select tenloai from loai";
        try {
            //Tạo một PreparedStatement để truy vấn CSDL với câu lệnh SQL đã được khai báo trước đó.
            PreparedStatement pst = conn.prepareStatement(sqlnguoidung);
            PreparedStatement pstda = conn.prepareStatement(sqlsanpham);
            //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
            ResultSet rs = pst.executeQuery();
            ResultSet rsda = pstda.executeQuery();
            while(rs.next()){
                cbbquyen.addItem(rs.getString("tenquyen"));
            }
            while(rsda.next()){
                cbbloai.addItem(rsda.getString("tenloai"));
            }
            //giải phóng bộ nhớ
            pst.close();
            pstda.close();
            rs.close();
            rsda.close();
            conn.close();
        } 
        catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }
    public void hienThi(){
        tablenguoidung.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = tablenguoidung.getSelectedRow();
                    if (selectedRow != -1) {
                        // Lấy giá trị từ các cột của hàng được chọn
                        String coltentaikhoan = tablenguoidung.getModel().getValueAt(selectedRow, 1).toString();
                        String colmatkhau = tablenguoidung.getModel().getValueAt(selectedRow, 2).toString();
                        String colhovaten = tablenguoidung.getModel().getValueAt(selectedRow, 3).toString();
                        String colsodienthoai = tablenguoidung.getModel().getValueAt(selectedRow, 4).toString();
                        String colquyen = tablenguoidung.getModel().getValueAt(selectedRow, 5).toString();
//                      // Hiển thị các giá trị lên các JTextField
                        txttentaikhoan.setText(coltentaikhoan);
                        txtmatkhau.setText(colmatkhau);
                        txthovaten.setText(colhovaten);
                        txtsodienthoai.setText(colsodienthoai);
                        cbbquyen.setSelectedItem(colquyen);
                    }
                }
            }
        });
        tablesanpham.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = tablesanpham.getSelectedRow();
                    if (selectedRow != -1) {
                        conn = cn.ketNoi();
                        // Lấy giá trị từ các cột của hàng được chọn
                        String colmasanpham = tablesanpham.getModel().getValueAt(selectedRow, 1).toString();
                        String coltensanpham = tablesanpham.getModel().getValueAt(selectedRow, 2).toString();
                        String colloai = tablesanpham.getModel().getValueAt(selectedRow, 3).toString();
                        String colgia = tablesanpham.getModel().getValueAt(selectedRow, 4).toString();
                        String colmota = tablesanpham.getModel().getValueAt(selectedRow, 5).toString();
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
                            JOptionPane.showConfirmDialog(null, e);
                        }
                        
                    }
                }
            }
        });
        tablekhuyenmai.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = tablekhuyenmai.getSelectedRow();
                    if (selectedRow != -1) {
                        // Lấy giá trị từ các cột của hàng được chọn
                        String colmakhuyenmai = tablekhuyenmai.getModel().getValueAt(selectedRow, 1).toString();
                        String coltenkhuyenmai = tablekhuyenmai.getModel().getValueAt(selectedRow, 2).toString();
                        String colmasanpham = tablekhuyenmai.getModel().getValueAt(selectedRow, 3).toString();
                        String coldieukien = tablekhuyenmai.getModel().getValueAt(selectedRow, 4).toString();
                        String colsoluong = tablekhuyenmai.getModel().getValueAt(selectedRow, 5).toString();
                        String colgiam = tablekhuyenmai.getModel().getValueAt(selectedRow, 6).toString();
                        String colngaybatdau = tablekhuyenmai.getModel().getValueAt(selectedRow, 7).toString();
                        String colngayketthuc = tablekhuyenmai.getModel().getValueAt(selectedRow, 8).toString();
                        // Hiển thị các giá trị lên các JTextField
                        txtmakhuyenmai.setText(colmakhuyenmai);
                        txttenkhuyenmai.setText(coltenkhuyenmai);
                        txtmasanphamkm.setText(colmasanpham);
                        spindieukien.setValue(Integer.parseInt(coldieukien));
                        spinsoluong.setValue(Integer.parseInt(colsoluong));
                        txtgiam.setText(colgiam);
                        //định dạng ngày tháng theo mẫu "dd/MM/yyyy"
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            //formatter.parse(colngaytao) chuyển date thành string
                            datengaybatdau.setDate(formatter.parse(colngaybatdau));
                            datengayketthuc.setDate(formatter.parse(colngayketthuc));
                        } catch (Exception e) {
                            JOptionPane.showConfirmDialog(null, e);
                        }
                    }
                }
            }
        });
    }
    public void updateTable(){
        conn = cn.ketNoi();
        if(conn!=null){
            String sql = "SELECT *, format(ngaytao,'dd/MM/yyyy') as 'ngaytaom' FROM nguoidung nd INNER JOIN quyen q ON nd.maquyen = q.maquyen";
            String sqlsp = "Select *, format(ngaytao,'dd/MM/yyyy') as 'ngaytaom' from sanpham INNER JOIN loai on sanpham.maloai=loai.maloai";
            String sqlkm = "select *, format(ngaybatdau,'dd/MM/yyyy') as 'ngaybatdaum', format(ngayketthuc,'dd/MM/yyyy') as 'ngayketthucm', format(ngaytao,'dd/MM/yyyy') as 'ngaytaom' from khuyenmai";
            try {
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst = conn.prepareStatement(sql);
                PreparedStatement pstsp = conn.prepareStatement(sqlsp);
                PreparedStatement pstkm = conn.prepareStatement(sqlkm);
                // thực thi câu truy vấn và lấy kết quả trả về vào đối tượng ResultSet
                ResultSet rs = pst.executeQuery();
                ResultSet rssp = pstsp.executeQuery();
                ResultSet rskm = pstkm.executeQuery();
                // tạo một mảng để chứa dữ liệu của từng hàng trong bảng
                Object[] dulieucot = new Object[7];
                Object[] dulieucotsp = new Object[7];
                Object[] dulieucotkm = new Object[10];
                int i=0,j=0,k=0;
                while(rs.next()){
                    i++;
                    // lấy dữ liệu các cột trong bảng nguoidung và đưa vào mảng dulieucot
                    dulieucot[0] = i;
                    dulieucot[1] = rs.getString("tennguoidung");
                    dulieucot[2] = rs.getString("matkhau");
                    dulieucot[3] = rs.getString("hovaten");
                    dulieucot[4] = rs.getString("sodienthoai");
                    dulieucot[5] = rs.getString("tenquyen");
                    dulieucot[6] = rs.getString("ngaytaom");
                    modelnguoidung.addRow(dulieucot);
                }
                while(rssp.next()){
                    j++;
                    dulieucotsp[0] = j;
                    dulieucotsp[1] = rssp.getString("masanpham");
                    dulieucotsp[2] = rssp.getString("tensanpham");
                    dulieucotsp[3] = rssp.getString("tenloai");
                    dulieucotsp[4] = rssp.getString("gia");
                    dulieucotsp[5] = rssp.getString("mota");
                    dulieucotsp[6] = rssp.getString("ngaytaom");
                    modelsanpham.addRow(dulieucotsp);
                }
                while(rskm.next()){
                    k++;
                    dulieucotkm[0] = k;
                    dulieucotkm[1] = rskm.getString("makhuyenmai");
                    dulieucotkm[2] = rskm.getString("tenkhuyenmai");
                    dulieucotkm[3] = rskm.getString("masanpham");
                    dulieucotkm[4] = rskm.getString("dieukien");
                    dulieucotkm[5] = rskm.getString("soluong");
                    dulieucotkm[6] = rskm.getString("giam");
                    dulieucotkm[7] = rskm.getString("ngaybatdaum");
                    dulieucotkm[8] = rskm.getString("ngayketthucm");
                    dulieucotkm[9] = rskm.getString("ngaytaom");
                    modelkhuyenmai.addRow(dulieucotkm);
                }
                //giải phóng bộ nhớ
                pst.close();
                rs.close();
                pstsp.close();
                rssp.close();
                pstkm.close();
                rskm.close();
                conn.close();
            } catch (Exception e) {
                JOptionPane.showConfirmDialog(null, e);
            }
        }
    }
    public void loadBang(){
        Object colnguoidung[] = {"STT","Tên tài khoản","Mật khẩu","Họ và tên","Số điện thoại","Vai trò","Ngày tạo"};
        Object colsanpham[] = {"STT","Mã sản phẩm","Tên sản phẩm","Loại","Giá","Mô tả","Ngày tạo"};
        Object colkhuyenmai[] = {"STT","Mã khuyến mãi","Tên khuyến mãi","Mã sản phẩm","Điều kiện","Số lượng","Giảm(%)","Ngày bắt đầu","Ngày kết thúc","Ngày tạo"};
        // đặt tên cho các cột của bảng
        modelnguoidung.setColumnIdentifiers(colnguoidung);
        modelsanpham.setColumnIdentifiers(colsanpham);
        modelkhuyenmai.setColumnIdentifiers(colkhuyenmai);
        //đặt mô hình dữ liệu cho bảng
        tablenguoidung.setModel(modelnguoidung);
        tablesanpham.setModel(modelsanpham);
        tablekhuyenmai.setModel(modelkhuyenmai);
        // tải dữ liệu từ CSDL vào JTable
        updateTable();
    }
    public void addRow(){
        conn = cn.ketNoi();
//        Date ngayhientai = new Date();
        //định dạng ngày tháng theo mẫu "dd/MM/yyyy"
//        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        //chuyển date sang string
//        String formattedngayhientai = dateformat.format(ngayhientai);
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
//        if(datengaytao.getDate()==null||!formattedngayhientai.equals(dateformat.format(datengaytao.getDate()))){
//            JOptionPane.showMessageDialog(null,"Ngày tạo không đúng");
//            //Dừng và không tiếp tục thực hiện các lệnh bên dưới
//            return;
//        }
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
                String sql = "insert into nguoidung(tennguoidung,matkhau,hovaten,sodienthoai,maquyen) values(?,?,?,?,?)";
                String sqlq = "select * from quyen where tenquyen=?";
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst1 = conn.prepareStatement(sql);
                PreparedStatement pstq = conn.prepareStatement(sqlq);
                //truyền giá trị cần kiểm tra vào
                pstq.setString(1, cbbquyen.getSelectedItem().toString());
                //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
                ResultSet rsq = pstq.executeQuery();
                //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
                pst1.setString(1, txttentaikhoan.getText());
                pst1.setString(2, txtmatkhau.getText());
                pst1.setString(3, txthovaten.getText());
                pst1.setString(4, txtsodienthoai.getText());
                if(rsq.next()){
                    if(cbbquyen.getSelectedItem().toString().equals(rsq.getString("tenquyen"))){
                        pst1.setInt(5,rsq.getInt("maquyen") );
                    }
                }
//                pst1.setString(6, dateformat.format(datengaytao.getDate()));
                //thực hiện cật nhật dữ liệu
                pst1.executeUpdate();
                //xóa hàng
                modelnguoidung.setRowCount(0);
                //hiển thị dữ liệu từ CSDL lên bảng
                loadBang();
                //giải phóng bộ nhớ
                pst1.close();
                pstq.close();
                rsq.close();
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
                ||txtgia.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Vui lòng nhập đầy đủ");
            //Dừng và không tiếp tục thực hiện các lệnh bên dưới
            return;
        }
        //StringUtils.isNumeric() kiểm tra chuỗi có phải là số hay ko, đúng return true
        if(!StringUtils.isNumeric(txtgia.getText())||Float.parseFloat(txtgia.getText())<0){
            JOptionPane.showMessageDialog(null,"Giá không hợp lệ");
            //Dừng và không tiếp tục thực hiện các lệnh bên dưới
            return;
        }
        //tạo câu lệnh để kiểm tra các đối tượng trong CSDL.
        String sql_kiemtrada = "select * from sanpham where masanpham=?";
        try {
            //Tạo một PreparedStatement để truy vấn CSDL với câu lệnh SQL đã được khai báo trước đó.
            PreparedStatement pstda = conn.prepareStatement(sql_kiemtrada);
            //truyền giá trị đối tượng cần kiểm tra vào PreparedStatement để thực hiện truy vấn CSDL.
            pstda.setString(1, txtmasanpham.getText());
            //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
            ResultSet rsda = pstda.executeQuery();
            if(rsda.next()){
                if(rsda.getString("masanpham").equals(txtmasanpham.getText())){
                    JOptionPane.showMessageDialog(null, "Mã đồ ăn đã tồn tại");
                    //Dừng và không tiếp tục thực hiện các lệnh bên dưới
                    pstda.close();
                    rsda.close();
                    return;
                }
            }
            else{
                //hiển thị một thông báo thành công
                JOptionPane.showMessageDialog(this,"Thêm thành công");
                //thêm thông tin người dùng mới vào cơ sở dữ liệu
                String sql = "insert into sanpham(masanpham,tensanpham,maloai,gia,mota,anh) values(?,?,?,?,?,?)";
                String sqll = "select * from loai where tenloai=?";
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst1 = conn.prepareStatement(sql);
                PreparedStatement pstloai = conn.prepareStatement(sqll);
                //truyền giá trị cần kiểm tra vào
                pstloai.setString(1, cbbloai.getSelectedItem().toString());
                //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
                ResultSet rsl = pstloai.executeQuery();
                //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
                pst1.setString(1, txtmasanpham.getText());
                pst1.setString(2, txttensanpham.getText());
                if(rsl.next()){
                    if(cbbloai.getSelectedItem().toString().equals(rsl.getString("tenloai"))){
                        pst1.setInt(3, rsl.getInt("maloai"));
                    }
                }
                pst1.setFloat(4, Float.parseFloat(txtgia.getText()));
                if(txtmota.getText().trim().equals("")){
                    pst1.setString(5, "Không có mô tả");
                }
                else pst1.setString(5, txtmota.getText());
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
                modelsanpham.setRowCount(0);
                //hiển thị dữ liệu từ CSDL lên bảng
                loadBang();
                //giải phóng bộ nhớ
                pst1.close();
                pstloai.close();
                rsl.close();
            }
            //giải phóng bộ nhớ
            pstda.close();
            rsda.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }
    public void addRowkm(){
        conn = cn.ketNoi();
        //lấy ngày hiện tại trên hệ thống
        Calendar hientai = Calendar.getInstance();
        //giảm 1 ngày
        hientai.add(Calendar.DAY_OF_MONTH, -1);
        Date homqua = hientai.getTime();
        //Kiểm tra xem các đối tượng được nhập vào có trống hay không
        if(txtmakhuyenmai.getText().trim().equals("")||txttenkhuyenmai.getText().trim().equals("")
                ||txtmasanphamkm.getText().trim().equals("")||txtgiam.getText().trim().equals("")
                ||datengaybatdau.getDate()==null||datengayketthuc.getDate()==null){
            JOptionPane.showMessageDialog(null,"Vui lòng nhập đầy đủ");
            //Dừng và không tiếp tục thực hiện các lệnh bên dưới
            return;
        }
        if(!StringUtils.isNumeric(txtgiam.getText())||Float.parseFloat(txtgiam.getText())<=0){
            JOptionPane.showMessageDialog(null,"% giá giảm không hợp lệ");
            //Dừng và không tiếp tục thực hiện các lệnh bên dưới
            return;
        }
        if(datengaybatdau.getDate().compareTo(homqua)<0||datengayketthuc.getDate().compareTo(datengaybatdau.getDate())<0){
            JOptionPane.showMessageDialog(null,"Ngày không hợp lệ");
            //Dừng và không tiếp tục thực hiện các lệnh bên dưới
            return;
        }
        //tạo câu lệnh để kiểm tra các đối tượng trong CSDL.
        String sql_kiemtra = "Select * from khuyenmai where makhuyenmai=?";
        try {
            //Tạo một PreparedStatement để truy vấn CSDL với câu lệnh SQL đã được khai báo trước đó.
            PreparedStatement pst = conn.prepareStatement(sql_kiemtra);
            //truyền giá trị đối tượng cần kiểm tra vào PreparedStatement để thực hiện truy vấn CSDL.
            pst.setString(1, txtmakhuyenmai.getText());
            //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getString("makhuyenmai").equals(txtmakhuyenmai.getText())){
                    JOptionPane.showMessageDialog(null, "Tên tài khoản đã tồn tại");
                    //Dừng và không tiếp tục thực hiện các lệnh bên dưới
                    return;
                }
            }
            else{
                //hiển thị một thông báo thành công
                JOptionPane.showMessageDialog(this,"Thêm thành công");
                //thêm thông tin người dùng mới vào cơ sở dữ liệu
                String sql = "insert into khuyenmai(makhuyenmai,tenkhuyenmai,masanpham,dieukien,soluong,giam,ngaybatdau,ngayketthuc,ngaytao) values(?,?,?,?,?,?,?,?,getdate())";
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst1 = conn.prepareStatement(sql);
                //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
                pst1.setString(1, txtmakhuyenmai.getText());
                pst1.setString(2, txttenkhuyenmai.getText());
                pst1.setString(3, txtmasanphamkm.getText());
                pst1.setInt(4, (int)spindieukien.getValue());
                pst1.setInt(5, (int)spinsoluong.getValue());
                pst1.setFloat(6,Float.parseFloat(txtgiam.getText()));
                //định dạng ngày tháng theo mẫu "yyyy-MM-dd" định dạng mặc định sqlserver
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                //chuyển date sang string dateformat.format(date);
                pst1.setString(7, dateformat.format(datengaybatdau.getDate()));
                pst1.setString(8, dateformat.format(datengayketthuc.getDate()));
                //thực hiện cật nhật dữ liệu
                pst1.executeUpdate();
                //xóa hàng
                modelkhuyenmai.setRowCount(0);
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
    public void deleteRow(){
        conn = cn.ketNoi();
        try {
            //xóa thông tin người dùng trong cơ sở dữ liệu
            String sql = "delete from nguoidung where tennguoidung=?";
            int rowIndex = tablenguoidung.getSelectedRow(); // lấy chỉ số hàng đang được chọn
            // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
            PreparedStatement pst1 = conn.prepareStatement(sql);
            //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
            pst1.setString(1, tablenguoidung.getValueAt(rowIndex, 1).toString());
            //thực hiện cật nhật dữ liệu
            pst1.executeUpdate();
            //xóa hàng
            modelnguoidung.setRowCount(0);
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
            int rowIndex = tablesanpham.getSelectedRow(); // lấy chỉ số hàng đang được chọn
            // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
            PreparedStatement pst = conn.prepareStatement(sql);
            //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
            pst.setString(1, tablesanpham.getValueAt(rowIndex, 1).toString());
            //thực hiện cật nhật dữ liệu
            pst.executeUpdate();
            //xóa hàng
            modelsanpham.setRowCount(0);
            //hiển thị dữ liệu từ CSDL lên bảng
            loadBang();
            //giải phóng bộ nhớ
            pst.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }
    public void deleteRowkm(){
        conn = cn.ketNoi();
        try {
            //xóa thông tin người dùng trong cơ sở dữ liệu
            String sql = "delete from khuyenmai where makhuyenmai=?";
            int rowIndex = tablekhuyenmai.getSelectedRow(); // lấy chỉ số hàng đang được chọn
            // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
            PreparedStatement pst = conn.prepareStatement(sql);
            //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
            pst.setString(1, tablekhuyenmai.getValueAt(rowIndex, 1).toString());
            //thực hiện cật nhật dữ liệu
            pst.executeUpdate();
            //xóa hàng
            modelkhuyenmai.setRowCount(0);
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
                ||txtsodienthoai.getText().equals("")){
//                ||datengaytao.getDate()==null){
            JOptionPane.showMessageDialog(null, "Không được bỏ trống");
            return;
        }
        //tạo câu lệnh để kiểm tra các đối tượng trong CSDL.
        String sql_kiemtra = "Select * from nguoidung where tennguoidung =?";
        try {
            int selectedRow = tablenguoidung.getSelectedRow();
            String coltennguoidung = null;
            if (selectedRow != -1) {
                coltennguoidung = tablenguoidung.getValueAt(selectedRow, 1).toString(); // lấy giá trị cột tên người dùng của hàng được chọn
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
                        pst_checkPhone.close();
                        rs_checkPhone.close();
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
                String sql = "update nguoidung set tennguoidung=?, matkhau=?, hovaten=?,sodienthoai=?,maquyen=? where tennguoidung=?";
                String sqlq = "select * from quyen where tenquyen=?";
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst1 = conn.prepareStatement(sql);
                PreparedStatement pstq = conn.prepareStatement(sqlq);
                //truyền giá trị cần kiểm tra vào
                pstq.setString(1, cbbquyen.getSelectedItem().toString());
                //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
                ResultSet rsq = pstq.executeQuery();
                //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
                pst1.setString(1, txttentaikhoan.getText());
                pst1.setString(2, txtmatkhau.getText());
                pst1.setString(3, txthovaten.getText());
                pst1.setString(4, txtsodienthoai.getText());
                if(rsq.next()){
                    if(cbbquyen.getSelectedItem().toString().equals(rsq.getString("tenquyen"))){
                        pst1.setInt(5,rsq.getInt("maquyen") );
                    }
                }
                //định dạng ngày tháng theo mẫu "dd/MM/yyyy"
//                SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
//                pst1.setString(6, dateformat.format(datengaytao.getDate()));
                pst1.setString(6, txttentaikhoan.getText());
                //thực hiện cật nhật dữ liệu
                pst1.executeUpdate();
                JOptionPane.showMessageDialog(this, "Sửa thành công");
                //xóa hàng
                modelnguoidung.setRowCount(0);
                modelsanpham.setRowCount(0);
                modelkhuyenmai.setRowCount(0);
                //hiển thị dữ liệu từ CSDL lên bảng
                loadBang();
                //giải phóng bộ nhớ
                pst1.close();
                pstq.close();
                rsq.close();
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
        if(txtmasanpham.getText().equals("")||txttensanpham.getText().equals("")||txtmota.getText().equals("")
                ||txtgia.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Không được bỏ trống");
        }
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
                if(!StringUtils.isNumeric(txtgia.getText())){
                    JOptionPane.showMessageDialog(null, "Giá không hợp lệ");
                    return;
                }
                //cật nhật thông tin người dùng trong cơ sở dữ liệu
                String sql = "update sanpham set masanpham=?, tensanpham=?, maloai=?,gia=?,mota=?,anh=? where masanpham=?";
                String sqll = "select * from loai where tenloai=?";
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst1 = conn.prepareStatement(sql);
                PreparedStatement pstl = conn.prepareStatement(sqll);
                //truyền giá trị cần kiểm tra vào
                pstl.setString(1, cbbloai.getSelectedItem().toString());
                //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
                ResultSet rsl = pstl.executeQuery();
                //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
                pst1.setString(1, txtmasanpham.getText());
                pst1.setString(2, txttensanpham.getText());
                if(rsl.next()){
                    if(cbbloai.getSelectedItem().toString().equals(rsl.getString("tenloai"))){
                        pst1.setInt(3, rsl.getInt("maloai"));
                    }
                }
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
                modelnguoidung.setRowCount(0);
                modelsanpham.setRowCount(0);
                modelkhuyenmai.setRowCount(0);
                //hiển thị dữ liệu từ CSDL lên bảng
                loadBang();
                //giải phóng bộ nhớ
                pst1.close();
                pstl.close();
                rsl.close();
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
    public void fixRowkm(){
        conn = cn.ketNoi();
        if(txtmakhuyenmai.getText().trim().equals("")||txttenkhuyenmai.getText().trim().equals("")
                ||txtmasanphamkm.getText().trim().equals("")||txtgiam.getText().trim().equals("")
                ||datengaybatdau.getDate()==null||datengayketthuc.getDate()==null){
//                ||datengaytao.getDate()==null){
            JOptionPane.showMessageDialog(null, "Không được bỏ trống");
            return;
        }
        //tạo câu lệnh để kiểm tra các đối tượng trong CSDL.
        String sql_kiemtra = "Select * from khuyenmai where makhuyenmai=?";
        try {
            int selectedRow = tablekhuyenmai.getSelectedRow();
            String colmakhuyenmai = null;
            if (selectedRow != -1) {
                // lấy giá trị cột tên người dùng của hàng được chọn
                colmakhuyenmai = tablekhuyenmai.getValueAt(selectedRow, 1).toString(); 
            }
            //Tạo một PreparedStatement để truy vấn CSDL với câu lệnh SQL đã được khai báo trước đó.
            PreparedStatement pst = conn.prepareStatement(sql_kiemtra);
            //truyền giá trị đối tượng cần kiểm tra vào PreparedStatement để thực hiện truy vấn CSDL.
            pst.setString(1, colmakhuyenmai);
            //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                if(!txtmakhuyenmai.getText().equals(colmakhuyenmai)){
                    JOptionPane.showMessageDialog(null, "Không được sửa mã khuyến mãi");
                    return;
                }
                if(!StringUtils.isNumeric(txtgiam.getText())||Float.parseFloat(txtgiam.getText())<=0){
                    JOptionPane.showMessageDialog(null, "% giá giảm không hợp lệ");
                    return;
                }
                //cật nhật thông tin người dùng trong cơ sở dữ liệu
                String sql = "update khuyenmai set makhuyenmai=?, tenkhuyenmai=?, masanpham=?,dieukien=?,soluong=?,giam=?,ngaybatdau=?,ngayketthuc=?,ngaytao=getdate() where makhuyenmai=?";
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst1 = conn.prepareStatement(sql);
                //truyền giá trị đối tượng cần thêm vào PreparedStatement để thực hiện truy vấn CSDL.
                pst1.setString(1, txtmakhuyenmai.getText());
                pst1.setString(2, txttenkhuyenmai.getText());
                pst1.setString(3, txtmasanphamkm.getText());
                pst.setInt(4, (int)spindieukien.getValue());
                pst.setInt(5, (int)spinsoluong.getValue());
                pst1.setFloat(6, Float.parseFloat(txtgiam.getText()));
                //định dạng ngày tháng theo mẫu "yyyy-MM-dd" mặc định của sqlserver
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                //chuyển date sang string dateformat.format(date);
                pst1.setString(7, dateformat.format(datengaybatdau.getDate()));
                pst1.setString(8, dateformat.format(datengayketthuc.getDate()));
                pst1.setString(9, txtmakhuyenmai.getText());
                //thực hiện cật nhật dữ liệu
                pst1.executeUpdate();
                JOptionPane.showMessageDialog(this, "Sửa thành công");
                //xóa hàng
                modelnguoidung.setRowCount(0);
                modelsanpham.setRowCount(0);
                modelkhuyenmai.setRowCount(0);
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
    public void timKiem(){
        conn = cn.ketNoi();
        String sql = "SELECT *, format(ngaytao,'dd/MM/yyyy') as 'ngaytaom' FROM nguoidung nd INNER JOIN quyen q ON nd.maquyen = q.maquyen";
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
                    dulieucot[5] = rs.getString("tenquyen");
                    dulieucot[6] = rs.getString("ngaytaom");
                     //xóa hàng
                    modelnguoidung.setRowCount(0);
                    modelnguoidung.addRow(dulieucot); 
                }
                else if(radsodienthoai.isSelected()&&rs.getString("sodienthoai").equals(txttimkiem.getText())){
                    i++;
                    // lấy dữ liệu các cột trong bảng nguoidung và đưa vào mảng dulieucot
                    dulieucot[0] = i;
                    dulieucot[1] = rs.getString("tennguoidung");
                    dulieucot[2] = rs.getString("matkhau");
                    dulieucot[3] = rs.getString("hovaten");
                    dulieucot[4] = rs.getString("sodienthoai");
                    dulieucot[5] = rs.getString("tenquyen");
                    dulieucot[6] = rs.getString("ngaytaom");
                     //xóa hàng
                    modelnguoidung.setRowCount(0);
                    modelnguoidung.addRow(dulieucot); 
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
    public void timKiemsp(){
        conn = cn.ketNoi();
        if(conn!=null){
//            String sql = "Select masanpham,tensanpham,maloai,gia,mota,format(ngaytao,'dd/MM/yyyy') as 'ngaytao' from sanpham";
            String sql = "Select *, format(ngaytao,'dd/MM/yyyy') as 'ngaytaom' from sanpham INNER JOIN loai on sanpham.maloai=loai.maloai";
            try {
                // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
                PreparedStatement pst = conn.prepareStatement(sql);
                // thực thi câu truy vấn và lấy kết quả trả về vào đối tượng ResultSet
                ResultSet rs = pst.executeQuery();
                // tạo một mảng để chứa dữ liệu của từng hàng trong bảng
                Object[] dulieucot = new Object[7];
                int i=0;
                while(rs.next()){
                    if(rs.getString("masanpham").equals(txttimkiemsp.getText())){
                        i++;
                        // lấy dữ liệu các cột trong bảng nguoidung và đưa vào mảng dulieucot
                        dulieucot[0] = i;
                        dulieucot[1] = rs.getString("masanpham");
                        dulieucot[2] = rs.getString("tensanpham");
                        dulieucot[3] = rs.getString("tenloai");
                        dulieucot[4] = rs.getFloat("gia");
                        dulieucot[5] = rs.getString("mota");
                        dulieucot[6] = rs.getString("ngaytao");
                         //xóa hàng
                        modelsanpham.setRowCount(0);
                        modelsanpham.addRow(dulieucot);
                    }
                     
                }
                if(txttimkiemsp.getText().trim().equals("")){
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
    public void timKiemkm(){
        conn = cn.ketNoi();
        String sql = "SELECT *, format(ngaybatdau,'dd/MM/yyyy') as 'ngaybatdaum', format(ngayketthuc,'dd/MM/yyyy') as 'ngayketthucm', format(ngaytao,'dd/MM/yyyy') as 'ngaytaom' FROM khuyenmai";
        try {
            // khởi tạo đối tượng PreparedStatement để thực thi câu truy vấn
            PreparedStatement pst = conn.prepareStatement(sql);
            // thực thi câu truy vấn và lấy kết quả trả về vào đối tượng ResultSet
            ResultSet rs = pst.executeQuery();
            // tạo một mảng để chứa dữ liệu của từng hàng trong bảng
            Object[] dulieucot = new Object[10];
            int i=0;
            while(rs.next()){
                if(radmakhuyenmai.isSelected()&&rs.getString("makhuyenmai").equals(txttimkiemkm.getText())){
                    i++;
                    // lấy dữ liệu các cột trong bảng nguoidung và đưa vào mảng dulieucot
                    dulieucot[0] = i;
                    dulieucot[1] = rs.getString("makhuyenmai");
                    dulieucot[2] = rs.getString("tenkhuyenmai");
                    dulieucot[3] = rs.getString("masanpham");
                    dulieucot[4] = rs.getString("dieukien");
                    dulieucot[5] = rs.getString("soluong");
                    dulieucot[6] = rs.getString("giam");
                    dulieucot[7] = rs.getString("ngaybatdaum");
                    dulieucot[8] = rs.getString("ngayketthucm");
                    dulieucot[9] = rs.getString("ngaytaom");
                     //xóa hàng
                    modelkhuyenmai.setRowCount(0);
                    modelkhuyenmai.addRow(dulieucot); 
                }
                if(radmasanphamkm.isSelected()&&rs.getString("masanpham").equals(txttimkiemkm.getText())){
                     //làm sạch bảng
                    if(i==0) modelkhuyenmai.setRowCount(0);
                    i++;
                    // lấy dữ liệu các cột trong bảng nguoidung và đưa vào mảng dulieucot
                    dulieucot[0] = i;
                    dulieucot[1] = rs.getString("makhuyenmai");
                    dulieucot[2] = rs.getString("tenkhuyenmai");
                    dulieucot[3] = rs.getString("masanpham");
                    dulieucot[4] = rs.getString("dieukien");
                    dulieucot[5] = rs.getString("soluong");
                    dulieucot[6] = rs.getString("giam");
                    dulieucot[7] = rs.getString("ngaybatdaum");
                    dulieucot[8] = rs.getString("ngayketthucm");
                    dulieucot[9] = rs.getString("ngaytaom");
                    //thêm hàng vào bảng
                    modelkhuyenmai.addRow(dulieucot); 
                }
            }
            if(radmakhuyenmai.isSelected()==false&&radmasanphamkm.isSelected()==false){
                JOptionPane.showMessageDialog(null, "Bạn chưa chọn gì cả");
            }
            else if(txttimkiemkm.getText().trim().equals("")){
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
    public void chonAnh(){
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
    }
    public void luuFile(){
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(this); // hiển thị dialog lựa chọn vị trí lưu file
        if (userSelection == JFileChooser.APPROVE_OPTION) { // người dùng chọn Save
            File fileToSave = fileChooser.getSelectedFile(); // lấy đường dẫn người dùng đã chọn
            // sử dụng đường dẫn này để lưu dữ liệu vào file
            try {
                // Tạo kết nối đến cơ sở dữ liệu
                conn = cn.ketNoi();

                // Thực hiện truy vấn để lấy dữ liệu cần lưu
                PreparedStatement pst = conn.prepareStatement("SELECT * FROM sanpham");
                PreparedStatement pstnd = conn.prepareStatement("SELECT * FROM nguoidung");
                // thực thi câu truy vấn và lấy kết quả trả về vào đối tượng ResultSet
                ResultSet rs = pst.executeQuery();
                ResultSet rsnd = pstnd.executeQuery();

                // Tạo file mới hoặc mở file đã tồn tại
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave));

                // Ghi dữ liệu vào file
                if(sp==1){
                    int i=1;
                    while (rs.next()) {
                        String loai=null;
                        if(rs.getInt("maloai")==0){
                            loai="Đồ ăn";
                        }
                        else if(rs.getInt("maloai")==1){
                            loai="Đồ tráng miệng";
                        }
                        else loai="Nước uống";
                        String line = i++ + "," + rs.getString("masanpham") + "," + rs.getString("tensanpham") + "," + loai + "," + rs.getString("gia") + "," + 
                                rs.getString("mota") + "," + rs.getString("ngaytao");
                        writer.write(line); 
                        writer.newLine();
                    }
                    JOptionPane.showMessageDialog(null, "Lưu thành công");
                }
                if(nd==1){
                    int i=1;
                    while (rsnd.next()) {
                        String vaitro=null;
                        if(rsnd.getInt("maquyen")==0){
                            vaitro="Admin";
                        }
                        else vaitro="User";
                        String line = i++ + "," + rsnd.getString("tennguoidung") + "," + rsnd.getString("matkhau") + "," + rsnd.getString("hovaten") + "," + 
                                rsnd.getString("sodienthoai") + "," + vaitro + "," + rsnd.getString("ngaytao");
                        writer.write(line); 
                        writer.newLine();
                    }
                    JOptionPane.showMessageDialog(null, "Lưu thành công");
                }
                if(km==1){
//                    int i=1;
//                    while (rs.next()) {
//                        String loai=null;
//                        if(rs.getInt("maloai")==0){
//                            loai="Đồ ăn";
//                        }
//                        else if(rs.getInt("maloai")==1){
//                            loai="Đồ tráng miệng";
//                        }
//                        else loai="Nước uống";
//                        String line = i++ + "," + rs.getString("masanpham") + "," + rs.getString("tensanpham") + "," + loai + "," + rs.getString("gia") + "," + 
//                                rs.getString("mota") + "," + rs.getString("ngaytao");
//                        writer.write(line); 
//                        writer.newLine();
//                    }
                    JOptionPane.showMessageDialog(null, "Lưu thành công");
                }
                // Đóng file sau khi hoàn tất quá trình ghi và giải phóng bộ nhớ
                pst.close();
                pstnd.close();
                rs.close();
                rsnd.close();
                writer.close();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lamini = new javax.swing.JLabel();
        laexit = new javax.swing.JLabel();
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
        laquyen = new javax.swing.JLabel();
        lahovaten = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        cbbquyen = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablenguoidung = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        btnthem = new javax.swing.JButton();
        btnsua = new javax.swing.JButton();
        btnxoa = new javax.swing.JButton();
        btntimkiem = new javax.swing.JButton();
        txttimkiem = new javax.swing.JTextField();
        btnreset = new javax.swing.JButton();
        radtentaikhoan = new javax.swing.JRadioButton();
        radsodienthoai = new javax.swing.JRadioButton();
        sanpham = new javax.swing.JPanel();
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
        jPanel17 = new javax.swing.JPanel();
        btnthemda = new javax.swing.JButton();
        btnsuada = new javax.swing.JButton();
        btnxoada = new javax.swing.JButton();
        btntimkiemda = new javax.swing.JButton();
        txttimkiemsp = new javax.swing.JTextField();
        btnresetda = new javax.swing.JButton();
        latimkiemmasanpham = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablesanpham = new javax.swing.JTable();
        khuyenmai = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lamakhuyenmai = new javax.swing.JLabel();
        txtmakhuyenmai = new javax.swing.JTextField();
        btnthemkm = new javax.swing.JButton();
        btnsuakm = new javax.swing.JButton();
        btnxoakm = new javax.swing.JButton();
        btnresetkm = new javax.swing.JButton();
        radmakhuyenmai = new javax.swing.JRadioButton();
        radmasanphamkm = new javax.swing.JRadioButton();
        txttimkiemkm = new javax.swing.JTextField();
        btntimkiemkm = new javax.swing.JButton();
        latenkhuyenmai = new javax.swing.JLabel();
        txttenkhuyenmai = new javax.swing.JTextField();
        lamasanphamkm = new javax.swing.JLabel();
        lagiam = new javax.swing.JLabel();
        txtmasanphamkm = new javax.swing.JTextField();
        txtgiam = new javax.swing.JTextField();
        lasoluong = new javax.swing.JLabel();
        ladieukien = new javax.swing.JLabel();
        spindieukien = new javax.swing.JSpinner();
        spinsoluong = new javax.swing.JSpinner();
        lathoigianhieuluc = new javax.swing.JLabel();
        datengaybatdau = new com.toedter.calendar.JDateChooser();
        Jlabelc = new javax.swing.JLabel();
        datengayketthuc = new com.toedter.calendar.JDateChooser();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablekhuyenmai = new javax.swing.JTable();
        danhthu = new javax.swing.JPanel();
        dangxuat = new javax.swing.JPanel();
        luu = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Giao diện Admin");

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lamini, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(laexit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lamini, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(laexit))))
        );

        jTabbedPane1.setBackground(new java.awt.Color(204, 204, 204));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setInheritsPopupMenu(true);
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(915, 600));

        nguoidung.setToolTipText("");
        nguoidung.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                nguoidungComponentShown(evt);
            }
        });

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

        laquyen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        laquyen.setText("Quyền:");
        laquyen.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(laquyen, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 30, 77, 31));

        lahovaten.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lahovaten.setText("Họ và tên:");
        lahovaten.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(lahovaten, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 94, 31));
        jPanel1.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(-64, -22, -1, -1));

        jPanel1.add(cbbquyen, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, -1, 30));

        jPanel6.setBackground(new java.awt.Color(153, 255, 204));

        jScrollPane4.setViewportView(null);

        tablenguoidung.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên tài khoản", "Mật khẩu", "Họ và tên", "Số điện thoại", "Quyền", "Ngày tạo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tablenguoidung);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
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

        sanpham.setToolTipText("");
        sanpham.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                sanphamComponentShown(evt);
            }
        });

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

        jPanel15.add(cbbloai, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 106, 131, 30));

        lachonanh.setBackground(new java.awt.Color(255, 255, 255));
        lachonanh.setToolTipText("Có thể không thêm");
        lachonanh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        lachonanh.setOpaque(true);
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

        jPanel17.setBackground(new java.awt.Color(204, 204, 255));

        btnthemda.setBackground(new java.awt.Color(204, 204, 204));
        btnthemda.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnthemda.setForeground(new java.awt.Color(102, 102, 102));
        btnthemda.setText("Thêm");
        btnthemda.setToolTipText("");
        btnthemda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthemdaActionPerformed(evt);
            }
        });

        btnsuada.setBackground(new java.awt.Color(204, 204, 204));
        btnsuada.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnsuada.setForeground(new java.awt.Color(102, 102, 102));
        btnsuada.setText("Sửa");
        btnsuada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsuadaActionPerformed(evt);
            }
        });

        btnxoada.setBackground(new java.awt.Color(204, 204, 204));
        btnxoada.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnxoada.setForeground(new java.awt.Color(102, 102, 102));
        btnxoada.setText("Xóa");
        btnxoada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxoadaActionPerformed(evt);
            }
        });

        btntimkiemda.setBackground(new java.awt.Color(204, 204, 204));
        btntimkiemda.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btntimkiemda.setForeground(new java.awt.Color(102, 102, 102));
        btntimkiemda.setText("Tìm kiếm");
        btntimkiemda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntimkiemdaActionPerformed(evt);
            }
        });

        txttimkiemsp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txttimkiemspKeyPressed(evt);
            }
        });

        btnresetda.setBackground(new java.awt.Color(204, 204, 204));
        btnresetda.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnresetda.setForeground(new java.awt.Color(102, 102, 102));
        btnresetda.setText("Reset");
        btnresetda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnresetdaActionPerformed(evt);
            }
        });

        latimkiemmasanpham.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        latimkiemmasanpham.setText("Mã đồ ăn:");
        latimkiemmasanpham.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnthemda)
                .addGap(18, 18, 18)
                .addComponent(btnsuada)
                .addGap(18, 18, 18)
                .addComponent(btnxoada)
                .addGap(18, 18, 18)
                .addComponent(btnresetda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(latimkiemmasanpham)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txttimkiemsp, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btntimkiemda)
                .addGap(14, 14, 14))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnthemda, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnsuada, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnxoada, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btntimkiemda, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttimkiemsp, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnresetda, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(latimkiemmasanpham, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        jPanel16.setBackground(new java.awt.Color(204, 204, 255));

        jScrollPane5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tablesanpham.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(tablesanpham);

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

        javax.swing.GroupLayout sanphamLayout = new javax.swing.GroupLayout(sanpham);
        sanpham.setLayout(sanphamLayout);
        sanphamLayout.setHorizontalGroup(
            sanphamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
        );
        sanphamLayout.setVerticalGroup(
            sanphamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sanphamLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jTabbedPane1.addTab("Sản phẩm", new javax.swing.ImageIcon(getClass().getResource("/image/sanpham.png")), sanpham, ""); // NOI18N

        khuyenmai.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                khuyenmaiComponentShown(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(51, 255, 204));

        lamakhuyenmai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lamakhuyenmai.setText("Mã khuyến mãi:");
        lamakhuyenmai.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnthemkm.setBackground(new java.awt.Color(204, 204, 204));
        btnthemkm.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnthemkm.setForeground(new java.awt.Color(102, 102, 102));
        btnthemkm.setText("Thêm");
        btnthemkm.setToolTipText("");
        btnthemkm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthemkmActionPerformed(evt);
            }
        });

        btnsuakm.setBackground(new java.awt.Color(204, 204, 204));
        btnsuakm.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnsuakm.setForeground(new java.awt.Color(102, 102, 102));
        btnsuakm.setText("Sửa");
        btnsuakm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsuakmActionPerformed(evt);
            }
        });

        btnxoakm.setBackground(new java.awt.Color(204, 204, 204));
        btnxoakm.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnxoakm.setForeground(new java.awt.Color(102, 102, 102));
        btnxoakm.setText("Xóa");
        btnxoakm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxoakmActionPerformed(evt);
            }
        });

        btnresetkm.setBackground(new java.awt.Color(204, 204, 204));
        btnresetkm.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnresetkm.setForeground(new java.awt.Color(102, 102, 102));
        btnresetkm.setText("Reset");
        btnresetkm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnresetkmActionPerformed(evt);
            }
        });

        radmakhuyenmai.setBackground(new java.awt.Color(51, 255, 204));
        radmakhuyenmai.setText("Mã khuyến mãi");
        radmakhuyenmai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radmakhuyenmaiActionPerformed(evt);
            }
        });

        radmasanphamkm.setBackground(new java.awt.Color(51, 255, 204));
        radmasanphamkm.setText("Mã đồ ăn:");
        radmasanphamkm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radmasanphamkmActionPerformed(evt);
            }
        });

        txttimkiemkm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txttimkiemkmKeyPressed(evt);
            }
        });

        btntimkiemkm.setBackground(new java.awt.Color(204, 204, 204));
        btntimkiemkm.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btntimkiemkm.setForeground(new java.awt.Color(102, 102, 102));
        btntimkiemkm.setText("Tìm kiếm");
        btntimkiemkm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntimkiemkmActionPerformed(evt);
            }
        });

        latenkhuyenmai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        latenkhuyenmai.setText("Tên khuyến mãi:");
        latenkhuyenmai.setToolTipText("");
        latenkhuyenmai.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lamasanphamkm.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lamasanphamkm.setText("Mã sản phẩm:");
        lamasanphamkm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lagiam.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lagiam.setText("Giảm(%):");
        lagiam.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lasoluong.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lasoluong.setText("Số lượng:");
        lasoluong.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        ladieukien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ladieukien.setText("Điều kiện:");
        ladieukien.setToolTipText("");
        ladieukien.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        spindieukien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        spindieukien.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        spindieukien.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        spinsoluong.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        spinsoluong.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        lathoigianhieuluc.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lathoigianhieuluc.setText("Thời gian hiệu lực:");
        lathoigianhieuluc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        datengaybatdau.setBackground(new java.awt.Color(51, 255, 204));

        Jlabelc.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        Jlabelc.setText("-");
        Jlabelc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        datengayketthuc.setBackground(new java.awt.Color(51, 255, 204));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(latenkhuyenmai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lamakhuyenmai, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtmakhuyenmai)
                                    .addComponent(txttenkhuyenmai, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lamasanphamkm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lagiam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtgiam)
                                    .addComponent(txtmasanphamkm, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnthemkm)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(btnsuakm)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnxoakm)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnresetkm)
                                        .addGap(0, 66, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(radmakhuyenmai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(radmasanphamkm, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lathoigianhieuluc)
                                        .addGap(18, 18, 18)
                                        .addComponent(datengaybatdau, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txttimkiemkm, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btntimkiemkm))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(ladieukien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lasoluong, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(spindieukien, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spinsoluong, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Jlabelc)
                        .addGap(64, 64, 64)
                        .addComponent(datengayketthuc, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(ladieukien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                                    .addComponent(spindieukien))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lasoluong, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                                    .addComponent(spinsoluong)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(lamasanphamkm, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(lagiam, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(txtmasanphamkm, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtgiam))))
                        .addGap(13, 13, 13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lamakhuyenmai, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtmakhuyenmai, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(latenkhuyenmai, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttenkhuyenmai, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lathoigianhieuluc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(datengaybatdau, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(datengayketthuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(7, 7, 7))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(Jlabelc, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnthemkm, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnsuakm, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnxoakm, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btntimkiemkm, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttimkiemkm, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnresetkm, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(radmakhuyenmai)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radmasanphamkm)))
                .addGap(20, 20, 20))
        );

        jPanel4.setBackground(new java.awt.Color(51, 255, 204));

        tablekhuyenmai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã khuyến mãi", "Tên khuyến mãi", "Mã sản phẩm", "Điều kiện", "Số lượng", "Giảm", "Ngày bắt đầu", "Ngày kết thúc", "Ngày tạo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablekhuyenmai);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout khuyenmaiLayout = new javax.swing.GroupLayout(khuyenmai);
        khuyenmai.setLayout(khuyenmaiLayout);
        khuyenmaiLayout.setHorizontalGroup(
            khuyenmaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        khuyenmaiLayout.setVerticalGroup(
            khuyenmaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(khuyenmaiLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Khuyến mãi", new javax.swing.ImageIcon(getClass().getResource("/image/khuyenmai.png")), khuyenmai); // NOI18N

        javax.swing.GroupLayout danhthuLayout = new javax.swing.GroupLayout(danhthu);
        danhthu.setLayout(danhthuLayout);
        danhthuLayout.setHorizontalGroup(
            danhthuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 756, Short.MAX_VALUE)
        );
        danhthuLayout.setVerticalGroup(
            danhthuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 583, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Danh thu", new javax.swing.ImageIcon(getClass().getResource("/image/doanhthu.png")), danhthu, ""); // NOI18N

        dangxuat.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                dangxuatComponentShown(evt);
            }
        });

        javax.swing.GroupLayout dangxuatLayout = new javax.swing.GroupLayout(dangxuat);
        dangxuat.setLayout(dangxuatLayout);
        dangxuatLayout.setHorizontalGroup(
            dangxuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 756, Short.MAX_VALUE)
        );
        dangxuatLayout.setVerticalGroup(
            dangxuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 583, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Đăng xuất", new javax.swing.ImageIcon(getClass().getResource("/image/thoat.png")), dangxuat); // NOI18N

        luu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        luu.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                luuComponentShown(evt);
            }
        });

        javax.swing.GroupLayout luuLayout = new javax.swing.GroupLayout(luu);
        luu.setLayout(luuLayout);
        luuLayout.setHorizontalGroup(
            luuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 756, Short.MAX_VALUE)
        );
        luuLayout.setVerticalGroup(
            luuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 583, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Lưu bảng", new javax.swing.ImageIcon(getClass().getResource("/image/luu.png")), luu); // NOI18N

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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE))
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
        resetNguoidung();
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
        //Tạo một đối tượng DefaultTableModel để quản lý dữ liệu của bảng được lấy từ bảng tablenguoidung
        DefaultTableModel model = (DefaultTableModel) tablenguoidung.getModel();
        //iểm tra xem có hàng nào trong bảng hay không, ko có  = -1
        if(tablenguoidung.getSelectedRow()==-1){
            if(tablenguoidung.getRowCount()==0){ //Kiểm tra xem bảng có hàng nào không
                JOptionPane.showMessageDialog(null, "Không có gì để xóa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
            else{
                JOptionPane.showMessageDialog(null, "Chọn hàng để xóa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
        }
        else{
            //Nếu một hàng trong bảng được chọn, nó sẽ được xóa
            deleteRow();
            //            model.removeRow(tablenguoidung.getSelectedRow());
        }
    }//GEN-LAST:event_btnxoaActionPerformed

    private void btnsuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsuaActionPerformed
        //Tạo một đối tượng DefaultTableModel để quản lý dữ liệu của bảng được lấy từ bảng tablenguoidung
        DefaultTableModel model = (DefaultTableModel) tablenguoidung.getModel();
        //kiểm tra xem có hàng nào trong bảng hay không, ko có  = -1
        if(tablenguoidung.getSelectedRow()==-1){
            if(tablenguoidung.getRowCount()==0){ //Kiểm tra xem bảng có hàng nào không
                JOptionPane.showMessageDialog(null, "Không có gì để sửa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
            else{
                JOptionPane.showMessageDialog(null, "Chọn hàng để sửa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
        }
        else{
            //Nếu một hàng trong bảng được chọn, nó sẽ được xóa
            fixRow();
            //            model.removeRow(tablenguoidung.getSelectedRow());
        }
    }//GEN-LAST:event_btnsuaActionPerformed

    private void btnthemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthemActionPerformed
        addRow();
    }//GEN-LAST:event_btnthemActionPerformed

    private void btnresetdaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnresetdaActionPerformed
        resetSanpham();
    }//GEN-LAST:event_btnresetdaActionPerformed

    private void btntimkiemdaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntimkiemdaActionPerformed
        timKiemsp();
    }//GEN-LAST:event_btntimkiemdaActionPerformed

    private void btnxoadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxoadaActionPerformed
        //Tạo một đối tượng DefaultTableModel để quản lý dữ liệu của bảng được lấy từ bảng tablenguoidung
        DefaultTableModel model = (DefaultTableModel) tablesanpham.getModel();
        //iểm tra xem có hàng nào trong bảng hay không, ko có  = -1
        if(tablesanpham.getSelectedRow()==-1){
            if(tablesanpham.getRowCount()==0){ //Kiểm tra xem bảng có hàng nào không
                JOptionPane.showMessageDialog(null, "Không có gì để xóa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
            else{
                JOptionPane.showMessageDialog(null, "Chọn hàng để xóa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
        }
        else{
            //Nếu một hàng trong bảng được chọn, nó sẽ được xóa
            deleteRowsp();
        }
    }//GEN-LAST:event_btnxoadaActionPerformed

    private void btnsuadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsuadaActionPerformed
        //Tạo một đối tượng DefaultTableModel để quản lý dữ liệu của bảng được lấy từ bảng tablenguoidung
        DefaultTableModel model = (DefaultTableModel) tablesanpham.getModel();
        //kiểm tra xem có hàng nào trong bảng hay không, ko có  = -1
        if(tablesanpham.getSelectedRow()==-1){
            if(tablesanpham.getRowCount()==0){ //Kiểm tra xem bảng có hàng nào không
                JOptionPane.showMessageDialog(null, "Không có gì để sửa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
            else{
                JOptionPane.showMessageDialog(null, "Chọn hàng để sửa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
        }
        else{
            //Nếu một hàng trong bảng được chọn, nó sẽ được xóa
            fixRowsp();
        }
    }//GEN-LAST:event_btnsuadaActionPerformed

    private void btnthemdaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthemdaActionPerformed
        addRowsp();
    }//GEN-LAST:event_btnthemdaActionPerformed

    private void btnchonanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnchonanhActionPerformed
        chonAnh();
    }//GEN-LAST:event_btnchonanhActionPerformed

    private void btnxoaanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxoaanhActionPerformed
        lachonanh.setIcon(null);
    }//GEN-LAST:event_btnxoaanhActionPerformed

    private void dangxuatComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_dangxuatComponentShown
        new DangNhap().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_dangxuatComponentShown

    private void luuComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_luuComponentShown
        luuFile();
        new GiaoDienAdmin().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_luuComponentShown

    private void nguoidungComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_nguoidungComponentShown
        nd=0;
        nd++;
        sp=0;
        km=0;
    }//GEN-LAST:event_nguoidungComponentShown

    private void sanphamComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_sanphamComponentShown
        sp=0;
        sp++;
        nd=0;
        km=0;
    }//GEN-LAST:event_sanphamComponentShown

    private void laminiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseClicked
        setExtendedState(ICONIFIED);
    }//GEN-LAST:event_laminiMouseClicked

    private void laminiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseEntered
        lamini.setOpaque(true);
        lamini.setBackground(new Color(242,242,242));
    }//GEN-LAST:event_laminiMouseEntered

    private void laminiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laminiMouseExited
        lamini.setOpaque(false);
        lamini.setBackground(new Color(204,204,204));
    }//GEN-LAST:event_laminiMouseExited

    private void laexitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_laexitMouseClicked

    private void laexitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseEntered
        laexit.setBackground(Color.RED);
        //dùng để hiển thị background label
        laexit.setOpaque(true);
    }//GEN-LAST:event_laexitMouseEntered

    private void laexitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_laexitMouseExited
        laexit.setBackground(new Color(204,204,204));
        laexit.setOpaque(false);
    }//GEN-LAST:event_laexitMouseExited

    private void btnthemkmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthemkmActionPerformed
        addRowkm();
    }//GEN-LAST:event_btnthemkmActionPerformed

    private void btnsuakmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsuakmActionPerformed
        //Tạo một đối tượng DefaultTableModel để quản lý dữ liệu của bảng được lấy từ bảng tablenguoidung
        DefaultTableModel model = (DefaultTableModel) tablesanpham.getModel();
        //kiểm tra xem có hàng nào trong bảng hay không, ko có  = -1
        if(tablesanpham.getSelectedRow()==-1){
            if(tablesanpham.getRowCount()==0){ //Kiểm tra xem bảng có hàng nào không
                JOptionPane.showMessageDialog(null, "Không có gì để sửa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
            else{
                JOptionPane.showMessageDialog(null, "Chọn hàng để sửa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
        }
        else{
            //Nếu một hàng trong bảng được chọn, nó sẽ được xóa
            fixRowkm();
            //            model.removeRow(tablenguoidung.getSelectedRow());
        }
    }//GEN-LAST:event_btnsuakmActionPerformed

    private void btnxoakmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxoakmActionPerformed
        //Tạo một đối tượng DefaultTableModel để quản lý dữ liệu của bảng được lấy từ bảng tablenguoidung
        DefaultTableModel model = (DefaultTableModel) tablesanpham.getModel();
        //iểm tra xem có hàng nào trong bảng hay không, ko có  = -1
        if(tablesanpham.getSelectedRow()==-1){
            if(tablesanpham.getRowCount()==0){ //Kiểm tra xem bảng có hàng nào không
                JOptionPane.showMessageDialog(null, "Không có gì để xóa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
            else{
                JOptionPane.showMessageDialog(null, "Chọn hàng để xóa","Dữ liệu User",JOptionPane.OK_OPTION);
            }
        }
        else{
            //Nếu một hàng trong bảng được chọn, nó sẽ được xóa
            deleteRowkm();
        }
    }//GEN-LAST:event_btnxoakmActionPerformed

    private void btnresetkmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnresetkmActionPerformed
        resetKhuyenmai();
    }//GEN-LAST:event_btnresetkmActionPerformed

    private void radmakhuyenmaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radmakhuyenmaiActionPerformed
        radmasanphamkm.setSelected(false);
    }//GEN-LAST:event_radmakhuyenmaiActionPerformed

    private void radmasanphamkmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radmasanphamkmActionPerformed
        radmakhuyenmai.setSelected(false);
    }//GEN-LAST:event_radmasanphamkmActionPerformed

    private void txttimkiemkmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttimkiemkmKeyPressed
        if(evt.getKeyCode()==evt.VK_ENTER){
            timKiemkm();
        }
    }//GEN-LAST:event_txttimkiemkmKeyPressed

    private void btntimkiemkmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntimkiemkmActionPerformed
        timKiemkm();
    }//GEN-LAST:event_btntimkiemkmActionPerformed

    private void txttimkiemspKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttimkiemspKeyPressed
        if(evt.getKeyCode()==evt.VK_ENTER){
            timKiem();
        }
    }//GEN-LAST:event_txttimkiemspKeyPressed

    private void khuyenmaiComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_khuyenmaiComponentShown
        km=0;
        km++;
        nd=0;
        sp=0;
    }//GEN-LAST:event_khuyenmaiComponentShown
    
    
    
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
    private javax.swing.JLabel Jlabelc;
    private javax.swing.JButton btnchonanh;
    private javax.swing.JButton btnreset;
    private javax.swing.JButton btnresetda;
    private javax.swing.JButton btnresetkm;
    private javax.swing.JButton btnsua;
    private javax.swing.JButton btnsuada;
    private javax.swing.JButton btnsuakm;
    private javax.swing.JButton btnthem;
    private javax.swing.JButton btnthemda;
    private javax.swing.JButton btnthemkm;
    private javax.swing.JButton btntimkiem;
    private javax.swing.JButton btntimkiemda;
    private javax.swing.JButton btntimkiemkm;
    private javax.swing.JButton btnxoa;
    private javax.swing.JButton btnxoaanh;
    private javax.swing.JButton btnxoada;
    private javax.swing.JButton btnxoakm;
    private javax.swing.JComboBox<String> cbbloai;
    private javax.swing.JComboBox<String> cbbquyen;
    private javax.swing.JPanel dangxuat;
    private javax.swing.JPanel danhthu;
    private com.toedter.calendar.JDateChooser datengaybatdau;
    private com.toedter.calendar.JDateChooser datengayketthuc;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JPanel khuyenmai;
    private javax.swing.JLabel lachonanh;
    private javax.swing.JLabel ladieukien;
    private javax.swing.JLabel laexit;
    private javax.swing.JLabel lagia;
    private javax.swing.JLabel lagiam;
    private javax.swing.JLabel lahovaten;
    private javax.swing.JLabel laloai;
    private javax.swing.JLabel lamakhuyenmai;
    private javax.swing.JLabel lamasanpham;
    private javax.swing.JLabel lamasanphamkm;
    private javax.swing.JLabel lamatkhau;
    private javax.swing.JLabel lamini;
    private javax.swing.JLabel lamota;
    private javax.swing.JLabel laquyen;
    private javax.swing.JLabel lasodienthoai;
    private javax.swing.JLabel lasoluong;
    private javax.swing.JLabel latenkhuyenmai;
    private javax.swing.JLabel latensanpham;
    private javax.swing.JLabel latentaikhoan;
    private javax.swing.JLabel lathoigianhieuluc;
    private javax.swing.JLabel latimkiemmasanpham;
    private javax.swing.JPanel luu;
    private javax.swing.JPanel nguoidung;
    private javax.swing.JRadioButton radmakhuyenmai;
    private javax.swing.JRadioButton radmasanphamkm;
    private javax.swing.JRadioButton radsodienthoai;
    private javax.swing.JRadioButton radtentaikhoan;
    private javax.swing.JPanel sanpham;
    private javax.swing.JSpinner spindieukien;
    private javax.swing.JSpinner spinsoluong;
    private javax.swing.JTable tablekhuyenmai;
    private javax.swing.JTable tablenguoidung;
    private javax.swing.JTable tablesanpham;
    private javax.swing.JTextField txtgia;
    private javax.swing.JTextField txtgiam;
    private javax.swing.JTextField txthovaten;
    private javax.swing.JTextField txtmakhuyenmai;
    private javax.swing.JTextField txtmasanpham;
    private javax.swing.JTextField txtmasanphamkm;
    private javax.swing.JTextField txtmatkhau;
    private javax.swing.JTextArea txtmota;
    private javax.swing.JTextField txtsodienthoai;
    private javax.swing.JTextField txttenkhuyenmai;
    private javax.swing.JTextField txttensanpham;
    private javax.swing.JTextField txttentaikhoan;
    private javax.swing.JTextField txttimkiem;
    private javax.swing.JTextField txttimkiemkm;
    private javax.swing.JTextField txttimkiemsp;
    // End of variables declaration//GEN-END:variables
}
