package wang.patrick.smarter;

import android.provider.BaseColumns;

public class DBStructure {

    public static abstract class tableEntry implements BaseColumns {
        public static final String TABLE_NAME = "dailyusage";
        public static final String COLUMN_ID = "hourid";
        public static final String COLUMN_FRIDGE = "fridge";
        public static final String COLUMN_WASH = "wash";
        public static final String COLUMN_AIR = "air";
        public static final String COLUMN_TEMP = "temp";

    }

}


