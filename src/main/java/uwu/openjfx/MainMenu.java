package uwu.openjfx;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.particle.ParticleSystem;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.FXGLScrollPane;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import uwu.openjfx.components.PlayerComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.function.Supplier;

import static com.almasb.fxgl.core.math.FXGLMath.noise1D;
import static com.almasb.fxgl.dsl.FXGL.getSettings;
import static com.almasb.fxgl.dsl.FXGL.loopBGM;
import static com.almasb.fxgl.dsl.FXGLForKtKt.random;

public class MainMenu extends FXGLMenu {

    private ParticleSystem particleSystem = new ParticleSystem();

    private ObjectProperty<Color> titleColor = new SimpleObjectProperty<>();
    private double t = 0.0;

    private final Pane menuRoot = new Pane();
    private final Pane menuContentRoot = new Pane();

    private final MenuContent emptyMenuContent = new MenuContent();

    //private PressAnyKeyState pressAnyKeyState = new PressAnyKeyState();

    private final MenuBox menu;

    public MainMenu(@NotNull MenuType type) {
        super(type);

        // code to customize the view of your menu

        String title = (type == MenuType.MAIN_MENU)
                ? FXGL.getSettings().getTitle()
                : "Paused";

        getContentRoot().getChildren().addAll(
                createBackground(getAppWidth(), getAppHeight()),
                //replace with function to get title
                createTitleView(title),
                //replace with function to get version string
                createVersionView(FXGL.getSettings().getVersion()),
                menuRoot,
                menuContentRoot
        );

        menu = (type == MenuType.MAIN_MENU)
                ? createMenuBodyMainMenu()
                : createMenuBodyGameMenu();

        double menuX = getAppWidth() / 5.0 + 25;
        double menuY = getAppHeight() * 3.0 / 5.0 + menu.getLayoutHeight() / 2.0;

        menuRoot.setTranslateX(menuX);
        menuRoot.setTranslateY(menuY);

        menuContentRoot.setTranslateX(getAppWidth() * 3.0 / 5.0 - 100);
        menuContentRoot.setTranslateY(menuY);

        // particle smoke
        Texture t = FXGL.texture("particles/smoke.png", 128.0, 128.0).brighter().brighter();

        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setBlendMode(BlendMode.SRC_OVER);
        emitter.setSourceImage(t.getImage());
        emitter.setSize(150.0, 220.0);
        emitter.setNumParticles(10);
        emitter.setEmissionRate(0.01);
        emitter.setVelocityFunction(e -> new Point2D(random() * 2.5, -random() * random(80, 120)));
        emitter.setExpireFunction(e -> Duration.seconds(random(4, 7)));
        emitter.setScaleFunction(e -> new Point2D(0.15, 0.10));
        emitter.setSpawnPointFunction(e -> new Point2D(random(0.0, getAppWidth() - 200.0), 120.0));

        particleSystem.addParticleEmitter(emitter, 0.0, FXGL.getAppHeight());

        getContentRoot().getChildren().add(3, particleSystem.getPane());

        menuRoot.getChildren().addAll(menu);
        menuContentRoot.getChildren().add(emptyMenuContent);
    }

    private final ArrayList<Animation<?>> animations = new ArrayList<>();

    @Override
    public void onCreate() {
        animations.clear();

        VBox menuBox = (VBox) menuRoot.getChildren().get(0);

        for (int i = 0; i < menuBox.getChildren().size(); i++) {
            Node node = menuBox.getChildren().get(i);
            node.setTranslateX(-250.0);

            Animation<?> animation = FXGL.animationBuilder()
                    .delay(Duration.seconds(i * 0.07))
                    .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                    .duration(Duration.seconds(0.66))
                    .translate(node)
                    .from(new Point2D(-250.0, 0.0))
                    .to(new Point2D(0.0, 0.0))
                    .build();

            animations.add(animation);

            animation.stop();
            animation.start();
        }
    }

