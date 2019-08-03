package vn.viethoang.truong.tkbclient;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import vn.viethoang.truong.adapter.ListTKBAdapter;
import vn.viethoang.truong.model.TKB;

import static android.content.Context.MODE_PRIVATE;


public class TKBFragment extends Fragment {


    private TextView txtThuHienTai, txtNgayHienTai;   // Hiển thị thứ và ngày hiện tại
    private int week_year_now, day_week_now;
    private String dateNow;


    private Spinner spWeekYear;
    private ArrayList<Integer> arrSpWeek;  // Phải lấy dữ liệu trong sqlite
    private ArrayAdapter<Integer> adapterSpWeek;

    private RecyclerView rcTKB;
    private ListTKBAdapter adapter;
    private ArrayList<TKB> arrItemTKBs;

    private Calendar calendar;


    public static String DATABASE_NAME = "SINHVIEN.sqlite";;
    private static SQLiteDatabase database = null;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TKBFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TKBFragment newInstance(String param1, String param2) {
        TKBFragment fragment = new TKBFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tkb, container, false);
        setHasOptionsMenu(true);  // HIỂN THỊ OPTIONSMENU TRONG FRAGMENT

        //  Mở Database
        database = getActivity().openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        calendar= Calendar.getInstance(); // Trả về ngày giờ hiện tại
        SimpleDateFormat sdf1= new SimpleDateFormat("dd/MM/yyyy");
        dateNow= sdf1.format(calendar.getTime());
        week_year_now= calendar.get(Calendar.WEEK_OF_YEAR);

        // Khai báo ánh xạ
        rcTKB= view.findViewById(R.id.Recycler_TKB);
        txtThuHienTai= view.findViewById(R.id.txtThuHienTai);
        txtNgayHienTai= view.findViewById(R.id.txtNgayHienTai);
        spWeekYear= view.findViewById(R.id.spWeekYear);

        txtNgayHienTai.setText(dateNow);

        // Lấy thứ
        day_week_now= calendar.get(Calendar.DAY_OF_WEEK);
        switch (day_week_now) {
            case Calendar.SUNDAY:
                txtThuHienTai.setText("Chủ nhật");
                txtThuHienTai.setTextColor(Color.RED);
                break;
            case Calendar.MONDAY:
                txtThuHienTai.setText("Thứ 2");
                break;

            case Calendar.TUESDAY:
                txtThuHienTai.setText("Thứ 3");
                break;
            case Calendar.WEDNESDAY:
                txtThuHienTai.setText("Thứ 4");
                break;
            case Calendar.THURSDAY:
                txtThuHienTai.setText("Thứ 5");
                break;
            case Calendar.FRIDAY:
                txtThuHienTai.setText("Thứ 6");
                break;
            case Calendar.SATURDAY:
                txtThuHienTai.setText("Thứ 7");
                break;
            default: Toast.makeText(getContext(),"Lỗi cập nhật thứ ngày", Toast.LENGTH_LONG).show();
        }

        // Hiển thị các tuần học
        showSPTuan(week_year_now);

        return view;

    }

    private void showSPTuan(int week_year_now) {
        /** SHOW CÁC TUẦN ĐÃ ĐĂNG KÝ LỊCH TẠI SPINER  **/
        int week_Max=0 , week_Min = 0;

        arrSpWeek= new ArrayList<>();

        // Lấy tuần lớn nhất
        Cursor cursor= database.rawQuery("SELECT MAX(TuanKT) FROM `THOIGIAN`", null);

        cursor.moveToFirst();
        week_Max= cursor.getInt(0);
        cursor.close();

        // Lấy tuần bé nhất
        Cursor rawQuery= database.rawQuery("SELECT MIN(TuanBD) FROM `THOIGIAN`", null);
        rawQuery.moveToFirst();
        week_Min= rawQuery.getInt(0);

        rawQuery.close();

        // Thêm số tuần vào arr
        for(int i=week_Min; i<=week_Max; i++)
            arrSpWeek.add(i);

        adapterSpWeek= new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, arrSpWeek);
        adapterSpWeek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSpWeek.notifyDataSetChanged();
        spWeekYear.setAdapter(adapterSpWeek);
        //spWeekYear.setSelected(false);

        /** XỬ LÝ KHI XEM NHIỆM VỤ CÁC TUẦN**/
        spWeekYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadTKBTrongTuan(arrSpWeek.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(),"Chưa chọn tuần !",Toast.LENGTH_SHORT).show();
            }
        });

        // Hiển thị tuần hiện tại spiner
        boolean test= false;
        for (int i=0 ; i<arrSpWeek.size(); i++){
            if (week_year_now== arrSpWeek.get(i)){
                spWeekYear.setSelection(i);
                test= true;
                break;
            }
        }

        if (!test){
            Toast.makeText(getContext(), "Tuần hiện tại chưa có lịch học", Toast.LENGTH_LONG).show();
            AlertDialog.Builder dialog=  new AlertDialog.Builder(getContext())
                    .setMessage(("Tuần hiện tại chưa có lịch học"))
                    .setNegativeButton("OK", null)
                    .setCancelable(false);
            dialog.show();
        }


    }

    private void loadTKBTrongTuan(int week_year) {
            Toast.makeText(getContext(), "Thời khóa biểu tuần: " + week_year, Toast.LENGTH_LONG).show();
            arrItemTKBs = new ArrayList<>();
            HashMap<String, String> hmHocPhan;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.WEEK_OF_YEAR, week_year);

            for (int i = 2; i <= 6; i++) {
                /** LẤY DANH SÁCH CÁC MÔN HỌC TRONG TUẦN **/
                cal.set(Calendar.DAY_OF_WEEK, i);
                String Ngay = sdf.format(cal.getTime());

                // Tìm kiếm học phần theo tuần và thứ để lấy danh sách các học phần trong tuần
                Cursor cursor = database.rawQuery("SELECT* FROM THOIGIAN WHERE Thu=? AND ? BETWEEN TuanBD AND TuanKT"
                        , new String[]{String.valueOf(i), String.valueOf(week_year)});

                hmHocPhan = new HashMap<>();
                hmHocPhan.clear();
                while (cursor.moveToNext()) {
                    String idHocPhan = cursor.getString(1);
                    String Tiet = cursor.getString(9);
                    hmHocPhan.put(Tiet, idHocPhan);
                    //Log.e("TEST MAHOCPHAN", hmHocPhan.toString());

                }
                arrItemTKBs.add(new TKB(i, Ngay, hmHocPhan));  // Danh sách thời khóa biểu các ngày
                Log.e("TEST MAHOCPHAN", hmHocPhan.toString());
                cursor.close();
            }

            adapter = new ListTKBAdapter(getContext(), arrItemTKBs);
            // Thiết lập hướng vuốt cho rcMenu. Nhớ set adapter cuối cùng
            //Bỏ khi sử dụng grid layout

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rcTKB.setLayoutManager(layoutManager);

            rcTKB.setAdapter(adapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
