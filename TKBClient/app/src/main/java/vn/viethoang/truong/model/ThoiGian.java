package vn.viethoang.truong.model;

import java.sql.Date;
import java.util.ArrayList;

public class ThoiGian {
    private int maTG;
    private String ngayBatDau;
    private String ngayKetThuc;
    private ArrayList<Lich> lichs;  // chứa thứ và tiết

    public ThoiGian() {
    }

    public ThoiGian(int maTG, String ngayBatDau, String ngayKetThuc, ArrayList<Lich> lichs) {
        this.maTG = maTG;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.lichs = lichs;
    }

    public int getMaTG() {
        return maTG;
    }

    public void setMaTG(int maTG) {
        this.maTG = maTG;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public ArrayList<Lich> getLichs() {
        return lichs;
    }

    public void setLichs(ArrayList<Lich> lichs) {
        this.lichs = lichs;
    }
}
