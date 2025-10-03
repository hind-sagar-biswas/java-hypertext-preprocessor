// Generated from /home/shinigami/Java/java-hypertext-preprocessor/src/main/antlr4/com/hindbiswas/jhp/JhpTemplateParser.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class JhpTemplateParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ECHO_START=1, RAW_ECHO_START=2, STMT_START=3, COMMENT_START=4, TEXT=5, 
		ECHO_END=6, RAW_ECHO_END=7, EXPR_LPAREN=8, EXPR_RPAREN=9, EXPR_LBRACKET=10, 
		EXPR_RBRACKET=11, EXPR_DOT=12, EXPR_COMMA=13, EXPR_QUESTION=14, EXPR_COLON=15, 
		EXPR_PLUS=16, EXPR_MINUS=17, EXPR_MULTIPLY=18, EXPR_DIVIDE=19, EXPR_MODULO=20, 
		EXPR_LT=21, EXPR_LTE=22, EXPR_GT=23, EXPR_GTE=24, EXPR_EQ=25, EXPR_NEQ=26, 
		EXPR_AND=27, EXPR_OR=28, EXPR_NOT=29, EXPR_NUMBER=30, EXPR_STRING_LITERAL=31, 
		EXPR_BOOLEAN_LITERAL=32, EXPR_NULL=33, EXPR_IDENTIFIER=34, EXPR_WS=35, 
		STMT_END=36, IF=37, ELSEIF=38, ELSE=39, ENDIF=40, FOR=41, ENDFOR=42, FOREACH=43, 
		ENDFOREACH=44, WHILE=45, ENDWHILE=46, BREAK=47, CONTINUE=48, INCLUDE=49, 
		BLOCK=50, ENDBLOCK=51, IN=52, LPAREN=53, RPAREN=54, LBRACKET=55, RBRACKET=56, 
		DOT=57, COMMA=58, SEMICOLON=59, QUESTION=60, COLON=61, PLUS=62, MINUS=63, 
		MULTIPLY=64, DIVIDE=65, MODULO=66, LT=67, LTE=68, GT=69, GTE=70, EQ=71, 
		NEQ=72, AND=73, OR=74, NOT=75, ASSIGN=76, PLUS_PLUS=77, MINUS_MINUS=78, 
		NUMBER=79, STRING_LITERAL=80, BOOLEAN_LITERAL=81, NULL=82, IDENTIFIER=83, 
		WS=84, COMMENT_END=85, COMMENT_TEXT=86;
	public static final int
		RULE_template = 0, RULE_templateElement = 1, RULE_echoStatement = 2, RULE_rawEchoStatement = 3, 
		RULE_controlStatement = 4, RULE_ifStatement = 5, RULE_elseIfStatement = 6, 
		RULE_elseStatement = 7, RULE_forStatement = 8, RULE_forInit = 9, RULE_forUpdate = 10, 
		RULE_foreachStatement = 11, RULE_whileStatement = 12, RULE_breakStatement = 13, 
		RULE_continueStatement = 14, RULE_includeStatement = 15, RULE_expression = 16, 
		RULE_primary = 17, RULE_functionCall = 18, RULE_argumentList = 19;
	private static String[] makeRuleNames() {
		return new String[] {
			"template", "templateElement", "echoStatement", "rawEchoStatement", "controlStatement", 
			"ifStatement", "elseIfStatement", "elseStatement", "forStatement", "forInit", 
			"forUpdate", "foreachStatement", "whileStatement", "breakStatement", 
			"continueStatement", "includeStatement", "expression", "primary", "functionCall", 
			"argumentList"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{{'", "'{{{'", "'{%'", "'{#'", null, "'}}'", "'}}}'", null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, "'%}'", "'if'", "'elseif'", "'else'", "'endif'", "'for'", 
			"'endfor'", "'foreach'", "'endforeach'", "'while'", "'endwhile'", "'break'", 
			"'continue'", "'include'", "'block'", "'endblock'", "'in'", null, null, 
			null, null, null, null, "';'", null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "'='", "'++'", 
			"'--'", null, null, null, null, null, null, "'#}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ECHO_START", "RAW_ECHO_START", "STMT_START", "COMMENT_START", 
			"TEXT", "ECHO_END", "RAW_ECHO_END", "EXPR_LPAREN", "EXPR_RPAREN", "EXPR_LBRACKET", 
			"EXPR_RBRACKET", "EXPR_DOT", "EXPR_COMMA", "EXPR_QUESTION", "EXPR_COLON", 
			"EXPR_PLUS", "EXPR_MINUS", "EXPR_MULTIPLY", "EXPR_DIVIDE", "EXPR_MODULO", 
			"EXPR_LT", "EXPR_LTE", "EXPR_GT", "EXPR_GTE", "EXPR_EQ", "EXPR_NEQ", 
			"EXPR_AND", "EXPR_OR", "EXPR_NOT", "EXPR_NUMBER", "EXPR_STRING_LITERAL", 
			"EXPR_BOOLEAN_LITERAL", "EXPR_NULL", "EXPR_IDENTIFIER", "EXPR_WS", "STMT_END", 
			"IF", "ELSEIF", "ELSE", "ENDIF", "FOR", "ENDFOR", "FOREACH", "ENDFOREACH", 
			"WHILE", "ENDWHILE", "BREAK", "CONTINUE", "INCLUDE", "BLOCK", "ENDBLOCK", 
			"IN", "LPAREN", "RPAREN", "LBRACKET", "RBRACKET", "DOT", "COMMA", "SEMICOLON", 
			"QUESTION", "COLON", "PLUS", "MINUS", "MULTIPLY", "DIVIDE", "MODULO", 
			"LT", "LTE", "GT", "GTE", "EQ", "NEQ", "AND", "OR", "NOT", "ASSIGN", 
			"PLUS_PLUS", "MINUS_MINUS", "NUMBER", "STRING_LITERAL", "BOOLEAN_LITERAL", 
			"NULL", "IDENTIFIER", "WS", "COMMENT_END", "COMMENT_TEXT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "JhpTemplateParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JhpTemplateParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(JhpTemplateParser.EOF, 0); }
		public List<TemplateElementContext> templateElement() {
			return getRuleContexts(TemplateElementContext.class);
		}
		public TemplateElementContext templateElement(int i) {
			return getRuleContext(TemplateElementContext.class,i);
		}
		public TemplateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_template; }
	}

	public final TemplateContext template() throws RecognitionException {
		TemplateContext _localctx = new TemplateContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_template);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 46L) != 0)) {
				{
				{
				setState(40);
				templateElement();
				}
				}
				setState(45);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(46);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateElementContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(JhpTemplateParser.TEXT, 0); }
		public EchoStatementContext echoStatement() {
			return getRuleContext(EchoStatementContext.class,0);
		}
		public RawEchoStatementContext rawEchoStatement() {
			return getRuleContext(RawEchoStatementContext.class,0);
		}
		public ControlStatementContext controlStatement() {
			return getRuleContext(ControlStatementContext.class,0);
		}
		public TemplateElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateElement; }
	}

	public final TemplateElementContext templateElement() throws RecognitionException {
		TemplateElementContext _localctx = new TemplateElementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_templateElement);
		try {
			setState(52);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TEXT:
				enterOuterAlt(_localctx, 1);
				{
				setState(48);
				match(TEXT);
				}
				break;
			case ECHO_START:
				enterOuterAlt(_localctx, 2);
				{
				setState(49);
				echoStatement();
				}
				break;
			case RAW_ECHO_START:
				enterOuterAlt(_localctx, 3);
				{
				setState(50);
				rawEchoStatement();
				}
				break;
			case STMT_START:
				enterOuterAlt(_localctx, 4);
				{
				setState(51);
				controlStatement();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EchoStatementContext extends ParserRuleContext {
		public TerminalNode ECHO_START() { return getToken(JhpTemplateParser.ECHO_START, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode ECHO_END() { return getToken(JhpTemplateParser.ECHO_END, 0); }
		public EchoStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_echoStatement; }
	}

	public final EchoStatementContext echoStatement() throws RecognitionException {
		EchoStatementContext _localctx = new EchoStatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_echoStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			match(ECHO_START);
			setState(55);
			expression(0);
			setState(56);
			match(ECHO_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RawEchoStatementContext extends ParserRuleContext {
		public TerminalNode RAW_ECHO_START() { return getToken(JhpTemplateParser.RAW_ECHO_START, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RAW_ECHO_END() { return getToken(JhpTemplateParser.RAW_ECHO_END, 0); }
		public RawEchoStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rawEchoStatement; }
	}

	public final RawEchoStatementContext rawEchoStatement() throws RecognitionException {
		RawEchoStatementContext _localctx = new RawEchoStatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_rawEchoStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			match(RAW_ECHO_START);
			setState(59);
			expression(0);
			setState(60);
			match(RAW_ECHO_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ControlStatementContext extends ParserRuleContext {
		public IfStatementContext ifStatement() {
			return getRuleContext(IfStatementContext.class,0);
		}
		public ForStatementContext forStatement() {
			return getRuleContext(ForStatementContext.class,0);
		}
		public ForeachStatementContext foreachStatement() {
			return getRuleContext(ForeachStatementContext.class,0);
		}
		public WhileStatementContext whileStatement() {
			return getRuleContext(WhileStatementContext.class,0);
		}
		public BreakStatementContext breakStatement() {
			return getRuleContext(BreakStatementContext.class,0);
		}
		public ContinueStatementContext continueStatement() {
			return getRuleContext(ContinueStatementContext.class,0);
		}
		public IncludeStatementContext includeStatement() {
			return getRuleContext(IncludeStatementContext.class,0);
		}
		public ControlStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_controlStatement; }
	}

	public final ControlStatementContext controlStatement() throws RecognitionException {
		ControlStatementContext _localctx = new ControlStatementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_controlStatement);
		try {
			setState(69);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(62);
				ifStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(63);
				forStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(64);
				foreachStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(65);
				whileStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(66);
				breakStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(67);
				continueStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(68);
				includeStatement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfStatementContext extends ParserRuleContext {
		public List<TerminalNode> STMT_START() { return getTokens(JhpTemplateParser.STMT_START); }
		public TerminalNode STMT_START(int i) {
			return getToken(JhpTemplateParser.STMT_START, i);
		}
		public TerminalNode IF() { return getToken(JhpTemplateParser.IF, 0); }
		public TerminalNode LPAREN() { return getToken(JhpTemplateParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(JhpTemplateParser.RPAREN, 0); }
		public List<TerminalNode> STMT_END() { return getTokens(JhpTemplateParser.STMT_END); }
		public TerminalNode STMT_END(int i) {
			return getToken(JhpTemplateParser.STMT_END, i);
		}
		public TerminalNode ENDIF() { return getToken(JhpTemplateParser.ENDIF, 0); }
		public List<TemplateElementContext> templateElement() {
			return getRuleContexts(TemplateElementContext.class);
		}
		public TemplateElementContext templateElement(int i) {
			return getRuleContext(TemplateElementContext.class,i);
		}
		public List<ElseIfStatementContext> elseIfStatement() {
			return getRuleContexts(ElseIfStatementContext.class);
		}
		public ElseIfStatementContext elseIfStatement(int i) {
			return getRuleContext(ElseIfStatementContext.class,i);
		}
		public ElseStatementContext elseStatement() {
			return getRuleContext(ElseStatementContext.class,0);
		}
		public IfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStatement; }
	}

	public final IfStatementContext ifStatement() throws RecognitionException {
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_ifStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			match(STMT_START);
			setState(72);
			match(IF);
			setState(73);
			match(LPAREN);
			setState(74);
			expression(0);
			setState(75);
			match(RPAREN);
			setState(76);
			match(STMT_END);
			setState(80);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(77);
					templateElement();
					}
					} 
				}
				setState(82);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(86);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(83);
					elseIfStatement();
					}
					} 
				}
				setState(88);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(90);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(89);
				elseStatement();
				}
				break;
			}
			setState(92);
			match(STMT_START);
			setState(93);
			match(ENDIF);
			setState(94);
			match(STMT_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseIfStatementContext extends ParserRuleContext {
		public TerminalNode STMT_START() { return getToken(JhpTemplateParser.STMT_START, 0); }
		public TerminalNode ELSEIF() { return getToken(JhpTemplateParser.ELSEIF, 0); }
		public TerminalNode LPAREN() { return getToken(JhpTemplateParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(JhpTemplateParser.RPAREN, 0); }
		public TerminalNode STMT_END() { return getToken(JhpTemplateParser.STMT_END, 0); }
		public List<TemplateElementContext> templateElement() {
			return getRuleContexts(TemplateElementContext.class);
		}
		public TemplateElementContext templateElement(int i) {
			return getRuleContext(TemplateElementContext.class,i);
		}
		public ElseIfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseIfStatement; }
	}

	public final ElseIfStatementContext elseIfStatement() throws RecognitionException {
		ElseIfStatementContext _localctx = new ElseIfStatementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_elseIfStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			match(STMT_START);
			setState(97);
			match(ELSEIF);
			setState(98);
			match(LPAREN);
			setState(99);
			expression(0);
			setState(100);
			match(RPAREN);
			setState(101);
			match(STMT_END);
			setState(105);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(102);
					templateElement();
					}
					} 
				}
				setState(107);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseStatementContext extends ParserRuleContext {
		public TerminalNode STMT_START() { return getToken(JhpTemplateParser.STMT_START, 0); }
		public TerminalNode ELSE() { return getToken(JhpTemplateParser.ELSE, 0); }
		public TerminalNode STMT_END() { return getToken(JhpTemplateParser.STMT_END, 0); }
		public List<TemplateElementContext> templateElement() {
			return getRuleContexts(TemplateElementContext.class);
		}
		public TemplateElementContext templateElement(int i) {
			return getRuleContext(TemplateElementContext.class,i);
		}
		public ElseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseStatement; }
	}

	public final ElseStatementContext elseStatement() throws RecognitionException {
		ElseStatementContext _localctx = new ElseStatementContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_elseStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			match(STMT_START);
			setState(109);
			match(ELSE);
			setState(110);
			match(STMT_END);
			setState(114);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(111);
					templateElement();
					}
					} 
				}
				setState(116);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForStatementContext extends ParserRuleContext {
		public List<TerminalNode> STMT_START() { return getTokens(JhpTemplateParser.STMT_START); }
		public TerminalNode STMT_START(int i) {
			return getToken(JhpTemplateParser.STMT_START, i);
		}
		public TerminalNode FOR() { return getToken(JhpTemplateParser.FOR, 0); }
		public TerminalNode LPAREN() { return getToken(JhpTemplateParser.LPAREN, 0); }
		public ForInitContext forInit() {
			return getRuleContext(ForInitContext.class,0);
		}
		public List<TerminalNode> SEMICOLON() { return getTokens(JhpTemplateParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(JhpTemplateParser.SEMICOLON, i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForUpdateContext forUpdate() {
			return getRuleContext(ForUpdateContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(JhpTemplateParser.RPAREN, 0); }
		public List<TerminalNode> STMT_END() { return getTokens(JhpTemplateParser.STMT_END); }
		public TerminalNode STMT_END(int i) {
			return getToken(JhpTemplateParser.STMT_END, i);
		}
		public TerminalNode ENDFOR() { return getToken(JhpTemplateParser.ENDFOR, 0); }
		public List<TemplateElementContext> templateElement() {
			return getRuleContexts(TemplateElementContext.class);
		}
		public TemplateElementContext templateElement(int i) {
			return getRuleContext(TemplateElementContext.class,i);
		}
		public ForStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forStatement; }
	}

	public final ForStatementContext forStatement() throws RecognitionException {
		ForStatementContext _localctx = new ForStatementContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_forStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(STMT_START);
			setState(118);
			match(FOR);
			setState(119);
			match(LPAREN);
			setState(120);
			forInit();
			setState(121);
			match(SEMICOLON);
			setState(122);
			expression(0);
			setState(123);
			match(SEMICOLON);
			setState(124);
			forUpdate();
			setState(125);
			match(RPAREN);
			setState(126);
			match(STMT_END);
			setState(130);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(127);
					templateElement();
					}
					} 
				}
				setState(132);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			setState(133);
			match(STMT_START);
			setState(134);
			match(ENDFOR);
			setState(135);
			match(STMT_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForInitContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(JhpTemplateParser.IDENTIFIER, 0); }
		public TerminalNode ASSIGN() { return getToken(JhpTemplateParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forInit; }
	}

	public final ForInitContext forInit() throws RecognitionException {
		ForInitContext _localctx = new ForInitContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_forInit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
			match(IDENTIFIER);
			setState(138);
			match(ASSIGN);
			setState(139);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForUpdateContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(JhpTemplateParser.IDENTIFIER, 0); }
		public TerminalNode PLUS_PLUS() { return getToken(JhpTemplateParser.PLUS_PLUS, 0); }
		public TerminalNode MINUS_MINUS() { return getToken(JhpTemplateParser.MINUS_MINUS, 0); }
		public TerminalNode ASSIGN() { return getToken(JhpTemplateParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForUpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forUpdate; }
	}

	public final ForUpdateContext forUpdate() throws RecognitionException {
		ForUpdateContext _localctx = new ForUpdateContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_forUpdate);
		int _la;
		try {
			setState(146);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(141);
				match(IDENTIFIER);
				setState(142);
				_la = _input.LA(1);
				if ( !(_la==PLUS_PLUS || _la==MINUS_MINUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(143);
				match(IDENTIFIER);
				setState(144);
				match(ASSIGN);
				setState(145);
				expression(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForeachStatementContext extends ParserRuleContext {
		public List<TerminalNode> STMT_START() { return getTokens(JhpTemplateParser.STMT_START); }
		public TerminalNode STMT_START(int i) {
			return getToken(JhpTemplateParser.STMT_START, i);
		}
		public TerminalNode FOREACH() { return getToken(JhpTemplateParser.FOREACH, 0); }
		public TerminalNode LPAREN() { return getToken(JhpTemplateParser.LPAREN, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(JhpTemplateParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(JhpTemplateParser.IDENTIFIER, i);
		}
		public TerminalNode IN() { return getToken(JhpTemplateParser.IN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(JhpTemplateParser.RPAREN, 0); }
		public List<TerminalNode> STMT_END() { return getTokens(JhpTemplateParser.STMT_END); }
		public TerminalNode STMT_END(int i) {
			return getToken(JhpTemplateParser.STMT_END, i);
		}
		public TerminalNode ENDFOREACH() { return getToken(JhpTemplateParser.ENDFOREACH, 0); }
		public TerminalNode COMMA() { return getToken(JhpTemplateParser.COMMA, 0); }
		public List<TemplateElementContext> templateElement() {
			return getRuleContexts(TemplateElementContext.class);
		}
		public TemplateElementContext templateElement(int i) {
			return getRuleContext(TemplateElementContext.class,i);
		}
		public ForeachStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_foreachStatement; }
	}

	public final ForeachStatementContext foreachStatement() throws RecognitionException {
		ForeachStatementContext _localctx = new ForeachStatementContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_foreachStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(STMT_START);
			setState(149);
			match(FOREACH);
			setState(150);
			match(LPAREN);
			setState(153);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(151);
				match(IDENTIFIER);
				setState(152);
				match(COMMA);
				}
				break;
			}
			setState(155);
			match(IDENTIFIER);
			setState(156);
			match(IN);
			setState(157);
			expression(0);
			setState(158);
			match(RPAREN);
			setState(159);
			match(STMT_END);
			setState(163);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(160);
					templateElement();
					}
					} 
				}
				setState(165);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			setState(166);
			match(STMT_START);
			setState(167);
			match(ENDFOREACH);
			setState(168);
			match(STMT_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatementContext extends ParserRuleContext {
		public List<TerminalNode> STMT_START() { return getTokens(JhpTemplateParser.STMT_START); }
		public TerminalNode STMT_START(int i) {
			return getToken(JhpTemplateParser.STMT_START, i);
		}
		public TerminalNode WHILE() { return getToken(JhpTemplateParser.WHILE, 0); }
		public TerminalNode LPAREN() { return getToken(JhpTemplateParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(JhpTemplateParser.RPAREN, 0); }
		public List<TerminalNode> STMT_END() { return getTokens(JhpTemplateParser.STMT_END); }
		public TerminalNode STMT_END(int i) {
			return getToken(JhpTemplateParser.STMT_END, i);
		}
		public TerminalNode ENDWHILE() { return getToken(JhpTemplateParser.ENDWHILE, 0); }
		public List<TemplateElementContext> templateElement() {
			return getRuleContexts(TemplateElementContext.class);
		}
		public TemplateElementContext templateElement(int i) {
			return getRuleContext(TemplateElementContext.class,i);
		}
		public WhileStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStatement; }
	}

	public final WhileStatementContext whileStatement() throws RecognitionException {
		WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_whileStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			match(STMT_START);
			setState(171);
			match(WHILE);
			setState(172);
			match(LPAREN);
			setState(173);
			expression(0);
			setState(174);
			match(RPAREN);
			setState(175);
			match(STMT_END);
			setState(179);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(176);
					templateElement();
					}
					} 
				}
				setState(181);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			setState(182);
			match(STMT_START);
			setState(183);
			match(ENDWHILE);
			setState(184);
			match(STMT_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BreakStatementContext extends ParserRuleContext {
		public TerminalNode STMT_START() { return getToken(JhpTemplateParser.STMT_START, 0); }
		public TerminalNode BREAK() { return getToken(JhpTemplateParser.BREAK, 0); }
		public TerminalNode STMT_END() { return getToken(JhpTemplateParser.STMT_END, 0); }
		public BreakStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStatement; }
	}

	public final BreakStatementContext breakStatement() throws RecognitionException {
		BreakStatementContext _localctx = new BreakStatementContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_breakStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			match(STMT_START);
			setState(187);
			match(BREAK);
			setState(188);
			match(STMT_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContinueStatementContext extends ParserRuleContext {
		public TerminalNode STMT_START() { return getToken(JhpTemplateParser.STMT_START, 0); }
		public TerminalNode CONTINUE() { return getToken(JhpTemplateParser.CONTINUE, 0); }
		public TerminalNode STMT_END() { return getToken(JhpTemplateParser.STMT_END, 0); }
		public ContinueStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continueStatement; }
	}

	public final ContinueStatementContext continueStatement() throws RecognitionException {
		ContinueStatementContext _localctx = new ContinueStatementContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_continueStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			match(STMT_START);
			setState(191);
			match(CONTINUE);
			setState(192);
			match(STMT_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IncludeStatementContext extends ParserRuleContext {
		public TerminalNode STMT_START() { return getToken(JhpTemplateParser.STMT_START, 0); }
		public TerminalNode INCLUDE() { return getToken(JhpTemplateParser.INCLUDE, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(JhpTemplateParser.STRING_LITERAL, 0); }
		public TerminalNode STMT_END() { return getToken(JhpTemplateParser.STMT_END, 0); }
		public IncludeStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_includeStatement; }
	}

	public final IncludeStatementContext includeStatement() throws RecognitionException {
		IncludeStatementContext _localctx = new IncludeStatementContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_includeStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			match(STMT_START);
			setState(195);
			match(INCLUDE);
			setState(196);
			match(STRING_LITERAL);
			setState(197);
			match(STMT_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenthesizedExpressionContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(JhpTemplateParser.LPAREN, 0); }
		public TerminalNode EXPR_LPAREN() { return getToken(JhpTemplateParser.EXPR_LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(JhpTemplateParser.RPAREN, 0); }
		public TerminalNode EXPR_RPAREN() { return getToken(JhpTemplateParser.EXPR_RPAREN, 0); }
		public ParenthesizedExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AdditiveExpressionContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(JhpTemplateParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(JhpTemplateParser.MINUS, 0); }
		public TerminalNode EXPR_PLUS() { return getToken(JhpTemplateParser.EXPR_PLUS, 0); }
		public TerminalNode EXPR_MINUS() { return getToken(JhpTemplateParser.EXPR_MINUS, 0); }
		public AdditiveExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RelationalExpressionContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LT() { return getToken(JhpTemplateParser.LT, 0); }
		public TerminalNode LTE() { return getToken(JhpTemplateParser.LTE, 0); }
		public TerminalNode GT() { return getToken(JhpTemplateParser.GT, 0); }
		public TerminalNode GTE() { return getToken(JhpTemplateParser.GTE, 0); }
		public TerminalNode EXPR_LT() { return getToken(JhpTemplateParser.EXPR_LT, 0); }
		public TerminalNode EXPR_LTE() { return getToken(JhpTemplateParser.EXPR_LTE, 0); }
		public TerminalNode EXPR_GT() { return getToken(JhpTemplateParser.EXPR_GT, 0); }
		public TerminalNode EXPR_GTE() { return getToken(JhpTemplateParser.EXPR_GTE, 0); }
		public RelationalExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TernaryExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode QUESTION() { return getToken(JhpTemplateParser.QUESTION, 0); }
		public TerminalNode EXPR_QUESTION() { return getToken(JhpTemplateParser.EXPR_QUESTION, 0); }
		public TerminalNode COLON() { return getToken(JhpTemplateParser.COLON, 0); }
		public TerminalNode EXPR_COLON() { return getToken(JhpTemplateParser.EXPR_COLON, 0); }
		public TernaryExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MemberAccessExpressionContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode DOT() { return getToken(JhpTemplateParser.DOT, 0); }
		public TerminalNode EXPR_DOT() { return getToken(JhpTemplateParser.EXPR_DOT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(JhpTemplateParser.IDENTIFIER, 0); }
		public TerminalNode EXPR_IDENTIFIER() { return getToken(JhpTemplateParser.EXPR_IDENTIFIER, 0); }
		public MemberAccessExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryExpressionContext extends ExpressionContext {
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public PrimaryExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NotExpressionContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode NOT() { return getToken(JhpTemplateParser.NOT, 0); }
		public TerminalNode EXPR_NOT() { return getToken(JhpTemplateParser.EXPR_NOT, 0); }
		public NotExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OrExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode OR() { return getToken(JhpTemplateParser.OR, 0); }
		public TerminalNode EXPR_OR() { return getToken(JhpTemplateParser.EXPR_OR, 0); }
		public OrExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallExpressionContext extends ExpressionContext {
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public FunctionCallExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AndExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode AND() { return getToken(JhpTemplateParser.AND, 0); }
		public TerminalNode EXPR_AND() { return getToken(JhpTemplateParser.EXPR_AND, 0); }
		public AndExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryMinusExpressionContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode MINUS() { return getToken(JhpTemplateParser.MINUS, 0); }
		public TerminalNode EXPR_MINUS() { return getToken(JhpTemplateParser.EXPR_MINUS, 0); }
		public UnaryMinusExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EqualityExpressionContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode EQ() { return getToken(JhpTemplateParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(JhpTemplateParser.NEQ, 0); }
		public TerminalNode EXPR_EQ() { return getToken(JhpTemplateParser.EXPR_EQ, 0); }
		public TerminalNode EXPR_NEQ() { return getToken(JhpTemplateParser.EXPR_NEQ, 0); }
		public EqualityExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicativeExpressionContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode MULTIPLY() { return getToken(JhpTemplateParser.MULTIPLY, 0); }
		public TerminalNode DIVIDE() { return getToken(JhpTemplateParser.DIVIDE, 0); }
		public TerminalNode MODULO() { return getToken(JhpTemplateParser.MODULO, 0); }
		public TerminalNode EXPR_MULTIPLY() { return getToken(JhpTemplateParser.EXPR_MULTIPLY, 0); }
		public TerminalNode EXPR_DIVIDE() { return getToken(JhpTemplateParser.EXPR_DIVIDE, 0); }
		public TerminalNode EXPR_MODULO() { return getToken(JhpTemplateParser.EXPR_MODULO, 0); }
		public MultiplicativeExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayAccessExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LBRACKET() { return getToken(JhpTemplateParser.LBRACKET, 0); }
		public TerminalNode EXPR_LBRACKET() { return getToken(JhpTemplateParser.EXPR_LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(JhpTemplateParser.RBRACKET, 0); }
		public TerminalNode EXPR_RBRACKET() { return getToken(JhpTemplateParser.EXPR_RBRACKET, 0); }
		public ArrayAccessExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 32;
		enterRecursionRule(_localctx, 32, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				_localctx = new PrimaryExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(200);
				primary();
				}
				break;
			case 2:
				{
				_localctx = new FunctionCallExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(201);
				functionCall();
				}
				break;
			case 3:
				{
				_localctx = new ParenthesizedExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(202);
				_la = _input.LA(1);
				if ( !(_la==EXPR_LPAREN || _la==LPAREN) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(203);
				expression(0);
				setState(204);
				_la = _input.LA(1);
				if ( !(_la==EXPR_RPAREN || _la==RPAREN) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 4:
				{
				_localctx = new NotExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(206);
				_la = _input.LA(1);
				if ( !(_la==EXPR_NOT || _la==NOT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(207);
				expression(9);
				}
				break;
			case 5:
				{
				_localctx = new UnaryMinusExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(208);
				_la = _input.LA(1);
				if ( !(_la==EXPR_MINUS || _la==MINUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(209);
				expression(8);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(246);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(244);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
					case 1:
						{
						_localctx = new MultiplicativeExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(212);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(213);
						((MultiplicativeExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 18)) & ~0x3f) == 0 && ((1L << (_la - 18)) & 492581209243655L) != 0)) ) {
							((MultiplicativeExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(214);
						expression(8);
						}
						break;
					case 2:
						{
						_localctx = new AdditiveExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(215);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(216);
						((AdditiveExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & -4611686018427191296L) != 0)) ) {
							((AdditiveExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(217);
						expression(7);
						}
						break;
					case 3:
						{
						_localctx = new RelationalExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(218);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(219);
						((RelationalExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 21)) & ~0x3f) == 0 && ((1L << (_la - 21)) & 1055531162664975L) != 0)) ) {
							((RelationalExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(220);
						expression(6);
						}
						break;
					case 4:
						{
						_localctx = new EqualityExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(221);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(222);
						((EqualityExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 25)) & ~0x3f) == 0 && ((1L << (_la - 25)) & 211106232532995L) != 0)) ) {
							((EqualityExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(223);
						expression(5);
						}
						break;
					case 5:
						{
						_localctx = new AndExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(224);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(225);
						_la = _input.LA(1);
						if ( !(_la==EXPR_AND || _la==AND) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(226);
						expression(4);
						}
						break;
					case 6:
						{
						_localctx = new OrExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(227);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(228);
						_la = _input.LA(1);
						if ( !(_la==EXPR_OR || _la==OR) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(229);
						expression(3);
						}
						break;
					case 7:
						{
						_localctx = new TernaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(230);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(231);
						_la = _input.LA(1);
						if ( !(_la==EXPR_QUESTION || _la==QUESTION) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(232);
						expression(0);
						setState(233);
						_la = _input.LA(1);
						if ( !(_la==EXPR_COLON || _la==COLON) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(234);
						expression(1);
						}
						break;
					case 8:
						{
						_localctx = new ArrayAccessExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(236);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(237);
						_la = _input.LA(1);
						if ( !(_la==EXPR_LBRACKET || _la==LBRACKET) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(238);
						expression(0);
						setState(239);
						_la = _input.LA(1);
						if ( !(_la==EXPR_RBRACKET || _la==RBRACKET) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 9:
						{
						_localctx = new MemberAccessExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(241);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(242);
						_la = _input.LA(1);
						if ( !(_la==EXPR_DOT || _la==DOT) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(243);
						_la = _input.LA(1);
						if ( !(_la==EXPR_IDENTIFIER || _la==IDENTIFIER) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					}
					} 
				}
				setState(248);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(JhpTemplateParser.IDENTIFIER, 0); }
		public TerminalNode NUMBER() { return getToken(JhpTemplateParser.NUMBER, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(JhpTemplateParser.STRING_LITERAL, 0); }
		public TerminalNode BOOLEAN_LITERAL() { return getToken(JhpTemplateParser.BOOLEAN_LITERAL, 0); }
		public TerminalNode NULL() { return getToken(JhpTemplateParser.NULL, 0); }
		public TerminalNode EXPR_IDENTIFIER() { return getToken(JhpTemplateParser.EXPR_IDENTIFIER, 0); }
		public TerminalNode EXPR_NUMBER() { return getToken(JhpTemplateParser.EXPR_NUMBER, 0); }
		public TerminalNode EXPR_STRING_LITERAL() { return getToken(JhpTemplateParser.EXPR_STRING_LITERAL, 0); }
		public TerminalNode EXPR_BOOLEAN_LITERAL() { return getToken(JhpTemplateParser.EXPR_BOOLEAN_LITERAL, 0); }
		public TerminalNode EXPR_NULL() { return getToken(JhpTemplateParser.EXPR_NULL, 0); }
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary; }
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_primary);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			_la = _input.LA(1);
			if ( !(((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & 17451448556060703L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(JhpTemplateParser.IDENTIFIER, 0); }
		public TerminalNode EXPR_IDENTIFIER() { return getToken(JhpTemplateParser.EXPR_IDENTIFIER, 0); }
		public TerminalNode LPAREN() { return getToken(JhpTemplateParser.LPAREN, 0); }
		public TerminalNode EXPR_LPAREN() { return getToken(JhpTemplateParser.EXPR_LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(JhpTemplateParser.RPAREN, 0); }
		public TerminalNode EXPR_RPAREN() { return getToken(JhpTemplateParser.EXPR_RPAREN, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_functionCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(251);
			_la = _input.LA(1);
			if ( !(_la==EXPR_IDENTIFIER || _la==IDENTIFIER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(252);
			_la = _input.LA(1);
			if ( !(_la==EXPR_LPAREN || _la==LPAREN) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(254);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -9214364803777036032L) != 0) || ((((_la - 75)) & ~0x3f) == 0 && ((1L << (_la - 75)) & 497L) != 0)) {
				{
				setState(253);
				argumentList();
				}
			}

			setState(256);
			_la = _input.LA(1);
			if ( !(_la==EXPR_RPAREN || _la==RPAREN) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JhpTemplateParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JhpTemplateParser.COMMA, i);
		}
		public List<TerminalNode> EXPR_COMMA() { return getTokens(JhpTemplateParser.EXPR_COMMA); }
		public TerminalNode EXPR_COMMA(int i) {
			return getToken(JhpTemplateParser.EXPR_COMMA, i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(258);
			expression(0);
			setState(263);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==EXPR_COMMA || _la==COMMA) {
				{
				{
				setState(259);
				_la = _input.LA(1);
				if ( !(_la==EXPR_COMMA || _la==COMMA) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(260);
				expression(0);
				}
				}
				setState(265);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 16:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 7);
		case 1:
			return precpred(_ctx, 6);
		case 2:
			return precpred(_ctx, 5);
		case 3:
			return precpred(_ctx, 4);
		case 4:
			return precpred(_ctx, 3);
		case 5:
			return precpred(_ctx, 2);
		case 6:
			return precpred(_ctx, 1);
		case 7:
			return precpred(_ctx, 13);
		case 8:
			return precpred(_ctx, 12);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001V\u010b\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0001\u0000\u0005\u0000*\b\u0000\n\u0000\f\u0000"+
		"-\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0003\u00015\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0003\u0004F\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005O\b\u0005\n\u0005\f\u0005"+
		"R\t\u0005\u0001\u0005\u0005\u0005U\b\u0005\n\u0005\f\u0005X\t\u0005\u0001"+
		"\u0005\u0003\u0005[\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0005\u0006h\b\u0006\n\u0006\f\u0006k\t\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007q\b\u0007\n\u0007"+
		"\f\u0007t\t\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0005\b\u0081\b\b\n\b\f\b\u0084\t\b"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u0093\b\n\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u009a\b\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0005"+
		"\u000b\u00a2\b\u000b\n\u000b\f\u000b\u00a5\t\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0005\f\u00b2\b\f\n\f\f\f\u00b5\t\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0003"+
		"\u0010\u00d3\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0005\u0010\u00f5\b\u0010\n"+
		"\u0010\f\u0010\u00f8\t\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0003\u0012\u00ff\b\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0005\u0013\u0106\b\u0013\n\u0013\f\u0013"+
		"\u0109\t\u0013\u0001\u0013\u0000\u0001 \u0014\u0000\u0002\u0004\u0006"+
		"\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&\u0000"+
		"\u0013\u0001\u0000MN\u0002\u0000\b\b55\u0002\u0000\t\t66\u0002\u0000\u001d"+
		"\u001dKK\u0002\u0000\u0011\u0011??\u0002\u0000\u0012\u0014@B\u0002\u0000"+
		"\u0010\u0011>?\u0002\u0000\u0015\u0018CF\u0002\u0000\u0019\u001aGH\u0002"+
		"\u0000\u001b\u001bII\u0002\u0000\u001c\u001cJJ\u0002\u0000\u000e\u000e"+
		"<<\u0002\u0000\u000f\u000f==\u0002\u0000\n\n77\u0002\u0000\u000b\u000b"+
		"88\u0002\u0000\f\f99\u0002\u0000\"\"SS\u0002\u0000\u001e\"OS\u0002\u0000"+
		"\r\r::\u0119\u0000+\u0001\u0000\u0000\u0000\u00024\u0001\u0000\u0000\u0000"+
		"\u00046\u0001\u0000\u0000\u0000\u0006:\u0001\u0000\u0000\u0000\bE\u0001"+
		"\u0000\u0000\u0000\nG\u0001\u0000\u0000\u0000\f`\u0001\u0000\u0000\u0000"+
		"\u000el\u0001\u0000\u0000\u0000\u0010u\u0001\u0000\u0000\u0000\u0012\u0089"+
		"\u0001\u0000\u0000\u0000\u0014\u0092\u0001\u0000\u0000\u0000\u0016\u0094"+
		"\u0001\u0000\u0000\u0000\u0018\u00aa\u0001\u0000\u0000\u0000\u001a\u00ba"+
		"\u0001\u0000\u0000\u0000\u001c\u00be\u0001\u0000\u0000\u0000\u001e\u00c2"+
		"\u0001\u0000\u0000\u0000 \u00d2\u0001\u0000\u0000\u0000\"\u00f9\u0001"+
		"\u0000\u0000\u0000$\u00fb\u0001\u0000\u0000\u0000&\u0102\u0001\u0000\u0000"+
		"\u0000(*\u0003\u0002\u0001\u0000)(\u0001\u0000\u0000\u0000*-\u0001\u0000"+
		"\u0000\u0000+)\u0001\u0000\u0000\u0000+,\u0001\u0000\u0000\u0000,.\u0001"+
		"\u0000\u0000\u0000-+\u0001\u0000\u0000\u0000./\u0005\u0000\u0000\u0001"+
		"/\u0001\u0001\u0000\u0000\u000005\u0005\u0005\u0000\u000015\u0003\u0004"+
		"\u0002\u000025\u0003\u0006\u0003\u000035\u0003\b\u0004\u000040\u0001\u0000"+
		"\u0000\u000041\u0001\u0000\u0000\u000042\u0001\u0000\u0000\u000043\u0001"+
		"\u0000\u0000\u00005\u0003\u0001\u0000\u0000\u000067\u0005\u0001\u0000"+
		"\u000078\u0003 \u0010\u000089\u0005\u0006\u0000\u00009\u0005\u0001\u0000"+
		"\u0000\u0000:;\u0005\u0002\u0000\u0000;<\u0003 \u0010\u0000<=\u0005\u0007"+
		"\u0000\u0000=\u0007\u0001\u0000\u0000\u0000>F\u0003\n\u0005\u0000?F\u0003"+
		"\u0010\b\u0000@F\u0003\u0016\u000b\u0000AF\u0003\u0018\f\u0000BF\u0003"+
		"\u001a\r\u0000CF\u0003\u001c\u000e\u0000DF\u0003\u001e\u000f\u0000E>\u0001"+
		"\u0000\u0000\u0000E?\u0001\u0000\u0000\u0000E@\u0001\u0000\u0000\u0000"+
		"EA\u0001\u0000\u0000\u0000EB\u0001\u0000\u0000\u0000EC\u0001\u0000\u0000"+
		"\u0000ED\u0001\u0000\u0000\u0000F\t\u0001\u0000\u0000\u0000GH\u0005\u0003"+
		"\u0000\u0000HI\u0005%\u0000\u0000IJ\u00055\u0000\u0000JK\u0003 \u0010"+
		"\u0000KL\u00056\u0000\u0000LP\u0005$\u0000\u0000MO\u0003\u0002\u0001\u0000"+
		"NM\u0001\u0000\u0000\u0000OR\u0001\u0000\u0000\u0000PN\u0001\u0000\u0000"+
		"\u0000PQ\u0001\u0000\u0000\u0000QV\u0001\u0000\u0000\u0000RP\u0001\u0000"+
		"\u0000\u0000SU\u0003\f\u0006\u0000TS\u0001\u0000\u0000\u0000UX\u0001\u0000"+
		"\u0000\u0000VT\u0001\u0000\u0000\u0000VW\u0001\u0000\u0000\u0000WZ\u0001"+
		"\u0000\u0000\u0000XV\u0001\u0000\u0000\u0000Y[\u0003\u000e\u0007\u0000"+
		"ZY\u0001\u0000\u0000\u0000Z[\u0001\u0000\u0000\u0000[\\\u0001\u0000\u0000"+
		"\u0000\\]\u0005\u0003\u0000\u0000]^\u0005(\u0000\u0000^_\u0005$\u0000"+
		"\u0000_\u000b\u0001\u0000\u0000\u0000`a\u0005\u0003\u0000\u0000ab\u0005"+
		"&\u0000\u0000bc\u00055\u0000\u0000cd\u0003 \u0010\u0000de\u00056\u0000"+
		"\u0000ei\u0005$\u0000\u0000fh\u0003\u0002\u0001\u0000gf\u0001\u0000\u0000"+
		"\u0000hk\u0001\u0000\u0000\u0000ig\u0001\u0000\u0000\u0000ij\u0001\u0000"+
		"\u0000\u0000j\r\u0001\u0000\u0000\u0000ki\u0001\u0000\u0000\u0000lm\u0005"+
		"\u0003\u0000\u0000mn\u0005\'\u0000\u0000nr\u0005$\u0000\u0000oq\u0003"+
		"\u0002\u0001\u0000po\u0001\u0000\u0000\u0000qt\u0001\u0000\u0000\u0000"+
		"rp\u0001\u0000\u0000\u0000rs\u0001\u0000\u0000\u0000s\u000f\u0001\u0000"+
		"\u0000\u0000tr\u0001\u0000\u0000\u0000uv\u0005\u0003\u0000\u0000vw\u0005"+
		")\u0000\u0000wx\u00055\u0000\u0000xy\u0003\u0012\t\u0000yz\u0005;\u0000"+
		"\u0000z{\u0003 \u0010\u0000{|\u0005;\u0000\u0000|}\u0003\u0014\n\u0000"+
		"}~\u00056\u0000\u0000~\u0082\u0005$\u0000\u0000\u007f\u0081\u0003\u0002"+
		"\u0001\u0000\u0080\u007f\u0001\u0000\u0000\u0000\u0081\u0084\u0001\u0000"+
		"\u0000\u0000\u0082\u0080\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000"+
		"\u0000\u0000\u0083\u0085\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000"+
		"\u0000\u0000\u0085\u0086\u0005\u0003\u0000\u0000\u0086\u0087\u0005*\u0000"+
		"\u0000\u0087\u0088\u0005$\u0000\u0000\u0088\u0011\u0001\u0000\u0000\u0000"+
		"\u0089\u008a\u0005S\u0000\u0000\u008a\u008b\u0005L\u0000\u0000\u008b\u008c"+
		"\u0003 \u0010\u0000\u008c\u0013\u0001\u0000\u0000\u0000\u008d\u008e\u0005"+
		"S\u0000\u0000\u008e\u0093\u0007\u0000\u0000\u0000\u008f\u0090\u0005S\u0000"+
		"\u0000\u0090\u0091\u0005L\u0000\u0000\u0091\u0093\u0003 \u0010\u0000\u0092"+
		"\u008d\u0001\u0000\u0000\u0000\u0092\u008f\u0001\u0000\u0000\u0000\u0093"+
		"\u0015\u0001\u0000\u0000\u0000\u0094\u0095\u0005\u0003\u0000\u0000\u0095"+
		"\u0096\u0005+\u0000\u0000\u0096\u0099\u00055\u0000\u0000\u0097\u0098\u0005"+
		"S\u0000\u0000\u0098\u009a\u0005:\u0000\u0000\u0099\u0097\u0001\u0000\u0000"+
		"\u0000\u0099\u009a\u0001\u0000\u0000\u0000\u009a\u009b\u0001\u0000\u0000"+
		"\u0000\u009b\u009c\u0005S\u0000\u0000\u009c\u009d\u00054\u0000\u0000\u009d"+
		"\u009e\u0003 \u0010\u0000\u009e\u009f\u00056\u0000\u0000\u009f\u00a3\u0005"+
		"$\u0000\u0000\u00a0\u00a2\u0003\u0002\u0001\u0000\u00a1\u00a0\u0001\u0000"+
		"\u0000\u0000\u00a2\u00a5\u0001\u0000\u0000\u0000\u00a3\u00a1\u0001\u0000"+
		"\u0000\u0000\u00a3\u00a4\u0001\u0000\u0000\u0000\u00a4\u00a6\u0001\u0000"+
		"\u0000\u0000\u00a5\u00a3\u0001\u0000\u0000\u0000\u00a6\u00a7\u0005\u0003"+
		"\u0000\u0000\u00a7\u00a8\u0005,\u0000\u0000\u00a8\u00a9\u0005$\u0000\u0000"+
		"\u00a9\u0017\u0001\u0000\u0000\u0000\u00aa\u00ab\u0005\u0003\u0000\u0000"+
		"\u00ab\u00ac\u0005-\u0000\u0000\u00ac\u00ad\u00055\u0000\u0000\u00ad\u00ae"+
		"\u0003 \u0010\u0000\u00ae\u00af\u00056\u0000\u0000\u00af\u00b3\u0005$"+
		"\u0000\u0000\u00b0\u00b2\u0003\u0002\u0001\u0000\u00b1\u00b0\u0001\u0000"+
		"\u0000\u0000\u00b2\u00b5\u0001\u0000\u0000\u0000\u00b3\u00b1\u0001\u0000"+
		"\u0000\u0000\u00b3\u00b4\u0001\u0000\u0000\u0000\u00b4\u00b6\u0001\u0000"+
		"\u0000\u0000\u00b5\u00b3\u0001\u0000\u0000\u0000\u00b6\u00b7\u0005\u0003"+
		"\u0000\u0000\u00b7\u00b8\u0005.\u0000\u0000\u00b8\u00b9\u0005$\u0000\u0000"+
		"\u00b9\u0019\u0001\u0000\u0000\u0000\u00ba\u00bb\u0005\u0003\u0000\u0000"+
		"\u00bb\u00bc\u0005/\u0000\u0000\u00bc\u00bd\u0005$\u0000\u0000\u00bd\u001b"+
		"\u0001\u0000\u0000\u0000\u00be\u00bf\u0005\u0003\u0000\u0000\u00bf\u00c0"+
		"\u00050\u0000\u0000\u00c0\u00c1\u0005$\u0000\u0000\u00c1\u001d\u0001\u0000"+
		"\u0000\u0000\u00c2\u00c3\u0005\u0003\u0000\u0000\u00c3\u00c4\u00051\u0000"+
		"\u0000\u00c4\u00c5\u0005P\u0000\u0000\u00c5\u00c6\u0005$\u0000\u0000\u00c6"+
		"\u001f\u0001\u0000\u0000\u0000\u00c7\u00c8\u0006\u0010\uffff\uffff\u0000"+
		"\u00c8\u00d3\u0003\"\u0011\u0000\u00c9\u00d3\u0003$\u0012\u0000\u00ca"+
		"\u00cb\u0007\u0001\u0000\u0000\u00cb\u00cc\u0003 \u0010\u0000\u00cc\u00cd"+
		"\u0007\u0002\u0000\u0000\u00cd\u00d3\u0001\u0000\u0000\u0000\u00ce\u00cf"+
		"\u0007\u0003\u0000\u0000\u00cf\u00d3\u0003 \u0010\t\u00d0\u00d1\u0007"+
		"\u0004\u0000\u0000\u00d1\u00d3\u0003 \u0010\b\u00d2\u00c7\u0001\u0000"+
		"\u0000\u0000\u00d2\u00c9\u0001\u0000\u0000\u0000\u00d2\u00ca\u0001\u0000"+
		"\u0000\u0000\u00d2\u00ce\u0001\u0000\u0000\u0000\u00d2\u00d0\u0001\u0000"+
		"\u0000\u0000\u00d3\u00f6\u0001\u0000\u0000\u0000\u00d4\u00d5\n\u0007\u0000"+
		"\u0000\u00d5\u00d6\u0007\u0005\u0000\u0000\u00d6\u00f5\u0003 \u0010\b"+
		"\u00d7\u00d8\n\u0006\u0000\u0000\u00d8\u00d9\u0007\u0006\u0000\u0000\u00d9"+
		"\u00f5\u0003 \u0010\u0007\u00da\u00db\n\u0005\u0000\u0000\u00db\u00dc"+
		"\u0007\u0007\u0000\u0000\u00dc\u00f5\u0003 \u0010\u0006\u00dd\u00de\n"+
		"\u0004\u0000\u0000\u00de\u00df\u0007\b\u0000\u0000\u00df\u00f5\u0003 "+
		"\u0010\u0005\u00e0\u00e1\n\u0003\u0000\u0000\u00e1\u00e2\u0007\t\u0000"+
		"\u0000\u00e2\u00f5\u0003 \u0010\u0004\u00e3\u00e4\n\u0002\u0000\u0000"+
		"\u00e4\u00e5\u0007\n\u0000\u0000\u00e5\u00f5\u0003 \u0010\u0003\u00e6"+
		"\u00e7\n\u0001\u0000\u0000\u00e7\u00e8\u0007\u000b\u0000\u0000\u00e8\u00e9"+
		"\u0003 \u0010\u0000\u00e9\u00ea\u0007\f\u0000\u0000\u00ea\u00eb\u0003"+
		" \u0010\u0001\u00eb\u00f5\u0001\u0000\u0000\u0000\u00ec\u00ed\n\r\u0000"+
		"\u0000\u00ed\u00ee\u0007\r\u0000\u0000\u00ee\u00ef\u0003 \u0010\u0000"+
		"\u00ef\u00f0\u0007\u000e\u0000\u0000\u00f0\u00f5\u0001\u0000\u0000\u0000"+
		"\u00f1\u00f2\n\f\u0000\u0000\u00f2\u00f3\u0007\u000f\u0000\u0000\u00f3"+
		"\u00f5\u0007\u0010\u0000\u0000\u00f4\u00d4\u0001\u0000\u0000\u0000\u00f4"+
		"\u00d7\u0001\u0000\u0000\u0000\u00f4\u00da\u0001\u0000\u0000\u0000\u00f4"+
		"\u00dd\u0001\u0000\u0000\u0000\u00f4\u00e0\u0001\u0000\u0000\u0000\u00f4"+
		"\u00e3\u0001\u0000\u0000\u0000\u00f4\u00e6\u0001\u0000\u0000\u0000\u00f4"+
		"\u00ec\u0001\u0000\u0000\u0000\u00f4\u00f1\u0001\u0000\u0000\u0000\u00f5"+
		"\u00f8\u0001\u0000\u0000\u0000\u00f6\u00f4\u0001\u0000\u0000\u0000\u00f6"+
		"\u00f7\u0001\u0000\u0000\u0000\u00f7!\u0001\u0000\u0000\u0000\u00f8\u00f6"+
		"\u0001\u0000\u0000\u0000\u00f9\u00fa\u0007\u0011\u0000\u0000\u00fa#\u0001"+
		"\u0000\u0000\u0000\u00fb\u00fc\u0007\u0010\u0000\u0000\u00fc\u00fe\u0007"+
		"\u0001\u0000\u0000\u00fd\u00ff\u0003&\u0013\u0000\u00fe\u00fd\u0001\u0000"+
		"\u0000\u0000\u00fe\u00ff\u0001\u0000\u0000\u0000\u00ff\u0100\u0001\u0000"+
		"\u0000\u0000\u0100\u0101\u0007\u0002\u0000\u0000\u0101%\u0001\u0000\u0000"+
		"\u0000\u0102\u0107\u0003 \u0010\u0000\u0103\u0104\u0007\u0012\u0000\u0000"+
		"\u0104\u0106\u0003 \u0010\u0000\u0105\u0103\u0001\u0000\u0000\u0000\u0106"+
		"\u0109\u0001\u0000\u0000\u0000\u0107\u0105\u0001\u0000\u0000\u0000\u0107"+
		"\u0108\u0001\u0000\u0000\u0000\u0108\'\u0001\u0000\u0000\u0000\u0109\u0107"+
		"\u0001\u0000\u0000\u0000\u0012+4EPVZir\u0082\u0092\u0099\u00a3\u00b3\u00d2"+
		"\u00f4\u00f6\u00fe\u0107";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}