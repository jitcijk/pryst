grammar Pryst;

compileUnit:
	declaration* EOF;

declaration:
    functionDeclaration | variableDeclaration;

/* We use rule this even for void methods which cannot have [] after parameters. This simplifies
 grammar and we can consider void to be a type, which renders the [] matching as a context-sensitive
 issue or a semantic check for invalid return type after parsing.
 */
functionDeclaration:
	typeTypeOrVoid IDENTIFIER formalParameters functionBody;

functionBody: block | ';';

typeTypeOrVoid: typeType | VOID;

variableDeclaration: typeType variableDeclarators ';';

variableDeclarators:
	variableDeclarator (',' variableDeclarator)*;

variableDeclarator:
	variableDeclaratorId ('=' variableInitializer)?;

variableDeclaratorId: IDENTIFIER ('[' ']')*;

variableInitializer: arrayInitializer | expression;

arrayInitializer:
	'{' (variableInitializer (',' variableInitializer)* (',')?)? '}';

formalParameters: s='(' formalParameterList? ')';

formalParameterList:
	formalParameter (',' formalParameter)*;

formalParameter: typeType variableDeclaratorId;

qualifiedName: IDENTIFIER;

literal:
	integerLiteral
	| floatLiteral
	| CHAR_LITERAL
	| STRING_LITERAL
	| BOOL_LITERAL
	| NULL_LITERAL;

integerLiteral:
	DECIMAL_LITERAL
	| HEX_LITERAL
	| OCT_LITERAL
	| BINARY_LITERAL;

floatLiteral: FLOAT_LITERAL | HEX_FLOAT_LITERAL;

elementValue: expression | elementValueArrayInitializer;

elementValueArrayInitializer:
	'{' (elementValue (',' elementValue)*)? (',')? '}';

// STATEMENTS / BLOCKS

block: '{' blockStatement* '}';

blockStatement:
	localVariableDeclaration ';'
	| statement;

localVariableDeclaration: typeType variableDeclarators;

statement:
	blockLabel = block
	| ifStatement
	| forStatement
	| whileStatement
	| doWhileStatement
	| tryCatchStatement
	| switchStatement
	| returnStatement
	| throwStatement
	| breakStatement
	| continueStatement
	| SEMI
	| statementExpression = expression ';';

ifStatement:
    IF parExpression statement (ELSE statement)?;

forStatement: FOR '(' forControl ')' statement;

whileStatement: WHILE parExpression statement;

doWhileStatement: DO statement WHILE parExpression ';';

tryCatchStatement: TRY block (catchClause+ finallyBlock? | finallyBlock);

switchStatement: SWITCH parExpression '{' switchBlockStatementGroup* switchLabel* '}';

returnStatement: RETURN expression? ';';

throwStatement: THROW expression ';';

breakStatement: BREAK IDENTIFIER? ';';

continueStatement: CONTINUE IDENTIFIER? ';';


catchClause: CATCH '(' catchType IDENTIFIER ')' block;

catchType: qualifiedName ('|' qualifiedName)*;

finallyBlock: FINALLY block;

/** Matches cases then statements, both of which are mandatory. To handle empty cases at the end, we
 * add switchLabel* to statement.
 */
switchBlockStatementGroup: switchLabel+ blockStatement+;

switchLabel:
	CASE (
		constantExpression = expression
		| enumConstantName = IDENTIFIER
	) ':'
	| DEFAULT ':';

forControl:
	enhancedForControl
	| forInit? ';' expression? ';' forUpdate = expressionList?;

forInit: localVariableDeclaration | expressionList;

enhancedForControl:
	typeType variableDeclaratorId ':' expression;

// EXPRESSIONS

parExpression: '(' expression ')';

expressionList: expression (',' expression)*;

functionCall:
	IDENTIFIER '(' expressionList? ')';

expression:
	primary
	| expression '[' expression ']'
	| functionCall
	| NEW creator
	| '(' typeType ')' expression
	| expression postfix = ('++' | '--')
	| prefix = ('+' | '-' | '++' | '--') expression
	| prefix = ('~' | '!') expression
	| expression bop = ('*' | '/' | '%') expression
	| expression bop = ('+' | '-') expression
	| expression bop = ('<=' | '>=' | '>' | '<') expression
	| expression bop = ('==' | '!=') expression
	| expression bop = '&' expression
	| expression bop = '^' expression
	| expression bop = '|' expression
	| expression bop = '&&' expression
	| expression bop = '||' expression
	| expression bop = ('<<' | '>>') expression
	| <assoc = right> expression bop = '?' expression ':' expression
	| <assoc = right> expression bop = (
		'='
		| '+='
		| '-='
		| '*='
		| '/='
		| '&='
		| '|='
		| '^='
		| '>>='
		| '<<='
		| '%='
	) expression
	| lambdaExpression;

