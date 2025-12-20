package com.chromatic.alchemist.view;

import com.chromatic.alchemist.controller.GameController;
import com.chromatic.alchemist.logging.GameLogger;
import com.chromatic.alchemist.model.GameModel;
import com.chromatic.alchemist.model.Level;
import com.chromatic.alchemist.model.Recipe;
import com.chromatic.alchemist.model.composite.*;
import com.chromatic.alchemist.model.decorator.AbilityDecorator;
import com.chromatic.alchemist.model.entity.Player;
import com.chromatic.alchemist.model.state.GameState;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

/**
 * Game View
 * 
 * Manages all visual rendering and UI scenes.
 * Handles:
 * - Menu scene
 * - Game scene (with canvas rendering)
 * - Pause overlay
 * - Options scene
 * - Game over scene
 * - Victory scene
 */

public class GameView {
    
    private final Stage stage;
    private final GameController controller;
    private final double width;
    private final double height;
    
    // Scenes
    private Scene menuScene;
    private Scene gameScene;
    private Scene optionsScene;
    private Scene gameOverScene;
    private Scene victoryScene;
    
    // Game scene components
    private Canvas gameCanvas;
    private GraphicsContext gc;
    private StackPane gameRoot;
    private VBox pauseOverlay;
    private HBox hudTop;
    private HBox hudBottom;
    
    // Fonts
    private Font titleFont;
    private Font buttonFont;
    private Font hudFont;
    private Font smallFont;
    
    // Settings
    private int difficulty = 1;
    
    public GameView(Stage stage, GameController controller, double width, double height) {
        this.stage = stage;
        this.controller = controller;
        this.width = width;
        this.height = height;
        
        // Initialize fonts
        titleFont = Font.font("Arial", FontWeight.BOLD, 48);
        buttonFont = Font.font("Arial", FontWeight.BOLD, 20);
        hudFont = Font.font("Arial", FontWeight.BOLD, 16);
        smallFont = Font.font("Arial", FontWeight.NORMAL, 14);
        
        // Create all scenes
        createMenuScene();
        createGameScene();
        createOptionsScene();
        
        GameLogger.getInstance().logInfo("GameView initialized");
    }
    
    // ==================== SCENE CREATION ====================
    
    private void createMenuScene() {
        // Main container with animated background
        StackPane root = new StackPane();
        
        // Animated background with floating particles
        Pane particleLayer = createAnimatedBackground();
        
        // Content layer - reduced spacing for better fit
        VBox content = new VBox(12);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(15, 40, 15, 40));
        
        // Decorative top orbs representing the 4 elements
        HBox elementOrbs = createElementOrbs();
        
