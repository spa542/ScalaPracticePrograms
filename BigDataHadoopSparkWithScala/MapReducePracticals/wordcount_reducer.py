#!/usr/bin/python
import sys

prev_word = None
prev_count = 0
word = None

# Process each key-value pair from the mapper
for line in sys.stdin:
   # Get the key and value from the current line
   line = line.strip()            #remove leading and trailing whitespace
   word, count = line.split('\t') 
   count = int(count)             # Convert the count to an int
  
   # If the current word is the same as the previous word,
   # increment its count, otherwise print the words count
   # to stdout
   if word == prev_word:
      prev_count += count
   else:
      # Write word and its number of occurrences as a key-value
      # pair to stdout
      if prev_word:
      	print '{0}\t{1}'.format(prev_word, prev_count)
      prev_word = word
      prev_count = count

# Output the count for the last word
if prev_word == word:	
  	print '{0}\t{1}'.format(prev_word, prev_count)
