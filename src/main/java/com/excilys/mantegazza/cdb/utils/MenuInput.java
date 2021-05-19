package com.excilys.mantegazza.cdb.utils;

public enum MenuInput {
	LIST_COMPUTERS(1), LIST_COMPANIES(2), SHOW_DETAILS(3), CREATE_COMPUTER(4), UPDATE_COMPUTER(5), DELETE_COMPUTER(6), EXIT(7);

	private int number;
	
	/**
	 * Private constructor for MenuInput
	 * @param i The number corresponding to the menu command
	 */
	private MenuInput(int i) {
		this.number = i;
	}
	
	/**
	 * The integer corresponding to this MenuInput command value
	 * @return
	 */
	public int getNumber() {
		return this.number;
	}
	
	/**
	 * Checks if an number is a valid value in the MenuInput enum.
	 * @param number The integer to check
	 * @return true if the number is one of this enum's values, false otherwise
	 */
	public static boolean isValid(int number) {
		return (number >= LIST_COMPUTERS.getNumber() && number <= EXIT.getNumber());
	}
	
	/**
	 * Creates a MenuInput enum from an integer.
	 * @param number the number to create the enum from
	 * @return A MenuInput enum corresponding to a MenuInput command, or null if it matches none
	 */
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