    @Override
    public void onDestroy() {
        // the scene is no longer active so reset everything
        // so that next time scene is active everything is loaded properly
        switchMenuTo(menu);
        switchMenuContentTo(emptyMenuContent);
    }

    @Override
    public void onUpdate(double tpf) {
        for (Animation<?> animation : animations) {
            animation.onUpdate(tpf);
        }

        double frequency = 1.7;

        t += tpf * frequency;

        particleSystem.onUpdate(tpf);

        Color color = Color.color(1.0, 1.0, 1.0, noise1D(t));
        titleColor.set(color);
    }

    private Texture createBackground(double w, double h) {
        Texture pausebg = FXGL.texture("background/background.jpg", w, h);
        if (getType() != MenuType.MAIN_MENU) {
            pausebg.setOpacity(0.25);
        }
        return pausebg;
    }

    private StackPane createTitleView(String title) {
        titleColor = new SimpleObjectProperty<>(Color.WHITE);

        Text text = FXGL.getUIFactoryService().newText(title.substring(0, 1), 100.0);
        text.strokeProperty().bind(titleColor);
        text.setStyle("-fx-fill: transparent;-fx-stroke-width: 1.5");

        Text text2 = FXGL.getUIFactoryService().newText(title.substring(1), 100.0);
        text2.setFill(null);
        text2.setStroke(titleColor.getValue());
        text2.setStrokeWidth(2.5);

        double textWidth = text.getLayoutBounds().getWidth() + text2.getLayoutBounds().getWidth();

        Rectangle border = new Rectangle(textWidth + 30, 65.0, null);
        border.setStyle("-fx-stroke: white;"
                + "-fx-stroke-width: 4.0;-fx-arc-width: 25.0;-fx-arc-height: 25.0");

        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter(50);

        Texture t = FXGL.texture("particles/trace_horizontal.png", 64.0, 64.0);

        emitter.setBlendMode(BlendMode.ADD);
        emitter.setSourceImage(t.getImage());
        emitter.setMaxEmissions(Integer.MAX_VALUE);
        emitter.setSize(18.0, 22.0);
        emitter.setNumParticles(2);
        emitter.setEmissionRate(0.2);
        emitter.setVelocityFunction(e -> {
            if (e % 2 == 0) {
                return new Point2D(random(-10, 0), random(0, 0));
            } else {
                return new Point2D(random(0, 10), random(0, 0));
            }
        });
        emitter.setExpireFunction(e -> Duration.seconds(random(4, 6)));
        emitter.setScaleFunction(e -> new Point2D(-0.03, -0.03));
        emitter.setSpawnPointFunction(e -> new Point2D(random(0, 0), random(0, 0)));
        emitter.setAccelerationFunction(() -> new Point2D(random(-1, 1), random(0, 0)));

        HBox box = new HBox(text, text2);
        box.setAlignment(Pos.CENTER);

        StackPane titleRoot = new StackPane();
        titleRoot.getChildren().addAll(border, box);

        titleRoot.setTranslateX(getAppWidth() / 2.0 - (textWidth + 30) / 2);
        titleRoot.setTranslateY(100);

        particleSystem = new ParticleSystem();

        if (!FXGL.getSettings().isExperimentalNative()) {
            particleSystem.addParticleEmitter(emitter, getAppWidth() / 2.0 - 30,
                    titleRoot.getTranslateY() + border.getHeight());
        }

        return titleRoot;
    }

    private Text createVersionView(String version) {
        Text view = FXGL.getUIFactoryService().newText(version);
        view.setTranslateY(getAppHeight() - 2);
        return view;
    }

    private void switchMenuTo(Node menu) {
        Node oldMenu = menuRoot.getChildren().get(0);

        FadeTransition ft = new FadeTransition(Duration.seconds(0.33), oldMenu);
        ft.setToValue(0.0);
        ft.setOnFinished(event -> {
            menu.setOpacity(0.0);
            menuRoot.getChildren().set(0, menu);
            oldMenu.setOpacity(1.0);

            FadeTransition ft2 = new FadeTransition(Duration.seconds(0.33), menu);
            ft2.setToValue(1.0);
            ft2.play();
        });
        ft.play();
    }

