package com.elegion.myfirstapplication.album;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.elegion.myfirstapplication.ApiUtils;
import com.elegion.myfirstapplication.App;
import com.elegion.myfirstapplication.R;
import com.elegion.myfirstapplication.comments.CommentFragment;
import com.elegion.myfirstapplication.db.MusicDao;
import com.elegion.myfirstapplication.model.Album;
import com.elegion.myfirstapplication.model.AlbumSong;
import com.elegion.myfirstapplication.model.Song;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailAlbumFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String ALBUM_KEY = "ALBUM_KEY";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefresher;
    private View mErrorView;
    private Album mAlbum;

    @NonNull
    private final SongsAdapter mSongsAdapter = new SongsAdapter();

    public static DetailAlbumFragment newInstance(Album album) {
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_KEY, album);

        DetailAlbumFragment fragment = new DetailAlbumFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.comment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_comment) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, CommentFragment.newInstance(mAlbum))
                    .addToBackStack(CommentFragment.class.getSimpleName())
                    .commit();
        }
    return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler);
        mRefresher = view.findViewById(R.id.refresher);
        mRefresher.setOnRefreshListener(this);
        mErrorView = view.findViewById(R.id.errorView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAlbum = (Album) getArguments().getSerializable(ALBUM_KEY);

        getActivity().setTitle(mAlbum.getName());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mSongsAdapter);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        mRefresher.post(() -> {
            mRefresher.setRefreshing(true);
            getAlbum();
        });
    }

    private void getAlbum() {
        ApiUtils.getApiService().getAlbum(mAlbum.getId())
                .subscribeOn(Schedulers.io())
                .doOnSuccess(album -> {
                    Collections.sort(album.getSongs(), new Song.SongComparator());

                    getMusicDao().insertSongs(album.getSongs());
                    for (Song song: album.getSongs()) {
                        getMusicDao().setLinkAlbumSong(new AlbumSong(
                                album.getId() + song.getId(),
                                        album.getId(),
                                        song.getId())
                        );
                    }
                })
                .onErrorReturn(throwable -> {
                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        Album insertedAlbum = getMusicDao().getAlbumById(mAlbum.getId());
                        insertedAlbum.setSongs(getMusicDao().getSongsFromAlbum(insertedAlbum.getId()));
                        return insertedAlbum;
                    } else return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mRefresher.setRefreshing(true))
                .doFinally(() -> mRefresher.setRefreshing(false))
                .subscribe(new DisposableSingleObserver<Album>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Album album) {
                        mErrorView.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mSongsAdapter.addData(album.getSongs(), true);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        mErrorView.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                });
    }
    private MusicDao getMusicDao() {
        return ((App) Objects.requireNonNull(getActivity()).getApplication()).getDatabase().getMusicDao();
    }
}
