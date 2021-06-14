package com.example.da.GUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.da.BLL.DBManager;
import com.example.da.BLL.TacGia;
import com.example.da.BLL.TaiKhoan;
import com.example.da.R;

import org.jetbrains.annotations.NotNull;

public class TaiKhoan_Activity extends AppCompatActivity {

    private DBManager dbManager;

    private EditText txtChuSoHuu, txtSDT, txtEmail, txtTaiKhoan, txtMatKhau, txtNhapLaiMK;
    private RadioButton rdNam, rdNu;
    private Button btnBoQua, btnLuu;
    RelativeLayout pnlNhapLaiMK;

    private SharedPreferences sharedPreferences;
    private boolean first = false;
    private String maTG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan_);

        txtChuSoHuu = findViewById(R.id.txtChuSoHuu);
        txtSDT = findViewById(R.id.txtSDT);
        rdNam = findViewById(R.id.rdNam);
        rdNu = findViewById(R.id.rdNu);
        txtEmail = findViewById(R.id.txtEmail);
        txtTaiKhoan = findViewById(R.id.txtTaiKhoan);
        txtMatKhau = findViewById(R.id.txtMatKhau);
        txtNhapLaiMK = findViewById(R.id.txtNhapLaiMK);
        pnlNhapLaiMK = findViewById(R.id.pnlNhapLaiMK);

        btnBoQua = findViewById(R.id.btnBoQua);
        btnLuu = findViewById(R.id.btnLuu);

        Startup();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Kiểm tra trạng thái có vừa đăng ký hay không
                sharedPreferences = getSharedPreferences("dk", MODE_PRIVATE);
                first = sharedPreferences.getBoolean("first", false);
                if(first) {
                    //Lấy dữ liệu vừa mới đăng ký
                    Intent intent = getIntent();
                    Bundle bundle = intent.getExtras();
                    if(bundle != null){
                        String taiKhoan = bundle.getString("taikhoan");
                        String matKhau = bundle.getString("matkhau");
                        TacGia tacGia = layThongTinTacGia(taiKhoan);
                        maTG = tacGia.getMaTG();
                        txtEmail.setText(tacGia.getEmail());
                        txtTaiKhoan.setText(taiKhoan);
                        txtMatKhau.setText(matKhau);
                    }
                    txtTaiKhoan.setEnabled(false);
                    txtMatKhau.setEnabled(false);
                    pnlNhapLaiMK.setVisibility(View.INVISIBLE);
                }
                else{
                    sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                    String taikhoan = sharedPreferences.getString("username", "");
                    String matKhau = sharedPreferences.getString("password", "");
                    TacGia tacGia = layThongTinTacGia(taikhoan);
                    maTG = tacGia.getMaTG();
                    txtChuSoHuu.setText(tacGia.getTenTG());
                    txtSDT.setText(tacGia.getSDT());
                    if(tacGia.getHinhAnh().trim().equals("male.png"))
                        rdNam.setChecked(true);
                    else
                        rdNu.setChecked(true);
                    txtEmail.setText(tacGia.getEmail());
                    txtTaiKhoan.setText(taikhoan);
                    txtMatKhau.setText(matKhau);

                    txtTaiKhoan.setEnabled(false);
                }
            }
        }, 100);

        btnBoQua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(first) {
                    final Dialog dialog = new Dialog(TaiKhoan_Activity.this);

                    dialog.setContentView(R.layout.dialog_boqua_luuthongtintaikhoan);

                    final Button btnYes = dialog.findViewById(R.id.btnYes_BoQua);
                    final Button btnNo = dialog.findViewById(R.id.btnNo_BoQua);

                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(TaiKhoan_Activity.this, DashboardActivity.class));
                        }
                    });
                    dialog.show();
                }
                else{
                    startActivity(new Intent(TaiKhoan_Activity.this, DashboardActivity.class));
                }
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    TacGia tacGia = new TacGia();
                    tacGia.setMaTG(maTG);
                    tacGia.setTenTG(txtChuSoHuu.getText().toString());
                    tacGia.setSDT(txtSDT.getText().toString());
                    if (rdNam.isChecked())
                        tacGia.setHinhAnh("male.png");
                    else
                        tacGia.setHinhAnh("female.png");
                    tacGia.setEmail(txtEmail.getText().toString());
                    tacGia.setTaiKhoan(txtTaiKhoan.getText().toString());

                    if(txtNhapLaiMK.getText().toString().trim().length() != 0){
                        if(txtMatKhau.getText().toString().trim().equals(txtNhapLaiMK.getText().toString().trim())){
                            TaiKhoan taiKhoan = new TaiKhoan();

                            taiKhoan.setTaiKhoan(txtTaiKhoan.getText().toString().trim());
                            taiKhoan.setMatKhau(txtMatKhau.getText().toString().trim());

                            update_TaiKhoan(taiKhoan);
                        }
                        else
                        {
                            Toast.makeText(TaiKhoan_Activity.this, "Mật khẩu và mật khẩu nhập lại không khớp với nhau", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    update_TacGia(tacGia);

                    sharedPreferences = getSharedPreferences("dk", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("first", false);
                    editor.commit();

                    startActivity(new Intent(TaiKhoan_Activity.this, DashboardActivity.class));
            }
        });
    }

    private void Startup(){
        dbManager = new DBManager(this, "QLStories.sqlite", null, 1);
        dbManager.QueryData("CREATE TABLE IF NOT EXISTS TAIKHOAN(TAIKHOAN VARCHAR(50) PRIMARY KEY, MATKHAU VARCHAR(50))");
        dbManager.QueryData("CREATE TABLE IF NOT EXISTS TACGIA(MATACGIA VARCHAR(50) PRIMARY KEY, TENTACGIA VARCHAR(50), " +
                "HINHANH VARCHAR(50), SDT NVARCHAR(1000), EMAIL VARCHAR(50), TAIKHOAN VARCHAR(50))");
    }

    @NotNull
    private TacGia layThongTinTacGia(String taiKhoan){
        TacGia tacGia = new TacGia();
        String strSQL = "SELECT * FROM TACGIA WHERE TAIKHOAN = '" + taiKhoan + "'";
        Cursor cursor = dbManager.getData(strSQL);

        while (cursor.moveToNext()) {
            tacGia.setMaTG(cursor.getString(0).toString());

            if(cursor.getString(1) != null)
                tacGia.setTenTG(cursor.getString(1).toString());
            else
                tacGia.setTenTG("");

            if(cursor.getString(2) != null)
                tacGia.setHinhAnh(cursor.getString(2).toString());
            else
                tacGia.setHinhAnh("");

            if(cursor.getString(3) != null)
                tacGia.setSDT(cursor.getString(3).toString());
            else
                tacGia.setSDT("");

            tacGia.setEmail(cursor.getString(4).toString());
            tacGia.setTaiKhoan(taiKhoan);
        }
        return tacGia;
    }

    private void update_TacGia(@NotNull TacGia tacGia){
        String strSQL = "UPDATE TACGIA SET TENTACGIA = '" + tacGia.getTenTG() + "', HINHANH = '" + tacGia.getHinhAnh() +
                "', SDT = '" + tacGia.getSDT() + "', EMAIL = '" + tacGia.getEmail() + "', TAIKHOAN = '" + tacGia.getTaiKhoan() + "' WHERE MATACGIA = '" + tacGia.getMaTG() + "'";
        dbManager.QueryData(strSQL);
    }

    private void update_TaiKhoan(@NotNull TaiKhoan taiKhoan){
        String strSQL = "UPDATE TAIKHOAN SET MATKHAU = '" + taiKhoan.getMatKhau() + "' WHERE TAIKHOAN = '" + taiKhoan.getTaiKhoan() + "'";
        dbManager.QueryData(strSQL);
    }
}