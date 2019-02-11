package hellomeet.vdartvidhya.com.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hellomeet.vdartvidhya.com.Models.MeetUsers;
import hellomeet.vdartvidhya.com.R;

/**
 * Created by Vidhya on 2/11/2019.
 * HelloMeet
 */
public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private List<MeetUsers> mUserData;
    private Context mContext;

    public UserAdapter(Context mContext, List<MeetUsers> mUserData) {
        this.mUserData = mUserData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.screen_userrowitem,parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.tvIcon.setText(String.valueOf(position+1));
        holder.tvusername.setText(mUserData.get(position).getUsername());
        holder.tvemail.setText(mUserData.get(position).getUseremail());
        Random mRandom = new Random();
        final int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
        ((GradientDrawable) holder.tvIcon.getBackground()).setColor(color);
    }


    @Override
    public int getItemCount() {
        return mUserData.size();
    }
}

class UserViewHolder extends RecyclerView.ViewHolder {

    TextView tvIcon, tvusername, tvemail;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        tvIcon =   itemView.findViewById(R.id.tvIcon);
        tvusername =  itemView.findViewById(R.id.tvusername);
        tvemail =  itemView.findViewById(R.id.tvemail);

    }

}//end

