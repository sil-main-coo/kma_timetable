package vn.viethoang.truong.model;

public class Lich {
    private int thu;
    private String tiet;

    public Lich() {
    }

    public Lich(int thu, String tiet) {
        this.thu = thu;
        this.tiet = tiet;
    }

    public int getThu() {
        return thu;
    }

    public void setThu(int thu) {
        this.thu = thu;
    }

    public String getTiet() {
        return tiet;
    }

    public void setTiet(String tiet) {
        this.tiet = tiet;
    }
}
