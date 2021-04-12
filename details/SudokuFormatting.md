# Sudoku formatting

Spoiler alert: if you read this you will feel like I think you're dumb. I wrote this for myself becase I actually *am* sometimes...

The string representation of a sudoku grid is straightforward:

```
1 2 3 4 5 6 7 8 9
4 5 6 7 8 9 1 2 3
7 8 9 1 2 3 4 5 6
2 3 4 5 6 7 8 9 1
5 6 7 8 9 1 2 3 4
8 9 1 2 3 4 5 6 7
3 4 5 6 7 8 9 1 2
6 7 8 9 1 2 3 4 5
9 1 2 3 4 5 6 7 8

1 2 3 4 5 6 7 8 9
4 5 6 7 8 - 1 2 3
7 - 9 1 2 3 4 5 6
2 3 - 5 6 - 8 9 1
5 6 7 8 9 1 2 3 4
8 9 1 2 3 4 5 - -
3 4 5 6 7 8 - - -
6 7 8 9 1 2 3 - 5
9 1 2 3 4 5 6 - 8
```

are correct and valid. 

- This handles multiple spaces between values.

- This does not handle multiple newlines between rows

- Non-numeric values are understook as empty cells: you can use whatever you want, '.', '-', '_', 'x', but no space (obviously).

- Inconsistent grids will throw an exception. E.g.:
```
1 1 . . .
. . .
```