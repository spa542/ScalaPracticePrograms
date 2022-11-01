from mrjob.job import MRJob
from mrjob.step import MRStep


class MRMostUsedWord(MRJob):

    def steps(self):
        return [
            MRStep(mapper=self.mapper_get_words,
                   reducer=self.reducer_count_words),
            MRStep(reducer=self.reducer_find_max_word)
		]

    def mapper_get_words(self, _, line):
        words = line.split()
        for word in words:
            yield (word.lower(), 1)		# Make a key(word) value(1) pair

    def reducer_count_words(self, word, counts):
        yield None, (sum(counts), word)		# Reduce to Count||word

    def reducer_find_max_word(self, _, count_word_pairs):
        yield max(count_word_pairs)		# max will be from count


if __name__ == '__main__':
    MRMostUsedWord.run()
