package vn.viethoang.truong.tkbclient;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class TKBMainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nv;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    final FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();

    private Fragment fragment= null;
    private Class framentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tkb);

        /** LOAD THỜI KHÓA BIỂU **/
        TKBFragment tkbFragment= new TKBFragment();
        fragmentTransaction.add(R.id.layoutContent, tkbFragment).commit();   // Gán fragment hiển thị

        addControls();
        addEvents();
    }

    private void addEvents() {
    }

    private void addControls() {
        drawerLayout= findViewById(R.id.drawerLayout);
        nv= findViewById(R.id.nv);

        // ADD CONTROLS FOR DRAWERLAYOUT
        toggle= new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nv);
    }

    /**
     *  Hàm xử lý khi người dùng chọn menu item tại menubar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void selectItemDrawer(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.info_app:
                 framentClass = InforAppFragment.class;
                break;
            case R.id.timetable:
                 framentClass = TKBFragment.class;
                break;
            case R.id.logout:
                 framentClass = DangXuatFragment.class;
                break;
            case R.id.feedback:
                if(isNetworkConnected()){
                    Intent intent = new Intent(TKBMainActivity.this,GopYActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(TKBMainActivity.this, "Không có kết nối internet", Toast.LENGTH_LONG).show();
                }
                break;
        }

        if (menuItem.getItemId()!=R.id.feedback) {
            try {
                fragment = (Fragment) framentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }


            fragmentManager.beginTransaction().replace(R.id.layoutContent, fragment).commit();
            menuItem.setChecked(true);
            drawerLayout.closeDrawers();
        }
    }

    // Cài đặt nội dung của drawer
    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // Hàm kiểm tra có kết nối intenet chưa
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
