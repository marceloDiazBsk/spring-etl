package corar.etl.util;

import com.google.common.base.CaseFormat;

public class StringUtil {
    public static String camelToSnakeCase(String value) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, value);
    }
}
