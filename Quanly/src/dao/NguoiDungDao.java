package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import jframe.GiaoDienAdmin;
import jframe.GiaoDienChinh;
import model.NguoiDung;

public class NguoiDungDao{
    //Khai báo biến kết nối CSDL
    Connection conn;
    //Tạo một PreparedStatement để truy vấn CSDL với câu lệnh SQL đã được khai báo trước đó.
    PreparedStatement pst;
    //Thực thi truy vấn CSDL và lưu kết quả trả về vào đối tượng ResultSet rs.
    ResultSet rs;
    //khởi tạo một đối tượng kết nối CSDL thông qua class ketnoisql
    KetNoisql cn = new KetNoisql();
    public NguoiDungDao() {
        
    }
    public NguoiDung dangNhap(String tentaikhoan, String matkhau){
        Connection conn = cn.ketNoi();
        NguoiDung nd=null;
        StringBuffer sb = new StringBuffer();
        //Kiểm tra xem tài khoản và mật khẩu được nhập vào có trống hay không, nếu có thì sẽ được thêm vào đối tượng StringBuffer sb.
        if(tentaikhoan.equals("")){
            sb.append("Tên tài khoản không được trống\n");
        }
        if(matkhau.equals("")){
            sb.append("Mật khẩu không được trống");
        }
        if(sb.length()>0){
            JOptionPane.showMessageDialog(null,sb.toString());
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
//                        //ẩn màn hình đăng nhập
//                        this.setVisible(false);
                    } else if (luachon == JOptionPane.NO_OPTION) {
                        new GiaoDienChinh().setVisible(true);
//                        //ẩn màn hình đăng nhập
//                        this.setVisible(false);
                    } else {
                        // Xử lý khi không chọn gì cả hoặc đóng cửa sổ
                    }
                    
                }
                else{
                    //hiển thị một thông báo thành công
                    JOptionPane.showMessageDialog(null,"Đăng nhập thành công");
                    //mở một cửa sổ mới (lớp manhinhchinh) 
                    new GiaoDienChinh(tentaikhoan,matkhau).setVisible(true);
//                    //ẩn màn hình đăng nhập
//                    this.setVisible(false);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Sai tài khoản hoặc mật khẩu");
            }
            //giải phóng bộ nhớ
            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();  
        }
        return nd;
    }
    public ArrayList<NguoiDung> layDuLieu() {
        ArrayList<NguoiDung> dsnd = new ArrayList<NguoiDung>();
        String sql = "SELECT * FROM nguoidung";
        try {
            Connection conn = cn.ketNoi();
            // Chuẩn bị câu truy vấn SQL
            pst = conn.prepareStatement(sql);
            // Thực hiện câu truy vấn SQL và nhận kết quả trả về
            rs = pst.executeQuery();
            //Lặp qua tất cả các bản ghi trong ResultSet và tạo đối tượng NguoiDung cho mỗi bản ghi
            int i=0;
            while (rs.next()) {
                String tennguoidung = rs.getString("tennguoidung");
                String matkhau = rs.getString("matkhau");
                String hovaten = rs.getString("hovaten");
                String sodienthoai = rs.getString("sodienthoai");
                int vaitro = rs.getInt("vaitro");
                String ngaytao = rs.getString("ngaytao");
                NguoiDung nd = new NguoiDung(tennguoidung, matkhau, hovaten, sodienthoai, vaitro, ngaytao);
                //Thêm đối tượng NguoiDung vào danh sách người dùng (dsnd)
                dsnd.add(i++,nd);
            }
            //Đóng ResultSet và PreparedStatement
            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Trả về danh sách người dùng
        return dsnd;
//        //Khai báo và tạo danh sách người dùng
//        ArrayList<NguoiDung> dsnd = new ArrayList<NguoiDung>();
//        try {
//            //Tạo đối tượng PreparedStatement
//            PreparedStatement ps = conn.prepareStatement("SELECT * FROM nguoidung");
//            //Thực thi câu lệnh SQL SELECT và lấy kết quả trả về vào ResultSet
//            ResultSet rs = ps.executeQuery();
//            //Lặp qua tất cả các bản ghi trong ResultSet và tạo đối tượng NguoiDung cho mỗi bản ghi
//            while (rs.next()) {
//                String tennguoidung = rs.getString("tennguoidung");
//                String matkhau = rs.getString("matkhau");
//                String hovaten = rs.getString("hovaten");
//                String sodienthoai = rs.getString("sodienthoai");
//                int vaitro = rs.getInt("vaitro");
//                String ngaytao = rs.getString("ngaytao");
//                NguoiDung nd = new NguoiDung(tennguoidung, matkhau, hovaten, sodienthoai, vaitro, ngaytao);
//                //Thêm đối tượng NguoiDung vào danh sách người dùng (dsnd)
//                dsnd.add(nd);
//            }
//            //Đóng ResultSet và PreparedStatement
//            rs.close();
//            ps.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
    public static void main(String[] args) {
        NguoiDungDao dao = new NguoiDungDao();
        ArrayList<NguoiDung> dsnd = dao.layDuLieu();
        System.out.println(dsnd.get(1));
        // Thực hiện thao tác với dsnd ở đây
        for (NguoiDung nguoiDung : dsnd) {
        System.out.println("Tên đăng nhập: " + nguoiDung.getHovaten());
        System.out.println("Họ tên: " + nguoiDung.getMatkhau());
        System.out.println("Email: " + nguoiDung.getHovaten());
        System.out.println("Tên đăng nhập: " + nguoiDung.getSodienthoai());
        System.out.println("Họ tên: " + nguoiDung.getVaitro());
        System.out.println("Email: " + nguoiDung.getNgaytao());
        // và các thông tin khác của người dùng nếu có
}
    }
}