    private void switchMenuContentTo(Node content) {
        menuContentRoot.getChildren().set(0, content);
    }

    private MenuBox createMenuBodyMainMenu() {
        MenuBox box = new MenuBox();

        //val enabledItems = FXGL.getSettings().enabledMenuItems;

        MenuButton itemNewGame = new MenuButton("New Game");
        itemNewGame.setChild(createNewGameMenu());
        box.add(itemNewGame);

        MenuButton itemOptions = new MenuButton("Options");
        itemOptions.setChild(createOptionsMenu());
        box.add(itemOptions);

        MenuButton itemExtra = new MenuButton("Extra");
        itemExtra.setChild(createExtraMenu());
        box.add(itemExtra);

        MenuButton itemExit = new MenuButton("Exit");
        itemExit.setOnAction(event -> fireExit());
        box.add(itemExit);

        return box;
    }

    private MenuBox createMenuBodyGameMenu() {
        MenuBox box = new MenuBox();

        EnumSet<MenuItem> enabledItems = FXGL.getSettings().getEnabledMenuItems();

        MenuButton itemResume = new MenuButton("Resume");
        itemResume.setOnAction(event -> fireResume());
        box.add(itemResume);

        MenuButton itemOptions = new MenuButton("Options");
        itemOptions.setChild(createOptionsMenu());
        box.add(itemOptions);

        MenuButton itemMain = new MenuButton("Main Menu");
        itemMain.setOnAction(event -> {
            String text = FXGL.localize("menu.exitMainMenu") + "\n"
                    + FXGL.localize("menu.unsavedProgress");

            FXGL.getDialogService().showConfirmationBox(text, yes -> {
                if (yes) {
                    resetToMainMenu();
                }
            });
        });
        box.add(itemMain);

        MenuButton itemExit = new MenuButton("Exit");
        itemExit.setOnAction(event -> fireExit());
        box.add(itemExit);

        /*
        if (enabledItems.contains(MenuItem.SAVE_LOAD)) {
            val itemSave = MenuButton("menu.save")
            itemSave.setOnAction { fireSave() }

            val itemLoad = MenuButton("menu.load")
            itemLoad.setMenuContent({ createContentLoad() }, isCached = false)

            box.add(itemSave)
            box.add(itemLoad)
        }
         */

        return box;
    }

    public static void resetToMainMenu() {
        FXGL.getGameController().gotoMainMenu();
        FXGL.getAudioPlayer().stopAllMusic();
        loopBGM("MainMenu.mp3");
        PlayerComponent.setPlayerName(null);
        PlayerComponent.setPlayerWeapon(null);
        PlayerComponent.setGameDifficulty(null);
    }

    private MenuBox createOptionsMenu() {
        MenuButton itemAudio = new MenuButton("Volume");
        itemAudio.setMenuContent(this::createContentAudio, false);

        MenuButton btnRestore = new MenuButton("Reset settings");
        String text = FXGL.localize("menu.settingsRestore");
        btnRestore.setOnAction(e -> FXGL.getDialogService().showConfirmationBox(text, yes -> {
            if (yes) {
                switchMenuContentTo(emptyMenuContent);
                restoreDefaultSettings();
                getSettings().setGlobalMusicVolume(0.25);
            }
        }));

        return new MenuBox(itemAudio, btnRestore);
    }

    private MenuBox createExtraMenu() {
        MenuButton itemWho = new MenuButton("Who Are We?");
        itemWho.setMenuContent(this::createContentWho, false);

        MenuButton itemCredits = new MenuButton("Other Credits");
        itemCredits.setMenuContent(this::createContentCredits, false);

        return new MenuBox(itemWho, itemCredits);
    }

