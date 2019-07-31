package common;

/** A convenience class for wrapping up an object to adapt it to a different interface. */
public class Wrapper<E> {
	protected final E impl;
	protected Wrapper(E impl) {
		if (impl == null)
			throw new NullPointerException();
		this.impl = impl;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof Wrapper
			&& ((Wrapper<?>) other).impl.equals(this.impl);
	}
	
	@Override
	public int hashCode() {
		return this.impl.hashCode();
	}
	
	@Override
	public String toString() {
		return this.impl.toString();
	}
	
	public E impl() { return this.impl; }
}