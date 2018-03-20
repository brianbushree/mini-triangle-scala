object ParserTest {

	val test1 = """
let var x: Integer 
in 
    x := 0
"""

	val test_1_tree = "Program(LetCommand(VarDeclaration(x,TypeDonoter(Integer)),AssignCommand(Vname(x),IntegerExpression(0))))"

	def test(input : String, expected : String) : Boolean = {
        var test : String = new Parser(input).parse().toString()
        if (test != expected) {
            println("output:\n" + test)
            println("expected:\n" + expected)
        }
        test == expected
    }

	def main(args: Array[String]) : Unit = {
		var p : Parser = new Parser(input)
		println(p.parse().toString())
    }
}