    private MenuContent createContentWho() {

        FXGLScrollPane pane = new FXGLScrollPane();
        pane.setPrefWidth(500.0);
        pane.setPrefHeight(getAppHeight() / 2.0);
        pane.setStyle("-fx-background:black;");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPrefWidth(pane.getPrefWidth() - 15);

        ArrayList<String> credits = new ArrayList<>(Arrays.asList(
                "Alice Wang",
                "",
                "Devan Moses",
                "",
                "James Johnson",
                "",
                "Jason Ng",
                "",
                "Ray Hung"
        ));

        for (String credit : credits) {
            vbox.getChildren().add(FXGL.getUIFactoryService().newText(credit));
        }

        pane.setContent(vbox);

        return new MenuContent(pane);
    }

    private MenuContent createContentCredits() {

        FXGLScrollPane pane = new FXGLScrollPane();
        pane.setPrefWidth(500.0);
        pane.setPrefHeight(getAppHeight() / 2.0);
        pane.setStyle("-fx-background:black;");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPrefWidth(pane.getPrefWidth() - 15);

        ArrayList<String> credits = new ArrayList<>(Arrays.asList(
                "Asset by 0x72, aekae13 from itch.io",
                "",
                "0x72.itch.io/dungeontileset-ii",
                "",
                "aekae13.itch.io/16x16-dungeon-walls-reconfig",
                "",
                "",
                "Powered by FXGL " + FXGL.getVersion(),
                "",
                "Author: Almas Baimagambetov",
                "",
                "https://github.com/AlmasB/FXGL"
        ));

        for (String credit : credits) {
            vbox.getChildren().add(FXGL.getUIFactoryService().newText(credit));
        }

        pane.setContent(vbox);

        return new MenuContent(pane);
    }

    private MenuBox createNewGameMenu() {
        MenuButton itemName = new MenuButton("Name");
        itemName.setMenuContent(this::createContentName, false);

        MenuButton itemDifficulty = new MenuButton("Difficulty");
        itemDifficulty.setMenuContent(this::createContentDifficulty, false);

        MenuButton itemWeapon = new MenuButton("Weapon");
        itemWeapon.setMenuContent(this::createContentWeapon, false);

        MenuButton itemLetsGo = new MenuButton("Let's Go!");
        itemLetsGo.setOnAction(e -> {
            if (PlayerComponent.getPlayerName() != null
                    && !PlayerComponent.getPlayerName().isEmpty()
                    && !PlayerComponent.getPlayerName().trim().isEmpty()
                    && PlayerComponent.getPlayerWeapon() != null
                    && PlayerComponent.getGameDifficulty() != null) {
                FXGL.getDialogService().showConfirmationBox("Are you sure?", yes -> {
                    if (yes) {
                        fireNewGame();
                    }
                });
            } else {
                FXGL.getDialogService().showMessageBox("You must enter your "
                        + "name and choose your weapon and difficulty");
            }
        });

        return new MenuBox(itemName, itemDifficulty, itemWeapon, itemLetsGo);
    }

    protected MenuContent createContentName() {
        MenuButton itemName = new MenuButton("Click me!");
        itemName.setOnAction(e -> FXGL.getDialogService().showInputBoxWithCancel(
                "What is your name?",
            t -> {
                return !(t == null || t.isEmpty()
                        || t.trim().isEmpty());
            },
            s -> {
                PlayerComponent.setPlayerName(s);
                itemName.updateText(PlayerComponent.getPlayerName());
            }));

        HBox hboxName = new HBox(15.0, itemName);

        hboxName.setAlignment(Pos.CENTER_RIGHT);

        return new MenuContent(hboxName);
    }

