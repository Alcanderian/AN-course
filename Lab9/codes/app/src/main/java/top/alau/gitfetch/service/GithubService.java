package top.alau.gitfetch.service;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import top.alau.gitfetch.model.Repo;
import top.alau.gitfetch.model.User;

/**
 * Created by alcanderian on 2017/12/21.
 */

public interface GithubService {
    @GET("/users/{user}/repos")
    Observable<ArrayList<Repo>> getRepos(@Path("user") String user);

    @GET("/users/{user}")
    Observable<User> getUser(@Path("user") String user);
}
