package vn.viethoang.truong.tkbclient;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import vn.viethoang.truong.tkbclient.R;

public class SplashScreenActivity extends AppCompatActivity {
    private String DATABASE_NAME = "SINHVIEN.sqlite";
    private String DB_FATH_SUFIX ="/databases/";
    private SQLiteDatabase database=null;

    private String prerencseName= "MAIN PRERENCES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (checkFirstRun(SplashScreenActivity.this, prerencseName)) {
                                xyLySaoChepCSDL_TuAssets_VaoHeThongMobile();     //GỌI HÀM XỬ LÝ SAO CHÉP
                                Intent intent = new Intent(SplashScreenActivity.this, DangNhapActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
                                if (checkSQLiteEmpty()) {
                                    Intent intent = new Intent(SplashScreenActivity.this, DangNhapActivity.class);
                                    startActivity(intent);
                                    finish();
                                    //Log.d("TESTSQLITE","Chưa có thời khóa biểu");
                                } else {
                                    Intent intent = new Intent(SplashScreenActivity.this, TKBMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    //Log.d("TESTSQLITE","Đã có thời khóa biểu");

                                }
                            }
                        }
                    });
                }
            }
        });

        thread.start();

    }

    private boolean checkFirstRun(Context context, String prerencseName) {
        boolean firstrun = context.getSharedPreferences(prerencseName, MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun){
            // Save the state
            getSharedPreferences(prerencseName, MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstrun", false)
                    .commit();
            return true;
        }
        return false;
    }

    private boolean checkSQLiteEmpty() {
        boolean empty = true;
        Cursor cur = database.rawQuery("SELECT `IdTG` FROM `THOIGIAN`", null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();

        return empty;
    }

    /*
     * 3 hàm sao chép và đọc dữ liệu từ SQLite
     */

    private void xyLySaoChepCSDL_TuAssets_VaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()){
            try{
                CopyDatabaseFromAsset();
                Log.e("KQCOPYSQLITE", "copy thành Công");
            }catch (Exception e){
                Toast.makeText(SplashScreenActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void CopyDatabaseFromAsset() {
        try{
            InputStream myInput = getAssets().open(DATABASE_NAME);
            String outFileName = layDuongDanLuuTru();
            File f = new File(getApplicationInfo().dataDir + DB_FATH_SUFIX);
            if (!f.exists()){
                f.mkdir();
            }
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffet = new byte[1024];
            int length;
            while((length= myInput.read(buffet)) > 0 ){
                myOutput.write(buffet , 0 , length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();

        }
        catch (Exception e){
            Log.e("LOI_SAO_CHEP",e.toString());
        }
    }

    private String layDuongDanLuuTru() {
        return getApplicationInfo().dataDir + DB_FATH_SUFIX +DATABASE_NAME;
    }

}
