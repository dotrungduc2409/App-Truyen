package com.example.da.GUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.da.BLL.DBManager;
import com.example.da.R;

public class RegisterActivity extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;

    private Button btnRegister;
    private EditText txtUsername, txtEmail, txtPassword, txtRetypePassword;

    private SharedPreferences sharedPreferences, sharedPreferences1;

    private DBManager dbManager;

    private void Startup(){
        dbManager = new DBManager(this, "QLStories.sqlite", null, 1);
        dbManager.QueryData("CREATE TABLE IF NOT EXISTS TAIKHOAN(TAIKHOAN VARCHAR(50) PRIMARY KEY, MATKHAU VARCHAR(50))");

        dbManager.QueryData("CREATE TABLE IF NOT EXISTS TACGIA(MATACGIA VARCHAR(50) PRIMARY KEY, TENTACGIA VARCHAR(50), " +
                "HINHANH VARCHAR(50), SDT NVARCHAR(1000), EMAIL VARCHAR(50), TAIKHOAN VARCHAR(50))");
    }

    private boolean kiemTraTaiKhoanCoTonTai(){
        String strSQL = "SELECT * FROM TAIKHOAN WHERE TaiKhoan = '" + txtUsername.getText().toString().trim() + "'";
        Cursor cursor = dbManager.getData(strSQL);
        if(cursor.getCount() == 0)
            return false;
        return true;
    }

    private int laySoLuongTacGia(){
        String strSQL = "SELECT * FROM TACGIA";
        Cursor cursor = dbManager.getData(strSQL);
        return cursor.getCount();
    }

    public void themTacGia(){
        String strSQL = "INSERT INTO TACGIA VALUES('TG" + String.valueOf(laySoLuongTacGia() + 1).trim() + "', NULL, NULL, NULL, '" + txtEmail.getText().toString() + "', '" + txtUsername.getText().toString() + "')";
        dbManager.QueryData(strSQL);
    }

    private void themTaiKhoan(){
        String strSQL = "INSERT INTO TaiKhoan VALUES('" + txtUsername.getText().toString().trim() + "', '"
                + txtPassword.getText().toString().trim() + "')";

        dbManager.QueryData(strSQL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtEmail = findViewById(R.id.txtEmail_Register);
        txtUsername = findViewById(R.id.txtUsername_Register);
        txtPassword = findViewById(R.id.txtPassword_Register);
        txtRetypePassword = findViewById(R.id.txtRetypePassword_Register);
        btnRegister = findViewById(R.id.btnRegister);
        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);

        //Sửa trạng thái đăng nhập thành không lưu
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogged", false);
        editor.commit();

        Startup();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtEmail.getText().length() == 0) {
                    Toast.makeText(RegisterActivity.this, "Email không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtUsername.getText().length() == 0) {
                    Toast.makeText(RegisterActivity.this, "Tài khoản không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtPassword.getText().length() == 0){
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!txtPassword.getText().toString().equals(txtRetypePassword.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Mật khẩu và nhập lại mật khẩu không giống nhau", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(kiemTraTaiKhoanCoTonTai()){
                    Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại, hãy thử đăng ký với tài khoản khác", Toast.LENGTH_SHORT).show();
                } else {

                    themTaiKhoan();
                    themTacGia();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", String.valueOf(txtUsername.getText()));
                    bundle.putString("password", String.valueOf(txtPassword.getText()));
                    intent.putExtras(bundle);

                    //Lưu trạng thái vừa mới đăng ký
                    sharedPreferences1 = getSharedPreferences("dk", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putBoolean("first", true);
                    editor.commit();

                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}