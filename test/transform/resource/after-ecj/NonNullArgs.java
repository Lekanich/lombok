class NonNullArgs extends Thread {

	@lombok.experimental.NonNullArgs
	NonNullArgs(String arg) {
		this(arg, "");
		if (arg == null) {
			throw new NullPointerException("arg");
		}
	}

	@lombok.experimental.NonNullArgs
	NonNullArgs(String arg, String arg2) {
		super(arg);
		if (arg2 == null) {
			throw new NullPointerException("arg2");
		}
		if (arg == null) throw new NullPointerException();
	}

	@lombok.experimental.NonNullArgs
	public void test2(String arg, String arg2, String arg3) {
		if (arg == null) {
			throw new NullPointerException("arg");
		}
		if (arg3 == null) {
			throw new NullPointerException("arg3");
		}
		if (arg2 == null) {
			throw new NullPointerException("arg2");
		}
		if (arg == null) System.out.println("Hello");
	}

	@lombok.experimental.NonNullArgs
	public void test3(String arg) {
		if (arg == null) {
			throw new NullPointerException("arg");
		}
		if (arg != null) throw new IllegalStateException();
	}

	@lombok.experimental.NonNullArgs
	public void test(String stringArg, String arg2, int primitiveArg) {
		if (stringArg == null) {
			throw new NullPointerException("stringArg");
		}
		if (arg2 == null) {
			throw new NullPointerException("arg2");
		}
	}

	@lombok.experimental.NonNullArgs
	public void test(String arg) {
		if (arg == null) {
			throw new NullPointerException("arg");
		}
		System.out.println("Hey");
		if (arg == null) throw new NullPointerException();
	}

	@lombok.experimental.NonNullArgs
	public void test21(@javax.annotation.Nullable String arg, @javax.annotation.Nullable String arg2, String arg3) {
		if (arg3 == null) {
			throw new NullPointerException("arg3");
		}
		if (arg2 == null) {
			throw new NullPointerException("arg2");
		}
		if (arg == null) System.out.println("Hello");
	}

	@lombok.experimental.NonNullArgs
	public void test31(@javax.annotation.Nullable String arg) {
		if (arg != null) throw new IllegalStateException();
	}

	@lombok.experimental.NonNullArgs
	public void test1(String stringArg, @javax.annotation.Nullable String arg2, int primitiveArg) {
		if (stringArg == null) {
			throw new NullPointerException("stringArg");
		}
	}

	@lombok.experimental.NonNullArgs
	public void test1(@javax.annotation.Nullable String arg) {
		System.out.println("Hey");
		if (arg == null) throw new NullPointerException();
	}
}