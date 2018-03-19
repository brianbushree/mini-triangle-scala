class AST () {

}

class Program (cmd : Command) extends AST {
	
}

class Command () extends AST {
	
}

class AssignCommand (vname : Vname, exp : Expression) extends Command {
	
}

class CallCommand (iden : String, expr : Expression) extends Command {
	
}

class SequentialCommand (cmd1 : Command, cmd2 : Command) extends Command {
	
}

class IfCommand (exp : Expression, cmd1 : Command, cmd2 : Command) extends Command {
	
}

class WhileCommand (exp : Expression, cmd : Command) extends Command {
	
}

class LetCommand (decl : Declaration, cmd : Command) extends Command {
	
}

class Expression () extends AST {
	
}

class IntegerExpression (i : Int) extends Expression {
	
}

class VnameExpression (vname : Vname) extends Expression {
	
}

class UnaryExpression (op : Operator, exp : Expression) extends Expression {
	
}

class BinaryExpression (exp1 : Expression, op : Operator, exp2 : Expression) extends Expression {
	
}

class Vname (name : String) extends AST {
	
}

class Declaration () extends AST {
	
}

class ConstDeclaration (vname : Vname, exp : Expression) extends Declaration {
	
}

class VarDeclaration (vname : Vname, typeD : TypeDenoter) extends Declaration {
	
}

class SequentialDeclaration (decl1 : Declaration, decl2 : Declaration) extends Declaration {

}

class TypeDenoter (type : String) extends AST {

}

class Operator (op : String) extends AST {

}