package com.example.da.BLL;

public class TacGia {
   String maTG;
   String tenTG;
   String hinhAnh;
   String SDT;
   String email;
   String taiKhoan;

    public TacGia() {

    }

    public TacGia(String maTG, String tenTG, String hinhAnh, String SDT, String email, String taiKhoan) {
        this.maTG = maTG;
        this.tenTG = tenTG;
        this.hinhAnh = hinhAnh;
        this.SDT = SDT;
        this.email = email;
        this.taiKhoan = taiKhoan;
    }

    public String getMaTG() {
        return maTG;
    }

    public void setMaTG(String maTG) {
        this.maTG = maTG;
    }

    public String getTenTG() {
        return tenTG;
    }

    public void setTenTG(String tenTG) {
        this.tenTG = tenTG;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }
}
