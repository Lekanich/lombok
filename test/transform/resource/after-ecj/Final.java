class FinalT {
	@lombok.experimental.Final
	FinalT(final java.lang.String cons, @lombok.experimental.NonFinal long zz) {
		final int yCons = 4;
	}

	@lombok.experimental.Final
	public void method(final int y, @lombok.experimental.NonFinal long z) {
		final int x = 3;
		@lombok.experimental.NonFinal long zero;
	}
}
