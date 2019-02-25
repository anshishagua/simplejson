# simplejson

simplejson is a library that parse to convert to json

json语法定义
/** Taken from "The Definitive ANTLR 4 Reference" by Terence Parr */

// Derived from http://json.org
grammar JSON;

json
   : value EOF
   ;

obj
   : '{' pair (',' pair)* '}'
   | '{' '}'
   ;

pair
   : STRING ':' value
   ;

array
   : '[' value (',' value)* ']'
   | '[' ']'
   ;

value
   : STRING
   | NUMBER
   | obj
   | array
   | TRUE
   | FALSE
   | NULL
   ;

STRING
   : '"' (ESC | SAFECODEPOINT)* '"'
   ;

TRUE: 'true';
FALSE: 'false';
NULL: 'null';

fragment ESC
   : '\\' (["\\/bfnrt] | UNICODE)
   ;
fragment UNICODE
   : 'u' HEX HEX HEX HEX
   ;
fragment HEX
   : [0-9a-fA-F]
   ;
fragment SAFECODEPOINT
   : ~ ["\\\u0000-\u001F]
   ;


NUMBER
   : '-'? INT ('.' [0-9] +)? EXP?
   ;


fragment INT
   : '0' | [1-9] [0-9]*
   ;

// no leading zeros

fragment EXP
   : [Ee] [+\-]? INT
   ;

// \- since - means "range" inside [...]

WS
   : [ \t\n\r] + -> skip
;

基本类型: boolean, byte, char, short, int, long, float, double
基本数组类型: boolean[], byte[], char[], short[], int[], long[], float[], double[]
枚举类型: Enum

JSON数据类型:

1. null
2. boolean: true, false
3. number
4. array
5. object
