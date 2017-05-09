package com.ngc.seaside.systemdescriptor.ide.contentassist.antlr.internal;

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.DFA;
import com.ngc.seaside.systemdescriptor.services.SystemDescriptorGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalSystemDescriptorParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_STRING", "RULE_ID", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'{'", "'}'", "','", "'['", "']'", "':'", "'metadata'", "'{}'", "'[]'"
    };
    public static final int RULE_STRING=4;
    public static final int RULE_SL_COMMENT=8;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__11=11;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int EOF=-1;
    public static final int RULE_ID=5;
    public static final int RULE_WS=9;
    public static final int RULE_ANY_OTHER=10;
    public static final int RULE_INT=6;
    public static final int RULE_ML_COMMENT=7;

    // delegates
    // delegators


        public InternalSystemDescriptorParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalSystemDescriptorParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalSystemDescriptorParser.tokenNames; }
    public String getGrammarFileName() { return "InternalSystemDescriptor.g"; }


    	private SystemDescriptorGrammarAccess grammarAccess;

    	public void setGrammarAccess(SystemDescriptorGrammarAccess grammarAccess) {
    		this.grammarAccess = grammarAccess;
    	}

    	@Override
    	protected Grammar getGrammar() {
    		return grammarAccess.getGrammar();
    	}

    	@Override
    	protected String getValueForTokenName(String tokenName) {
    		return tokenName;
    	}



    // $ANTLR start "entryRuleModel"
    // InternalSystemDescriptor.g:53:1: entryRuleModel : ruleModel EOF ;
    public final void entryRuleModel() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:54:1: ( ruleModel EOF )
            // InternalSystemDescriptor.g:55:1: ruleModel EOF
            {
             before(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_1);
            ruleModel();

            state._fsp--;

             after(grammarAccess.getModelRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // InternalSystemDescriptor.g:62:1: ruleModel : ( ( rule__Model__GreetingsAssignment )* ) ;
    public final void ruleModel() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:66:2: ( ( ( rule__Model__GreetingsAssignment )* ) )
            // InternalSystemDescriptor.g:67:2: ( ( rule__Model__GreetingsAssignment )* )
            {
            // InternalSystemDescriptor.g:67:2: ( ( rule__Model__GreetingsAssignment )* )
            // InternalSystemDescriptor.g:68:3: ( rule__Model__GreetingsAssignment )*
            {
             before(grammarAccess.getModelAccess().getGreetingsAssignment()); 
            // InternalSystemDescriptor.g:69:3: ( rule__Model__GreetingsAssignment )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==17) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalSystemDescriptor.g:69:4: rule__Model__GreetingsAssignment
            	    {
            	    pushFollow(FOLLOW_3);
            	    rule__Model__GreetingsAssignment();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

             after(grammarAccess.getModelAccess().getGreetingsAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRuleMetadata"
    // InternalSystemDescriptor.g:78:1: entryRuleMetadata : ruleMetadata EOF ;
    public final void entryRuleMetadata() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:79:1: ( ruleMetadata EOF )
            // InternalSystemDescriptor.g:80:1: ruleMetadata EOF
            {
             before(grammarAccess.getMetadataRule()); 
            pushFollow(FOLLOW_1);
            ruleMetadata();

            state._fsp--;

             after(grammarAccess.getMetadataRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleMetadata"


    // $ANTLR start "ruleMetadata"
    // InternalSystemDescriptor.g:87:1: ruleMetadata : ( ( rule__Metadata__Group__0 ) ) ;
    public final void ruleMetadata() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:91:2: ( ( ( rule__Metadata__Group__0 ) ) )
            // InternalSystemDescriptor.g:92:2: ( ( rule__Metadata__Group__0 ) )
            {
            // InternalSystemDescriptor.g:92:2: ( ( rule__Metadata__Group__0 ) )
            // InternalSystemDescriptor.g:93:3: ( rule__Metadata__Group__0 )
            {
             before(grammarAccess.getMetadataAccess().getGroup()); 
            // InternalSystemDescriptor.g:94:3: ( rule__Metadata__Group__0 )
            // InternalSystemDescriptor.g:94:4: rule__Metadata__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Metadata__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getMetadataAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleMetadata"


    // $ANTLR start "entryRuleObject"
    // InternalSystemDescriptor.g:103:1: entryRuleObject : ruleObject EOF ;
    public final void entryRuleObject() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:104:1: ( ruleObject EOF )
            // InternalSystemDescriptor.g:105:1: ruleObject EOF
            {
             before(grammarAccess.getObjectRule()); 
            pushFollow(FOLLOW_1);
            ruleObject();

            state._fsp--;

             after(grammarAccess.getObjectRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleObject"


    // $ANTLR start "ruleObject"
    // InternalSystemDescriptor.g:112:1: ruleObject : ( ( rule__Object__Group__0 ) ) ;
    public final void ruleObject() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:116:2: ( ( ( rule__Object__Group__0 ) ) )
            // InternalSystemDescriptor.g:117:2: ( ( rule__Object__Group__0 ) )
            {
            // InternalSystemDescriptor.g:117:2: ( ( rule__Object__Group__0 ) )
            // InternalSystemDescriptor.g:118:3: ( rule__Object__Group__0 )
            {
             before(grammarAccess.getObjectAccess().getGroup()); 
            // InternalSystemDescriptor.g:119:3: ( rule__Object__Group__0 )
            // InternalSystemDescriptor.g:119:4: rule__Object__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Object__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getObjectAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleObject"


    // $ANTLR start "entryRuleArray"
    // InternalSystemDescriptor.g:128:1: entryRuleArray : ruleArray EOF ;
    public final void entryRuleArray() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:129:1: ( ruleArray EOF )
            // InternalSystemDescriptor.g:130:1: ruleArray EOF
            {
             before(grammarAccess.getArrayRule()); 
            pushFollow(FOLLOW_1);
            ruleArray();

            state._fsp--;

             after(grammarAccess.getArrayRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleArray"


    // $ANTLR start "ruleArray"
    // InternalSystemDescriptor.g:137:1: ruleArray : ( ( rule__Array__Group__0 ) ) ;
    public final void ruleArray() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:141:2: ( ( ( rule__Array__Group__0 ) ) )
            // InternalSystemDescriptor.g:142:2: ( ( rule__Array__Group__0 ) )
            {
            // InternalSystemDescriptor.g:142:2: ( ( rule__Array__Group__0 ) )
            // InternalSystemDescriptor.g:143:3: ( rule__Array__Group__0 )
            {
             before(grammarAccess.getArrayAccess().getGroup()); 
            // InternalSystemDescriptor.g:144:3: ( rule__Array__Group__0 )
            // InternalSystemDescriptor.g:144:4: rule__Array__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Array__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getArrayAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleArray"


    // $ANTLR start "entryRuleEmptyObject"
    // InternalSystemDescriptor.g:153:1: entryRuleEmptyObject : ruleEmptyObject EOF ;
    public final void entryRuleEmptyObject() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:154:1: ( ruleEmptyObject EOF )
            // InternalSystemDescriptor.g:155:1: ruleEmptyObject EOF
            {
             before(grammarAccess.getEmptyObjectRule()); 
            pushFollow(FOLLOW_1);
            ruleEmptyObject();

            state._fsp--;

             after(grammarAccess.getEmptyObjectRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleEmptyObject"


    // $ANTLR start "ruleEmptyObject"
    // InternalSystemDescriptor.g:162:1: ruleEmptyObject : ( ( rule__EmptyObject__IsEmptyAssignment ) ) ;
    public final void ruleEmptyObject() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:166:2: ( ( ( rule__EmptyObject__IsEmptyAssignment ) ) )
            // InternalSystemDescriptor.g:167:2: ( ( rule__EmptyObject__IsEmptyAssignment ) )
            {
            // InternalSystemDescriptor.g:167:2: ( ( rule__EmptyObject__IsEmptyAssignment ) )
            // InternalSystemDescriptor.g:168:3: ( rule__EmptyObject__IsEmptyAssignment )
            {
             before(grammarAccess.getEmptyObjectAccess().getIsEmptyAssignment()); 
            // InternalSystemDescriptor.g:169:3: ( rule__EmptyObject__IsEmptyAssignment )
            // InternalSystemDescriptor.g:169:4: rule__EmptyObject__IsEmptyAssignment
            {
            pushFollow(FOLLOW_2);
            rule__EmptyObject__IsEmptyAssignment();

            state._fsp--;


            }

             after(grammarAccess.getEmptyObjectAccess().getIsEmptyAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleEmptyObject"


    // $ANTLR start "entryRuleEmptyArray"
    // InternalSystemDescriptor.g:178:1: entryRuleEmptyArray : ruleEmptyArray EOF ;
    public final void entryRuleEmptyArray() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:179:1: ( ruleEmptyArray EOF )
            // InternalSystemDescriptor.g:180:1: ruleEmptyArray EOF
            {
             before(grammarAccess.getEmptyArrayRule()); 
            pushFollow(FOLLOW_1);
            ruleEmptyArray();

            state._fsp--;

             after(grammarAccess.getEmptyArrayRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleEmptyArray"


    // $ANTLR start "ruleEmptyArray"
    // InternalSystemDescriptor.g:187:1: ruleEmptyArray : ( ( rule__EmptyArray__IsEmptyAssignment ) ) ;
    public final void ruleEmptyArray() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:191:2: ( ( ( rule__EmptyArray__IsEmptyAssignment ) ) )
            // InternalSystemDescriptor.g:192:2: ( ( rule__EmptyArray__IsEmptyAssignment ) )
            {
            // InternalSystemDescriptor.g:192:2: ( ( rule__EmptyArray__IsEmptyAssignment ) )
            // InternalSystemDescriptor.g:193:3: ( rule__EmptyArray__IsEmptyAssignment )
            {
             before(grammarAccess.getEmptyArrayAccess().getIsEmptyAssignment()); 
            // InternalSystemDescriptor.g:194:3: ( rule__EmptyArray__IsEmptyAssignment )
            // InternalSystemDescriptor.g:194:4: rule__EmptyArray__IsEmptyAssignment
            {
            pushFollow(FOLLOW_2);
            rule__EmptyArray__IsEmptyAssignment();

            state._fsp--;


            }

             after(grammarAccess.getEmptyArrayAccess().getIsEmptyAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleEmptyArray"


    // $ANTLR start "entryRuleObjectValue"
    // InternalSystemDescriptor.g:203:1: entryRuleObjectValue : ruleObjectValue EOF ;
    public final void entryRuleObjectValue() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:204:1: ( ruleObjectValue EOF )
            // InternalSystemDescriptor.g:205:1: ruleObjectValue EOF
            {
             before(grammarAccess.getObjectValueRule()); 
            pushFollow(FOLLOW_1);
            ruleObjectValue();

            state._fsp--;

             after(grammarAccess.getObjectValueRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleObjectValue"


    // $ANTLR start "ruleObjectValue"
    // InternalSystemDescriptor.g:212:1: ruleObjectValue : ( ( rule__ObjectValue__Alternatives ) ) ;
    public final void ruleObjectValue() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:216:2: ( ( ( rule__ObjectValue__Alternatives ) ) )
            // InternalSystemDescriptor.g:217:2: ( ( rule__ObjectValue__Alternatives ) )
            {
            // InternalSystemDescriptor.g:217:2: ( ( rule__ObjectValue__Alternatives ) )
            // InternalSystemDescriptor.g:218:3: ( rule__ObjectValue__Alternatives )
            {
             before(grammarAccess.getObjectValueAccess().getAlternatives()); 
            // InternalSystemDescriptor.g:219:3: ( rule__ObjectValue__Alternatives )
            // InternalSystemDescriptor.g:219:4: rule__ObjectValue__Alternatives
            {
            pushFollow(FOLLOW_2);
            rule__ObjectValue__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getObjectValueAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleObjectValue"


    // $ANTLR start "entryRuleTerminalObject"
    // InternalSystemDescriptor.g:228:1: entryRuleTerminalObject : ruleTerminalObject EOF ;
    public final void entryRuleTerminalObject() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:229:1: ( ruleTerminalObject EOF )
            // InternalSystemDescriptor.g:230:1: ruleTerminalObject EOF
            {
             before(grammarAccess.getTerminalObjectRule()); 
            pushFollow(FOLLOW_1);
            ruleTerminalObject();

            state._fsp--;

             after(grammarAccess.getTerminalObjectRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleTerminalObject"


    // $ANTLR start "ruleTerminalObject"
    // InternalSystemDescriptor.g:237:1: ruleTerminalObject : ( ( rule__TerminalObject__Group__0 ) ) ;
    public final void ruleTerminalObject() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:241:2: ( ( ( rule__TerminalObject__Group__0 ) ) )
            // InternalSystemDescriptor.g:242:2: ( ( rule__TerminalObject__Group__0 ) )
            {
            // InternalSystemDescriptor.g:242:2: ( ( rule__TerminalObject__Group__0 ) )
            // InternalSystemDescriptor.g:243:3: ( rule__TerminalObject__Group__0 )
            {
             before(grammarAccess.getTerminalObjectAccess().getGroup()); 
            // InternalSystemDescriptor.g:244:3: ( rule__TerminalObject__Group__0 )
            // InternalSystemDescriptor.g:244:4: rule__TerminalObject__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__TerminalObject__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getTerminalObjectAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleTerminalObject"


    // $ANTLR start "rule__ObjectValue__Alternatives"
    // InternalSystemDescriptor.g:252:1: rule__ObjectValue__Alternatives : ( ( ( rule__ObjectValue__ValueAssignment_0 ) ) | ( ruleObject ) | ( ruleArray ) | ( ruleEmptyObject ) | ( ruleEmptyArray ) );
    public final void rule__ObjectValue__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:256:1: ( ( ( rule__ObjectValue__ValueAssignment_0 ) ) | ( ruleObject ) | ( ruleArray ) | ( ruleEmptyObject ) | ( ruleEmptyArray ) )
            int alt2=5;
            switch ( input.LA(1) ) {
            case RULE_STRING:
                {
                alt2=1;
                }
                break;
            case 11:
                {
                alt2=2;
                }
                break;
            case 14:
                {
                alt2=3;
                }
                break;
            case 18:
                {
                alt2=4;
                }
                break;
            case 19:
                {
                alt2=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // InternalSystemDescriptor.g:257:2: ( ( rule__ObjectValue__ValueAssignment_0 ) )
                    {
                    // InternalSystemDescriptor.g:257:2: ( ( rule__ObjectValue__ValueAssignment_0 ) )
                    // InternalSystemDescriptor.g:258:3: ( rule__ObjectValue__ValueAssignment_0 )
                    {
                     before(grammarAccess.getObjectValueAccess().getValueAssignment_0()); 
                    // InternalSystemDescriptor.g:259:3: ( rule__ObjectValue__ValueAssignment_0 )
                    // InternalSystemDescriptor.g:259:4: rule__ObjectValue__ValueAssignment_0
                    {
                    pushFollow(FOLLOW_2);
                    rule__ObjectValue__ValueAssignment_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getObjectValueAccess().getValueAssignment_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalSystemDescriptor.g:263:2: ( ruleObject )
                    {
                    // InternalSystemDescriptor.g:263:2: ( ruleObject )
                    // InternalSystemDescriptor.g:264:3: ruleObject
                    {
                     before(grammarAccess.getObjectValueAccess().getObjectParserRuleCall_1()); 
                    pushFollow(FOLLOW_2);
                    ruleObject();

                    state._fsp--;

                     after(grammarAccess.getObjectValueAccess().getObjectParserRuleCall_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalSystemDescriptor.g:269:2: ( ruleArray )
                    {
                    // InternalSystemDescriptor.g:269:2: ( ruleArray )
                    // InternalSystemDescriptor.g:270:3: ruleArray
                    {
                     before(grammarAccess.getObjectValueAccess().getArrayParserRuleCall_2()); 
                    pushFollow(FOLLOW_2);
                    ruleArray();

                    state._fsp--;

                     after(grammarAccess.getObjectValueAccess().getArrayParserRuleCall_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalSystemDescriptor.g:275:2: ( ruleEmptyObject )
                    {
                    // InternalSystemDescriptor.g:275:2: ( ruleEmptyObject )
                    // InternalSystemDescriptor.g:276:3: ruleEmptyObject
                    {
                     before(grammarAccess.getObjectValueAccess().getEmptyObjectParserRuleCall_3()); 
                    pushFollow(FOLLOW_2);
                    ruleEmptyObject();

                    state._fsp--;

                     after(grammarAccess.getObjectValueAccess().getEmptyObjectParserRuleCall_3()); 

                    }


                    }
                    break;
                case 5 :
                    // InternalSystemDescriptor.g:281:2: ( ruleEmptyArray )
                    {
                    // InternalSystemDescriptor.g:281:2: ( ruleEmptyArray )
                    // InternalSystemDescriptor.g:282:3: ruleEmptyArray
                    {
                     before(grammarAccess.getObjectValueAccess().getEmptyArrayParserRuleCall_4()); 
                    pushFollow(FOLLOW_2);
                    ruleEmptyArray();

                    state._fsp--;

                     after(grammarAccess.getObjectValueAccess().getEmptyArrayParserRuleCall_4()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ObjectValue__Alternatives"


    // $ANTLR start "rule__Metadata__Group__0"
    // InternalSystemDescriptor.g:291:1: rule__Metadata__Group__0 : rule__Metadata__Group__0__Impl rule__Metadata__Group__1 ;
    public final void rule__Metadata__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:295:1: ( rule__Metadata__Group__0__Impl rule__Metadata__Group__1 )
            // InternalSystemDescriptor.g:296:2: rule__Metadata__Group__0__Impl rule__Metadata__Group__1
            {
            pushFollow(FOLLOW_4);
            rule__Metadata__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Metadata__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Metadata__Group__0"


    // $ANTLR start "rule__Metadata__Group__0__Impl"
    // InternalSystemDescriptor.g:303:1: rule__Metadata__Group__0__Impl : ( ( rule__Metadata__TypeAssignment_0 ) ) ;
    public final void rule__Metadata__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:307:1: ( ( ( rule__Metadata__TypeAssignment_0 ) ) )
            // InternalSystemDescriptor.g:308:1: ( ( rule__Metadata__TypeAssignment_0 ) )
            {
            // InternalSystemDescriptor.g:308:1: ( ( rule__Metadata__TypeAssignment_0 ) )
            // InternalSystemDescriptor.g:309:2: ( rule__Metadata__TypeAssignment_0 )
            {
             before(grammarAccess.getMetadataAccess().getTypeAssignment_0()); 
            // InternalSystemDescriptor.g:310:2: ( rule__Metadata__TypeAssignment_0 )
            // InternalSystemDescriptor.g:310:3: rule__Metadata__TypeAssignment_0
            {
            pushFollow(FOLLOW_2);
            rule__Metadata__TypeAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getMetadataAccess().getTypeAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Metadata__Group__0__Impl"


    // $ANTLR start "rule__Metadata__Group__1"
    // InternalSystemDescriptor.g:318:1: rule__Metadata__Group__1 : rule__Metadata__Group__1__Impl ;
    public final void rule__Metadata__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:322:1: ( rule__Metadata__Group__1__Impl )
            // InternalSystemDescriptor.g:323:2: rule__Metadata__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Metadata__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Metadata__Group__1"


    // $ANTLR start "rule__Metadata__Group__1__Impl"
    // InternalSystemDescriptor.g:329:1: rule__Metadata__Group__1__Impl : ( ( rule__Metadata__JsonAssignment_1 )? ) ;
    public final void rule__Metadata__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:333:1: ( ( ( rule__Metadata__JsonAssignment_1 )? ) )
            // InternalSystemDescriptor.g:334:1: ( ( rule__Metadata__JsonAssignment_1 )? )
            {
            // InternalSystemDescriptor.g:334:1: ( ( rule__Metadata__JsonAssignment_1 )? )
            // InternalSystemDescriptor.g:335:2: ( rule__Metadata__JsonAssignment_1 )?
            {
             before(grammarAccess.getMetadataAccess().getJsonAssignment_1()); 
            // InternalSystemDescriptor.g:336:2: ( rule__Metadata__JsonAssignment_1 )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==11) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalSystemDescriptor.g:336:3: rule__Metadata__JsonAssignment_1
                    {
                    pushFollow(FOLLOW_2);
                    rule__Metadata__JsonAssignment_1();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getMetadataAccess().getJsonAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Metadata__Group__1__Impl"


    // $ANTLR start "rule__Object__Group__0"
    // InternalSystemDescriptor.g:345:1: rule__Object__Group__0 : rule__Object__Group__0__Impl rule__Object__Group__1 ;
    public final void rule__Object__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:349:1: ( rule__Object__Group__0__Impl rule__Object__Group__1 )
            // InternalSystemDescriptor.g:350:2: rule__Object__Group__0__Impl rule__Object__Group__1
            {
            pushFollow(FOLLOW_5);
            rule__Object__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Object__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__Group__0"


    // $ANTLR start "rule__Object__Group__0__Impl"
    // InternalSystemDescriptor.g:357:1: rule__Object__Group__0__Impl : ( '{' ) ;
    public final void rule__Object__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:361:1: ( ( '{' ) )
            // InternalSystemDescriptor.g:362:1: ( '{' )
            {
            // InternalSystemDescriptor.g:362:1: ( '{' )
            // InternalSystemDescriptor.g:363:2: '{'
            {
             before(grammarAccess.getObjectAccess().getLeftCurlyBracketKeyword_0()); 
            match(input,11,FOLLOW_2); 
             after(grammarAccess.getObjectAccess().getLeftCurlyBracketKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__Group__0__Impl"


    // $ANTLR start "rule__Object__Group__1"
    // InternalSystemDescriptor.g:372:1: rule__Object__Group__1 : rule__Object__Group__1__Impl rule__Object__Group__2 ;
    public final void rule__Object__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:376:1: ( rule__Object__Group__1__Impl rule__Object__Group__2 )
            // InternalSystemDescriptor.g:377:2: rule__Object__Group__1__Impl rule__Object__Group__2
            {
            pushFollow(FOLLOW_6);
            rule__Object__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Object__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__Group__1"


    // $ANTLR start "rule__Object__Group__1__Impl"
    // InternalSystemDescriptor.g:384:1: rule__Object__Group__1__Impl : ( ( rule__Object__FirstObjectAssignment_1 ) ) ;
    public final void rule__Object__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:388:1: ( ( ( rule__Object__FirstObjectAssignment_1 ) ) )
            // InternalSystemDescriptor.g:389:1: ( ( rule__Object__FirstObjectAssignment_1 ) )
            {
            // InternalSystemDescriptor.g:389:1: ( ( rule__Object__FirstObjectAssignment_1 ) )
            // InternalSystemDescriptor.g:390:2: ( rule__Object__FirstObjectAssignment_1 )
            {
             before(grammarAccess.getObjectAccess().getFirstObjectAssignment_1()); 
            // InternalSystemDescriptor.g:391:2: ( rule__Object__FirstObjectAssignment_1 )
            // InternalSystemDescriptor.g:391:3: rule__Object__FirstObjectAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Object__FirstObjectAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getObjectAccess().getFirstObjectAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__Group__1__Impl"


    // $ANTLR start "rule__Object__Group__2"
    // InternalSystemDescriptor.g:399:1: rule__Object__Group__2 : rule__Object__Group__2__Impl rule__Object__Group__3 ;
    public final void rule__Object__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:403:1: ( rule__Object__Group__2__Impl rule__Object__Group__3 )
            // InternalSystemDescriptor.g:404:2: rule__Object__Group__2__Impl rule__Object__Group__3
            {
            pushFollow(FOLLOW_6);
            rule__Object__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Object__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__Group__2"


    // $ANTLR start "rule__Object__Group__2__Impl"
    // InternalSystemDescriptor.g:411:1: rule__Object__Group__2__Impl : ( ( rule__Object__Group_2__0 )* ) ;
    public final void rule__Object__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:415:1: ( ( ( rule__Object__Group_2__0 )* ) )
            // InternalSystemDescriptor.g:416:1: ( ( rule__Object__Group_2__0 )* )
            {
            // InternalSystemDescriptor.g:416:1: ( ( rule__Object__Group_2__0 )* )
            // InternalSystemDescriptor.g:417:2: ( rule__Object__Group_2__0 )*
            {
             before(grammarAccess.getObjectAccess().getGroup_2()); 
            // InternalSystemDescriptor.g:418:2: ( rule__Object__Group_2__0 )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==13) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalSystemDescriptor.g:418:3: rule__Object__Group_2__0
            	    {
            	    pushFollow(FOLLOW_7);
            	    rule__Object__Group_2__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

             after(grammarAccess.getObjectAccess().getGroup_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__Group__2__Impl"


    // $ANTLR start "rule__Object__Group__3"
    // InternalSystemDescriptor.g:426:1: rule__Object__Group__3 : rule__Object__Group__3__Impl ;
    public final void rule__Object__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:430:1: ( rule__Object__Group__3__Impl )
            // InternalSystemDescriptor.g:431:2: rule__Object__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Object__Group__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__Group__3"


    // $ANTLR start "rule__Object__Group__3__Impl"
    // InternalSystemDescriptor.g:437:1: rule__Object__Group__3__Impl : ( '}' ) ;
    public final void rule__Object__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:441:1: ( ( '}' ) )
            // InternalSystemDescriptor.g:442:1: ( '}' )
            {
            // InternalSystemDescriptor.g:442:1: ( '}' )
            // InternalSystemDescriptor.g:443:2: '}'
            {
             before(grammarAccess.getObjectAccess().getRightCurlyBracketKeyword_3()); 
            match(input,12,FOLLOW_2); 
             after(grammarAccess.getObjectAccess().getRightCurlyBracketKeyword_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__Group__3__Impl"


    // $ANTLR start "rule__Object__Group_2__0"
    // InternalSystemDescriptor.g:453:1: rule__Object__Group_2__0 : rule__Object__Group_2__0__Impl rule__Object__Group_2__1 ;
    public final void rule__Object__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:457:1: ( rule__Object__Group_2__0__Impl rule__Object__Group_2__1 )
            // InternalSystemDescriptor.g:458:2: rule__Object__Group_2__0__Impl rule__Object__Group_2__1
            {
            pushFollow(FOLLOW_5);
            rule__Object__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Object__Group_2__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__Group_2__0"


    // $ANTLR start "rule__Object__Group_2__0__Impl"
    // InternalSystemDescriptor.g:465:1: rule__Object__Group_2__0__Impl : ( ',' ) ;
    public final void rule__Object__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:469:1: ( ( ',' ) )
            // InternalSystemDescriptor.g:470:1: ( ',' )
            {
            // InternalSystemDescriptor.g:470:1: ( ',' )
            // InternalSystemDescriptor.g:471:2: ','
            {
             before(grammarAccess.getObjectAccess().getCommaKeyword_2_0()); 
            match(input,13,FOLLOW_2); 
             after(grammarAccess.getObjectAccess().getCommaKeyword_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__Group_2__0__Impl"


    // $ANTLR start "rule__Object__Group_2__1"
    // InternalSystemDescriptor.g:480:1: rule__Object__Group_2__1 : rule__Object__Group_2__1__Impl ;
    public final void rule__Object__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:484:1: ( rule__Object__Group_2__1__Impl )
            // InternalSystemDescriptor.g:485:2: rule__Object__Group_2__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Object__Group_2__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__Group_2__1"


    // $ANTLR start "rule__Object__Group_2__1__Impl"
    // InternalSystemDescriptor.g:491:1: rule__Object__Group_2__1__Impl : ( ( rule__Object__ObjectsAssignment_2_1 ) ) ;
    public final void rule__Object__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:495:1: ( ( ( rule__Object__ObjectsAssignment_2_1 ) ) )
            // InternalSystemDescriptor.g:496:1: ( ( rule__Object__ObjectsAssignment_2_1 ) )
            {
            // InternalSystemDescriptor.g:496:1: ( ( rule__Object__ObjectsAssignment_2_1 ) )
            // InternalSystemDescriptor.g:497:2: ( rule__Object__ObjectsAssignment_2_1 )
            {
             before(grammarAccess.getObjectAccess().getObjectsAssignment_2_1()); 
            // InternalSystemDescriptor.g:498:2: ( rule__Object__ObjectsAssignment_2_1 )
            // InternalSystemDescriptor.g:498:3: rule__Object__ObjectsAssignment_2_1
            {
            pushFollow(FOLLOW_2);
            rule__Object__ObjectsAssignment_2_1();

            state._fsp--;


            }

             after(grammarAccess.getObjectAccess().getObjectsAssignment_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__Group_2__1__Impl"


    // $ANTLR start "rule__Array__Group__0"
    // InternalSystemDescriptor.g:507:1: rule__Array__Group__0 : rule__Array__Group__0__Impl rule__Array__Group__1 ;
    public final void rule__Array__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:511:1: ( rule__Array__Group__0__Impl rule__Array__Group__1 )
            // InternalSystemDescriptor.g:512:2: rule__Array__Group__0__Impl rule__Array__Group__1
            {
            pushFollow(FOLLOW_8);
            rule__Array__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Array__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__Group__0"


    // $ANTLR start "rule__Array__Group__0__Impl"
    // InternalSystemDescriptor.g:519:1: rule__Array__Group__0__Impl : ( '[' ) ;
    public final void rule__Array__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:523:1: ( ( '[' ) )
            // InternalSystemDescriptor.g:524:1: ( '[' )
            {
            // InternalSystemDescriptor.g:524:1: ( '[' )
            // InternalSystemDescriptor.g:525:2: '['
            {
             before(grammarAccess.getArrayAccess().getLeftSquareBracketKeyword_0()); 
            match(input,14,FOLLOW_2); 
             after(grammarAccess.getArrayAccess().getLeftSquareBracketKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__Group__0__Impl"


    // $ANTLR start "rule__Array__Group__1"
    // InternalSystemDescriptor.g:534:1: rule__Array__Group__1 : rule__Array__Group__1__Impl rule__Array__Group__2 ;
    public final void rule__Array__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:538:1: ( rule__Array__Group__1__Impl rule__Array__Group__2 )
            // InternalSystemDescriptor.g:539:2: rule__Array__Group__1__Impl rule__Array__Group__2
            {
            pushFollow(FOLLOW_9);
            rule__Array__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Array__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__Group__1"


    // $ANTLR start "rule__Array__Group__1__Impl"
    // InternalSystemDescriptor.g:546:1: rule__Array__Group__1__Impl : ( ( rule__Array__FirstItemAssignment_1 ) ) ;
    public final void rule__Array__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:550:1: ( ( ( rule__Array__FirstItemAssignment_1 ) ) )
            // InternalSystemDescriptor.g:551:1: ( ( rule__Array__FirstItemAssignment_1 ) )
            {
            // InternalSystemDescriptor.g:551:1: ( ( rule__Array__FirstItemAssignment_1 ) )
            // InternalSystemDescriptor.g:552:2: ( rule__Array__FirstItemAssignment_1 )
            {
             before(grammarAccess.getArrayAccess().getFirstItemAssignment_1()); 
            // InternalSystemDescriptor.g:553:2: ( rule__Array__FirstItemAssignment_1 )
            // InternalSystemDescriptor.g:553:3: rule__Array__FirstItemAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Array__FirstItemAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getArrayAccess().getFirstItemAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__Group__1__Impl"


    // $ANTLR start "rule__Array__Group__2"
    // InternalSystemDescriptor.g:561:1: rule__Array__Group__2 : rule__Array__Group__2__Impl rule__Array__Group__3 ;
    public final void rule__Array__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:565:1: ( rule__Array__Group__2__Impl rule__Array__Group__3 )
            // InternalSystemDescriptor.g:566:2: rule__Array__Group__2__Impl rule__Array__Group__3
            {
            pushFollow(FOLLOW_9);
            rule__Array__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Array__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__Group__2"


    // $ANTLR start "rule__Array__Group__2__Impl"
    // InternalSystemDescriptor.g:573:1: rule__Array__Group__2__Impl : ( ( rule__Array__Group_2__0 )* ) ;
    public final void rule__Array__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:577:1: ( ( ( rule__Array__Group_2__0 )* ) )
            // InternalSystemDescriptor.g:578:1: ( ( rule__Array__Group_2__0 )* )
            {
            // InternalSystemDescriptor.g:578:1: ( ( rule__Array__Group_2__0 )* )
            // InternalSystemDescriptor.g:579:2: ( rule__Array__Group_2__0 )*
            {
             before(grammarAccess.getArrayAccess().getGroup_2()); 
            // InternalSystemDescriptor.g:580:2: ( rule__Array__Group_2__0 )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==13) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalSystemDescriptor.g:580:3: rule__Array__Group_2__0
            	    {
            	    pushFollow(FOLLOW_7);
            	    rule__Array__Group_2__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

             after(grammarAccess.getArrayAccess().getGroup_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__Group__2__Impl"


    // $ANTLR start "rule__Array__Group__3"
    // InternalSystemDescriptor.g:588:1: rule__Array__Group__3 : rule__Array__Group__3__Impl ;
    public final void rule__Array__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:592:1: ( rule__Array__Group__3__Impl )
            // InternalSystemDescriptor.g:593:2: rule__Array__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Array__Group__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__Group__3"


    // $ANTLR start "rule__Array__Group__3__Impl"
    // InternalSystemDescriptor.g:599:1: rule__Array__Group__3__Impl : ( ']' ) ;
    public final void rule__Array__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:603:1: ( ( ']' ) )
            // InternalSystemDescriptor.g:604:1: ( ']' )
            {
            // InternalSystemDescriptor.g:604:1: ( ']' )
            // InternalSystemDescriptor.g:605:2: ']'
            {
             before(grammarAccess.getArrayAccess().getRightSquareBracketKeyword_3()); 
            match(input,15,FOLLOW_2); 
             after(grammarAccess.getArrayAccess().getRightSquareBracketKeyword_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__Group__3__Impl"


    // $ANTLR start "rule__Array__Group_2__0"
    // InternalSystemDescriptor.g:615:1: rule__Array__Group_2__0 : rule__Array__Group_2__0__Impl rule__Array__Group_2__1 ;
    public final void rule__Array__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:619:1: ( rule__Array__Group_2__0__Impl rule__Array__Group_2__1 )
            // InternalSystemDescriptor.g:620:2: rule__Array__Group_2__0__Impl rule__Array__Group_2__1
            {
            pushFollow(FOLLOW_8);
            rule__Array__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Array__Group_2__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__Group_2__0"


    // $ANTLR start "rule__Array__Group_2__0__Impl"
    // InternalSystemDescriptor.g:627:1: rule__Array__Group_2__0__Impl : ( ',' ) ;
    public final void rule__Array__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:631:1: ( ( ',' ) )
            // InternalSystemDescriptor.g:632:1: ( ',' )
            {
            // InternalSystemDescriptor.g:632:1: ( ',' )
            // InternalSystemDescriptor.g:633:2: ','
            {
             before(grammarAccess.getArrayAccess().getCommaKeyword_2_0()); 
            match(input,13,FOLLOW_2); 
             after(grammarAccess.getArrayAccess().getCommaKeyword_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__Group_2__0__Impl"


    // $ANTLR start "rule__Array__Group_2__1"
    // InternalSystemDescriptor.g:642:1: rule__Array__Group_2__1 : rule__Array__Group_2__1__Impl ;
    public final void rule__Array__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:646:1: ( rule__Array__Group_2__1__Impl )
            // InternalSystemDescriptor.g:647:2: rule__Array__Group_2__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Array__Group_2__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__Group_2__1"


    // $ANTLR start "rule__Array__Group_2__1__Impl"
    // InternalSystemDescriptor.g:653:1: rule__Array__Group_2__1__Impl : ( ( rule__Array__ItemsAssignment_2_1 ) ) ;
    public final void rule__Array__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:657:1: ( ( ( rule__Array__ItemsAssignment_2_1 ) ) )
            // InternalSystemDescriptor.g:658:1: ( ( rule__Array__ItemsAssignment_2_1 ) )
            {
            // InternalSystemDescriptor.g:658:1: ( ( rule__Array__ItemsAssignment_2_1 ) )
            // InternalSystemDescriptor.g:659:2: ( rule__Array__ItemsAssignment_2_1 )
            {
             before(grammarAccess.getArrayAccess().getItemsAssignment_2_1()); 
            // InternalSystemDescriptor.g:660:2: ( rule__Array__ItemsAssignment_2_1 )
            // InternalSystemDescriptor.g:660:3: rule__Array__ItemsAssignment_2_1
            {
            pushFollow(FOLLOW_2);
            rule__Array__ItemsAssignment_2_1();

            state._fsp--;


            }

             after(grammarAccess.getArrayAccess().getItemsAssignment_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__Group_2__1__Impl"


    // $ANTLR start "rule__TerminalObject__Group__0"
    // InternalSystemDescriptor.g:669:1: rule__TerminalObject__Group__0 : rule__TerminalObject__Group__0__Impl rule__TerminalObject__Group__1 ;
    public final void rule__TerminalObject__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:673:1: ( rule__TerminalObject__Group__0__Impl rule__TerminalObject__Group__1 )
            // InternalSystemDescriptor.g:674:2: rule__TerminalObject__Group__0__Impl rule__TerminalObject__Group__1
            {
            pushFollow(FOLLOW_10);
            rule__TerminalObject__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__TerminalObject__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TerminalObject__Group__0"


    // $ANTLR start "rule__TerminalObject__Group__0__Impl"
    // InternalSystemDescriptor.g:681:1: rule__TerminalObject__Group__0__Impl : ( ( rule__TerminalObject__ElementAssignment_0 ) ) ;
    public final void rule__TerminalObject__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:685:1: ( ( ( rule__TerminalObject__ElementAssignment_0 ) ) )
            // InternalSystemDescriptor.g:686:1: ( ( rule__TerminalObject__ElementAssignment_0 ) )
            {
            // InternalSystemDescriptor.g:686:1: ( ( rule__TerminalObject__ElementAssignment_0 ) )
            // InternalSystemDescriptor.g:687:2: ( rule__TerminalObject__ElementAssignment_0 )
            {
             before(grammarAccess.getTerminalObjectAccess().getElementAssignment_0()); 
            // InternalSystemDescriptor.g:688:2: ( rule__TerminalObject__ElementAssignment_0 )
            // InternalSystemDescriptor.g:688:3: rule__TerminalObject__ElementAssignment_0
            {
            pushFollow(FOLLOW_2);
            rule__TerminalObject__ElementAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getTerminalObjectAccess().getElementAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TerminalObject__Group__0__Impl"


    // $ANTLR start "rule__TerminalObject__Group__1"
    // InternalSystemDescriptor.g:696:1: rule__TerminalObject__Group__1 : rule__TerminalObject__Group__1__Impl rule__TerminalObject__Group__2 ;
    public final void rule__TerminalObject__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:700:1: ( rule__TerminalObject__Group__1__Impl rule__TerminalObject__Group__2 )
            // InternalSystemDescriptor.g:701:2: rule__TerminalObject__Group__1__Impl rule__TerminalObject__Group__2
            {
            pushFollow(FOLLOW_8);
            rule__TerminalObject__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__TerminalObject__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TerminalObject__Group__1"


    // $ANTLR start "rule__TerminalObject__Group__1__Impl"
    // InternalSystemDescriptor.g:708:1: rule__TerminalObject__Group__1__Impl : ( ':' ) ;
    public final void rule__TerminalObject__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:712:1: ( ( ':' ) )
            // InternalSystemDescriptor.g:713:1: ( ':' )
            {
            // InternalSystemDescriptor.g:713:1: ( ':' )
            // InternalSystemDescriptor.g:714:2: ':'
            {
             before(grammarAccess.getTerminalObjectAccess().getColonKeyword_1()); 
            match(input,16,FOLLOW_2); 
             after(grammarAccess.getTerminalObjectAccess().getColonKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TerminalObject__Group__1__Impl"


    // $ANTLR start "rule__TerminalObject__Group__2"
    // InternalSystemDescriptor.g:723:1: rule__TerminalObject__Group__2 : rule__TerminalObject__Group__2__Impl ;
    public final void rule__TerminalObject__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:727:1: ( rule__TerminalObject__Group__2__Impl )
            // InternalSystemDescriptor.g:728:2: rule__TerminalObject__Group__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__TerminalObject__Group__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TerminalObject__Group__2"


    // $ANTLR start "rule__TerminalObject__Group__2__Impl"
    // InternalSystemDescriptor.g:734:1: rule__TerminalObject__Group__2__Impl : ( ( rule__TerminalObject__ContentAssignment_2 ) ) ;
    public final void rule__TerminalObject__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:738:1: ( ( ( rule__TerminalObject__ContentAssignment_2 ) ) )
            // InternalSystemDescriptor.g:739:1: ( ( rule__TerminalObject__ContentAssignment_2 ) )
            {
            // InternalSystemDescriptor.g:739:1: ( ( rule__TerminalObject__ContentAssignment_2 ) )
            // InternalSystemDescriptor.g:740:2: ( rule__TerminalObject__ContentAssignment_2 )
            {
             before(grammarAccess.getTerminalObjectAccess().getContentAssignment_2()); 
            // InternalSystemDescriptor.g:741:2: ( rule__TerminalObject__ContentAssignment_2 )
            // InternalSystemDescriptor.g:741:3: rule__TerminalObject__ContentAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__TerminalObject__ContentAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getTerminalObjectAccess().getContentAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TerminalObject__Group__2__Impl"


    // $ANTLR start "rule__Model__GreetingsAssignment"
    // InternalSystemDescriptor.g:750:1: rule__Model__GreetingsAssignment : ( ruleMetadata ) ;
    public final void rule__Model__GreetingsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:754:1: ( ( ruleMetadata ) )
            // InternalSystemDescriptor.g:755:2: ( ruleMetadata )
            {
            // InternalSystemDescriptor.g:755:2: ( ruleMetadata )
            // InternalSystemDescriptor.g:756:3: ruleMetadata
            {
             before(grammarAccess.getModelAccess().getGreetingsMetadataParserRuleCall_0()); 
            pushFollow(FOLLOW_2);
            ruleMetadata();

            state._fsp--;

             after(grammarAccess.getModelAccess().getGreetingsMetadataParserRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__GreetingsAssignment"


    // $ANTLR start "rule__Metadata__TypeAssignment_0"
    // InternalSystemDescriptor.g:765:1: rule__Metadata__TypeAssignment_0 : ( ( 'metadata' ) ) ;
    public final void rule__Metadata__TypeAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:769:1: ( ( ( 'metadata' ) ) )
            // InternalSystemDescriptor.g:770:2: ( ( 'metadata' ) )
            {
            // InternalSystemDescriptor.g:770:2: ( ( 'metadata' ) )
            // InternalSystemDescriptor.g:771:3: ( 'metadata' )
            {
             before(grammarAccess.getMetadataAccess().getTypeMetadataKeyword_0_0()); 
            // InternalSystemDescriptor.g:772:3: ( 'metadata' )
            // InternalSystemDescriptor.g:773:4: 'metadata'
            {
             before(grammarAccess.getMetadataAccess().getTypeMetadataKeyword_0_0()); 
            match(input,17,FOLLOW_2); 
             after(grammarAccess.getMetadataAccess().getTypeMetadataKeyword_0_0()); 

            }

             after(grammarAccess.getMetadataAccess().getTypeMetadataKeyword_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Metadata__TypeAssignment_0"


    // $ANTLR start "rule__Metadata__JsonAssignment_1"
    // InternalSystemDescriptor.g:784:1: rule__Metadata__JsonAssignment_1 : ( ruleObject ) ;
    public final void rule__Metadata__JsonAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:788:1: ( ( ruleObject ) )
            // InternalSystemDescriptor.g:789:2: ( ruleObject )
            {
            // InternalSystemDescriptor.g:789:2: ( ruleObject )
            // InternalSystemDescriptor.g:790:3: ruleObject
            {
             before(grammarAccess.getMetadataAccess().getJsonObjectParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
            ruleObject();

            state._fsp--;

             after(grammarAccess.getMetadataAccess().getJsonObjectParserRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Metadata__JsonAssignment_1"


    // $ANTLR start "rule__Object__FirstObjectAssignment_1"
    // InternalSystemDescriptor.g:799:1: rule__Object__FirstObjectAssignment_1 : ( ruleTerminalObject ) ;
    public final void rule__Object__FirstObjectAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:803:1: ( ( ruleTerminalObject ) )
            // InternalSystemDescriptor.g:804:2: ( ruleTerminalObject )
            {
            // InternalSystemDescriptor.g:804:2: ( ruleTerminalObject )
            // InternalSystemDescriptor.g:805:3: ruleTerminalObject
            {
             before(grammarAccess.getObjectAccess().getFirstObjectTerminalObjectParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
            ruleTerminalObject();

            state._fsp--;

             after(grammarAccess.getObjectAccess().getFirstObjectTerminalObjectParserRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__FirstObjectAssignment_1"


    // $ANTLR start "rule__Object__ObjectsAssignment_2_1"
    // InternalSystemDescriptor.g:814:1: rule__Object__ObjectsAssignment_2_1 : ( ruleTerminalObject ) ;
    public final void rule__Object__ObjectsAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:818:1: ( ( ruleTerminalObject ) )
            // InternalSystemDescriptor.g:819:2: ( ruleTerminalObject )
            {
            // InternalSystemDescriptor.g:819:2: ( ruleTerminalObject )
            // InternalSystemDescriptor.g:820:3: ruleTerminalObject
            {
             before(grammarAccess.getObjectAccess().getObjectsTerminalObjectParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_2);
            ruleTerminalObject();

            state._fsp--;

             after(grammarAccess.getObjectAccess().getObjectsTerminalObjectParserRuleCall_2_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Object__ObjectsAssignment_2_1"


    // $ANTLR start "rule__Array__FirstItemAssignment_1"
    // InternalSystemDescriptor.g:829:1: rule__Array__FirstItemAssignment_1 : ( ruleObjectValue ) ;
    public final void rule__Array__FirstItemAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:833:1: ( ( ruleObjectValue ) )
            // InternalSystemDescriptor.g:834:2: ( ruleObjectValue )
            {
            // InternalSystemDescriptor.g:834:2: ( ruleObjectValue )
            // InternalSystemDescriptor.g:835:3: ruleObjectValue
            {
             before(grammarAccess.getArrayAccess().getFirstItemObjectValueParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
            ruleObjectValue();

            state._fsp--;

             after(grammarAccess.getArrayAccess().getFirstItemObjectValueParserRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__FirstItemAssignment_1"


    // $ANTLR start "rule__Array__ItemsAssignment_2_1"
    // InternalSystemDescriptor.g:844:1: rule__Array__ItemsAssignment_2_1 : ( ruleObjectValue ) ;
    public final void rule__Array__ItemsAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:848:1: ( ( ruleObjectValue ) )
            // InternalSystemDescriptor.g:849:2: ( ruleObjectValue )
            {
            // InternalSystemDescriptor.g:849:2: ( ruleObjectValue )
            // InternalSystemDescriptor.g:850:3: ruleObjectValue
            {
             before(grammarAccess.getArrayAccess().getItemsObjectValueParserRuleCall_2_1_0()); 
            pushFollow(FOLLOW_2);
            ruleObjectValue();

            state._fsp--;

             after(grammarAccess.getArrayAccess().getItemsObjectValueParserRuleCall_2_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Array__ItemsAssignment_2_1"


    // $ANTLR start "rule__EmptyObject__IsEmptyAssignment"
    // InternalSystemDescriptor.g:859:1: rule__EmptyObject__IsEmptyAssignment : ( ( '{}' ) ) ;
    public final void rule__EmptyObject__IsEmptyAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:863:1: ( ( ( '{}' ) ) )
            // InternalSystemDescriptor.g:864:2: ( ( '{}' ) )
            {
            // InternalSystemDescriptor.g:864:2: ( ( '{}' ) )
            // InternalSystemDescriptor.g:865:3: ( '{}' )
            {
             before(grammarAccess.getEmptyObjectAccess().getIsEmptyLeftCurlyBracketRightCurlyBracketKeyword_0()); 
            // InternalSystemDescriptor.g:866:3: ( '{}' )
            // InternalSystemDescriptor.g:867:4: '{}'
            {
             before(grammarAccess.getEmptyObjectAccess().getIsEmptyLeftCurlyBracketRightCurlyBracketKeyword_0()); 
            match(input,18,FOLLOW_2); 
             after(grammarAccess.getEmptyObjectAccess().getIsEmptyLeftCurlyBracketRightCurlyBracketKeyword_0()); 

            }

             after(grammarAccess.getEmptyObjectAccess().getIsEmptyLeftCurlyBracketRightCurlyBracketKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__EmptyObject__IsEmptyAssignment"


    // $ANTLR start "rule__EmptyArray__IsEmptyAssignment"
    // InternalSystemDescriptor.g:878:1: rule__EmptyArray__IsEmptyAssignment : ( ( '[]' ) ) ;
    public final void rule__EmptyArray__IsEmptyAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:882:1: ( ( ( '[]' ) ) )
            // InternalSystemDescriptor.g:883:2: ( ( '[]' ) )
            {
            // InternalSystemDescriptor.g:883:2: ( ( '[]' ) )
            // InternalSystemDescriptor.g:884:3: ( '[]' )
            {
             before(grammarAccess.getEmptyArrayAccess().getIsEmptyLeftSquareBracketRightSquareBracketKeyword_0()); 
            // InternalSystemDescriptor.g:885:3: ( '[]' )
            // InternalSystemDescriptor.g:886:4: '[]'
            {
             before(grammarAccess.getEmptyArrayAccess().getIsEmptyLeftSquareBracketRightSquareBracketKeyword_0()); 
            match(input,19,FOLLOW_2); 
             after(grammarAccess.getEmptyArrayAccess().getIsEmptyLeftSquareBracketRightSquareBracketKeyword_0()); 

            }

             after(grammarAccess.getEmptyArrayAccess().getIsEmptyLeftSquareBracketRightSquareBracketKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__EmptyArray__IsEmptyAssignment"


    // $ANTLR start "rule__ObjectValue__ValueAssignment_0"
    // InternalSystemDescriptor.g:897:1: rule__ObjectValue__ValueAssignment_0 : ( RULE_STRING ) ;
    public final void rule__ObjectValue__ValueAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:901:1: ( ( RULE_STRING ) )
            // InternalSystemDescriptor.g:902:2: ( RULE_STRING )
            {
            // InternalSystemDescriptor.g:902:2: ( RULE_STRING )
            // InternalSystemDescriptor.g:903:3: RULE_STRING
            {
             before(grammarAccess.getObjectValueAccess().getValueSTRINGTerminalRuleCall_0_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getObjectValueAccess().getValueSTRINGTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ObjectValue__ValueAssignment_0"


    // $ANTLR start "rule__TerminalObject__ElementAssignment_0"
    // InternalSystemDescriptor.g:912:1: rule__TerminalObject__ElementAssignment_0 : ( RULE_STRING ) ;
    public final void rule__TerminalObject__ElementAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:916:1: ( ( RULE_STRING ) )
            // InternalSystemDescriptor.g:917:2: ( RULE_STRING )
            {
            // InternalSystemDescriptor.g:917:2: ( RULE_STRING )
            // InternalSystemDescriptor.g:918:3: RULE_STRING
            {
             before(grammarAccess.getTerminalObjectAccess().getElementSTRINGTerminalRuleCall_0_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getTerminalObjectAccess().getElementSTRINGTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TerminalObject__ElementAssignment_0"


    // $ANTLR start "rule__TerminalObject__ContentAssignment_2"
    // InternalSystemDescriptor.g:927:1: rule__TerminalObject__ContentAssignment_2 : ( ruleObjectValue ) ;
    public final void rule__TerminalObject__ContentAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:931:1: ( ( ruleObjectValue ) )
            // InternalSystemDescriptor.g:932:2: ( ruleObjectValue )
            {
            // InternalSystemDescriptor.g:932:2: ( ruleObjectValue )
            // InternalSystemDescriptor.g:933:3: ruleObjectValue
            {
             before(grammarAccess.getTerminalObjectAccess().getContentObjectValueParserRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
            ruleObjectValue();

            state._fsp--;

             after(grammarAccess.getTerminalObjectAccess().getContentObjectValueParserRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__TerminalObject__ContentAssignment_2"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000003000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x00000000000C4810L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x000000000000A000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000010000L});

}