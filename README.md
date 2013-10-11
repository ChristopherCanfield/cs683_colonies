cs683_colonies
==============

BU MET CS683: Colonies App

Colonies is the project I created for the BU MET CS683: Mobile Application Development course, which I took in Spring 2013. It is a game / software toy / virtual life simulator. The user can select attributes for groups (colonies) of organisms, and then place the organisms on the screen and watch them grow, reproduce, smile/frown and die. My niece wanted to be able to pop the organisms, so that's possible as well.

Android functionality:
* Explicit Intents, including passing data between Activities using Bundles and startActivityForResult()
* UI elements: Buttons, SeekBar, ImageViews, Layouts, tabs
* Four different sceens (Activities)
* Dynamically changing UI elements, such as replacing the image in an ImageView in response to user input, time (see: Handlers), or other game state changes
* Android xml animation
* Threading, and communicating between threads using Handlers
* Filesystem: Serialization and reading text files
* Saving complex state information when the Activity is paused, and then restoring that state on resume (object Serialization + Android Activity lifecycle)
* Testing using Android JUnit (located in the ColoniesApp/ColoniesTests folder)
* Android support library for compatibility with older versions
* Probably more

As with any real app, there are many instances where best practices were not followed. In particular, the way I laid out the screen is inefficient (too many views and too much nesting) and does not scale properly to all screen layouts. I should have drawn the images directly, instead of using ImageViews. Additionally, the quality of my code and comments fell as the end of the semester approached. The simulation component is also not nearly as advanced as I would have liked, but then this was a Mobile Application Development course, not an AI course.

However, despite the shortcomings I was able to turn in a complete medium-scale app, which my 10 year-old neice enjoys, so I rate the project as a success.

- Christopher D. Canfield
