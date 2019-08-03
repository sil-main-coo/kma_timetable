package vn.viethoang.truong.tkbclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.viethoang.truong.model.HocPhan;
import vn.viethoang.truong.model.Lich;
import vn.viethoang.truong.model.LichDiaDiem;
import vn.viethoang.truong.model.ThoiGian;

public class TKBTask extends AsyncTask<String, Void, HashMap<Integer, HocPhan>> {
    public AsyncResponse delegate;
    private ProgressDialog progressDialog;
    private Context context;
    private String json;
    private String check = "Login missed";

    public TKBTask(Context context, AsyncResponse delegate) {
        this.context= context;
        this.delegate = delegate;
    }

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(HashMap<Integer, HocPhan> output);
    }



    @Override
    protected HashMap<Integer, HocPhan> doInBackground(String... strings) {
        HashMap <Integer, HocPhan> hmTKB= new HashMap<>();


        StringBuilder duLieu;
        try {
            URL url = new URL("http://kmatkb.freevar.com/TKB/hihi.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Uri.Builder uri = new Uri.Builder();
            uri.appendQueryParameter("Username", strings[0]);
            uri.appendQueryParameter("Password", strings[1]);
            String duLieuPOST = uri.build().getEncodedQuery();  // Trả về kiểu chuỗi

            OutputStream outputStream = connection.getOutputStream();  // Lấy luồng ra
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);

            writer.write(duLieuPOST);  // Ghi dữ liệu POST vào luồng output stream
            writer.flush();
            writer.close();

            outputStreamWriter.close();
            outputStream.close();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            duLieu = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                duLieu.append(line);
            }
            json = duLieu.toString();
            Log.d("KIEMTRA", json);
//            Toast.makeText(context, json, Toast.LENGTH_LONG).show();
            connection.disconnect();

            if (json.equals(check)) {
                Log.e("LOI", "DANG NHAP THAT BAI");
                return null;
            } else {
                Log.d("sts", "DANG NHAP THAnh cong");
                //Log.d("BAOCAO", "Hoàn tất POST");
                Log.d("BAOCAO", json);

                // Use JSON Ojb to reader data
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("TKB");
                //Log.e("BAOCAO", jsonArray.length()+"");

                for (int i = 1; i < jsonArray.length() - 1; i++) {
                    HocPhan hocPhan = new HocPhan();
                    JSONObject item = jsonArray.getJSONObject(i);
                    if (item.has("LopHocPhan"))
                        hocPhan.setTenHocPhan(item.getString("LopHocPhan"));
                    //Log.e("BAOCAO1", item.getString("LopHocPhan")+"");
                    if (item.has("HocPhan"))
                        hocPhan.setMaHocPhan(item.getString("HocPhan"));
                    if (item.has("DiaDiem")) {
                        JSONArray jsArr = new JSONArray(item.getString("DiaDiem"));
                        LichDiaDiem lichDiaDiem = new LichDiaDiem();
                        List<HashMap<Integer, String>> ListHMChoHocs= new ArrayList<>();
                        HashMap<Integer, String> hmDiaDiem= new HashMap<>();

                        //List<Integer> arMaTG = new ArrayList<>();
                        for (int n = 0; n < jsArr.length(); n++) {
                            JSONObject object = jsArr.getJSONObject(n);
                            if (object.has("MaThoiGians")) {
                                JSONArray jsArrMaTG = new JSONArray(object.getString("MaThoiGians"));
                                for (int m = 0; m < jsArrMaTG.length(); m++) {
                                    int maTG = jsArrMaTG.getInt(m);
                                    String choHoc = "";
                                    if (object.has("ChoHoc")) {
                                        choHoc = object.getString("ChoHoc");
                                        hmDiaDiem.put(maTG, choHoc);  // hmDia diem chứa key: mã thời gian, value: địa điểm
                                        //lichDiaDiem.setChoHoc(choHoc);
                                    }
                                    //arMaTG.add(maTG);
                                    ListHMChoHocs.add(hmDiaDiem);   // List chứa danh sách các hashmap địa điểm theo mã thời gian
                                }

                            }
                            lichDiaDiem.setListHMChoHocs(ListHMChoHocs);
                            //lichDiaDiem.setHmChoHocs(arMaTG, choHoc);


                        }
                        hocPhan.setLichDiaDiem(lichDiaDiem);
                    }
                    if (item.has("GiangVien"))
                        hocPhan.setGiangVien(item.getString("GiangVien"));
                    if (item.has("SySo")) {
                        String syso= item.getString("SySo");
                        //Log.e("SYSO", syso);
                        if(syso.equals(""))
                            hocPhan.setSySo(-1);
                        else
                            hocPhan.setSySo(item.getInt("SySo"));
                    }
                    if (item.has("SoDK")){
                        String sodk= item.getString("SoDK");
                        if(sodk.equals(""))
                            hocPhan.setSoDK(-1);
                        else
                            hocPhan.setSoDK(item.getInt("SoDK"));
                    }
                    if (item.has("SoTC")){
                        String tc= item.getString("SoTC");
                        if(tc.equals(""))
                            hocPhan.setSoTC(-1);
                        else
                            hocPhan.setSoTC(item.getInt("SoTC"));
                    }
                    if (item.has("ThoiGian")) {
                        ArrayList<ThoiGian> thoiGians = new ArrayList<>();
                        JSONObject jsOjb = new JSONObject(item.getString("ThoiGian"));
                        JSONArray jsArr = jsOjb.getJSONArray("ThoiGian");
                        for (int j = 0; j < jsArr.length(); j++) {
                            ThoiGian thoiGian = new ThoiGian();
                            JSONObject jsOjbMa = jsArr.getJSONObject(j);
                            if (jsOjbMa.has("MaThoiGian"))
                                thoiGian.setMaTG(Integer.parseInt(jsOjbMa.getString("MaThoiGian")));
                            if (jsOjbMa.has("NgayBatDau")) {
                                // Chuyển chuỗi ngày sang date để lưu vào sqlite
                                String cThoiGian= jsOjbMa.getString("NgayBatDau");
                                String[] words=cThoiGian.split("/");
                                StringBuilder builderThoiGian= new StringBuilder();

                                builderThoiGian.append(words[2]);
                                builderThoiGian.append("-");
                                builderThoiGian.append(words[1]);
                                builderThoiGian.append("-");
                                builderThoiGian.append(words[0]);

                                thoiGian.setNgayBatDau(builderThoiGian.toString()); }
                            if (jsOjbMa.has("NgayKetThuc")) {
                                // Chuyển chuỗi ngày sang date để lưu vào sqlite
                                String cThoiGian= jsOjbMa.getString("NgayKetThuc");
                                String[] words=cThoiGian.split("/");
                                StringBuilder builderThoiGian= new StringBuilder();

                                builderThoiGian.append(words[2]);
                                builderThoiGian.append("-");
                                builderThoiGian.append(words[1]);
                                builderThoiGian.append("-");
                                builderThoiGian.append(words[0]);

                                thoiGian.setNgayKetThuc(builderThoiGian.toString()); }
                            if (jsOjbMa.has("Lich")) {
                                String jsMaGoc = jsOjbMa.getString("Lich");
                                String jsMa = jsMaGoc.substring(1, jsMaGoc.length() - 1);
                                JSONArray jsArrNgay = new JSONArray(jsMa);
                                ArrayList<Lich> liches = new ArrayList<>();
                                for (int k = 0; k < jsArrNgay.length(); k++) {
                                    Lich lich = new Lich();
                                    JSONObject jsOjbThu = jsArrNgay.getJSONObject(k);
                                    if (jsOjbThu.has("Thu"))
                                        lich.setThu(Integer.parseInt(jsOjbThu.getString("Thu")));
                                    if (jsOjbThu.has("Tiet"))
                                        lich.setTiet(jsOjbThu.getString("Tiet"));
                                    liches.add(lich);
                                }
                                thoiGian.setLichs(liches);
                            }
                            thoiGians.add(thoiGian);
                        }

                        hocPhan.setThoiGians(thoiGians);

                    }

                    hmTKB.put(i, hocPhan);

                }
                //Log.e("iii", hmTKB.get(0).getTenHocPhan()+" - "+hmTKB.get(0).getGiangVien());
                return hmTKB;
            }
            } catch(MalformedURLException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
                //  } catch (JSONException e) {
                e.printStackTrace();
            } catch(JSONException e){
                e.printStackTrace();
            }


        return null;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog= new ProgressDialog(context);
        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.show();
    }


    @Override
    protected void onPostExecute(HashMap<Integer, HocPhan> s) {
        super.onPostExecute(s);

        //Log.e("iii", json);

        if(s!=null)
        {
            // Do something awesome here
            delegate.processFinish(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
         }
        else
        {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (json.equals(check))
                Toast.makeText(context,"Sai tài khoản hoặc mật khẩu !",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context,"Thời khóa biểu đang trong quá trình cập nhật !",Toast.LENGTH_SHORT).show();
        }
    }
}
