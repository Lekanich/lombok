import java.lang.System;

class FinalT {
	@lombok.experimental.Final
	FinalT(final java.lang.String cons, @lombok.experimental.NonFinal long zz) {
		final int yCons = 4;
	}

	@lombok.experimental.Final
	public void method(final int y, @lombok.experimental.NonFinal long z) {
		final int x = 3;
		@lombok.experimental.NonFinal long zero;
		zero = 0;
		for (int i = 0; i < 10; i++) {
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
	}
}
