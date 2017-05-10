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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'.'", "'package'", "'data'", "'{'", "'}'", "'model'", "','", "'['", "']'", "':'", "'{}'", "'[]'"
    };
    public static final int RULE_STRING=5;
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
    public static final int RULE_ID=4;
    public static final int RULE_WS=9;
    public static final int RULE_ANY_OTHER=10;
    public static final int RULE_INT=6;
    public static final int T__22=22;
    public static final int RULE_ML_COMMENT=7;
    public static final int T__20=20;
    public static final int T__21=21;

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



    // $ANTLR start "entryRuleDescriptor"
    // InternalSystemDescriptor.g:53:1: entryRuleDescriptor : ruleDescriptor EOF ;
    public final void entryRuleDescriptor() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:54:1: ( ruleDescriptor EOF )
            // InternalSystemDescriptor.g:55:1: ruleDescriptor EOF
            {
             before(grammarAccess.getDescriptorRule()); 
            pushFollow(FOLLOW_1);
            ruleDescriptor();

            state._fsp--;

             after(grammarAccess.getDescriptorRule()); 
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
    // $ANTLR end "entryRuleDescriptor"


    // $ANTLR start "ruleDescriptor"
    // InternalSystemDescriptor.g:62:1: ruleDescriptor : ( ( rule__Descriptor__Group__0 ) ) ;
    public final void ruleDescriptor() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:66:2: ( ( ( rule__Descriptor__Group__0 ) ) )
            // InternalSystemDescriptor.g:67:2: ( ( rule__Descriptor__Group__0 ) )
            {
            // InternalSystemDescriptor.g:67:2: ( ( rule__Descriptor__Group__0 ) )
            // InternalSystemDescriptor.g:68:3: ( rule__Descriptor__Group__0 )
            {
             before(grammarAccess.getDescriptorAccess().getGroup()); 
            // InternalSystemDescriptor.g:69:3: ( rule__Descriptor__Group__0 )
            // InternalSystemDescriptor.g:69:4: rule__Descriptor__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Descriptor__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getDescriptorAccess().getGroup()); 

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
    // $ANTLR end "ruleDescriptor"


    // $ANTLR start "entryRuleQualifiedName"
    // InternalSystemDescriptor.g:78:1: entryRuleQualifiedName : ruleQualifiedName EOF ;
    public final void entryRuleQualifiedName() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:79:1: ( ruleQualifiedName EOF )
            // InternalSystemDescriptor.g:80:1: ruleQualifiedName EOF
            {
             before(grammarAccess.getQualifiedNameRule()); 
            pushFollow(FOLLOW_1);
            ruleQualifiedName();

            state._fsp--;

             after(grammarAccess.getQualifiedNameRule()); 
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
    // $ANTLR end "entryRuleQualifiedName"


    // $ANTLR start "ruleQualifiedName"
    // InternalSystemDescriptor.g:87:1: ruleQualifiedName : ( ( rule__QualifiedName__Group__0 ) ) ;
    public final void ruleQualifiedName() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:91:2: ( ( ( rule__QualifiedName__Group__0 ) ) )
            // InternalSystemDescriptor.g:92:2: ( ( rule__QualifiedName__Group__0 ) )
            {
            // InternalSystemDescriptor.g:92:2: ( ( rule__QualifiedName__Group__0 ) )
            // InternalSystemDescriptor.g:93:3: ( rule__QualifiedName__Group__0 )
            {
             before(grammarAccess.getQualifiedNameAccess().getGroup()); 
            // InternalSystemDescriptor.g:94:3: ( rule__QualifiedName__Group__0 )
            // InternalSystemDescriptor.g:94:4: rule__QualifiedName__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__QualifiedName__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getQualifiedNameAccess().getGroup()); 

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
    // $ANTLR end "ruleQualifiedName"


    // $ANTLR start "entryRuleUnqualifiedName"
    // InternalSystemDescriptor.g:103:1: entryRuleUnqualifiedName : ruleUnqualifiedName EOF ;
    public final void entryRuleUnqualifiedName() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:104:1: ( ruleUnqualifiedName EOF )
            // InternalSystemDescriptor.g:105:1: ruleUnqualifiedName EOF
            {
             before(grammarAccess.getUnqualifiedNameRule()); 
            pushFollow(FOLLOW_1);
            ruleUnqualifiedName();

            state._fsp--;

             after(grammarAccess.getUnqualifiedNameRule()); 
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
    // $ANTLR end "entryRuleUnqualifiedName"


    // $ANTLR start "ruleUnqualifiedName"
    // InternalSystemDescriptor.g:112:1: ruleUnqualifiedName : ( RULE_ID ) ;
    public final void ruleUnqualifiedName() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:116:2: ( ( RULE_ID ) )
            // InternalSystemDescriptor.g:117:2: ( RULE_ID )
            {
            // InternalSystemDescriptor.g:117:2: ( RULE_ID )
            // InternalSystemDescriptor.g:118:3: RULE_ID
            {
             before(grammarAccess.getUnqualifiedNameAccess().getIDTerminalRuleCall()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getUnqualifiedNameAccess().getIDTerminalRuleCall()); 

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
    // $ANTLR end "ruleUnqualifiedName"


    // $ANTLR start "entryRulePackage"
    // InternalSystemDescriptor.g:128:1: entryRulePackage : rulePackage EOF ;
    public final void entryRulePackage() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:129:1: ( rulePackage EOF )
            // InternalSystemDescriptor.g:130:1: rulePackage EOF
            {
             before(grammarAccess.getPackageRule()); 
            pushFollow(FOLLOW_1);
            rulePackage();

            state._fsp--;

             after(grammarAccess.getPackageRule()); 
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
    // $ANTLR end "entryRulePackage"


    // $ANTLR start "rulePackage"
    // InternalSystemDescriptor.g:137:1: rulePackage : ( ( rule__Package__Group__0 ) ) ;
    public final void rulePackage() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:141:2: ( ( ( rule__Package__Group__0 ) ) )
            // InternalSystemDescriptor.g:142:2: ( ( rule__Package__Group__0 ) )
            {
            // InternalSystemDescriptor.g:142:2: ( ( rule__Package__Group__0 ) )
            // InternalSystemDescriptor.g:143:3: ( rule__Package__Group__0 )
            {
             before(grammarAccess.getPackageAccess().getGroup()); 
            // InternalSystemDescriptor.g:144:3: ( rule__Package__Group__0 )
            // InternalSystemDescriptor.g:144:4: rule__Package__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Package__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getPackageAccess().getGroup()); 

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
    // $ANTLR end "rulePackage"


    // $ANTLR start "entryRuleData"
    // InternalSystemDescriptor.g:153:1: entryRuleData : ruleData EOF ;
    public final void entryRuleData() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:154:1: ( ruleData EOF )
            // InternalSystemDescriptor.g:155:1: ruleData EOF
            {
             before(grammarAccess.getDataRule()); 
            pushFollow(FOLLOW_1);
            ruleData();

            state._fsp--;

             after(grammarAccess.getDataRule()); 
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
    // $ANTLR end "entryRuleData"


    // $ANTLR start "ruleData"
    // InternalSystemDescriptor.g:162:1: ruleData : ( ( rule__Data__Group__0 ) ) ;
    public final void ruleData() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:166:2: ( ( ( rule__Data__Group__0 ) ) )
            // InternalSystemDescriptor.g:167:2: ( ( rule__Data__Group__0 ) )
            {
            // InternalSystemDescriptor.g:167:2: ( ( rule__Data__Group__0 ) )
            // InternalSystemDescriptor.g:168:3: ( rule__Data__Group__0 )
            {
             before(grammarAccess.getDataAccess().getGroup()); 
            // InternalSystemDescriptor.g:169:3: ( rule__Data__Group__0 )
            // InternalSystemDescriptor.g:169:4: rule__Data__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Data__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getDataAccess().getGroup()); 

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
    // $ANTLR end "ruleData"


    // $ANTLR start "entryRuleModel"
    // InternalSystemDescriptor.g:178:1: entryRuleModel : ruleModel EOF ;
    public final void entryRuleModel() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:179:1: ( ruleModel EOF )
            // InternalSystemDescriptor.g:180:1: ruleModel EOF
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
    // InternalSystemDescriptor.g:187:1: ruleModel : ( ( rule__Model__Group__0 ) ) ;
    public final void ruleModel() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:191:2: ( ( ( rule__Model__Group__0 ) ) )
            // InternalSystemDescriptor.g:192:2: ( ( rule__Model__Group__0 ) )
            {
            // InternalSystemDescriptor.g:192:2: ( ( rule__Model__Group__0 ) )
            // InternalSystemDescriptor.g:193:3: ( rule__Model__Group__0 )
            {
             before(grammarAccess.getModelAccess().getGroup()); 
            // InternalSystemDescriptor.g:194:3: ( rule__Model__Group__0 )
            // InternalSystemDescriptor.g:194:4: rule__Model__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Model__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getModelAccess().getGroup()); 

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


    // $ANTLR start "entryRuleElement"
    // InternalSystemDescriptor.g:203:1: entryRuleElement : ruleElement EOF ;
    public final void entryRuleElement() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:204:1: ( ruleElement EOF )
            // InternalSystemDescriptor.g:205:1: ruleElement EOF
            {
             before(grammarAccess.getElementRule()); 
            pushFollow(FOLLOW_1);
            ruleElement();

            state._fsp--;

             after(grammarAccess.getElementRule()); 
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
    // $ANTLR end "entryRuleElement"


    // $ANTLR start "ruleElement"
    // InternalSystemDescriptor.g:212:1: ruleElement : ( ( rule__Element__Alternatives ) ) ;
    public final void ruleElement() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:216:2: ( ( ( rule__Element__Alternatives ) ) )
            // InternalSystemDescriptor.g:217:2: ( ( rule__Element__Alternatives ) )
            {
            // InternalSystemDescriptor.g:217:2: ( ( rule__Element__Alternatives ) )
            // InternalSystemDescriptor.g:218:3: ( rule__Element__Alternatives )
            {
             before(grammarAccess.getElementAccess().getAlternatives()); 
            // InternalSystemDescriptor.g:219:3: ( rule__Element__Alternatives )
            // InternalSystemDescriptor.g:219:4: rule__Element__Alternatives
            {
            pushFollow(FOLLOW_2);
            rule__Element__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getElementAccess().getAlternatives()); 

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
    // $ANTLR end "ruleElement"


    // $ANTLR start "entryRuleObject"
    // InternalSystemDescriptor.g:228:1: entryRuleObject : ruleObject EOF ;
    public final void entryRuleObject() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:229:1: ( ruleObject EOF )
            // InternalSystemDescriptor.g:230:1: ruleObject EOF
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
    // InternalSystemDescriptor.g:237:1: ruleObject : ( ( rule__Object__Group__0 ) ) ;
    public final void ruleObject() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:241:2: ( ( ( rule__Object__Group__0 ) ) )
            // InternalSystemDescriptor.g:242:2: ( ( rule__Object__Group__0 ) )
            {
            // InternalSystemDescriptor.g:242:2: ( ( rule__Object__Group__0 ) )
            // InternalSystemDescriptor.g:243:3: ( rule__Object__Group__0 )
            {
             before(grammarAccess.getObjectAccess().getGroup()); 
            // InternalSystemDescriptor.g:244:3: ( rule__Object__Group__0 )
            // InternalSystemDescriptor.g:244:4: rule__Object__Group__0
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
    // InternalSystemDescriptor.g:253:1: entryRuleArray : ruleArray EOF ;
    public final void entryRuleArray() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:254:1: ( ruleArray EOF )
            // InternalSystemDescriptor.g:255:1: ruleArray EOF
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
    // InternalSystemDescriptor.g:262:1: ruleArray : ( ( rule__Array__Group__0 ) ) ;
    public final void ruleArray() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:266:2: ( ( ( rule__Array__Group__0 ) ) )
            // InternalSystemDescriptor.g:267:2: ( ( rule__Array__Group__0 ) )
            {
            // InternalSystemDescriptor.g:267:2: ( ( rule__Array__Group__0 ) )
            // InternalSystemDescriptor.g:268:3: ( rule__Array__Group__0 )
            {
             before(grammarAccess.getArrayAccess().getGroup()); 
            // InternalSystemDescriptor.g:269:3: ( rule__Array__Group__0 )
            // InternalSystemDescriptor.g:269:4: rule__Array__Group__0
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
    // InternalSystemDescriptor.g:278:1: entryRuleEmptyObject : ruleEmptyObject EOF ;
    public final void entryRuleEmptyObject() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:279:1: ( ruleEmptyObject EOF )
            // InternalSystemDescriptor.g:280:1: ruleEmptyObject EOF
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
    // InternalSystemDescriptor.g:287:1: ruleEmptyObject : ( ( rule__EmptyObject__IsEmptyAssignment ) ) ;
    public final void ruleEmptyObject() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:291:2: ( ( ( rule__EmptyObject__IsEmptyAssignment ) ) )
            // InternalSystemDescriptor.g:292:2: ( ( rule__EmptyObject__IsEmptyAssignment ) )
            {
            // InternalSystemDescriptor.g:292:2: ( ( rule__EmptyObject__IsEmptyAssignment ) )
            // InternalSystemDescriptor.g:293:3: ( rule__EmptyObject__IsEmptyAssignment )
            {
             before(grammarAccess.getEmptyObjectAccess().getIsEmptyAssignment()); 
            // InternalSystemDescriptor.g:294:3: ( rule__EmptyObject__IsEmptyAssignment )
            // InternalSystemDescriptor.g:294:4: rule__EmptyObject__IsEmptyAssignment
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
    // InternalSystemDescriptor.g:303:1: entryRuleEmptyArray : ruleEmptyArray EOF ;
    public final void entryRuleEmptyArray() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:304:1: ( ruleEmptyArray EOF )
            // InternalSystemDescriptor.g:305:1: ruleEmptyArray EOF
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
    // InternalSystemDescriptor.g:312:1: ruleEmptyArray : ( ( rule__EmptyArray__IsEmptyAssignment ) ) ;
    public final void ruleEmptyArray() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:316:2: ( ( ( rule__EmptyArray__IsEmptyAssignment ) ) )
            // InternalSystemDescriptor.g:317:2: ( ( rule__EmptyArray__IsEmptyAssignment ) )
            {
            // InternalSystemDescriptor.g:317:2: ( ( rule__EmptyArray__IsEmptyAssignment ) )
            // InternalSystemDescriptor.g:318:3: ( rule__EmptyArray__IsEmptyAssignment )
            {
             before(grammarAccess.getEmptyArrayAccess().getIsEmptyAssignment()); 
            // InternalSystemDescriptor.g:319:3: ( rule__EmptyArray__IsEmptyAssignment )
            // InternalSystemDescriptor.g:319:4: rule__EmptyArray__IsEmptyAssignment
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
    // InternalSystemDescriptor.g:328:1: entryRuleObjectValue : ruleObjectValue EOF ;
    public final void entryRuleObjectValue() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:329:1: ( ruleObjectValue EOF )
            // InternalSystemDescriptor.g:330:1: ruleObjectValue EOF
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
    // InternalSystemDescriptor.g:337:1: ruleObjectValue : ( ( rule__ObjectValue__Alternatives ) ) ;
    public final void ruleObjectValue() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:341:2: ( ( ( rule__ObjectValue__Alternatives ) ) )
            // InternalSystemDescriptor.g:342:2: ( ( rule__ObjectValue__Alternatives ) )
            {
            // InternalSystemDescriptor.g:342:2: ( ( rule__ObjectValue__Alternatives ) )
            // InternalSystemDescriptor.g:343:3: ( rule__ObjectValue__Alternatives )
            {
             before(grammarAccess.getObjectValueAccess().getAlternatives()); 
            // InternalSystemDescriptor.g:344:3: ( rule__ObjectValue__Alternatives )
            // InternalSystemDescriptor.g:344:4: rule__ObjectValue__Alternatives
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
    // InternalSystemDescriptor.g:353:1: entryRuleTerminalObject : ruleTerminalObject EOF ;
    public final void entryRuleTerminalObject() throws RecognitionException {
        try {
            // InternalSystemDescriptor.g:354:1: ( ruleTerminalObject EOF )
            // InternalSystemDescriptor.g:355:1: ruleTerminalObject EOF
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
    // InternalSystemDescriptor.g:362:1: ruleTerminalObject : ( ( rule__TerminalObject__Group__0 ) ) ;
    public final void ruleTerminalObject() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:366:2: ( ( ( rule__TerminalObject__Group__0 ) ) )
            // InternalSystemDescriptor.g:367:2: ( ( rule__TerminalObject__Group__0 ) )
            {
            // InternalSystemDescriptor.g:367:2: ( ( rule__TerminalObject__Group__0 ) )
            // InternalSystemDescriptor.g:368:3: ( rule__TerminalObject__Group__0 )
            {
             before(grammarAccess.getTerminalObjectAccess().getGroup()); 
            // InternalSystemDescriptor.g:369:3: ( rule__TerminalObject__Group__0 )
            // InternalSystemDescriptor.g:369:4: rule__TerminalObject__Group__0
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


    // $ANTLR start "rule__Element__Alternatives"
    // InternalSystemDescriptor.g:377:1: rule__Element__Alternatives : ( ( ruleData ) | ( ruleModel ) );
    public final void rule__Element__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:381:1: ( ( ruleData ) | ( ruleModel ) )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==13) ) {
                alt1=1;
            }
            else if ( (LA1_0==16) ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // InternalSystemDescriptor.g:382:2: ( ruleData )
                    {
                    // InternalSystemDescriptor.g:382:2: ( ruleData )
                    // InternalSystemDescriptor.g:383:3: ruleData
                    {
                     before(grammarAccess.getElementAccess().getDataParserRuleCall_0()); 
                    pushFollow(FOLLOW_2);
                    ruleData();

                    state._fsp--;

                     after(grammarAccess.getElementAccess().getDataParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalSystemDescriptor.g:388:2: ( ruleModel )
                    {
                    // InternalSystemDescriptor.g:388:2: ( ruleModel )
                    // InternalSystemDescriptor.g:389:3: ruleModel
                    {
                     before(grammarAccess.getElementAccess().getModelParserRuleCall_1()); 
                    pushFollow(FOLLOW_2);
                    ruleModel();

                    state._fsp--;

                     after(grammarAccess.getElementAccess().getModelParserRuleCall_1()); 

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
    // $ANTLR end "rule__Element__Alternatives"


    // $ANTLR start "rule__ObjectValue__Alternatives"
    // InternalSystemDescriptor.g:398:1: rule__ObjectValue__Alternatives : ( ( ( rule__ObjectValue__ValueAssignment_0 ) ) | ( ruleObject ) | ( ruleArray ) | ( ruleEmptyObject ) | ( ruleEmptyArray ) );
    public final void rule__ObjectValue__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:402:1: ( ( ( rule__ObjectValue__ValueAssignment_0 ) ) | ( ruleObject ) | ( ruleArray ) | ( ruleEmptyObject ) | ( ruleEmptyArray ) )
            int alt2=5;
            switch ( input.LA(1) ) {
            case RULE_STRING:
                {
                alt2=1;
                }
                break;
            case 14:
                {
                alt2=2;
                }
                break;
            case 18:
                {
                alt2=3;
                }
                break;
            case 21:
                {
                alt2=4;
                }
                break;
            case 22:
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
                    // InternalSystemDescriptor.g:403:2: ( ( rule__ObjectValue__ValueAssignment_0 ) )
                    {
                    // InternalSystemDescriptor.g:403:2: ( ( rule__ObjectValue__ValueAssignment_0 ) )
                    // InternalSystemDescriptor.g:404:3: ( rule__ObjectValue__ValueAssignment_0 )
                    {
                     before(grammarAccess.getObjectValueAccess().getValueAssignment_0()); 
                    // InternalSystemDescriptor.g:405:3: ( rule__ObjectValue__ValueAssignment_0 )
                    // InternalSystemDescriptor.g:405:4: rule__ObjectValue__ValueAssignment_0
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
                    // InternalSystemDescriptor.g:409:2: ( ruleObject )
                    {
                    // InternalSystemDescriptor.g:409:2: ( ruleObject )
                    // InternalSystemDescriptor.g:410:3: ruleObject
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
                    // InternalSystemDescriptor.g:415:2: ( ruleArray )
                    {
                    // InternalSystemDescriptor.g:415:2: ( ruleArray )
                    // InternalSystemDescriptor.g:416:3: ruleArray
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
                    // InternalSystemDescriptor.g:421:2: ( ruleEmptyObject )
                    {
                    // InternalSystemDescriptor.g:421:2: ( ruleEmptyObject )
                    // InternalSystemDescriptor.g:422:3: ruleEmptyObject
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
                    // InternalSystemDescriptor.g:427:2: ( ruleEmptyArray )
                    {
                    // InternalSystemDescriptor.g:427:2: ( ruleEmptyArray )
                    // InternalSystemDescriptor.g:428:3: ruleEmptyArray
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


    // $ANTLR start "rule__Descriptor__Group__0"
    // InternalSystemDescriptor.g:437:1: rule__Descriptor__Group__0 : rule__Descriptor__Group__0__Impl rule__Descriptor__Group__1 ;
    public final void rule__Descriptor__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:441:1: ( rule__Descriptor__Group__0__Impl rule__Descriptor__Group__1 )
            // InternalSystemDescriptor.g:442:2: rule__Descriptor__Group__0__Impl rule__Descriptor__Group__1
            {
            pushFollow(FOLLOW_3);
            rule__Descriptor__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Descriptor__Group__1();

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
    // $ANTLR end "rule__Descriptor__Group__0"


    // $ANTLR start "rule__Descriptor__Group__0__Impl"
    // InternalSystemDescriptor.g:449:1: rule__Descriptor__Group__0__Impl : ( ( rule__Descriptor__PackageAssignment_0 ) ) ;
    public final void rule__Descriptor__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:453:1: ( ( ( rule__Descriptor__PackageAssignment_0 ) ) )
            // InternalSystemDescriptor.g:454:1: ( ( rule__Descriptor__PackageAssignment_0 ) )
            {
            // InternalSystemDescriptor.g:454:1: ( ( rule__Descriptor__PackageAssignment_0 ) )
            // InternalSystemDescriptor.g:455:2: ( rule__Descriptor__PackageAssignment_0 )
            {
             before(grammarAccess.getDescriptorAccess().getPackageAssignment_0()); 
            // InternalSystemDescriptor.g:456:2: ( rule__Descriptor__PackageAssignment_0 )
            // InternalSystemDescriptor.g:456:3: rule__Descriptor__PackageAssignment_0
            {
            pushFollow(FOLLOW_2);
            rule__Descriptor__PackageAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getDescriptorAccess().getPackageAssignment_0()); 

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
    // $ANTLR end "rule__Descriptor__Group__0__Impl"


    // $ANTLR start "rule__Descriptor__Group__1"
    // InternalSystemDescriptor.g:464:1: rule__Descriptor__Group__1 : rule__Descriptor__Group__1__Impl ;
    public final void rule__Descriptor__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:468:1: ( rule__Descriptor__Group__1__Impl )
            // InternalSystemDescriptor.g:469:2: rule__Descriptor__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Descriptor__Group__1__Impl();

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
    // $ANTLR end "rule__Descriptor__Group__1"


    // $ANTLR start "rule__Descriptor__Group__1__Impl"
    // InternalSystemDescriptor.g:475:1: rule__Descriptor__Group__1__Impl : ( ( ( rule__Descriptor__ElementsAssignment_1 ) ) ( ( rule__Descriptor__ElementsAssignment_1 )* ) ) ;
    public final void rule__Descriptor__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:479:1: ( ( ( ( rule__Descriptor__ElementsAssignment_1 ) ) ( ( rule__Descriptor__ElementsAssignment_1 )* ) ) )
            // InternalSystemDescriptor.g:480:1: ( ( ( rule__Descriptor__ElementsAssignment_1 ) ) ( ( rule__Descriptor__ElementsAssignment_1 )* ) )
            {
            // InternalSystemDescriptor.g:480:1: ( ( ( rule__Descriptor__ElementsAssignment_1 ) ) ( ( rule__Descriptor__ElementsAssignment_1 )* ) )
            // InternalSystemDescriptor.g:481:2: ( ( rule__Descriptor__ElementsAssignment_1 ) ) ( ( rule__Descriptor__ElementsAssignment_1 )* )
            {
            // InternalSystemDescriptor.g:481:2: ( ( rule__Descriptor__ElementsAssignment_1 ) )
            // InternalSystemDescriptor.g:482:3: ( rule__Descriptor__ElementsAssignment_1 )
            {
             before(grammarAccess.getDescriptorAccess().getElementsAssignment_1()); 
            // InternalSystemDescriptor.g:483:3: ( rule__Descriptor__ElementsAssignment_1 )
            // InternalSystemDescriptor.g:483:4: rule__Descriptor__ElementsAssignment_1
            {
            pushFollow(FOLLOW_4);
            rule__Descriptor__ElementsAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getDescriptorAccess().getElementsAssignment_1()); 

            }

            // InternalSystemDescriptor.g:486:2: ( ( rule__Descriptor__ElementsAssignment_1 )* )
            // InternalSystemDescriptor.g:487:3: ( rule__Descriptor__ElementsAssignment_1 )*
            {
             before(grammarAccess.getDescriptorAccess().getElementsAssignment_1()); 
            // InternalSystemDescriptor.g:488:3: ( rule__Descriptor__ElementsAssignment_1 )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==13||LA3_0==16) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalSystemDescriptor.g:488:4: rule__Descriptor__ElementsAssignment_1
            	    {
            	    pushFollow(FOLLOW_4);
            	    rule__Descriptor__ElementsAssignment_1();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

             after(grammarAccess.getDescriptorAccess().getElementsAssignment_1()); 

            }


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
    // $ANTLR end "rule__Descriptor__Group__1__Impl"


    // $ANTLR start "rule__QualifiedName__Group__0"
    // InternalSystemDescriptor.g:498:1: rule__QualifiedName__Group__0 : rule__QualifiedName__Group__0__Impl rule__QualifiedName__Group__1 ;
    public final void rule__QualifiedName__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:502:1: ( rule__QualifiedName__Group__0__Impl rule__QualifiedName__Group__1 )
            // InternalSystemDescriptor.g:503:2: rule__QualifiedName__Group__0__Impl rule__QualifiedName__Group__1
            {
            pushFollow(FOLLOW_5);
            rule__QualifiedName__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__QualifiedName__Group__1();

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
    // $ANTLR end "rule__QualifiedName__Group__0"


    // $ANTLR start "rule__QualifiedName__Group__0__Impl"
    // InternalSystemDescriptor.g:510:1: rule__QualifiedName__Group__0__Impl : ( RULE_ID ) ;
    public final void rule__QualifiedName__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:514:1: ( ( RULE_ID ) )
            // InternalSystemDescriptor.g:515:1: ( RULE_ID )
            {
            // InternalSystemDescriptor.g:515:1: ( RULE_ID )
            // InternalSystemDescriptor.g:516:2: RULE_ID
            {
             before(grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_0()); 

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
    // $ANTLR end "rule__QualifiedName__Group__0__Impl"


    // $ANTLR start "rule__QualifiedName__Group__1"
    // InternalSystemDescriptor.g:525:1: rule__QualifiedName__Group__1 : rule__QualifiedName__Group__1__Impl ;
    public final void rule__QualifiedName__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:529:1: ( rule__QualifiedName__Group__1__Impl )
            // InternalSystemDescriptor.g:530:2: rule__QualifiedName__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__QualifiedName__Group__1__Impl();

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
    // $ANTLR end "rule__QualifiedName__Group__1"


    // $ANTLR start "rule__QualifiedName__Group__1__Impl"
    // InternalSystemDescriptor.g:536:1: rule__QualifiedName__Group__1__Impl : ( ( rule__QualifiedName__Group_1__0 )* ) ;
    public final void rule__QualifiedName__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:540:1: ( ( ( rule__QualifiedName__Group_1__0 )* ) )
            // InternalSystemDescriptor.g:541:1: ( ( rule__QualifiedName__Group_1__0 )* )
            {
            // InternalSystemDescriptor.g:541:1: ( ( rule__QualifiedName__Group_1__0 )* )
            // InternalSystemDescriptor.g:542:2: ( rule__QualifiedName__Group_1__0 )*
            {
             before(grammarAccess.getQualifiedNameAccess().getGroup_1()); 
            // InternalSystemDescriptor.g:543:2: ( rule__QualifiedName__Group_1__0 )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==11) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalSystemDescriptor.g:543:3: rule__QualifiedName__Group_1__0
            	    {
            	    pushFollow(FOLLOW_6);
            	    rule__QualifiedName__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

             after(grammarAccess.getQualifiedNameAccess().getGroup_1()); 

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
    // $ANTLR end "rule__QualifiedName__Group__1__Impl"


    // $ANTLR start "rule__QualifiedName__Group_1__0"
    // InternalSystemDescriptor.g:552:1: rule__QualifiedName__Group_1__0 : rule__QualifiedName__Group_1__0__Impl rule__QualifiedName__Group_1__1 ;
    public final void rule__QualifiedName__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:556:1: ( rule__QualifiedName__Group_1__0__Impl rule__QualifiedName__Group_1__1 )
            // InternalSystemDescriptor.g:557:2: rule__QualifiedName__Group_1__0__Impl rule__QualifiedName__Group_1__1
            {
            pushFollow(FOLLOW_7);
            rule__QualifiedName__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__QualifiedName__Group_1__1();

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
    // $ANTLR end "rule__QualifiedName__Group_1__0"


    // $ANTLR start "rule__QualifiedName__Group_1__0__Impl"
    // InternalSystemDescriptor.g:564:1: rule__QualifiedName__Group_1__0__Impl : ( '.' ) ;
    public final void rule__QualifiedName__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:568:1: ( ( '.' ) )
            // InternalSystemDescriptor.g:569:1: ( '.' )
            {
            // InternalSystemDescriptor.g:569:1: ( '.' )
            // InternalSystemDescriptor.g:570:2: '.'
            {
             before(grammarAccess.getQualifiedNameAccess().getFullStopKeyword_1_0()); 
            match(input,11,FOLLOW_2); 
             after(grammarAccess.getQualifiedNameAccess().getFullStopKeyword_1_0()); 

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
    // $ANTLR end "rule__QualifiedName__Group_1__0__Impl"


    // $ANTLR start "rule__QualifiedName__Group_1__1"
    // InternalSystemDescriptor.g:579:1: rule__QualifiedName__Group_1__1 : rule__QualifiedName__Group_1__1__Impl ;
    public final void rule__QualifiedName__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:583:1: ( rule__QualifiedName__Group_1__1__Impl )
            // InternalSystemDescriptor.g:584:2: rule__QualifiedName__Group_1__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__QualifiedName__Group_1__1__Impl();

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
    // $ANTLR end "rule__QualifiedName__Group_1__1"


    // $ANTLR start "rule__QualifiedName__Group_1__1__Impl"
    // InternalSystemDescriptor.g:590:1: rule__QualifiedName__Group_1__1__Impl : ( RULE_ID ) ;
    public final void rule__QualifiedName__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:594:1: ( ( RULE_ID ) )
            // InternalSystemDescriptor.g:595:1: ( RULE_ID )
            {
            // InternalSystemDescriptor.g:595:1: ( RULE_ID )
            // InternalSystemDescriptor.g:596:2: RULE_ID
            {
             before(grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_1_1()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_1_1()); 

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
    // $ANTLR end "rule__QualifiedName__Group_1__1__Impl"


    // $ANTLR start "rule__Package__Group__0"
    // InternalSystemDescriptor.g:606:1: rule__Package__Group__0 : rule__Package__Group__0__Impl rule__Package__Group__1 ;
    public final void rule__Package__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:610:1: ( rule__Package__Group__0__Impl rule__Package__Group__1 )
            // InternalSystemDescriptor.g:611:2: rule__Package__Group__0__Impl rule__Package__Group__1
            {
            pushFollow(FOLLOW_7);
            rule__Package__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Package__Group__1();

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
    // $ANTLR end "rule__Package__Group__0"


    // $ANTLR start "rule__Package__Group__0__Impl"
    // InternalSystemDescriptor.g:618:1: rule__Package__Group__0__Impl : ( 'package' ) ;
    public final void rule__Package__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:622:1: ( ( 'package' ) )
            // InternalSystemDescriptor.g:623:1: ( 'package' )
            {
            // InternalSystemDescriptor.g:623:1: ( 'package' )
            // InternalSystemDescriptor.g:624:2: 'package'
            {
             before(grammarAccess.getPackageAccess().getPackageKeyword_0()); 
            match(input,12,FOLLOW_2); 
             after(grammarAccess.getPackageAccess().getPackageKeyword_0()); 

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
    // $ANTLR end "rule__Package__Group__0__Impl"


    // $ANTLR start "rule__Package__Group__1"
    // InternalSystemDescriptor.g:633:1: rule__Package__Group__1 : rule__Package__Group__1__Impl ;
    public final void rule__Package__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:637:1: ( rule__Package__Group__1__Impl )
            // InternalSystemDescriptor.g:638:2: rule__Package__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Package__Group__1__Impl();

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
    // $ANTLR end "rule__Package__Group__1"


    // $ANTLR start "rule__Package__Group__1__Impl"
    // InternalSystemDescriptor.g:644:1: rule__Package__Group__1__Impl : ( ( rule__Package__NameAssignment_1 ) ) ;
    public final void rule__Package__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:648:1: ( ( ( rule__Package__NameAssignment_1 ) ) )
            // InternalSystemDescriptor.g:649:1: ( ( rule__Package__NameAssignment_1 ) )
            {
            // InternalSystemDescriptor.g:649:1: ( ( rule__Package__NameAssignment_1 ) )
            // InternalSystemDescriptor.g:650:2: ( rule__Package__NameAssignment_1 )
            {
             before(grammarAccess.getPackageAccess().getNameAssignment_1()); 
            // InternalSystemDescriptor.g:651:2: ( rule__Package__NameAssignment_1 )
            // InternalSystemDescriptor.g:651:3: rule__Package__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Package__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getPackageAccess().getNameAssignment_1()); 

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
    // $ANTLR end "rule__Package__Group__1__Impl"


    // $ANTLR start "rule__Data__Group__0"
    // InternalSystemDescriptor.g:660:1: rule__Data__Group__0 : rule__Data__Group__0__Impl rule__Data__Group__1 ;
    public final void rule__Data__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:664:1: ( rule__Data__Group__0__Impl rule__Data__Group__1 )
            // InternalSystemDescriptor.g:665:2: rule__Data__Group__0__Impl rule__Data__Group__1
            {
            pushFollow(FOLLOW_7);
            rule__Data__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Data__Group__1();

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
    // $ANTLR end "rule__Data__Group__0"


    // $ANTLR start "rule__Data__Group__0__Impl"
    // InternalSystemDescriptor.g:672:1: rule__Data__Group__0__Impl : ( 'data' ) ;
    public final void rule__Data__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:676:1: ( ( 'data' ) )
            // InternalSystemDescriptor.g:677:1: ( 'data' )
            {
            // InternalSystemDescriptor.g:677:1: ( 'data' )
            // InternalSystemDescriptor.g:678:2: 'data'
            {
             before(grammarAccess.getDataAccess().getDataKeyword_0()); 
            match(input,13,FOLLOW_2); 
             after(grammarAccess.getDataAccess().getDataKeyword_0()); 

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
    // $ANTLR end "rule__Data__Group__0__Impl"


    // $ANTLR start "rule__Data__Group__1"
    // InternalSystemDescriptor.g:687:1: rule__Data__Group__1 : rule__Data__Group__1__Impl rule__Data__Group__2 ;
    public final void rule__Data__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:691:1: ( rule__Data__Group__1__Impl rule__Data__Group__2 )
            // InternalSystemDescriptor.g:692:2: rule__Data__Group__1__Impl rule__Data__Group__2
            {
            pushFollow(FOLLOW_8);
            rule__Data__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Data__Group__2();

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
    // $ANTLR end "rule__Data__Group__1"


    // $ANTLR start "rule__Data__Group__1__Impl"
    // InternalSystemDescriptor.g:699:1: rule__Data__Group__1__Impl : ( ( rule__Data__NameAssignment_1 ) ) ;
    public final void rule__Data__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:703:1: ( ( ( rule__Data__NameAssignment_1 ) ) )
            // InternalSystemDescriptor.g:704:1: ( ( rule__Data__NameAssignment_1 ) )
            {
            // InternalSystemDescriptor.g:704:1: ( ( rule__Data__NameAssignment_1 ) )
            // InternalSystemDescriptor.g:705:2: ( rule__Data__NameAssignment_1 )
            {
             before(grammarAccess.getDataAccess().getNameAssignment_1()); 
            // InternalSystemDescriptor.g:706:2: ( rule__Data__NameAssignment_1 )
            // InternalSystemDescriptor.g:706:3: rule__Data__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Data__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getDataAccess().getNameAssignment_1()); 

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
    // $ANTLR end "rule__Data__Group__1__Impl"


    // $ANTLR start "rule__Data__Group__2"
    // InternalSystemDescriptor.g:714:1: rule__Data__Group__2 : rule__Data__Group__2__Impl rule__Data__Group__3 ;
    public final void rule__Data__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:718:1: ( rule__Data__Group__2__Impl rule__Data__Group__3 )
            // InternalSystemDescriptor.g:719:2: rule__Data__Group__2__Impl rule__Data__Group__3
            {
            pushFollow(FOLLOW_9);
            rule__Data__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Data__Group__3();

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
    // $ANTLR end "rule__Data__Group__2"


    // $ANTLR start "rule__Data__Group__2__Impl"
    // InternalSystemDescriptor.g:726:1: rule__Data__Group__2__Impl : ( '{' ) ;
    public final void rule__Data__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:730:1: ( ( '{' ) )
            // InternalSystemDescriptor.g:731:1: ( '{' )
            {
            // InternalSystemDescriptor.g:731:1: ( '{' )
            // InternalSystemDescriptor.g:732:2: '{'
            {
             before(grammarAccess.getDataAccess().getLeftCurlyBracketKeyword_2()); 
            match(input,14,FOLLOW_2); 
             after(grammarAccess.getDataAccess().getLeftCurlyBracketKeyword_2()); 

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
    // $ANTLR end "rule__Data__Group__2__Impl"


    // $ANTLR start "rule__Data__Group__3"
    // InternalSystemDescriptor.g:741:1: rule__Data__Group__3 : rule__Data__Group__3__Impl ;
    public final void rule__Data__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:745:1: ( rule__Data__Group__3__Impl )
            // InternalSystemDescriptor.g:746:2: rule__Data__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Data__Group__3__Impl();

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
    // $ANTLR end "rule__Data__Group__3"


    // $ANTLR start "rule__Data__Group__3__Impl"
    // InternalSystemDescriptor.g:752:1: rule__Data__Group__3__Impl : ( '}' ) ;
    public final void rule__Data__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:756:1: ( ( '}' ) )
            // InternalSystemDescriptor.g:757:1: ( '}' )
            {
            // InternalSystemDescriptor.g:757:1: ( '}' )
            // InternalSystemDescriptor.g:758:2: '}'
            {
             before(grammarAccess.getDataAccess().getRightCurlyBracketKeyword_3()); 
            match(input,15,FOLLOW_2); 
             after(grammarAccess.getDataAccess().getRightCurlyBracketKeyword_3()); 

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
    // $ANTLR end "rule__Data__Group__3__Impl"


    // $ANTLR start "rule__Model__Group__0"
    // InternalSystemDescriptor.g:768:1: rule__Model__Group__0 : rule__Model__Group__0__Impl rule__Model__Group__1 ;
    public final void rule__Model__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:772:1: ( rule__Model__Group__0__Impl rule__Model__Group__1 )
            // InternalSystemDescriptor.g:773:2: rule__Model__Group__0__Impl rule__Model__Group__1
            {
            pushFollow(FOLLOW_7);
            rule__Model__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Model__Group__1();

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
    // $ANTLR end "rule__Model__Group__0"


    // $ANTLR start "rule__Model__Group__0__Impl"
    // InternalSystemDescriptor.g:780:1: rule__Model__Group__0__Impl : ( 'model' ) ;
    public final void rule__Model__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:784:1: ( ( 'model' ) )
            // InternalSystemDescriptor.g:785:1: ( 'model' )
            {
            // InternalSystemDescriptor.g:785:1: ( 'model' )
            // InternalSystemDescriptor.g:786:2: 'model'
            {
             before(grammarAccess.getModelAccess().getModelKeyword_0()); 
            match(input,16,FOLLOW_2); 
             after(grammarAccess.getModelAccess().getModelKeyword_0()); 

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
    // $ANTLR end "rule__Model__Group__0__Impl"


    // $ANTLR start "rule__Model__Group__1"
    // InternalSystemDescriptor.g:795:1: rule__Model__Group__1 : rule__Model__Group__1__Impl rule__Model__Group__2 ;
    public final void rule__Model__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:799:1: ( rule__Model__Group__1__Impl rule__Model__Group__2 )
            // InternalSystemDescriptor.g:800:2: rule__Model__Group__1__Impl rule__Model__Group__2
            {
            pushFollow(FOLLOW_8);
            rule__Model__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Model__Group__2();

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
    // $ANTLR end "rule__Model__Group__1"


    // $ANTLR start "rule__Model__Group__1__Impl"
    // InternalSystemDescriptor.g:807:1: rule__Model__Group__1__Impl : ( ( rule__Model__NameAssignment_1 ) ) ;
    public final void rule__Model__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:811:1: ( ( ( rule__Model__NameAssignment_1 ) ) )
            // InternalSystemDescriptor.g:812:1: ( ( rule__Model__NameAssignment_1 ) )
            {
            // InternalSystemDescriptor.g:812:1: ( ( rule__Model__NameAssignment_1 ) )
            // InternalSystemDescriptor.g:813:2: ( rule__Model__NameAssignment_1 )
            {
             before(grammarAccess.getModelAccess().getNameAssignment_1()); 
            // InternalSystemDescriptor.g:814:2: ( rule__Model__NameAssignment_1 )
            // InternalSystemDescriptor.g:814:3: rule__Model__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Model__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getModelAccess().getNameAssignment_1()); 

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
    // $ANTLR end "rule__Model__Group__1__Impl"


    // $ANTLR start "rule__Model__Group__2"
    // InternalSystemDescriptor.g:822:1: rule__Model__Group__2 : rule__Model__Group__2__Impl rule__Model__Group__3 ;
    public final void rule__Model__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:826:1: ( rule__Model__Group__2__Impl rule__Model__Group__3 )
            // InternalSystemDescriptor.g:827:2: rule__Model__Group__2__Impl rule__Model__Group__3
            {
            pushFollow(FOLLOW_9);
            rule__Model__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Model__Group__3();

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
    // $ANTLR end "rule__Model__Group__2"


    // $ANTLR start "rule__Model__Group__2__Impl"
    // InternalSystemDescriptor.g:834:1: rule__Model__Group__2__Impl : ( '{' ) ;
    public final void rule__Model__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:838:1: ( ( '{' ) )
            // InternalSystemDescriptor.g:839:1: ( '{' )
            {
            // InternalSystemDescriptor.g:839:1: ( '{' )
            // InternalSystemDescriptor.g:840:2: '{'
            {
             before(grammarAccess.getModelAccess().getLeftCurlyBracketKeyword_2()); 
            match(input,14,FOLLOW_2); 
             after(grammarAccess.getModelAccess().getLeftCurlyBracketKeyword_2()); 

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
    // $ANTLR end "rule__Model__Group__2__Impl"


    // $ANTLR start "rule__Model__Group__3"
    // InternalSystemDescriptor.g:849:1: rule__Model__Group__3 : rule__Model__Group__3__Impl ;
    public final void rule__Model__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:853:1: ( rule__Model__Group__3__Impl )
            // InternalSystemDescriptor.g:854:2: rule__Model__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Model__Group__3__Impl();

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
    // $ANTLR end "rule__Model__Group__3"


    // $ANTLR start "rule__Model__Group__3__Impl"
    // InternalSystemDescriptor.g:860:1: rule__Model__Group__3__Impl : ( '}' ) ;
    public final void rule__Model__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:864:1: ( ( '}' ) )
            // InternalSystemDescriptor.g:865:1: ( '}' )
            {
            // InternalSystemDescriptor.g:865:1: ( '}' )
            // InternalSystemDescriptor.g:866:2: '}'
            {
             before(grammarAccess.getModelAccess().getRightCurlyBracketKeyword_3()); 
            match(input,15,FOLLOW_2); 
             after(grammarAccess.getModelAccess().getRightCurlyBracketKeyword_3()); 

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
    // $ANTLR end "rule__Model__Group__3__Impl"


    // $ANTLR start "rule__Object__Group__0"
    // InternalSystemDescriptor.g:876:1: rule__Object__Group__0 : rule__Object__Group__0__Impl rule__Object__Group__1 ;
    public final void rule__Object__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:880:1: ( rule__Object__Group__0__Impl rule__Object__Group__1 )
            // InternalSystemDescriptor.g:881:2: rule__Object__Group__0__Impl rule__Object__Group__1
            {
            pushFollow(FOLLOW_10);
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
    // InternalSystemDescriptor.g:888:1: rule__Object__Group__0__Impl : ( '{' ) ;
    public final void rule__Object__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:892:1: ( ( '{' ) )
            // InternalSystemDescriptor.g:893:1: ( '{' )
            {
            // InternalSystemDescriptor.g:893:1: ( '{' )
            // InternalSystemDescriptor.g:894:2: '{'
            {
             before(grammarAccess.getObjectAccess().getLeftCurlyBracketKeyword_0()); 
            match(input,14,FOLLOW_2); 
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
    // InternalSystemDescriptor.g:903:1: rule__Object__Group__1 : rule__Object__Group__1__Impl rule__Object__Group__2 ;
    public final void rule__Object__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:907:1: ( rule__Object__Group__1__Impl rule__Object__Group__2 )
            // InternalSystemDescriptor.g:908:2: rule__Object__Group__1__Impl rule__Object__Group__2
            {
            pushFollow(FOLLOW_11);
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
    // InternalSystemDescriptor.g:915:1: rule__Object__Group__1__Impl : ( ( rule__Object__FirstObjectAssignment_1 ) ) ;
    public final void rule__Object__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:919:1: ( ( ( rule__Object__FirstObjectAssignment_1 ) ) )
            // InternalSystemDescriptor.g:920:1: ( ( rule__Object__FirstObjectAssignment_1 ) )
            {
            // InternalSystemDescriptor.g:920:1: ( ( rule__Object__FirstObjectAssignment_1 ) )
            // InternalSystemDescriptor.g:921:2: ( rule__Object__FirstObjectAssignment_1 )
            {
             before(grammarAccess.getObjectAccess().getFirstObjectAssignment_1()); 
            // InternalSystemDescriptor.g:922:2: ( rule__Object__FirstObjectAssignment_1 )
            // InternalSystemDescriptor.g:922:3: rule__Object__FirstObjectAssignment_1
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
    // InternalSystemDescriptor.g:930:1: rule__Object__Group__2 : rule__Object__Group__2__Impl rule__Object__Group__3 ;
    public final void rule__Object__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:934:1: ( rule__Object__Group__2__Impl rule__Object__Group__3 )
            // InternalSystemDescriptor.g:935:2: rule__Object__Group__2__Impl rule__Object__Group__3
            {
            pushFollow(FOLLOW_11);
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
    // InternalSystemDescriptor.g:942:1: rule__Object__Group__2__Impl : ( ( rule__Object__Group_2__0 )* ) ;
    public final void rule__Object__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:946:1: ( ( ( rule__Object__Group_2__0 )* ) )
            // InternalSystemDescriptor.g:947:1: ( ( rule__Object__Group_2__0 )* )
            {
            // InternalSystemDescriptor.g:947:1: ( ( rule__Object__Group_2__0 )* )
            // InternalSystemDescriptor.g:948:2: ( rule__Object__Group_2__0 )*
            {
             before(grammarAccess.getObjectAccess().getGroup_2()); 
            // InternalSystemDescriptor.g:949:2: ( rule__Object__Group_2__0 )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==17) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalSystemDescriptor.g:949:3: rule__Object__Group_2__0
            	    {
            	    pushFollow(FOLLOW_12);
            	    rule__Object__Group_2__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop5;
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
    // InternalSystemDescriptor.g:957:1: rule__Object__Group__3 : rule__Object__Group__3__Impl ;
    public final void rule__Object__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:961:1: ( rule__Object__Group__3__Impl )
            // InternalSystemDescriptor.g:962:2: rule__Object__Group__3__Impl
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
    // InternalSystemDescriptor.g:968:1: rule__Object__Group__3__Impl : ( '}' ) ;
    public final void rule__Object__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:972:1: ( ( '}' ) )
            // InternalSystemDescriptor.g:973:1: ( '}' )
            {
            // InternalSystemDescriptor.g:973:1: ( '}' )
            // InternalSystemDescriptor.g:974:2: '}'
            {
             before(grammarAccess.getObjectAccess().getRightCurlyBracketKeyword_3()); 
            match(input,15,FOLLOW_2); 
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
    // InternalSystemDescriptor.g:984:1: rule__Object__Group_2__0 : rule__Object__Group_2__0__Impl rule__Object__Group_2__1 ;
    public final void rule__Object__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:988:1: ( rule__Object__Group_2__0__Impl rule__Object__Group_2__1 )
            // InternalSystemDescriptor.g:989:2: rule__Object__Group_2__0__Impl rule__Object__Group_2__1
            {
            pushFollow(FOLLOW_10);
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
    // InternalSystemDescriptor.g:996:1: rule__Object__Group_2__0__Impl : ( ',' ) ;
    public final void rule__Object__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1000:1: ( ( ',' ) )
            // InternalSystemDescriptor.g:1001:1: ( ',' )
            {
            // InternalSystemDescriptor.g:1001:1: ( ',' )
            // InternalSystemDescriptor.g:1002:2: ','
            {
             before(grammarAccess.getObjectAccess().getCommaKeyword_2_0()); 
            match(input,17,FOLLOW_2); 
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
    // InternalSystemDescriptor.g:1011:1: rule__Object__Group_2__1 : rule__Object__Group_2__1__Impl ;
    public final void rule__Object__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1015:1: ( rule__Object__Group_2__1__Impl )
            // InternalSystemDescriptor.g:1016:2: rule__Object__Group_2__1__Impl
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
    // InternalSystemDescriptor.g:1022:1: rule__Object__Group_2__1__Impl : ( ( rule__Object__ObjectsAssignment_2_1 ) ) ;
    public final void rule__Object__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1026:1: ( ( ( rule__Object__ObjectsAssignment_2_1 ) ) )
            // InternalSystemDescriptor.g:1027:1: ( ( rule__Object__ObjectsAssignment_2_1 ) )
            {
            // InternalSystemDescriptor.g:1027:1: ( ( rule__Object__ObjectsAssignment_2_1 ) )
            // InternalSystemDescriptor.g:1028:2: ( rule__Object__ObjectsAssignment_2_1 )
            {
             before(grammarAccess.getObjectAccess().getObjectsAssignment_2_1()); 
            // InternalSystemDescriptor.g:1029:2: ( rule__Object__ObjectsAssignment_2_1 )
            // InternalSystemDescriptor.g:1029:3: rule__Object__ObjectsAssignment_2_1
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
    // InternalSystemDescriptor.g:1038:1: rule__Array__Group__0 : rule__Array__Group__0__Impl rule__Array__Group__1 ;
    public final void rule__Array__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1042:1: ( rule__Array__Group__0__Impl rule__Array__Group__1 )
            // InternalSystemDescriptor.g:1043:2: rule__Array__Group__0__Impl rule__Array__Group__1
            {
            pushFollow(FOLLOW_13);
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
    // InternalSystemDescriptor.g:1050:1: rule__Array__Group__0__Impl : ( '[' ) ;
    public final void rule__Array__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1054:1: ( ( '[' ) )
            // InternalSystemDescriptor.g:1055:1: ( '[' )
            {
            // InternalSystemDescriptor.g:1055:1: ( '[' )
            // InternalSystemDescriptor.g:1056:2: '['
            {
             before(grammarAccess.getArrayAccess().getLeftSquareBracketKeyword_0()); 
            match(input,18,FOLLOW_2); 
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
    // InternalSystemDescriptor.g:1065:1: rule__Array__Group__1 : rule__Array__Group__1__Impl rule__Array__Group__2 ;
    public final void rule__Array__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1069:1: ( rule__Array__Group__1__Impl rule__Array__Group__2 )
            // InternalSystemDescriptor.g:1070:2: rule__Array__Group__1__Impl rule__Array__Group__2
            {
            pushFollow(FOLLOW_14);
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
    // InternalSystemDescriptor.g:1077:1: rule__Array__Group__1__Impl : ( ( rule__Array__FirstItemAssignment_1 ) ) ;
    public final void rule__Array__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1081:1: ( ( ( rule__Array__FirstItemAssignment_1 ) ) )
            // InternalSystemDescriptor.g:1082:1: ( ( rule__Array__FirstItemAssignment_1 ) )
            {
            // InternalSystemDescriptor.g:1082:1: ( ( rule__Array__FirstItemAssignment_1 ) )
            // InternalSystemDescriptor.g:1083:2: ( rule__Array__FirstItemAssignment_1 )
            {
             before(grammarAccess.getArrayAccess().getFirstItemAssignment_1()); 
            // InternalSystemDescriptor.g:1084:2: ( rule__Array__FirstItemAssignment_1 )
            // InternalSystemDescriptor.g:1084:3: rule__Array__FirstItemAssignment_1
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
    // InternalSystemDescriptor.g:1092:1: rule__Array__Group__2 : rule__Array__Group__2__Impl rule__Array__Group__3 ;
    public final void rule__Array__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1096:1: ( rule__Array__Group__2__Impl rule__Array__Group__3 )
            // InternalSystemDescriptor.g:1097:2: rule__Array__Group__2__Impl rule__Array__Group__3
            {
            pushFollow(FOLLOW_14);
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
    // InternalSystemDescriptor.g:1104:1: rule__Array__Group__2__Impl : ( ( rule__Array__Group_2__0 )* ) ;
    public final void rule__Array__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1108:1: ( ( ( rule__Array__Group_2__0 )* ) )
            // InternalSystemDescriptor.g:1109:1: ( ( rule__Array__Group_2__0 )* )
            {
            // InternalSystemDescriptor.g:1109:1: ( ( rule__Array__Group_2__0 )* )
            // InternalSystemDescriptor.g:1110:2: ( rule__Array__Group_2__0 )*
            {
             before(grammarAccess.getArrayAccess().getGroup_2()); 
            // InternalSystemDescriptor.g:1111:2: ( rule__Array__Group_2__0 )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==17) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // InternalSystemDescriptor.g:1111:3: rule__Array__Group_2__0
            	    {
            	    pushFollow(FOLLOW_12);
            	    rule__Array__Group_2__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop6;
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
    // InternalSystemDescriptor.g:1119:1: rule__Array__Group__3 : rule__Array__Group__3__Impl ;
    public final void rule__Array__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1123:1: ( rule__Array__Group__3__Impl )
            // InternalSystemDescriptor.g:1124:2: rule__Array__Group__3__Impl
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
    // InternalSystemDescriptor.g:1130:1: rule__Array__Group__3__Impl : ( ']' ) ;
    public final void rule__Array__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1134:1: ( ( ']' ) )
            // InternalSystemDescriptor.g:1135:1: ( ']' )
            {
            // InternalSystemDescriptor.g:1135:1: ( ']' )
            // InternalSystemDescriptor.g:1136:2: ']'
            {
             before(grammarAccess.getArrayAccess().getRightSquareBracketKeyword_3()); 
            match(input,19,FOLLOW_2); 
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
    // InternalSystemDescriptor.g:1146:1: rule__Array__Group_2__0 : rule__Array__Group_2__0__Impl rule__Array__Group_2__1 ;
    public final void rule__Array__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1150:1: ( rule__Array__Group_2__0__Impl rule__Array__Group_2__1 )
            // InternalSystemDescriptor.g:1151:2: rule__Array__Group_2__0__Impl rule__Array__Group_2__1
            {
            pushFollow(FOLLOW_13);
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
    // InternalSystemDescriptor.g:1158:1: rule__Array__Group_2__0__Impl : ( ',' ) ;
    public final void rule__Array__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1162:1: ( ( ',' ) )
            // InternalSystemDescriptor.g:1163:1: ( ',' )
            {
            // InternalSystemDescriptor.g:1163:1: ( ',' )
            // InternalSystemDescriptor.g:1164:2: ','
            {
             before(grammarAccess.getArrayAccess().getCommaKeyword_2_0()); 
            match(input,17,FOLLOW_2); 
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
    // InternalSystemDescriptor.g:1173:1: rule__Array__Group_2__1 : rule__Array__Group_2__1__Impl ;
    public final void rule__Array__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1177:1: ( rule__Array__Group_2__1__Impl )
            // InternalSystemDescriptor.g:1178:2: rule__Array__Group_2__1__Impl
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
    // InternalSystemDescriptor.g:1184:1: rule__Array__Group_2__1__Impl : ( ( rule__Array__ItemsAssignment_2_1 ) ) ;
    public final void rule__Array__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1188:1: ( ( ( rule__Array__ItemsAssignment_2_1 ) ) )
            // InternalSystemDescriptor.g:1189:1: ( ( rule__Array__ItemsAssignment_2_1 ) )
            {
            // InternalSystemDescriptor.g:1189:1: ( ( rule__Array__ItemsAssignment_2_1 ) )
            // InternalSystemDescriptor.g:1190:2: ( rule__Array__ItemsAssignment_2_1 )
            {
             before(grammarAccess.getArrayAccess().getItemsAssignment_2_1()); 
            // InternalSystemDescriptor.g:1191:2: ( rule__Array__ItemsAssignment_2_1 )
            // InternalSystemDescriptor.g:1191:3: rule__Array__ItemsAssignment_2_1
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
    // InternalSystemDescriptor.g:1200:1: rule__TerminalObject__Group__0 : rule__TerminalObject__Group__0__Impl rule__TerminalObject__Group__1 ;
    public final void rule__TerminalObject__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1204:1: ( rule__TerminalObject__Group__0__Impl rule__TerminalObject__Group__1 )
            // InternalSystemDescriptor.g:1205:2: rule__TerminalObject__Group__0__Impl rule__TerminalObject__Group__1
            {
            pushFollow(FOLLOW_15);
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
    // InternalSystemDescriptor.g:1212:1: rule__TerminalObject__Group__0__Impl : ( ( rule__TerminalObject__ElementAssignment_0 ) ) ;
    public final void rule__TerminalObject__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1216:1: ( ( ( rule__TerminalObject__ElementAssignment_0 ) ) )
            // InternalSystemDescriptor.g:1217:1: ( ( rule__TerminalObject__ElementAssignment_0 ) )
            {
            // InternalSystemDescriptor.g:1217:1: ( ( rule__TerminalObject__ElementAssignment_0 ) )
            // InternalSystemDescriptor.g:1218:2: ( rule__TerminalObject__ElementAssignment_0 )
            {
             before(grammarAccess.getTerminalObjectAccess().getElementAssignment_0()); 
            // InternalSystemDescriptor.g:1219:2: ( rule__TerminalObject__ElementAssignment_0 )
            // InternalSystemDescriptor.g:1219:3: rule__TerminalObject__ElementAssignment_0
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
    // InternalSystemDescriptor.g:1227:1: rule__TerminalObject__Group__1 : rule__TerminalObject__Group__1__Impl rule__TerminalObject__Group__2 ;
    public final void rule__TerminalObject__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1231:1: ( rule__TerminalObject__Group__1__Impl rule__TerminalObject__Group__2 )
            // InternalSystemDescriptor.g:1232:2: rule__TerminalObject__Group__1__Impl rule__TerminalObject__Group__2
            {
            pushFollow(FOLLOW_13);
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
    // InternalSystemDescriptor.g:1239:1: rule__TerminalObject__Group__1__Impl : ( ':' ) ;
    public final void rule__TerminalObject__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1243:1: ( ( ':' ) )
            // InternalSystemDescriptor.g:1244:1: ( ':' )
            {
            // InternalSystemDescriptor.g:1244:1: ( ':' )
            // InternalSystemDescriptor.g:1245:2: ':'
            {
             before(grammarAccess.getTerminalObjectAccess().getColonKeyword_1()); 
            match(input,20,FOLLOW_2); 
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
    // InternalSystemDescriptor.g:1254:1: rule__TerminalObject__Group__2 : rule__TerminalObject__Group__2__Impl ;
    public final void rule__TerminalObject__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1258:1: ( rule__TerminalObject__Group__2__Impl )
            // InternalSystemDescriptor.g:1259:2: rule__TerminalObject__Group__2__Impl
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
    // InternalSystemDescriptor.g:1265:1: rule__TerminalObject__Group__2__Impl : ( ( rule__TerminalObject__ContentAssignment_2 ) ) ;
    public final void rule__TerminalObject__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1269:1: ( ( ( rule__TerminalObject__ContentAssignment_2 ) ) )
            // InternalSystemDescriptor.g:1270:1: ( ( rule__TerminalObject__ContentAssignment_2 ) )
            {
            // InternalSystemDescriptor.g:1270:1: ( ( rule__TerminalObject__ContentAssignment_2 ) )
            // InternalSystemDescriptor.g:1271:2: ( rule__TerminalObject__ContentAssignment_2 )
            {
             before(grammarAccess.getTerminalObjectAccess().getContentAssignment_2()); 
            // InternalSystemDescriptor.g:1272:2: ( rule__TerminalObject__ContentAssignment_2 )
            // InternalSystemDescriptor.g:1272:3: rule__TerminalObject__ContentAssignment_2
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


    // $ANTLR start "rule__Descriptor__PackageAssignment_0"
    // InternalSystemDescriptor.g:1281:1: rule__Descriptor__PackageAssignment_0 : ( rulePackage ) ;
    public final void rule__Descriptor__PackageAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1285:1: ( ( rulePackage ) )
            // InternalSystemDescriptor.g:1286:2: ( rulePackage )
            {
            // InternalSystemDescriptor.g:1286:2: ( rulePackage )
            // InternalSystemDescriptor.g:1287:3: rulePackage
            {
             before(grammarAccess.getDescriptorAccess().getPackagePackageParserRuleCall_0_0()); 
            pushFollow(FOLLOW_2);
            rulePackage();

            state._fsp--;

             after(grammarAccess.getDescriptorAccess().getPackagePackageParserRuleCall_0_0()); 

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
    // $ANTLR end "rule__Descriptor__PackageAssignment_0"


    // $ANTLR start "rule__Descriptor__ElementsAssignment_1"
    // InternalSystemDescriptor.g:1296:1: rule__Descriptor__ElementsAssignment_1 : ( ruleElement ) ;
    public final void rule__Descriptor__ElementsAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1300:1: ( ( ruleElement ) )
            // InternalSystemDescriptor.g:1301:2: ( ruleElement )
            {
            // InternalSystemDescriptor.g:1301:2: ( ruleElement )
            // InternalSystemDescriptor.g:1302:3: ruleElement
            {
             before(grammarAccess.getDescriptorAccess().getElementsElementParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
            ruleElement();

            state._fsp--;

             after(grammarAccess.getDescriptorAccess().getElementsElementParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__Descriptor__ElementsAssignment_1"


    // $ANTLR start "rule__Package__NameAssignment_1"
    // InternalSystemDescriptor.g:1311:1: rule__Package__NameAssignment_1 : ( ruleQualifiedName ) ;
    public final void rule__Package__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1315:1: ( ( ruleQualifiedName ) )
            // InternalSystemDescriptor.g:1316:2: ( ruleQualifiedName )
            {
            // InternalSystemDescriptor.g:1316:2: ( ruleQualifiedName )
            // InternalSystemDescriptor.g:1317:3: ruleQualifiedName
            {
             before(grammarAccess.getPackageAccess().getNameQualifiedNameParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
            ruleQualifiedName();

            state._fsp--;

             after(grammarAccess.getPackageAccess().getNameQualifiedNameParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__Package__NameAssignment_1"


    // $ANTLR start "rule__Data__NameAssignment_1"
    // InternalSystemDescriptor.g:1326:1: rule__Data__NameAssignment_1 : ( ruleUnqualifiedName ) ;
    public final void rule__Data__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1330:1: ( ( ruleUnqualifiedName ) )
            // InternalSystemDescriptor.g:1331:2: ( ruleUnqualifiedName )
            {
            // InternalSystemDescriptor.g:1331:2: ( ruleUnqualifiedName )
            // InternalSystemDescriptor.g:1332:3: ruleUnqualifiedName
            {
             before(grammarAccess.getDataAccess().getNameUnqualifiedNameParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
            ruleUnqualifiedName();

            state._fsp--;

             after(grammarAccess.getDataAccess().getNameUnqualifiedNameParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__Data__NameAssignment_1"


    // $ANTLR start "rule__Model__NameAssignment_1"
    // InternalSystemDescriptor.g:1341:1: rule__Model__NameAssignment_1 : ( ruleUnqualifiedName ) ;
    public final void rule__Model__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1345:1: ( ( ruleUnqualifiedName ) )
            // InternalSystemDescriptor.g:1346:2: ( ruleUnqualifiedName )
            {
            // InternalSystemDescriptor.g:1346:2: ( ruleUnqualifiedName )
            // InternalSystemDescriptor.g:1347:3: ruleUnqualifiedName
            {
             before(grammarAccess.getModelAccess().getNameUnqualifiedNameParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
            ruleUnqualifiedName();

            state._fsp--;

             after(grammarAccess.getModelAccess().getNameUnqualifiedNameParserRuleCall_1_0()); 

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
    // $ANTLR end "rule__Model__NameAssignment_1"


    // $ANTLR start "rule__Object__FirstObjectAssignment_1"
    // InternalSystemDescriptor.g:1356:1: rule__Object__FirstObjectAssignment_1 : ( ruleTerminalObject ) ;
    public final void rule__Object__FirstObjectAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1360:1: ( ( ruleTerminalObject ) )
            // InternalSystemDescriptor.g:1361:2: ( ruleTerminalObject )
            {
            // InternalSystemDescriptor.g:1361:2: ( ruleTerminalObject )
            // InternalSystemDescriptor.g:1362:3: ruleTerminalObject
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
    // InternalSystemDescriptor.g:1371:1: rule__Object__ObjectsAssignment_2_1 : ( ruleTerminalObject ) ;
    public final void rule__Object__ObjectsAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1375:1: ( ( ruleTerminalObject ) )
            // InternalSystemDescriptor.g:1376:2: ( ruleTerminalObject )
            {
            // InternalSystemDescriptor.g:1376:2: ( ruleTerminalObject )
            // InternalSystemDescriptor.g:1377:3: ruleTerminalObject
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
    // InternalSystemDescriptor.g:1386:1: rule__Array__FirstItemAssignment_1 : ( ruleObjectValue ) ;
    public final void rule__Array__FirstItemAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1390:1: ( ( ruleObjectValue ) )
            // InternalSystemDescriptor.g:1391:2: ( ruleObjectValue )
            {
            // InternalSystemDescriptor.g:1391:2: ( ruleObjectValue )
            // InternalSystemDescriptor.g:1392:3: ruleObjectValue
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
    // InternalSystemDescriptor.g:1401:1: rule__Array__ItemsAssignment_2_1 : ( ruleObjectValue ) ;
    public final void rule__Array__ItemsAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1405:1: ( ( ruleObjectValue ) )
            // InternalSystemDescriptor.g:1406:2: ( ruleObjectValue )
            {
            // InternalSystemDescriptor.g:1406:2: ( ruleObjectValue )
            // InternalSystemDescriptor.g:1407:3: ruleObjectValue
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
    // InternalSystemDescriptor.g:1416:1: rule__EmptyObject__IsEmptyAssignment : ( ( '{}' ) ) ;
    public final void rule__EmptyObject__IsEmptyAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1420:1: ( ( ( '{}' ) ) )
            // InternalSystemDescriptor.g:1421:2: ( ( '{}' ) )
            {
            // InternalSystemDescriptor.g:1421:2: ( ( '{}' ) )
            // InternalSystemDescriptor.g:1422:3: ( '{}' )
            {
             before(grammarAccess.getEmptyObjectAccess().getIsEmptyLeftCurlyBracketRightCurlyBracketKeyword_0()); 
            // InternalSystemDescriptor.g:1423:3: ( '{}' )
            // InternalSystemDescriptor.g:1424:4: '{}'
            {
             before(grammarAccess.getEmptyObjectAccess().getIsEmptyLeftCurlyBracketRightCurlyBracketKeyword_0()); 
            match(input,21,FOLLOW_2); 
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
    // InternalSystemDescriptor.g:1435:1: rule__EmptyArray__IsEmptyAssignment : ( ( '[]' ) ) ;
    public final void rule__EmptyArray__IsEmptyAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1439:1: ( ( ( '[]' ) ) )
            // InternalSystemDescriptor.g:1440:2: ( ( '[]' ) )
            {
            // InternalSystemDescriptor.g:1440:2: ( ( '[]' ) )
            // InternalSystemDescriptor.g:1441:3: ( '[]' )
            {
             before(grammarAccess.getEmptyArrayAccess().getIsEmptyLeftSquareBracketRightSquareBracketKeyword_0()); 
            // InternalSystemDescriptor.g:1442:3: ( '[]' )
            // InternalSystemDescriptor.g:1443:4: '[]'
            {
             before(grammarAccess.getEmptyArrayAccess().getIsEmptyLeftSquareBracketRightSquareBracketKeyword_0()); 
            match(input,22,FOLLOW_2); 
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
    // InternalSystemDescriptor.g:1454:1: rule__ObjectValue__ValueAssignment_0 : ( RULE_STRING ) ;
    public final void rule__ObjectValue__ValueAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1458:1: ( ( RULE_STRING ) )
            // InternalSystemDescriptor.g:1459:2: ( RULE_STRING )
            {
            // InternalSystemDescriptor.g:1459:2: ( RULE_STRING )
            // InternalSystemDescriptor.g:1460:3: RULE_STRING
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
    // InternalSystemDescriptor.g:1469:1: rule__TerminalObject__ElementAssignment_0 : ( RULE_STRING ) ;
    public final void rule__TerminalObject__ElementAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1473:1: ( ( RULE_STRING ) )
            // InternalSystemDescriptor.g:1474:2: ( RULE_STRING )
            {
            // InternalSystemDescriptor.g:1474:2: ( RULE_STRING )
            // InternalSystemDescriptor.g:1475:3: RULE_STRING
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
    // InternalSystemDescriptor.g:1484:1: rule__TerminalObject__ContentAssignment_2 : ( ruleObjectValue ) ;
    public final void rule__TerminalObject__ContentAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalSystemDescriptor.g:1488:1: ( ( ruleObjectValue ) )
            // InternalSystemDescriptor.g:1489:2: ( ruleObjectValue )
            {
            // InternalSystemDescriptor.g:1489:2: ( ruleObjectValue )
            // InternalSystemDescriptor.g:1490:3: ruleObjectValue
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
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000012002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000028000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000644020L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x00000000000A0000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000100000L});

}