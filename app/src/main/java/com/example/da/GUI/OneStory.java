package com.example.da.GUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.da.BLL.Comment;
import com.example.da.BLL.CommentAdapter;
import com.example.da.BLL.DBManager;
import com.example.da.BLL.Story;
import com.example.da.BLL.TacGia;
import com.example.da.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OneStory extends AppCompatActivity {

    private TextView lblTitle, lblBody, lblTenTacGia, lblSoLike, lblSocmt, lblSoShare, lblNgayDang;
    private ImageButton btnLike, btnComment, btnShare, btnSend;
    private ImageView imageViewAccount;

    private EditText txtComment;
    private RecyclerView recyclerView;
    private int like = 0;

    private CommentAdapter commentAdapter;
    private List<Comment> comments;

    private DBManager dbManager;

    private String maCC = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_story);

        lblTitle = findViewById(R.id.lbl_title_story);
        lblBody = findViewById(R.id.lbl_body_story);
        lblTenTacGia = findViewById(R.id.lblNameAccount);
        lblSoLike = findViewById(R.id.textLike_O);
        lblSocmt = findViewById(R.id.textComment_O);
        lblSoShare = findViewById(R.id.textShare_O);
        lblNgayDang = findViewById(R.id.lblngayDang_One);
        imageViewAccount = findViewById(R.id.img_account);

        btnLike = findViewById(R.id.buttonLike_O);
        btnComment = findViewById(R.id.buttonComment_O);
        btnShare = findViewById(R.id.buttonShare_O);
        btnSend = findViewById(R.id.btnSend);

        txtComment = findViewById(R.id.txtComment);

        recyclerView = findViewById(R.id.recyclerView_cmt);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        comments = new ArrayList<>();

        Startup();

        //Lấy dữ liệu truyền
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            maCC = bundle.getString("maCC");
            Story story = layCauChuyen(maCC);

            lblTitle.setText(story.getTenCauChuyen());
            lblBody.setText(story.getNoiDung());
            lblNgayDang.setText(story.getNgayDang());
            String maTG = story.getMaTG();
            TacGia tacGia = layThongTinTGCuaCauChuyen(maTG);
            if(tacGia.getTenTG().length() == 0)
                lblTenTacGia.setText(maTG);
            else
                lblTenTacGia.setText(tacGia.getTenTG());
            if(tacGia.getHinhAnh().trim().equals("male.png"))
                imageViewAccount.setImageResource(R.drawable.male);
            else
                imageViewAccount.setImageResource(R.drawable.female);
            lblSoLike.setText(String.valueOf(story.getSoLike()));
            lblSocmt.setText(String.valueOf(story.getSoComment()));
            lblSoShare.setText(String.valueOf(story.getSoShare()));
        }

        layDanhSachBinhLuan_MaCauChuyen(maCC);

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(like == 0) {
                    lblSoLike.setText(String.valueOf(Integer.valueOf(lblSoLike.getText().toString().trim()) + 1));
                    update_LikeCauChuyen(maCC, true);
                    like = 1;
                }
                else {
                    lblSoLike.setText(String.valueOf(Integer.valueOf(lblSoLike.getText().toString().trim()) - 1));
                    update_LikeCauChuyen(maCC, false);
                    like = 0;
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ml = "MBL" + maCC + String.valueOf(laySoLuongBinhLuan(maCC) + 1);

                String maCauChuyen = maCC;

                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                String taiKhoan = sharedPreferences.getString("username", "");
                TacGia tacGia = layThongTinTGCuaTaiKhoan(taiKhoan);
                String tacgia = tacGia.getMaTG();

                String noiDung = txtComment.getText().toString();

                if(noiDung.trim().length() != 0)
                    themComment(ml, maCauChuyen, tacgia, noiDung);

                lblSocmt.setText(String.valueOf(Integer.valueOf(lblSocmt.getText().toString().trim()) + 1));
                update_CommentCauChuyen(maCC);

                Intent intent = new Intent(OneStory.this, OneStory.class);
                Bundle bundle = new Bundle();
                bundle.putString("maCC", maCC);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OneStory.this, DashboardActivity.class));
    }

    private void Startup(){
        dbManager = new DBManager(this, "QLStories.sqlite", null, 1);

        dbManager.QueryData("CREATE TABLE IF NOT EXISTS TACGIA(MATACGIA VARCHAR(50) PRIMARY KEY, TENTACGIA VARCHAR(50), " +
                "HINHANH VARCHAR(50), SDT NVARCHAR(1000), EMAIL VARCHAR(50), TAIKHOAN VARCHAR(50))");

        dbManager.QueryData("CREATE TABLE IF NOT EXISTS CAUCHUYEN(MACAUCHUYEN VARCHAR(50) PRIMARY KEY, " +
                "TENCAUCHUYEN VARCHAR(50), NOIDUNG NVARCHAR(1000), NGAYDANG DATETIME, SOLIKE INT, SOCOMMENT INT, SOSHARE INT, TACGIA VARCHAR(50))");

        dbManager.QueryData("CREATE TABLE IF NOT EXISTS BINHLUAN(MABINHLUAN VARCHAR(50) PRIMARY KEY, MACAUCHUYEN VARCHAR(50), TACGIA VARCHAR(50), NOIDUNG NVARCHAR(1000))");
    }

    @NotNull
    private TacGia layThongTinTGCuaCauChuyen(String maTG){
        String strSQL = "SELECT * FROM TACGIA WHERE MATACGIA = '" + maTG + "'";
        TacGia tacGia = new TacGia();
        Cursor cursor = dbManager.getData(strSQL);
        while (cursor.moveToNext()) {
            tacGia.setMaTG(maTG);
            tacGia.setTenTG(cursor.getString(1));
            tacGia.setHinhAnh(cursor.getString(2));
            tacGia.setSDT(cursor.getString(3));
            tacGia.setEmail(cursor.getString(4));
            tacGia.setTaiKhoan(cursor.getString(5));
        }
        return tacGia;
    }

    @NotNull
    private TacGia layThongTinTGCuaTaiKhoan(String taiKhoan){
        String strSQL = "SELECT * FROM TACGIA WHERE TAIKHOAN = '" + taiKhoan + "'";
        TacGia tacGia = new TacGia();
        Cursor cursor = dbManager.getData(strSQL);
        while (cursor.moveToNext()) {
            tacGia.setMaTG(cursor.getString(0));
            tacGia.setTenTG(cursor.getString(1));
            tacGia.setHinhAnh(cursor.getString(2));
            tacGia.setSDT(cursor.getString(3));
            tacGia.setEmail(cursor.getString(4));
            tacGia.setTaiKhoan(taiKhoan);
        }
        return tacGia;
    }

    @NotNull
    private Story layCauChuyen(String maCC){
        String strSQL = "SELECT * FROM CAUCHUYEN WHERE MACAUCHUYEN = '" + maCC + "'";
        Cursor cursor = dbManager.getData(strSQL);
        Story story = new Story();
        while (cursor.moveToNext()){
            story.setMaCauChuyen(maCC);
            story.setTenCauChuyen(cursor.getString(1).toString());
            story.setNoiDung(cursor.getString(2).toString());
            story.setNgayDang(cursor.getString(3).toString());
            story.setSoLike(cursor.getInt(4));
            story.setSoComment(cursor.getInt(5));
            story.setSoShare(cursor.getInt(6));
            story.setMaTG(cursor.getString(7));
        }
        return story;
    }

    private void layDanhSachBinhLuan_MaCauChuyen(String maCC){
        String strSQL = "SELECT * FROM BINHLUAN WHERE MACAUCHUYEN = '" + maCC + "'";
        Cursor cursor = dbManager.getData(strSQL);
        while (cursor.moveToNext()) {
            String maBL = cursor.getString(0).toString();
            String maTG = cursor.getString(2).toString();
            String noiDung = cursor.getString(3).toString();

            Comment comment = new Comment(maBL, maCC, maTG, noiDung);

            comments.add(comment);
        }
        commentAdapter = new CommentAdapter(comments, this);
        commentAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(commentAdapter);
    }

    private int laySoLuongBinhLuan(String maCC){
        String strSQL = "SELECT * FROM BINHLUAN WHERE MACAUCHUYEN = '" + maCC + "'";
        Cursor cursor = dbManager.getData(strSQL);
        return cursor.getCount();
    }

    private void themComment(String maBL, String maCC, String tacGia, String noiDung){
        String strSQL = "INSERT INTO BINHLUAN VALUES ('" + maBL + "', '" + maCC + "', '" + tacGia + "', '" + noiDung + "')";
        dbManager.QueryData(strSQL);
    }

    private void update_LikeCauChuyen(String maCC, boolean like){
        String strSQL = "";
        if(like)
            strSQL = "UPDATE CAUCHUYEN SET SOLIKE = SOLIKE + 1 WHERE MACAUCHUYEN = '" + maCC + "'";
        else
            strSQL = "UPDATE CAUCHUYEN SET SOLIKE = SOLIKE - 1 WHERE MACAUCHUYEN = '" + maCC + "'";
        dbManager.QueryData(strSQL);
    }

    private void update_CommentCauChuyen(String maCC){
        String strSQL = "UPDATE CAUCHUYEN SET SOCOMMENT = SOCOMMENT + 1 WHERE MACAUCHUYEN = '" + maCC + "'";

        dbManager.QueryData(strSQL);
    }

}