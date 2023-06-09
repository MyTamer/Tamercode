package edu.java.homework.hw08.ipj08.exer01;

import java.util.Arrays;

public class MyString {

    private static final int BASE_LENGTH = 500;

    private char[] string = new char[BASE_LENGTH];

    public MyString() {
        string = null;
    }

    public MyString(char[] charArray) {
        setString(charArray);
    }

    public MyString(String string) {
        if (string == null) {
            throw new IllegalArgumentException("String object is null-pointed");
        }
        char[] charArray = string.toCharArray();
        setString(charArray);
    }

    public char[] getString() {
        return string;
    }

    public void setString(char[] string) {
        copyCharArray(string);
    }

    /**
     * convert the filed "string" in chars in lower case
     */
    public MyString toLowerCase() {
        int diff = 'a' - 'A';
        char[] charArray = getString();
        for (int iter = 0; iter < getString().length; iter++) {
            if (charArray[iter] >= 'A' && charArray[iter] <= 'Z') {
                charArray[iter] = (char) (charArray[iter] + diff);
            }
        }
        return new MyString(charArray);
    }

    /**
     * convert the filed "string" in chars in upper case
     * 
     * @return MyString --> return modified MyString object
     */
    public MyString toUpperCase() {
        int diff = 'a' - 'A';
        char[] charArray = getString();
        for (int iter = 0; iter < getString().length; iter++) {
            if (charArray[iter] >= 'a' && charArray[iter] <= 'z') {
                charArray[iter] = (char) (charArray[iter] - diff);
            }
        }
        return new MyString(charArray);
    }

    /**
     * replace the white spaces in the beginning and the end of the string
     * 
     * @return MyString --> return modified MyString object
     */
    public MyString trim() {
        char[] charArray = this.getString();
        int i = 0;
        int length = charArray.length;
        int startIndexSubstring = 0;
        int endIndexSubstring = length - 1;
        while (i < length && charArray[i] == ' ') {
            startIndexSubstring++;
            i++;
        }
        i = length - 1;
        while (i >= 0 && charArray[i] == ' ') {
            endIndexSubstring--;
            i--;
        }
        endIndexSubstring++;
        int lengthNewCharArray = endIndexSubstring - startIndexSubstring;
        char[] newCharArray = new char[lengthNewCharArray];
        for (i = 0; i < lengthNewCharArray; i++) {
            newCharArray[i] = charArray[startIndexSubstring + i];
        }
        return new MyString(newCharArray);
    }

    /**
     * get MyString object which string field will be added to string field of
     * the current object
     * 
     * @param str
     *            --> get MyStrng object
     * @return modified MyString object
     */
    public MyString concat(MyString ms) {
        if (ms == null) {
            throw new IllegalArgumentException("The MyString object is null-pointed");
        }
        if (ms.getString() == null) {
            throw new IllegalArgumentException("String filed of the MyString object is null-pointed");
        }
        char[] charArrayExtra = ms.getString();
        int lengthExtraArray = charArrayExtra.length;
        char[] charArrayCurrent = getString();
        int lengthArray = getString().length;
        if (lengthExtraArray + lengthArray > BASE_LENGTH) {
            lengthExtraArray = BASE_LENGTH - lengthArray;
        }
        char[] charArrayTemp = new char[lengthExtraArray + lengthArray];
        for (int i = 0; i < lengthArray; i++) {
            charArrayTemp[i] = charArrayCurrent[i];
        }
        for (int i = lengthArray, j = 0; j < lengthExtraArray; i++, j++) {
            charArrayTemp[i] = charArrayExtra[j];
        }
        return new MyString(charArrayTemp);
    }

    /**
     * 
     * @param character
     * @return -1 if the character is not found or return the position where the
     *         character is
     */
    public int indexOf(char ch) {
        return indexOf(ch, 0);
    }

    /**
     * 
     * @param ch
     *            --> character which we search
     * @param fromindex
     *            --> define the starting position of the search
     * @return -1 if the character is not found or return the position where the
     *         character is
     */
    public int indexOf(char ch, int fromIndex) {
        if (fromIndex >= getString().length || fromIndex < 0) {
            throw new IllegalArgumentException("Incorrect index in the character array");
        }
        char[] charArr = getString();
        int length = charArr.length;
        int i = fromIndex;
        while (i < charArr.length && ch != charArr[i]) {
            i++;
        }
        return ((i == length) ? -1 : i);
    }

