package com.elegion.myfirstapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elegion.myfirstapplication.model.User;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class AuthFragment extends Fragment {
    private AutoCompleteTextView mEmail;
    private EditText mPassword;
    private Button mEnter;
    private Button mRegister;

    private Disposable mCompositeDisposable;

    public static AuthFragment newInstance() {
        Bundle args = new Bundle();

        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isEmailValid() && isPasswordValid()) {

                String email = mEmail.getText().toString();
                String pass = mPassword.getText().toString();

                mCompositeDisposable = new CompositeDisposable(
                        ApiUtils.getApiService(email, pass).authUser()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            Intent startProfileIntent = new Intent(getActivity(), ProfileActivity.class);
                            startProfileIntent.putExtra(ProfileActivity.USER_KEY, user);
                            startActivity(startProfileIntent);
                            (Objects.requireNonNull(getActivity())).finish();
                        }, throwable -> {
                            HttpException httpException = (HttpException) throwable;

                            if(httpException.code() == 401) {
                                showMessage(R.string.response_code_401);
                            }
                            else if(httpException.code() == 500) {
                                showMessage(R.string.response_code_500);
                            }
                        }, () -> {
                            mCompositeDisposable.dispose();
                        }));
            } else {
                showMessage(R.string.input_error);
            }
        }
    };

    private View.OnClickListener mOnRegisterClickListener = view -> getFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainer, RegistrationFragment.newInstance())
            .addToBackStack(RegistrationFragment.class.getName())
            .commit();

    private View.OnFocusChangeListener mOnEmailFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                mEmail.showDropDown();
            }
        }
    };

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(mEmail.getText())
                && Patterns.EMAIL_ADDRESS.matcher(mEmail.getText()).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(mPassword.getText());
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_auth, container, false);

        mEmail = v.findViewById(R.id.etEmail);
        mPassword = v.findViewById(R.id.etPassword);
        mEnter = v.findViewById(R.id.buttonEnter);
        mRegister = v.findViewById(R.id.buttonRegister);

        mEnter.setOnClickListener(mOnEnterClickListener);
        mRegister.setOnClickListener(mOnRegisterClickListener);
        mEmail.setOnFocusChangeListener(mOnEmailFocusChangeListener);

        return v;
    }
}
