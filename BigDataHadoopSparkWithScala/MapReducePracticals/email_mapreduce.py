from mrjob.job import MRJob
from mrjob.step import MRStep


class MRTopSenders(MRJob):

  def mapper(self, key, record):
    splits = record.split(",")
    if len(splits) > 21:			# Ignore lines with less then 22 fields on spliting		
      if len(splits[3]) and len(splits[4]):	# Ignote line with from/to as blanks				
        yield (splits[3],splits[4]), 1		# Assign value as 1 to each to, from combination	# (t1,f1 ) , 1 ; # (t1,f1 ) , 1 ; # (t2,f2 ) , 1

  def sum_by_from(self, tofrom, count):
    yield tofrom[0], (sum(count),tofrom[1])	# Sum count on basis of To, From combination		# t1 ,(2, f1) ; t2 ( 1,f2)
  
  def find_top_three(self, to, countfrom):
    topsenders = sorted(countfrom)[0:3]
    fromnames = [ ts[1] for ts in topsenders ]
    yield to, ",".join(fromnames)		# o/p will be TO, FROM1, FROM2, FROM3 for all the senders  

  def hillary_only(self,to,topsenders):
    if to == "H":				# o/p will be H, FROM1, FROM2, FROM3 for all the senders
      yield to, topsenders

      
  def steps(self):
    return [ MRStep(mapper=self.mapper,
		    reducer=self.sum_by_from),
		MRStep(reducer=self.find_top_three),
		MRStep(mapper=self.hillary_only) ]



      
if __name__ == '__main__':
    MRTopSenders.run()
