package com.caesarcipherer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.caesarcipherer.R;
import com.caesarcipherer.databinding.AboutAppFragmentBinding;

/**
 * Фрагмент "О приложении", отображающий информацию о приложении.
 *
 * @author Жегздринь А.Р.
 * @version 1.0
 */
public class AboutAppFragment extends Fragment{
    /**
     * Связывание для фрагмента "О приложении".
     */
    AboutAppFragmentBinding binding;

    /**
     * Вызывается при создании представления фрагмента.
     *
     * @param inflater           Инфлятор макета.
     * @param container          Контейнер для группы представлений.
     * @param savedInstanceState Сохраненное состояние экземпляра.
     * @return Корневое представление.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AboutAppFragmentBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    /**
     * Вызывается после создания представления фрагмента.
     *
     * @param view               Представление.
     * @param savedInstanceState Сохраненное состояние экземпляра.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