lambdaExpression: lambdaParameters '=>' lambdaBody;

lambdaParameters:
	IDENTIFIER
	| '(' formalParameterList? ')'
	| '(' IDENTIFIER (',' IDENTIFIER)* ')';

lambdaBody: expression | block;

primary:
	'(' expression ')'
	| THIS
	| literal
	| IDENTIFIER
	;

creator:
	createdName arrayCreatorRest;

createdName: primitiveType;

arrayCreatorRest:
	'[' (
		']' ('[' ']')* arrayInitializer
		| expression ']' ('[' expression ']')* ('[' ']')*
	);

typeType: primitiveType ('[' ']')*;

primitiveType:
	BOOL
	| CHAR
	| STR
	| INT
	| FLOAT;

// Keywords

BOOL: 'bool';
BREAK: 'break';
CASE: 'case';
CATCH: 'catch';
CHAR: 'char';
CONST: 'const';
CONTINUE: 'continue';
DEFAULT: 'default';
DO: 'do';
DOUBLE: 'double';
ELSE: 'else';
ENUM: 'enum';
FINALLY: 'finally';
FLOAT: 'float';
FOR: 'for';
IF: 'if';
IMPORT: 'import';
INT: 'int';
LONG: 'long';
NEW: 'new';
RETURN: 'return';
STATIC: 'static';
STR: 'str';
SWITCH: 'switch';
THIS: 'this';
THROW: 'throw';
TRY: 'try';
VOID: 'void';
WHILE: 'while';

// Literals

DECIMAL_LITERAL: ('0' | [1-9] (Digits? | '_'+ Digits)) [lL]?;
HEX_LITERAL:
	'0' [xX] [0-9a-fA-F] ([0-9a-fA-F_]* [0-9a-fA-F])? [lL]?;
OCT_LITERAL: '0' '_'* [0-7] ([0-7_]* [0-7])? [lL]?;
BINARY_LITERAL: '0' [bB] [01] ([01_]* [01])? [lL]?;

FLOAT_LITERAL: (Digits '.' Digits? | '.' Digits) ExponentPart? [fFdD]?
	| Digits (ExponentPart [fFdD]? | [fFdD]);

HEX_FLOAT_LITERAL:
	'0' [xX] (HexDigits '.'? | HexDigits? '.' HexDigits) [pP] [+-]? Digits [fFdD]?;

BOOL_LITERAL: 'true' | 'false';

CHAR_LITERAL: '\'' (~['\\\r\n] | EscapeSequence) '\'';

STRING_LITERAL: '"' (~["\\\r\n] | EscapeSequence)* '"';
NULL_LITERAL: 'null';
// Separators
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
LBRACK: '[';
RBRACK: ']';
SEMI: ';';
COMMA: ',';
DOT: '.';
// Operators
ASSIGN: '=';
GT: '>';
LT: '<';
BANG: '!';
TILDE: '~';
QUESTION: '?';
COLON: ':';
EQUAL: '==';
LE: '<=';
GE: '>=';
NOTEQUAL: '!=';
AND: '&&';
OR: '||';
INC: '++';
DEC: '--';
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
BITAND: '&';
BITOR: '|';
CARET: '^';
MOD: '%';
LSHIFT: '<<';
RSHIFT: '>>';
ADD_ASSIGN: '+=';
SUB_ASSIGN: '-=';
MUL_ASSIGN: '*=';
DIV_ASSIGN: '/=';
AND_ASSIGN: '&=';
OR_ASSIGN: '|=';
XOR_ASSIGN: '^=';
MOD_ASSIGN: '%=';
LSHIFT_ASSIGN: '<<=';
RSHIFT_ASSIGN: '>>=';
ARROW: '=>';
// Whitespace and comments
WS: [ \t\r\n\u000C]+ -> channel(HIDDEN);
COMMENT: '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT: '//' ~[\r\n]* -> channel(HIDDEN);

// Identifiers

IDENTIFIER: Letter LetterOrDigit*;

// Fragment rules

fragment ExponentPart: [eE] [+-]? Digits;

fragment EscapeSequence:
	'\\' [btnfr"'\\]
	| '\\' ([0-3]? [0-7])? [0-7]
	| '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit;
fragment HexDigits: HexDigit ((HexDigit | '_')* HexDigit)?;
fragment HexDigit: [0-9a-fA-F];
fragment Digits: [0-9] ([0-9_]* [0-9])?;
fragment LetterOrDigit: Letter | [0-9];
fragment Letter:
	[a-zA-Z$_] // these are the "pryst letters" below 0x7F
	| ~[\u0000-\u007F\uD800-\uDBFF] // covers all characters above 0x7F which are not a surrogate
	| [\uD800-\uDBFF] [\uDC00-\uDFFF];
// covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF