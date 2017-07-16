# Library
There are countless packages to use for easier content/core development within this library such as the <b><sup>1</sup> event package</b> that is for creating an <b><sup>1</sup> event-based</b> project or the <b><sup>2</sup> module package</b>, which allows your project to be <b><sup>2</sup> modular</b> so you can create separated content that can be easily added or removed without effecting your core project.

## How Easy It Is
<b>Tick Implementation</b>
```java
/**
 * This method will execute a tick within 15000ms (15 seconds)
 */
new Tick() {
  protected void tick() {
    queue(1000); // You can call the 'queue(Long)' method to execute this tick for ANY specific period
    System.out.println("Hello World");
  }
}.queue(15000); // This will automatically queue the Tick for your Ticker to execute in 15 seconds
```
<b>Event Implementation</b>
```java
PlayerWalkEvent event = new PlayerWalkEvent(player, previousLocation, newLocation); // Code=(player, previous_tile, walk_tile)
event.call(); // Calls the event for any EventListener that a 
if (event.isCancelled())
  return; // If the event is cancelled, then you wont have to walk the player to the next tile
  
executePlayerWalkingHere();

_____________________________________________________________________________________________


/**
 * This will listen for the PlayerWalkEvent to be 'called' and once it sees that it has been called, then it will execute this listener
 */
EventListener playerWalkListener = new EventListener() {
  @EventMethod // Just add this annotation to any method in an EventListener and it becomes a method that listens to the specified event
  private void onWalk(PlayerWalkEvent event) {
    // Because you received a notification that the player has walked, you can check to see if they have walked to a specific location, and if they have, then you can damage them or teleport them or anything
  }
};
```


## Goal
The goal of this project is to give an ease to RuneScape Private Server development for any developer by allowing shorter, comprehendable, and cleaner code, as well as easily understandable designs that even the most unexperienced should be able to use.

Here is a brief list of things to complete:
- [ ] Server Features
  - [ ] Region Management (Dynamic/Static Map Building)
  - [x] Flexible Tick System
  - [x] Event Listening/Managing
  - [ ] Easy Mask/Update handling
  - [ ] Multiple Protocol Support
  - [ ] Easy packet reading/writing
- [ ] Content Features
  - [x] Interface handling and functionalities
  - [x] Action system
  - [ ] Entity design and interaction
  - [ ] Mini Game handling
  - [ ] Skill design and management
  - [x] Container system
- [x] Configuration serialization (player saves, server data, etc.)
- [x] Modular system for separated programming

## Setting Up
- <b>Eclipse</b>
  * You can download the library [Here](https://github.com/tehnewb/Library/releases)
  * You also need to download the dependencies for the project [Here](https://www.dropbox.com/sh/05aej3isz120vf3/AACxOGhV0Z2tfBYfcfKJr8nEa?dl=0)
  * Create a folder in your Eclipse Project to place the Library and dependency files you downloaded
  * Right-click the Library JAR file and the dependency JAR files in your Eclipse Project
  * Click on Build Path -> Add to Build Path
  * <b>You're all set up!</b>
  
- <b>IntelliJ IDEA</b>
  * You can download the library [Here](https://github.com/tehnewb/Library/releases)
  * 
