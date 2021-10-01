import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Cible extends Entity {
    private final Image cible;

    /**
     * Constructeur de la classe cible
     *
     * @param x Position en x du coins bas gauche de la cible
     * @param y Position en y du coins bas gauche de la cible
     */
    public Cible(double x, double y) {
        this.x = x;
        this.y = y;
        cible = new Image("images/cible.png");
        largeur = 50;
        hauteur = 50;
    }

    /**
     * Position la cible avec son centre en x,y
     *
     * @param x Position en x du centre de la cible
     * @param y Position en y du centre de la cible
     */
    public void setCenterXY(double x, double y) {
        this.x = x - largeur / 2;
        this.y = y - hauteur / 2;
    }

    /**
     * Affiche la cible sur le contexte grpahique
     *
     * @param context Contexte graphique sur lequel dessiner l'entité
     * @param offsetX Offset en x pour positionner la l'entité
     * @param offsetY Offset en y pour positionner la l'entité
     */
    @Override
    public void draw(GraphicsContext context, double offsetX, double offsetY) {
        context.drawImage(cible, getXAffichage(offsetX), getYAffichage(offsetY), largeur, hauteur);
    }
}
