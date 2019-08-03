package vn.viethoang.truong.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LichDiaDiem {
    private List<HashMap<Integer, String>> ListHMChoHocs;  // list mã thời gian chỗ học

    public LichDiaDiem() {
    }

    public LichDiaDiem(List<HashMap<Integer, String>> listHMChoHocs) {
        ListHMChoHocs = listHMChoHocs;
    }

    public List<HashMap<Integer, String>> getListHMChoHocs() {
        return ListHMChoHocs;
    }

    public void setListHMChoHocs(List<HashMap<Integer, String>> listHMChoHocs) {
        ListHMChoHocs = listHMChoHocs;
    }
}
