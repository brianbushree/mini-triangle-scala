import java.io.ByteArrayInputStream
import scala.collection.mutable.ArrayBuffer
import java.lang.StringBuilder

object Token {
	val TK_IDENTIFIER: Int  = 0
	val TK_INTLITERAL: Int  = 1
	val TK_OPERATOR: Int    = 2
	val TK_BEGIN: Int       = 3  // begin
	val TK_CONST: Int       = 4  // const
	val TK_DO: Int          = 5  // do
	val TK_ELSE: Int        = 6  // else
	val TK_END: Int         = 7  // end
	val TK_IF: Int          = 8  // if
	val TK_IN: Int          = 9  // in
	val TK_LET: Int         = 10 // let
	val TK_THEN: Int        = 11 // then
	val TK_VAR: Int         = 12 // var
	val TK_WHILE: Int       = 13 // while
	val TK_SEMICOLON: Int   = 14 // ;
	val TK_COLON: Int       = 15 // :
	val TK_BECOMES: Int     = 16 // :=
	val TK_IS: Int          = 17 // ~
	val TK_LPAREN: Int      = 18 // (
	val TK_RPAREN: Int      = 19 // )
	val TK_EOT: Int         = 20 // end of text

	var TOKENS = scala.collection.mutable.Map[Int, String]()
	TOKENS += (TK_IDENTIFIER -> "IDENTIFIER")
	TOKENS += (TK_INTLITERAL -> "INTLITERAL")
	TOKENS += (TK_OPERATOR -> "OPERATOR")
	TOKENS += (TK_BEGIN -> "BEGIN")
	TOKENS += (TK_CONST -> "CONST")
	TOKENS += (TK_DO -> "DO")
	TOKENS += (TK_ELSE -> "ELSE")
	TOKENS += (TK_END -> "END")
	TOKENS += (TK_IF -> "IF")
	TOKENS += (TK_IN -> "IN")
	TOKENS += (TK_LET -> "LET")
	TOKENS += (TK_THEN -> "THEN")
	TOKENS += (TK_VAR -> "VAR")
	TOKENS += (TK_WHILE -> "WHILE")
	TOKENS += (TK_SEMICOLON -> "SEMICOLON")
	TOKENS += (TK_COLON -> "COLON")
	TOKENS += (TK_BECOMES -> "BECOMES")
	TOKENS += (TK_IS -> "IS")
	TOKENS += (TK_LPAREN -> "LPAREN")
	TOKENS += (TK_RPAREN -> "RPAREN")
	TOKENS += (TK_EOT -> "EOT")
}

/*  simple Token format  */
class Token (
	val ttype: Int,
	val value: String,
	val pos:   Int
) {
	override def toString: String = {
		s"(${Token.TOKENS(ttype)}(${value}) at ${pos})"
	}
}

/* scanner exception */
class ScannerError (val pos: Int, val char: Char) extends Exception {
	override def toString: String = {
		s"ScannerError at pos = ${pos}, char = ${char}"
	}
}

