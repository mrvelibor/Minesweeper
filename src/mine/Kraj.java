package mine;

public class Kraj extends Exception {
	private boolean pobeda;
	
	public Kraj(boolean p)
	{ pobeda = p; }
	
	public boolean pobeda()
	{ return pobeda; }
}
