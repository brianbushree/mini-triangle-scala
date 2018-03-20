object ParserTest {

	val input = """
let var x: Integer 
in 
    x := 0
"""

	def main(args: Array[String]) : Unit = {
		var p : Parser = new Parser(input)
		println(p.parse().toString())
    }
}