class Scanner (
	val inputstr: ByteArrayInputStream,
	var eot: Boolean = false,  // end of input
	var pos: Int = 0,          // position in input
	var char: Char = 0.toChar, // current character
	) {

	/* primary String constructor */
	def this(input: String) {
		this(new ByteArrayInputStream(input.getBytes("UTF-8")))
		char_take()
	}

	/**
	 * Main entry point to scanner object.
	 *
	 *  Return a list of Tokens.
	 */
	def scan(): ArrayBuffer[Token] = {

		var tokens: ArrayBuffer[Token] = new ArrayBuffer[Token]()

		var token : Token = null
		do {
			token = scan_token()
			tokens.append(token)
		} while (token.ttype != Token.TK_EOT)

		tokens
	}

	/* Scan a single token from input text. */
	def scan_token(): Token = {

		var token : Token = null
		while (token == null) {

			if (char_eot()) {
				return new Token(Token.TK_EOT, 0.toString, char_pos())
			} else {
				token = match_token(char_current())
			}

		}

		token
	}

	/* Regex helpers */
	val Whitespace = "(\\s)".r
	val Letter = "([a-zA-Z])".r
	val Digit = "(\\d)".r
	val Special = "([~\\(\\);])".r
	val Operators = """([+-\\*/<>=\\])""".r

	/* Match/consume a token */
	def match_token(c : Char): Token = c match {
		case Whitespace(c) => char_take();null
		case '!' => comment()
		case Digit(c) => number()
		case Letter(c) => letter()
		case ':' => colon()
		case Special(c) => special()
		case Operators(c) => operators()
		case _ => throw new ScannerError(char_pos(), char_current()); null
	}

	/* Take special character */
	def special(): Token = {
		var c : Char = char_current()
		val t : Int = c match {
			case '~' => Token.TK_IS
			case '(' => Token.TK_LPAREN
			case ')' => Token.TK_RPAREN
			case ';' => Token.TK_SEMICOLON
		}

		char_take()
		new Token(t, 0.toString, char_pos() - 1)
	}

	/* Take operator */
	def operators(): Token = {
		var c : Char = char_current()
		char_take()
		new Token(Token.TK_OPERATOR, c.toString, char_pos() - 1)
	}

	/* Take comment */
	def comment(): Token = {
		while (!char_eot() && char_current() != '\n') {
			char_take()
		}
		char_take()
		null
	}

	/* Take number */
	def number(): Token = {
		val s : StringBuilder = new StringBuilder()
		var c : Char = char_current()
		while(!char_eot() && c.isDigit) {
			s.append(c)
			char_take()
			c = char_current()
		}
		new Token(Token.TK_INTLITERAL, s.toString(), char_pos() - s.toString().length)
	}

	/* Take identifier or keyword */
	def letter(): Token = {
		val s : StringBuilder = new StringBuilder()
		var c : Char = char_current()
		var key : Token = null

		while(!char_eot() && (c.isLetter || c.isDigit)) {
			s.append(c)
			char_take()
			c = char_current()

			key = keyword(s.toString)
			if (key != null) {
				return key;
			}
		}
		new Token(Token.TK_IDENTIFIER, s.toString(), char_pos() - s.toString().length)
	}

	/* Take keyword */
	def keyword(s : String): Token = s match {
		case "in" => new Token(Token.TK_IN, 0.toString, char_pos() - s.length)
		case "let" => new Token(Token.TK_LET, 0.toString, char_pos() - s.length)
		case "var" => new Token(Token.TK_VAR, 0.toString, char_pos() - s.length)
		case "end" => new Token(Token.TK_END, 0.toString, char_pos() - s.length)
		case "begin" => new Token(Token.TK_BEGIN, 0.toString, char_pos() - s.length)
		case "while" => new Token(Token.TK_WHILE, 0.toString, char_pos() - s.length)
		case "const" => new Token(Token.TK_CONST, 0.toString, char_pos() - s.length)
		case _ => null
	}

	/* Take ':' OR ':=' */ 
	def colon(): Token = {
		char_take()
		char_current() match {
			case '=' => char_take(); new Token(Token.TK_BECOMES, 0.toString, char_pos() - 2)
			case _  => new Token(Token.TK_COLON, 0.toString, char_pos() - 1)
		}
	}

	/* Return in the current input character. */
	def char_current(): Char = char

	/**
	 * Consume the current character and read the 
	 *  next character from the input text.
	 *
	 *  Update self.char, self.eot, and self.pos
	 */
	def char_take() : Char = {

		var char_prev : Char = char

		char = inputstr.read().toChar
		if (char.toInt == 65535) {
			eot = true
		}

		pos += 1

		char_prev
	}

	/* Return the position of the current character in the input text. */
	def char_pos() : Int = pos - 1

	/* Determine if we are at the end of the input text. */
	def char_eot() : Boolean = eot

}




