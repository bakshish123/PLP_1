Project 1 – Delphi Interpreter
COP5556 – Spring 2026

Team Members
Bakshish Singh 8930-8993
Ashmit Sharma 2838-1009

Implemented Features:
---------------------
✔ Classes and Objects  
✔ Constructors  
✔ Destructors  
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
test_object.pas          → object instantiation  
test_constructor.pas     → constructor execution  
test_destructor.pas      → destructor behavior  
test_encapsulation.pas   → private/public enforcement  
test_Input_Output.pas    → input/output testing  


Important Notes for TA:
-----------------------

**1. For test_Input_Output.pas:**
When running this test, the program will pause and wait for input.
You must enter an integer number into the terminal.
The program will then print the same number back to the terminal.
This demonstrates correct intake and output functionality.

**2. For test_encapsulation.pas:**
This test intentionally attempts to access a private field or method.
The program will throw an error or terminate.
This behavior is expected and demonstrates that encapsulation
(access modifiers) is enforced correctly by the interpreter.


Notes:
------
• This interpreter supports integer types only.  
• Object creation is done using:
     obj := ClassName;
• Constructors run automatically when the object is created.
