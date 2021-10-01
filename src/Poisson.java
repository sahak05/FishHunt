import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Poisson extends Entity {
    protected Image imgPoisson;

    /**
     * Méthode pour dessiner le poisson
     *
     * @param context Contexte graphique sur lequel dessiner le poisson
     * @param offsetX Offset en x pour positionner le poisson
     * @param offsetY Offset en y pour positionner le poisson
     */
    @Override
    public void draw(GraphicsContext context, double offsetX, double offsetY) {
        context.drawImage(imgPoisson, getXAffichage(offsetX), getYAffichage(offsetY), largeur, hauteur);
    }

    protected double vitesse(int niveau) {
        return 100 * Math.pow(niveau, 1.0 / 3.0) + 200;
    }

    /**
     * Met le poisson aléatoirement à gauche ou à doite de l'écran, le place entre
     * 1/5 et 4/5 de la hauteur de l'écran (selon le haut du poisson), met sa vitesse
     * dans la bonne directiont inverse l'image pour faire face à la bonne direction.
     */
    protected void setPointDepart() {
        // On place le poisson entre 1/5 et 4/5 de la hauteur par rapport au haut du poisson
        y = ThreadLocalRandom.current().nextDouble(FishHunt.HEIGHT * (1.0 / 5.0) - hauteur,
                FishHunt.HEIGHT * (4.0 / 5.0) - hauteur + 1);

        if (Math.random() > 0.5) {
            // on place le poisson à gauche de l'écran
            x = -largeur;
            if (vx < 0) {
                vx *= -1;
            }
        } else {
            // On place le poisson à droite
            x = FishHunt.WIDTH;
            if (vx > 0) {
                vx *= -1;
            }
            // Inverse l'image
            imgPoisson = ImageHelpers.flop(imgPoisson);
        }
    }

    /**
     * Modifie la taille du poisson pour que la largeur soit entre 80 et 120px
     * Et calcul la hauteur pour préserver le ratio original de l'image
     */
    protected void setLargeurHauteur() {
        largeur = ThreadLocalRandom.current().nextInt(80, 120 + 1);
        hauteur = imgPoisson.getHeight() * (largeur / imgPoisson.getWidth());
    }

}
