class FinalT {
	private int globalField;

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
		globalField = 0;
		int[] array = {3,4,5,6,7};
		for (int i : array) {
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

		try {
			System.out.print('c');
		} catch (Throwable some) {
			some = null;
		}
	}
}
