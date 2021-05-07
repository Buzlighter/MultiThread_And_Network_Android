package com.elegion.myfirstapplication.comments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.elegion.myfirstapplication.ApiUtils;
import com.elegion.myfirstapplication.App;
import com.elegion.myfirstapplication.R;
import com.elegion.myfirstapplication.album.DetailAlbumFragment;
import com.elegion.myfirstapplication.db.MusicDao;
import com.elegion.myfirstapplication.model.Album;
import com.elegion.myfirstapplication.model.Comment;

import java.util.List;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class CommentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private Album mAlbum;
    private SwipeRefreshLayout mRefresher;
    private View mErrorView;
    private View mNoCommentsView;
    private ImageButton mBtnSendComment;
    private EditText mEtComment;
    public boolean firstStart = true;

    private CommentsAdapter mCommentsAdapter = new CommentsAdapter();


    public static CommentFragment newInstance(Album album) {
        Bundle args = new Bundle();
        args.putSerializable(DetailAlbumFragment.ALBUM_KEY, album);

        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_comments, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler);
        mRefresher = view.findViewById(R.id.refresher);
        mRefresher.setOnRefreshListener(this);
        mErrorView = view.findViewById(R.id.errorView);
        mNoCommentsView = view.findViewById(R.id.no_comments_view);
        mBtnSendComment = view.findViewById(R.id.btnSend_comment);
        mEtComment = view.findViewById(R.id.etComment);

        mBtnSendComment.setOnClickListener(sendOnclickListener);
        mEtComment.setOnKeyListener(sendOnKeyListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAlbum = (Album) getArguments().getSerializable(DetailAlbumFragment.ALBUM_KEY);

        getActivity().setTitle(mAlbum.getName());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mCommentsAdapter);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        mRefresher.post(() -> {
            mRefresher.setRefreshing(true);
            getComments();
        });
    }

    @SuppressLint("CheckResult")
    private void getComments() {

        int commentListSize = mCommentsAdapter.getItemCount();
        ApiUtils.getApiService()
                .getCommentsByAlbumId(mAlbum.getId())
                .subscribeOn(Schedulers.io())
                .doOnSuccess(comments -> {
                    getMusicDao().insertComments(comments);})
                .onErrorReturn(throwable -> {
                    if (networkNotAvailable()) {
                        return getMusicDao().getCommentsByAlbumId(mAlbum.getId());
                    } else { return null;}
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(comments -> {
                    mCommentsAdapter.addData(comments, true);
                    mNoCommentsView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    if (networkNotAvailable()) {
                        showMessage("Нет подключения");
                    }
                    else {
                        if (!firstStart) {
                            if (comments.size() != commentListSize) {
                                showMessage("Комментарии обновлены");
                            } else {
                                showMessage("Комментариев новых нет");
                            }
                        }
                        if (comments.size() == 0) {
                            mRecyclerView.setVisibility(View.INVISIBLE);
                            mErrorView.setVisibility(View.GONE);
                            mNoCommentsView.setVisibility(View.VISIBLE);
                        }
                    }
                    firstStart = false;
                }, throwable -> {
                    if (throwable instanceof HttpException) {
                        mRecyclerView.setVisibility(View.INVISIBLE);
                        mErrorView.setVisibility(View.VISIBLE);
                        mNoCommentsView.setVisibility(View.GONE);
                        HttpException httpException = (HttpException) throwable;
                        switch (httpException.code()) {
                            case 401:
                                showMessage(R.string.response_code_401);
                                break;
                            case 500:
                                showMessage(R.string.response_code_500);
                                break;
                            default:
                                showMessage("Ошибка сервера");
                        }
                    }
                });
        mRefresher.setRefreshing(false);
    }

    private boolean networkNotAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo == null || !activeNetworkInfo.isConnected();
    }

    private MusicDao getMusicDao() {
        return ((App) Objects.requireNonNull(getActivity()).getApplication()).getDatabase().getMusicDao();
    }


    public View.OnClickListener sendOnclickListener = v -> {
        clickSendHandle();
    };

    public View.OnKeyListener sendOnKeyListener = (v, keyCode, event) -> {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)) {
            clickSendHandle();
            return true;
        }
        return false;
    };

    private void clickSendHandle() {
        if(mEtComment.getText().toString().isEmpty()) {
            showMessage("Текст для отправки отсутсвует");
        }

        Comment comment = new Comment();
        comment.setText(mEtComment.getText().toString());
        comment.setAlbumId(mAlbum.getId());
        postComment(comment);
        mEtComment.getText().clear();
    }

    @SuppressLint("CheckResult")
    private void postComment(Comment comment) {
        ApiUtils.getApiService()
                .addComment(comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    mRefresher.post(() -> {
                        mRefresher.setRefreshing(true);
                        getComments();
                        mRecyclerView.scrollToPosition(mCommentsAdapter.getItemCount() - 1);
                    });
                }, throwable -> {
                    if (throwable instanceof HttpException) {
                        mRecyclerView.setVisibility(View.INVISIBLE);
                        mErrorView.setVisibility(View.VISIBLE);
                        mNoCommentsView.setVisibility(View.GONE);
                        HttpException httpException = (HttpException)throwable;
                        switch (httpException.code()) {
                            case 400: showMessage(R.string.response_code_400); break;
                            case 401: showMessage(R.string.response_code_401); break;
                            case 500: showMessage(R.string.response_code_500); break;
                            default: showMessage("Ошибка сервера");
                        }
                    }
                });
    }


    public void showMessage(int msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


}
