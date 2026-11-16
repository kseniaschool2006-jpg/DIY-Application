package com.example.diyproject.ui.authorization;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.diyproject.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private TextInputLayout tilLogin;
    private TextInputEditText etLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilLogin = view.findViewById(R.id.til_login);
        etLogin = view.findViewById(R.id.et_login);

        view.findViewById(R.id.btn_login).setOnClickListener(v -> onLoginClick());

        view.findViewById(R.id.btn_google).setOnClickListener(v -> showStub());
        view.findViewById(R.id.btn_instagram).setOnClickListener(v -> showStub());
        view.findViewById(R.id.btn_tiktok).setOnClickListener(v -> showStub());
        view.findViewById(R.id.btn_pinterest).setOnClickListener(v -> showStub());

        etLogin.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                tilLogin.setError(null);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void onLoginClick() {
        String value = etLogin.getText() == null ? "" : etLogin.getText().toString().trim();

        boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(value).matches();
        boolean isPhone = Patterns.PHONE.matcher(value).matches()
                && value.replaceAll("\\D", "").length() >= 10;

        if (!isEmail && !isPhone) {
            tilLogin.setError("Введите почту или номер телефона");
            return;
        }

        // TODO: когда появится бэкенд, здесь будет запрос на отправку кода

        Bundle args = new Bundle();
        args.putString(CodeFragment.ARG_LOGIN, value);
        args.putBoolean(CodeFragment.ARG_IS_EMAIL, isEmail);

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_login_to_code, args);
    }

    private void showStub() {
        Toast.makeText(requireContext(), "Скоро будет доступно", Toast.LENGTH_SHORT).show();
    }
}