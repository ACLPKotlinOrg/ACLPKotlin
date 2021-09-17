package com.nlp.aclpkotlin

import java.util.*

/*
 * Notes for processing commands:
 * "Function $name $numberOfParameters (n)$parameterType $parameterName $return"
 * "If/else if $condition"
 * "Variable $type $name $value"
 * "Set $name $value"
 * "For $initialValue $comparedValue $inc/dec"
 * "While $initialValue $comparedValue $condition"
 * "Print $value"
 * ""
 * "Return $value"
 * Update $operator $operand $value
 */

/*
TODO: Create a parser for Python, C++, C, C#, Javascript, Swift
 */

class ACLPMethods {
    companion object {
        var variableList = mutableListOf<String>()
        //should have methods to load commands depending on language
        fun treeParser(list: List<KVObject>, index: Int = 0): String{
            return if (index < list.size){
                var result = ""
                val item = list[index]
                if (item.key == index){
                    val splitValue = item.value.split(" ").toTypedArray()
                    when(item.type){
                        "UPDATE" -> {
                            result = "${splitValue[2]} = ${splitValue[2]} ${parseOperand(splitValue[1])} ${splitValue[3]}"+
                                    treeParser(list, index+1)
                        }
                        "END" -> {
                            result = "} + \n"// + treeParser(list, index+1) //most likely won't need the second part
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
                            result = "public ${splitValue[splitValue.size - 1]} ${splitValue[1]} ($params){\n" +
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
                        else -> result = "//Invalid Command: ${item.value}"
                    }
                }
                result
            } else {
                ""
            }
        }

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

        private fun parseCondition(condition: Array<String>): String{
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
            return returnString
        }

        private fun parseNumber(numberString: String): Int{
            return if (numberString.toIntOrNull() == null){
                var numberInt = 0
                when(numberString.uppercase()){
                    "ONE" -> numberInt = 1
                    "TWO" -> numberInt = 1
                    "THREE" -> numberInt = 1
                    "FOUR" -> numberInt = 1
                    "FIVE" -> numberInt = 1
                }
                numberInt
            } else {
                numberString.toInt()
            }
        }
    }
}

class KVObject {
    var id = UUID.randomUUID().toString()
    var key: Int = 0
    lateinit var value: String
    lateinit var type: String
}