package com.ngc.seaside.systemdescriptor.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import com.ngc.seaside.systemdescriptor.services.SystemDescriptorGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalSystemDescriptorParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'.'", "'package'", "'data'", "'{'", "'}'", "'model'", "','", "'['", "']'", "'{}'", "'[]'", "':'"
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

        public InternalSystemDescriptorParser(TokenStream input, SystemDescriptorGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "Descriptor";
       	}

       	@Override
       	protected SystemDescriptorGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleDescriptor"
    // InternalSystemDescriptor.g:64:1: entryRuleDescriptor returns [EObject current=null] : iv_ruleDescriptor= ruleDescriptor EOF ;
    public final EObject entryRuleDescriptor() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDescriptor = null;


        try {
            // InternalSystemDescriptor.g:64:51: (iv_ruleDescriptor= ruleDescriptor EOF )
            // InternalSystemDescriptor.g:65:2: iv_ruleDescriptor= ruleDescriptor EOF
            {
             newCompositeNode(grammarAccess.getDescriptorRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDescriptor=ruleDescriptor();

            state._fsp--;

             current =iv_ruleDescriptor; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDescriptor"


    // $ANTLR start "ruleDescriptor"
    // InternalSystemDescriptor.g:71:1: ruleDescriptor returns [EObject current=null] : ( ( (lv_package_0_0= rulePackage ) ) ( (lv_elements_1_0= ruleElement ) )+ ) ;
    public final EObject ruleDescriptor() throws RecognitionException {
        EObject current = null;

        EObject lv_package_0_0 = null;

        EObject lv_elements_1_0 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:77:2: ( ( ( (lv_package_0_0= rulePackage ) ) ( (lv_elements_1_0= ruleElement ) )+ ) )
            // InternalSystemDescriptor.g:78:2: ( ( (lv_package_0_0= rulePackage ) ) ( (lv_elements_1_0= ruleElement ) )+ )
            {
            // InternalSystemDescriptor.g:78:2: ( ( (lv_package_0_0= rulePackage ) ) ( (lv_elements_1_0= ruleElement ) )+ )
            // InternalSystemDescriptor.g:79:3: ( (lv_package_0_0= rulePackage ) ) ( (lv_elements_1_0= ruleElement ) )+
            {
            // InternalSystemDescriptor.g:79:3: ( (lv_package_0_0= rulePackage ) )
            // InternalSystemDescriptor.g:80:4: (lv_package_0_0= rulePackage )
            {
            // InternalSystemDescriptor.g:80:4: (lv_package_0_0= rulePackage )
            // InternalSystemDescriptor.g:81:5: lv_package_0_0= rulePackage
            {

            					newCompositeNode(grammarAccess.getDescriptorAccess().getPackagePackageParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_3);
            lv_package_0_0=rulePackage();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getDescriptorRule());
            					}
            					set(
            						current,
            						"package",
            						lv_package_0_0,
            						"com.ngc.seaside.systemdescriptor.SystemDescriptor.Package");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalSystemDescriptor.g:98:3: ( (lv_elements_1_0= ruleElement ) )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==13||LA1_0==16) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalSystemDescriptor.g:99:4: (lv_elements_1_0= ruleElement )
            	    {
            	    // InternalSystemDescriptor.g:99:4: (lv_elements_1_0= ruleElement )
            	    // InternalSystemDescriptor.g:100:5: lv_elements_1_0= ruleElement
            	    {

            	    					newCompositeNode(grammarAccess.getDescriptorAccess().getElementsElementParserRuleCall_1_0());
            	    				
            	    pushFollow(FOLLOW_4);
            	    lv_elements_1_0=ruleElement();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getDescriptorRule());
            	    					}
            	    					add(
            	    						current,
            	    						"elements",
            	    						lv_elements_1_0,
            	    						"com.ngc.seaside.systemdescriptor.SystemDescriptor.Element");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDescriptor"


    // $ANTLR start "entryRuleQualifiedName"
    // InternalSystemDescriptor.g:121:1: entryRuleQualifiedName returns [String current=null] : iv_ruleQualifiedName= ruleQualifiedName EOF ;
    public final String entryRuleQualifiedName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedName = null;


        try {
            // InternalSystemDescriptor.g:121:53: (iv_ruleQualifiedName= ruleQualifiedName EOF )
            // InternalSystemDescriptor.g:122:2: iv_ruleQualifiedName= ruleQualifiedName EOF
            {
             newCompositeNode(grammarAccess.getQualifiedNameRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQualifiedName=ruleQualifiedName();

            state._fsp--;

             current =iv_ruleQualifiedName.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleQualifiedName"


    // $ANTLR start "ruleQualifiedName"
    // InternalSystemDescriptor.g:128:1: ruleQualifiedName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_2=null;


        	enterRule();

        try {
            // InternalSystemDescriptor.g:134:2: ( (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) )
            // InternalSystemDescriptor.g:135:2: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            {
            // InternalSystemDescriptor.g:135:2: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            // InternalSystemDescriptor.g:136:3: this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )*
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            			current.merge(this_ID_0);
            		

            			newLeafNode(this_ID_0, grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_0());
            		
            // InternalSystemDescriptor.g:143:3: (kw= '.' this_ID_2= RULE_ID )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==11) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalSystemDescriptor.g:144:4: kw= '.' this_ID_2= RULE_ID
            	    {
            	    kw=(Token)match(input,11,FOLLOW_6); 

            	    				current.merge(kw);
            	    				newLeafNode(kw, grammarAccess.getQualifiedNameAccess().getFullStopKeyword_1_0());
            	    			
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_5); 

            	    				current.merge(this_ID_2);
            	    			

            	    				newLeafNode(this_ID_2, grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_1_1());
            	    			

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleQualifiedName"


    // $ANTLR start "entryRuleUnqualifiedName"
    // InternalSystemDescriptor.g:161:1: entryRuleUnqualifiedName returns [String current=null] : iv_ruleUnqualifiedName= ruleUnqualifiedName EOF ;
    public final String entryRuleUnqualifiedName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleUnqualifiedName = null;


        try {
            // InternalSystemDescriptor.g:161:55: (iv_ruleUnqualifiedName= ruleUnqualifiedName EOF )
            // InternalSystemDescriptor.g:162:2: iv_ruleUnqualifiedName= ruleUnqualifiedName EOF
            {
             newCompositeNode(grammarAccess.getUnqualifiedNameRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleUnqualifiedName=ruleUnqualifiedName();

            state._fsp--;

             current =iv_ruleUnqualifiedName.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleUnqualifiedName"


    // $ANTLR start "ruleUnqualifiedName"
    // InternalSystemDescriptor.g:168:1: ruleUnqualifiedName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_ID_0= RULE_ID ;
    public final AntlrDatatypeRuleToken ruleUnqualifiedName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;


        	enterRule();

        try {
            // InternalSystemDescriptor.g:174:2: (this_ID_0= RULE_ID )
            // InternalSystemDescriptor.g:175:2: this_ID_0= RULE_ID
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            		current.merge(this_ID_0);
            	

            		newLeafNode(this_ID_0, grammarAccess.getUnqualifiedNameAccess().getIDTerminalRuleCall());
            	

            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleUnqualifiedName"


    // $ANTLR start "entryRulePackage"
    // InternalSystemDescriptor.g:185:1: entryRulePackage returns [EObject current=null] : iv_rulePackage= rulePackage EOF ;
    public final EObject entryRulePackage() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePackage = null;


        try {
            // InternalSystemDescriptor.g:185:48: (iv_rulePackage= rulePackage EOF )
            // InternalSystemDescriptor.g:186:2: iv_rulePackage= rulePackage EOF
            {
             newCompositeNode(grammarAccess.getPackageRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePackage=rulePackage();

            state._fsp--;

             current =iv_rulePackage; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePackage"


    // $ANTLR start "rulePackage"
    // InternalSystemDescriptor.g:192:1: rulePackage returns [EObject current=null] : (otherlv_0= 'package' ( (lv_name_1_0= ruleQualifiedName ) ) ) ;
    public final EObject rulePackage() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:198:2: ( (otherlv_0= 'package' ( (lv_name_1_0= ruleQualifiedName ) ) ) )
            // InternalSystemDescriptor.g:199:2: (otherlv_0= 'package' ( (lv_name_1_0= ruleQualifiedName ) ) )
            {
            // InternalSystemDescriptor.g:199:2: (otherlv_0= 'package' ( (lv_name_1_0= ruleQualifiedName ) ) )
            // InternalSystemDescriptor.g:200:3: otherlv_0= 'package' ( (lv_name_1_0= ruleQualifiedName ) )
            {
            otherlv_0=(Token)match(input,12,FOLLOW_6); 

            			newLeafNode(otherlv_0, grammarAccess.getPackageAccess().getPackageKeyword_0());
            		
            // InternalSystemDescriptor.g:204:3: ( (lv_name_1_0= ruleQualifiedName ) )
            // InternalSystemDescriptor.g:205:4: (lv_name_1_0= ruleQualifiedName )
            {
            // InternalSystemDescriptor.g:205:4: (lv_name_1_0= ruleQualifiedName )
            // InternalSystemDescriptor.g:206:5: lv_name_1_0= ruleQualifiedName
            {

            					newCompositeNode(grammarAccess.getPackageAccess().getNameQualifiedNameParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_name_1_0=ruleQualifiedName();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getPackageRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_1_0,
            						"com.ngc.seaside.systemdescriptor.SystemDescriptor.QualifiedName");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePackage"


    // $ANTLR start "entryRuleData"
    // InternalSystemDescriptor.g:227:1: entryRuleData returns [EObject current=null] : iv_ruleData= ruleData EOF ;
    public final EObject entryRuleData() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleData = null;


        try {
            // InternalSystemDescriptor.g:227:45: (iv_ruleData= ruleData EOF )
            // InternalSystemDescriptor.g:228:2: iv_ruleData= ruleData EOF
            {
             newCompositeNode(grammarAccess.getDataRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleData=ruleData();

            state._fsp--;

             current =iv_ruleData; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleData"


    // $ANTLR start "ruleData"
    // InternalSystemDescriptor.g:234:1: ruleData returns [EObject current=null] : (otherlv_0= 'data' ( (lv_name_1_0= ruleUnqualifiedName ) ) otherlv_2= '{' otherlv_3= '}' ) ;
    public final EObject ruleData() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:240:2: ( (otherlv_0= 'data' ( (lv_name_1_0= ruleUnqualifiedName ) ) otherlv_2= '{' otherlv_3= '}' ) )
            // InternalSystemDescriptor.g:241:2: (otherlv_0= 'data' ( (lv_name_1_0= ruleUnqualifiedName ) ) otherlv_2= '{' otherlv_3= '}' )
            {
            // InternalSystemDescriptor.g:241:2: (otherlv_0= 'data' ( (lv_name_1_0= ruleUnqualifiedName ) ) otherlv_2= '{' otherlv_3= '}' )
            // InternalSystemDescriptor.g:242:3: otherlv_0= 'data' ( (lv_name_1_0= ruleUnqualifiedName ) ) otherlv_2= '{' otherlv_3= '}'
            {
            otherlv_0=(Token)match(input,13,FOLLOW_6); 

            			newLeafNode(otherlv_0, grammarAccess.getDataAccess().getDataKeyword_0());
            		
            // InternalSystemDescriptor.g:246:3: ( (lv_name_1_0= ruleUnqualifiedName ) )
            // InternalSystemDescriptor.g:247:4: (lv_name_1_0= ruleUnqualifiedName )
            {
            // InternalSystemDescriptor.g:247:4: (lv_name_1_0= ruleUnqualifiedName )
            // InternalSystemDescriptor.g:248:5: lv_name_1_0= ruleUnqualifiedName
            {

            					newCompositeNode(grammarAccess.getDataAccess().getNameUnqualifiedNameParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_7);
            lv_name_1_0=ruleUnqualifiedName();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getDataRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_1_0,
            						"com.ngc.seaside.systemdescriptor.SystemDescriptor.UnqualifiedName");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,14,FOLLOW_8); 

            			newLeafNode(otherlv_2, grammarAccess.getDataAccess().getLeftCurlyBracketKeyword_2());
            		
            otherlv_3=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_3, grammarAccess.getDataAccess().getRightCurlyBracketKeyword_3());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleData"


    // $ANTLR start "entryRuleModel"
    // InternalSystemDescriptor.g:277:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalSystemDescriptor.g:277:46: (iv_ruleModel= ruleModel EOF )
            // InternalSystemDescriptor.g:278:2: iv_ruleModel= ruleModel EOF
            {
             newCompositeNode(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleModel=ruleModel();

            state._fsp--;

             current =iv_ruleModel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // InternalSystemDescriptor.g:284:1: ruleModel returns [EObject current=null] : (otherlv_0= 'model' ( (lv_name_1_0= ruleUnqualifiedName ) ) otherlv_2= '{' otherlv_3= '}' ) ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:290:2: ( (otherlv_0= 'model' ( (lv_name_1_0= ruleUnqualifiedName ) ) otherlv_2= '{' otherlv_3= '}' ) )
            // InternalSystemDescriptor.g:291:2: (otherlv_0= 'model' ( (lv_name_1_0= ruleUnqualifiedName ) ) otherlv_2= '{' otherlv_3= '}' )
            {
            // InternalSystemDescriptor.g:291:2: (otherlv_0= 'model' ( (lv_name_1_0= ruleUnqualifiedName ) ) otherlv_2= '{' otherlv_3= '}' )
            // InternalSystemDescriptor.g:292:3: otherlv_0= 'model' ( (lv_name_1_0= ruleUnqualifiedName ) ) otherlv_2= '{' otherlv_3= '}'
            {
            otherlv_0=(Token)match(input,16,FOLLOW_6); 

            			newLeafNode(otherlv_0, grammarAccess.getModelAccess().getModelKeyword_0());
            		
            // InternalSystemDescriptor.g:296:3: ( (lv_name_1_0= ruleUnqualifiedName ) )
            // InternalSystemDescriptor.g:297:4: (lv_name_1_0= ruleUnqualifiedName )
            {
            // InternalSystemDescriptor.g:297:4: (lv_name_1_0= ruleUnqualifiedName )
            // InternalSystemDescriptor.g:298:5: lv_name_1_0= ruleUnqualifiedName
            {

            					newCompositeNode(grammarAccess.getModelAccess().getNameUnqualifiedNameParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_7);
            lv_name_1_0=ruleUnqualifiedName();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getModelRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_1_0,
            						"com.ngc.seaside.systemdescriptor.SystemDescriptor.UnqualifiedName");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,14,FOLLOW_8); 

            			newLeafNode(otherlv_2, grammarAccess.getModelAccess().getLeftCurlyBracketKeyword_2());
            		
            otherlv_3=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_3, grammarAccess.getModelAccess().getRightCurlyBracketKeyword_3());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRuleElement"
    // InternalSystemDescriptor.g:327:1: entryRuleElement returns [EObject current=null] : iv_ruleElement= ruleElement EOF ;
    public final EObject entryRuleElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleElement = null;


        try {
            // InternalSystemDescriptor.g:327:48: (iv_ruleElement= ruleElement EOF )
            // InternalSystemDescriptor.g:328:2: iv_ruleElement= ruleElement EOF
            {
             newCompositeNode(grammarAccess.getElementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleElement=ruleElement();

            state._fsp--;

             current =iv_ruleElement; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleElement"


    // $ANTLR start "ruleElement"
    // InternalSystemDescriptor.g:334:1: ruleElement returns [EObject current=null] : (this_Data_0= ruleData | this_Model_1= ruleModel ) ;
    public final EObject ruleElement() throws RecognitionException {
        EObject current = null;

        EObject this_Data_0 = null;

        EObject this_Model_1 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:340:2: ( (this_Data_0= ruleData | this_Model_1= ruleModel ) )
            // InternalSystemDescriptor.g:341:2: (this_Data_0= ruleData | this_Model_1= ruleModel )
            {
            // InternalSystemDescriptor.g:341:2: (this_Data_0= ruleData | this_Model_1= ruleModel )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==13) ) {
                alt3=1;
            }
            else if ( (LA3_0==16) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // InternalSystemDescriptor.g:342:3: this_Data_0= ruleData
                    {

                    			newCompositeNode(grammarAccess.getElementAccess().getDataParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_Data_0=ruleData();

                    state._fsp--;


                    			current = this_Data_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalSystemDescriptor.g:351:3: this_Model_1= ruleModel
                    {

                    			newCompositeNode(grammarAccess.getElementAccess().getModelParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_Model_1=ruleModel();

                    state._fsp--;


                    			current = this_Model_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleElement"


    // $ANTLR start "entryRuleObject"
    // InternalSystemDescriptor.g:363:1: entryRuleObject returns [EObject current=null] : iv_ruleObject= ruleObject EOF ;
    public final EObject entryRuleObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleObject = null;


        try {
            // InternalSystemDescriptor.g:363:47: (iv_ruleObject= ruleObject EOF )
            // InternalSystemDescriptor.g:364:2: iv_ruleObject= ruleObject EOF
            {
             newCompositeNode(grammarAccess.getObjectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleObject=ruleObject();

            state._fsp--;

             current =iv_ruleObject; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleObject"


    // $ANTLR start "ruleObject"
    // InternalSystemDescriptor.g:370:1: ruleObject returns [EObject current=null] : (otherlv_0= '{' ( (lv_firstObject_1_0= ruleTerminalObject ) ) (otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) ) )* otherlv_4= '}' ) ;
    public final EObject ruleObject() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_firstObject_1_0 = null;

        EObject lv_objects_3_0 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:376:2: ( (otherlv_0= '{' ( (lv_firstObject_1_0= ruleTerminalObject ) ) (otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) ) )* otherlv_4= '}' ) )
            // InternalSystemDescriptor.g:377:2: (otherlv_0= '{' ( (lv_firstObject_1_0= ruleTerminalObject ) ) (otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) ) )* otherlv_4= '}' )
            {
            // InternalSystemDescriptor.g:377:2: (otherlv_0= '{' ( (lv_firstObject_1_0= ruleTerminalObject ) ) (otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) ) )* otherlv_4= '}' )
            // InternalSystemDescriptor.g:378:3: otherlv_0= '{' ( (lv_firstObject_1_0= ruleTerminalObject ) ) (otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) ) )* otherlv_4= '}'
            {
            otherlv_0=(Token)match(input,14,FOLLOW_9); 

            			newLeafNode(otherlv_0, grammarAccess.getObjectAccess().getLeftCurlyBracketKeyword_0());
            		
            // InternalSystemDescriptor.g:382:3: ( (lv_firstObject_1_0= ruleTerminalObject ) )
            // InternalSystemDescriptor.g:383:4: (lv_firstObject_1_0= ruleTerminalObject )
            {
            // InternalSystemDescriptor.g:383:4: (lv_firstObject_1_0= ruleTerminalObject )
            // InternalSystemDescriptor.g:384:5: lv_firstObject_1_0= ruleTerminalObject
            {

            					newCompositeNode(grammarAccess.getObjectAccess().getFirstObjectTerminalObjectParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_10);
            lv_firstObject_1_0=ruleTerminalObject();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getObjectRule());
            					}
            					set(
            						current,
            						"firstObject",
            						lv_firstObject_1_0,
            						"com.ngc.seaside.systemdescriptor.SystemDescriptor.TerminalObject");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalSystemDescriptor.g:401:3: (otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==17) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalSystemDescriptor.g:402:4: otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) )
            	    {
            	    otherlv_2=(Token)match(input,17,FOLLOW_9); 

            	    				newLeafNode(otherlv_2, grammarAccess.getObjectAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalSystemDescriptor.g:406:4: ( (lv_objects_3_0= ruleTerminalObject ) )
            	    // InternalSystemDescriptor.g:407:5: (lv_objects_3_0= ruleTerminalObject )
            	    {
            	    // InternalSystemDescriptor.g:407:5: (lv_objects_3_0= ruleTerminalObject )
            	    // InternalSystemDescriptor.g:408:6: lv_objects_3_0= ruleTerminalObject
            	    {

            	    						newCompositeNode(grammarAccess.getObjectAccess().getObjectsTerminalObjectParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_10);
            	    lv_objects_3_0=ruleTerminalObject();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getObjectRule());
            	    						}
            	    						add(
            	    							current,
            	    							"objects",
            	    							lv_objects_3_0,
            	    							"com.ngc.seaside.systemdescriptor.SystemDescriptor.TerminalObject");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            otherlv_4=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getObjectAccess().getRightCurlyBracketKeyword_3());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleObject"


    // $ANTLR start "entryRuleArray"
    // InternalSystemDescriptor.g:434:1: entryRuleArray returns [EObject current=null] : iv_ruleArray= ruleArray EOF ;
    public final EObject entryRuleArray() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleArray = null;


        try {
            // InternalSystemDescriptor.g:434:46: (iv_ruleArray= ruleArray EOF )
            // InternalSystemDescriptor.g:435:2: iv_ruleArray= ruleArray EOF
            {
             newCompositeNode(grammarAccess.getArrayRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleArray=ruleArray();

            state._fsp--;

             current =iv_ruleArray; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleArray"


    // $ANTLR start "ruleArray"
    // InternalSystemDescriptor.g:441:1: ruleArray returns [EObject current=null] : (otherlv_0= '[' ( (lv_firstItem_1_0= ruleObjectValue ) ) (otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) ) )* otherlv_4= ']' ) ;
    public final EObject ruleArray() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_firstItem_1_0 = null;

        EObject lv_items_3_0 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:447:2: ( (otherlv_0= '[' ( (lv_firstItem_1_0= ruleObjectValue ) ) (otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) ) )* otherlv_4= ']' ) )
            // InternalSystemDescriptor.g:448:2: (otherlv_0= '[' ( (lv_firstItem_1_0= ruleObjectValue ) ) (otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) ) )* otherlv_4= ']' )
            {
            // InternalSystemDescriptor.g:448:2: (otherlv_0= '[' ( (lv_firstItem_1_0= ruleObjectValue ) ) (otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) ) )* otherlv_4= ']' )
            // InternalSystemDescriptor.g:449:3: otherlv_0= '[' ( (lv_firstItem_1_0= ruleObjectValue ) ) (otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) ) )* otherlv_4= ']'
            {
            otherlv_0=(Token)match(input,18,FOLLOW_11); 

            			newLeafNode(otherlv_0, grammarAccess.getArrayAccess().getLeftSquareBracketKeyword_0());
            		
            // InternalSystemDescriptor.g:453:3: ( (lv_firstItem_1_0= ruleObjectValue ) )
            // InternalSystemDescriptor.g:454:4: (lv_firstItem_1_0= ruleObjectValue )
            {
            // InternalSystemDescriptor.g:454:4: (lv_firstItem_1_0= ruleObjectValue )
            // InternalSystemDescriptor.g:455:5: lv_firstItem_1_0= ruleObjectValue
            {

            					newCompositeNode(grammarAccess.getArrayAccess().getFirstItemObjectValueParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_12);
            lv_firstItem_1_0=ruleObjectValue();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getArrayRule());
            					}
            					set(
            						current,
            						"firstItem",
            						lv_firstItem_1_0,
            						"com.ngc.seaside.systemdescriptor.SystemDescriptor.ObjectValue");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalSystemDescriptor.g:472:3: (otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==17) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalSystemDescriptor.g:473:4: otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) )
            	    {
            	    otherlv_2=(Token)match(input,17,FOLLOW_11); 

            	    				newLeafNode(otherlv_2, grammarAccess.getArrayAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalSystemDescriptor.g:477:4: ( (lv_items_3_0= ruleObjectValue ) )
            	    // InternalSystemDescriptor.g:478:5: (lv_items_3_0= ruleObjectValue )
            	    {
            	    // InternalSystemDescriptor.g:478:5: (lv_items_3_0= ruleObjectValue )
            	    // InternalSystemDescriptor.g:479:6: lv_items_3_0= ruleObjectValue
            	    {

            	    						newCompositeNode(grammarAccess.getArrayAccess().getItemsObjectValueParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_12);
            	    lv_items_3_0=ruleObjectValue();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getArrayRule());
            	    						}
            	    						add(
            	    							current,
            	    							"items",
            	    							lv_items_3_0,
            	    							"com.ngc.seaside.systemdescriptor.SystemDescriptor.ObjectValue");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            otherlv_4=(Token)match(input,19,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getArrayAccess().getRightSquareBracketKeyword_3());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleArray"


    // $ANTLR start "entryRuleEmptyObject"
    // InternalSystemDescriptor.g:505:1: entryRuleEmptyObject returns [EObject current=null] : iv_ruleEmptyObject= ruleEmptyObject EOF ;
    public final EObject entryRuleEmptyObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEmptyObject = null;


        try {
            // InternalSystemDescriptor.g:505:52: (iv_ruleEmptyObject= ruleEmptyObject EOF )
            // InternalSystemDescriptor.g:506:2: iv_ruleEmptyObject= ruleEmptyObject EOF
            {
             newCompositeNode(grammarAccess.getEmptyObjectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEmptyObject=ruleEmptyObject();

            state._fsp--;

             current =iv_ruleEmptyObject; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEmptyObject"


    // $ANTLR start "ruleEmptyObject"
    // InternalSystemDescriptor.g:512:1: ruleEmptyObject returns [EObject current=null] : ( (lv_isEmpty_0_0= '{}' ) ) ;
    public final EObject ruleEmptyObject() throws RecognitionException {
        EObject current = null;

        Token lv_isEmpty_0_0=null;


        	enterRule();

        try {
            // InternalSystemDescriptor.g:518:2: ( ( (lv_isEmpty_0_0= '{}' ) ) )
            // InternalSystemDescriptor.g:519:2: ( (lv_isEmpty_0_0= '{}' ) )
            {
            // InternalSystemDescriptor.g:519:2: ( (lv_isEmpty_0_0= '{}' ) )
            // InternalSystemDescriptor.g:520:3: (lv_isEmpty_0_0= '{}' )
            {
            // InternalSystemDescriptor.g:520:3: (lv_isEmpty_0_0= '{}' )
            // InternalSystemDescriptor.g:521:4: lv_isEmpty_0_0= '{}'
            {
            lv_isEmpty_0_0=(Token)match(input,20,FOLLOW_2); 

            				newLeafNode(lv_isEmpty_0_0, grammarAccess.getEmptyObjectAccess().getIsEmptyLeftCurlyBracketRightCurlyBracketKeyword_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getEmptyObjectRule());
            				}
            				setWithLastConsumed(current, "isEmpty", true, "{}");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEmptyObject"


    // $ANTLR start "entryRuleEmptyArray"
    // InternalSystemDescriptor.g:536:1: entryRuleEmptyArray returns [EObject current=null] : iv_ruleEmptyArray= ruleEmptyArray EOF ;
    public final EObject entryRuleEmptyArray() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEmptyArray = null;


        try {
            // InternalSystemDescriptor.g:536:51: (iv_ruleEmptyArray= ruleEmptyArray EOF )
            // InternalSystemDescriptor.g:537:2: iv_ruleEmptyArray= ruleEmptyArray EOF
            {
             newCompositeNode(grammarAccess.getEmptyArrayRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEmptyArray=ruleEmptyArray();

            state._fsp--;

             current =iv_ruleEmptyArray; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEmptyArray"


    // $ANTLR start "ruleEmptyArray"
    // InternalSystemDescriptor.g:543:1: ruleEmptyArray returns [EObject current=null] : ( (lv_isEmpty_0_0= '[]' ) ) ;
    public final EObject ruleEmptyArray() throws RecognitionException {
        EObject current = null;

        Token lv_isEmpty_0_0=null;


        	enterRule();

        try {
            // InternalSystemDescriptor.g:549:2: ( ( (lv_isEmpty_0_0= '[]' ) ) )
            // InternalSystemDescriptor.g:550:2: ( (lv_isEmpty_0_0= '[]' ) )
            {
            // InternalSystemDescriptor.g:550:2: ( (lv_isEmpty_0_0= '[]' ) )
            // InternalSystemDescriptor.g:551:3: (lv_isEmpty_0_0= '[]' )
            {
            // InternalSystemDescriptor.g:551:3: (lv_isEmpty_0_0= '[]' )
            // InternalSystemDescriptor.g:552:4: lv_isEmpty_0_0= '[]'
            {
            lv_isEmpty_0_0=(Token)match(input,21,FOLLOW_2); 

            				newLeafNode(lv_isEmpty_0_0, grammarAccess.getEmptyArrayAccess().getIsEmptyLeftSquareBracketRightSquareBracketKeyword_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getEmptyArrayRule());
            				}
            				setWithLastConsumed(current, "isEmpty", true, "[]");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEmptyArray"


    // $ANTLR start "entryRuleObjectValue"
    // InternalSystemDescriptor.g:567:1: entryRuleObjectValue returns [EObject current=null] : iv_ruleObjectValue= ruleObjectValue EOF ;
    public final EObject entryRuleObjectValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleObjectValue = null;


        try {
            // InternalSystemDescriptor.g:567:52: (iv_ruleObjectValue= ruleObjectValue EOF )
            // InternalSystemDescriptor.g:568:2: iv_ruleObjectValue= ruleObjectValue EOF
            {
             newCompositeNode(grammarAccess.getObjectValueRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleObjectValue=ruleObjectValue();

            state._fsp--;

             current =iv_ruleObjectValue; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleObjectValue"


    // $ANTLR start "ruleObjectValue"
    // InternalSystemDescriptor.g:574:1: ruleObjectValue returns [EObject current=null] : ( ( (lv_value_0_0= RULE_STRING ) ) | this_Object_1= ruleObject | this_Array_2= ruleArray | this_EmptyObject_3= ruleEmptyObject | this_EmptyArray_4= ruleEmptyArray ) ;
    public final EObject ruleObjectValue() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;
        EObject this_Object_1 = null;

        EObject this_Array_2 = null;

        EObject this_EmptyObject_3 = null;

        EObject this_EmptyArray_4 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:580:2: ( ( ( (lv_value_0_0= RULE_STRING ) ) | this_Object_1= ruleObject | this_Array_2= ruleArray | this_EmptyObject_3= ruleEmptyObject | this_EmptyArray_4= ruleEmptyArray ) )
            // InternalSystemDescriptor.g:581:2: ( ( (lv_value_0_0= RULE_STRING ) ) | this_Object_1= ruleObject | this_Array_2= ruleArray | this_EmptyObject_3= ruleEmptyObject | this_EmptyArray_4= ruleEmptyArray )
            {
            // InternalSystemDescriptor.g:581:2: ( ( (lv_value_0_0= RULE_STRING ) ) | this_Object_1= ruleObject | this_Array_2= ruleArray | this_EmptyObject_3= ruleEmptyObject | this_EmptyArray_4= ruleEmptyArray )
            int alt6=5;
            switch ( input.LA(1) ) {
            case RULE_STRING:
                {
                alt6=1;
                }
                break;
            case 14:
                {
                alt6=2;
                }
                break;
            case 18:
                {
                alt6=3;
                }
                break;
            case 20:
                {
                alt6=4;
                }
                break;
            case 21:
                {
                alt6=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // InternalSystemDescriptor.g:582:3: ( (lv_value_0_0= RULE_STRING ) )
                    {
                    // InternalSystemDescriptor.g:582:3: ( (lv_value_0_0= RULE_STRING ) )
                    // InternalSystemDescriptor.g:583:4: (lv_value_0_0= RULE_STRING )
                    {
                    // InternalSystemDescriptor.g:583:4: (lv_value_0_0= RULE_STRING )
                    // InternalSystemDescriptor.g:584:5: lv_value_0_0= RULE_STRING
                    {
                    lv_value_0_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

                    					newLeafNode(lv_value_0_0, grammarAccess.getObjectValueAccess().getValueSTRINGTerminalRuleCall_0_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getObjectValueRule());
                    					}
                    					setWithLastConsumed(
                    						current,
                    						"value",
                    						lv_value_0_0,
                    						"org.eclipse.xtext.common.Terminals.STRING");
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalSystemDescriptor.g:601:3: this_Object_1= ruleObject
                    {

                    			newCompositeNode(grammarAccess.getObjectValueAccess().getObjectParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_Object_1=ruleObject();

                    state._fsp--;


                    			current = this_Object_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalSystemDescriptor.g:610:3: this_Array_2= ruleArray
                    {

                    			newCompositeNode(grammarAccess.getObjectValueAccess().getArrayParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_Array_2=ruleArray();

                    state._fsp--;


                    			current = this_Array_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalSystemDescriptor.g:619:3: this_EmptyObject_3= ruleEmptyObject
                    {

                    			newCompositeNode(grammarAccess.getObjectValueAccess().getEmptyObjectParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_EmptyObject_3=ruleEmptyObject();

                    state._fsp--;


                    			current = this_EmptyObject_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 5 :
                    // InternalSystemDescriptor.g:628:3: this_EmptyArray_4= ruleEmptyArray
                    {

                    			newCompositeNode(grammarAccess.getObjectValueAccess().getEmptyArrayParserRuleCall_4());
                    		
                    pushFollow(FOLLOW_2);
                    this_EmptyArray_4=ruleEmptyArray();

                    state._fsp--;


                    			current = this_EmptyArray_4;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleObjectValue"


    // $ANTLR start "entryRuleTerminalObject"
    // InternalSystemDescriptor.g:640:1: entryRuleTerminalObject returns [EObject current=null] : iv_ruleTerminalObject= ruleTerminalObject EOF ;
    public final EObject entryRuleTerminalObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTerminalObject = null;


        try {
            // InternalSystemDescriptor.g:640:55: (iv_ruleTerminalObject= ruleTerminalObject EOF )
            // InternalSystemDescriptor.g:641:2: iv_ruleTerminalObject= ruleTerminalObject EOF
            {
             newCompositeNode(grammarAccess.getTerminalObjectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTerminalObject=ruleTerminalObject();

            state._fsp--;

             current =iv_ruleTerminalObject; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTerminalObject"


    // $ANTLR start "ruleTerminalObject"
    // InternalSystemDescriptor.g:647:1: ruleTerminalObject returns [EObject current=null] : ( ( (lv_element_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_content_2_0= ruleObjectValue ) ) ) ;
    public final EObject ruleTerminalObject() throws RecognitionException {
        EObject current = null;

        Token lv_element_0_0=null;
        Token otherlv_1=null;
        EObject lv_content_2_0 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:653:2: ( ( ( (lv_element_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_content_2_0= ruleObjectValue ) ) ) )
            // InternalSystemDescriptor.g:654:2: ( ( (lv_element_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_content_2_0= ruleObjectValue ) ) )
            {
            // InternalSystemDescriptor.g:654:2: ( ( (lv_element_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_content_2_0= ruleObjectValue ) ) )
            // InternalSystemDescriptor.g:655:3: ( (lv_element_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_content_2_0= ruleObjectValue ) )
            {
            // InternalSystemDescriptor.g:655:3: ( (lv_element_0_0= RULE_STRING ) )
            // InternalSystemDescriptor.g:656:4: (lv_element_0_0= RULE_STRING )
            {
            // InternalSystemDescriptor.g:656:4: (lv_element_0_0= RULE_STRING )
            // InternalSystemDescriptor.g:657:5: lv_element_0_0= RULE_STRING
            {
            lv_element_0_0=(Token)match(input,RULE_STRING,FOLLOW_13); 

            					newLeafNode(lv_element_0_0, grammarAccess.getTerminalObjectAccess().getElementSTRINGTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getTerminalObjectRule());
            					}
            					setWithLastConsumed(
            						current,
            						"element",
            						lv_element_0_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_1=(Token)match(input,22,FOLLOW_11); 

            			newLeafNode(otherlv_1, grammarAccess.getTerminalObjectAccess().getColonKeyword_1());
            		
            // InternalSystemDescriptor.g:677:3: ( (lv_content_2_0= ruleObjectValue ) )
            // InternalSystemDescriptor.g:678:4: (lv_content_2_0= ruleObjectValue )
            {
            // InternalSystemDescriptor.g:678:4: (lv_content_2_0= ruleObjectValue )
            // InternalSystemDescriptor.g:679:5: lv_content_2_0= ruleObjectValue
            {

            					newCompositeNode(grammarAccess.getTerminalObjectAccess().getContentObjectValueParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_content_2_0=ruleObjectValue();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getTerminalObjectRule());
            					}
            					set(
            						current,
            						"content",
            						lv_content_2_0,
            						"com.ngc.seaside.systemdescriptor.SystemDescriptor.ObjectValue");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTerminalObject"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000012002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000028000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000344020L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x00000000000A0000L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000400000L});

}