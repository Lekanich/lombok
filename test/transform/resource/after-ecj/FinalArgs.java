class FinalArgsT {
	@lombok.experimental.FinalArgs
	FinalArgsT(final String cons, @lombok.experimental.NonFinal long zz) {
		int yCons = 4;
	}

	@lombok.experimental.FinalArgs
	public void method(final int y, @lombok.experimental.NonFinal long z) {
		int x = 3;
		@lombok.experimental.NonFinal long zero;
	}
}
