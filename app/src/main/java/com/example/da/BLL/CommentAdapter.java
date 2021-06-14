package com.example.da.BLL;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private List<Comment> commnets;
    private Context context;

    private DBManager dbManager;

    private void Startup(){
        dbManager = new DBManager(context, "QLStories.sqlite", null, 1);

        dbManager.QueryData("CREATE TABLE IF NOT EXISTS TACGIA(MATACGIA VARCHAR(50) PRIMARY KEY, TENTACGIA VARCHAR(50), " +
                "HINHANH VARCHAR(50), SDT NVARCHAR(1000), EMAIL VARCHAR(50), TAIKHOAN VARCHAR(50))");
    }

    public TacGia layTacGia(String maTG){
        String strSQL = "SELECT * FROM TACGIA WHERE MATACGIA = '" + maTG + "'";
        Cursor cursor = dbManager.getData(strSQL);
        TacGia tg = new TacGia();
        while (cursor.moveToNext()) {
            tg.setMaTG(cursor.getString(0).toString());
            tg.setTenTG(cursor.getString(1).toString());
            tg.setHinhAnh(cursor.getString(2).toString());
            tg.setSDT(cursor.getString(3).toString());
            tg.setEmail(cursor.getString(4).toString());
            tg.setTaiKhoan(cursor.getString(5).toString());
        }
        return tg;
    }

    public CommentAdapter(List<Comment> comments, Context context) {
        this.commnets = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentAdapter.CommentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.one_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.setCommentData(commnets.get(position));
    }

    @Override
    public int getItemCount() {
        if(commnets == null)
            return 0;
        return commnets.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        private ImageView coverImage;
        private TextView textName;
        private TextView textComment;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);

            coverImage = itemView.findViewById(R.id.img_account_cmt);
            textName = itemView.findViewById(R.id.lbl_taiKhoan_cmt);
            textComment = itemView.findViewById(R.id.lbl_binhLuan_cmt);

            Startup();
        }

        void setCommentData(@NotNull Comment commentData) {
            TacGia tacGia = layTacGia(commentData.getTenTaiKhoan());
            if(tacGia.getHinhAnh().equals("male.png"))
                coverImage.setImageResource(R.drawable.male);
            else
                coverImage.setImageResource(R.drawable.female);
            textName.setText(tacGia.getTenTG());
            textComment.setText(commentData.getNoiDungcmt());
        }
    }
}