    protected MenuContent createContentDifficulty() {
        MenuButton itemEasy = new MenuButton("Easy");
        itemEasy.setOnAction(
            e -> {
                PlayerComponent.setGameDifficulty(itemEasy.getText());
                PlayerComponent.setGold(1000);
                itemEasy.updateText(itemEasy.getText());
            });

        MenuButton itemMedium = new MenuButton("Medium");
        itemMedium.setOnAction(
            e -> {
                PlayerComponent.setGameDifficulty(itemMedium.getText());
                PlayerComponent.setGold(800);
                itemMedium.updateText(itemMedium.getText());
            });

        MenuButton itemHard = new MenuButton("Hard");
        itemHard.setOnAction(
            e -> {
                PlayerComponent.setGameDifficulty(itemHard.getText());
                PlayerComponent.setGold(600);
                itemHard.updateText(itemHard.getText());
            });

        return new MenuContent(itemEasy, itemMedium, itemHard);
    }

    protected MenuContent createContentWeapon() {
        MenuButton itemSword = new MenuButton("Sword");
        itemSword.setOnAction(
            e -> {
                PlayerComponent.setPlayerWeapon(itemSword.getText());
                itemSword.updateText(itemSword.getText());

            });

        MenuButton itemWand = new MenuButton("Wand");
        itemWand.setOnAction(
            e -> {
                PlayerComponent.setPlayerWeapon(itemWand.getText());
                itemWand.updateText(itemWand.getText());
            });

        MenuButton itemBow = new MenuButton("Bow");
        itemBow.setOnAction(
            e -> {
                PlayerComponent.setPlayerWeapon(itemBow.getText());
                itemBow.updateText(itemBow.getText());
            });

        return new MenuContent(itemSword, itemWand, itemBow);
    }

    /**
     * @return menu content containing music and sound volume sliders
     */
    protected MenuContent createContentAudio() {
        Slider sliderMusic = new Slider(0.0, 1.0, 1.0);
        sliderMusic.valueProperty().bindBidirectional(FXGL.getSettings()
                .globalMusicVolumeProperty());

        Text textMusic = FXGL.getUIFactoryService().newText(
                FXGL.localizedStringProperty("menu.music.volume").concat(": "));

        Text percentMusic = FXGL.getUIFactoryService().newText("");

        percentMusic.textProperty().bind(sliderMusic.valueProperty()
                .multiply(100).asString("%.0f"));

        Slider sliderSound = new Slider(0.0, 1.0, 1.0);

        sliderSound.valueProperty().bindBidirectional(FXGL.getSettings()
                .globalSoundVolumeProperty());

        Text textSound = FXGL.getUIFactoryService().newText(
                FXGL.localizedStringProperty("menu.sound.volume").concat(": "));

        Text percentSound = FXGL.getUIFactoryService().newText("");

        percentSound.textProperty().bind(sliderSound.valueProperty()
                .multiply(100).asString("%.0f"));

        HBox hboxMusic = new HBox(15.0, textMusic, sliderMusic, percentMusic);
        HBox hboxSound = new HBox(15.0, textSound, sliderSound, percentSound);

        hboxMusic.setAlignment(Pos.CENTER_RIGHT);
        hboxSound.setAlignment(Pos.CENTER_RIGHT);

        return new MenuContent(hboxMusic, hboxSound);
    }

    private class MenuBox extends VBox {

        private final double layoutHeight = 6.0 * getChildren().size();

        public double getLayoutHeight() {
            return layoutHeight;
        }

        public MenuBox(MenuButton... items) {
            for (MenuButton item : items) {
                getChildren().add(item);
            }
        }

        public void add(MenuButton item) {
            item.setParent(this);
            getChildren().addAll(item);
        }
    }

    private class MenuContent extends VBox {

        private Runnable onOpen;
        private Runnable onClose;

        private int maxW;

        public MenuContent(Node... items) {
            if (Arrays.stream(items).toArray().length > 0) {
                maxW = (int) (items[0].getLayoutBounds().getWidth());

                for (Node n : items) {
                    int w = (int) (n.getLayoutBounds().getWidth());
                    if (w > maxW) {
                        maxW = w;
                    }
                }

                for (Node item : items) {
                    getChildren().addAll(item);
                }
            }

            sceneProperty().addListener((a, b, newScene) -> {
                if (newScene != null) {
                    onOpen();
                } else {
                    onClose();
                }
            });
        }

