abstract class AST () {

}

class Program (val cmd : Command) extends AST {
	override def toString: String = {
		s"Program(${cmd.toString()})"
	}
}

abstract class Command () extends AST {
	
}

class AssignCommand (val vname : Vname, val expr : Expression) extends Command {
	override def toString: String = {
		s"AssignCommand(${vname.toString},${expr.toString})"
	}
}

class CallCommand (val iden : Identifier, val expr : Expression) extends Command {
	override def toString: String = {
		s"CallCommand(${iden.toString},${expr.toString})"
	}
}

class SequentialCommand (val cmd1 : Command, val cmd2 : Command) extends Command {
	override def toString: String = {
		s"SequentialCommand(${cmd1.toString},${cmd2.toString})"
	}
}

class IfCommand (val expr : Expression, val cmd1 : Command, val cmd2 : Command) extends Command {
	override def toString: String = {
		s"IfCommand(${expr.toString},${cmd1.toString},${cmd2.toString})"
	}
}

class WhileCommand (val expr : Expression, val cmd : Command) extends Command {
	override def toString: String = {
		s"WhileCommand(${expr.toString},${cmd.toString})"
	}
}

class LetCommand (val decl : Declaration, val cmd : Command) extends Command {
	override def toString: String = {
		s"LetCommand(${decl.toString},${cmd.toString})"
	}
}

abstract class Expression () extends AST {
	
}

class IntegerExpression (val value : Int) extends Expression {
	override def toString: String = {
		s"IntegerExpression(${value.toString})"
	}
}

class VnameExpression (val vname : Vname) extends Expression {
	override def toString: String = {
		s"VnameExpression(${vname.toString})"
	}
}

class UnaryExpression (val op : Operator, val expr : Expression) extends Expression {
	override def toString: String = {
		s"UnaryExpression(${op.toString},${expr.toString})"
	}
}

class BinaryExpression (val expr1 : Expression, val op : Operator, val expr2 : Expression) extends Expression {
	override def toString: String = {
		s"BinaryExpression(${expr1.toString},${op.toString},${expr2.toString})"
	}
}

abstract class Vname (val iden : Identifier) extends AST {
	
}

class SimpleVname (iden : Identifier) extends Vname(iden) {
	override def toString: String = {
		s"Vname(${iden.toString})"
	}
}

abstract class Declaration () extends AST {
	
}

class ConstDeclaration (val iden : Identifier, val expr : Expression) extends Declaration {
	override def toString: String = {
		s"ConstDeclaration(${iden.toString},${expr.toString})"
	}
}

class VarDeclaration (val iden : Identifier, val typeD : TypeDenoter) extends Declaration {
	override def toString: String = {
		s"VarDeclaration(${iden.toString},${typeD.toString})"
	}
}

class SequentialDeclaration (val decl1 : Declaration, val decl2 : Declaration) extends Declaration {
	override def toString: String = {
		s"SequentialDeclaration(${decl1.toString},${decl2.toString})"
	}
}

abstract class TypeDenoter (val iden : Identifier) extends AST {

}

class SimpleTypeDenoter (iden : Identifier) extends TypeDenoter(iden) {
	override def toString: String = {
		s"TypeDenoter(${iden.toString})"
	}
}

abstract class Terminal () extends AST {

}

class Identifier (val spelling : String) extends Terminal {
	override def toString: String = {
		s"${spelling}"
	}
}

class Operator (val op : String) extends AST {
	override def toString: String = {
		s"${op}"
	}
}