# 🌈 Chromatic Alchemist

**A Design Patterns Showcase Game**

![Java](https://img.shields.io/badge/Java-17-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-17.0.2-blue)
![Maven](https://img.shields.io/badge/Maven-3.8+-red)
![License](https://img.shields.io/badge/License-MIT-green)

---

## 📖 Description

**Chromatic Alchemist** is an original JavaFX game where you control an **Alchemist Orb** that can transmute between four elemental states: **Fire**, **Water**, **Earth**, and **Air**. Each elemental state allows you to absorb specific colored essence particles while navigating through hierarchical chamber structures.

### 🎮 Gameplay

- **Transmute** between elemental states to absorb color-compatible essences
- **Navigate** through interconnected chambers with obstacles and power-ups
- **Complete recipes** by collecting required essence combinations
- **Use special abilities** unique to each elemental state
- **Collect power-ups** that stack temporary abilities on your orb

---

## ✨ Features

- **4 Design Patterns** implemented with real game mechanics
- **5 Unique Levels** with increasing complexity
- **6 Power-up Types** with stackable decorator effects
- **4 Elemental States** with unique abilities
- **Complete Logging System** with Log4j2
- **JavaFX GUI** with menu, options, pause, and game over scenes

---

## 🏗️ Design Patterns

### 1. State Pattern (Mandatory)
**Location:** `com.chromatic.alchemist.model.state`

The Player's elemental state determines behavior:
- **FireState** - Absorbs RED/ORANGE, Speed: 1.2x, Ability: BURST (2x speed burst)
- **WaterState** - Absorbs BLUE/CYAN, Speed: 1.0x, Ability: FLOW (phase through obstacles)
- **EarthState** - Absorbs GREEN/BROWN, Speed: 0.8x, Ability: SHIELD (temporary invincibility)
- **AirState** - Absorbs WHITE/YELLOW, Speed: 1.4x, Ability: DASH (quick dash movement)

```java
// Example: State transition
player.transmuteToFire();  // Changes state and behavior
player.getCurrentState().useSpecialAbility(player);  // State-specific ability
```

### 2. Decorator Pattern (Mandatory)
**Location:** `com.chromatic.alchemist.model.decorator`

Power-ups wrap player abilities with stackable effects:
- **SpeedBoostDecorator** - 1.5x movement speed
- **ShieldDecorator** - 50% damage reduction
- **MagnetDecorator** - Attracts nearby essences
- **MultiAbsorbDecorator** - Absorb multiple essences at once
- **ScoreMultiplierDecorator** - 2x score multiplier
- **RangeBoostDecorator** - 2x absorption range

```java
// Example: Stacking decorators
PlayerAbility ability = new BasePlayerAbility();
ability = new SpeedBoostDecorator(ability, 10.0);  // 10 second duration
ability = new ShieldDecorator(ability, 5.0);       // Stack shield on top
```

### 3. Composite Pattern (Mandatory)
**Location:** `com.chromatic.alchemist.model.composite`

Chambers can contain:
- **SimpleChamber** (Leaf) - Contains essences, obstacles, and power-ups
- **CompoundChamber** (Composite) - Contains child chambers + its own content

```java
// Example: Hierarchical chamber structure
CompoundChamber world = new CompoundChamber("World", 0, 0, 1024, 768);
SimpleChamber northRoom = new SimpleChamber("North", 0, 0, 512, 384);
SimpleChamber southRoom = new SimpleChamber("South", 0, 384, 512, 384);
world.add(northRoom);
world.add(southRoom);

// Get ALL essences from entire hierarchy
List<EssenceParticle> allEssences = world.getAllEssences();
```

### 4. Observer Pattern (4th Pattern)
**Location:** `com.chromatic.alchemist.model.observer`

Event-driven game notifications:
- **GameEventManager** (Subject/Singleton) - Manages subscriptions
- **GameObserver** (Observer Interface) - Receives notifications
- **GameEvent** - Event data with type and payload

```java
// Example: Subscribing to events
GameEventManager.getInstance().subscribe(
    new GameEvent.EventType[]{EventType.ESSENCE_COLLECTED, EventType.LEVEL_COMPLETED},
    this
);

// Firing events
GameEventManager.getInstance().fireEvent(
    new GameEvent(EventType.SCORE_CHANGED)
        .put("score", 1500)
        .put("delta", 100)
);
```

---

## 📁 Project Structure

```
DesignPatternsProject/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/chromatic/alchemist/
│       │       ├── Main.java                    # Entry point
│       │       ├── controller/
│       │       │   └── GameController.java      # MVC Controller
│       │       ├── logging/
│       │       │   └── GameLogger.java          # Singleton logger
│       │       ├── model/
│       │       │   ├── GameModel.java           # Game logic
│       │       │   ├── Level.java               # Level wrapper
│       │       │   ├── Recipe.java              # Objectives
│       │       │   ├── LevelGenerator.java      # Level creation
│       │       │   ├── state/                   # STATE PATTERN
│       │       │   │   ├── ElementalState.java
│       │       │   │   ├── FireState.java
│       │       │   │   ├── WaterState.java
│       │       │   │   ├── EarthState.java
│       │       │   │   ├── AirState.java
│       │       │   │   └── GameState.java
│       │       │   ├── decorator/               # DECORATOR PATTERN
│       │       │   │   ├── PlayerAbility.java
│       │       │   │   ├── BasePlayerAbility.java
│       │       │   │   ├── AbilityDecorator.java
│       │       │   │   ├── SpeedBoostDecorator.java
│       │       │   │   ├── ShieldDecorator.java
│       │       │   │   ├── MagnetDecorator.java
│       │       │   │   ├── MultiAbsorbDecorator.java
│       │       │   │   ├── ScoreMultiplierDecorator.java
│       │       │   │   └── RangeBoostDecorator.java
│       │       │   ├── composite/               # COMPOSITE PATTERN
│       │       │   │   ├── ChamberComponent.java
│       │       │   │   ├── SimpleChamber.java
│       │       │   │   ├── CompoundChamber.java
│       │       │   │   ├── EssenceParticle.java
│       │       │   │   ├── Obstacle.java
│       │       │   │   └── PowerUp.java
│       │       │   ├── observer/                # OBSERVER PATTERN
│       │       │   │   ├── GameObserver.java
│       │       │   │   ├── GameEvent.java
│       │       │   │   └── GameEventManager.java
│       │       │   └── entity/
│       │       │       └── Player.java
│       │       └── view/
│       │           ├── GameApplication.java     # JavaFX Application
│       │           └── GameView.java            # Rendering
│       └── resources/
│           └── log4j2.xml                       # Logging config
├── docs/
│   ├── uml/
│   │   └── ChromaticAlchemist.puml             # UML diagram
│   └── soutenance/
│       └── presentation_guide.md               # Presentation guide
├── logs/
│   └── example_game.log                        # Example log file
├── pom.xml
└── README.md
```

---

## 🚀 Installation & Running

### Prerequisites
- Java 17 or higher
- Maven 3.8 or higher

### Build
```bash
cd DesignPatternsProject
mvn clean compile
```

### Run
```bash
mvn javafx:run
```

### Package
```bash
mvn package
java -jar target/chromatic-alchemist-1.0.0.jar
```

---

## 🎮 Controls

| Key | Action |
|-----|--------|
| `W` / `↑` | Move Up |
| `S` / `↓` | Move Down |
| `A` / `←` | Move Left |
| `D` / `→` | Move Right |
| `1` | Transmute to Fire |
| `2` | Transmute to Water |
| `3` | Transmute to Earth |
| `4` | Transmute to Air |
| `SPACE` | Use Special Ability |
| `ESC` / `P` | Pause |

---

## 📊 Logging System

The game uses Log4j2 with the format: `[YYYY-MM-DD HH:MM:SS] [LEVEL] Message`

### Log Files
- `logs/game.log` - Main game log
- `logs/state.log` - State transitions
- `logs/event.log` - Game events

### Example Log Output
```
[2024-01-15 14:30:00] [INFO] =====================================
[2024-01-15 14:30:00] [INFO]      CHROMATIC ALCHEMIST v1.0
[2024-01-15 14:30:00] [INFO] =====================================
[2024-01-15 14:30:01] [INFO] Game started
[2024-01-15 14:30:01] [INFO] Level 1 started: Tutorial - The First Transmutation
[2024-01-15 14:30:05] [INFO] State change: FIRE -> WATER
[2024-01-15 14:30:08] [INFO] Essence collected: BLUE (+10 points)
[2024-01-15 14:30:12] [INFO] Power-up collected: SPEED_BOOST
[2024-01-15 14:30:12] [INFO] Decorator added: SpeedBoost (10.0s)
```

---

## 📐 UML Diagram

The UML diagram is located at `docs/uml/ChromaticAlchemist.puml` and can be rendered using:
- [PlantUML Online](https://www.plantuml.com/plantuml/uml/)
- PlantUML VS Code Extension
- IntelliJ IDEA with PlantUML plugin

---

## 🎯 Levels

1. **Tutorial** - Learn basic controls and Fire state
2. **Dual Chambers** - Two connected chambers with Fire and Water
3. **Four Elements** - All four elemental areas
4. **Nested Chambers** - Complex hierarchical structure
5. **Final Challenge** - Ultimate test with all mechanics

---

## 👥 Credits

**Design Patterns Project** - Educational game showcasing GoF patterns

---

## 📄 License

This project is created for educational purposes.
