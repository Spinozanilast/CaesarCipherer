package com.caesarcipherer.historyComponents;

/**
 * Элемент истории.
 *
 * @author Жегздринь Ангелина
 * @version 1.0
 */
public class HistoryItem {
    private final String textID; // ID текста
    private final String textToProcess; // Текст для обработки
    private final String processedText; // Обработанный текст
    private final boolean isEncrypting; // Флаг шифрования
    private String dateProcessedAt; // Дата обработки

    /**
     * Конструктор элемента истории.
     *
     * @param textID        ID текста.
     * @param textToProcess Текст для обработки.
     * @param processedText Обработанный текст.
     * @param isEncrypting  Флаг шифрования.
     * @param dateCreatedAt Дата создания.
     */
    public HistoryItem(int textID , String textToProcess, String processedText, boolean isEncrypting, String dateCreatedAt) {
        this.textID = String.valueOf(textID);
        this.textToProcess = textToProcess;
        this.processedText = processedText;
        this.isEncrypting = isEncrypting;
        this.dateProcessedAt = String.valueOf(dateCreatedAt);

    }

    /**
     * Возвращает текст для обработки.
     *
     * @return Текст для обработки.
     */
    public String getTextToProcess() {
        return textToProcess;
    }

    /**
     * Возвращает обработанный текст.
     *
     * @return Обработанный текст.
     */
    public String getProcessedText() {
        return processedText;
    }

    /**
     * Возвращает флаг шифрования.
     *
     * @return Флаг шифрования.
     */
    public boolean isEncrypting() {
        return isEncrypting;
    }

    /**
     * Возвращает ID текста.
     *
     * @return ID текста.
     */
    public String getTextID() {
        return textID;
    }

    /**
     * Возвращает дату обработки.
     *
     * @return Дата обработки.
     */
    public String getDateProcessedAt() {
        return dateProcessedAt;
    }

    /**
     * Устанавливает дату обработки.
     *
     * @param dateProcessedAt Дата обработки.
     */
    public void setDateProcessedAt(String dateProcessedAt) {
        this.dateProcessedAt = dateProcessedAt;
    }
}
