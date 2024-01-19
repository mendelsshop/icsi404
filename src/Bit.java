public class Bit {
	private static boolean not(Boolean b) {
		return b ? false : true;
	}

	private static Boolean and(Boolean b1, Boolean b2) {
		return b1 ? b2 : false;
	}

	private static Boolean or(Boolean b1, Boolean b2) {
		return b1 ? true : b2;
	}

	private static Boolean xor(Boolean b1, Boolean b2) {
		return b1 ? b2 ? false : true : b2 ? true : false;
	}

	private Boolean value;

	public Bit(Boolean startValue) {
		value = startValue;
	}

	public void set(Boolean newValue) {
		value = newValue;
	}

	public void toggle() {
		value = not(value);
	}

	public void set() {
		value = true;
	}

	public void clear() {
		value = false;
	}

	public Boolean getValue() {
		return value;
	}

	public Bit and(Bit other) {
		return new Bit(and(value, other.getValue()));
	}

	public Bit or(Bit other) {
		return new Bit(or(value, other.getValue()));
	}

	public Bit xor(Bit other) {
		return new Bit(xor(value, other.getValue()));
	}

	public Bit not() {
		return new Bit(not(value));
	}

	@Override
	public String toString() {
		return value ? "t" : "f";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bit other = (Bit) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