        /**
         * Set on open handler.
         *
         * @param onOpenAction method to be called when content opens
         */
        private void setOnOpen(Runnable onOpenAction) {
            this.onOpen = onOpenAction;
        }

        /**
         * Set on close handler.
         *
         * @param onCloseAction method to be called when content closes
         */
        private void setOnClose(Runnable onCloseAction) {
            this.onClose = onCloseAction;
        }

        private void onOpen() {
            if (onOpen != null) {
                onOpen.run();
            }
        }

        private void onClose() {
            if (onClose != null) {
                onClose.run();
            }
        }
    }

    private class MenuButton extends Pane {
        private MenuBox parent;
        private MenuContent cachedContent;
        private String text;

        private final Button btn;

        private boolean isAnimating = false;

        public MenuButton(String stringKey) {
            text = stringKey;
            btn = FXGL.getUIFactoryService().newButton(text);
            btn.setStyle("-fx-background-color: transparent;");
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setOnMouseEntered(event -> {
                FXGL.play("ui_hover.wav");
            });
            btn.setOnMouseClicked(event -> {
                FXGL.play("ui_confirm.wav");
            });

            Polygon p = new Polygon(0.0, 0.0, 220.0, 0.0, 250.0, 35.0, 0.0, 35.0);
            p.setMouseTransparent(true);

            LinearGradient g = new LinearGradient(0.0, 1.0, 1.0, 0.2, true, CycleMethod.NO_CYCLE,
                    new Stop(0.6, Color.color(1.0, 0.8, 0.0, 0.34)),
                    new Stop(0.85, Color.color(1.0, 0.8, 0.0, 0.74)),
                    new Stop(1.0, Color.WHITE));

            p.fillProperty().bind(
                    Bindings.when(btn.pressedProperty())
                            .then((Paint) Color.color(1.0, 0.8, 0.0, 0.75))
                            .otherwise(g)
            );

            p.setStroke(Color.color(0.1, 0.1, 0.1, 0.15));
            p.setEffect(new GaussianBlur());

            p.visibleProperty().bind(btn.hoverProperty());

            getChildren().addAll(btn, p);

            btn.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                if (isFocused) {
                    boolean isOK = !isAnimating;
                    // original in kt but idk what it means in java
                    //boolean isOK = animations.none(it.isAnimating) && !isAnimating;
                    for (Animation<?> animation : animations) {
                        if (animation.isAnimating()) {
                            isOK = false;
                            break;
                        }
                    }
                    if (isOK) {
                        isAnimating = true;

                        FXGL.animationBuilder()
                                .onFinished(() -> isAnimating = false)
                                .bobbleDown(this)
                                .buildAndPlay(MainMenu.this);
                    }
                }
            });
        }

        public void updateText(String newText) {
            text = newText;
            btn.setText(text);
        }
        public String getText() {
            return text;
        }

        public void setOnAction(EventHandler<ActionEvent> e) {
            btn.setOnAction(e);
        }

        public void setParent(MenuBox menu) {
            parent = menu;
        }

        public void setMenuContent(Supplier<MenuContent> contentSupplier, Boolean isCached) {

            btn.addEventHandler(ActionEvent.ACTION, e -> {
                if (cachedContent == null || !isCached) {
                    cachedContent = contentSupplier.get();
                }

                switchMenuContentTo(cachedContent);
            });
        }

        public void setChild(MenuBox menu) {
            MenuButton back = new MenuButton("back");
            menu.getChildren().add(back);


            back.addEventHandler(ActionEvent.ACTION, e -> {
                switchMenuTo(parent);
                switchMenuContentTo(emptyMenuContent);
            });

            btn.addEventHandler(ActionEvent.ACTION, e -> switchMenuTo(menu));
        }
    }
}
