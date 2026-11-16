package com.example.diyproject.ui.authorization;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.diyproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
public class CodeFragment extends Fragment {

    public static final String ARG_LOGIN = "login";
    public static final String ARG_IS_EMAIL = "is_email";
    private static final int CODE_LENGTH = 6;

    private TextInputLayout tilCode;
    private TextInputEditText etCode;
    private String login;
    private boolean isEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = requireArguments();
        login = args.getString(ARG_LOGIN, "");
        isEmail = args.getBoolean(ARG_IS_EMAIL);

        tilCode = view.findViewById(R.id.til_code);
        etCode = view.findViewById(R.id.et_code);
        TextView tvMessage = view.findViewById(R.id.tv_code_message);

        String target = isEmail ? "вашу почту" : "ваш номер телефона";
        tvMessage.setText("Мы прислали " + CODE_LENGTH + "-значный код на " + target
                + ". Введите его в поле ниже для входа в аккаунт");
        tilCode.setError("Введите " + CODE_LENGTH + "-значный код");

        view.findViewById(R.id.btn_back).setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());

        view.findViewById(R.id.btn_confirm).setOnClickListener(v -> onConfirmClick());

        etCode.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                tilCode.setError(null);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void onConfirmClick() {
        String code = etCode.getText() == null ? "" : etCode.getText().toString().trim();

        if (code.length() != CODE_LENGTH) {
            tilCode.setError("Введите 6-значный код");
            return;
        }

        verifyCode(login, code);
    }

    private void verifyCode(String login, String code) {
        // TODO: когда появится бэкенд, здесь будет запрос на проверку кода
        // (например, через ViewModel/репозиторий). Пока считаем код верным.
        // NavHostFragment.findNavController(this).navigate(R.id.action_code_to_home);
    }
}