        // Title with glow animation - slightly smaller
        Label titleLabel = new Label("CHROMATIC");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 52));
        titleLabel.setTextFill(Color.WHITE);
        
        // Gradient text effect using multiple labels
        DropShadow titleShadow = new DropShadow();
        titleShadow.setColor(Color.GOLD);
        titleShadow.setRadius(20);
        titleShadow.setSpread(0.3);
        Glow titleGlow = new Glow(0.8);
        titleGlow.setInput(titleShadow);
        titleLabel.setEffect(titleGlow);
        
        // Animate title glow
        Timeline titleAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(titleGlow.levelProperty(), 0.4)),
            new KeyFrame(Duration.seconds(1.5), new KeyValue(titleGlow.levelProperty(), 1.0)),
            new KeyFrame(Duration.seconds(3), new KeyValue(titleGlow.levelProperty(), 0.4))
        );
        titleAnimation.setCycleCount(Animation.INDEFINITE);
        titleAnimation.play();
        
        Label titleLabel2 = new Label("ALCHEMIST");
        titleLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        titleLabel2.setTextFill(Color.GOLD);
        DropShadow shadow2 = new DropShadow();
        shadow2.setColor(Color.ORANGE);
        shadow2.setRadius(15);
        titleLabel2.setEffect(shadow2);
        
        // Subtitle with typewriter-like appearance
        Label subtitleLabel = new Label("✦ Master the Elements ✦ Complete the Recipes ✦");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        subtitleLabel.setTextFill(Color.LIGHTGRAY);
        
        // Fade in animation for subtitle
        FadeTransition subtitleFade = new FadeTransition(Duration.seconds(2), subtitleLabel);
        subtitleFade.setFromValue(0);
        subtitleFade.setToValue(1);
        subtitleFade.play();
        
        // Spacer
        Region spacer1 = new Region();
        spacer1.setPrefHeight(8);
        
        // Styled buttons with hover animations
        Button newGameBtn = createAnimatedMenuButton("⚔  NEW GAME", "#FF6B35", "#FF8C42");
        newGameBtn.setOnAction(e -> controller.newGame());
        
        Button howToPlayBtn = createAnimatedMenuButton("📖  HOW TO PLAY", "#9B59B6", "#8E44AD");
        howToPlayBtn.setOnAction(e -> showHowToPlayScene());
        
        Button optionsBtn = createAnimatedMenuButton("⚙  OPTIONS", "#4ECDC4", "#45B7AA");
        optionsBtn.setOnAction(e -> showOptionsScene());
        
        Button exitBtn = createAnimatedMenuButton("✖  EXIT", "#95190C", "#B91C1C");
        exitBtn.setOnAction(e -> controller.exitGame());
        
        // Controls panel with glass effect - smaller
        VBox controlsBox = createControlsPanel();
        
        // Version label
        Label versionLabel = new Label("v1.0.0");
        versionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        versionLabel.setTextFill(Color.gray(0.5));
        
        content.getChildren().addAll(
            elementOrbs,
            titleLabel, 
            titleLabel2,
            subtitleLabel, 
            spacer1,
            newGameBtn,
            howToPlayBtn,
            optionsBtn, 
            exitBtn, 
            controlsBox,
            versionLabel
        );
        
        root.getChildren().addAll(particleLayer, content);
        
        menuScene = new Scene(root, width, height);
    }
    
    //Creates animated floating particles background.
    private Pane createAnimatedBackground() {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: linear-gradient(to bottom, #0a0a1a, #1a1a3a, #0d0d2a);");
        pane.setPrefSize(width, height);
        
        Random random = new Random();
        Color[] colors = {
            Color.web("#FF6B35", 0.6),  // Fire
            Color.web("#4ECDC4", 0.6),  // Water
            Color.web("#7CB342", 0.6),  // Earth
            Color.web("#E0E0E0", 0.6)   // Air
        };
        
        // Create floating particles
        for (int i = 0; i < 30; i++) {
            Circle particle = new Circle(random.nextDouble() * 4 + 2);
            particle.setFill(colors[random.nextInt(colors.length)]);
            particle.setCenterX(random.nextDouble() * width);
            particle.setCenterY(random.nextDouble() * height);
            particle.setEffect(new Glow(0.8));
            
            // Floating animation
            TranslateTransition translate = new TranslateTransition(
                Duration.seconds(random.nextDouble() * 10 + 8), particle);
            translate.setByY(-height - 50);
            translate.setCycleCount(Animation.INDEFINITE);
            translate.setInterpolator(Interpolator.LINEAR);
            
            // Randomize start time
            translate.setDelay(Duration.seconds(random.nextDouble() * 5));
            
            // Fade animation
            FadeTransition fade = new FadeTransition(Duration.seconds(3), particle);
            fade.setFromValue(0.2);
            fade.setToValue(0.8);
            fade.setCycleCount(Animation.INDEFINITE);
            fade.setAutoReverse(true);
            
            // Scale pulse
            ScaleTransition scale = new ScaleTransition(Duration.seconds(2), particle);
            scale.setFromX(0.8);
            scale.setFromY(0.8);
            scale.setToX(1.2);
            scale.setToY(1.2);
            scale.setCycleCount(Animation.INDEFINITE);
            scale.setAutoReverse(true);
            
            pane.getChildren().add(particle);
            translate.play();
            fade.play();
            scale.play();
        }
        
        // Add decorative lines
        for (int i = 0; i < 5; i++) {
            Rectangle line = new Rectangle(1, height * 0.6);
            line.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.5, Color.web("#ffffff", 0.1)),
                new Stop(1, Color.TRANSPARENT)
            ));
            line.setX(width * 0.1 + (i * width * 0.2));
            line.setY(height * 0.2);
            pane.getChildren().add(line);
        }
        
        return pane;
    }
    
    /**
     * Creates the 4 element orbs display.
     */
    private HBox createElementOrbs() {
        HBox orbs = new HBox(20);
        orbs.setAlignment(Pos.CENTER);
        orbs.setPadding(new Insets(0, 0, 5, 0));
        
        String[][] elements = {
            {"🔥", "#FF6B35", "#FF8C42"},  // Fire
            {"💧", "#4ECDC4", "#45B7AA"},  // Water
            {"🌍", "#7CB342", "#8BC34A"},  // Earth
            {"💨", "#E0E0E0", "#FFFFFF"}   // Air
        };
        
        for (int i = 0; i < elements.length; i++) {
            StackPane orbContainer = new StackPane();
            
            // Outer glow circle - smaller
            Circle outerGlow = new Circle(28);
            outerGlow.setFill(Color.web(elements[i][1], 0.2));
            outerGlow.setEffect(new Glow(0.5));
            
            // Main orb - smaller
            Circle orb = new Circle(20);
            orb.setFill(new RadialGradient(
                0, 0, 0.3, 0.3, 0.8, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(elements[i][2])),
                new Stop(1, Color.web(elements[i][1]))
            ));
            
            DropShadow orbShadow = new DropShadow();
            orbShadow.setColor(Color.web(elements[i][1]));
            orbShadow.setRadius(15);
            orbShadow.setSpread(0.3);
            orb.setEffect(orbShadow);
            
            // Element emoji
            Label emoji = new Label(elements[i][0]);
            emoji.setFont(Font.font(20));
            
            orbContainer.getChildren().addAll(outerGlow, orb, emoji);
            
            // Floating animation for each orb
            TranslateTransition float1 = new TranslateTransition(Duration.seconds(2 + i * 0.3), orbContainer);
            float1.setByY(-10);
            float1.setCycleCount(Animation.INDEFINITE);
            float1.setAutoReverse(true);
            float1.setInterpolator(Interpolator.EASE_BOTH);
            float1.play();
            
            // Rotation for outer glow
            RotateTransition rotate = new RotateTransition(Duration.seconds(8), outerGlow);
            rotate.setByAngle(360);
            rotate.setCycleCount(Animation.INDEFINITE);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.play();
            
            orbs.getChildren().add(orbContainer);
        }
        
        return orbs;
    }
    
    //Creates an animated menu button with gradient and hover effects.
    private Button createAnimatedMenuButton(String text, String color1, String color2) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btn.setPrefWidth(280);
        btn.setPrefHeight(55);
        btn.setTextFill(Color.WHITE);
        
        String normalStyle = String.format(
            "-fx-background-color: linear-gradient(to right, %s, %s);" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-border-color: rgba(255,255,255,0.3);" +
            "-fx-border-width: 2;" +
            "-fx-cursor: hand;",
            color1, color2
        );
        
        String hoverStyle = String.format(
            "-fx-background-color: linear-gradient(to right, %s, %s);" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-border-color: rgba(255,255,255,0.8);" +
            "-fx-border-width: 2;" +
            "-fx-cursor: hand;",
            color2, color1
        );
        
        btn.setStyle(normalStyle);
        
        // Shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web(color1, 0.5));
        shadow.setRadius(10);
        shadow.setSpread(0.1);
        btn.setEffect(shadow);
        
        // Hover animations
        btn.setOnMouseEntered(e -> {
            btn.setStyle(hoverStyle);
            
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), btn);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
            
            shadow.setRadius(20);
            shadow.setSpread(0.2);
        });
        
        btn.setOnMouseExited(e -> {
            btn.setStyle(normalStyle);
            
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), btn);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
            
            shadow.setRadius(10);
            shadow.setSpread(0.1);
        });
        
        // Click effect
        btn.setOnMousePressed(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(50), btn);
            scale.setToX(0.95);
            scale.setToY(0.95);
            scale.play();
        });
        
        btn.setOnMouseReleased(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(50), btn);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });
        
        return btn;
    }
    
    //Creates the controls info panel with glass effect.
    private VBox createControlsPanel() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(20));
        panel.setMaxWidth(400);
        panel.setStyle(
            "-fx-background-color: rgba(255,255,255,0.05);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(255,255,255,0.1);" +
            "-fx-border-width: 1;"
        );
        
        Label controlsTitle = new Label("⌨ CONTROLS");
        controlsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        controlsTitle.setTextFill(Color.GOLD);
        
        // Controls grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(30);
        grid.setVgap(8);
        
        String[][] controls = {
            {"WASD / Arrows", "Move"},
            {"1 / 2 / 3 / 4", "Fire / Water / Earth / Air"},
            {"SPACE", "Special Ability"},
            {"ESC / P", "Pause"}
        };
        
        for (int i = 0; i < controls.length; i++) {
            Label key = new Label(controls[i][0]);
            key.setFont(Font.font("Consolas", FontWeight.BOLD, 13));
            key.setTextFill(Color.web("#4ECDC4"));
            key.setStyle(
                "-fx-background-color: rgba(78, 205, 196, 0.2);" +
                "-fx-padding: 3 8 3 8;" +
                "-fx-background-radius: 5;"
            );
            
            Label action = new Label(controls[i][1]);
            action.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
            action.setTextFill(Color.LIGHTGRAY);
            
            grid.add(key, 0, i);
            grid.add(action, 1, i);
        }
        
        panel.getChildren().addAll(controlsTitle, grid);
        
        return panel;
    }
    
    //Creates the game scene with canvas.
    private void createGameScene() {
        gameRoot = new StackPane();
        gameRoot.setStyle("-fx-background-color: #000000;");
        
        // Game canvas
        gameCanvas = new Canvas(width, height);
        gc = gameCanvas.getGraphicsContext2D();
        
        // HUD - Top
        hudTop = new HBox(20);
        hudTop.setAlignment(Pos.TOP_LEFT);
        hudTop.setPadding(new Insets(10));
        hudTop.setPickOnBounds(false);
        StackPane.setAlignment(hudTop, Pos.TOP_LEFT);
        
        // HUD - Bottom
        hudBottom = new HBox(20);
        hudBottom.setAlignment(Pos.BOTTOM_CENTER);
        hudBottom.setPadding(new Insets(10));
        hudBottom.setPickOnBounds(false);
        StackPane.setAlignment(hudBottom, Pos.BOTTOM_CENTER);
        
        // Pause overlay
        pauseOverlay = createPauseOverlay();
        pauseOverlay.setVisible(false);
        
        gameRoot.getChildren().addAll(gameCanvas, hudTop, hudBottom, pauseOverlay);
        
        gameScene = new Scene(gameRoot, width, height);
        
        // Set up input handling
        gameScene.setOnKeyPressed(e -> controller.handleKeyPressed(e.getCode()));
        gameScene.setOnKeyReleased(e -> controller.handleKeyReleased(e.getCode()));
    }
    
   //Creates the pause overlay. 
    private VBox createPauseOverlay() {
        VBox overlay = new VBox(20);
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        overlay.setPadding(new Insets(50));
        
        Label pauseLabel = new Label("PAUSED");
        pauseLabel.setFont(titleFont);
        pauseLabel.setTextFill(Color.WHITE);
        
        Button resumeBtn = createMenuButton("RESUME");
        resumeBtn.setOnAction(e -> controller.resumeGame());
        
        Button menuBtn = createMenuButton("MAIN MENU");
        menuBtn.setOnAction(e -> controller.returnToMenu());
        
        overlay.getChildren().addAll(pauseLabel, resumeBtn, menuBtn);
        
        return overlay;
    }
    
    //Creates the options scene.
    private void createOptionsScene() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0f0c29, #302b63, #24243e);");
        root.setPadding(new Insets(50));
        
        Label titleLabel = new Label("OPTIONS");
        titleLabel.setFont(titleFont);
        titleLabel.setTextFill(Color.GOLD);
        
        // Difficulty slider
        VBox difficultyBox = new VBox(10);
        difficultyBox.setAlignment(Pos.CENTER);
        
        Label diffLabel = new Label("DIFFICULTY");
        diffLabel.setFont(buttonFont);
        diffLabel.setTextFill(Color.WHITE);
        
        HBox diffSliderBox = new HBox(10);
        diffSliderBox.setAlignment(Pos.CENTER);
        
        Label easyLabel = new Label("Easy");
        easyLabel.setTextFill(Color.LIGHTGRAY);
        
        Slider diffSlider = new Slider(1, 3, difficulty);
        diffSlider.setMajorTickUnit(1);
        diffSlider.setMinorTickCount(0);
        diffSlider.setSnapToTicks(true);
        diffSlider.setShowTickMarks(true);
        diffSlider.setPrefWidth(200);
        diffSlider.valueProperty().addListener((obs, old, newVal) -> {
            difficulty = newVal.intValue();
            controller.setDifficulty(difficulty);
        });
        
        Label hardLabel = new Label("Hard");
        hardLabel.setTextFill(Color.LIGHTGRAY);
        
        diffSliderBox.getChildren().addAll(easyLabel, diffSlider, hardLabel);
        difficultyBox.getChildren().addAll(diffLabel, diffSliderBox);
        
        // Back button
        Button backBtn = createMenuButton("BACK");
        backBtn.setOnAction(e -> showMenuScene());
        
        root.getChildren().addAll(titleLabel, difficultyBox, backBtn);
        
        optionsScene = new Scene(root, width, height);
    }
    
    //Creates a styled menu button.
    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setFont(buttonFont);
        btn.setPrefWidth(200);
        btn.setPrefHeight(50);
        btn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #4a4a8a, #2a2a5a);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #6a6aaa;" +
            "-fx-border-width: 2;"
        );
        
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #6a6aaa, #4a4a8a);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #8a8acc;" +
            "-fx-border-width: 2;"
        ));
        
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #4a4a8a, #2a2a5a);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #6a6aaa;" +
            "-fx-border-width: 2;"
        ));
        
        return btn;
    }
    
    // ==================== SCENE SWITCHING ====================
    
    public void showMenuScene() {
        stage.setScene(menuScene);
        GameLogger.getInstance().logGameStateChange("*", "MENU");
    }
    
    public void showGameScene() {
        stage.setScene(gameScene);
        gameScene.getRoot().requestFocus();
    }
    
    public void showOptionsScene() {
        stage.setScene(optionsScene);
    }
    
    //Shows the How to Play scene with game instructions.
    public void showHowToPlayScene() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0a0a1a, #1a1a3a, #0d0d2a);");
        
        // Scrollable content
        VBox content = new VBox(15);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(30, 50, 30, 50));
        content.setMaxWidth(900);
        
        // Title
        Label titleLabel = new Label("📖 HOW TO PLAY");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.GOLD);
        DropShadow titleShadow = new DropShadow();
        titleShadow.setColor(Color.ORANGE);
        titleShadow.setRadius(10);
        titleLabel.setEffect(titleShadow);
        
        // Game objective section
        VBox objectiveBox = createInfoSection("🎯 OBJECTIVE",
            "You are the Chromatic Alchemist, controlling a mystical orb that can transmute between four elemental states.\n\n" +
            "• Absorb colored essence particles that match your current element\n" +
            "• Complete recipes by collecting the required essences\n" +
            "• Progress through 5 increasingly challenging levels\n" +
            "• Avoid obstacles and use power-ups strategically",
            "#FF6B35");
        
        // Elements section
        VBox elementsBox = createInfoSection("🔮 THE FOUR ELEMENTS",
            "Each element has unique properties:\n\n" +
            "🔥 FIRE (Press 1) - Absorbs RED & ORANGE essences\n" +
            "     Speed: Fast (1.2x) | Ability: BURST - Speed boost\n\n" +
            "💧 WATER (Press 2) - Absorbs BLUE & CYAN essences\n" +
            "     Speed: Normal (1.0x) | Ability: FLOW - Phase through obstacles\n\n" +
            "🌍 EARTH (Press 3) - Absorbs GREEN & BROWN essences\n" +
            "     Speed: Slow (0.8x) | Ability: SHIELD - Temporary invincibility\n\n" +
            "💨 AIR (Press 4) - Absorbs WHITE & YELLOW essences\n" +
            "     Speed: Fastest (1.4x) | Ability: DASH - Quick dash movement",
            "#4ECDC4");
        
        // Power-ups section
        VBox powerupsBox = createInfoSection("⚡ POWER-UPS",
            "Collect power-ups to gain temporary abilities:\n\n" +
            "🚀 SPEED BOOST - Move 1.5x faster\n" +
            "🛡️ SHIELD - Reduce damage by 50%\n" +
            "🧲 MAGNET - Attract nearby compatible essences\n" +
            "✨ MULTI-ABSORB - Absorb multiple essences at once\n" +
            "💰 SCORE MULTIPLIER - Double your points\n" +
            "📡 RANGE BOOST - Increase absorption range\n\n" +
            "Power-ups can STACK! Combine them for maximum effect.",
            "#9B59B6");
        
        // Controls section
        VBox controlsBox = createInfoSection("⌨️ CONTROLS",
            "MOVEMENT:\n" +
            "   W / ↑  - Move Up\n" +
            "   S / ↓  - Move Down\n" +
            "   A / ←  - Move Left\n" +
            "   D / →  - Move Right\n\n" +
            "TRANSMUTATION:\n" +
            "   1 - Transform to Fire\n" +
            "   2 - Transform to Water\n" +
            "   3 - Transform to Earth\n" +
            "   4 - Transform to Air\n\n" +
            "ACTIONS:\n" +
            "   SPACE - Use Special Ability\n" +
            "   ESC / P - Pause Game",
            "#7CB342");
        
        // Tips section
        VBox tipsBox = createInfoSection("💡 TIPS",
            "• Switch elements strategically to collect all essence colors\n" +
            "• Use special abilities to escape dangerous situations\n" +
            "• Power-ups have limited duration - use them wisely!\n" +
            "• Complete recipes for bonus points\n" +
            "• Watch out for moving and rotating obstacles\n" +
            "• The absorption range indicator shows your collection area",
            "#E0E0E0");
        
        // Back button
        Button backBtn = createAnimatedMenuButton("← BACK TO MENU", "#4ECDC4", "#45B7AA");
        backBtn.setOnAction(e -> showMenuScene());
        
        Region spacer = new Region();
        spacer.setPrefHeight(10);
        
        content.getChildren().addAll(
            titleLabel,
            objectiveBox,
            elementsBox,
            powerupsBox,
            controlsBox,
            tipsBox,
            spacer,
            backBtn
        );
        
        // Make scrollable
        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        
        root.getChildren().add(scrollPane);
        
        Scene howToPlayScene = new Scene(root, width, height);
        stage.setScene(howToPlayScene);
    }
    
    //Creates a styled info section for the How to Play screen.
    private VBox createInfoSection(String title, String content, String accentColor) {
        VBox section = new VBox(8);
        section.setPadding(new Insets(15));
        section.setStyle(
            "-fx-background-color: rgba(255,255,255,0.05);" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: " + accentColor + "40;" +
            "-fx-border-width: 1;"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web(accentColor));
        
        Label contentLabel = new Label(content);
        contentLabel.setFont(Font.font("Consolas", FontWeight.NORMAL, 13));
        contentLabel.setTextFill(Color.LIGHTGRAY);
        contentLabel.setWrapText(true);
        
        section.getChildren().addAll(titleLabel, contentLabel);
        return section;
    }
    
    public void showPauseOverlay() {
        pauseOverlay.setVisible(true);
    }
    
    public void hidePauseOverlay() {
        pauseOverlay.setVisible(false);
        gameScene.getRoot().requestFocus();
    }
    
    public void showGameOverScene(int score, int level) {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1a0000, #4a0000, #2a0000);");
        root.setPadding(new Insets(50));
        
        Label titleLabel = new Label("GAME OVER");
        titleLabel.setFont(titleFont);
        titleLabel.setTextFill(Color.RED);
        titleLabel.setEffect(new Glow(0.6));
        
        Label scoreLabel = new Label("Final Score: " + score);
        scoreLabel.setFont(buttonFont);
        scoreLabel.setTextFill(Color.WHITE);
        
        Label levelLabel = new Label("Reached Level: " + level);
        levelLabel.setFont(buttonFont);
        levelLabel.setTextFill(Color.LIGHTGRAY);
        
        Button retryBtn = createMenuButton("TRY AGAIN");
        retryBtn.setOnAction(e -> controller.newGame());
        
        Button menuBtn = createMenuButton("MAIN MENU");
        menuBtn.setOnAction(e -> showMenuScene());
        
        root.getChildren().addAll(titleLabel, scoreLabel, levelLabel, retryBtn, menuBtn);
        
        gameOverScene = new Scene(root, width, height);
        stage.setScene(gameOverScene);
    }
    
    public void showVictoryScene(int score) {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0a2a0a, #1a4a1a, #0a3a0a);");
        root.setPadding(new Insets(50));
        
        Label titleLabel = new Label("VICTORY!");
        titleLabel.setFont(titleFont);
        titleLabel.setTextFill(Color.GOLD);
        titleLabel.setEffect(new Glow(0.8));
        
        Label subtitleLabel = new Label("You have mastered the Chromatic Arts!");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        subtitleLabel.setTextFill(Color.LIGHTGREEN);
        
        Label scoreLabel = new Label("Final Score: " + score);
        scoreLabel.setFont(buttonFont);
        scoreLabel.setTextFill(Color.WHITE);
        
        Button playAgainBtn = createMenuButton("PLAY AGAIN");
        playAgainBtn.setOnAction(e -> controller.newGame());
        
        Button menuBtn = createMenuButton("MAIN MENU");
        menuBtn.setOnAction(e -> showMenuScene());
        
        root.getChildren().addAll(titleLabel, subtitleLabel, scoreLabel, playAgainBtn, menuBtn);
        
        victoryScene = new Scene(root, width, height);
        stage.setScene(victoryScene);
    }
    
    // ==================== RENDERING ====================
    
    public void render(GameModel model) {
        if (model.getCurrentState() != GameState.PLAYING && 
            model.getCurrentState() != GameState.PAUSED) {
            return;
        }
        
        // Clear canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        
        // Render level chambers
        Level level = model.getCurrentLevel();
        if (level != null) {
            renderChamber(level.getRootChamber());
        }
        
        // Render player
        Player player = model.getPlayer();
        if (player != null) {
            renderPlayer(player);
        }
        
        // Render HUD
        renderHUD(model);
    }
    
    private void renderChamber(ChamberComponent chamber) {
        // Draw chamber background
        gc.setFill(Color.web(chamber.getBackgroundColor()));
        gc.fillRect(chamber.getX(), chamber.getY(), chamber.getWidth(), chamber.getHeight());
        
        // Draw chamber border
        gc.setStroke(Color.web(chamber.getBorderColor()));
        gc.setLineWidth(3);
        gc.strokeRect(chamber.getX(), chamber.getY(), chamber.getWidth(), chamber.getHeight());
        
        // Draw chamber name
        gc.setFill(Color.web(chamber.getBorderColor()).deriveColor(0, 1, 1, 0.5));
        gc.setFont(smallFont);
        gc.fillText(chamber.getName(), chamber.getX() + 10, chamber.getY() + 20);
        
        // Render children first (if composite)
        for (ChamberComponent child : chamber.getChildren()) {
            renderChamber(child);
        }
        
        // Render obstacles
        for (Obstacle obstacle : chamber.getAllObstacles()) {
            renderObstacle(obstacle);
        }
        
        // Render power-ups
        for (PowerUp powerUp : chamber.getAllPowerUps()) {
            if (!powerUp.isCollected()) {
                renderPowerUp(powerUp);
            }
        }
        
        // Render essences
        for (EssenceParticle essence : chamber.getAllEssences()) {
            if (!essence.isCollected()) {
                renderEssence(essence);
            }
        }
    }
    
    private void renderEssence(EssenceParticle essence) {
        double x = essence.getX();
        double y = essence.getFloatY();
        double radius = essence.getVisualRadius();
        
        // Glow effect
        gc.setFill(Color.web(essence.getGlowColor()).deriveColor(0, 1, 1, 0.3));
        gc.fillOval(x - radius * 1.5, y - radius * 1.5, radius * 3, radius * 3);
        
        // Main essence
        RadialGradient gradient = new RadialGradient(
            0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web(essence.getGlowColor())),
            new Stop(1, Color.web(essence.getColorHex()))
        );
        gc.setFill(gradient);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        
        // Inner highlight
        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.5));
        gc.fillOval(x - radius * 0.4, y - radius * 0.4, radius * 0.6, radius * 0.6);
    }
    
    private void renderObstacle(Obstacle obstacle) {
        double x = obstacle.getX();
        double y = obstacle.getY();
        double w = obstacle.getVisualWidth();
        double h = obstacle.getVisualHeight();
        
        gc.save();
        
        // Apply rotation for rotating obstacles
        if (obstacle.getType() == Obstacle.ObstacleType.ROTATING) {
            gc.translate(x + w / 2, y + h / 2);
            gc.rotate(obstacle.getRotation());
            gc.translate(-(x + w / 2), -(y + h / 2));
        }
        
        // Draw obstacle
        gc.setFill(Color.web(obstacle.getColor()));
        gc.fillRect(x, y, w, h);
        
        // Border
        gc.setStroke(Color.web(obstacle.getColor()).brighter());
        gc.setLineWidth(2);
        gc.strokeRect(x, y, w, h);
        
        gc.restore();
    }
    
    private void renderPowerUp(PowerUp powerUp) {
        double x = powerUp.getX();
        double y = powerUp.getFloatY();
        double radius = powerUp.getVisualRadius();
        
        gc.save();
        
        // Rotation
        gc.translate(x, y);
        gc.rotate(powerUp.getRotationAngle());
        gc.translate(-x, -y);
        
        // Outer glow
        gc.setFill(Color.web(powerUp.getColor()).deriveColor(0, 1, 1, 0.2));
        gc.fillOval(x - radius * 1.8, y - radius * 1.8, radius * 3.6, radius * 3.6);
        
        // Main shape 
        gc.setFill(Color.web(powerUp.getColor()));
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        
        // Inner star pattern
        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.7));
        gc.fillOval(x - radius * 0.5, y - radius * 0.5, radius, radius);
        
        gc.restore();
        
        // Label
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(powerUp.getDisplayName(), x, y + radius + 15);
        gc.setTextAlign(TextAlignment.LEFT);
    }
    
    private void renderPlayer(Player player) {
        double x = player.getX();
        double y = player.getY();
        double radius = player.getVisualRadius();
        
        // Invincibility flash
        if (player.isInvincible() && (System.currentTimeMillis() / 100) % 2 == 0) {
            return;
        }
        
        // Phasing effect
        double alpha = player.isPhasing() ? 0.5 : 1.0;
        
        // Outer glow
        gc.setFill(Color.web(player.getGlowColor()).deriveColor(0, 1, 1, 0.3 * alpha));
        gc.fillOval(x - radius * 2, y - radius * 2, radius * 4, radius * 4);
        
        // Shield effect
        if (player.isShielded()) {
            gc.setStroke(Color.CYAN.deriveColor(0, 1, 1, 0.6));
            gc.setLineWidth(4);
            gc.strokeOval(x - radius * 1.5, y - radius * 1.5, radius * 3, radius * 3);
        }
        
        // Main orb
        RadialGradient gradient = new RadialGradient(
            0, 0, 0.3, 0.3, 0.8, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.WHITE.deriveColor(0, 1, 1, alpha)),
            new Stop(0.5, Color.web(player.getStateColor()).deriveColor(0, 1, 1, alpha)),
            new Stop(1, Color.web(player.getGlowColor()).deriveColor(0, 1, 1, alpha))
        );
        gc.setFill(gradient);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        
        // Inner core
        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.8 * alpha));
        gc.fillOval(x - radius * 0.3, y - radius * 0.3, radius * 0.6, radius * 0.6);
        
        // State indicator ring
        gc.setStroke(Color.web(player.getGlowColor()));
        gc.setLineWidth(2);
        gc.strokeOval(x - radius - 5, y - radius - 5, radius * 2 + 10, radius * 2 + 10);
        
        // Absorption range indicator
        double absRange = player.getAbsorptionRange();
        gc.setStroke(Color.web(player.getStateColor()).deriveColor(0, 1, 1, 0.2));
        gc.setLineWidth(1);
        gc.strokeOval(x - absRange, y - absRange, absRange * 2, absRange * 2);
    }
    
    private void renderHUD(GameModel model) {
        Player player = model.getPlayer();
        Level level = model.getCurrentLevel();
        
        if (player == null || level == null) return;
        
        double padding = 15;
        double barWidth = 200;
        double barHeight = 20;
        
        // Health bar
        gc.setFill(Color.DARKRED);
        gc.fillRect(padding, padding, barWidth, barHeight);
        gc.setFill(Color.RED);
        double healthPercent = (double) player.getHealth() / player.getMaxHealth();
        gc.fillRect(padding, padding, barWidth * healthPercent, barHeight);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRect(padding, padding, barWidth, barHeight);
        gc.setFill(Color.WHITE);
        gc.setFont(hudFont);
        gc.fillText("HP: " + player.getHealth() + "/" + player.getMaxHealth(), padding + 5, padding + 15);
        
        // Score
        gc.setFill(Color.GOLD);
        gc.fillText("Score: " + player.getScore(), padding, padding + barHeight + 25);
        
        // Level info
        gc.setFill(Color.WHITE);
        gc.fillText("Level " + level.getLevelNumber() + ": " + level.getName(), padding, padding + barHeight + 50);
        
        // Completion
        gc.setFill(Color.LIGHTGREEN);
        gc.fillText(String.format("Progress: %.0f%%", level.getCompletionPercentage()), padding, padding + barHeight + 75);
        
        // Current element state
        double stateX = width - 150;
        gc.setFill(Color.web(player.getStateColor()));
        gc.fillOval(stateX, padding, 30, 30);
        gc.setStroke(Color.WHITE);
        gc.strokeOval(stateX, padding, 30, 30);
        gc.setFill(Color.WHITE);
        gc.fillText(player.getStateName(), stateX + 40, padding + 20);
        
        // Compatible colors
        gc.setFont(smallFont);
        gc.setFill(Color.LIGHTGRAY);
        String[] colors = player.getCurrentState().getCompatibleColors();
        gc.fillText("Absorbs: " + String.join(", ", colors), stateX - 50, padding + 50);
        
        // Active decorators
        String[] decorators = player.getActiveDecoratorNames();
        if (decorators.length > 0) {
            gc.setFill(Color.YELLOW);
            gc.fillText("Active: " + String.join(", ", decorators), padding, height - 30);
        }
        
        // Element transmutation hints
        gc.setFill(Color.GRAY);
        gc.fillText("1:Fire  2:Water  3:Earth  4:Air  SPACE:Ability", width / 2 - 150, height - 15);
        
        // Time
        gc.setFill(Color.WHITE);
        gc.setFont(hudFont);
        gc.fillText(String.format("Time: %.1fs", model.getLevelTime()), width - 120, height - 30);
    }
}
