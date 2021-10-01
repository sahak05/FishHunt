import javafx.scene.image.Image;

public class Crabe extends Poisson {
    private final boolean droite;
    private double tempsChangementDirection;

    /**
     * Constructeur de la classe Crabe
     *
     * @param niveau Niveau auquel le jeu est rendu
     */
    public Crabe(int niveau) {
        vx = 1.3 * vitesse(niveau);
        imgPoisson = new Image("images/crabe.png");
        setLargeurHauteur();
        setPointDepart();
        droite = vx > 0;
    }

    /**
     * Met à jour le crabe
     *
     * @param dt Temps écoulé depuis le dernier update
     */
    @Override
    public void update(double dt) {
        super.update(dt);
        tempsChangementDirection += dt;

        // On avance pour 0.5s
        if (tempsChangementDirection > 0.5) {
            if ((droite && vx > 0) || (!droite && vx < 0)) {
                tempsChangementDirection = 0;
                vx *= -1;
            }
        } else if (tempsChangementDirection > 0.25) {
            // On recule pour 0.25s
            if ((droite && vx < 0) || (!droite && vx > 0)) {
                tempsChangementDirection = 0;
                vx *= -1;
            }
        }
    }
}
