class FinalT {
	@lombok.experimental.Final
	FinalT(java.lang.String cons, @lombok.experimental.NonFinal long zz) {
		int yCons = 4;
	}

	@lombok.experimental.Final
	public void method(int y, @lombok.experimental.NonFinal long z) {
		int x = 3;
		@lombok.experimental.NonFinal long zero;
		zero = 0;
		for (int i = 0; i < 10; i++) {
			System.out.println(i);
		}

		{
			int some = 0;
			{
				int fail = 0;
				for (int j = 0; j < 10; j++) {
					int k = 0;
				}
			}
		}
	}
}
