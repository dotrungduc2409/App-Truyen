package com.example.da.GUI;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.da.BLL.DBManager;
import com.example.da.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton btRegister;
    private TextView tvLogin;
    private Button buttonLogin;
    private EditText txtUsername, txtPassword;
    private SharedPreferences sharedPreferences;

    private DBManager dbManager;

    private void Startup(){
        dbManager = new DBManager(this, "QLStories.sqlite", null, 1);
        dbManager.QueryData("CREATE TABLE IF NOT EXISTS TAIKHOAN(TAIKHOAN VARCHAR(50) PRIMARY KEY, MATKHAU VARCHAR(50))");
    }

    private boolean kiemTraDangNhap(){
        String strSQL = "SELECT * FROM TAIKHOAN WHERE TaiKhoan = '" + txtUsername.getText().toString().trim() + "' AND MatKhau = '" + txtPassword.getText().toString().trim() + "'";
        Cursor cursor = dbManager.getData(strSQL);
        if(cursor.getCount() == 0)
            return false;
        return true;
    }

    private int soLan;

    private boolean first = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btRegister  = findViewById(R.id.btRegister);
        tvLogin     = findViewById(R.id.tvLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        soLan = 0;

        Startup();

        //Lấy dữ liệu vừa mới đăng ký
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            txtUsername.setText(bundle.getString("username"));
            txtPassword.setText(bundle.getString("password"));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Kiểm tra trạng thái trước đây có lưu đăng nhập hay không
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                if(preferences.getBoolean("isLogged", false)) {
                    txtUsername.setText(preferences.getString("username", ""));
                    txtPassword.setText(preferences.getString("password", ""));
                    if(kiemTraDangNhap())
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    else {
                        Toast.makeText(LoginActivity.this, "Không thể đăng nhập, có thể mật khẩu đã được thay đổi trước đó!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //Kiểm tra trạng thái có vừa đăng ký hay không
                SharedPreferences sharedPreferences1 = getSharedPreferences("dk", MODE_PRIVATE);
                first = sharedPreferences1.getBoolean("first", false);
            }
        }, 100);

        btRegister.setOnClickListener(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kiemTraDangNhap()) {
                    //Lưu lại trạng thái đăng nhập
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", txtUsername.getText().toString().trim());
                    editor.putString("password", txtPassword.getText().toString().trim());
                    editor.putBoolean("isLogged", true);
                    editor.commit();
                    if(!first) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);

                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập lần đầu thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, TaiKhoan_Activity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("taikhoan", txtUsername.getText().toString());
                        bundle1.putString("matkhau", txtPassword.getText().toString());
                        intent.putExtras(bundle1);

                        startActivity(intent);
                    }
                }
                else {
                    if(soLan < 5) {
                        soLan++;
                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        final Dialog dialog = new Dialog(LoginActivity.this);
                        dialog.setContentView(R.layout.dialog_login_false);

                        final Button btnYes = dialog.findViewById(R.id.btnYes_TK);
                        final Button btnNo = dialog.findViewById(R.id.btnNo_TK);

                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                btRegister.callOnClick();
                            }
                        });

                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v==btRegister){
            Intent intent   = new Intent(LoginActivity.this, RegisterActivity.class);
            Pair[] pairs    = new Pair[1];
            pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
            startActivity(intent,activityOptions.toBundle());
        }
    }
}