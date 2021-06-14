package com.example.da.BLL;

import com.example.da.R;

public class Story {
    private String maCauChuyen;
    private String tenCauChuyen;
    private String noiDung;
    private String ngayDang;
    private int soLike;
    private int soComment;
    private int soShare;
    private String maTG;

    public Story() {
    }

    public Story(String maCauChuyen, String tenCauChuyen, String noiDung, String ngayDang, int soLike, int soComment, int soShare, String maTG) {
        this.maCauChuyen = maCauChuyen;
        this.tenCauChuyen = tenCauChuyen;
        this.noiDung = noiDung;
        this.ngayDang = ngayDang;
        this.soLike = soLike;
        this.soComment = soComment;
        this.soShare = soShare;
        this.maTG = maTG;
    }

    public String getMaCauChuyen() {
        return maCauChuyen;
    }

    public void setMaCauChuyen(String maCauChuyen) {
        this.maCauChuyen = maCauChuyen;
    }

    public String getTenCauChuyen() {
        return tenCauChuyen;
    }

    public void setTenCauChuyen(String tenCauChuyen) {
        this.tenCauChuyen = tenCauChuyen;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(String ngayDang) {
        this.ngayDang = ngayDang;
    }

    public int getSoLike() {
        return soLike;
    }

    public void setSoLike(int soLike) {
        this.soLike = soLike;
    }

    public int getSoComment() {
        return soComment;
    }

    public void setSoComment(int soComment) {
        this.soComment = soComment;
    }

    public int getSoShare() {
        return soShare;
    }

    public void setSoShare(int soShare) {
        this.soShare = soShare;
    }

    public String getMaTG() {
        return maTG;
    }

    public void setMaTG(String maTG) {
        this.maTG = maTG;
    }
}
