# Explainations

First, I just wrote this because.
Thus, don't judge me ok ?

Also, I made an assessment of the program's needs in memory space [here](details/Memory.md).

## Cell representation

An obvious need while representing a sudoku grid is the representation of one cell in this grid. This is the `Cell` class (see [here](../Cell.java)), whose instances hold:

- Its value
- An array of `Cell`s containing the other `Cell`s on the same line
- An array of `Cell`s containing the other `Cell`s on the same column
- An array of `Cell`s containing the other `Cell`s on the same block
- An array of booleans remembering wether or not this cell could accept each possible value

## Grid representation

Also, the sudoku grid is an instance of `SudokuGrid` (see [here](../SudokuGrid.java)) that features: 

- An 9 by 9 array of `Cell`s
- A hash map that maps the `Cell`s that have not been assigned yet to their position on the grid (abusing the `Move` class).
- Some other `Grid` instance that is helpful to display the grid as a string.

## Assignation

When assigning some value, the grid will then send the message: "*you have been assigned a* `v`" to the concerned cell, that will raise an exception if that cell could not accept it because of clash. If not, the concerned cell will send to all its neighboors: "*I have now a* `v`, *therefore you cannot accept them anymore*".

## Finding one move

> Here is the cool stuff.

-- Me, 2021     (yes I make my words look cool for theatrical effect)

So, when I have to find one move on a given grid, what do the program do ?

First, a bit of context. We work on a stack of grids, and the "current" grid is the one at the top. (Note for newbies: a stack is a data structure containing a set of ordered object, that we can access in a LIFO fashion: Last In First Out. This behaves like a regular stack of pancakes: you can only add one or take one on the top, but you won't be able to take the second one).

This is motivated because of the way assumption are defined: 
> I try this, and if it doesn't work I get back to where I am now

Here, we remember the *this* and the *where I am now* on two stacks.

1) Look in the set of not yet assigned cells, and find the one that has the less possible values, let's call this cell Cécile.
2) If Cécile has one possible value: assign this value to Cécile
3) Else if Cécile has more than one possibility, copy the current grid and add it on the top of the assuption stack. Then assign to Cécile the least possible value, push the move that has just been made (assigning some value `v` to Cécile) and go on.
4) Aaaargh, Cécile has 0 possible values ! This means that you should have made an incorrect assumption. Then, pop a grid on the stack of grids and the corresponding false assumption. By definition, this move featured a cell that had more than one possible value. Thus, assign to this cell the next possible value.

Note that this stack is erased each time the user modify something on the grid, because there is no way of keeping track of all that shit easily (I just gave up, for short).

## Solving a whole grid

Eh actually, this just finds a move and plays it until the grid is full.

Just like a normal human solves spaghettis.

## GUI

The Graphical User Interface (GUI) mainly consists in:
| Class | Function |
| ----- | -------- |
| `JDigitField`   | Hold a valid digit (1 to 9) and transmit keyboard events to `SudokuGUI` for handling shortcuts |
| `SudokuGUI`     | Hold the representation of the grid as an array of `JDigitField`, and react to user actions |
| `SudokuMenuBar` | Be the menu bar I dreamt of for my super sudoku program. This will be the menu bar displayed in the window |
| `SudokuWindow`  | Hold the window (and actually the entry point of the program), building the `SudokuGUI` linked to its `SudokuMenuBar`. |

GUI sucks, btw.