package com.caesarcipherer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.caesarcipherer.databinding.ActivityMainBinding;
import com.caesarcipherer.fragments.AboutAppFragment;
import com.caesarcipherer.fragments.AboutAuthorFragment;
import com.caesarcipherer.fragments.CiphererFragment;
import com.caesarcipherer.fragments.HistoryFragment;
import com.caesarcipherer.fragments.TitleFragment;
import com.caesarcipherer.historyComponents.OnHistoryItemButtonClickListener;

import java.util.Objects;

/**
 * Главная активность приложения, содержашая необходимые фрагменты.
 *
 * @author Жегздринь Ангелина
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity implements OnHistoryItemButtonClickListener{
    /**
     * Контроллер навигации для управления переходами между фрагментами.
     */
    private NavController navController;

    /**
     * Привязка к основной активности.
     */
    private ActivityMainBinding binding;

    /**
     * Конфигурация панели приложения.
     */
    private AppBarConfiguration appBarConfiguration;


    /**
     * Вызывается при создании активности.
     *
     * @param savedInstanceState Сохраненное состояние экземпляра.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        FragmentContainerView containerView = findViewById(R.id.nav_host_fragment_content_main);

        containerView.post(() -> {
            navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        });
    }

    /**
     * Вызывается при создании опций меню.
     *
     * @param menu Меню.
     * @return true, если меню было создано успешно.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Вызывается при выборе пункта меню.
     *
     * @param item Выбранный пункт меню.
     * @return true, если пункт меню был обработан.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Получаем активный фрагмент
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment_content_main);
        Fragment activeFragment = Objects.requireNonNull(navHostFragment).
                getChildFragmentManager().getFragments().get(0);

        // Обрабатываем выбор пункта меню "Об авторе"
        if (item.getItemId() == R.id.action_about_author) {
            // В зависимости от активного фрагмента, переходим к фрагменту "Об авторе"
            if (activeFragment instanceof CiphererFragment) {
                NavHostFragment.findNavController(activeFragment)
                        .navigate(R.id.action_to_AboutAuthor);
            }
            else if (activeFragment instanceof HistoryFragment){
                NavHostFragment.findNavController(activeFragment)
                        .navigate(R.id.action_to_AboutAuthor);
            }
            else if (activeFragment instanceof AboutAppFragment){
                NavHostFragment.findNavController(activeFragment)
                        .navigate(R.id.action_to_AboutAuthor);
            }
            else if (activeFragment instanceof  TitleFragment){
                NavHostFragment.findNavController(activeFragment)
                        .navigate(R.id.action_to_AboutAuthor);
            }
            return true;
        }

        // Обрабатываем выбор пункта меню "О приложении"
        if(item.getItemId() == R.id.action_about_app){
            // В зависимости от активного фрагмента, переходим к фрагменту "О приложении"
            if (activeFragment instanceof CiphererFragment) {
                NavHostFragment.findNavController(activeFragment)
                        .navigate(R.id.action_to_AboutApp);
            }
            else if (activeFragment instanceof HistoryFragment){
                NavHostFragment.findNavController(activeFragment)
                        .navigate(R.id.action_to_AboutApp);
            }
            else if (activeFragment instanceof AboutAuthorFragment){
                NavHostFragment.findNavController(activeFragment)
                        .navigate(R.id.action_to_AboutApp);
            }
            else if (activeFragment instanceof TitleFragment){
                NavHostFragment.findNavController(activeFragment)
                        .navigate(R.id.action_to_AboutApp);
            }
            return true;
        }
        // Обрабатываем выбор пункта меню "О приложении"
        if(item.getItemId() == R.id.action_title_list){
            // В зависимости от активного фрагмента, переходим к фрагменту "О приложении"
            if (activeFragment instanceof CiphererFragment) {
                NavHostFragment.findNavController(activeFragment)
                        .navigate(R.id.action_to_Title);
            }
            else if (activeFragment instanceof HistoryFragment){
                NavHostFragment.findNavController(activeFragment)
                        .navigate(R.id.action_to_Title);
            }
            else if (activeFragment instanceof AboutAuthorFragment){
                NavHostFragment.findNavController(activeFragment)
                        .navigate(R.id.action_to_Title);
            }
            else if (activeFragment instanceof TitleFragment){
                NavHostFragment.findNavController(activeFragment)
                        .navigate(R.id.action_to_Title);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Вызывается при поддержке навигации вверх.
     *
     * @return true, если навигация была успешной.
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Обрабатывает нажатие кнопки элемента истории.
     *
     * @param editTextUpString   Текст, который будет установлен в верхнее текстовое поле.
     * @param editTextDownString Текст, который будет установлен в нижнее текстовое поле.
     */
    @Override
    public void onHistoryItemButtonClicked(String editTextUpString, String editTextDownString) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment_content_main);
        Fragment activeFragment = Objects.requireNonNull(navHostFragment).
                getChildFragmentManager().getFragments().get(0);

        if (!(activeFragment instanceof CiphererFragment)) {
            Bundle ciphererBundle = new Bundle();
            ciphererBundle.putString(CiphererFragment.ARG_EDIT_TEXT_UP, editTextUpString);
            ciphererBundle.putString(CiphererFragment.ARG_EDIT_TEXT_DOWN, editTextDownString);
            navController.navigate(R.id.action_to_Main, ciphererBundle);
        }
        else {
            ((CiphererFragment) activeFragment).setEditTexts(editTextUpString, editTextDownString);
        }
    }
}


