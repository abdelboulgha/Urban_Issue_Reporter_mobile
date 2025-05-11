package com.example.urban_issue_reporter_mobile.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class VoteManager {
    private static final String PREFS_NAME = "UserVotesPrefs";
    private static final String VOTED_RECLAMATIONS_KEY = "votedReclamations";

    // Vérifier si l'utilisateur a déjà voté pour une réclamation
    public static boolean hasVoted(Context context, int reclamationId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> votedIds = prefs.getStringSet(VOTED_RECLAMATIONS_KEY, new HashSet<>());
        return votedIds.contains(String.valueOf(reclamationId));
    }

    // Enregistrer un vote
    public static void addVote(Context context, int reclamationId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> votedIds = new HashSet<>(prefs.getStringSet(VOTED_RECLAMATIONS_KEY, new HashSet<>()));
        votedIds.add(String.valueOf(reclamationId));
        prefs.edit().putStringSet(VOTED_RECLAMATIONS_KEY, votedIds).apply();
    }

    // Retirer un vote
    public static void removeVote(Context context, int reclamationId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> votedIds = new HashSet<>(prefs.getStringSet(VOTED_RECLAMATIONS_KEY, new HashSet<>()));
        votedIds.remove(String.valueOf(reclamationId));
        prefs.edit().putStringSet(VOTED_RECLAMATIONS_KEY, votedIds).apply();
    }
}
