# Chromatic Alchemist Game 

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

## 🎯 Levels

1. **Tutorial** - Learn basic controls and Fire state
2. **Dual Chambers** - Two connected chambers with Fire and Water
3. **Four Elements** - All four elemental areas
4. **Nested Chambers** - Complex hierarchical structure
5. **Final Challenge** - Ultimate test with all mechanics

---


## 👥 Members

- Elhouche Mariem
- Boudidah Eya 

---

## 📊 Tools

- **Language:** Java 17
- **Framework GUI:** JavaFX
- **Logging:** Log4j2
- **Build:** Maven

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

### 🔁 State Pattern:    
The **State Pattern** controls the player's behavior based on their current **elemental state**.  
Each state modifies movement speed, absorption behavior, and provides a unique ability.

### 🎨 Decorator Pattern
The **Decorator Pattern** is used to dynamically enhance player abilities through **stackable power-ups**.

### 🧩 Composite Pattern
The **Composite Pattern** represents the hierarchical structure of game chambers.

### 📣 Observer Pattern
The **Observer Pattern** enables an **event-driven architecture** for game notifications.

---

## 🚀 Installation & Running

### Prerequisites
- Java 17 or higher
- Maven 3.8 or higher

### Clone
```bash
git clone https://github.com/Eya931/Chromatic_Achemist_game.git
```

### Build
```bash
cd Chromatic_Achemist_game
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
├── logs/
│   └── example_game.log                        # Example log file
├── pom.xml
└── README.md
```

