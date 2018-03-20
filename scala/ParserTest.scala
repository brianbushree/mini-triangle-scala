object ParserTest {

	val test1 = """
let var x: Integer 
in 
    x := 0
"""

	val test1Exp = "Program(LetCommand(VarDeclaration(x,TypeDenoter(Integer)),AssignCommand(Vname(x),IntegerExpression(0))))"


	val test2 = """
! This is a comment.
let
    const m ~ 7;
    var n: Integer
in
    begin
        n := 2 * m * m;
        putint(n)
    end
"""

	val test2Exp = "Program(LetCommand(SequentialDeclaration(ConstDeclaration(m,IntegerExpression(7)),VarDeclaration(n,TypeDenoter(Integer))),SequentialCommand(AssignCommand(Vname(n),BinaryExpression(BinaryExpression(IntegerExpression(2),*,VnameExpression(Vname(m))),*,VnameExpression(Vname(m)))),CallCommand(putint,VnameExpression(Vname(n))))))"


	val test3 = """
! Factorial
let var x: Integer;
    var fact: Integer
in
  begin
    getint(x);
    fact := 1;
    while x > 0 do
      begin
        fact := fact * x;
        x := x - 1
      end;
    putint(fact)
  end
"""

	val test3Exp = "Program(LetCommand(SequentialDeclaration(VarDeclaration(x,TypeDenoter(Integer)),VarDeclaration(fact,TypeDenoter(Integer))),SequentialCommand(SequentialCommand(SequentialCommand(CallCommand(getint,VnameExpression(Vname(x))),AssignCommand(Vname(fact),IntegerExpression(1))),WhileCommand(BinaryExpression(VnameExpression(Vname(x)),>,IntegerExpression(0)),SequentialCommand(AssignCommand(Vname(fact),BinaryExpression(VnameExpression(Vname(fact)),*,VnameExpression(Vname(x)))),AssignCommand(Vname(x),BinaryExpression(VnameExpression(Vname(x)),-,IntegerExpression(1)))))),CallCommand(putint,VnameExpression(Vname(fact))))))"

	def test(input : String, expected : String) : Boolean = {
        var test : String = new Parser(input).parse().toString()
        if (test != expected) {
            println("output:\n" + test)
            println("expected:\n" + expected)
        }
        test == expected
    }

	def main(args: Array[String]) : Unit = {
        try {
            println("test1: " + test(test1, test1Exp))
            println("test2: " + test(test2, test2Exp))
            println("test3: " + test(test3, test3Exp))
            // println("test4: " + test(test4, test4Exp))
        } catch {
            case e: Exception => println(e.toString())
        }
    }
}