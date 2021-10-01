import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Entity {
    protected double largeur;
    protected double hauteur;

    protected double x;
    protected double y;

    // Vitesse en x et y
    protected double vx;
    protected double vy;

    // Accélération en x et y
    protected double ax;
    protected double ay;

    protected Color color;

    /**
     * @param dt Temps écoulé depuis le dernier update
     */
    public void update(double dt) {
        // Met à jour la position et la vitesse de l'entité
        vx += dt * ax;
        vy += dt * ay;
        x += dt * vx;
        y += dt * vy;
    }

    /**
     * Méthode abstraite pour dessiner l'entité
     *
     * @param context Contexte graphique sur lequel dessiner l'entité
     * @param offsetX Offset en x pour positionner la l'entité
     * @param offsetY Offset en y pour positionner la l'entité
     */
    public abstract void draw(GraphicsContext context, double offsetX, double offsetY);

    /**
     * Retourne vrai si l'entité est en haut de l'écran + une marge
     *
     * @param offsetY Offset en y pour positionner la l'entité
     * @param margin  Marge à rajouter à la vérification
     * @return Vrai si l'entité est sortie de l'écran
     */
    public boolean offScreenTop(double offsetY, double margin) {
        return -y - offsetY + hauteur < margin;
    }

    /**
     * Retourne la coordonnée en x dans l'écran
     *
     * @param offsetX Offset en x pour positionner la l'entité
     * @return Le x dans l'écran pour afficher
     */
    public double getXAffichage(double offsetX) {
        return x - offsetX;
    }

    /**
     * Retourne la coordonnée en y dans l'écran
     *
     * @param offsetY Offset en y pour positionner la l'entité
     * @return La valeur de y dans l'écran de jeu
     */
    public double getYAffichage(double offsetY) {
        return -y - offsetY - hauteur;
    }

    /**
     * Retourne vrai si l'entité à gauche de l'écran + une marge
     *
     * @param offsetX Offset en x pour positionner la l'entité
     * @param margin  Marge à rajouter à la vérification
     * @return Vrai si l'entité est sortie de l'écran
     */
    public boolean offScreenLeft(double offsetX, double margin) {
        return x - offsetX + largeur < -margin;
    }

    /**
     * Retourne vrai si l'entité est droite de l'écran + une marge
     *
     * @param offsetX Offset en x pour positionner la l'entité
     * @param margin  Marge à rajouter à la vérification
     * @return Vrai si l'entité est sortie de l'écran
     */
    public boolean offScreenRight(double offsetX, double margin) {
        return x - offsetX > FishHunt.WIDTH + margin;
    }
}
