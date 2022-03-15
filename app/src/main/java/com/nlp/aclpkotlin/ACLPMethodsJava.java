package com.nlp.aclpkotlin;

import java.util.List;

public class ACLPMethodsJava {

    public static String treeParserPython(List<KVObject> list,int index) {
        String result = "";
        if (index < list.size()){
            result = "";
            KVObject item = list.get(index);
            if (item.getKey() == index) {
                String [] splitValue = item.getValue().split(" ");
                item.type = splitValue[0].toUpperCase();
                switch (item.type){
                    case "UPDATE":
                        result = spacing(index) + splitValue[2] + " = " + splitValue[2] + " " + parseOperand(splitValue[1]) + " " + splitValue[3] +
                                treeParserPython(list, index+1);
                        break;
                    case "END":
                        result = "\n";
                        break;
                    case "SET":
                        result = "";
                        break;
                    case "MAIN":
                        result = "def main(args):\n\t" + treeParserPython(list, index+1);
                        break;
                    case "FUNCTION":
                        int numParameters = parseNumber(splitValue[2]);
                        StringBuilder parameters = new StringBuilder();
                        if (numParameters > 0) {
                            int counter = 1;
                            int indexVal = 2;
                            while (counter < numParameters){
                                String paramType = splitValue[indexVal];
                                String paramName = splitValue[indexVal+1];
                                if (parameters.length() == 0) {
                                    parameters = new StringBuilder(paramType + " " + paramName);
                                } else {
                                    parameters.append(", ").append(paramType).append(" ").append(paramName);
                                }
                                indexVal += 2;
                                counter++;
                            }
                            result = spacing(index) + "def " + splitValue[1] + "(" + parameters + ")\n"  +
                                    treeParserPython(list, index+1);
                        }
                        break;
                    case "PRINT":
                        result =  spacing(index) + "print('" + splitValue[1] + "')" + treeParserPython(list, index+1);
                        break;
                    case "VARIABLE":
                        result = spacing(index) + splitValue[1] + " = " + splitValue[2] + "\n" + treeParserPython(list, index+1);
                        break;
                    case "IF":
                        result = spacing(index) + "if "+parseCondition(splitValue)+": \n"+treeParserPython(list, index+1);
                        break;
                    case "ELIF":
                        result = spacing(index) + "elif " + splitValue[1] + ": \n" + treeParserPython(list, index + 1);
                        break;
                    case "ELSE":
                        result = spacing(index) + "else: " + treeParserPython(list, index + 1);
                        break;
                    case "FOR":
                        result = spacing(index) + "for " + splitValue[1] + " in " + splitValue[2] + ":\n" + treeParserPython(list, index + 1);
                        break;
                    case "WHILE":
                        result = spacing(index) + "while " + parseCondition(splitValue) + ":\n" + treeParserPython(list, index + 1);
                        break;
                    case "ARRAY":
                        result = spacing(index) + splitValue[1] + " = " + treeParserPython(list, index + 1);
                        break;
                    default:
                        result = "Invalid Command";
                }
            }

        }
        return result;
    }

    private static String parseOperand(String operand) {
        operand = operand.toLowerCase();
        switch (operand){
            case "add":
                return "+";
            case "subtract":
                return "-";
            case "multiply":
                return "*";
            case "divide":
                return "/";
            case "modulo":
                return "%";
            default:
                return "";
        }
    }

    private static String parseCondition(String[] condition) {
        StringBuilder conditionVal = new StringBuilder();
        for (String word: condition) {
            switch (word) {
                case "greater":
                    conditionVal.append(" > ");
                    break;
                case "lesser":
                    conditionVal.append(" < ");
                    break;
                case "equal":
                    conditionVal.append(" == ");
                    break;
                case "not":
                    conditionVal.insert(0, "!");
                    break;
                case "and":
                    conditionVal.append(" and ");
                    break;
                case "or":
                    conditionVal.append(" or ");
                    break;
                default:
                    conditionVal.append(word);
            }
        }
        return conditionVal.toString();
    }

    private static int parseNumber(String numberString) {
        int resultValue = 0;
        if (!isInt(numberString)){
            switch (numberString.toUpperCase()){
                case "ONE":
                    resultValue = 1;
                    break;
                case "TWO":
                    resultValue = 2;
                    break;
                case "THREE":
                    resultValue = 3;
                    break;
                case "FOUR":
                    resultValue = 4;
                    break;
                case "FIVE":
                    resultValue = 5;
                    break;
            }
        }
        else {
            resultValue = Integer.parseInt(numberString);
        }
        return resultValue;
    }

    private static boolean isInt(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    private static String spacing(int index) {
        StringBuilder str = new StringBuilder();
        for (int i  = 0; i < index; i++) {
            str.append("\t");
        }
        return str.toString();
    }

}
