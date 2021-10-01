import javafx.scene.image.Image;

public class EtoileDeMer extends Poisson {
    private final double yCentre;
    private final double amplitude;
    private final double periode;
    private double tempsOscillation;

    /**
     * Constructeur de la classe EtoileDeMer
     *
     * @param niveau Niveau auquel le jeu est rendu
     */
    public EtoileDeMer(int niveau) {
        vx = vitesse(niveau);
        imgPoisson = new Image("images/star.png");
        setLargeurHauteur();
        setPointDepart();
        amplitude = 50;
        periode = 1;
        yCentre = y;
    }

    /**
     * Met a jour l'étoile
     *
     * @param dt Temps écoulé depuis le dernier update
     */
    @Override
    public void update(double dt) {
        super.update(dt);
        tempsOscillation += dt;
        y = amplitude * Math.sin((tempsOscillation * 2 * Math.PI) / periode) + yCentre;
    }
}
