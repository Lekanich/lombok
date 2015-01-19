class FinalT {
	FinalT(final java.lang.String cons, long zz) {
		final int yCons = 4;
	}

	public void method(final int y, long z) {
		final int x = 3;
		long zero;
		zero = 0;
		for (int i = 0; i < 10; i++) {
			System.out.println(i);
		}
		final int[] array = {3, 4, 5, 6, 7};
		for (final int i : array) {
			System.out.println(i);
		}
		{
			final int some = 0;
			{
				final int fail = 0;
				for (int j = 0; j < 10; j++) {
					final int k = 0;
				}
			}
		}
		try {
			System.out.print('c');
		} catch (final Throwable some) {
			some = null;
		}
	}
}
