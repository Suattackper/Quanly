
package dao;
import java.sql.*;

public class KetNoisql {
    public Connection ketNoi(){
        // Khởi tạo một đối tượng Connection với giá trị null.
        Connection conn = null;
        try {
            //Đăng ký trình điều khiển JDBC của SQL Server.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //Thiết lập chuỗi kết nối với CSDL SQL Server đang chạy trên máy chủ có tên là "VvV\ANHTAN" và port là 1433, sử dụng CSDL "test" và không sử dụng SSL để mã hóa kết nối.
            String url = "jdbc:sqlserver://VvV\\ANHTAN:1433;databaseName=Quanly;encrypt=false";
            //Thiết lập tên đăng nhập của người dùng đang kết nối đến CSDL
            String user = "sa";
            //Thiết lập mật khẩu của người dùng đang kết nối đến CSDL.
            String pwd = "123456";
            //Thực hiện kết nối đến CSDL với thông tin kết nối đã được thiết lập.
            conn = DriverManager.getConnection(url, user, pwd);
            if(conn!=null){
                System.out.println("ket noi thanh cong");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return conn;
    }
}
