package com.example.da.GUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.da.BLL.DBManager;
import com.example.da.BLL.MyStoriesAdapter;
import com.example.da.BLL.StoriesAdapter;
import com.example.da.BLL.Story;
import com.example.da.BLL.TacGia;
import com.example.da.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyStory_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyStoriesAdapter storiesAdapter;
    private Story story;
    private List<Story> stories;
    private TacGia tacGia;
    private DBManager dbManager;

    private void Startup(){
        dbManager = new DBManager(this, "QLStories.sqlite", null, 1);

        dbManager.QueryData("CREATE TABLE IF NOT EXISTS CAUCHUYEN(MACAUCHUYEN VARCHAR(50) PRIMARY KEY, " +
                "TENCAUCHUYEN VARCHAR(50), NOIDUNG NVARCHAR(1000), NGAYDANG DATETIME, SOLIKE INT, SOCOMMENT INT, SOSHARE INT, TACGIA VARCHAR(50))");
    }

    private void layDanhSachCacCauChuyen_TheoTacGia(String tacgia){
        String strSQL = "SELECT * FROM CAUCHUYEN WHERE TACGIA = '" + tacgia + "'";
        Cursor cursor = dbManager.getData(strSQL);
        while (cursor.moveToNext()) {
            String maCauChuyen = cursor.getString(0).toString();
            String tenCauChuyen = cursor.getString(1).toString();
            String noiDung = cursor.getString(2).toString();
            String ngayDang = cursor.getString(3).toString();
            int soLike = cursor.getInt(4);
            int soComment = cursor.getInt(5);
            int soShare = cursor.getInt(6);
            String tacGia = cursor.getString(7).toString();

            Story story = new Story(maCauChuyen, tenCauChuyen, noiDung, ngayDang, soLike, soComment, soShare, tacGia);

            stories.add(story);
        }

        storiesAdapter = new MyStoriesAdapter(stories, this, story, getMenuInflater());
        story = storiesAdapter.getStory();
        recyclerView.setAdapter(storiesAdapter);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story_);

        recyclerView = findViewById(R.id.recyclerView_My);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        stories = new ArrayList<Story>();
        story = new Story();
        tacGia = new TacGia();

        Startup();

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String taiKhoan = sharedPreferences.getString("username", "");
        tacGia = layThongTinTGCuaTaiKhoan(taiKhoan);

        layDanhSachCacCauChuyen_TheoTacGia(tacGia.getMaTG());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSua: {
                Intent intent = new Intent(MyStory_Activity.this, WriteStory.class);
                Bundle bundle = new Bundle();
                bundle.putString("maCC", story.getMaCauChuyen());
                bundle.putString("tenCC", story.getTenCauChuyen());
                bundle.putString("noiDung", story.getNoiDung());
                intent.putExtras(bundle);
                startActivity(intent);
                MyStory_Activity.this.finish();
                break;
            }
            case R.id.mnuXoa: {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                String message = "Bạn có chắc muốn xoá \"" + story.getMaCauChuyen() + " - " + story.getTenCauChuyen() + "\" không?";
                dialog.setMessage(message);
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strSQL = String.format("DELETE FROM CAUCHUYEN WHERE MACAUCHUYEN = '%s'", story.getMaCauChuyen());
                        dbManager.QueryData(strSQL);
                        Toast.makeText(MyStory_Activity.this, "Xoá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        layDanhSachCacCauChuyen_TheoTacGia(tacGia.getMaTG());
                    }
                });
                dialog.show();
                break;
            }
        }
        return super.onContextItemSelected(item);
    }
}