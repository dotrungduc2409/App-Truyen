package com.example.da.BLL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da.GUI.OneStory;
import com.example.da.R;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoryViewHolder>{

    private List<Story> stories;
    private Context context;

    public StoriesAdapter(List<Story> stories, Context context) {
        this.stories = stories;
        this.context = context;
    }

    private DBManager dbManager;

    private void Startup(){
        dbManager = new DBManager(context, "QLStories.sqlite", null, 1);

        dbManager.QueryData("CREATE TABLE IF NOT EXISTS CAUCHUYEN(MACAUCHUYEN VARCHAR(50) PRIMARY KEY, " +
                "TENCAUCHUYEN VARCHAR(50), NOIDUNG NVARCHAR(1000), NGAYDANG DATETIME, SOLIKE INT, SOCOMMENT INT, SOSHARE INT, TACGIA VARCHAR(50))");
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.one_story, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final StoryViewHolder holder, int position) {
        holder.setStoryData(stories.get(position));

        final Story story = stories.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, OneStory.class);
                Bundle bundle = new Bundle();
                bundle.putString("maCC", story.getMaCauChuyen());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(stories == null)
           return 0;
        return stories.size();
    }

    class StoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView coverImage;
        private TextView textTitle, textBody, textLike, textComment, textShare, lblNgayDang;
        private ImageButton buttonLike, buttonComment, buttonShare;

        StoryViewHolder(@NonNull final View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.imageCover);
            textTitle = itemView.findViewById(R.id.textTitleStory);
            textBody = itemView.findViewById(R.id.textBodyStory);
            buttonLike = itemView.findViewById(R.id.buttonLike);
            textLike = itemView.findViewById(R.id.textLike);
            buttonComment = itemView.findViewById(R.id.buttonComment);
            textComment = itemView.findViewById(R.id.textComment);
            buttonShare = itemView.findViewById(R.id.buttonShare);
            textShare = itemView.findViewById(R.id.textShare);
            lblNgayDang = itemView.findViewById(R.id.lblngayDang);
            
            Startup();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setStoryData(@NotNull Story story) {
            try {
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                Date ngayDang = dateFormat.parse(story.getNgayDang().toString());

                long millis=System.currentTimeMillis();
                Date ngayHienTai = dateFormat.parse(new java.sql.Date(millis).toString());

                c1.setTime(ngayDang);
                c2.setTime(ngayHienTai);

                long noDay = (c2.getTime().getTime() - c1.getTime().getTime()) / (24 * 3600 * 1000);

                if(noDay <= 1)
                    coverImage.setImageResource(R.drawable.newstory);
                else
                    coverImage.setImageResource(R.drawable.oldstory);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            lblNgayDang.setText(story.getNgayDang());
            textTitle.setText(story.getTenCauChuyen());
            try{
                String noiDung = story.getNoiDung().substring(0, 30);
                textBody.setText(noiDung + " continue...");
            }
            catch (Exception e){
                textBody.setText(story.getNoiDung());
            }
            textLike.setText(String.valueOf(story.getSoLike()));
            textComment.setText(String.valueOf(story.getSoComment()));
            textShare.setText(String.valueOf(story.getSoShare()));
        }
    }
}
