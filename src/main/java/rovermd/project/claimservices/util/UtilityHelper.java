package rovermd.project.claimservices.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilityHelper {

    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9.]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

    public static boolean isEmpty(final String str) {
        return (str == null) || (str.length() <= 0);
    }


    public static boolean isEmpty(Object str) {
        return (str == null);
    }

    public static boolean isInValidDate(final String date, final String ClaimCreateDate) {
        return date.equals("00000000") || date.equals("19000101") || Integer.parseInt(date.replace("-", "")) > Integer.parseInt(ClaimCreateDate);
    }

    public static String CheckStringVariable(String VariableName) {
        return VariableName == null ? "" : VariableName ;
    }


}
