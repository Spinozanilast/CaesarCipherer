package com.caesarcipherer.historyComponents;

/**
 * Интерфейс слушателя нажатия на элемент истории.
 * <p>
 * Этот интерфейс предназначен для обработки событий нажатия на элементы истории в адаптере.
 * <p>
 * Пример использования:
 * <pre>
 * {@code
 * public class MyFragment extends Fragment implements OnHistoryItemButtonClickListener {
 *     ...
 *     @Override
 *     public void onHistoryItemClick(HistoryItem item) {
 *         // Обработка нажатия на элемент истории
 *     }
 * }
 * }*
 * </pre>
 */
public interface OnHistoryItemButtonClickListener {
    /**
     * Метод, вызываемый при нажатии на элемент истории.
     *
     * @param editTextUpString   the edit text up string
     * @param editTextDownString the edit text down string
     */
    void onHistoryItemButtonClicked(String editTextUpString, String editTextDownString);
}
