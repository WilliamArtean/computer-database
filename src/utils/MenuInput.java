package utils;

public enum MenuInput {
	LIST_COMPUTERS(1), LIST_COMPANIES(2), SHOW_DETAILS(3), CREATE_COMPUTER(4), UPDATE_COMPUTER(5), DELETE_COMPUTER(6), EXIT(7);

	private int number;
	
	private MenuInput(int i) {
		this.number = i;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public static boolean isValid(int number) {
		return (number >= LIST_COMPUTERS.getNumber() && number <= EXIT.getNumber());
	}
	
}
