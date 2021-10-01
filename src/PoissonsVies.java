import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PoissonsVies extends Entity {
    private final Image poisson;
    private final int taillePoisson;
    private final int padding;
    private int vies;

    /**
     * Constructeur de la classe PoissonVies
     *
     * @param maxVies Nombre de vies maximales pour le jeu
     * @param y       Ou afficher les vies en y
     */
    public PoissonsVies(int maxVies, double y) {
        padding = 15;
        taillePoisson = 30;
        poisson = new Image("images/fish/00.png");
        vies = maxVies;
        largeur = taillePoisson * maxVies + padding * (maxVies - 1);
        hauteur = taillePoisson;
        x = (FishHunt.WIDTH - largeur) / 2;
        this.y = y;
    }

    public void setVies(int vies) {
        this.vies = vies;
    }

    /**
     * Affiche le nombre de vies restantes comme des poissons à l'écran
     *
     * @param context Contexte graphique sur lequel dessiner l'entité
     * @param offsetX Offset en x pour positionner la l'entité
     * @param offsetY Offset en y pour positionner la l'entité
     */
    @Override
    public void draw(GraphicsContext context, double offsetX, double offsetY) {
        for (int i = 0; i < vies; i++) {
            context.drawImage(poisson, getXAffichage(offsetX) + (taillePoisson + padding) * i, getYAffichage(offsetY)
                    , taillePoisson, taillePoisson);
        }
    }
}
