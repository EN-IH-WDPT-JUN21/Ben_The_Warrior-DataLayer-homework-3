package com.ironhack.homework3.utils;

import com.ironhack.homework3.dao.main.Menu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrinterTest {
    @MockBean
    private Menu menu;

    private final static String RED = "\u001B[31m";
    private final static String RESET = "\u001B[0m";
    private final static String BRIGHT_GREEN = "\u001B[92m";
    private final static String BG_RED = "\u001B[41m";

    // Setup for print testing.
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.out.println(RESET);
        System.setOut(standardOut);
    }

    // ============================== MAIN PRINT TESTS ==============================
    // ==================== TEST clearCommandLine() ====================
    @Test
    @DisplayName("Clear command line")
    void clearCommandLine_printClearCommands() {
        Printer.clearCommandLine();
        assertEquals("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n\033[H\033[2J", outputStreamCaptor.toString());
    }

    // ==================== TEST printCenterString() ====================
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Print centered - Empty text")
    void printCenterString_noTextWithBorderStyle_emptyLine() {
        Printer.printCenterString("", 20, BG_RED, "");
        String output = BG_RED + "  " + RESET + "                " + RESET + BG_RED + "  " + RESET + "\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Print centered - Simple text")
    void printCenterString_textWithBorderStyle_simpleLine() {
        Printer.printCenterString("Test", 20, BG_RED, "");
        String output = BG_RED + "  " + RESET + "      Test      " + RESET + BG_RED + "  " + RESET + "\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Print centered - Big word")
    void printCenterString_bigWordWithBorderStyle_multipleLines() {
        Printer.printCenterString("TestTestTestTestTestTestTestTestTest", 20, BG_RED, "");
        String output = BG_RED + "  " + RESET + "TestTestTestTest" + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "TestTestTestTest" + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "      Test      " + RESET + BG_RED + "  " + RESET + "\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Print centered - Big text")
    void printCenterString_bigTextWithBorderStyle_multipleLines() {
        Printer.printCenterString("Test Test Test Test Test Test Test Test Test", 20, BG_RED, "");
        String output = BG_RED + "  " + RESET + " Test Test Test " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + " Test Test Test " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + " Test Test Test " + RESET + BG_RED + "  " + RESET + "\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Print centered - Big word colored")
    void printCenterString_bigWordColoredWithBorderStyle_multipleLines() {
        Printer.printCenterString(RED + "TestTe" + BRIGHT_GREEN + "stTestTestT" + RESET + "estTestTestTe" + RED + "stTest", 20, BG_RED, "");
        String output = BG_RED + "  " + RESET + RED + "TestTe" + BRIGHT_GREEN + "stTestTest" + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + RED + BRIGHT_GREEN + "T" + RESET + "estTestTestTe" + RED + "st" + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "      " + RED + BRIGHT_GREEN + RESET + RED + "Test      " + RESET + BG_RED + "  " + RESET + "\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Print centered - Big text colored")
    void printCenterString_bigTextColoredWithBorderStyle_multipleLines() {
        Printer.printCenterString(RED + "Test Test Test " + BRIGHT_GREEN + "Test Test Test Test " + RESET + "Test Test", 20, BG_RED, "");
        String output = BG_RED + "  " + RESET + " " + RED + "Test Test Test " + BRIGHT_GREEN + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + " " + RED + BRIGHT_GREEN + "Test Test Test " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + " " + RED + BRIGHT_GREEN + "Test " + RESET + "Test Test " + RESET + BG_RED + "  " + RESET + "\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }

    // ==================== TEST printLeftString() ====================
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Print on left - Empty text")
    void printLeftString_noTextWithBorderStyle_emptyLine() {
        Printer.printLeftString("", 0, 20, BG_RED, "");
        String output = BG_RED + "  " + RESET + "                " + RESET + BG_RED + "  " + RESET + "\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Print on left - Simple text and format")
    void printLeftString_textWithBorderStyle_simpleLine() {
        Printer.printLeftString("Test", 2, 20, BG_RED, "");
        String output = BG_RED + "  " + RESET + "  Test          " + RESET + BG_RED + "  " + RESET + "\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Print on left - Big word")
    void printLeftString_bigWordWithBorderStyle_multipleLines() {
        Printer.printLeftString("TestTestTestTestTestTestTestTest", 2, 20, BG_RED, "");
        String output = BG_RED + "  " + RESET + "  TestTestTest  " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "  TestTestTest  " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "  TestTest      " + RESET + BG_RED + "  " + RESET + "\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Print on left - Big text")
    void printLeftString_bigTextWithBorderStyle_multipleLines() {
        Printer.printLeftString("Test Test Test Test Test Test Test Test Test", 2, 20, BG_RED, "");
        String output = BG_RED + "  " + RESET + "  Test Test     " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "  Test Test     " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "  Test Test     " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "  Test Test     " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "  Test          " + RESET + BG_RED + "  " + RESET + "\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Print on left - Big word colored")
    void printLeftString_bigWordColoredWithBorderStyle_multipleLines() {
        Printer.printLeftString(RED + "TestTe" + BRIGHT_GREEN + "stTestTestT" + RESET + "estTestTestTe" + RED + "stTest", 2, 20, BG_RED, "");
        String output = BG_RED + "  " + RESET + "  " + RED + "TestTe" + BRIGHT_GREEN + "stTest  " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "  " + RED + BRIGHT_GREEN + "TestT" + RESET + "estTest  " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "  " + RED + BRIGHT_GREEN + RESET + "TestTe" + RED + "stTest  " + RESET + BG_RED + "  " + RESET + "\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Print on left - Big text colored")
    void printLeftString_bigTextColoredWithBorderStyle_multipleLines() {
        Printer.printLeftString(RED + "Test Test Test " + BRIGHT_GREEN + "Test Test Test Test " + RESET + "Test Test", 2, 20, BG_RED, "");
        String output = BG_RED + "  " + RESET + "  " + RED + "Test Test     " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "  " + RED + "Test " + BRIGHT_GREEN + "Test     " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "  " + RED + BRIGHT_GREEN + "Test Test     " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "  " + RED + BRIGHT_GREEN + "Test " + RESET + "Test     " + RESET + BG_RED + "  " + RESET + "\r\n" +
                BG_RED + "  " + RESET + "  " + RED + BRIGHT_GREEN + RESET + "Test          " + RESET + BG_RED + "  " + RESET + "\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }

    // ============================== AUX METHODS TESTS ==============================
    // ==================== TEST createTextRows() ====================
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "TestTest", " Test Test", "Test Test "})
    @DisplayName("Create Rows - Smaller text")
    void createTextRows_textSmallerThanEmptySpace_OneRow(String s) {
        ArrayList<String> rows = Printer.createTextRows(s, 10);
        assertEquals(1, rows.size());
        assertEquals(s, rows.get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {RED + RESET, RED + " " + RESET, BRIGHT_GREEN + "   " + RESET,
            RED + "Test" + BRIGHT_GREEN + "Test" + RESET, BG_RED + " Test Test" + RESET,
            BRIGHT_GREEN + "Test " + BG_RED + "Test "})
    @DisplayName("Create Rows - Smaller text with color")
    void createTextRows_textSmallerThanEmptySpaceWithColor_OneRow(String s) {
        ArrayList<String> rows = Printer.createTextRows(s, 10);
        assertEquals(1, rows.size());
        assertEquals(s, rows.get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"TestTestTest", "Test Test Test", " Test Test t", "TestTestTestTestTestTestTest",
            "Test test TestTest Test Test"})
    @DisplayName("Create Rows - Larger text")
    void createTextRows_textLargerThanEmptySpace_moreThanOneRow(String s) {
        assertTrue(Printer.createTextRows(s, 10).size() > 1);
    }

    @ParameterizedTest
    @ValueSource(strings = {RED + "TestTestTest" + RESET, "Test " + BG_RED + "Test" + RESET + " Test",
            " Te" + BG_RED + "st Test" + RESET + " t", RED + "TestTest" + RESET + "TestTestTest" + BRIGHT_GREEN + "TestTest",
            "Test" + RED + " test Test" + BG_RED + "Test Test" + RESET + " Test"})
    @DisplayName("Create Rows - Larger text with color")
    void createTextRows_textLargerThanEmptySpaceWithColor_moreThanOneRow(String s) {
        assertTrue(Printer.createTextRows(s, 10).size() > 1);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "TestTest:[TestTest]",
            "TestTestTestTest:[TestTestTe, stTest]",
            "TestTestTestTestTestTestTestTestTest:[TestTestTe, stTestTest, TestTestTe, stTest]",
            "Test Test Test Test:[Test Test , Test Test]",
            "Test TestT T est:[Test , TestT T , est]",
            "TestTest Test Test TestTestTest TestTest:[TestTest , Test Test , TestTestTe, st , TestTest]",
    }, delimiter = ':')
    @DisplayName("Create Rows - Exact row values")
    void createTextRows_text_correctRowText(String inputText, String outputRows) {
        assertEquals(outputRows, Printer.createTextRows(inputText, 10).toString());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "T\u001B[31mest\u001B[92mTest\u001B[0m:" +
                    "[T\u001B[31mest\u001B[92mTest\u001B[0m]",
            "T\u001B[31me\u001B[92mstTestT\u001B[31mest\u001B[0mTest:" +
                    "[T\u001B[31me\u001B[92mstTestT\u001B[31me, \u001B[31m\u001B[92m\u001B[31mst\u001B[0mTest]",
            "T\u001B[92mestTestTe\u001B[0mstTestTest\u001B[92mTestTest\u001B[31mTestTest\u001B[0m:" +
                    "[T\u001B[92mestTestTe\u001B[0m, \u001B[92m\u001B[0mstTestTest\u001B[92m, \u001B[92m\u001B[0m\u001B[92mTestTest\u001B[31mTe, \u001B[92m\u001B[0m\u001B[92m\u001B[31mstTest\u001B[0m]",
            "T\u001B[31mest Test\u001B[0m \u001B[31mTest\u001B[0m Test:" +
                    "[T\u001B[31mest Test\u001B[0m \u001B[31m, \u001B[31m\u001B[0m\u001B[31mTest\u001B[0m Test]"
    }, delimiter = ':')
    @DisplayName("Create Rows - Exact row values with color")
    void createTextRows_textWithColor_correctRowText(String inputText, String outputRows) {
        assertEquals(outputRows, Printer.createTextRows(inputText, 10).toString());
    }

    // The CsvSource deletes the start of the ASCII color code, so a normal test is needed.
    @Test
    @DisplayName("Create Rows - Exact row values with color - Initial Word")
    void createTextRows_textWithColorInFirstWord_correctRowText() {
        String s1 = "\u001B[31mTest\u001B[0m";
        String s2 = "\u001B[31mTest Test\u001B[0m \u001B[31mTest\u001B[0m Test";
        assertEquals("[\u001B[31mTest\u001B[0m]", Printer.createTextRows(s1, 10).toString());
        assertEquals("[\u001B[31mTest Test\u001B[0m \u001B[31m, \u001B[31m\u001B[0m\u001B[31mTest\u001B[0m Test]", Printer.createTextRows(s2, 10).toString());
    }

    // ==================== TEST numberOfTextRows() ====================
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "TestTest", " Test Test", "Test Test "})
    @DisplayName("Number of Rows - Smaller text")
    void numberOfTextRows_textSmallerThanEmptySpace_OneRow(String s) {
        assertEquals(1, Printer.numberOfTextRows(s, 10));
    }

    @ParameterizedTest
    @ValueSource(strings = {RED + RESET, RED + " " + RESET, BRIGHT_GREEN + "   " + RESET,
            RED + "Test" + BRIGHT_GREEN + "Test" + RESET, BG_RED + " Test Test" + RESET,
            BRIGHT_GREEN + "Test " + BG_RED + "Test "})
    @DisplayName("Number of Rows - Smaller text with color")
    void numberOfTextRows_textSmallerThanEmptySpaceWithColor_OneRow(String s) {
        assertEquals(1, Printer.numberOfTextRows(s, 10));
    }

    @ParameterizedTest
    @ValueSource(strings = {"TestTestTest", "Test Test Test", " Test Test t", "TestTestTestTestTestTestTest",
            "Test test TestTest Test Test"})
    @DisplayName("Number of Rows - Larger text")
    void numberOfTextRows_textLargerThanEmptySpace_moreThanOneRow(String s) {
        assertTrue(Printer.numberOfTextRows(s, 10) > 1);
    }

    @ParameterizedTest
    @ValueSource(strings = {RED + "TestTestTest" + RESET, "Test " + BG_RED + "Test" + RESET + " Test",
            " Te" + BG_RED + "st Test" + RESET + " t", RED + "TestTest" + RESET + "TestTestTest" + BRIGHT_GREEN + "TestTest",
            "Test" + RED + " test Test" + BG_RED + "Test Test" + RESET + " Test"})
    @DisplayName("Number of Rows - Larger text with color")
    void numberOfTextRows_textLargerThanEmptySpaceWithColor_moreThanOneRow(String s) {
        assertTrue(Printer.numberOfTextRows(s, 10) > 1);
    }

    @ParameterizedTest
    @CsvSource({"6,10", "10,5", "12,5", "18,4", "19,3", "24,3", "30,2", "49,1", "100,1"})
    @DisplayName("Number of Rows - Exact number of rows")
    void numberOfTextRows_fixedTextWithVariableEmptySpace_correctNumberOfRows(int inputEmptySpace, int expectedRows) {
        String s = "Test Test Test Test Test Test Test Test Test Test";
        assertEquals(expectedRows, Printer.numberOfTextRows(s, inputEmptySpace));
    }

    @ParameterizedTest
    @CsvSource({"6,10", "10,5", "12,5", "18,4", "19,3", "24,3", "30,2", "49,1", "100,1"})
    @DisplayName("Number of Rows - Exact number of rows with color")
    void numberOfTextRows_fixedTextWithVariableEmptySpaceWithColor_correctNumberOfRows(int inputEmptySpace, int expectedRows) {
        String s = "Test " + RED + "Test Test" + BRIGHT_GREEN + " Test Test " + BG_RED + "Test Test" + RESET + " Test Test Test";
        assertEquals(expectedRows, Printer.numberOfTextRows(s, inputEmptySpace));
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 7, 13, 17, 18, 20})
    @DisplayName("Number of Rows - Same rows with and without color")
    void numberOfTextRows_textWithAndWithoutColor_sameNumberOfRows(int emptySize) {
        String s = "Test TestTest Test";
        String sc = "Test " + RED + "Test" + BRIGHT_GREEN + "Test" + BG_RED + " Test" + RESET;
        assertEquals(Printer.numberOfTextRows(s, emptySize), Printer.numberOfTextRows(sc, emptySize));
    }

    // ==================== TEST divideText() ====================
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    ", "TestTest", " Test Test", "Test Test ", "TestTestTest", "Test Test Test",
            " Test Test t", "TestTestTestTestTestTestTest", "Test test TestTest Test Test",
            "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest Test "})
    @DisplayName("Divide Text - Smaller text")
    void divideText_textEqualOrSmallerThanEmptySpace_originalStringAndEmptyString(String s) {
        String[] dividedString = Printer.divideText(s, s.length());
        assertTrue(dividedString[0].equals(s) && dividedString[1].equals(""));
    }

    @ParameterizedTest
    @ValueSource(strings = {"TestTest", "TestTestTest", "TestTestTestTestTestTestTest",
            "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest Test "})
    @DisplayName("Divide Text - Larger first word 1")
    void divideText_firstWordLargerThanEmptySpace_firstWordSplitByEmptySpaceAndRemainingStringExists(String s) {
        String[] dividedString = Printer.divideText(s, 3);
        assertTrue(dividedString[0].equals("Tes") && dividedString[1].length() > 0);
    }

    @Test
    @DisplayName("Divide Text - Larger first word 2")
    void divideText_firstWordLargerThanEmptySpace_correctSplitText() {
        String[] dividedString = Printer.divideText("TestTestTest Test", 6);
        assertTrue(dividedString[0].equals("TestTe") && dividedString[1].equals("stTest Test"));
    }

    @ParameterizedTest
    @ValueSource(strings = {" Test Test", "Test Test ", "Test Test Test", " Test Test t",
            "Test test TestTest Test Test"})
    @DisplayName("Divide Text - Larger text 1")
    void divideText_textLargerThanEmptySpace_wordsThatFitAndRemainingStringExists(String s) {
        String[] dividedString = Printer.divideText(s, 6);
        assertTrue(dividedString[0].equals("Test ") && dividedString[1].length() > 0);
    }

    @Test
    @DisplayName("Divide Text - Larger text 2")
    void divideText_textLargerThanEmptySpace_correctSplitText() {
        String[] dividedString = Printer.divideText("Test Test TestTest", 12);
        assertTrue(dividedString[0].equals("Test Test ") && dividedString[1].equals("TestTest"));
    }

    // ==================== TEST getColorCodes() ====================
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "     ", "Test", "TestTestTestTestTestTestTest",
            "TestTest TestTestTest - TestTest: Test? `\"", "-931t3tn 9qcg7637 613",
            "-93jkhg9qn3v9w783gh q783ghq8v3 gq3y8igbq58y3bgakgba3jygbxnev853 g3ma8yg nuebv antg ye ng vzyug ajyv znrut3 gegz1t3 tn9qcg7 637613"})
    @DisplayName("Color Code Mapping - No colors")
    void getColorCodes_noASCIIColorCode_EmptyTreeMap(String s) {
        assertTrue(Printer.getColorCodes(s).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {RED + "" + RESET, RED + " " + RESET, RED + "Test" + RESET,
            RED + "TestTestTestTestTestTestTest" + RESET, RED + "TestTest TestTestTest - TestTest: Test? `\"" + RESET,
            RED + "-931t3 tn9qcg7 637613" + RESET,
            RED + "-93jkhg9qn3v9w783gh q783ghq8v3 gq3y8igbq58y3bgakgba3jygbxnev853 g3ma8yg nuebv antg ye ng vzyug ajyv znrut3 gegz1t3 tn9qcg7 637613" + RESET})
    @DisplayName("Color Code Mapping - Two colors")
    void getColorCodes_twoASCIIColorCode_TreeMapWithSizeTwo(String s) {
        assertEquals(2, Printer.getColorCodes(s).size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"TestTest " + RED + "TestTestTest" + RESET,
            "Blablaba " + RED + "TestTestTest" + RESET + " TestTestTest"})
    @DisplayName("Color Code Mapping - Two colors - Indexes")
    void getColorCodes_twoASCIIColorCodeInIndex9And26_correctColorCodeIndexes(String s) {
        TreeMap<Integer, String> cm = Printer.getColorCodes(s);
        assertEquals(9, cm.firstKey());
        assertEquals(26, cm.lastKey());
    }

    @ParameterizedTest
    @ValueSource(strings = {"TestTest " + RED + "TestTestTest" + BRIGHT_GREEN + BG_RED + "Lalallalalaala" + RESET,
            "Blablaba " + RED + "TestTestTest" + BRIGHT_GREEN + " TestTestTest" + BG_RED + "Lalalala" + RESET})
    @DisplayName("Color Code Mapping - Multiple colors")
    void getColorCodes_multipleASCIIColorCode_savesAllColorCode(String s) {
        TreeMap<Integer, String> cm = Printer.getColorCodes(s);
        assertTrue(cm.containsValue(RED));
        assertTrue(cm.containsValue(BRIGHT_GREEN));
        assertTrue(cm.containsValue(BG_RED));
        assertTrue(cm.containsValue(RESET));
    }

    @ParameterizedTest
    @ValueSource(strings = {"TestTest " + RED + "TestTestTest" + BRIGHT_GREEN + "TestTestTestTest" + RESET,
            "Blablabl," + RED + "TestTestTest" + BRIGHT_GREEN + " Test Test Test " + RESET + "Blaaaa!"})
    @DisplayName("Color Code Mapping - Multiple colors - Exact mapping")
    void getColorCodes_multipleASCIIColorCode_correctColorTreeMap(String s) {
        TreeMap<Integer, String> cm = Printer.getColorCodes(s);
        TreeMap<Integer, String> aux = new TreeMap<>();
        aux.put(9, RED);
        aux.put(26, BRIGHT_GREEN);
        aux.put(47, RESET);
        assertEquals(aux, cm);
    }
}