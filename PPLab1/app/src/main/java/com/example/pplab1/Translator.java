package com.example.pplab1;


public class Translator {
    private static String parseOne(String number, boolean isThousand){
        String response = "";
        switch (number.charAt(0)){
            case '1':
                if (isThousand){
                    response += "одна ";
                }else{
                    response += "один ";
                }
                break;
            case '2':
                if (isThousand){
                    response += "две ";
                }else{
                    response += "два ";
                }
                break;
            case '3':
                response += "три ";
                break;
            case '4':
                response += "четыре ";
                break;
            case '5':
                response += "пять ";
                break;
            case '6':
                response += "шесть ";
                break;
            case '7':
                response += "семь ";
                break;
            case '8':
                response += "восемь ";
                break;
            case '9':
                response += "девять ";
                break;
        }
        return response;
    }
    private static String parseTen(String numbers, boolean isThousand){
        String response = "";
        if(numbers.charAt(0) == '1'){
            switch (numbers.charAt(1)){
                case '0':
                    response += "десять ";
                    break;
                case '1':
                    response += "одинадцать ";
                    break;
                case '2':
                    response += "двенадцать ";
                    break;
                case '3':
                    response += "тринадцать ";
                    break;
                case '4':
                    response += "четырнадцать ";
                    break;
                case '5':
                    response += "пятнадцать ";
                    break;
                case '6':
                    response += "шестнадцать ";
                    break;
                case '7':
                    response += "семьнадцать ";
                    break;
                case '8':
                    response += "восемьнадцать ";
                    break;
                case '9':
                    response += "девятнадцать ";
                    break;
            }
        }else{
            switch (numbers.charAt(0)){
                case '2':
                    response += "двадцать ";
                    break;
                case '3':
                    response += "тридцать ";
                    break;
                case '4':
                    response += "сорок ";
                    break;
                case '5':
                    response += "пятьдесят ";
                    break;
                case '6':
                    response += "шестьдесят ";
                    break;
                case '7':
                    response += "семьдесят ";
                    break;
                case '8':
                    response += "восемьдесят ";
                    break;
                case '9':
                    response += "девяносто ";
                    break;
            }
            response += parseOne(numbers.substring(1,2), isThousand);
        }
        return response;
    }
    private static String parseHundred(String numbers, boolean isThousand){
        String response = "";
        switch (numbers.charAt(0)){
            case '1':
                response += "сто ";
                break;
            case '2':
                response += "двести ";
                break;
            case '3':
                response += "триста ";
                break;
            case '4':
                response += "четыреста ";
                break;
            case '5':
                response += "пятьсот ";
                break;
            case '6':
                response += "шестьсот ";
                break;
            case '7':
                response += "семьсот ";
                break;
            case '8':
                response += "восемьмсот ";
                break;
            case '9':
                response += "девятьсот ";
                break;
        }
        response += parseTen(numbers.substring(1,3), isThousand);
        return response;
    }
    public static String parseString(String numbers){
        String response = ""; //*** ***
        if (numbers.length() > 6){
            return "один миллион";
        }else{
            while(numbers.length() < 6){
                numbers = "0" + numbers;
            }
            response += parseHundred(numbers.substring(0,3),true);
            if(numbers.charAt(2) == '1'){
                response += "тысяча ";
            }else if(numbers.charAt(2) == '2' || numbers.charAt(2) == '3' || numbers.charAt(2) == '4'){
                response += "тысячи ";
            }else if(numbers.charAt(2) == '0'){
                response += "";
            }
            else{
                response += "тысяч ";
            }
            response += parseHundred(numbers.substring(3,6), false);
        }
        return response;
    }
    public static String parseInt(int number){
        return parseString(Integer.toString(number));
    }
}
