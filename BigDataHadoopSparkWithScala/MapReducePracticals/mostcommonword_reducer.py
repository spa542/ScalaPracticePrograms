#!/usr/bin/python

import sys

dict1={}				# Define a dictionary that will contain key value pair. Key will be each word from mapper and value will be 1 as assigned by mapper for each word					
firsttime = "true"				

for line in sys.stdin:
 word,count=line.split(',')		# Split the word and 1 assigned in the mapper into variable word and count

 if(firsttime=="true"):				
  dict1[word]=int(count)		# When coming for first time, dictionary will be empty. Hence just move the (word, count) in dictionary
  firsttime="false"


 wordexist=[key for key in dict1]	# Check if word exists in dictionary
 if(word in wordexist):
   dict1[word]=dict1[word]+int(count)	# If the word exist, just add the count 
 else:
   dict1[word]=int(count)		# If the word does not exist, just move the (word, count) in dictionary


maximum=max(dict1,key=dict1.get)
print(maximum,dict1[maximum])
