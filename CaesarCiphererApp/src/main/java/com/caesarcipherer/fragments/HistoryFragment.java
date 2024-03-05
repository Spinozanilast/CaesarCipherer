package com.caesarcipherer.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesarcipherer.R;
import com.caesarcipherer.databinding.HistoryFragmentBinding;
import com.caesarcipherer.historyComponents.HistoryAdapter;
import com.caesarcipherer.historyComponents.HistoryItem;
import com.caesarcipherer.historyComponents.OnHistoryItemButtonClickListener;
import com.caesarcipherer.sqlite.DataBaseHelper;

import java.util.Comparator;
import java.util.List;

/**
 * Фрагмент истории.
 *
 * @author Жегздринь А.Р.
 * @version 1.1
 */
public class HistoryFragment extends Fragment{
    /**
     * Привязка к фрагменту истории.
     */
    private HistoryFragmentBinding binding;

    /**
     * Метод, вызываемый при нажатии на элемент истории.
     */
    OnHistoryItemButtonClickListener listener;
    /**
     * Список элементов истории.
     */
    private List<HistoryItem> historyItems;

    /**
     * Виджет для отображения списка на основе изменяемых данных.
     */
    private RecyclerView recyclerView;

    /**
     * Адаптер для отображения элементов истории.
     */
    private HistoryAdapter adapter;


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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = HistoryFragmentBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerView;
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

        binding.clearHistoryButton.setOnClickListener(showDeleteHistoryButton());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        historyItems = dataBaseHelper.getAllHistory();

        if (historyItems.isEmpty()) {
            Toast.makeText(getContext(), "История пуста", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new HistoryAdapter(historyItems, listener);
            recyclerView.setAdapter(adapter);
        }

        addLayoutsSortActions();
    }

    /**
     * Метод для отображения кнопки удаления истории.
     *
     * @return OnClickListener, который отображает диалоговое окно для подтверждения удаления истории.
     */
    private View.OnClickListener showDeleteHistoryButton() {
        return v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Удаление истории");
            builder.setMessage("Вы уверены, что хотите удалить всю историю?");

            // Добавляем кнопку "Удалить"
            builder.setPositiveButton("Удалить", (dialog, which) -> {
                clearDatabaseWithHistory();
            });

            // Добавляем кнопку "Отмена"
            builder.setNegativeButton("Отмена", (dialog, which) -> {
                dialog.dismiss();
            });

            // Создаем и отображаем диалог
            AlertDialog dialog = builder.create();
            dialog.show();
        };
    }

    /**
     * Очищает содержимое таблицы базы данных.
     * Этот метод удаляет все записи из таблицы, связанной с историей.
     * После удаления обновляется адаптер (если используется) для отображения изменений.
     */
    @SuppressLint("NotifyDataSetChanged")
    private void clearDatabaseWithHistory() {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
            SQLiteDatabase writableDb = dataBaseHelper.getWritableDatabase();
            SQLiteDatabase readableDb = dataBaseHelper.getReadableDatabase();

            // Удаление данных из таблицы
            dataBaseHelper.clearTableData(writableDb);
            dataBaseHelper.clearTableData(readableDb);

            // Обновление адаптера (если используется)
            adapter.notifyDataSetChanged();
        } catch (NullPointerException exception) {
            Toast.makeText(getContext(), "История пуста", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Метод для добавления действий сортировки в макеты.
     */
    private void addLayoutsSortActions() {
        LinearLayout layoutSortById = binding.idLayout;

        final boolean[] isSortByIdReversed = {false};
        ImageView idSortIcon = binding.idNumberIcon;

        layoutSortById.setOnClickListener(v -> {
            try {
                sortBy(idSortIcon, (o1, o2) -> isSortByIdReversed[0] ? Integer.compare(
                        Integer.parseInt(o2.getTextID()),
                        Integer.parseInt(o1.getTextID()))
                        :
                        Integer.compare(Integer.parseInt(o1.getTextID()),
                                Integer.parseInt(o2.getTextID())), isSortByIdReversed);
                isSortByIdReversed[0] = !isSortByIdReversed[0];
            } catch (NullPointerException exception) {
                Toast.makeText(getContext(), "История пуста", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Метод для сортировки элементов истории.
     *
     * @param icon               Иконка, которую нужно обновить в зависимости от направления сортировки.
     * @param comparator         Компаратор для сортировки элементов истории.
     * @param isSortByIdReversed Флаг, указывающий, следует ли выполнить обратную сортировку.
     */
    @SuppressLint("NotifyDataSetChanged")
    private void sortBy(ImageView icon, Comparator<HistoryItem> comparator, boolean[] isSortByIdReversed) {
        historyItems.sort(comparator);
        adapter.notifyDataSetChanged();
        icon.setImageResource(isSortByIdReversed[0] ? R.drawable.rightordersorticon : R.drawable.ascordersorticon);
    }

    /**
     * Вызывается, когда фрагмент связывается с активностью.
     *
     * @param context Контекст активности, к которой прикреплен фрагмент.
     * @throws RuntimeException Если активность не реализует интерфейс OnHistoryItemButtonClickListener.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnHistoryItemButtonClickListener) {
            listener = (OnHistoryItemButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    "must implemented OnHistoryItemButtonClickListener");
        }
    }

    /**
     * Вызывается, когда фрагмент отсоединяется от активности.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}