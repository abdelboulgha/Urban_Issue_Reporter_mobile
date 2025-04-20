package com.example.urban_issue_reporter_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.urban_issue_reporter_mobile.adapter.OnboardingAdapter;

import java.util.ArrayList;
import java.util.List;

public class welcomePage extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout indicatorLayout;
    private List<OnboardingItem> onboardingItems;
    private OnboardingAdapter onboardingAdapter;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialiser les vues
        viewPager = findViewById(R.id.viewPagerOnboarding);
        indicatorLayout = findViewById(R.id.pageIndicator);
        btnLogin = findViewById(R.id.btnLogin);

        // Préparer les données d'onboarding
        setupOnboardingItems();

        // Configurer l'adaptateur de ViewPager
        onboardingAdapter = new OnboardingAdapter(this, onboardingItems);
        viewPager.setAdapter(onboardingAdapter);

        // Configurer les indicateurs de page
        setupIndicators();
        setCurrentIndicator(0);

        // Configurer le listener de changement de page
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);

                // Changer le texte du bouton sur la dernière page
                if (position == onboardingItems.size() - 1) {
                    btnLogin.setText("Se connecter");
                } else {
                    btnLogin.setText("Suivant");
                }
            }
        });

        // Configuration du bouton pour naviguer entre les pages ou vers la page d'authentification
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() < onboardingItems.size() - 1) {
                    // Si ce n'est pas la dernière page, passer à la page suivante
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {
                    // Si c'est la dernière page, aller à la page d'authentification
                    Intent intent = new Intent(welcomePage.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Fermer l'activité de bienvenue
                }
            }
        });
    }

    private void setupOnboardingItems() {
        onboardingItems = new ArrayList<>();

        // Ajouter les éléments d'onboarding
        OnboardingItem item1 = new OnboardingItem();
        item1.setTitle("Signalez les problèmes");
        item1.setDescription("Photographiez et signalez facilement les problèmes d'infrastructure dans votre quartier");
        item1.setImageResId(android.R.drawable.ic_menu_camera);

        OnboardingItem item2 = new OnboardingItem();
        item2.setTitle("Localisez avec précision");
        item2.setDescription("Utilisez la carte pour indiquer l'emplacement exact du problème");
        item2.setImageResId(android.R.drawable.ic_menu_mylocation);

        OnboardingItem item3 = new OnboardingItem();
        item3.setTitle("Suivez les mises à jour");
        item3.setDescription("Recevez des notifications sur l'avancement des réparations");
        item3.setImageResId(android.R.drawable.ic_popup_reminder);

        onboardingItems.add(item1);
        onboardingItems.add(item2);
        onboardingItems.add(item3);
    }

    private void setupIndicators() {
        ImageView[] indicators = new ImageView[onboardingItems.size()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            indicatorLayout.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = indicatorLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) indicatorLayout.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicator_inactive));
            }
        }
    }
}