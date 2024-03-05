package com.caesarcipherer.fragments;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesarcipherer.R;
import com.caesarcipherer.cipherer.CaesarCipherer;
import com.caesarcipherer.databinding.ContentMainCiphererFragmentBinding;
import com.caesarcipherer.historyComponents.HistoryAdapter;
import com.caesarcipherer.historyComponents.HistoryItem;
import com.caesarcipherer.historyComponents.OnHistoryItemButtonClickListener;
import com.caesarcipherer.sqlite.DataBaseHelper;

import java.util.List;
import java.util.Objects;

/**
 * Фрагмент "Шифровальщик", представляющий основной интерфейс для шифрования и дешифрования текста.
 *
 * @author Жездринь А.Р.
 * @version 1.1
 */
public class CiphererFragment extends Fragment{
    /**
     * Слушатель кнопки элемента истории
     */
    OnHistoryItemButtonClickListener listener;
    /**
     * Константа для текстового поля ввода вверху
     */
    public static final String ARG_EDIT_TEXT_UP = "editTextUp";
    /**
     * Константа для текстового поля ввода внизу
     */
    public static final String ARG_EDIT_TEXT_DOWN = "editTextDown";

    /**
     * Привязка к основному фрагменту шифрования контента.
     */
    private ContentMainCiphererFragmentBinding binding;

    /**
     * Текстовое поле для ввода, которое будет зашифровано.
     */
    private EditText upEditText;

    /**
     * Текстовое поле для расшифрованного текста.
     */
    private EditText downEditText;

    /**
     * Список алфавитов.
     */
    private ListView listViewAlphabets;

    /**
     * Выпадающий список для ключа шифрования.
     */
    private Spinner spinnerCipherKey;

    /**
     * Индекс выбранного алфавита.
     */
    private int selectedAlphabetIndex = -1;

    /**
     * Помощник базы данных.
     */
    private DataBaseHelper dataBaseHelper;

    /**
     * Режим шифрования.
     */
    private CipherMode cipherMode;

    /**
     * Линейный затвор.
     */
    private LinearLayout linearShutterLayout;

    /**
     * Начальная высота макета.
     */
    int layoutInitialHeight;

    /**
     * Флаг, указывающий, максимизирован ли затвор.
     */
    private boolean isMaximizedShutter = true;


    /**
     * Вызывается при создании представления фрагмента.
     *
     * @param inflater           Инфлятор макета.
     * @param container          Контейнер для группы представлений.
     * @param savedInstanceState Сохраненное состояние экземпляра.
     * @return Корневое представление.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ContentMainCiphererFragmentBinding.inflate(inflater, container, false);
        dataBaseHelper = new DataBaseHelper(getContext());
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
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setAdapterToAlphabetListView();
        spinnerAlphabetAddOnItemSelected();
        handleEncryptionButtonAction();
        initRecyclerViewHistory();
        linearShutterLayout = binding.shutterContainer;
        layoutInitialHeight = linearShutterLayout.getLayoutParams().height;
        setButtonActions();
        setCipherModeSwitcher();
    }

    /**
     * Инициализация пользовательского интерфейса.
     * @param view Пользовательский интерфейс.
     */
    private void initViews(View view) {
        cipherMode = CipherMode.PlainTextEncrypter;
        upEditText = binding.upTextEdit;
        downEditText = binding.downTextEdit;
        if (getArguments() != null) {
            upEditText.setText(getArguments().getString(ARG_EDIT_TEXT_UP));
            downEditText.setText(getArguments().getString(ARG_EDIT_TEXT_DOWN));
        }
    }


    /**
     * Установка действий для кнопок.
     */
    private void setButtonActions() {
        binding.btnShowEncryptingHistory.setOnClickListener(view ->
                NavHostFragment.findNavController(CiphererFragment.this)
                        .navigate(R.id.action_to_History)
        );
        binding.shutterViewLine.setOnClickListener(view -> animateView());
    }

