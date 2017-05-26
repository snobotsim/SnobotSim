#ifdef CTR_USG_REP
  typedef struct _Talon_Reporting_Flags_t {
	  unsigned ThrottleMode:1;
	  unsigned PositionMode:1;
	  unsigned SpeedMode:1;
	  unsigned CurrentMode:1;
	  unsigned VoltageMode:1;
	  unsigned FollowerMode:1;
	  unsigned MotionProfileMode:1;
	  unsigned MotionMagicMode:1;
	  unsigned ReservedControlModes:7;
	  unsigned Disabled:1;
	  unsigned ReservedFlags:16;
  } Talon_Reporting_Flags_t;



protected:

	template <typename T> class repFlags{
		public:
			uint32_t arbId;
			uint8_t bytes[4];
			CTR_Code err;
			T * operator -> ()
			{
				return (T *)bytes;
			}
			T & operator*()
			{
				return *(T *)bytes;
			}
	};
#endif
