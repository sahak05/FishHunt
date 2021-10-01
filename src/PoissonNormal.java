import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class PoissonNormal extends Poisson {
    /**
     * Constructeut de la classe PoissonNormal
     *
     * @param niveau Niveau auquel le jeu est rendu
     */
    public PoissonNormal(int niveau) {
        ay = -100;
        vx = vitesse(niveau);

        vy = ThreadLocalRandom.current().nextInt(100, 200 + 1);

        // On genere une couleur aléatoire
        color = Color.rgb(ThreadLocalRandom.current().nextInt(0, 255 + 1),
                ThreadLocalRandom.current().nextInt(0, 255 + 1),
                ThreadLocalRandom.current().nextInt(0, 255 + 1));

        // On prends une image aléatoire pour le poisson et on la colore
        int indexImage = ThreadLocalRandom.current().nextInt(0, 7 + 1);
        imgPoisson = ImageHelpers.colorize(new Image("images/fish/0" + indexImage + ".png"), color);

        setLargeurHauteur();
        setPointDepart();
    }
}
