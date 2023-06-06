package test;

import java.util.Arrays;
import java.util.LinkedList;

public class MyStringTest {
    public static void main(String[] args) {
        // 문자열에 '#'이나 ':'과 같은 여러 구분 기호가 모두 포함되어 있는 경우
        // 문자열을 나누는 방법 샘플
        String input = "ghi:abc##def:jkl";
        String[] strArr = MyString.splitToStringArray(input, "[:#]", true);
        LinkedList<String> strList = MyString.splitToLinkedList(input, "[:#]", true);
        String str1 = MyString.joinString(strArr, ":");
        String str2 = MyString.joinString(strList, ":");
        System.out.println("input: " + input);
        System.out.println("strArr: " + Arrays.toString(strArr));
        System.out.println("strList: " + strList);
        System.out.println("str1: " + str1);
        System.out.println("str2: " + str2);
        strArr = MyString.sortArray(strArr, true);
        strList = MyString.sortLinkedList(strList, true);
        System.out.println("strArr: " + Arrays.toString(strArr));
        System.out.println("strList: " + strList);
        String[] convArr = MyString.convertStringListToStringArray(strList);
        System.out.println("convArr: " + Arrays.toString(convArr));
        LinkedList<String> convList = MyString.convertStringArrayToLinkedList(strArr);
        System.out.println("convList: " + convList);
    }
}
