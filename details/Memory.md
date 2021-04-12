# Assessment of memory needs

Warning: this may become more technical (but well it does not mean that it is ununderstandable for an other reason that my explaination clarity)

- `&` denotes a reference
- `[n]` denotes an array of size `n`
- `<A, B>` denotes a tuple of types `A` and `B`

## Size of one cell

Code of `Cell` class [here](../Cell.java).

| Variable  | Description                      | Type         | Size  in bytes |
| --------- | -------------------------------- | ------------ | -------------- |
| `v`       | The value of that cell           | `int`        | 4              |
| `line`    | The `Cell`s on the same line     | `&Cell[8]`   | 8×8 = 64       |
| `column`  | The `Cell`s on the same column   | `&Cell[8]`   | 8×8 = 64       |
| `block`   | The `Cell`s on the same block    | `&Cell[8]`   | 8×8 = 64       |
| `accepts` | Accepted values in that `Cell`   | `boolean[9]` | 9×1 = 9        |
| total     |                                  |              | 205            |

## Size of one grid

Code of `SudokuGrid` class [here](../SudokuGrid.java)

| Variable      | Description                           | Type                 | Size  in bytes     |
| ------------- | ------------------------------------- | -------------------- | ------------------ |
| `cells`       | The `Cell`s in the grid               | `Cell[9][9]`         | 9×9×205 = 16605    |
| `notSetCells` | The map of not set cells (worst case) | `<&Cell, Move>[9×9]` | 9×9×(6+3×4) = 1458 |
| `grid`        | The helper for ascii display          | `Grid`               | 530 (irrelevant computation skipped) |
| total         |                                       |                      | 18593              |

## Maximum size of stack

By design (see [explainations here](Explaination-full.md)), the stack of grids is incremented at each assupmtion.

So, if we want to solve a grid that is initially empty (who never thought that you could create sudoku grids yourself ?), we may have to make a lot of assumptions. But how many of them ?

1) 81
2) Each end of line does not constitute an assumption → -9 → 72
3) The last line does not requires any assumption (because of columns) → -8 (beware of the one we already counted on point 2.) → 64
4) Each last cell of block does not requires any assumption → -4 → 60
I say 4 because (I think that) the worst case is when you fill the grid line by line. Thus, the last element of the block up right will be at the end of third line, so that it has already been counted in 2. Same for the block middle right with sixth line. And again for bottom blocks and the last line. Remains 4: up left, up middle, middle left and middle middle.

So that my guess is that this program would at most use: 

```
60 * 18593 = 1115580 bytes = 1.115 Mo
```

Btw, empirical test shown stack sizes varying (what we could have expected: it depends on the grid "geometry") from 46 to 51.

This is an upper bound: the `notSetCell` variable will not be full that often, and may be averaged to half its maximum size.