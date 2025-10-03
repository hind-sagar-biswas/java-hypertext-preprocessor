lexer grammar JhpTemplateLexer;

// Template delimiters
RAW_ECHO_START  : '{{{' -> pushMode(EXPRESSION_MODE);
ECHO_START      : '{{' -> pushMode(EXPRESSION_MODE);
STMT_START      : '{%' -> pushMode(STATEMENT_MODE);
COMMENT_START   : '{#' -> pushMode(COMMENT_MODE);

// Text content (anything outside of template tags)
TEXT
    : (~[{])+
    | '{'
    ;

// Expression Mode (for {{ }} and {{{ }}})
mode EXPRESSION_MODE;
RAW_ECHO_END    : '}}}' -> popMode;
ECHO_END        : '}}' -> popMode;

EXPR_LPAREN     : '(' ;
EXPR_RPAREN     : ')' ;
EXPR_LBRACKET   : '[' ;
EXPR_RBRACKET   : ']' ;
EXPR_DOT        : '.' ;
EXPR_COMMA      : ',' ;
EXPR_QUESTION   : '?' ;
EXPR_COLON      : ':' ;

// Operators in expression mode
EXPR_PLUS       : '+' ;
EXPR_MINUS      : '-' ;
EXPR_MULTIPLY   : '*' ;
EXPR_DIVIDE     : '/' ;
EXPR_MODULO     : '%' ;
EXPR_LT         : '<' ;
EXPR_LTE        : '<=' ;
EXPR_GT         : '>' ;
EXPR_GTE        : '>=' ;
EXPR_EQ         : '==' ;
EXPR_NEQ        : '!=' ;
EXPR_AND        : '&&' ;
EXPR_OR         : '||' ;
EXPR_NOT        : '!' ;

// Literals in expression mode
EXPR_NUMBER
    : [0-9]+ ('.' [0-9]+)?
    ;

EXPR_STRING_LITERAL
    : '"' (~["\\\r\n] | '\\' .)* '"'
    | '\'' (~['\\\r\n] | '\\' .)* '\''
    ;

EXPR_BOOLEAN_LITERAL
    : 'true'
    | 'false'
    ;

EXPR_NULL
    : 'null'
    ;

EXPR_IDENTIFIER
    : [a-zA-Z_][a-zA-Z0-9_]*
    ;

EXPR_WS
    : [ \t\r\n]+ -> skip
    ;

// Statement Mode (for {% %})
mode STATEMENT_MODE;
STMT_END        : '%}' -> popMode;

// Keywords
IF              : 'if' ;
ELSEIF          : 'elseif' ;
ELSE            : 'else' ;
ENDIF           : 'endif' ;
FOR             : 'for' ;
ENDFOR          : 'endfor' ;
FOREACH         : 'foreach' ;
ENDFOREACH      : 'endforeach' ;
WHILE           : 'while' ;
ENDWHILE        : 'endwhile' ;
BREAK           : 'break' ;
CONTINUE        : 'continue' ;
INCLUDE         : 'include' ;
BLOCK           : 'block' ;
ENDBLOCK        : 'endblock' ;
IN              : 'in' ;

// Delimiters and operators in statement mode
LPAREN          : '(' ;
RPAREN          : ')' ;
LBRACKET        : '[' ;
RBRACKET        : ']' ;
DOT             : '.' ;
COMMA           : ',' ;
SEMICOLON       : ';' ;
QUESTION        : '?' ;
COLON           : ':' ;

PLUS            : '+' ;
MINUS           : '-' ;
MULTIPLY        : '*' ;
DIVIDE          : '/' ;
MODULO          : '%' ;
LT              : '<' ;
LTE             : '<=' ;
GT              : '>' ;
GTE             : '>=' ;
EQ              : '==' ;
NEQ             : '!=' ;
AND             : '&&' ;
OR              : '||' ;
NOT             : '!' ;
ASSIGN          : '=' ;
PLUS_PLUS       : '++' ;
MINUS_MINUS     : '--' ;

// Literals in statement mode
NUMBER
    : [0-9]+ ('.' [0-9]+)?
    ;

STRING_LITERAL
    : '"' (~["\\\r\n] | '\\' .)* '"'
    | '\'' (~['\\\r\n] | '\\' .)* '\''
    ;

BOOLEAN_LITERAL
    : 'true'
    | 'false'
    ;

NULL
    : 'null'
    ;

IDENTIFIER
    : [a-zA-Z_][a-zA-Z0-9_]*
    ;

WS
    : [ \t\r\n]+ -> skip
    ;

// Comment Mode
mode COMMENT_MODE;
COMMENT_END     : '#}' -> popMode;
COMMENT_TEXT    : . -> skip;
