package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Category;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.SearchList;
import io.kodular.nsmarttechnologies6.RentEasy.activity.CityChooserActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.DetailActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.PlacesSearcherActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.SearchListActivity;


public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {
    private Activity context;
    private List<Category> list;
    int[] images;
    int Id;

    public HomeCategoryAdapter(List<Category> list, FragmentActivity context, int[] images) {
        this.context = context;
        this.list = list;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_category, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String title = list.get(position).getTitle();
        viewHolder.setData(title);
        viewHolder.ivImage.setImageResource(images[position]);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CityChooserActivity.class);
                i.putExtra("search","true");
                if (position==0) {
                   Id=1;
                }else if (position==1) {
                    Id=4;
                }else if (position==2) {
                    Id=6;
                }else if (position==3) {
                    Id=3;
                }else if (position==4) {
                    Id=2;
                }else if (position==5) {
                    Id=5;
                }
                i.putExtra("id",String.valueOf(Id));
                context.startActivity(i);
                /*
                Intent intent = new Intent(context, SearchListActivity.class);
                intent.putExtra("pincode", "null");
                intent.putExtra("is_Avialable", "null");
                intent.putExtra("category","null");
                intent.putExtra("isLiked","null");
                intent.putExtra("home-category",String.valueOf(position));
                context.startActivity(intent);

                 */
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView title;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            title = itemView.findViewById(R.id.tv_title);
        }
        public void setData(String title2) {
           title.setText(title2);
        }
    }
}
