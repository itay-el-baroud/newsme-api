package com.medianote.app;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.Holder> {
    List<UserModel> list;
    public UsersAdapter(List<UserModel> list){ this.list=list; }
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }
    @Override public void onBindViewHolder(@NonNull Holder h, int position) {
        h.name.setText(list.get(position).name);
        h.role.setText(list.get(position).role);
    }
    @Override public int getItemCount(){ return list.size(); }
    static class Holder extends RecyclerView.ViewHolder{
        TextView name, role;
        Holder(View v){ super(v); name=v.findViewById(R.id.tv_username); role=v.findViewById(R.id.tv_role); }
    }
}
