package com.ctre;

public enum CTR_Code
{
	CTR_OKAY(0),				//!< No Error - Function executed as expected
	CTR_RxTimeout(1),			//!< CAN frame has not been received within specified period of time.
	CTR_TxTimeout(2),			//!< Not used.
	CTR_InvalidParamValue(3), 	//!< Caller passed an invalid param
	CTR_UnexpectedArbId(4),	//!< Specified CAN Id is invalid.
	CTR_TxFailed(5),			//!< Could not transmit the CAN frame.
	CTR_SigNotUpdated(6),		//!< Have not received an value response for signal.
	CTR_BufferFull(7),			//!< Caller attempted to insert data into a buffer that is full.
	CTR_UnknownError(8);		//!< Error code not supported

	private int value; private CTR_Code(int value) { this.value = value; } 
	public static CTR_Code getEnum(int value) {
		for (CTR_Code e : CTR_Code.values()) {
			if (e.value == value) {
				return e;
			}
		}
		return CTR_UnknownError;
	}
	public int IntValue() { return value;}
}
