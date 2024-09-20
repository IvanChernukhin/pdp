Custom Composables
Implement a Radial Progress Bar with Custom Animation (video #1 attached), don’t pay attention to horizontal swipe, it’s just to launch animation)
Create a custom composable that draws a radial progress bar with a unique gradient fill (gradient color of your choice instead of a green one).
Animate the progress change with a spring-based easing function (depending on the param (progress, numeric), demonstrate in preview mode its changing).
Add a central text indicator that displays the current progress as a percentage (instead of Maturity Value SAR 1,010).
Implement a Custom Scaffold (video #2 attached):
Its main idea is shown on the video and says that we need to be able to scroll the content up to the point where its bottom is above floating buttons.
should accept modifier and two composable blocks: floatingButtons: @Composable () -> Unit and content: @Composable (Size) -> Unit as parameters
hint on how we can do this: first obtain placeable of floating buttons section, then obtain its width and height, then do the same with content with passing these sizes to it (content will use it to add this bottom padding to itself), and then do layout (place placeables), as usual.
Implement a custom pie chart (image #3 attached):
Should receive an appropriate state as a parameter (should be stable)
Each section is drawn with a different color based on a state.
Add an animation that smoothly collapses/expands sections when the chart’s data changes.
(*Extra) Add the effect of the border appearing around the section as a reaction to the click on that section.
3 files
![Video 1](https://github.com/IvanChernukhin/pdp/blob/master/Screen%20Recording%202024-08-09%20at%2011.23.18.mov)
![Video 2](https://github.com/IvanChernukhin/pdp/blob/master/Screen%20Recording%202024-08-09%20at%2011.47.50.mov)
![Image 1](https://github.com/IvanChernukhin/pdp/blob/master/image%20(2).png)

Side Effects
What side effect functions and how exactly can be used in these cases (if more than 1 applies please list all that comes to mind, if necessary - describing in words the whole solution up to view model):
Calculation of how many actual (re)drawings happened while user sees a particular screen.
Scrolling to the top of the LazyColumn on some events based on state (for instance, when user triggers refresh and gets an updated list, we should scroll to the top)
Implementation of the function which returns the state of the keyboard (allowing to subscribe on whether it’s opened):
@Composable fun keyboardAsState(): State<Boolean> { … }
Implementation of user inactivity tracker (calling a callback from parameters after custom amount of seconds)
Implementation of the debouncing text function, which returns pair of text state and lambda be able to change the text outside
@Composable fun debouncingText(text: String, timeMillis: Long = 500, onDebounceText: (String) -> Unit): Pair<String, (String) -> Unit> { … }
