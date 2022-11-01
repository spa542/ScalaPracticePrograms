#!/usr/bin/python
import sys
for line in sys.stdin:
	words = line.split()	#split the line into words
	for word in words:
		print '{0}\t{1}'.format(word,1)
		# write the results to STDOUT (standard output);
        	# what we output here will be the input for the
       	        # Reduce step, i.e. the input for reducer.py
        	#
        	# tab-delimited; the trivial word count is 1
