package com.caesarcipherer.historyComponents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caesarcipherer.R;

import java.util.List;

/**
 * Адаптер истории для RecycleView
 *
 * @author Жегздринь Ангелина
 * @version 1.1
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<HistoryItem> historyItems; // Список элементов истории

    private OnHistoryItemButtonClickListener listener;

    /**
     * Конструктор адаптера истории.
     *
     * @param historyItems    Список элементов истории.
     * @param onClickListener the on click listener
     */
    public HistoryAdapter(List<HistoryItem> historyItems, OnHistoryItemButtonClickListener onClickListener) {
        this.historyItems = historyItems;
        this.listener = onClickListener;
    }

    /**
     * Вызывается при создании ViewHolder.
     *
     * @param parent   Родительское представление.
     * @param viewType Тип представления.
     * @return ViewHolder. view holder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Вызывается при привязке ViewHolder.
     *
     * @param holder   ViewHolder.
     * @param position Позиция в списке.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryItem item = historyItems.get(position);
        holder.textID.setText(String.format("Запись %s", item.getTextID()));
        holder.textToProcessView.setText(item.getTextToProcess());
        holder.processedTextView.setText(item.getProcessedText());
        holder.dateCreatedAt.setText(item.getDateProcessedAt());
        holder.isEncryptedImageView.setImageResource(item.isEncrypting() ? R.mipmap.ic_encrypted_icon : R.mipmap.ic_decrypted_icon);
        holder.isEncryptedImageView.setTooltipText(item.isEncrypting() ? "Шифрование" : "Дешифрование");
        holder.imgBtnInsertTexts.setOnClickListener(view -> listener.onHistoryItemButtonClicked(
                item.getTextToProcess(),
                item.getProcessedText()));
    }

    /**
     * Возвращает количество элементов в списке.
     *
     * @return Количество элементов.
     */
    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    /**
     * ViewHolder для элементов истории.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Text id.
         */
        TextView textID;
        /**
         * The Text to process view.
         */
        TextView textToProcessView;
        /**
         * The Processed text view.
         */
        TextView processedTextView;
        /**
         * The Is encrypted image view.
         */
        ImageView isEncryptedImageView;
        /**
         * The Date created at.
         */
        TextView dateCreatedAt;
        /**
         * The Img btn insert texts.
         */
        ImageButton imgBtnInsertTexts;

        /**
         * Конструктор ViewHolder.
         *
         * @param itemView Представление элемента.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textID = itemView.findViewById(R.id.text_id);
            dateCreatedAt = itemView.findViewById(R.id.text_processed_at);
            textToProcessView = itemView.findViewById(R.id.text_to_process);
            processedTextView = itemView.findViewById(R.id.processed_text);
            isEncryptedImageView = itemView.findViewById(R.id.imageview_encrypted_or_decrypted);
            imgBtnInsertTexts = itemView.findViewById(R.id.insert_texts_to_cipherer);
        }
    }
}
