package com.nlp.aclpkotlin

import android.util.Log

/*
 * Notes for processing commands:
 * "Function $name $numberOfParameters (n)$parameterType $parameterName $return"
 * "If/else if $condition"
 * "Variable $type $name $value"
 * "Set $name $value"
 * "For $initialValue $comparedValue $inc/dec"
 * "While $initialValue $comparedValue $condition"
 * "Print $value"
 * "Return $value"
 * Update $operator $operand $value
 * Array $name $type $size
 */

/*
TODO: Create a parser for Python, C++, C, C#, Javascript, Swift
 */

/**
 * ACLPMethods will contain the main methods for parsing and organizing the commands from the list.
 */
class ACLPMethods {
    companion object {
        private var variableList = mutableListOf<String>()

        /**
         * @author Viraj Patel
         * @param list: List<KVObject>
         * @param index: Int, default value is 0 for the initial call(Optional)
         * @return String
         * @exception list should not be null
         * treeParser takes the parameters defined above and uses recursion to check the type to set
         * the result to the equivalent values.
         */
        fun treeParser(list: List<KVObject>, index: Int = 0): String {
            return if (index < list.size){
                var result = ""
                val item = list[index]
                if (item.key == index){
                    val splitValue = item.value.split(" ").toTypedArray()
                    Log.e("skndflknsd", item.value)
                    item.type = splitValue[0].uppercase()
                    when(item.type){
                        "UPDATE" -> {
                            result = "${splitValue[2]} = ${splitValue[2]} ${parseOperand(splitValue[1])} ${splitValue[3]}"+
                                    treeParser(list, index+1)
                        }
                        "END" -> {
                            result = "} \n"// + treeParser(list, index+1) //most likely won't need the second part
                        }
                        "SET" -> {
                            for (variable in variableList){
                                result = if(splitValue[1] == variable)
                                    "$variable = ${splitValue[2]}"
                                else
                                    "${splitValue[1]} = ${splitValue[2]}"
                            }
                        }
                        "CLASS" -> {
                            if (splitValue[1].isNotEmpty()){
                                result = "public class ${splitValue[0]}{\n" +
                                        treeParser(list, index+1)// + "\n}"
                            }
                        }
                        "MAIN" -> {
                            result = "public static void main(String args[]){\n" +
                                    treeParser(list, index+1)// + "\n}"
                        }
                        "FUNCTION" -> {
                            val numOfParams = parseNumber(splitValue[2])
                            var params = ""
                            if (numOfParams > 0) {
                                var counter = 1
                                var index = 2
                                while (counter < numOfParams){
                                    val paramType = splitValue[index]
                                    val paramName = splitValue[index+1]
                                    params = if (params.isEmpty())
                                        "$paramType $paramName"
                                    else
                                        "$params, $paramType $paramName"
                                    index += 2
                                    counter ++
                                }
                            }
                            result = "public ${splitValue[splitValue.size - 1]} ${splitValue[1]}($params){\n" +
                                    treeParser(list, index+1)
                        }
                        "PRINT" -> {
                            result = "System.out.println(\"${splitValue[1]}\");" + treeParser(list, index+1)
                        }
                        "STRING" -> {
                            variableList.add(splitValue[1])
                            result = "String ${splitValue[1]} = ${splitValue[2]}; \n" + treeParser(list, index+1)
                        }
                        "DOUBLE" -> {
                            variableList.add(splitValue[1])
                            result = "double ${splitValue[1]} = ${splitValue[2]}; \n" + treeParser(list, index+1)
                        }
                        "FLOAT" -> {
                            variableList.add(splitValue[1])
                            result = "float ${splitValue[1]} = ${splitValue[2]}; \n" + treeParser(list, index+1)
                        }
                        "INT" -> {
                            variableList.add(splitValue[1])
                            result = "int ${splitValue[1]} = ${splitValue[2]}; \n" + treeParser(list, index+1)
                        }
                        "BOOLEAN" -> {
                            variableList.add(splitValue[1])
                            result = "boolean ${splitValue[1]} = ${splitValue[2]}; \n" + treeParser(list, index+1)
                        }
                        "IF" -> {
                            result = "if(${parseCondition(splitValue)}){\n" +
                                    treeParser(list, index+1)// + "\n}"
                        }
                        "ELSE IF" -> {
                            result = "else if(${splitValue[1]}){\n" +
                                    treeParser(list, index + 1)// + "\n}"
                        }
                        "ELSE" -> {
                            result = "else{\n" +
                                    treeParser(list, index + 1)// + "\n}"
                        }
                        "FOR" -> {
                            //"For $initialValue $comparedValue $inc/dec"
                            val condition = if(splitValue[3].uppercase() == "INCREASING"){
                                "temp = ${splitValue[1]}; temp < ${splitValue[2]}; temp++"
                            }else{
                                "temp = ${splitValue[1]}; temp < ${splitValue[2]}; temp--"
                            }
                            result = "for($condition){\n" +
                                    treeParser(list, index + 1)// + "\n}"
                        }
                        "WHILE" -> {
                            //"While $initialValue $comparedValue $condition
                            result = "while(${parseCondition(splitValue)}){\n" +
                                    treeParser(list, index+1)// + "\n}"
                        }
                        "ARRAY" -> {
                            //Example
                            //0, 1, 2, 3
                            //Array $name, $type, $size
                            //int[] arrayName = new int[size];
                            result = "${splitValue[2]}[] ${splitValue[1]} = new ${splitValue[2]}[${splitValue[3]}];" +
                                    treeParser(list, index + 1)
                        }
                        else -> result = "//Invalid Command: ${item.value}"
                    }
                }
                result
            } else {
                ""
            }
        }

        /**
         * @author Viraj Patel
         * @param operand: String
         * @return String
         * @exception operand should not be null
         * @sample operand = "add" -> "+"
         * @sample operand = "value" -> "value"
         * parseOperand checks the parameter String operand and returns the equivalent value.
         */
        private  fun parseOperand(operand: String) : String {
            operand.lowercase()
            return when(operand){
                "add" -> "+"
                "multiply" -> "*"
                "subtract" -> "-"
                "divide" -> "/"
                "module" -> "%"
                else -> ""
            }
        }

        /**
         * @author Viraj Patel
         * @param condition: Array<String>
         * @return String
         * @exception condition should not be null
         * @sample condition = ["1", "great", "0"] -> 1>0
         * @sample condition = ["nor", "isValue"] -> !isValue
         * parseCondition uses the parameter condition array to return equivalent output,
         * look at the samples for examples.
         */
        public fun parseCondition(condition: Array<String>): String{
            var returnString = ""
            for(word in condition){
                word.lowercase()
                when(word){
                    "great" -> returnString = "$returnString> "
                    "less" -> returnString = "$returnString> "
                    "equal" -> returnString = "$returnString= "
                    "nor" -> returnString = "!$returnString "
                    "and" -> returnString = "$returnString &&"
                    "or" -> returnString = "$returnString ||"
                    else -> returnString += word
                }
            }
            return returnString0
        }

        /**
         * @author Viraj Patel
         * @param numberString: String
         * @return Integer
         * @exception numberString should not be null
         * @sample numberString = "three" -> "3"
         * @sample numberString = "5" -> "5"
         * parseNumber takes the String value of number and checks for the equivalent digit
         * if it can't find one within the range, then it returns the original value.
         */
        private fun parseNumber(numberString: String): Int{
            return if (numberString.toIntOrNull() == null){
                var numberInt = 0
                when(numberString.uppercase()){
                    "ONE" -> numberInt = 1
                    "TWO" -> numberInt = 2
                    "THREE" -> numberInt = 3
                    "FOUR" -> numberInt = 4
                    "FIVE" -> numberInt = 5
                }
                numberInt
            } else {
                numberString.toInt()
            }
        }
    }
}