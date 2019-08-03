package vn.viethoang.truong.model;

import java.util.HashMap;

public class TKB {
    int Thu;
    String Ngay;
    HashMap<String, String> hmHocPhan;  // Key: Tiết, Value: Mã học phần

    public TKB() {
    }

    public TKB(int thu, String ngay, HashMap<String, String> hmHocPhan) {
        Thu = thu;
        Ngay = ngay;
        this.hmHocPhan = hmHocPhan;
    }

    public HashMap<String, String> getHmHocPhan() {
        return hmHocPhan;
    }

    public void setHmHocPhan(HashMap<String, String> hmHocPhan) {
        this.hmHocPhan = hmHocPhan;
    }


    public int getThu() {
        return Thu;
    }

    public void setThu(int thu) {
        Thu = thu;
    }

    public String getNgay() {
        return Ngay;
    }

    public void setNgay(String ngay) {
        Ngay = ngay;
    }
}
