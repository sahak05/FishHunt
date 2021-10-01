import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Controleur {
    private final Jeu jeu;

    /**
     * Constructeur du controleur
     */
    public Controleur() {
        jeu = new Jeu();
    }

    /**
     * Demande au jeu d'augmenter le niveau
     */
    public void levelUp() {
        jeu.levelUp();
    }

    /**
     * Demande au jeu d'augmenter le score
     */
    public void augmenterScore() {
        jeu.augmenterScore();
    }

    /**
     * Demande au jeu de terminé la partie
     */
    public void gameOver() {
        jeu.gameOver();
    }

    /**
     * Demande au jeu de déplacer la cible
     *
     * @param x Position en x (selon le canvas)
     * @param y Position en y (selon le canvas)
     */
    public void bougerCible(double x, double y) {
        jeu.bougerCible(x, y);
    }

    /**
     * Demande au jeu de lancer une balle
     *
     * @param x Position en x (selon le canvas)
     * @param y Position en y (selon le canvas)
     */
    public void lancerBalle(double x, double y) {
        jeu.lancerBalle(x, y);
    }

    /**
     * Demande aua jeu d'ajouter une vie
     */
    public void ajouterVie() {
        jeu.ajouterVie();
    }

    /**
     * Demande au jeu de se mettre à jour
     *
     * @param dt Temps depuis le dernier update
     */
    public void update(double dt) {
        jeu.update(dt);
    }

    /**
     * Demande au jeu de dessiner à l'écran
     *
     * @param context Contexte graphique sur lequel dessiner le jeu
     */
    public void draw(GraphicsContext context) {
        jeu.draw(context);
    }

    /**
     * Demande au jeu les high scores
     *
     * @return Les scores du jeu
     */
    public ArrayList<String> getScores() {
        return jeu.getScores();
    }

    /**
     * Demande au jeu si la partie est terminée
     *
     * @return Vrai si la partie est terminée
     */
    public boolean gameEnded() {
        return jeu.gameEnded();
    }

    /**
     * Demande au jeu de commencer une nouvelle partie
     */
    public void newGame() {
        jeu.reset();
    }

    /**
     * Retourne le dernier score si c'est un highscore (top 10)
     * ou -1 si ce ne l'est pas
     *
     * @return le high score ou -1 si c'est n'est pas un highscore
     */
    public int getNewHighScore() {
        return jeu.getNewHighScore();
    }

    /**
     * Demande au jeu d'enregistrer le score le plus récent
     *
     * @param name Nom du joueur qui à fait le score
     */
    public void saveScore(String name) {
        jeu.saveScore(name);
    }
}
