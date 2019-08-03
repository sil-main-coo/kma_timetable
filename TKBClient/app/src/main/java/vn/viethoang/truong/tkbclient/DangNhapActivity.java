package vn.viethoang.truong.tkbclient;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import vn.viethoang.truong.model.HocPhan;
import vn.viethoang.truong.model.ThoiGian;
import vn.viethoang.truong.tkbclient.R;


public class DangNhapActivity extends AppCompatActivity {
    private  String DATABASE_NAME = "SINHVIEN.sqlite";;
    private  SQLiteDatabase database = null;


    private EditText txtUserName, txtPassword;
    private Button btnLogin;
    private HashMap<Integer, HocPhan> hashMap;

    private String tenThongTinDangNhap="login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);

        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        addControls();
        addEvents();

    }



    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()) {
                    xyLyLogin();
                }
                else{
                    Toast.makeText(DangNhapActivity.this,"Bạn chưa kết nối Intenet !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Hàm kiểm tra có kết nối intenet chưa
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    private void xyLyLogin() {

        String userName= txtUserName.getText().toString().trim();
        String password= txtPassword.getText().toString().trim();

        if (txtUserName.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()){
            Toast.makeText(this,"Bạn chưa nhập tài khoản hoặc mật khẩu !",Toast.LENGTH_LONG).show();
        }else {
            String passwordMD5 = convertPassMd5(password).trim();
            hashMap = new HashMap<>();

            // Thực thi TKBTask lấy dữ liệu Hashmap
            new TKBTask(this, new TKBTask.AsyncResponse(){

                @Override
                public void processFinish(HashMap<Integer, HocPhan> output) {
                    //Here you will receive the result fired from async class
                    //of onPostExecute(result) method.
                    hashMap.putAll(output);  //
                    Log.e("KET QUẢ HM", hashMap.size()+"");
                    if (!hashMap.isEmpty()){
                Log.e("KET QUẢ HM", hashMap.get(9).getThoiGians().get(0).getNgayBatDau().trim()+"");
                        // Thực hiện lưu vào sqlite và chuyển màn
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        TruyenDuLieuVaoSQL(hashMap);
                        Log.e("KQLUUDL", "Hoàn tất lưu dữ liệu");
                        Intent intent= new Intent(DangNhapActivity.this, TKBMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }).execute(userName, passwordMD5);


        }


    }

    // Hàm truyền dữ liệu ở Hashmap vào SQLite
    private void TruyenDuLieuVaoSQL(HashMap<Integer, HocPhan> hashMap) {
        for(int i = 1; i<=hashMap.size(); i++){
            // dữ liệu bảng LOPHOCPHAN
            ContentValues values = new ContentValues();
            values.put("MaHocPhan", hashMap.get(i).getMaHocPhan().trim());
            values.put("TenHocPhan", hashMap.get(i).getTenHocPhan().trim());
            values.put("GiaoVien", hashMap.get(i).getGiangVien().trim());
            values.put("SySo", hashMap.get(i).getSySo());
            values.put("SoDK",hashMap.get(i).getSoDK());
            values.put("SoTC", hashMap.get(i).getSoTC());
            long r = database.insert("LOPHOCPHAN", null, values);
            Log.d("KQ THEM SQL LOPHOCPHAN", r+"");

            // dữ liệu bảng THOIGIAN
            ContentValues data = new ContentValues();
            for (ThoiGian thoiGian: hashMap.get(i).getThoiGians()){
                for(int j= 0; j<thoiGian.getLichs().size(); j++){
                    data.put("IdHocPhan", i+"");
                    data.put("MaHocPhan", hashMap.get(i).getMaHocPhan().trim());
                    data.put("MaTG", thoiGian.getMaTG());
                    data.put("NgayBD", thoiGian.getNgayBatDau());
                    data.put("NgayKT", thoiGian.getNgayKetThuc());
                    int tuanBD= chuyenNgaySangTuan(thoiGian.getNgayBatDau());
                    data.put("TuanBD", tuanBD);
                    int tuanKT= chuyenNgaySangTuan(thoiGian.getNgayKetThuc());
                    data.put("TuanKT", tuanKT);
                    data.put("Thu",thoiGian.getLichs().get(j).getThu());
                    data.put("Tiet", thoiGian.getLichs().get(j).getTiet());
                    for(int m=0; m< hashMap.get(i).getLichDiaDiem().getListHMChoHocs().size(); m++){
                        Log.d("MATG", hashMap.get(i).getLichDiaDiem().getListHMChoHocs().get(m).containsKey(thoiGian.getMaTG())+"");
                        // TH1: Khi trong học phần có nhiều địa điểm học tùy vào mã thời gian
                        // (Nếu hashmap con (có trong list cha) chứa key trùng với mã thời gian
                        if (hashMap.get(i).getLichDiaDiem().getListHMChoHocs().get(m).containsKey(thoiGian.getMaTG())){
                            data.put("DiaDiem", hashMap.get(i).getLichDiaDiem().getListHMChoHocs().get(m).get(thoiGian.getMaTG()));
                            break;
                        }
                        // TH2: Khi địa điểm của học phần là cùng một nơi trong suốt thời gian học
                        // Key 0 biểu thị cho địa điểm học phần luôn cùng 1 chỗ
                        if (hashMap.get(i).getLichDiaDiem().getListHMChoHocs().get(m).containsKey(0)){
                            data.put("DiaDiem", hashMap.get(i).getLichDiaDiem().getListHMChoHocs().get(m).get(0));
                            break;
                        }
                    }
                    long a = database.insert("THOIGIAN", null, data);
                    Log.d("KQ THEM SQL a", a+"");

                }
            }

            Log.d("Success ", i+" !");
        }
    }

    // Định dạng ngày sang tuần của năm
    private int chuyenNgaySangTuan(String ngayBatDau) {

        Calendar now = Calendar.getInstance();
        try {
            now.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(ngayBatDau));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int week = now.get(Calendar.WEEK_OF_YEAR);
        return week;
    }

    private void addControls() {
        txtUserName= findViewById(R.id.txtUserName);
        txtPassword= findViewById(R.id.txtPassword);
        btnLogin= findViewById(R.id.btnLogin);

    }

    //Mã hóa mật khẩu bằng MD5
    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Ghi nhớ đăng nhập
        SharedPreferences preferences= getSharedPreferences(tenThongTinDangNhap,MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("UserName",txtUserName.getText().toString());
        editor.putString("PassWord",txtPassword.getText().toString());
        //editor.putBoolean("SAVE",chkLuuDangNhap.isChecked());
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Lấy dữ liệu ghi nhớ đăng nhập
        SharedPreferences preferences= getSharedPreferences(tenThongTinDangNhap,MODE_PRIVATE);
        String userName= preferences.getString("UserName","");
        String passWord= preferences.getString("PassWord","");
        //boolean save= preferences.getBoolean("SAVE",false);

        txtUserName.setText(userName);
        txtPassword.setText(passWord);

    }
}
