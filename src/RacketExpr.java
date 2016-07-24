import java.util.ArrayList;

public class RacketExpr {
	
	private static class ExprNode {
		public enum ExprType {
			OPERATOR,
			FUNCTION,
			VARIABLE,
			NUMBER,
			BOOLEAN
		};
		
		public ExprType type;
		
		public char operator;
		public String function;
		public String variable;
		public double number;
		public boolean bool;
		
		public ExprNode left;
		public ExprNode right;
		
		public ExprNode(char operator) {
			this.type = ExprType.OPERATOR;
			this.operator = operator;
			this.left = null;
			this.right = null;
		}
		
		public ExprNode(String name, boolean isFunc) {
			if (isFunc) {
				this.type = ExprType.FUNCTION;
				this.function = name;
				this.variable = null;
			} else {
				this.type = ExprType.VARIABLE;
				this.function = null;
				this.variable = name;
			}
			this.left = null;
			this.right = null;
		}
		
		public ExprNode(double num) {
			this.type = ExprType.NUMBER;
			this.number = num;
			this.left = null;
			this.right = null;
		}
		
		public ExprNode(boolean bool) {
			this.type = ExprType.BOOLEAN;
			this.bool = bool;
			this.left = null;
			this.right = null;
		}
		
		public void print() {
			System.out.println("{" + type + ", " + operator + ", " 
					+ function + ", " + variable + ", " + number + "}");
			if (left != null) {
				System.out.print("LEFT: ");
				left.print();
			}
			if (right != null) {
				System.out.print("RIGHT: ");
				right.print();
			}
		}
		
		public double interpNum(Dictionary constants) {
			double arg1 = 0;
			double arg2 = 0;
			
			if (left != null) arg1 = left.interpNum(constants);
			if (right != null) arg2 = right.interpNum(constants);
			
			switch (type) {
				case NUMBER:
					return number;
				case VARIABLE:
					return constants.lookup(variable);
				case OPERATOR:
					if (operator == '+') {
						return Functions.add.apply(arg1, arg2);
					} else if (operator == '-') {
						return Functions.sub.apply(arg1, arg2);
					} else if (operator == '*') {
						return Functions.mult.apply(arg1, arg2);
					} else if (operator == '/') {
						return Functions.quot.apply(arg1, arg2);
					} else if (operator == '=') {
						throw new IllegalArgumentException();
					}
				case FUNCTION:
					if (function.compareTo("mod") == 0) {
						return Functions.mod.apply(arg1, arg2);
					} else if (function.compareTo("max") == 0) {
						return Functions.max.apply(arg1, arg2);
					} else if (function.compareTo("min") == 0) {
						return Functions.min.apply(arg1, arg2);
					} else if (function.compareTo("abs") == 0) {
						return Functions.abs.apply(arg1);
					} else if (function.compareTo("sin") == 0) {
						return Functions.sin.apply(arg1);
					} else if (function.compareTo("cos") == 0) {
						return Functions.cos.apply(arg1);
					} else if (function.compareTo("tan") == 0) {
						return Functions.tan.apply(arg1);
					} else if (function.compareTo("sqr") == 0) {
						return Functions.sqr.apply(arg1);
					} else if (function.compareTo("sqrt") == 0) {
						return Functions.sqrt.apply(arg1);
					} else if (function.compareTo("expt") == 0) {
						return Functions.expt.apply(arg1, arg2);
					} else if (function.compareTo("exp") == 0) {
						return Functions.exp.apply(arg1);
					}
			}
			
			return Double.MIN_VALUE;
		}
		
		public boolean interpBool(Dictionary constants) {
			
			switch (type) {
				case BOOLEAN:
						return bool;
				case FUNCTION:
					if (Functions.numBoolFunc.contains(function)) {
						double arg1 = 0;
						double arg2 = 0;
						
						if (left != null) arg1 = left.interpNum(constants);
						if (right != null) arg2 = right.interpNum(constants);
						
						if (function.compareTo("=") == 0) {
							return Functions.dblEqual.test(arg1, arg2);
						} else if (function.compareTo("<") == 0) {
							return Functions.lessThan.test(arg1, arg2);
						} else if (function.compareTo(">") == 0) {
							return Functions.greaterThan.test(arg1, arg2);
						} else if (function.compareTo("<=") == 0) {
							return Functions.lessThanEql.test(arg1, arg2);
						} else if (function.compareTo(">=") == 0) {
							return Functions.greaterThanEql.test(arg1, arg2);
						}
					} else {
						boolean arg1 = false;
						boolean arg2 = false;
						
						if (left != null) arg1 = left.interpBool(constants);
						if (right != null) arg2 = right.interpBool(constants);
						
						if (function.compareTo("not") == 0) {
							return Functions.not.test(arg1);
						} else if (function.compareTo("or") == 0) {
							return Functions.or.test(arg1, arg2);
						} else if (function.compareTo("and") == 0) {
							return Functions.and.test(arg1, arg2);
						}
					}
				default:
					throw new IllegalArgumentException();
			}
		}
	}
	

