import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map
import scala.collection.mutable.HashMap

trait TypedObject {
	def negate : TypedObject
	def toInt : Int
	def class_name : String = {
		this.getClass.getName
	}  
}

class IntObj (var v : Int) extends TypedObject {
	def negate : IntObj = {
		new IntObj(-v)
	}

	def toInt : Int = {
		v
	}
}
class CharObj (var v : Char) extends TypedObject {
	def negate : CharObj = {
		new CharObj(v)
	}

	def toInt : Int = {
		v.toInt
	}
}
class BoolObj (var v : Boolean) extends TypedObject {
	def negate : BoolObj = {
		new BoolObj(!v)
	}

	def toInt : Int = {
		if (v) 1 else 0
	}
}

object Evaluator {

	def do_int_op(i1 : Int, i2 : Int, op : String) : Int = {
		op match {
			case "+" => { i1 + i2 }
			case "-" => { i1 - i2 }
			case "*" => { i1 * i2 }
			case "/" => { i1 / i2 }
			case _   => { println("error") ; 0}
		}
	}

}


class Evaluator (var tree : Program,
				 var env : ArrayBuffer[Map[String, TypedObject]]) {

	def this(tree : Program) {
		this(tree, new ArrayBuffer[Map[String, TypedObject]]())
	}

	def add_env(name : String,
				vtype : String,
				value : TypedObject) {
		env(env.size - 1) += (name -> value)
	}

	def lookup_env(name : String): TypedObject = {
		env(env.size - 1)(name)
	}

	def update_env(name : String,
				   value : TypedObject) {
		env(env.size - 1)(name) = value
	}

	def run() {
		if (!tree.cmd.isInstanceOf[LetCommand]) {
			println("error")
		}

		eval_command(tree.cmd)
	}

	def eval_command(tree : Command) = tree match {
		case let: LetCommand => {
			eval_let_command(let)
		}

		case seq: SequentialCommand => {
			eval_seq_command(seq)
		}

		case asn: AssignCommand => {
			eval_assign_command(asn)
		}

		case call: CallCommand => {
			eval_call_command(call)
		}

		case whle: WhileCommand => {
			eval_while_command(whle)
		}

		case ifcmd: IfCommand => {
			eval_if_command(ifcmd)
		}

		case _ => {
			println("error")
		}
	}

	def eval_declaration(tree : Declaration): Unit = tree match {
		case vr: VarDeclaration => {
			add_env(vr.iden.spelling, vr.typeD.iden.spelling, new IntObj(0))
		}

		case cnst: ConstDeclaration => {
			add_env(cnst.iden.spelling, "Integer", eval_expression(cnst.expr))
		}

		case seq: SequentialDeclaration => {
			eval_declaration(seq.decl1)
			eval_declaration(seq.decl2)
		}

		case _ => {
			println("error")
		}
	}

	def eval_let_command(tree : LetCommand) {
		env += new HashMap[String, TypedObject]
		eval_declaration(tree.decl)
		eval_command(tree.cmd)
		env.remove(env.size - 1)
	}

	def eval_seq_command(tree : SequentialCommand) {
		eval_command(tree.cmd1)
		eval_command(tree.cmd2)
	}

	def eval_assign_command(tree : AssignCommand) {
		update_env(tree.vname.iden.spelling, eval_expression(tree.expr))
	}

	def eval_call_command(tree : CallCommand) {
		tree.iden.spelling match {
			case "putint" => {
				println(eval_expression(tree.expr))
			}

			case "getint" => {
				if (tree.expr.isInstanceOf[VnameExpression]) {
					var e : VnameExpression = tree.expr.asInstanceOf[VnameExpression]
					update_env(e.vname.iden.spelling, 				new IntObj(scala.io.StdIn.readInt()))
				} else {
					println("error")
				}
			}

			case _ => {
				println("error")
			}
		}
	}

	def eval_while_command(tree : WhileCommand) {

		var expr_val : Boolean = true
		
		while (true) {

			expr_val = eval_expression(tree.expr) match {
				case b: BoolObj => {
					b.v
				}

				case _ => {
					println("error: while expression must return a boolean")
					false
				}
			}

			if (!expr_val) {
				return
			}

			eval_command(tree.cmd)

		}
	}

	def eval_if_command(tree : IfCommand) {

		var expr_val : Boolean = eval_expression(tree.expr) match {
			case b: BoolObj => {
				b.v
			}

			case _ => {
				println("error: while expression must return a boolean")
				false
			}
		}

		if (expr_val) {
			eval_command(tree.cmd1)
		} else {
			eval_command(tree.cmd2)
		}
	}

	def eval_expression(tree : Expression) : TypedObject =  tree match {
			
		case i : IntegerExpression => {
			new IntObj(i.value)
		}

		case v : VnameExpression => {
			lookup_env(v.vname.iden.spelling)
		}

		case u : UnaryExpression => {
			u.op.op match {
				case "+" => {
					eval_expression(u.expr)
				}
				case "-" => {
					eval_expression(u.expr).negate
				}
				case _ => {
					println("error: bad unary operator")
					null
				}
			}
		}

		case b : BinaryExpression => {
			var e1 : TypedObject = eval_expression(b.expr1)
			var e2 : TypedObject = eval_expression(b.expr2)

			if (e1.class_name == e2.class_name) {

				b.op.op match {
					case "=" => {
						new BoolObj(e1.toInt == e2.toInt)
					}

					case _ => {
						e1 match {
							case i1 : IntObj => {
								var i2 : IntObj = e2.asInstanceOf[IntObj]

								new IntObj(Evaluator.do_int_op(i1.v, i2.v, b.op.op))
							}

							case c1 : CharObj => {
								var c2 : CharObj = e2.asInstanceOf[CharObj]

								new CharObj(Evaluator.do_int_op(c1.v.toInt, c2.v.toInt, b.op.op).toChar)
							}

							case b1 : BoolObj => {
								var b2 : BoolObj = e2.asInstanceOf[BoolObj]

								new BoolObj(if (Evaluator.do_int_op(b1.toInt, b2.toInt, b.op.op) != 0) true else false)
							}

							case _ => {
								println("error")
								null
							}
						}
					}				
				}

			} else {
				println("error: binary expressions must have same type")
				null
			}
		}

		case _ => {
			println("error")
			null
		}

	}	

}