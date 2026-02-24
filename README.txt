Project 1 – Delphi Interpreter
COP5556 – Spring 2026

Implemented Features:
---------------------
✔ Classes and Objects
✔ Constructors
✔ Encapsulation (public/private)
✔ Integer input and output (readln / writeln)
✔ Java Interpreter using ANTLR4 visitor pattern

Bonus Features:
---------------
Inheritance and Interfaces were attempted but not completed.

How to Compile and Run:
-----------------------

1. Generate parser and lexer:
   antlr -visitor delphi.g4

2. Compile Java files:
   javac *.java

3. Run interpreter with a test file:
   java Main test_class.pas

Example:
   java Main test_constructor.pas

Test Files:
-----------
test_class.pas           → basic class creation
test_object.pas
test_constructor.pas     → constructor execution
test_destructor.pas     
test_encapsulation.pas   → private/public enforcement
test_Input_Output.pas    → input/output testing

Notes:
------
This interpreter supports integer types only.
Object creation is done using:
   obj := ClassName;
Constructors run automatically when object is created.