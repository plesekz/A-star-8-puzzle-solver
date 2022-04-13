To run the program:
	1. Navigate to the "8-puzzle.jar" file's location.
	2. run the file with "java -jar 8-puzzle.jar" command.
		The command accepts following optional arguments:
		-s (followed by nine digits [0-8])
			can be used to define a starting state
			the digits represent the tile ID
			the digits are order left to right starting
			with the first line

			Default value:
				724
				302
				164
		-t (followed by nine digits [0-8])
			can be used to define a target state
			the digits represent the tile ID
			the digits are order left to right starting
			with the first line
			
			Default value:
				012
				345
				678

		-d (followed by an int)
			can be used to specify a depth limit.

			Default value = 14

		-h (followed by an int)
			can be used to specify a heuristic to be used.
			Start with 0.
			Add 16 to use Manhattan distance of all tiles as a heuristic.
			Add 08 to use Eucliddean distance of all tiles as a heuristic.
			Add 04 to use Manhattan distance of the empty tiles as a heuristic.
			Add 02 to use Euclidean distance of the empty tiles as a heuristic.
			Add 01 to use the trivial f(n)=0 heuristic.

If the program finishes without running out of memory, it produces a file named after the heuristic used.