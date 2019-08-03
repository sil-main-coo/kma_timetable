package vn.viethoang.truong.model;


import java.util.ArrayList;

public class HocPhan {
    private String tenHocPhan;
    private String maHocPhan;
    private String giangVien;
    private int soDK;
    private int sySo;
    private int soTC;
    private LichDiaDiem lichDiaDiem;
    private ArrayList<ThoiGian> thoiGians;

    public HocPhan() {
    }

    public HocPhan(String tenHocPhan, String maHocPhan, String giangVien, int soDK, int sySo, int soTC, LichDiaDiem lichDiaDiem, ArrayList<ThoiGian> thoiGians) {
        this.tenHocPhan = tenHocPhan;
        this.maHocPhan = maHocPhan;
        this.giangVien = giangVien;
        this.soDK = soDK;
        this.sySo = sySo;
        this.soTC = soTC;
        this.lichDiaDiem = lichDiaDiem;
        this.thoiGians = thoiGians;
    }

    public String getTenHocPhan() {
        return tenHocPhan;
    }

    public void setTenHocPhan(String tenHocPhan) {
        this.tenHocPhan = tenHocPhan;
    }

    public String getMaHocPhan() {
        return maHocPhan;
    }

    public void setMaHocPhan(String maHocPhan) {
        this.maHocPhan = maHocPhan;
    }

    public String getGiangVien() {
        return giangVien;
    }

    public void setGiangVien(String giangVien) {
        this.giangVien = giangVien;
    }

    public int getSoDK() {
        return soDK;
    }

    public void setSoDK(int soDK) {
        this.soDK = soDK;
    }

    public int getSySo() {
        return sySo;
    }

    public void setSySo(int sySo) {
        this.sySo = sySo;
    }

    public int getSoTC() {
        return soTC;
    }

    public void setSoTC(int soTC) {
        this.soTC = soTC;
    }

    public LichDiaDiem getLichDiaDiem() {
        return lichDiaDiem;
    }

    public void setLichDiaDiem(LichDiaDiem lichDiaDiem) {
        this.lichDiaDiem = lichDiaDiem;
    }

    public ArrayList<ThoiGian> getThoiGians() {
        return thoiGians;
    }

    public void setThoiGians(ArrayList<ThoiGian> thoiGians) {
        this.thoiGians = thoiGians;
    }
}
