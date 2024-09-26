# Pacman
A Pacman clone created using Java and the LibGDX framework.

![Pacman](https://i.imgur.com/dxAQDZZ.png)

### Levels
The motivation for this project was mainly educational as a result there are only 2 levels. All levels should be placed under core/assets/Mazes/ the format of which should appear as the following:

```
11 20
XXXXXXXXXXXXXXXXXXXX
X    X        X    X
X XX X XXXXXX X XX X
X X      g       X X
X X XX XX  XX XX X X
X      Xg ggX      X
X X XX XXXXXX XX X X
X X      I       X X
X XX X XXXXXX X XX X
X    X  S     X    X
XXXXXXXXXXXXXXXXXXXX
```

The 2 space separated numbers on the first line indicate heigh and width of the maze, respectively. Every X represents a wall, g a ghost, I the initial fruit bonus, and S the starting position of Pacman. There is also a data file associated to each level containing additional information. Admittedly, the data files are a bit convoluted but this was before I knew of XML and other more conventional forms of data storage.
