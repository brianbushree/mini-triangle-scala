import scala.collection.mutable.ArrayBuffer

class Parser (var tokens : ArrayBuffer[Token],
			  var pos: Int = 0,
			  var currentToken: Token = null,
			  ) {

	def this(input : String) {
		this(new Scanner(input).scan())
		currentToken = tokens(pos)
	}

	def acceptCurrent() {
		pos = pos + 1
		currentToken = tokens(pos)
	}

	def acceptToken(expect : Int) {
		if (currentToken.ttype == expect) {
			acceptCurrent()
		} else {
			throw new IllegalArgumentException(s"expecting ${Token.TOKENS(expect)}, found ${Token.TOKENS(currentToken.ttype)} at pos ${pos}\n${currentToken.toString}")
		}
	}

	def parse(): Program = {
		var prog : Program = parseProgram()
		if (currentToken.ttype != Token.TK_EOT) {
			throw new IllegalArgumentException(s"expecting ${Token.TOKENS(Token.TK_EOT)}, found ${Token.TOKENS(currentToken.ttype)} at pos ${pos}")
		}
		prog
	}

	def parseProgram(): Program = {
		new Program(parseCommand())
	}

	def parseCommand(): Command = {
		var cmd1 : Command = parseSingleCommand()

		while (currentToken.ttype == Token.TK_SEMICOLON) {
			acceptCurrent()
			var cmd2 : Command = parseSingleCommand()
			cmd1 = new SequentialCommand(cmd1, cmd2)
		}

		cmd1
	}

	def parseSingleCommand(): Command = currentToken.ttype match {

		case Token.TK_IDENTIFIER => {
			var iden : Identifier = parseIdentifier()

			currentToken.ttype match {

				case Token.TK_BECOMES => {
					acceptCurrent()
					var expr : Expression = parseExpression()
					new AssignCommand(new SimpleVname(iden), expr)
				}

				case Token.TK_LPAREN => {
					acceptCurrent()
					var expr : Expression = parseExpression()
					acceptToken(Token.TK_RPAREN)
					new CallCommand(iden, expr)
				}

				case _ => {
					throw new IllegalArgumentException(s"111error parsing command, unexpected ${currentToken.ttype} at position ${pos}")
					null
				}
			}

		}
		case Token.TK_IF => {
			acceptCurrent()
			var expr : Expression = parseExpression()
			acceptToken(Token.TK_THEN)
			var cmd1 : Command = parseSingleCommand()
			acceptToken(Token.TK_ELSE)
			var cmd2 : Command = parseSingleCommand()
			new IfCommand(expr, cmd1, cmd2)
		}
		case Token.TK_WHILE => {
			acceptCurrent()
			var expr : Expression = parseExpression()
			acceptToken(Token.TK_DO)
			var cmd : Command = parseSingleCommand()
			new WhileCommand(expr, cmd)
		}
		case Token.TK_LET => {
			acceptCurrent()
			var decl : Declaration = parseDeclaration()
			acceptToken(Token.TK_IN)
			var cmd : Command = parseSingleCommand()
			new LetCommand(decl, cmd)
		}
		case Token.TK_BEGIN => {
			acceptCurrent()
			var cmd : Command = parseCommand()
			acceptToken(Token.TK_END)
			cmd
		}
		case _ => {
			throw new IllegalArgumentException(s"222error parsing command, unexpected ${currentToken.ttype} at position ${pos}")
			null
		}
	}

	def parseExpression(): Expression = {
		var expr1 : Expression = parseSingleExpression()

		while (currentToken.ttype == Token.TK_OPERATOR) {
			var op : Operator = parseOperator()
			var expr2 : Expression = parseSingleExpression()
			expr1 = new BinaryExpression(expr1, op, expr2)
		}

		expr1
	}

	def parseSingleExpression(): Expression = currentToken.ttype match {
		
		case Token.TK_INTLITERAL => {
			var i : Int = parseIntegerLiteral()
			new IntegerExpression(i)
		}

		case Token.TK_IDENTIFIER => {
			var iden : Identifier = parseIdentifier()
			new VnameExpression(new SimpleVname(iden))		
		}

		case Token.TK_OPERATOR => {
			var op : Operator = parseOperator()
			var expr : Expression = parseExpression()
			new UnaryExpression(op, expr)
		}

		case _ => {
			throw new IllegalArgumentException(s"error parsing expression, unexpected ${currentToken.ttype} at position ${pos}")
			null
		}
	}

	def parseDeclaration(): Declaration = {
		var decl1 : Declaration = parseSingleDeclaration()

		while (currentToken.ttype == Token.TK_SEMICOLON) {
			acceptCurrent()
			var decl2 : Declaration = parseSingleDeclaration()
			decl1 = new SequentialDeclaration(decl1, decl2)
		}

		decl1
	}

	def parseSingleDeclaration(): Declaration = currentToken.ttype match {
		
		case Token.TK_CONST => {
			acceptCurrent()
			var iden : Identifier = parseIdentifier()
			acceptToken(Token.TK_IS)
			var expr : Expression = parseExpression()
			new ConstDeclaration(iden, expr)
		}

		case Token.TK_VAR => {
			acceptCurrent()
			var iden : Identifier = parseIdentifier()
			acceptToken(Token.TK_COLON)
			var typeD : TypeDenoter = parseTypeDenoter()
			new VarDeclaration(iden, typeD)
		}

		case _ => {
			throw new IllegalArgumentException(s"error parsing declaration, unexpected ${currentToken.ttype} at position ${pos}")
			null
		}

	}

	def parseTypeDenoter(): TypeDenoter = {

		var iden : Identifier = parseIdentifier()
		new SimpleTypeDenoter(iden)

	}

	def parseIdentifier(): Identifier = currentToken.ttype match {

		case Token.TK_IDENTIFIER => {
			var value : String = currentToken.value
			acceptCurrent()
			new Identifier(value)
		}

		case _ => {
			throw new IllegalArgumentException(s"error parsing identifier, unexpected ${currentToken.ttype} at position ${pos}")
			null
		}
		
	}

	def parseIntegerLiteral(): Int = currentToken.ttype match {

		case Token.TK_INTLITERAL => {
			var value : String = currentToken.value
			acceptCurrent()
			value.toInt
		}

		case _ => {
			throw new IllegalArgumentException(s"error parsing integer literal, unexpected ${currentToken.ttype} at position ${pos}")
			0
		}
	}

	def parseOperator(): Operator = currentToken.ttype match {

		case Token.TK_OPERATOR => {
			var value : String = currentToken.value
			acceptCurrent()
			new Operator(value)
		}

		case _ => {
			throw new IllegalArgumentException(s"error parsing operator, unexpected ${currentToken.ttype} at position ${pos}")
			null
		}

	}

}