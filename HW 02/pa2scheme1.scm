(define (palindrome lst)
	(append lst (reverse lst)))

(palindrome (list 'a 'b 'c))