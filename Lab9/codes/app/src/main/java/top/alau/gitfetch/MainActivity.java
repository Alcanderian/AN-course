package top.alau.gitfetch;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.alau.gitfetch.adapter.CommonAdapter;
import top.alau.gitfetch.factory.ServiceFactory;
import top.alau.gitfetch.model.User;
import top.alau.gitfetch.service.GithubService;
import top.alau.gitfetch.tool.Permission;

public class MainActivity extends AppCompatActivity {
    private EditText etFetchUsername;
    private Button btnClear;
    private Button btnFetch;
    private RecyclerView rvUserInfo;
    private ProgressBar pb;
    private CommonAdapter caUserInfo;
    private Retrofit retrofit;
    private GithubService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Permission.verify(MainActivity.this, Manifest.permission.INTERNET, 1))
            runActivity();
    }

    private void runActivity() {
        setContentView(R.layout.activity_main);

        etFetchUsername = findViewById(R.id.et_fetch_username);
        btnClear = findViewById(R.id.btn_clear);
        btnFetch = findViewById(R.id.btn_fetch);
        pb = findViewById(R.id.pb);
        rvUserInfo = findViewById(R.id.rv_user_info);
        caUserInfo = new CommonAdapter(
                MainActivity.this,
                R.layout.user_card,
                new ArrayList<HashMap<String, Object>>()) {
            @Override
            public void convertData(ViewHolder holder, HashMap<String, Object> map) {
                ((TextView) holder.getView(R.id.txt_username)).setText((String) map.get("login"));
                ((TextView) holder.getView(R.id.txt_id)).setText((String) map.get("id"));
                ((TextView) holder.getView(R.id.txt_blog)).setText((String) map.get("blog"));
            }
        };
        retrofit = ServiceFactory.getRetrofit(ServiceFactory.URL);
        service = retrofit.create(GithubService.class);

        caUserInfo.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, ReposActivity.class);
                intent.putExtra("username", (String) caUserInfo.data.get(position).get("login"));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("删除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                caUserInfo.data.remove(position);
                                caUserInfo.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
                return true;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvUserInfo.setLayoutManager(layoutManager);
        rvUserInfo.setAdapter(caUserInfo);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFetchUsername.setText("");
            }
        });

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etFetchUsername.getText().toString().trim();

                if (username.isEmpty())
                    Toast.makeText(MainActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                else {
                    fetch(username);
                }
            }
        });
    }

    private void fetch(String username) {
        service.getUser(username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        btnFetch.setEnabled(false);
                        pb.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCompleted() {
                        btnFetch.setEnabled(true);
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                        btnFetch.setEnabled(true);
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(final User user) {
                        caUserInfo.data.add(new HashMap<String, Object>() {{
                            put("login", user.login);
                            put("id", "id: " + user.id.toString());
                            put("blog", "blog: " + user.blog);
                        }});
                        caUserInfo.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED))
            System.exit(0);
        else
            runActivity();
    }
}
