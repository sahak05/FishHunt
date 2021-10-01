import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Balle extends Entity {
    private final double vitesseRayon;
    private double rayon;

    /**
     * Constructeur de la classe balle
     *
     * @param x Position du centre de la balle en x
     * @param y Position du centre de la balle en y
     */
    public Balle(double x, double y) {
        color = Color.BLACK;
        rayon = 50;
        this.y = y;
        this.x = x;
        largeur = 2 * rayon;
        hauteur = largeur;
        vitesseRayon = -300;
    }

    /**
     * Affiche la balle sur le contexte graphique
     *
     * @param context Contexte graphique sur lequel dessiner l'entité
     * @param offsetX Offset en x pour positionner la l'entité
     * @param offsetY Offset en y pour positionner la l'entité
     */
    @Override
    public void draw(GraphicsContext context, double offsetX, double offsetY) {
        context.setFill(color);
        context.fillOval(getXAffichage(offsetX) - rayon, getYAffichage(offsetY) - rayon, largeur, hauteur);
    }

    /**
     * Met a jour le rayon, la hauteur et la largeur en plus des coordonnées
     *
     * @param dt Temps écoulé depuis le dernier update
     */
    @Override
    public void update(double dt) {
        super.update(dt);
        rayon += vitesseRayon * dt;
        largeur = 2 * rayon;
        hauteur = largeur;
    }

    /**
     * Donne le y d'affichage en prenant en compte qu'il est au centre de la balle
     *
     * @param offsetY Offset en y pour positionner la l'entité
     * @return Le y pour afficher la balle
     */
    @Override
    public double getYAffichage(double offsetY) {
        return -y - offsetY;
    }

    public double getRayon() {
        return rayon;
    }

    /**
     * Vérifie si le centre de la balle touche à un poisson
     *
     * @param poisson Le poisson avec le lequel on vérifie la collision
     * @return Vrai si le centre de la cible est sur le poisson
     */
    public boolean touches(Poisson poisson) {
        return poisson.x <= x && poisson.x + poisson.largeur >= x && poisson.y <= y && poisson.y + poisson.hauteur >= y;
    }
}
