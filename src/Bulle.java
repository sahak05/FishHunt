import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class Bulle extends Entity {
    /**
     * Crée une bulle en bas de l'écran en xBase +- 20px, avec une vitesse
     * de 350 à 450px/s vers le haut
     *
     * @param xBase x autour du quel placer la bulle
     */
    public Bulle(int xBase) {
        color = Color.rgb(0, 0, 255, 0.4);
        int rayon = ThreadLocalRandom.current().nextInt(10, 40 + 1);
        x = ThreadLocalRandom.current().nextInt(xBase - 20, xBase + 20 + 1);
        largeur = hauteur = 2 * rayon;
        vy = ThreadLocalRandom.current().nextInt(350, 450 + 1);
        y = -hauteur;
    }

    /**
     * Dessine la bulle
     *
     * @param context Contexte graphique sur lequel dessiner la bulle
     * @param offsetX Offset en x pour positionner la bulle
     * @param offsetY Offset en y pour positionner la bulle
     */
    @Override
    public void draw(GraphicsContext context, double offsetX, double offsetY) {
        context.setFill(color);
        context.fillOval(getXAffichage(offsetX) - largeur / 2, getYAffichage(offsetY) - hauteur / 2, largeur, hauteur);
    }
}