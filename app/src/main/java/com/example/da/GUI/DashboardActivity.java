package com.example.da.GUI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;

import com.example.da.BLL.DBManager;
import com.example.da.BLL.OnboaringAdapter;
import com.example.da.BLL.OnboaringItem;
import com.example.da.BLL.StoriesAdapter;
import com.example.da.BLL.Story;
import com.example.da.BLL.TacGia;
import com.example.da.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StoriesAdapter storiesAdapter;
    private List<Story> stories;

    private FloatingActionButton btnWrite;
    private BottomAppBar bottomAppBar;

    private DBManager dbManager;

    private void Startup(){
        dbManager = new DBManager(this, "QLStories.sqlite", null, 1);

        dbManager.QueryData("CREATE TABLE IF NOT EXISTS CAUCHUYEN(MACAUCHUYEN VARCHAR(50) PRIMARY KEY, " +
                "TENCAUCHUYEN VARCHAR(50), NOIDUNG NVARCHAR(1000), NGAYDANG DATETIME, SOLIKE INT, SOCOMMENT INT, SOSHARE INT, TACGIA VARCHAR(50))");
    }

    private void layDanhSachCacCauChuyen(String strSQL){
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

        storiesAdapter = new StoriesAdapter(stories, this);

        recyclerView.setAdapter(storiesAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        stories = new ArrayList<Story>();

        Startup();

        layDanhSachCacCauChuyen("SELECT * FROM CAUCHUYEN");

        btnWrite = findViewById(R.id.btnWrite);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, WriteStory.class));
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuFollow:
                        startActivity(new Intent(DashboardActivity.this, MyStory_Activity.class));
                        break;
                    case R.id.menuSettings:
                        Intent intent = new Intent(DashboardActivity.this, TaiKhoan_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.menuLogout:
                        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLogged", false);
                        editor.commit();
                        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }
}