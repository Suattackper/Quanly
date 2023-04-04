/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author ACER
 */
public class KiemTrasdt {
//    ^ và $ là ký tự đầu và ký tự cuối, đảm bảo rằng chuỗi chỉ chứa đúng định dạng số điện thoại và không có ký tự thừa.
//    0 là ký tự đầu tiên của số điện thoại.
//    \\d là ký tự số, tương đương với [0-9].
//    {9} đảm bảo rằng chuỗi có đúng 9 ký tự số sau ký tự đầu tiên.
    public static boolean isPhoneNumber(String str){
        String regex = "^(0\\d{9})$";
        return str.matches(regex);
    }
}
