package vn.viethoang.truong.tkbclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DangXuatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DangXuatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DangXuatFragment extends Fragment {
    public static String DATABASE_NAME = "SINHVIEN.sqlite";;
    private static SQLiteDatabase database = null;

    private ProgressDialog progressDialog;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DangXuatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DangXuatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DangXuatFragment newInstance(String param1, String param2) {
        DangXuatFragment fragment = new DangXuatFragment();
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

        AlertDialog.Builder dialog=  new AlertDialog.Builder(getContext())
                .setMessage(("Bạn sẽ đăng xuất ?"))
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        XuLyDangXuat();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(getActivity(), TKBMainActivity.class);
                        startActivity(intent);

                    }
                })
                .setCancelable(false);

        dialog.show();


        return null;
    }

    private void XuLyDangXuat() {
        Handler handler= new Handler();

        progressDialog= new ProgressDialog(getContext());
        progressDialog.setMessage("Đang đăng xuất...");
        progressDialog.show();
        // Xóa sạch dữ liệu trong database
        database = getActivity().openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        database.delete("THOIGIAN", null, null);
        database.delete("LOPHOCPHAN", null, null);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
            }
        }, 3000);

        Intent intent= new Intent(getActivity(), DangNhapActivity.class);
        startActivity(intent);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
