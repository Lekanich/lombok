class FinalArgsT {
	@lombok.experimental.FinalArgs
	FinalArgsT(String cons, @lombok.experimental.NonFinal long zz) {
		int yCons = 4;
	}

	@lombok.experimental.FinalArgs
	public void method(int y, @lombok.experimental.NonFinal long z) {
		int x = 3;
		long zero;
	}
}