	public enum ReturnType {
		DOUBLE,
		BOOLEAN
	};
	
	private ReturnType retType;
	private ExprNode root;
	
	public RacketExpr() {
		root = null;
		retType = null;
	}
	
	public ReturnType getReturnType() {
		return retType;
	}
	
	private static ExprNode parseExpr(ArrayList<String> parsedTokens, int index) {
		final int len = parsedTokens.size();
		int pos = index;
		String token;
		ExprNode node = null;
		
		while (pos < len) {
			token = parsedTokens.get(pos);
			//System.out.println(token + ", " + FuncTree.boolFunc.contains(token));
			
			if (token.compareTo("(") == 0 || token.compareTo(")") == 0) {
				++pos;
			} else if (token.compareTo("+") == 0 || token.compareTo("-") == 0 ||
					   token.compareTo("*") == 0 || token.compareTo("/") == 0) {
			    char operator = token.charAt(0);
				node = new ExprNode(operator);
				
				++pos;
				node.left = parseExpr(parsedTokens, pos);
				
				if (parsedTokens.get(pos).charAt(0) == '(') {
					while (parsedTokens.get(pos).charAt(0) != ')') {
						++pos;
					}
				}
				
				++pos;
				node.right = parseExpr(parsedTokens, pos);
				
				return node;
			} else if (Character.isDigit(token.charAt(0))) {
				double number = Double.parseDouble(token);
				node = new ExprNode(number);
				return node;
			} else if (Character.isAlphabetic(token.charAt(0))) {
				String var = token;
				
				if (var.compareTo("true") == 0 ||
					var.compareTo("false") == 0) {
					node = new ExprNode(Boolean.valueOf(var));
					return node;
				} else if (Functions.unaryFunc.contains(var)) {
					node = new ExprNode(var, true);
					
					++pos;
					node.left = parseExpr(parsedTokens, pos);
					node.right = null;
				} else if (Functions.binaryFunc.contains(var) ||
						   Functions.boolBoolFunc.contains(var)) {
					node = new ExprNode(var, true);
					
					++pos;
					node.left = parseExpr(parsedTokens, pos);
					
					if (parsedTokens.get(pos).charAt(0) == '(') {
						while (parsedTokens.get(pos).charAt(0) != ')') {
							++pos;
						}
					}
					
					++pos;
					node.right = parseExpr(parsedTokens, pos);
				} else {
					node = new ExprNode(var, false);
				}
				
				return node;
			} else if (Functions.numBoolFunc.contains(token)) {
				node = new ExprNode(token, true);
				
				++pos;
				node.left = parseExpr(parsedTokens, pos);
				
				if (parsedTokens.get(pos).charAt(0) == '(') {
					while (parsedTokens.get(pos).charAt(0) != ')') {
						++pos;
					}
				}
				
				++pos;
				node.right = parseExpr(parsedTokens, pos);
				
				return node;
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		return node;
	}
	
	public RacketExpr(ArrayList<String> tokens) {
		root = parseExpr(tokens, 0);
		
		if (root.type == ExprNode.ExprType.BOOLEAN ||
			Functions.numBoolFunc.contains(root.function) ||
			Functions.boolBoolFunc.contains(root.function)) {
			retType = ReturnType.BOOLEAN;
		} else {
			retType = ReturnType.DOUBLE;
		}
	}
	
	public boolean interpBool(Dictionary constants) {
		return root.interpBool(constants);
	}
	
	public double interpNum(Dictionary constants) {
		return root.interpNum(constants);
	}
	
	public static void addConst(ArrayList<String> tokens, Dictionary constants) {
		String key = tokens.get(2);
		double val = parseExpr(tokens, 3).interpNum(constants);
		
		constants.add(key, val);
	}
	
	public void print() {
		System.out.println("RETURN TYPE: " + retType);
		root.print();
	}
}
