package com.example.urban_issue_reporter_mobile.ui.faq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.urban_issue_reporter_mobile.R;
import com.example.urban_issue_reporter_mobile.databinding.FragmentFaqBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.imageview.ShapeableImageView;

public class faq extends Fragment {

    private FragmentFaqBinding binding;
    private FaqViewModel faqViewModel;

    public faq() {
        // Required empty public constructor
    }

    public static faq newInstance() {
        return new faq();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        faqViewModel = new ViewModelProvider(this).get(FaqViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFaqBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupFaqItems();

        return root;
    }

    private void setupFaqItems() {
        // FAQ data
        String[][] faqItems = {
                {"Comment créer une nouvelle réclamation ?",
                        "Pour créer une réclamation, rendez-vous dans l'onglet 'Nouvelle réclamation' du menu principal. Remplissez le formulaire avec les détails du problème et soumettez-le. Vous recevrez un numéro de suivi."},
                {"Comment suivre l'état de ma réclamation ?",
                        "Toutes vos réclamations apparaissent dans votre tableau de bord. Le statut est mis à jour en temps réel (En attente, En cours, Résolu, Rejeté). Vous pouvez aussi filtrer par statut."},
                {"Combien de temps prend le traitement d'une réclamation ?",
                        "Le délai varie selon la complexité du problème. Les réclamations urgentes sont traitées sous 48h. Vous recevrez des notifications à chaque étape du processus."},
                {"Puis-je modifier une réclamation après l'avoir soumise ?",
                        "Oui, tant que la réclamation est en statut 'En attente'. Utilisez le bouton 'Modifier' dans votre liste de réclamations. Une fois en cours de traitement, contactez le support."},
                {"Comment fonctionne le système de votes ?",
                        "Les citoyens peuvent voter pour les réclamations qui les concernent. Plus une réclamation reçoit de votes, plus elle est priorisée dans notre système de traitement."},
                {"Que faire si ma réclamation est rejetée ?",
                        "Consultez les motifs de rejet dans les détails de la réclamation. Si vous n'êtes pas d'accord, vous pouvez faire appel en cliquant sur 'Contester' et fournir des informations supplémentaires."}
        };

        LinearLayout faqContainer = binding.faqContainer;

        for (int i = 0; i < faqItems.length; i++) {
            MaterialCardView card = new MaterialCardView(requireContext());
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 32); // bottom margin
            card.setLayoutParams(cardParams);
            card.setRadius(8);
            card.setCardElevation(2);

            LinearLayout cardContent = new LinearLayout(requireContext());
            cardContent.setOrientation(LinearLayout.VERTICAL);
            cardContent.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            card.addView(cardContent);

            // Header layout (question + expand icon)
            LinearLayout headerLayout = new LinearLayout(requireContext());
            headerLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            headerLayout.setPadding(32, 24, 32, 24);
            headerLayout.setLayoutParams(headerParams);

            // Question text
            TextView questionText = new TextView(requireContext());
            LinearLayout.LayoutParams questionParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
            );
            questionText.setLayoutParams(questionParams);
            questionText.setText(faqItems[i][0]);
            questionText.setTextSize(16);
            questionText.setTextColor(getResources().getColor(R.color.teal_700, null));
            headerLayout.addView(questionText);

            // Expand icon
            ShapeableImageView expandIcon = new ShapeableImageView(requireContext());
            expandIcon.setImageResource(R.drawable.baseline_expand_more_24);
            expandIcon.setContentDescription("Expand");
            headerLayout.addView(expandIcon);

            cardContent.addView(headerLayout);

            // Answer container (initially hidden)
            LinearLayout answerContainer = new LinearLayout(requireContext());
            answerContainer.setOrientation(LinearLayout.VERTICAL);
            answerContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            answerContainer.setPadding(32, 0, 32, 24);
            answerContainer.setVisibility(View.GONE);

            // Answer text
            TextView answerText = new TextView(requireContext());
            answerText.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            answerText.setText(faqItems[i][1]);
            answerText.setTextSize(14);
            answerText.setLineSpacing(0, 1.6f);
            answerContainer.addView(answerText);

            cardContent.addView(answerContainer);

            // Set click listener for expansion
            final boolean[] isExpanded = {false};
            headerLayout.setOnClickListener(v -> {
                isExpanded[0] = !isExpanded[0];
                answerContainer.setVisibility(isExpanded[0] ? View.VISIBLE : View.GONE);
                expandIcon.setRotation(isExpanded[0] ? 180 : 0);
            });

            // Expand first item by default
            if (i == 0) {
                isExpanded[0] = true;
                answerContainer.setVisibility(View.VISIBLE);
                expandIcon.setRotation(180);
            }

            faqContainer.addView(card);
        }

        // Add support section at the bottom
        TextView supportHeaderText = new TextView(requireContext());
        LinearLayout.LayoutParams supportHeaderParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        supportHeaderParams.setMargins(0, 64, 0, 16);
        supportHeaderText.setLayoutParams(supportHeaderParams);
        supportHeaderText.setText("Vous ne trouvez pas réponse à votre question ?");
        supportHeaderText.setTextSize(18);
        supportHeaderText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        faqContainer.addView(supportHeaderText);

        TextView supportContactText = new TextView(requireContext());
        supportContactText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        supportContactText.setText("Contactez notre support technique à support@reclamations.gov");
        supportContactText.setTextSize(14);
        supportContactText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        faqContainer.addView(supportContactText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}