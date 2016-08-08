package com.huangyezhaobiao.enums;

public enum PopTypeEnum {

	NEW_ORDER(100), ORDERRESULT(101), COUNTDOWN(102), SYSTEMMESSAGE(103),LOGOUT(105);

	private int type;

	PopTypeEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static PopTypeEnum getPopType(int type) {
		switch (type) {
		case 100:
			return PopTypeEnum.NEW_ORDER;
		case 101:
			return PopTypeEnum.ORDERRESULT;
		case 102:
			return PopTypeEnum.COUNTDOWN;
		case 103:
			return PopTypeEnum.SYSTEMMESSAGE;
		case 105:
			return PopTypeEnum.LOGOUT;
		default:
			return PopTypeEnum.NEW_ORDER;
		}
	}

	public static void main(String[] arg) {

		int type = 123123;
		switch (PopTypeEnum.getPopType(type)) {

		case NEW_ORDER:
			break;
		case ORDERRESULT:
			break;
		case COUNTDOWN:
			break;
		case SYSTEMMESSAGE:
			break;
		default:
			break;
		}

	}

}
