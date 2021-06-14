package com.example.da.GUI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.da.BLL.DBManager;
import com.example.da.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class WriteStory extends AppCompatActivity {

    private Button btnBack, btnSave;
    private EditText txtBody;
    private SharedPreferences sharedPreferences;
    private DBManager dbManager;

    private String maCC, tenCC, noiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_story);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        txtBody = findViewById(R.id.txtBody);

        Startup();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle == null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(WriteStory.this, DashboardActivity.class));
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog_Save();
                }
            });
        }
        else {
            maCC = bundle.getString("maCC");
            tenCC = bundle.getString("tenCC");
            noiDung = bundle.getString("noiDung");
            if(noiDung != null)
                txtBody.setText(noiDung);

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(WriteStory.this, MyStory_Activity.class));
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog_Update();
                }
            });
        }
    }

    public void Dialog_Update(){
        final Dialog dialog = new Dialog(WriteStory.this);
        dialog.setContentView(R.layout.dialog_savestory);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        final EditText txtNameStory = dialog.findViewById(R.id.txtNameStory);
        final Button btnYes = dialog.findViewById(R.id.btnYes);
        final Button btnCancel = dialog.findViewById(R.id.btnCancel);
        if(tenCC != null)
            txtNameStory.setText(tenCC);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma = "";
                if(maCC != null)
                    ma = maCC;
                String tenCC = txtNameStory.getText().toString().trim();
                String noiDung = txtBody.getText().toString().trim();

                String strSQL = "UPDATE CAUCHUYEN SET TENCAUCHUYEN = '" + tenCC + "', NOIDUNG = '" + noiDung + "' WHERE MACAUCHUYEN = '" + ma + "'";

                dbManager.QueryData(strSQL);

                startActivity(new Intent(WriteStory.this, MyStory_Activity.class));
            }
        });

        dialog.show();
    }

    public void Dialog_Save(){
        final Dialog dialog = new Dialog(WriteStory.this);
        dialog.setContentView(R.layout.dialog_savestory);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        final EditText txtNameStory = dialog.findViewById(R.id.txtNameStory);
        final Button btnYes = dialog.findViewById(R.id.btnYes);
        final Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maCC = "MCC" + String.valueOf(laySoLuongCauChuyen() + 1);
                String tenCC = txtNameStory.getText().toString().trim();
                String noiDung = txtBody.getText().toString().trim();
                long millis=System.currentTimeMillis();
                String ngayDang = new java.sql.Date(millis).toString();
                int soLike = 0;
                int soComment = 0;
                int soShare = 0;
                String taiKhoan = sharedPreferences.getString("username", "");
                String tacGia = layMaTacGia(taiKhoan);

                String strSQL = "INSERT INTO CAUCHUYEN VALUES('" + maCC + "', '" + tenCC + "', '" + noiDung + "', '" + ngayDang
                        + "', " + soLike + ", " + soComment + ", " + soShare + ", '" + tacGia + "')";

                dbManager.QueryData(strSQL);

                startActivity(new Intent(WriteStory.this, DashboardActivity.class));
            }
        });

        dialog.show();
    }

    private void Startup(){
        dbManager = new DBManager(this, "QLStories.sqlite", null, 1);

        dbManager.QueryData("CREATE TABLE IF NOT EXISTS TACGIA(MATACGIA VARCHAR(50) PRIMARY KEY, TENTACGIA VARCHAR(50), " +
                "HINHANH VARCHAR(50), SDT NVARCHAR(1000), EMAIL VARCHAR(50), TAIKHOAN VARCHAR(50))");

        dbManager.QueryData("CREATE TABLE IF NOT EXISTS CAUCHUYEN(MACAUCHUYEN VARCHAR(50) PRIMARY KEY, " +
                "TENCAUCHUYEN VARCHAR(50), NOIDUNG NVARCHAR(1000), NGAYDANG DATETIME, SOLIKE INT, SOCOMMENT INT, SOSHARE INT, TACGIA VARCHAR(50))");
    }

    private int laySoLuongCauChuyen(){
        String strSQL = "SELECT * FROM CAUCHUYEN";
        Cursor cursor = dbManager.getData(strSQL);
        return cursor.getCount();
    }

    public String layMaTacGia(String taiKhoan){
        String strSQL = "SELECT MaTacGia FROM TacGia WHERE TaiKhoan = '" + taiKhoan + "'";
        Cursor cursor = dbManager.getData(strSQL);
        String maTG = "";
        while (cursor.moveToNext()) {
            maTG = cursor.getString(0).toString();
        }
        return maTG;
    }
}