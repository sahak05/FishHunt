/* Tp3 FishHunt
 * Atheurs :
 * SADIKOU Abdoul (p1232312)
 * Émile Larose-Levac (p1242331)
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FishHunt extends Application {
    public static final double WIDTH = 640;
    public static final double HEIGHT = 480;

    private Stage stage;
    private Controleur controleur;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Lance le jeu
     *
     * @param stage Stage sur lequel mettre le jeu
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        controleur = new Controleur();

        // On affiche le menu au lancement de l'app
        this.stage.setScene(creerSceneMenu());
        this.stage.setTitle("Fish Hunt");
        this.stage.setResizable(false);
        this.stage.show();
    }

    /**
     * Fait la scene du menu et la renvoi
     *
     * @return La scene de menu
     */
    Scene creerSceneMenu() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        Scene sceneMenu = new Scene(root, WIDTH, HEIGHT);

        Button btnJeu = new Button("Nouvelle Partie!");
        Button btnScores = new Button("Meilleurs scores");

        root.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 139), CornerRadii.EMPTY, Insets.EMPTY)));
        ImageView logo = new ImageView(new Image("images/logo.png"));
        logo.setFitWidth(400);
        logo.setPreserveRatio(true);

        btnJeu.setOnAction((e) -> {
            controleur.newGame();
            stage.setScene(creerSceneJeu());
        });

        btnScores.setOnAction((e) -> stage.setScene(creerSceneScores()));

        // On met une marge entre les elements
        VBox.setMargin(btnJeu, new Insets(0, 0, 10, 0));
        VBox.setMargin(logo, new Insets(0, 0, 50, 0));
        root.getChildren().addAll(logo, btnJeu, btnScores);

        return sceneMenu;
    }

    /**
     * Fait la scene du jeu et le retourne
     *
     * @return scene du jeu
     */
    Scene creerSceneJeu() {
        Pane root = new Pane();
        // On met le background bleu aussi pour pas avoir de flash blanc
        root.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 139), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene sceneJeu = new Scene(root, WIDTH, HEIGHT);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        GraphicsContext context = canvas.getGraphicsContext2D();

        // Pour debug on veut controller certains aspect du jeu en pesant sur des touches
        sceneJeu.setOnKeyPressed((value) -> {
            KeyCode keycode = value.getCode();
            if (keycode == KeyCode.H) {
                // Augmente le niveau si on pese sur H
                controleur.levelUp();
            } else if (keycode == KeyCode.J) {
                // Augmente le score si on pese sur J
                controleur.augmenterScore();
            } else if (keycode == KeyCode.K) {
                // Augmente les vies si on pese sur K
                controleur.ajouterVie();
            } else if (keycode == KeyCode.L) {
                // Termine la partie si on pese sur L
                controleur.gameOver();
            }
        });

        // On bouge la cible lorsque le curseur bouge
        root.setOnMouseMoved((mouseEvent -> controleur.bougerCible(mouseEvent.getX(), mouseEvent.getY())));

        // On envoit une balle et bouge la cible lorsque l'on clique
        root.setOnMousePressed(mouseEvent -> {
            controleur.bougerCible(mouseEvent.getX(), mouseEvent.getY());
            controleur.lancerBalle(mouseEvent.getX(), mouseEvent.getY());
        });

        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double deltaTime = (now - lastTime) * 1e-9;

                // A chaque tick on met à jour le jeu et on le dessine
                controleur.update(deltaTime);
                controleur.draw(context);

                // Si la partie est terminé on va à la scene des highScore
                if (controleur.gameEnded()) {
                    stage.setScene(creerSceneScores());
                    // On arrete le timer si la scene n'est pas visible
                    stop();
                }

                lastTime = now;
            }
        };

        timer.start();

        return sceneJeu;
    }

    /**
     * Fait la scene qui affiche les scores et la retourne
     *
     * @return scene des scores
     */
    Scene creerSceneScores() {
        VBox root = new VBox();

        root.setAlignment(Pos.CENTER);
        Scene scoresScene = new Scene(root, WIDTH, HEIGHT);

        Text titre = new Text("Meilleurs scores");
        titre.setFont(Font.font(30));
        // On met une mare pour que le titre soit pas collé au dessud de la fenetre
        VBox.setMargin(titre, new Insets(20, 0, 0, 0));

        Button btnMenu = new Button("Menu");
        btnMenu.setOnAction(actionEvent -> stage.setScene(creerSceneMenu()));

        ListView<String> scoresList = new ListView<>();
        // On limite à 80% de la fenetre la liste
        scoresList.setMaxWidth(FishHunt.WIDTH * 0.8);
        scoresList.getItems().setAll(controleur.getScores());

        // Pour avoir le background aux couleurs qui alternes sans aucun éléments
        if (scoresList.getItems().size() == 0) {
            scoresList.getItems().add(" ");
        }

        // On veut un petit espace entre chaque éléments
        root.setSpacing(10);

        root.getChildren().addAll(titre, scoresList);

        int newScore = controleur.getNewHighScore();

        if (newScore != -1) {
            HBox ligneScore = new HBox();
            ligneScore.setAlignment(Pos.CENTER);
            ligneScore.setSpacing(5);
            Text votreNom = new Text("Votre nom :");
            TextField nom = new TextField();
            Text nbPoints = new Text("a fait " + newScore + " points!");
            Button btnAjouter = new Button("Ajouter!");
            ligneScore.getChildren().addAll(votreNom, nom, nbPoints, btnAjouter);

            btnAjouter.setOnAction(actionEvent -> {
                controleur.saveScore(nom.getText());
                // On rafraichit la liste des scores
                scoresList.getItems().setAll(controleur.getScores());
                // On enleve la ligne qui sauve le score
                root.getChildren().remove(ligneScore);
            });

            root.getChildren().add(ligneScore);
        }

        // On ajoute un peut d'espace en bas du bout menu pour
        // pas qu'il soit collé au bas de l'écran
        VBox.setMargin(btnMenu, new Insets(0, 0, 20, 0));
        root.getChildren().add(btnMenu);

        return scoresScene;
    }
}
