# simplejson

simplejson是一个纯java实现不依赖于第三方库的java库,实现了json的解析、序列化和反序列化。



基本类型: boolean, byte, char, short, int, long, float, double
基本数组类型: boolean[], byte[], char[], short[], int[], long[], float[], double[]
枚举类型: Enum

JSON数据类型:

1. null
2. boolean: true, false
3. number
4. array
5. object

相应的，我们定义了和这几种数据类型对应的JAVA类，分别为
1. JSONNull
2. JSONBoolean
3. JSONNumber
4. JSONArray
5. JSONObject

它们都实现了JSONValue接口

1. JSON解析
主要是根据json.org里对json的文法定义解析json字符串
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

具体实现中,词法分析和语法分析是一次扫描中完成的,采用递归下降语法分析
JSONScanner负责json字符串解析,返回JSONValue对象为解析后的结果 
入口方法parse
忽略掉空格(非字符串中的空格)读取下一个字符,如果是
1. '['   说明可能是array,调用parseJSONArray

while (没有达到字符串末尾) {
    读取下一个字符, 如果不是'[',抛出异常
    
    while (没有达到字符串末尾) {
        value = parse()
        
        读取下一个字符, 
        (1) 如果是',' 添加value到JSONArray continue
        (2) 如果是']' 返回JSONArray
        (3) 抛出异常
    }
}

没有遇到结束']',抛出异常


2. '{'   说明可能是object,调用parseJSONObject

while (没有达到字符串末尾) {
    读取下一个字符, 如果不是'{',抛出异常
    
    while (没有达到字符串末尾) {
        key = parseString()
        
        读取下一个字符, 如果不是':',抛出异常
        
        value = parse()
        
        读取下一个字符
        (1) 如果是',' 添加key,value到JSONObject, continue
        (2) 如果是'}' 返回JSONObject
        (3) 抛出异常
    }
}

没有遇到结束'}',抛出异常

3. '"'   说明可能是string,调用parseJSONString

while (没有达到字符串末尾) {
    读取下一个字符, 如果不是'"',抛出异常
    
    while (没有达到字符串末尾) {                        
        读取下一个字符
        (1) 如果是'"' 返回JSONString
        (2) 如果不是'\' 添加到JSONString continue 
        (3) 是'\'
            如果达到字符串结尾,抛出异常
            读取下一个字符
            (1) 如果是'\', '"', '\b', '\f', '\r', '\n', '\t' 添加到JSONString continue
            (2) 如果是'u' 读取接下来的4个字符,如果到达字符串末尾,抛出异常,否则添加到JSONString continue
            (3) 非法的转义字符,抛出异常
    }
}

没有遇到结束'"',抛出异常

4. 'n'   说明可能是null,调用parseJSONNull

读取接下来的4个字符

(1) 不是null,抛出异常
(2) 发挥JSONNull.NULL

5. 't'   说明可能是true,调用parseJSONBoolean
6. 'f'   说明可能是false,调用parseJSONBoolean

读取下一个字符
(1) t
    读取接下来3个字符
    (1) 不是ure,或者达到字符串结尾,抛出异常
    (2) 返回JSONBoolean
(2) f
    读取接下来4个字符
    (1) 不是alse,或者达到字符串结尾,抛出异常
    (2) 返回JSONBoolean

7. '-0123456789' 说明可能是number,调用parseJSONNumber
读取下一个字符,如果不是'-0123456789',抛出异常

正则表达式匹配数

判断是否有'.'
(1) 有
   返回Double.parseDouble()
   抛出异常,返回Bigdecimal
(2) 没有
   返回Integer.parseInt()
   抛出异常,返回Long.parseLong()
   抛出异常,返回BigInteger

8. 非1-7,说明是非法JSON,抛出JSONException。实现中,JSONException是一个运行时异常,
因此用户不需要显式捕获,简化了代码的编写。当然为了预防可能出错,也是可以捕获的。

入口方法parse流程

读取下一个字符
如果到达字符串结尾,抛出异常

(1) '[' parseJSONArray
(2) '{' parseJSONObject
(3) '"' parseJSONString
(4) 't' parseJSONBoolean
(5) 'f' parseJSONBoolean
(6) 'n' parseJSONNull
(7) '-0123456789' parseJSONNumber
(8) 抛出异常

2. JSON序列化和反序列化
(1) 序列化 
Object序列化为String
(2) 反序列化