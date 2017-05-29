Patty Commins and Anna Meyer

We will build the game of life. The user will be able to select which cells are "alive" at the start. Then the simulation will run either until completion or until the user chooses to stop. The main screen will have the game board, as well as a small control panel containing the number of steps, speed, and start, stop, and pause buttons. There will also be an option to graph the population thoughout a particular simulation.

MVC is appropriate for our project because the "model" is stored in the box class, the controller communicates both with the individual boxes and the user interface, and the "view" is the user interface itself (stored in the fxml file). The model stores the information about each box in the grid, namely, whether it is alive or not. The primary view will be the grid of boxes and the user will also have an option to see a graph of population over time.

Classes we need:
Main
Controller
Box (each square on the game grid will be an individual object with a position and alive/dead status)
FMXL

Questions:
1) Would you recommend that we put the graph and any other statistics on the same page as the grid of boxes, or do you think the grid should disappear while viewing the graph so that it can be larger?
2) We don't really see a use for serialization. Can you think of any advantages to this for our project?
