=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: alynie
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays: My 2D array is a private int 2D array called "landscape" in the GameCourt class,
  which stores the land of the game (the hill that the ball goes on). The landscape 2D array
  stores a 1 if there is a physical barrier there, and 0 otherwise. Using 2D arrays makes the most sense
  considering that it represents the gridded layout of the screen, where each positioning of objects
  will constantly reference back to whether or not it can occupy space in the screen.

  Landscape is primary used/accessed in two ways: first, it's used to physically draw the landscape. Second, it's used
  for the movement functions of both the ball and the person. When the person and ball moves forward and
  backwards, their x-velocity is constant but the speed to which they move up the mountain is entirely dependent
  on the first "block of space" that represents the line of the mountain where land meets air
  . (which is done using a while loop).

  I decided to use ints because they are super easy to check when accessing whether or not the person/ball
  can move past that specific block.
  I could've used boolean as well but it made more logical sense to me to give each tile a value for a barrier
  rather than assign the empty spaces as "false" or "true" - but either datatype would have likely worked for
  this game's purposes.

  2. Collections & Maps
  I implement LinkedLists in two parts of my game. The first is in the "Rain" class. I
  use a LinkedList that stores an array of size three at each item to denote the total raindrops.
  (The array stores the x & y coords and the raindrop type of the rain)
  For every couple of seconds, the rain will get heavier or lighter by ~150 raindrops
  randomly - hypothetically, the rain could intensify up to infinite raindrops.
  The LinkedList here is necessary because it expands and shrinks at random by
  a certain number of raindrops. The reasons as to why I did not use a map is I think that I would
  have to store the coordinates of each raindrop in the map value, but there can be multiple
  raindrops at any certain coordinate set so this doesn't make too much sense. I decided not to use
  an ArrayList because the indexing and accessing each rain drop didn't matter too much, and it is
  easier to manipulate LinkedLists instead.

  The second is in the "GameCourt" class. This is where I use a
  LinkedList to track the number of key presses per interval of time - basically,
  for every 30 milliseconds of time, the total number of keyReleases the user puts within that time period is
  added to the list, and then the last x time intervals will be added together
  in order to determine the velocity of the person moving up the mountain.
  The reason that LinkedLists must be used here rather than just an array of a given size length
  is that the ball rolls back down if the total keyspressed don't hit a certain threshold, but that is only
  if the size of the LinkedList has already passed the "time threshold". Basically, if I only instantiated an
  Array of size, say 12, then for the first couple seconds of the game, the person would be moving downwords/backwards
  whereas they shouldn't because time hasn't even passed yet. I opted for linkedLists here for the same reasons as above,
  there's no mapping involved, and it is ordered & needs to be easily manipulated.

  3. Inheritance/Subtyping
  I have the Weather interface present that has the methods of both draw and change -
  dynamic dispatch is used in the main GameCourt class when the weather is generated randomly
  at every reset. The Rain and the Sun class implement the weather interface in pretty differentiated ways.
  While the Sun will just simply rotate on itself when it "changes", the Rain class has a private collection
  that stores around 300 raindrops that each move at random velocities in one specific direction down the picture.

  4. jUnit Testing:
  I tested all the methods that didn't have to do with the interfaces & actual
  in the GameTest document! Mostly, I was able to test several edge cases (what happens
  if the person is at the very top of the hill, or what happens if the array that
  was passed in was null).

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  RunSisyphus - Runs the jpanels and widgets of the GUI
  GameCourt - "main" class where most of the game functionality/logic occurs as well as the painting component.
  contains methods that allows the ball to move up/roll down, gets the speed at which the ball should move up,
  and also has key event listeners.
  Menu - this is to load the menu when the rock rolls back to the bottom of the hill to ask if "you are happy".
  has the option of selecting yes or no.
  Rain - implements the weather interface: paints a rainy day in the background & the rain will
  change intensity at random in every couple of seconds.
  Sun - implements the weather interface; the sun will shine and rotate every other second.
  Sisyphus - class that codes the behavior of the person. implements different states of the person, like moving
  up the hill, walking down the hill, or ending the game. also allows the person to move up or down the hill.
  EndScene - loads in the image of the "end scene" in order to display in GameCourt at the very end.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  Yes! I was extremely confused at first as I designed the game, and decided to plunge
  into coding (since I was stressed by deadlines) based on piece by piece rather than overlaying
  a strategic way to actually design the way I should implement the game. As a consequence I was
  frequently confused on the ways to actually code the components of the game (specifically, how to code
  the ending scene, the menu box, etc), but eventually I was able to make it work out.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  The design is frankly more messy than not. There is a good seperation in functionality
  with regards to the methods that I implemented, but not necessarily good seperation
  and implementation of classes/subtyping (there are probably
  a lot of overlapping methods that I could've figured out a way
  to simply extend or provide abstract classes for).

  The private state is encapsulated well, because they're not changeable outside of the class.

  The largest change I would make in my design is to make the rock a seperate class of its own,
  and maybe make a class that was just "HillObjects" that were able to combine a lot of the individual
  methods of the Sisyphus class and the rock class. I realized there was a lot of overlapping behavior
  and it would have been much more organized if I initially created that class and worked from there.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  Most of the stuff I figured out on my own, but I did reference some images of people walking up & down the stairs
  in order to draw the frames for the people. I also referenced some websites on Sisyphus to better think about
  how exactly I could more accurately implement the game: http://dbanach.com/sisyphus.htm