    /**
     * Установка переключателя режима шифрования.
     */
    private void setCipherModeSwitcher() {
        SwitchCompat cipherModeSwitcher = binding.cipherModeSwitcher;
        cipherModeSwitcher.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                cipherModeSwitcher.setText(R.string.deciphering_mode);
                cipherMode = CipherMode.EncryptedTextDecrypter;
                upEditText.setHint(R.string.text_cipheredText);
                downEditText.setHint(R.string.deciphered_text);
            } else {
                cipherMode = CipherMode.PlainTextEncrypter;
                cipherModeSwitcher.setText(R.string.ciphering_mode);
                upEditText.setHint(R.string.text_toCipher);
                downEditText.setHint(R.string.text_cipheredText);
            }
        });
    }


    /**
     * Анимация пользовательского интерфейса и изменение размеров.
     */
    private void animateView() {
        ValueAnimator animator = new ValueAnimator();
        int targetHeight = isMaximizedShutter ? 70 : 500;
        isMaximizedShutter = !isMaximizedShutter;
        animator.setIntValues(linearShutterLayout.getHeight(), targetHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = linearShutterLayout.getLayoutParams();
                layoutParams.height = value;
                linearShutterLayout.setLayoutParams(layoutParams);
            }
        });
        animator.setDuration(500);
        animator.start();
    }

    /**
     * Инициализация адаптера для списка алфавитов.
     */
    private void setAdapterToAlphabetListView() {
        upEditText = binding.upTextEdit;
        downEditText = binding.downTextEdit;

        if (getArguments() != null) {
            upEditText.setText(getArguments().getString(ARG_EDIT_TEXT_UP));
            downEditText.setText(getArguments().getString(ARG_EDIT_TEXT_DOWN));
        }

        listViewAlphabets = binding.listViewAlphabets;
        spinnerCipherKey = binding.spinnerKeyField;
        spinnerCipherKey.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (spinnerCipherKey.getCount() == 0) {
                        listViewAlphabets.setBackground(ResourcesCompat.getDrawable(getResources(),
                                R.drawable.error_edittext_background, requireActivity().getTheme()));
                        Toast.makeText(getContext(), "Выберите алфавит!", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });
        String[] alphabets = {
                getString(R.string.english_alphabet),
                getString(R.string.russian_alphabet),
                getString(R.string.greek_alphabet),
                getString(R.string.polish_alphabet),
                getString(R.string.ukranian_alphabet)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.requireContext(),
                android.R.layout.simple_list_item_1,
                alphabets);
        View listItem = adapter.getView(0, null, listViewAlphabets);
        listItem.measure(0, 0);
        int totalHeight = listItem.getMeasuredHeight();

        ViewGroup.LayoutParams params = listViewAlphabets.getLayoutParams();
        params.height = totalHeight + (listViewAlphabets.getDividerHeight() * (listViewAlphabets.getCount() - 1));
        listViewAlphabets.setAdapter(adapter);
        listViewAlphabets.setLayoutParams(params);
        listViewAlphabets.requestLayout();
    }

    /**
     * Обработка действия кнопки шифрования.
     * Этот метод проверяет, пуст ли ввод пользователя и выбран ли алфавит и ключ.
     * Если все в порядке, выполняется операция шифрования и обновляется история.
     */
    private void handleEncryptionButtonAction() {
        ImageButton btnEncrypt = binding.btnEncrypt;
        btnEncrypt.setOnClickListener(view -> {
            if (isInputEmpty()) {
                showInputError();
                return;
            }

            if (isAlphabetOrKeyNotSelected()) {
                showAlphabetOrKeyError();
                return;
            }

            performCipherOperation();
            updateHistory();
        });
    }

    /**
     * Проверяет, пуст ли ввод.
     * @return true, если ввод пуст, иначе false.
     */
    private boolean isInputEmpty() {
        return upEditText.getText().length() == 0;
    }

    /**
     * Показывает ошибку, если ввод пуст.
     */
    private void showInputError() {
        upEditText.setBackground(ResourcesCompat.getDrawable(getResources(),
                R.drawable.error_edittext_background, requireActivity().getTheme()));
        Toast.makeText(getContext(), "Введите текст в верхнее поле для обработки!", Toast.LENGTH_LONG)
                .show();
    }

    /**
     * Проверяет, выбран ли алфавит и ключ.
     * @return true, если алфавит или ключ не выбраны, иначе false.
     */
    private boolean isAlphabetOrKeyNotSelected() {
        return selectedAlphabetIndex == -1;
    }

    /**
     * Показывает ошибку, если алфавит или ключ не выбраны.
     */
    private void showAlphabetOrKeyError() {
        spinnerCipherKey.setBackground(ResourcesCompat.getDrawable(getResources(),
                R.drawable.error_edittext_background, requireActivity().getTheme()));
        listViewAlphabets.setBackground(ResourcesCompat.getDrawable(getResources(),
                R.drawable.error_edittext_background, requireActivity().getTheme()));
        Toast.makeText(getContext(), "Не выбран алфавит или ключ!", Toast.LENGTH_LONG).show();
    }

    /**
     * Выполняет операцию шифрования или дешифрования в зависимости от выбранного режима.
     */
    private void performCipherOperation() {
        CaesarCipherer caesarCipherer = new CaesarCipherer(getAlphabetFull(selectedAlphabetIndex));
        String inputText = String.valueOf(upEditText.getText());
        String outputText;
        int isEncrypting;

        if (cipherMode == CipherMode.PlainTextEncrypter) {
            outputText = caesarCipherer.encrypt(inputText,
                    Integer.parseInt((String) spinnerCipherKey.getSelectedItem()));
            isEncrypting = 1;
        } else {
            outputText = caesarCipherer.decrypt(inputText,
                    Integer.parseInt((String) spinnerCipherKey.getSelectedItem()));
            isEncrypting = 0;
        }

        downEditText.setText(outputText);
        dataBaseHelper.insertData(inputText, outputText, isEncrypting);
    }

    /**
     * Обновляет историю шифрования.
     */
    private void updateHistory() {
        initRecyclerViewHistory();
    }

    /**
     * Устанавливает слушатель выбора элемента для списка алфавитов.
     */
    private void spinnerAlphabetAddOnItemSelected() {
        listViewAlphabets.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedAlphabetIndex = i;
            String alphabet = getAlphabetFull(selectedAlphabetIndex);
            String[] validKeyValues = new String[alphabet.length()];
            for (int iteration = 0; iteration < alphabet.length(); iteration++) {
                validKeyValues[iteration] = String.valueOf(iteration + 1);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireActivity(),
                    android.R.layout.simple_spinner_item, validKeyValues);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCipherKey.setAdapter(arrayAdapter);
            spinnerCipherKey.setBackground(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.edittext_border_selector, requireActivity().getTheme()));
            listViewAlphabets.setBackground(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.edittext_border_selector, requireActivity().getTheme()));
        });
    }

    /**
     * Возвращает полный алфавит на основе индекса.
     *
     * @param i индекс алфавита
     * @return полный алфавит
     */
    private String getAlphabetFull(int i) {
        return getResources().getStringArray(R.array.alphabets)[i];
    }

    /**
     * Инициализация RecyclerView для отображения истории шифрования.
     */
    private void initRecyclerViewHistory() {
        RecyclerView recyclerViewHistory = binding.recyclerViewHistory;
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewHistory.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        List<HistoryItem> newHistoryItems = dataBaseHelper.getFirstThreeRecords();
        HistoryAdapter newHistoryAdapter = new HistoryAdapter(newHistoryItems, listener);
        recyclerViewHistory.setAdapter(newHistoryAdapter);
        RecyclerView.ViewHolder viewHolder = Objects.requireNonNull(recyclerViewHistory.getAdapter())
                .createViewHolder(recyclerViewHistory, 0);
        Objects.requireNonNull(recyclerViewHistory.getLayoutManager())
                .measureChildWithMargins(viewHolder.itemView, 0, 0);
        recyclerViewHistory.getLayoutParams().height = viewHolder.itemView.getMeasuredHeight();
        recyclerViewHistory.getRecycledViewPool().putRecycledView(viewHolder);
    }

    /**
     * Установка текста в текстовые поля.
     *
     * @param upEditText   Текст для верхнего текстового поля.
     * @param downEditText Текст для нижнего текстового поля.
     */
    public void setEditTexts(String upEditText, String downEditText){
        this.upEditText.setText(upEditText);
        this.downEditText.setText(downEditText);
    }

    /**
     * Подключение к контексту слушателя и проверка.
     * Проверяет, реализует ли контекст интерфейс OnHistoryItemButtonClickListener.
     *
     * @param context Контекст, в котором используется фрагмент.
     * @throws RuntimeException Если контекст не реализует интерфейс OnHistoryItemButtonClickListener.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnHistoryItemButtonClickListener){
            listener = (OnHistoryItemButtonClickListener) context;
        }
        else {
            throw new RuntimeException(context.toString() +
                    "must implemented OnHistoryItemButtonClickListener");
        }
    }

    /**
     * Отключение от контекста слушателя.
     * Удаляет ссылку на слушателя.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }



    /**
     * Режимы шифрования/дешифрования.
     */
    enum CipherMode {
        /**
         * Plain text encrypter cipher mode.
         */
        PlainTextEncrypter,
        /**
         * Encrypted text decrypter cipher mode.
         */
        EncryptedTextDecrypter
    }

}