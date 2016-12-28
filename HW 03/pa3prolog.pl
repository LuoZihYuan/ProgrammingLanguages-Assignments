/* Facts */
male(mark).
male(mel).
male(richard).
male(tom).
male(adam).

female(amy).
female(jane).
female(joan).
female(betty).
female(rosa).
female(fran).

parent(mel, joan).
parent(jane, betty).
parent(jane, tom).
parent(richard, adam).
parent(richard, rosa).
parent(joan, fran).
parent(mark, jane).
parent(mark, richard).
parent(amy, jane).
parent(amy, richard).
parent(amy, joan).

/* Clause */
mother(X, Y) :-
	parent(X, Y),
	female(X).
	
father(X, Y) :-
	parent(X, Y),
	male(X).

child(X, Y) :-
	parent(Y, X).

sibling(X, Y) :-
	parent(Z, X),
	parent(Z, Y),
	X \= Y. % X's sibling will not be X, same applies to Y.

sister(X, Y) :-
	sibling(X, Y),
	female(X).

brother(X, Y) :-
	sibling(X, Y),
	male(X).

daughter(X, Y) :-
	parent(Y, X),
	female(X).

son(X, Y) :-
	parent(Y, X),
	male(X).

uncle(X, Y) :-
	parent(Z, Y),
	brother(X, Z).

aunt(X, Y) :-
	parent(Z, Y),
	sister(X, Z).

cousin(X, Y) :-
	parent(I, X),
	parent(J, Y),
	sibling(I, J),
	X \= Y. % X's cousin will not be X, same applies to Y.