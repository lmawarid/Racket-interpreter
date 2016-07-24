import java.util.ArrayList;
import java.util.Scanner;

public class Interpreter {
	
	private static boolean isBlank(String str) {
		final int len = str.length();
		
		for (int i = 0; i < len; ++i) {
			if (str.charAt(i) != ' ') return false;
		}
		
		return true;
	}
	
	private static ArrayList<String> parse(String expr, int[] parens) {
		ArrayList<String> tokens = new ArrayList<String>();
		
		final int len = expr.length();
		int pos = 0;
		
		StringBuilder token = new StringBuilder();
		
		while (pos < len) {
			char chr = expr.charAt(pos);
			
			if (Character.isWhitespace(chr)) {
				++pos;
			} else if (chr == '(' || chr == ')') {
				if (chr == '(') ++parens[0];
				else ++parens[1];
				
				String paren = Character.toString(chr);
				tokens.add(paren);
				++pos;
			} else if (Character.isDigit(chr)) {
				while (Character.isDigit(chr) || chr == '.') {
					token.append(chr);
					++pos;
					
					if (pos >= len) break;
					chr = expr.charAt(pos);
				}
				
				tokens.add(token.toString());
				token.setLength(0);
			} else {
				while (!Character.isWhitespace(chr) && chr != ')') {
					token.append(chr);
					++pos;
					
					if (pos >= len) break;
					chr = expr.charAt(pos);
				}
				
				tokens.add(token.toString());
				token.setLength(0);
			}
		}
		
		return tokens;
	}
	
	private static void printTokens(ArrayList<String> tokens) {
		System.out.print("[ ");
		for (String token : tokens) {
			System.out.print(token + " ");
		}
		System.out.println("] ");
	}

	public static void main(String[] args) {
		Dictionary constants = new Dictionary();
		Scanner in = new Scanner(System.in);
		ArrayList<String> parsedStream = new ArrayList<String>();
		int[] parens = {0, 0};
		String expr;
		
		
		do {
			expr = in.nextLine();
			ArrayList<String> tokens;
			
			if (!isBlank(expr)) {
				tokens = parse(expr, parens);
				// printTokens(tokens);
				parsedStream.addAll(tokens);
				
				if (parens[0] == parens[1]) {
					if (tokens.get(0).compareTo("(") == 0 &&
						tokens.get(1).compareTo("define") == 0) {
						RacketExpr.addConst(parsedStream, constants);
					} else {
						RacketExpr rexp = new RacketExpr(parsedStream);
						// rexp.print();
						if (rexp.getReturnType() == RacketExpr.ReturnType.BOOLEAN) {
							System.out.println(rexp.interpBool(constants));
						} else if (rexp.getReturnType() == RacketExpr.ReturnType.DOUBLE) {
							System.out.println(rexp.interpNum(constants));
						}
						rexp = null;
						parens[0] = 0; parens[1] = 0;
						parsedStream.clear();
					}
				}
			}
		} while (in.hasNextLine());
		
		
		in.close();
	}

}
