package com.elegion.myfirstapplication;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elegion.myfirstapplication.model.User;
import com.google.gson.Gson;

public class RegistrationFragment extends Fragment {
    private EditText mEmail;
    private EditText mName;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private Button mRegistration;


    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    private View.OnClickListener mOnRegistrationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isInputValid()) {
                User user = new User(
                        mEmail.getText().toString(),
                        mName.getText().toString(),
                        mPassword.getText().toString());

                ApiUtils.getApiService().registration(user).enqueue(
                        new retrofit2.Callback<User>() {
                            //используем Handler, чтобы показывать ошибки в Main потоке, т.к. наши коллбеки возвращаются в рабочем потоке
                            Handler mainHandler = new Handler(getActivity().getMainLooper());

                            @Override
                            public void onResponse(retrofit2.Call<User> call, final retrofit2.Response<User> response) {
                                mainHandler.post(() -> {
                                    if (!response.isSuccessful()) {
                                        //todo добавить полноценную обработку ошибок по кодам ответа от сервера и телу запроса
                                        switch (response.code()) {
                                            case 400:
                                                /*
                                                От сервера приходит сообщение только "Такой email уже существует",
                                                в полях имя и пароль приходит null от сервера, поэтому
                                                в Description принимаю только email, чтобы избежать NPE
                                                */
                                                highLightErrorField(mEmail);
                                                Gson gson = new Gson();
                                                User message = gson.fromJson(response.errorBody().charStream(), User.class);

                                                String description =
                                                        message.getErrors().getEmail().toString();
                                                showMessage(description);
                                                break;

                                            case 500:
                                                showMessage(R.string.response_code_500);
                                                break;
                                            default:
                                                showMessage(R.string.registration_error);
                                                break;
                                        }


                                    } else {
                                        showMessage(R.string.registration_success);
                                        getFragmentManager().popBackStack();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                                mainHandler.post(() ->
                                        showMessage(R.string.request_error));
                            }
                        });
            } else {
                if (!mPassword.getText().toString().equals(mPasswordAgain.getText().toString())) {
                    highLightErrorField(mPassword);
                    highLightErrorField(mPasswordAgain);
                }
                showMessage(R.string.input_error);
            }
        }
    };

    public void highLightErrorField(EditText editText) {
        editText.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_registration, container, false);

        mEmail = view.findViewById(R.id.etEmail);
        mName = view.findViewById(R.id.etName);
        mPassword = view.findViewById(R.id.etPassword);
        mPasswordAgain = view.findViewById(R.id.tvPasswordAgain);
        mRegistration = view.findViewById(R.id.btnRegistration);

        mRegistration.setOnClickListener(mOnRegistrationClickListener);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mEmail.setText("trinity11@mail.ru");
        mName.setText("trinity");
        mPassword.setText("trinity11");
        mPasswordAgain.setText("trinity11");
    }

    private boolean isInputValid() {
        return isEmailValid(mEmail.getText().toString())
                && !TextUtils.isEmpty(mName.getText())
                && isPasswordsValid();
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordsValid() {
        String password = mPassword.getText().toString();
        String passwordAgain = mPasswordAgain.getText().toString();

        return password.equals(passwordAgain)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(passwordAgain);
    }

    private  void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    private  void showMessage(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }
}
