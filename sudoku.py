# Program to Solve a Soduku Puzze in Python
# By Rhomaios Ram
# 20 Dec 2016
#

# Define Functions
# ---------------------------
def cross(A, B): 
# Cross product of elements in A and elements in B
    "Cross product of elements in A and elements in B."
    return [a+b for a in A for b in B]

#
# --------------------------------------
#
def test():
# A set of unit tests
    "A set of unit tests."
    assert len(squares) == 81
    assert len(unitlist) == 27
    assert all(len(units[s]) == 3 for s in squares)
    assert all(len(peers[s]) == 20 for s in squares)
    assert units['C2'] == [['A2', 'B2', 'C2', 'D2', 'E2', 'F2', 'G2', 'H2', 'I2'],
                           ['C1', 'C2', 'C3', 'C4', 'C5', 'C6', 'C7', 'C8', 'C9'],
                           ['A1', 'A2', 'A3', 'B1', 'B2', 'B3', 'C1', 'C2', 'C3']]
    assert peers['C2'] == set(['A2', 'B2', 'D2', 'E2', 'F2', 'G2', 'H2', 'I2',
                               'C1', 'C3', 'C4', 'C5', 'C6', 'C7', 'C8', 'C9',
                               'A1', 'A3', 'B1', 'B3'])
    print 'All tests pass.'

#
# --------------------------------------
def display(values):
    "Display these values as a 2-D grid."
    width = 1+max(len(values[s]) for s in squares)
    line = '+'.join(['-'*(width*3)]*3)
    for r in rows:
        print ''.join(values[r+c].center(width)+('|' if c in '36' else '')
                      for c in cols)
        if r in 'CF': print line
    print

#
# --------------------------------------

def parse_grid(grid):
    """Convert grid to a dict of possible values, {square: digits}, or
    return False if a contradiction is detected."""
    ## To start, every square can be any digit; then assign values from the grid.
    values = dict((s, digits) for s in squares)
    for s,d in grid_values(grid).items(): 
#  this basically says for each key (s), with value d in the tuple list grid_values()
# Do the next statement.....
		print "values = ", values, '\n'
		print "s = ", s ,'\n'
		print "d = ", d, '\n'
		assign(values, s,d)
#        if d in digits and not assign(values, s, d):
#            return False ## (Fail if we can't assign d to square s.)
    return values

#
# --------------------------------------

def grid_values(grid):
	# This function removes any extraneous characters
    "Convert grid into a dict of {square: char} with '0' or '.' for empties."
    chars = [c for c in grid if c in digits or c in '0.']
    assert len(chars) == 81
    return dict(zip(squares, chars))
#
# --------------------------------------
def assign(values, s, d):
    """Eliminate all the other values (except d) from values[s] and propagate.
    Return values, except return False if a contradiction is detected."""
    other_values = values[s].replace(d, '')
    print "s in assign = ", s,
    print " other_values =", other_values,
    if all(eliminate(values, s, d2) for d2 in other_values):
        return values
    else:
        return False

#
# --------------------------------------
def eliminate(values, s, d):
    """Eliminate d from values[s]; propagate when values or places <= 2.
    Return values, except return False if a contradiction is detected."""
    print "in eliminate: d(2) = ", d
    if d not in values[s]:
        return values ## Already eliminated
    values[s] = values[s].replace(d,'')
    ## (1) If a square s is reduced to one value d2, then eliminate d2 from the peers.
    print "in eliminate .. 2"
    if len(values[s]) == 0:
		return False ## Contradiction: removed last value
    elif len(values[s]) == 1:
        d2 = values[s]
        if not all(eliminate(values, s2, d2) for s2 in peers[s]):
            return False
    print "in eliminate ..3\n"
	## (2) If a unit u is reduced to only one place for a value d, then put it there.
    for u in units[s]:
	dplaces = [s for s in u if d in values[s]]
	if len(dplaces) == 0:
	    return False ## Contradiction: no place for this value
	elif len(dplaces) == 1:
	    # d can only be in one place in unit; assign it there
            if not assign(values, dplaces[0], d):
            	print "assign call from eliminate\n"
                return False
    return values


#
# --------------------------------------
#  Main function

digits   = '123456789'
rows     = 'ABCDEFGHI'
cols     = digits
squares  = cross(rows, cols)
unitlist = ([cross(rows, c) for c in cols] +
            [cross(r, cols) for r in rows] +
            [cross(rs, cs) for rs in ('ABC','DEF','GHI') for cs in ('123','456','789')])
#  units stores key pairs of a square and all the adjacent squares (rows, cols, boxes)
units = dict((s, [u for u in unitlist if s in u]) 
             for s in squares) 
# peers squeezes out all the duplicate adjacencies including the cell itself - so 8 others in box, + 6 others in row + col
# the set function removes all duplicates
# set(s) gives you the cell itself, which must be subtracted from
# sum(units[s], []) which gives you all unduplicated cells.  The complexity of this call is to give right type of 
# output for dict which does not acccept a list as a key (which is what set(units[s] returns))
# the sum function turns the list into a tuple (a read only list.. and therefore hashable)
peers = dict((s, set(sum(units[s],[]))-set([s]))
             for s in squares)
test()
# print squares 
# print '\n'
# print units
# print peers
# print (sum(units[s],[]) for s in squares)

grid1 = "4.....8.5.3..........7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......"
grid2 = """
4 . . |. . . |8 . 5 
. 3 . |. . . |. . . 
. . . |7 . . |. . . 
------+------+------
. 2 . |. . . |. 6 . 
. . . |. 8 . |4 . . 
. . . |. 1 . |. . . 
------+------+------
. . . |6 . 3 |. 7 . 
5 . . |2 . . |. . . 
1 . 4 |. . . |. . . 
"""
print "grid2= "
print '\n'
print grid2
values = parse_grid(grid2)

# display(values)


