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
import com.caesarcipherer.databinding.AboutPersonFragmentBinding;

/**
 * Фрагмент "Об авторе", представляющий информацию об авторе.
 *
 * @author Авторский ?.?.
 * @version 1.0
 */
public class AboutAuthorFragment extends Fragment{
    /**
     * Привязка для фрагмента "Об авторе".
     */
    AboutPersonFragmentBinding binding;

    /**
     * Создает вид для фрагмента "Об авторе".
     *
     * @param inflater           Инфлейтор макета.
     * @param container          Контейнер для группы видов.
     * @param savedInstanceState Сохраненное состояние экземпляра.
     * @return Созданный вид.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AboutPersonFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    /**
     * Вызывается после создания вида фрагмента "Об авторе".
     *
     * @param view               Созданный вид.
     * @param savedInstanceState Сохраненное состояние экземпляра.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
}


