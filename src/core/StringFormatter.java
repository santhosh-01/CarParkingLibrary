package core;

// Utility Class for formatting string
public class StringFormatter {

    private StringFormatter() {}

    // To return string centered with given character
    public static String center(String str, int maxChar, char spaceFill) {
        String spaceFillString = String.valueOf(spaceFill);
        int length = str.length();
        int totalSpaces = (maxChar - length);
        int spaces = totalSpaces / 2;
        if(totalSpaces % 2 == 0) {
            return spaceFillString.repeat(spaces) + str + spaceFillString.repeat(spaces);
        }
        else {
            return spaceFillString.repeat(spaces + 1) + str + spaceFillString.repeat(spaces);
        }
    }

    public static String center(String str, int maxChar) {
        return center(str, maxChar, ' ');
    }

    public static String getCharLine(char lineFill, int maxChar) {
        String lineFillString = String.valueOf(lineFill);
        return lineFillString.repeat(maxChar);
    }
}
