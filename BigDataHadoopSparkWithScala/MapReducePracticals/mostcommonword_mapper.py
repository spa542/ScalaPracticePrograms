#!/usr/bin/python

import sys
for line in sys.stdin:
	words = line.split() 
	for word in words:
		print '{0},{1}'.format(word.lower(),1)		# Make a key(word) value(1) pair
