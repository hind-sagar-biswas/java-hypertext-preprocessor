grammar Jhp;

// -------------------- Parser rules --------------------
template
    : element* EOF
    ;

element
    : text
    | interpolation
    | ifBlock
    | forBlock
    | letStmt
    | includeStmt
    ;

text
    : TEXT
    ;

// interpolation: {{ expr }}
interpolation
    : INTERP_OPEN expression INTERP_CLOSE
    ;

// if/else/endif block
ifBlock
    : STMT_OPEN IF expression STMT_CLOSE
      template
      (STMT_OPEN ELSE STMT_CLOSE template)?
      STMT_OPEN ENDIF STMT_CLOSE
    ;

// for ... in ... / endfor
forBlock
    : STMT_OPEN FOR ID IN expression STMT_CLOSE
      template
      STMT_OPEN ENDFOR STMT_CLOSE
    ;

// let declarations: inferred or typed
letStmt
    : STMT_OPEN LET ID ASSIGN expression STMT_CLOSE                // let x = expr
    | STMT_OPEN ID ID ASSIGN expression STMT_CLOSE                // Type id = expr
    ;

// include "file" with a=expr, b=expr
includeStmt
    : STMT_OPEN INCLUDE STRING (WITH argList)? STMT_CLOSE
    ;

argList
    : arg (COMMA arg)*
    ;

arg
    : ID ASSIGN expression
    ;

// -------------------- Expressions --------------------
expression
    : lambdaExpr
    ;

// lambda: x -> expr  or (x,y) -> expr
lambdaExpr
    : lambdaParams ARROW expression
    | logicalOrExpr
    ;

lambdaParams
    : ID
    | LPAR ID (COMMA ID)* RPAR
    ;

// logical / comparison / arithmetic precedence
logicalOrExpr
    : logicalAndExpr (OR logicalAndExpr)*
    ;

logicalAndExpr
    : equalityExpr (AND equalityExpr)*
    ;

equalityExpr
    : relationalExpr ((EQ | NEQ) relationalExpr)*
    ;

relationalExpr
    : additiveExpr ((LT | GT | LE | GE) additiveExpr)*
    ;

additiveExpr
    : multiplicativeExpr ((ADD | SUB) multiplicativeExpr)*
    ;

multiplicativeExpr
    : unaryExpr ((MUL | DIV | MOD) unaryExpr)*
    ;

unaryExpr
    : (NOT | SUB)? primaryExpr
    ;

primaryExpr
    : literal
    | ID memberAccess*
    | LPAR expression RPAR
    ;

// memberAccess: .field or .method(args) or [index]
memberAccess
    : DOT ID (LPAR (expression (COMMA expression)*)? RPAR)?
    | LBRACK expression RBRACK
    ;

// literals
literal
    : STRING
    | NUMBER
    | TRUE
    | FALSE
    | NULL
    ;

// -------------------- Lexer rules --------------------
INTERP_OPEN : '{{' ;
INTERP_CLOSE: '}}' ;
STMT_OPEN  : '{%' ;
STMT_CLOSE : '%}' ;

COMMENT_OPEN : '{#' ;
COMMENT_CLOSE: '#}' ;

// comments are skipped (non-greedy)
COMMENT : COMMENT_OPEN .*? COMMENT_CLOSE -> skip ;

// TEXT: any run of characters that does not start a tag ('{{','{%','{#')
// The rule below allows a single '{' that is not followed by '{', '%' or '#'.
TEXT
    : (~'{' | '{' ~('{' | '%' | '#'))+
    ;

// Keywords (make these explicit so they are tokenized separately from ID)
IF      : 'if' ;
ELSE    : 'else' ;
ENDIF   : 'endif' ;
FOR     : 'for' ;
ENDFOR  : 'endfor' ;
LET     : 'let' ;
IN      : 'in' ;
INCLUDE : 'include' ;
WITH    : 'with' ;
TRUE    : 'true' ;
FALSE   : 'false' ;
NULL    : 'null' ;

// Operators and punctuation
ARROW : '->' ;
OR    : '||' ;
AND   : '&&' ;
EQ    : '==' ;
NEQ   : '!=' ;
LE    : '<=' ;
GE    : '>=' ;
LT    : '<' ;
GT    : '>' ;
ADD   : '+' ;
SUB   : '-' ;
MUL   : '*' ;
DIV   : '/' ;
MOD   : '%' ;
NOT   : '!' ;
ASSIGN: '=' ;
COMMA : ',' ;
DOT   : '.' ;
LPAR  : '(' ;
RPAR  : ')' ;
LBRACK: '[' ;
RBRACK: ']' ;

// identifiers, numbers, strings
ID
    : [a-zA-Z_] [a-zA-Z0-9_]*
    ;

NUMBER
    : [0-9]+ ('.' [0-9]+)?
    ;

STRING
    : '"' (ESC | ~["\\])* '"'
    | '\'' (ESC | ~['\\])* '\''
    ;

// whitespace
WS : [ \t\r\n]+ -> skip ;

// fragments
fragment ESC
    : '\\' [btnfr"'\\]
    ;