    /**
     * 
     * @param beginIndex
     *            show the index which will be the begining of the substring
     * @return MyString object where is the result
     */
    public MyString substring(int beginIndex) {
        if (beginIndex >= getString().length || beginIndex < 0) {
            throw new IllegalArgumentException("Incorrect index in the character array");
        }
        char[] charArr = getString();
        int length = charArr.length;
        char[] charArrResult = new char[length - beginIndex];
        for (int i = 0; i < charArrResult.length; i++) {
            charArrResult[i] = charArr[i + beginIndex];
        }
        return new MyString(charArrResult);
    }

    /**
     * 
     * @param beginIndex
     *            of the substring
     * @param endIndex
     *            of the substring
     * @return MyString object where is the result
     */
    public MyString substring(int beginIndex, int endIndex) {
        char[] charArr = getString();
        int length = charArr.length;
        boolean isWrongBeginIndex = beginIndex >= length || beginIndex < 0;
        boolean isWrongEndIndex = endIndex >= length || endIndex < 0;
        if (endIndex <= beginIndex || isWrongBeginIndex || isWrongEndIndex) {
            throw new IllegalArgumentException("Incorrect begin/end indexes in the character array");
        }
        char[] charArrResult = new char[endIndex - beginIndex + 1];
        for (int j = 0; j < charArrResult.length; j++) {
            charArrResult[j] = charArr[j + beginIndex];
        }
        return new MyString(charArrResult);
    }

    /**
     * 
     * @return the length of the string
     */
    public int length() {
        return getString().length;
    }

    /**
     * @return the character of index number index. If index is out of range
     *         return ' '
     */
    public char charAt(int index) {
        if (index < 0 || index >= getString().length) {
            return ' ';
        } else {
            return getString()[index];
        }
    }

    /**
     * 
     * @param anotherMyStrng
     * @return -1 if this.string is smaller lexicographically than
     *         anotherMyStrng
     * @return 0 if this.string is equal lexicographically with anotherMyStrng
     * @return 1 if this.string is bigger lexicographically than anotherMyStrng
     */
    public int compareTo(MyString anotherMyStrng) {
        if (anotherMyStrng.getString() == null) {
            throw new IllegalArgumentException("char array is null-pointed");
        }
        char[] charArray = anotherMyStrng.getString();
        if (string.length < charArray.length) {
            return 1;
        } else if (string.length > charArray.length) {
            return -1;
        } else {
            int iter = 0;
            int length = string.length;
            while (iter < length && string[iter] == charArray[iter]) {
                iter++;
            }
            if (iter == length) {
                return 0;
            } else {
                int code1 = (int) string[iter];
                int code2 = (int) charArray[iter];
                if (code1 < code2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    }

    /**
     * @return -1, 0, 1 use the same logic as compareTo() but here we ignore the
     *         case of characters
     */
    public int compareToIgnoreCase(MyString anotherMyStrng) {
        MyString modifiedMyStrng = anotherMyStrng.toLowerCase();
        MyString currentMyStrng = this.toLowerCase();
        return currentMyStrng.compareTo(modifiedMyStrng);
    }

    public void printCompareResult(MyString ms) {
        System.out.println("?  " + this.toString() + " == " + ms.toString() + " : " + compareTo(ms));
    }

    public void printCompareIgnoreCaseResult(MyString ms) {
        System.out.println("?  " + this.toString() + " == " + ms.toString() + " : " + compareToIgnoreCase(ms));
    }

    private void copyCharArray(char[] string) {
        if (string == null) {
            throw new IllegalArgumentException("char array is null-pointed");
        }
        if (string.length > BASE_LENGTH) {
            for (int iter = 0; iter < BASE_LENGTH; iter++) {
                this.string[iter] = string[iter];
            }
        } else {
            this.string = string;
        }
    }

    private String convertCharArrayToString() {
        StringBuffer sb = new StringBuffer();
        if (string == null) {
            return null;
        } else {
            sb.append(string);
            return sb.toString();
        }
    }

    /**
     * 
     * @param input
     *            character array
     * @return a new object in heap which is a copy of the input parametar
     */
    private char[] copyCharArrayInDeep(char[] chArr) {
        if (chArr == null) {
            throw new IllegalArgumentException("The character Array is null-pointed");
        }
        int length = chArr.length;
        char[] newCharArray = new char[length];
        for (int i = 0; i < length; i++) {
            newCharArray[i] = chArr[i];
        }
        return newCharArray;
    }

    @Override
    public String toString() {
        return convertCharArrayToString();
    }
}
