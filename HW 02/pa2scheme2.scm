(define (dbl_atm atom lst)
	(cond 
		((= (length lst) 0) (list ))
		((list? (car lst)) 
				(append (list (dbl_atm atom (car lst))) (dbl_atm atom (cdr lst))))
		((equal? atom (car lst)) 
				(append (list atom atom) (dbl_atm atom (cdr lst))))
		(else (append (list (car lst)) (dbl_atm atom (cdr lst))))))

(dbl_atm 'a (list 'a (list 'b 'c 'a (list 'a 'd))))