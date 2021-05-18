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
	
	public static MenuInput fromInteger(int number) {
		switch(number) {
		case 1:
			return LIST_COMPUTERS;
		case 2:
			return LIST_COMPANIES;
		case 3:
			return SHOW_DETAILS;
		case 4:
			return CREATE_COMPUTER;
		case 5:
			return UPDATE_COMPUTER;
		case 6:
			return DELETE_COMPUTER;
		case 7:
			return EXIT;
		}
		
		return null;
	}
	
}
