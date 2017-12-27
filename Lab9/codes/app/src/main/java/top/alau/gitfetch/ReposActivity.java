package top.alau.gitfetch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.alau.gitfetch.adapter.CommonAdapter;
import top.alau.gitfetch.factory.ServiceFactory;
import top.alau.gitfetch.model.Repo;
import top.alau.gitfetch.service.GithubService;

public class ReposActivity extends AppCompatActivity {
    private RecyclerView rvRepos;
    private ProgressBar pb;
    private CommonAdapter caRepos;
    private ArrayList<HashMap<String, Object>> repos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);

        rvRepos = findViewById(R.id.rv_repos);
        pb = findViewById(R.id.pb);
        repos = new ArrayList<>();
        caRepos = new CommonAdapter(ReposActivity.this, R.layout.repo_info, repos) {
            @Override
            public void convertData(ViewHolder vh, HashMap<String, Object> map) {
                ((TextView) vh.getView(R.id.txt_repo_name))
                        .setText((String) map.get("name"));
                ((TextView) vh.getView(R.id.txt_lang))
                        .setText((String) map.get("language"));
                ((TextView) vh.getView(R.id.txt_description))
                        .setText((String) map.get("description"));
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(ReposActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRepos.setLayoutManager(layoutManager);
        rvRepos.setAdapter(caRepos);

        Observable<ArrayList<Repo>> observable = ServiceFactory
                .getRetrofit(ServiceFactory.URL)
                .create(GithubService.class)
                .getRepos(getIntent().getStringExtra("username"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Subscriber<ArrayList<Repo>>() {
            @Override
            public void onStart() {
                super.onStart();
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCompleted() {
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(ReposActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onNext(final ArrayList<Repo> list) {
                for (final Repo item : list) {
                    repos.add(new HashMap<String, Object>() {{
                        put("name", item.name);
                        put("language", item.language == null ? "unknown" : item.language);
                        put("description", item.description == null ? "null" : item.description);
                    }});
                }
                caRepos.notifyDataSetChanged();
            }
        });
    }
}
