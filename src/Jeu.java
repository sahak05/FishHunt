import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class Jeu {
    private double offsetX;
    private double offsetY;

    private int niveau;
    private double timerShowLevel;
    private int score;
    private int vies;
    private int maxVies;
    private boolean partieTermine;
    private double gameOverTimer;

    private PoissonsVies poissonsVies;
    private Cible cible;

    private ArrayList<Bulle> bulles;
    private double tempsDerniereBulles;

    private ArrayList<Balle> balles;

    private ArrayList<Poisson> poissons;
    private double tempsDernierPoissonNorm;
    private double tempsDernierPoissonSpec;

    private final HighScoreManager hsManager;


    /**
     * Constructeur de la classe Jeu
     */
    public Jeu() {
        String cheminScores = "scores.txt";
        hsManager = new HighScoreManager(cheminScores);
        reset();
    }

    /**
     * Met tout les éléments du jeux au point de déaprt
     */
    public void reset() {
        offsetY = -FishHunt.HEIGHT;
        offsetX = 0;
        tempsDerniereBulles = 3;
        tempsDernierPoissonNorm = 3;
        tempsDernierPoissonSpec = 5;

        gameOverTimer = 3;

        bulles = new ArrayList<>();
        balles = new ArrayList<>();
        poissons = new ArrayList<>();
        niveau = 1;
        timerShowLevel = 1;
        score = 0;
        maxVies = 3;
        vies = maxVies;
        poissonsVies = new PoissonsVies(vies, FishHunt.HEIGHT - 130);
        cible = new Cible(0, 0);
        cible.setCenterXY(FishHunt.WIDTH / 2, FishHunt.HEIGHT / 2);
        partieTermine = false;
    }

    /**
     * Augmente le niveau du jeux
     */
    public void levelUp() {
        niveau++;
        timerShowLevel = 3;
        // On enleve les poissons qui seraient encore à l'écran
        poissons = new ArrayList<>();
    }

    /**
     * Déplace la cible à la position x,y (centre de la cible)
     *
     * @param x Position en x
     * @param y Position en y (selon le canvas)
     */
    public void bougerCible(double x, double y) {
        cible.setCenterXY(x, FishHunt.HEIGHT - y);
    }

    /**
     * Ajoute une nouvelle balle à la postion x,y
     *
     * @param x Position en x (selon le canvas)
     * @param y Position en y (selon le canvas)
     */
    public void lancerBalle(double x, double y) {
        balles.add(new Balle(x, FishHunt.HEIGHT - y));
    }

    /**
     * Termine le jeu
     */
    public void gameOver() {
        vies = 0;
        partieTermine = true;
    }

    /**
     * Rajoute une vie, ne peut pas dépasser le maximum
     */
    public void ajouterVie() {
        if (vies < maxVies) {
            vies++;
        }
    }

    /**
     * Augmente le score de 1 et change le niveau au besoin
     */
    public void augmenterScore() {
        score++;
        if (score % 5 == 0) {
            levelUp();
        }
    }

    /**
     * Met à jour toutes les entités du jeu
     *
     * @param dt Temps depuis le dernier update
     */
    public void update(double dt) {
        if (timerShowLevel > 0) {
            timerShowLevel -= dt;

        } else {
            updatePoissons(dt);
        }

        if (partieTermine) {
            gameOverTimer -= dt;
        }

        updateBalles(dt);

        poissonsVies.setVies(vies);

        updateBulles(dt);

    }

    /**
     * Met à jour toutes les balles et vérifies si elle attrapent un poisson
     *
     * @param dt Temps depuis le dernier update
     */
    private void updateBalles(double dt) {
        Iterator<Balle> balleIterator = balles.iterator();
        while (balleIterator.hasNext()) {
            Balle balle = balleIterator.next();
            balle.update(dt);
            // On vérifie si les balles attrapent un poisson lorsaque le rayon est à 0
            if (balle.getRayon() <= 0) {
                // Les balles continues d'update mais ne peuvent pas attraper
                // de poisson lorsque la partie est terminée
                if (!partieTermine) {
                    Iterator<Poisson> poissonIterator = poissons.iterator();
                    while (poissonIterator.hasNext()) {
                        if (balle.touches(poissonIterator.next())) {
                            poissonIterator.remove();
                            augmenterScore();
                        }
                    }
                }

                // On enleve les bulles qui sont disparues
                balleIterator.remove();
            }
        }
    }

    /**
     * Met à jour toutes les bulle et en génère des nouvelles toutes le 3s
     *
     * @param dt Temps depuis le dernier update
     */
    private void updateBulles(double dt) {
        tempsDerniereBulles += dt;
        if (tempsDerniereBulles > 3) {
            tempsDerniereBulles = 0;
            // On génères 3 groupes de 5 bulles à toute les 3 secondes
            for (int i = 0; i < 3; i++) {
                int xBase = ThreadLocalRandom.current().nextInt(0, (int) FishHunt.WIDTH);
                for (int j = 0; j < 5; j++) {
                    bulles.add(new Bulle(xBase));
                }
            }
        }
        Iterator<Bulle> bulleIterator = bulles.iterator();
        // On détruit les bulles qui sont sorties de l'écran
        while (bulleIterator.hasNext()) {
            Bulle bulle = bulleIterator.next();
            bulle.update(dt);
            if (bulle.offScreenTop(offsetY, 0)) {
                bulleIterator.remove();
            }
        }
    }

    /**
     * Met à jour les entités Poisson
     *
     * @param dt Temps depuis la derniere mise a jour
     */
    private void updatePoissons(double dt) {
        Iterator<Poisson> poissonIterator = poissons.iterator();
        while (poissonIterator.hasNext()) {
            Poisson poisson = poissonIterator.next();
            poisson.update(dt);
            // Si le poisson est en dehors de la fenetre un le supprime et dimminue les vies
            if (poisson.offScreenLeft(offsetX, 0) || poisson.offScreenRight(offsetX, 0)) {
                poissonIterator.remove();
                vies--;
                if (vies <= 0) {
                    gameOver();
                }
            }
        }

        tempsDernierPoissonNorm += dt;

        if (tempsDernierPoissonNorm >= 3) {
            // A toute les 3 secondes on ajoute un nouveau poisson
            tempsDernierPoissonNorm = 0;
            poissons.add(new PoissonNormal(niveau));
        }

        if (niveau >= 2) {
            tempsDernierPoissonSpec += dt;
            if (tempsDernierPoissonSpec >= 5) {
                // A toute les 5 secondes on ajoute un nouveau poisson spécial
                tempsDernierPoissonSpec = 0;
                Poisson nouvPoisson;
                if (Math.random() > 0.5) {
                    nouvPoisson = new EtoileDeMer(niveau);
                } else {
                    nouvPoisson = new Crabe(niveau);
                }
                poissons.add(nouvPoisson);
            }
        }
    }

    /**
     * Affiche le score à l'écran
     *
     * @param context Contexte graphique sur lequel dessiner le score
     */
    private void afficherScore(GraphicsContext context) {
        context.setFill(Color.WHITE);
        context.setFont(Font.font(20));
        context.setTextAlign(TextAlignment.CENTER);
        context.setTextBaseline(VPos.TOP);

        context.fillText(String.valueOf(score), FishHunt.WIDTH / 2, 30);
    }

    /**
     * Affiche le niveau à l'écran
     *
     * @param context Contexte graphique sur lequel dessiner le niveau
     */
    private void afficherNiveau(GraphicsContext context) {
        context.setFill(Color.WHITE);
        context.setFont(Font.font(40));
        context.setTextAlign(TextAlignment.CENTER);
        context.setTextBaseline(VPos.CENTER);

        context.fillText("Level " + niveau, FishHunt.WIDTH / 2, FishHunt.HEIGHT / 2);
    }

    /**
     * Affiche En gros GAME OVER à l'écran
     *
     * @param context Contexte graphique sur lequel dessiner game over
     */
    private void afficherGameOver(GraphicsContext context) {
        context.setFill(Color.RED);
        context.setFont(Font.font(60));
        context.setTextAlign(TextAlignment.CENTER);
        context.setTextBaseline(VPos.CENTER);

        context.fillText("GAME OVER", FishHunt.WIDTH / 2, FishHunt.HEIGHT / 2);
    }

    /**
     * Affiche toutes les entités du jeu à l'écran
     *
     * @param context Contexte graphique sur lequel dessiner les entitéss
     */
    public void draw(GraphicsContext context) {
        // On affiche le background bleu
        context.setFill(Color.rgb(0, 0, 139));
        context.fillRect(0, 0, FishHunt.WIDTH, FishHunt.HEIGHT);

        for (Bulle bulle : bulles) {
            bulle.draw(context, offsetX, offsetY);
        }

        if (timerShowLevel > 0) {
            afficherNiveau(context);
        }

        for (Poisson poisson : poissons) {
            poisson.draw(context, offsetX, offsetY);
        }

        for (Balle balle : balles) {
            balle.draw(context, offsetX, offsetY);
        }

        afficherScore(context);
        cible.draw(context, offsetX, offsetY);
        poissonsVies.draw(context, offsetX, offsetY);

        if (partieTermine) {
            afficherGameOver(context);
        }
    }

    /**
     * Retourne si la partie et terminé et qu'on à affiché l'écran game over
     *
     * @return Vrai si la partie est terminé
     */
    public boolean gameEnded() {
        return partieTermine && gameOverTimer < 0;
    }


    /**
     * Renvois les high scores sous le format # - nom - score
     *
     * @return Les scores formattés en string
     */
    public ArrayList<String> getScores() {
        ArrayList<String> scoresFormatte = new ArrayList<>();
        ArrayList<Pair<Integer, String>> highScores = hsManager.getScores();

        for (int i = 0; i < highScores.size(); i++) {
            scoresFormatte.add("#" + (i + 1) + " - " + highScores.get(i).getValue() + " - " + highScores.get(i).getKey());
        }

        return scoresFormatte;
    }

    /**
     * Retourne le dernier score si c'est un highscore (top 10)
     * ou -1 si ce ne l'est pas
     *
     * @return le high score ou -1 si c'est n'est pas un highscore
     */
    public int getNewHighScore() {
        if (!gameEnded() || !hsManager.isNewHighScore(score)) {
            return -1;
        }
        return score;
    }

    /**
     * Enregistre le dernier score
     *
     * @param nom Nom du joueur qui à fait le dernier score
     */
    public void saveScore(String nom) {
        hsManager.saveScore(nom, score);
        partieTermine = false;
    }
}
