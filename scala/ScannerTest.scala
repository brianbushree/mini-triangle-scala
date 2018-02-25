object ScannerTest {

    val test1 : String = """
let var x: Integer 
in 
    x := 0
"""

    val test1Exp : String = """ArrayBuffer((LET(0) at 1), (VAR(0) at 5), (IDENTIFIER(x) at 9), (COLON(0) at 10), (IDENTIFIER(Integer) at 12), (IN(0) at 21), (IDENTIFIER(x) at 29), (BECOMES(0) at 31), (INTLITERAL(0) at 34), (EOT(0) at 36))"""

    val test2 : String = """
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

    val test2Exp : String = """ArrayBuffer((LET(0) at 22), (CONST(0) at 30), (IDENTIFIER(m) at 36), (IS(0) at 38), (INTLITERAL(7) at 40), (SEMICOLON(0) at 41), (VAR(0) at 47), (IDENTIFIER(n) at 51), (COLON(0) at 52), (IDENTIFIER(Integer) at 54), (IN(0) at 62), (BEGIN(0) at 69), (IDENTIFIER(n) at 83), (BECOMES(0) at 85), (INTLITERAL(2) at 88), (OPERATOR(*) at 90), (IDENTIFIER(m) at 92), (OPERATOR(*) at 94), (IDENTIFIER(m) at 96), (SEMICOLON(0) at 97), (IDENTIFIER(putint) at 107), (LPAREN(0) at 113), (IDENTIFIER(n) at 114), (RPAREN(0) at 115), (END(0) at 121), (EOT(0) at 125))"""

    val test3 : String = "foo bar22 33 474747 + - * / < > = \\ ; : := ~ ( )"

    val test3Exp : String = """ArrayBuffer((IDENTIFIER(foo) at 0), (IDENTIFIER(bar22) at 4), (INTLITERAL(33) at 10), (INTLITERAL(474747) at 13), (OPERATOR(+) at 20), (OPERATOR(-) at 22), (OPERATOR(*) at 24), (OPERATOR(/) at 26), (OPERATOR(<) at 28), (OPERATOR(>) at 30), (OPERATOR(=) at 32), (OPERATOR(\) at 34), (SEMICOLON(0) at 36), (COLON(0) at 38), (BECOMES(0) at 40), (IS(0) at 43), (LPAREN(0) at 45), (RPAREN(0) at 47), (EOT(0) at 48))"""

    val test4 : String = """
! Factorial
let var x: Integer
    var fact: Integer
in
    begin
        fact := 1
        while x > 0:
            fact := fact * x
            x := x - 1
        putint(fact)
    end
"""

    val test4Exp : String = """ArrayBuffer((LET(0) at 13), (VAR(0) at 17), (IDENTIFIER(x) at 21), (COLON(0) at 22), (IDENTIFIER(Integer) at 24), (VAR(0) at 36), (IDENTIFIER(fact) at 40), (COLON(0) at 44), (IDENTIFIER(Integer) at 46), (IN(0) at 54), (BEGIN(0) at 61), (IDENTIFIER(fact) at 75), (BECOMES(0) at 80), (INTLITERAL(1) at 83), (WHILE(0) at 93), (IDENTIFIER(x) at 99), (OPERATOR(>) at 101), (INTLITERAL(0) at 103), (COLON(0) at 104), (IDENTIFIER(fact) at 118), (BECOMES(0) at 123), (IDENTIFIER(fact) at 126), (OPERATOR(*) at 131), (IDENTIFIER(x) at 133), (IDENTIFIER(x) at 147), (BECOMES(0) at 149), (IDENTIFIER(x) at 152), (OPERATOR(-) at 154), (INTLITERAL(1) at 156), (IDENTIFIER(putint) at 166), (LPAREN(0) at 172), (IDENTIFIER(fact) at 173), (RPAREN(0) at 177), (END(0) at 183), (EOT(0) at 187))"""

    def test(input : String, expected : String) : Boolean = {
        var test : String = new Scanner(input).scan().toString()
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
            println("test4: " + test(test4, test4Exp))
        } catch {
            case e: Exception => println(e.toString())
        }
    }
}