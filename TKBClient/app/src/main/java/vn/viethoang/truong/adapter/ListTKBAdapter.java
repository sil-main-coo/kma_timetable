package vn.viethoang.truong.adapter;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import vn.viethoang.truong.model.TKB;
import vn.viethoang.truong.tkbclient.R;

import static android.content.Context.MODE_PRIVATE;
import static vn.viethoang.truong.tkbclient.TKBFragment.DATABASE_NAME;

public class ListTKBAdapter extends RecyclerView.Adapter<ListTKBAdapter.ViewHolder>{
    private ArrayList<TKB> arrItemTKBs;
    private Context context;
    private SQLiteDatabase database = null;


    // Tạo contructer context
    public ListTKBAdapter(Context context, ArrayList<TKB> arrItemTKBs) {
        this.context = context;
        this.arrItemTKBs= arrItemTKBs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Lấy view
        View view= LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        return new ViewHolder(view);  // Khởi tạo truyền view và trả lại giá trị viewholder
    }

    // Xử lý sự kiện cho các view trong item
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txtThu.setText("Thứ "+arrItemTKBs.get(position).getThu());
        holder.txtNgay.setText(arrItemTKBs.get(position).getNgay());

        String[] words=arrItemTKBs.get(position).getNgay().split("/");
        StringBuilder builderThoiGian= new StringBuilder();

        builderThoiGian.append(words[2]);
        builderThoiGian.append("-");
        builderThoiGian.append(words[1]);
        builderThoiGian.append("-");
        builderThoiGian.append(words[0]);

        final String ngay= builderThoiGian.toString();

        //Log.e("DATE", ngay);


        //Log.e("HMAdapter", arrItemTKBs.get(position).getThu()+arrItemTKBs.get(position).getHmHocPhan().toString());
        if (arrItemTKBs.get(position).getHmHocPhan().containsKey("1,2,3"))
            holder.txt_1_3.setText(getMHP(arrItemTKBs.get(position).getHmHocPhan().get("1,2,3")));
        if (arrItemTKBs.get(position).getHmHocPhan().containsKey("4,5,6"))
            holder.txt_4_6.setText(getMHP(arrItemTKBs.get(position).getHmHocPhan().get("4,5,6")));
        if (arrItemTKBs.get(position).getHmHocPhan().containsKey("7,8,9"))
            holder.txt_7_9.setText(getMHP(arrItemTKBs.get(position).getHmHocPhan().get("7,8,9")));

        if (arrItemTKBs.get(position).getHmHocPhan().containsKey("10,11,12"))
            holder.txt_10_12.setText(getMHP(arrItemTKBs.get(position).getHmHocPhan().get("10,11,12")));
        if (arrItemTKBs.get(position).getHmHocPhan().containsKey("13,14,15,16"))
            holder.txt_13_16.setText(getMHP(arrItemTKBs.get(position).getHmHocPhan().get("13,14,15,16")));

