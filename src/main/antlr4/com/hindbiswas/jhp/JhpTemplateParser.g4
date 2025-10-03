parser grammar JhpTemplateParser;

options {
    tokenVocab=JhpTemplateLexer;
}

// Parser Rules
template
    : templateElement* EOF
    ;

templateElement
    : TEXT
    | echoStatement
    | rawEchoStatement
    | controlStatement
    ;

// Echo statements
echoStatement
    : ECHO_START expression ECHO_END
    ;

rawEchoStatement
    : RAW_ECHO_START expression RAW_ECHO_END
    ;

// Control statements
controlStatement
    : ifStatement
    | forStatement
    | foreachStatement
    | whileStatement
    | breakStatement
    | continueStatement
    | includeStatement
    ;

// If statement
ifStatement
    : STMT_START IF LPAREN expression RPAREN STMT_END
      templateElement*
      elseIfStatement*
      elseStatement?
      STMT_START ENDIF STMT_END
    ;

elseIfStatement
    : STMT_START ELSEIF LPAREN expression RPAREN STMT_END
      templateElement*
    ;

elseStatement
    : STMT_START ELSE STMT_END
      templateElement*
    ;

// For statement
forStatement
    : STMT_START FOR LPAREN forInit SEMICOLON expression SEMICOLON forUpdate RPAREN STMT_END
      templateElement*
      STMT_START ENDFOR STMT_END
    ;

forInit
    : IDENTIFIER ASSIGN expression
    ;

forUpdate
    : IDENTIFIER (PLUS_PLUS | MINUS_MINUS)
    | IDENTIFIER ASSIGN expression
    ;

// Foreach statement
foreachStatement
    : STMT_START FOREACH LPAREN (IDENTIFIER COMMA)? IDENTIFIER IN expression RPAREN STMT_END
      templateElement*
      STMT_START ENDFOREACH STMT_END
    ;

// While statement
whileStatement
    : STMT_START WHILE LPAREN expression RPAREN STMT_END
      templateElement*
      STMT_START ENDWHILE STMT_END
    ;

// Break and Continue
breakStatement
    : STMT_START BREAK STMT_END
    ;

continueStatement
    : STMT_START CONTINUE STMT_END
    ;

// Include statement
includeStatement
    : STMT_START INCLUDE STRING_LITERAL STMT_END
    ;

// Expressions
expression
    : primary                                                                       # PrimaryExpression
    | expression (LBRACKET | EXPR_LBRACKET) expression (RBRACKET | EXPR_RBRACKET)  # ArrayAccessExpression
    | expression (DOT | EXPR_DOT) (IDENTIFIER | EXPR_IDENTIFIER)                   # MemberAccessExpression
    | functionCall                                                                  # FunctionCallExpression
    | (LPAREN | EXPR_LPAREN) expression (RPAREN | EXPR_RPAREN)                     # ParenthesizedExpression
    | (NOT | EXPR_NOT) expression                                                   # NotExpression
    | (MINUS | EXPR_MINUS) expression                                               # UnaryMinusExpression
    | expression op=(MULTIPLY | DIVIDE | MODULO | EXPR_MULTIPLY | EXPR_DIVIDE | EXPR_MODULO) expression  # MultiplicativeExpression
    | expression op=(PLUS | MINUS | EXPR_PLUS | EXPR_MINUS) expression             # AdditiveExpression
    | expression op=(LT | LTE | GT | GTE | EXPR_LT | EXPR_LTE | EXPR_GT | EXPR_GTE) expression    # RelationalExpression
    | expression op=(EQ | NEQ | EXPR_EQ | EXPR_NEQ) expression                     # EqualityExpression
    | expression (AND | EXPR_AND) expression                                        # AndExpression
    | expression (OR | EXPR_OR) expression                                          # OrExpression
    | <assoc=right> expression (QUESTION | EXPR_QUESTION) expression (COLON | EXPR_COLON) expression  # TernaryExpression
    ;

primary
    : IDENTIFIER
    | NUMBER
    | STRING_LITERAL
    | BOOLEAN_LITERAL
    | NULL
    | EXPR_IDENTIFIER
    | EXPR_NUMBER
    | EXPR_STRING_LITERAL
    | EXPR_BOOLEAN_LITERAL
    | EXPR_NULL
    ;

functionCall
    : (IDENTIFIER | EXPR_IDENTIFIER) (LPAREN | EXPR_LPAREN) argumentList? (RPAREN | EXPR_RPAREN)
    ;

argumentList
    : expression ((COMMA | EXPR_COMMA) expression)*
    ;
