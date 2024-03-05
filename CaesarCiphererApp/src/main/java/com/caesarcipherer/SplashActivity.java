package com.caesarcipherer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Экран-заставка приложения.
 *
 * @author Жегздринь А.Р.
 * @version 1.0
 */
public class SplashActivity extends AppCompatActivity {
    /**
     *  Задержка экрана-заставки в миллисекундах
     */
    private static final int SPLASH_DELAY = 2000;

    /**
     * Вызывается при создании активности.
     *
     * @param savedInstanceState Сохраненное состояние экземпляра.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Инициализация анимированного логотипа
        ImageView animatedLogo = findViewById(R.id.splashAppIcon);

        // Загрузка анимации
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);

        // Запуск анимации
        animatedLogo.startAnimation(animation);

        // Запуск задержки перед переходом на главную активность
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}
