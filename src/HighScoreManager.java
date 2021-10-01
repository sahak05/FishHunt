import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class HighScoreManager {
    private final String cheminScores;
    private ArrayList<Pair<Integer, String>> highScores;

    /**
     * Constructeur de la classe HighScoreManager
     *
     * @param highScorePath Chemin du fichier ou aller chercker et stocker les scores
     */
    public HighScoreManager(String highScorePath) {
        cheminScores = highScorePath;
        fetchHighScores();
    }

    /**
     * Lit les scores dans le fichier scores.txt et les tries si le fichier existe
     */
    private void fetchHighScores() {
        highScores = new ArrayList<>();

        try {
            // On essais de lire le fichier contenant les highScore
            FileReader fr = new FileReader(cheminScores);
            BufferedReader br = new BufferedReader(fr);
            String ligne;
            String name = "";
            boolean isName = true;
            while ((ligne = br.readLine()) != null) {
                if (isName) {
                    isName = false;
                    name = ligne;
                } else {
                    isName = true;
                    Integer score = Integer.valueOf(ligne);
                    highScores.add(new Pair<>(score, name));
                }
            }
        } catch (IOException e) {
            System.out.println("Le fichier de scores n'existe pas, il sera créé à la sauvegarde du prochain score");
        }
        // On remet en ordre au cas ou
        ordonnerScores();
    }

    /**
     * Trie les scores et enleve ceux qui ne sont pas dans le top 10
     */
    private void ordonnerScores() {
        // On trie du plus grand au plus petit selon le score
        highScores.sort(Collections.reverseOrder(Comparator.comparing(Pair::getKey)));
        if (highScores.size() > 10) {
            Iterator<Pair<Integer, String>> scoreIterator = highScores.iterator();
            int i = 0;
            while (scoreIterator.hasNext()) {
                scoreIterator.next();
                // On enleve tout les scores en bas des 10 premiers
                if (i >= 10) {
                    scoreIterator.remove();
                }
                i++;
            }
        }
    }

    /**
     * Enregistre le score
     *
     * @param nom      Nom du joueur qui à fait le score
     * @param newScore Score du joueur
     */
    public void saveScore(String nom, int newScore) {
        highScores.add(new Pair<>(newScore, nom));
        // On remet en ordre les scores
        ordonnerScores();

        try {
            File fichier = new File(cheminScores);
            fichier.createNewFile(); // Crée le fichier s'il n'existe pas
            // On veut réécrir le fichier
            BufferedWriter writer = new BufferedWriter(new FileWriter(fichier, false));
            for (Pair<Integer, String> score : highScores) {
                writer.write(score.getValue());
                writer.newLine();
                writer.write(String.valueOf(score.getKey()));
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Pair<Integer, String>> getScores() {
        return highScores;
    }

    /**
     * Vérifie si un score fait parti des 10 meilleur
     *
     * @param score Le score à vérifier
     * @return Vrai si le score fait parti du top 10
     */
    public boolean isNewHighScore(int score) {
        return highScores.size() < 10 || score > highScores.get(highScores.size() - 1).getKey();
    }
}
