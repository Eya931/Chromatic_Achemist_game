package com.chromatic.alchemist.model.observer;

/**
 * OBSERVER PATTERN - Observer Interface
 * 
 * Defines the interface for objects that want to be notified of game events.
 * Observers register with the GameEventManager and receive notifications
 * when relevant events occur.
 */

public interface GameObserver {   
    //Called when a game event occurs.
    void onGameEvent(GameEvent event);
}
