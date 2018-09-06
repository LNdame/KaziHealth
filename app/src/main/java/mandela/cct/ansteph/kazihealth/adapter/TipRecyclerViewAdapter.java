package mandela.cct.ansteph.kazihealth.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.model.TipItem;
import mandela.cct.ansteph.kazihealth.view.tip.TipDetail;

/**
 * Created by loicstephan on 2018/05/18.
 */

public class TipRecyclerViewAdapter extends RecyclerView.Adapter<TipRecyclerViewAdapter.ViewHolder> {


    public ArrayList<TipItem> mTipItems;
    Context mContext;

    public TipRecyclerViewAdapter(ArrayList<TipItem> mTipItems, Context mContext) {
        this.mTipItems = mTipItems;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tip_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
            holder.txtPosition.setText(mTipItems.get(position).getPosition());
        holder.txtName.setText(mTipItems.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,TipDetail.class);
                i.putExtra("Tip",mTipItems.get(position));

                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTipItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public final View mView;

        public final TextView txtPosition;
        public final TextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;


            txtPosition = (TextView) itemView.findViewById(R.id.txtTipPosition);
            txtName = (TextView) itemView.findViewById(R.id.txtTipName);
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            // recyclerViewClickListener.onRecyclerViewListItemClicked(v, position);


        }
    }
}
