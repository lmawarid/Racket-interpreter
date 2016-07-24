import java.util.List;
import java.util.Arrays;
import java.util.function.*;

public final class Functions {
	private static final String[] numbools = {"=", "<", ">", "<=", ">="};
	private static final String[] boolbools = {"not", "and", "or"};
	private static final String[] binary = {"+", "-", "*", "/", "expt", "mod", "max", "min"};
	private static final String[] unary = {"abs", "exp", "sqr", "sqrt", "sin", "cos", "tan"};
	
	private static final double EPSILON = Double.MIN_VALUE;
	
	public static final List<String> numBoolFunc = Arrays.asList(numbools);
	public static final List<String> boolBoolFunc = Arrays.asList(boolbools);
	public static final List<String> binaryFunc = Arrays.asList(binary);
	public static final List<String> unaryFunc = Arrays.asList(unary);
	
	public static final BiPredicate<Double, Double> dblEqual = (a, b) -> Math.abs(a - b) <= EPSILON;
	public static final BiPredicate<Double, Double> lessThan = (a, b) -> a < b;
	public static final BiPredicate<Double, Double> greaterThan = (a, b) -> a > b;
	public static final BiPredicate<Double, Double> lessThanEql = (a, b) -> a <= b;
	public static final BiPredicate<Double, Double> greaterThanEql = (a, b) -> a >= b;
	
	public static final BiPredicate<Boolean, Boolean> and = (a, b) -> a && b;
	public static final BiPredicate<Boolean, Boolean> or = (a, b) -> a || b;
	public static final Predicate<Boolean> not = (a) -> !a;
	
	public static final BinaryOperator<Double> add = (a, b) -> a + b;
	public static final BinaryOperator<Double> sub = (a, b) -> a - b;
	public static final BinaryOperator<Double> mult = (a, b) -> a * b;
	public static final BinaryOperator<Double> quot = (a, b) -> a / b;
	public static final BinaryOperator<Double> expt = (a, b) -> Math.pow(a, b);
	
	public static final BinaryOperator<Double> mod = (a, b) -> a % b;
	public static final BinaryOperator<Double> max = (a, b) -> (a > b) ? a : b;
	public static final BinaryOperator<Double> min = (a, b) -> (a < b) ? a : b;
	
	public static final UnaryOperator<Double> abs = (a) -> Math.abs(a);
	public static final UnaryOperator<Double> exp = (a) -> Math.exp(a);
	public static final UnaryOperator<Double> sqr = (a) -> a * a;
	public static final UnaryOperator<Double> sqrt = (a) -> Math.sqrt(a);
	public static final UnaryOperator<Double> sin = (a) -> Math.sin(a);
	public static final UnaryOperator<Double> cos = (a) -> Math.cos(a);
	public static final UnaryOperator<Double> tan = (a) -> Math.tan(a);
}