        holder.tabRow_1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ListTKBAdapter", "Xử lý 1-3");
                if (holder.txt_1_3.getText().toString().isEmpty())
                    Toast.makeText(context, "Tiết 1-3 trống", Toast.LENGTH_LONG).show();
                else {
                    XuLyXemChiTietHocPhan(arrItemTKBs.get(position).getHmHocPhan().get("1,2,3"), ngay);
                }
            }
        });
        holder.tabRow_4_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ListTKBAdapter", "Xử lý 4-6");
                if (holder.txt_4_6.getText().toString().isEmpty())
                    Toast.makeText(context, "Tiết 4-6 trống", Toast.LENGTH_LONG).show();
                else {
                    XuLyXemChiTietHocPhan(arrItemTKBs.get(position).getHmHocPhan().get("4,5,6"), ngay);
                }
            }
        });
        holder.tabRow_7_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ListTKBAdapter", "Xử lý 7-9");
                if (holder.txt_7_9.getText().toString().isEmpty())
                    Toast.makeText(context, "Tiết 7-9 trống", Toast.LENGTH_LONG).show();
                else {
                    XuLyXemChiTietHocPhan(arrItemTKBs.get(position).getHmHocPhan().get("7,8,9"), ngay);
                }
            }
        });
        holder.tabRow_10_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ListTKBAdapter", "Xử lý 10-12");
                if (holder.txt_10_12.getText().toString().isEmpty())
                    Toast.makeText(context, "Tiết 10-12 trống", Toast.LENGTH_LONG).show();
                else {
                    XuLyXemChiTietHocPhan(arrItemTKBs.get(position).getHmHocPhan().get("10,11,12"), ngay);
                }
            }
        });
        holder.tabRow_13_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ListTKBAdapter", "Xử lý 13-16");
                if (holder.txt_13_16.getText().toString().isEmpty())
                    Toast.makeText(context, "Tiết 13-16 trống", Toast.LENGTH_LONG).show();
                else {
                    XuLyXemChiTietHocPhan(arrItemTKBs.get(position).getHmHocPhan().get("13,14,15,16"), ngay);
                }
            }
        });



    }

    private void XuLyXemChiTietHocPhan(String idHP, String ngay) {
        String DATABASE_NAME = "SINHVIEN.sqlite";
        database= context.getApplicationContext().openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        String tenHocphan="", giaoVien="", diaDiem="", maHP="";
        int sySo= -1;
        int soDK= -1;
        int soTC= -1;

        Cursor cursor=
                database.query("LOPHOCPHAN", null, "IDHocPhan=?", new String[]{idHP}
                , null, null, null);
        while(cursor.moveToNext()){
            maHP= cursor.getString(1);
            tenHocphan= cursor.getString(2);
            giaoVien= cursor.getString(3);
            sySo= cursor.getInt(4);
            soDK= cursor.getInt(5);
            soTC= cursor.getInt(6);
        }
        cursor.close();

        // Lấy ngày BD
        Cursor query1= database.rawQuery("SELECT MIN(NgayBD) FROM THOIGIAN WHERE IDHocPhan=?"
                ,new String[]{idHP} );

        query1.moveToFirst();
        String ngayBD= query1.getString(0);
        query1.close();

        // Lấy ngày KT
        Cursor query= database.rawQuery("SELECT MAX(NgayKT) FROM THOIGIAN WHERE IDHocPhan=?"
                ,new String[]{idHP} );

        query.moveToFirst();
        String ngayKT= query.getString(0);
        query.close();



        Cursor rawQuery= database.rawQuery("SELECT* FROM THOIGIAN WHERE IDHocPhan=? AND ? BETWEEN NgayBD AND NgayKT"
                ,new String[]{idHP, ngay});
        while(rawQuery.moveToNext()){
            diaDiem= rawQuery.getString(10);
        }
        rawQuery.close();

        AlertDialog.Builder dialog=  new AlertDialog.Builder(context).setTitle("CHI TIẾT HỌC PHẦN")
                .setMessage(("+ Mã học phần: "+maHP+"\n"+"+ Tên học phần: "+tenHocphan+"\n"+"+ Giáo viên: "+giaoVien+"\n"
                +"+ Sỹ số: "+sySo+"\n"+"+ Số đăng ký: "+soDK+"\n"+"+ Số tín chỉ: "+soTC+"\n"
                        +"+ Ngày BD: "+ngayBD+"\n"+"+ Ngày KT: "+ngayKT+"\n"+"+ Địa điểm: "+diaDiem))
                .setNegativeButton("OK", null)
                .setCancelable(false);

        dialog.show();
    }

    private String getMHP(String IdHP){
        String MHP="";
        database= context.getApplicationContext().openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
        Cursor rawQuery= database.rawQuery("SELECT* FROM LOPHOCPHAN WHERE IdHocPhan=?"
                ,new String[]{IdHP});
        while(rawQuery.moveToNext()){
             MHP= rawQuery.getString(1);
        }
        rawQuery.close();

        return MHP;
    }

    @Override
    public int getItemCount() {
        return (arrItemTKBs == null) ? 0 : arrItemTKBs.size();
    }

    // Khai báo ánh xạ cho các view trong item
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtThu, txtNgay;
        private TableRow tabRow_1_3, tabRow_4_6, tabRow_7_9, tabRow_10_12, tabRow_13_16;
        private TextView txt_1_3, txt_4_6, txt_7_9, txt_10_12, txt_13_16;

        public ViewHolder(View itemView) {
            super(itemView);

            txtThu= itemView.findViewById(R.id.txtThu);
            txtNgay= itemView.findViewById(R.id.txtNgay);
            tabRow_1_3= itemView.findViewById(R.id.tabrow_1_3);
            tabRow_4_6= itemView.findViewById(R.id.tabrow_4_6);
            tabRow_7_9= itemView.findViewById(R.id.tabrow_7_9);
            tabRow_10_12= itemView.findViewById(R.id.tabrow_10_12);
            tabRow_13_16= itemView.findViewById(R.id.tabrow_13_16);
            txt_1_3= itemView.findViewById(R.id.txt_1_3);
            txt_4_6= itemView.findViewById(R.id.txt_4_6);
            txt_7_9= itemView.findViewById(R.id.txt_7_9);
            txt_10_12= itemView.findViewById(R.id.txt_10_12);
            txt_13_16= itemView.findViewById(R.id.txt_13_16);


        }
    }
}
