package model;

public class NguoiDung {
    private String tennguoidung;
    private String matkhau;
    private String hovaten;
    private String sodienthoai;
    private int vaitro;
    private String ngaytao;

    public NguoiDung() {
    }

    public NguoiDung(String tennguoidung, String matkhau, String hovaten, String sodienthoai, int vaitro, String ngaytao) {
        this.tennguoidung = tennguoidung;
        this.matkhau = matkhau;
        this.hovaten = hovaten;
        this.sodienthoai = sodienthoai;
        this.vaitro = vaitro;
        this.ngaytao = ngaytao;
    }

    public String getTennguoidung() {
        return tennguoidung;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public String getHovaten() {
        return hovaten;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public int getVaitro() {
        return vaitro;
    }

    public String getNgaytao() {
        return ngaytao;
    }

    public void setTennguoidung(String tennguoidung) {
        this.tennguoidung = tennguoidung;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public void setHovaten(String hovaten) {
        this.hovaten = hovaten;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public void setVaitro(int vaitro) {
        this.vaitro = vaitro;
    }

    public void setNgaytao(String ngaytao) {
        this.ngaytao = ngaytao;
    }
}
