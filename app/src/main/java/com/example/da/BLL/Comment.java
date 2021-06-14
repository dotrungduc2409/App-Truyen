package com.example.da.BLL;

public class Comment {
    String maBL;
    String maCC;
    String tenTaiKhoan;
    String noiDungcmt;

    public Comment() {
    }

    public Comment(String maBL, String maCC, String tenTaiKhoan, String noiDungcmt) {
        this.maBL = maBL;
        this.maCC = maCC;
        this.tenTaiKhoan = tenTaiKhoan;
        this.noiDungcmt = noiDungcmt;
    }

    public String getMaBL() {
        return maBL;
    }

    public void setMaBL(String maBL) {
        this.maBL = maBL;
    }

    public String getMaCC() {
        return maCC;
    }

    public void setMaCC(String maCC) {
        this.maCC = maCC;
    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public String getNoiDungcmt() {
        return noiDungcmt;
    }

    public void setNoiDungcmt(String noiDungcmt) {
        this.noiDungcmt = noiDungcmt;
    }
}
