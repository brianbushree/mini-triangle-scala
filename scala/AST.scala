abstract class AST () {

}

class Program (cmd : Command) extends AST {
	override def toString: String = {
		s"Program(${cmd.toString()})"
	}
}

abstract class Command () extends AST {
	
}

class AssignCommand (vname : Vname, exp : Expression) extends Command {
	override def toString: String = {
		s"AssignCommand(${vname.toString},${exp.toString})"
	}
}

class CallCommand (iden : Identifier, expr : Expression) extends Command {
	override def toString: String = {
		s"CallCommand(${iden.toString},${expr.toString})"
	}
}

class SequentialCommand (cmd1 : Command, cmd2 : Command) extends Command {
	override def toString: String = {
		s"SequentialCommand(${cmd1.toString},${cmd2.toString})"
	}
}

class IfCommand (exp : Expression, cmd1 : Command, cmd2 : Command) extends Command {
	override def toString: String = {
		s"IfCommand(${exp.toString},${cmd1.toString},${cmd2.toString})"
	}
}

class WhileCommand (exp : Expression, cmd : Command) extends Command {
	override def toString: String = {
		s"WhileCommand(${exp.toString},${cmd.toString})"
	}
}

class LetCommand (decl : Declaration, cmd : Command) extends Command {
	override def toString: String = {
		s"LetCommand(${decl.toString},${cmd.toString})"
	}
}

abstract class Expression () extends AST {
	
}

class IntegerExpression (i : Int) extends Expression {
	override def toString: String = {
		s"IntegerExpression(${i.toString})"
	}
}

class VnameExpression (vname : Vname) extends Expression {
	override def toString: String = {
		s"VnameExpression(${vname.toString})"
	}
}

class UnaryExpression (op : Operator, exp : Expression) extends Expression {
	override def toString: String = {
		s"UnaryExpression(${op.toString},${exp.toString})"
	}
}

class BinaryExpression (exp1 : Expression, op : Operator, exp2 : Expression) extends Expression {
	override def toString: String = {
		s"BinaryExpression(${exp1.toString},${op.toString},${exp2.toString})"
	}
}

abstract class Vname () extends AST {
	
}

class SimpleVname (iden : Identifier) extends Vname {
	override def toString: String = {
		s"${iden.toString}"
	}
}

abstract class Declaration () extends AST {
	
}

class ConstDeclaration (vname : Vname, exp : Expression) extends Declaration {
	override def toString: String = {
		s"ConstDeclaration(${vname.toString},${exp.toString})"
	}
}

class VarDeclaration (vname : Vname, typeD : TypeDenoter) extends Declaration {
	override def toString: String = {
		s"VarDeclaration(${vname.toString},${typeD.toString})"
	}
}

class SequentialDeclaration (decl1 : Declaration, decl2 : Declaration) extends Declaration {
	override def toString: String = {
		s"SequentialDeclaration(${decl1.toString},${decl2.toString})"
	}
}

abstract class TypeDenoter extends AST {

}

class SimpleTypeDenoter (iden : Identifier) extends TypeDenoter {
	override def toString: String = {
		s"TypeDenoter(${iden.toString})"
	}
}

abstract class Terminal () extends AST {

}

class Identifier (spelling : String) extends Terminal {
	override def toString: String = {
		s"${spelling}"
	}
}

class Operator (op : String) extends AST {
	override def toString: String = {
		s"${op}"
	}
}