
public class Main {
	public static void main(String[] args) {
		Communication com = new Communication();
		Alice caller = new Alice(com);
		Bob callee = new Bob(com);
		caller.start();
		callee.start();
	}
}
