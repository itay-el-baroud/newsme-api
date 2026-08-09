package com.medianote.app;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
public class UsersActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        SharedPreferences p = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String myName = p.getString("username", "انا");
        
        RecyclerView rv = findViewById(R.id.rv_users);
        rv.setLayoutManager(new LinearLayoutManager(this));
        List<UserModel> list = new ArrayList<>();
        // بيانات تجريبية لحد ما تربطها بالسيرفر
        list.add(new UserModel("Ahmed", "client"));
        list.add(new UserModel("Mohamed Driver", "driver"));
        list.add(new UserModel("Sara", "client"));
        list.add(new UserModel(myName + " (انت)", p.getString("user_role","client")));
        
        rv.setAdapter(new UsersAdapter(list));
    }
}
