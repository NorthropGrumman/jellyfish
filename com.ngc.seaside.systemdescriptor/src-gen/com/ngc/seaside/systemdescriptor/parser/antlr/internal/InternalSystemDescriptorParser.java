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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_STRING", "RULE_ID", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'metadata'", "'{'", "','", "'}'", "'['", "']'", "'{}'", "'[]'", "':'"
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

        public InternalSystemDescriptorParser(TokenStream input, SystemDescriptorGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "Model";
       	}

       	@Override
       	protected SystemDescriptorGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleModel"
    // InternalSystemDescriptor.g:64:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalSystemDescriptor.g:64:46: (iv_ruleModel= ruleModel EOF )
            // InternalSystemDescriptor.g:65:2: iv_ruleModel= ruleModel EOF
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
    // InternalSystemDescriptor.g:71:1: ruleModel returns [EObject current=null] : ( (lv_greetings_0_0= ruleMetadata ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_greetings_0_0 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:77:2: ( ( (lv_greetings_0_0= ruleMetadata ) )* )
            // InternalSystemDescriptor.g:78:2: ( (lv_greetings_0_0= ruleMetadata ) )*
            {
            // InternalSystemDescriptor.g:78:2: ( (lv_greetings_0_0= ruleMetadata ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==11) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalSystemDescriptor.g:79:3: (lv_greetings_0_0= ruleMetadata )
            	    {
            	    // InternalSystemDescriptor.g:79:3: (lv_greetings_0_0= ruleMetadata )
            	    // InternalSystemDescriptor.g:80:4: lv_greetings_0_0= ruleMetadata
            	    {

            	    				newCompositeNode(grammarAccess.getModelAccess().getGreetingsMetadataParserRuleCall_0());
            	    			
            	    pushFollow(FOLLOW_3);
            	    lv_greetings_0_0=ruleMetadata();

            	    state._fsp--;


            	    				if (current==null) {
            	    					current = createModelElementForParent(grammarAccess.getModelRule());
            	    				}
            	    				add(
            	    					current,
            	    					"greetings",
            	    					lv_greetings_0_0,
            	    					"com.ngc.seaside.systemdescriptor.SystemDescriptor.Metadata");
            	    				afterParserOrEnumRuleCall();
            	    			

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


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


    // $ANTLR start "entryRuleMetadata"
    // InternalSystemDescriptor.g:100:1: entryRuleMetadata returns [EObject current=null] : iv_ruleMetadata= ruleMetadata EOF ;
    public final EObject entryRuleMetadata() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMetadata = null;


        try {
            // InternalSystemDescriptor.g:100:49: (iv_ruleMetadata= ruleMetadata EOF )
            // InternalSystemDescriptor.g:101:2: iv_ruleMetadata= ruleMetadata EOF
            {
             newCompositeNode(grammarAccess.getMetadataRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMetadata=ruleMetadata();

            state._fsp--;

             current =iv_ruleMetadata; 
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
    // $ANTLR end "entryRuleMetadata"


    // $ANTLR start "ruleMetadata"
    // InternalSystemDescriptor.g:107:1: ruleMetadata returns [EObject current=null] : ( ( (lv_type_0_0= 'metadata' ) ) ( (lv_json_1_0= ruleObject ) )? ) ;
    public final EObject ruleMetadata() throws RecognitionException {
        EObject current = null;

        Token lv_type_0_0=null;
        EObject lv_json_1_0 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:113:2: ( ( ( (lv_type_0_0= 'metadata' ) ) ( (lv_json_1_0= ruleObject ) )? ) )
            // InternalSystemDescriptor.g:114:2: ( ( (lv_type_0_0= 'metadata' ) ) ( (lv_json_1_0= ruleObject ) )? )
            {
            // InternalSystemDescriptor.g:114:2: ( ( (lv_type_0_0= 'metadata' ) ) ( (lv_json_1_0= ruleObject ) )? )
            // InternalSystemDescriptor.g:115:3: ( (lv_type_0_0= 'metadata' ) ) ( (lv_json_1_0= ruleObject ) )?
            {
            // InternalSystemDescriptor.g:115:3: ( (lv_type_0_0= 'metadata' ) )
            // InternalSystemDescriptor.g:116:4: (lv_type_0_0= 'metadata' )
            {
            // InternalSystemDescriptor.g:116:4: (lv_type_0_0= 'metadata' )
            // InternalSystemDescriptor.g:117:5: lv_type_0_0= 'metadata'
            {
            lv_type_0_0=(Token)match(input,11,FOLLOW_4); 

            					newLeafNode(lv_type_0_0, grammarAccess.getMetadataAccess().getTypeMetadataKeyword_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getMetadataRule());
            					}
            					setWithLastConsumed(current, "type", lv_type_0_0, "metadata");
            				

            }


            }

            // InternalSystemDescriptor.g:129:3: ( (lv_json_1_0= ruleObject ) )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==12) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // InternalSystemDescriptor.g:130:4: (lv_json_1_0= ruleObject )
                    {
                    // InternalSystemDescriptor.g:130:4: (lv_json_1_0= ruleObject )
                    // InternalSystemDescriptor.g:131:5: lv_json_1_0= ruleObject
                    {

                    					newCompositeNode(grammarAccess.getMetadataAccess().getJsonObjectParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_json_1_0=ruleObject();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getMetadataRule());
                    					}
                    					set(
                    						current,
                    						"json",
                    						lv_json_1_0,
                    						"com.ngc.seaside.systemdescriptor.SystemDescriptor.Object");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

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
    // $ANTLR end "ruleMetadata"


    // $ANTLR start "entryRuleObject"
    // InternalSystemDescriptor.g:152:1: entryRuleObject returns [EObject current=null] : iv_ruleObject= ruleObject EOF ;
    public final EObject entryRuleObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleObject = null;


        try {
            // InternalSystemDescriptor.g:152:47: (iv_ruleObject= ruleObject EOF )
            // InternalSystemDescriptor.g:153:2: iv_ruleObject= ruleObject EOF
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
    // InternalSystemDescriptor.g:159:1: ruleObject returns [EObject current=null] : (otherlv_0= '{' ( (lv_firstObject_1_0= ruleTerminalObject ) ) (otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) ) )* otherlv_4= '}' ) ;
    public final EObject ruleObject() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_firstObject_1_0 = null;

        EObject lv_objects_3_0 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:165:2: ( (otherlv_0= '{' ( (lv_firstObject_1_0= ruleTerminalObject ) ) (otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) ) )* otherlv_4= '}' ) )
            // InternalSystemDescriptor.g:166:2: (otherlv_0= '{' ( (lv_firstObject_1_0= ruleTerminalObject ) ) (otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) ) )* otherlv_4= '}' )
            {
            // InternalSystemDescriptor.g:166:2: (otherlv_0= '{' ( (lv_firstObject_1_0= ruleTerminalObject ) ) (otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) ) )* otherlv_4= '}' )
            // InternalSystemDescriptor.g:167:3: otherlv_0= '{' ( (lv_firstObject_1_0= ruleTerminalObject ) ) (otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) ) )* otherlv_4= '}'
            {
            otherlv_0=(Token)match(input,12,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getObjectAccess().getLeftCurlyBracketKeyword_0());
            		
            // InternalSystemDescriptor.g:171:3: ( (lv_firstObject_1_0= ruleTerminalObject ) )
            // InternalSystemDescriptor.g:172:4: (lv_firstObject_1_0= ruleTerminalObject )
            {
            // InternalSystemDescriptor.g:172:4: (lv_firstObject_1_0= ruleTerminalObject )
            // InternalSystemDescriptor.g:173:5: lv_firstObject_1_0= ruleTerminalObject
            {

            					newCompositeNode(grammarAccess.getObjectAccess().getFirstObjectTerminalObjectParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_6);
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

            // InternalSystemDescriptor.g:190:3: (otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==13) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalSystemDescriptor.g:191:4: otherlv_2= ',' ( (lv_objects_3_0= ruleTerminalObject ) )
            	    {
            	    otherlv_2=(Token)match(input,13,FOLLOW_5); 

            	    				newLeafNode(otherlv_2, grammarAccess.getObjectAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalSystemDescriptor.g:195:4: ( (lv_objects_3_0= ruleTerminalObject ) )
            	    // InternalSystemDescriptor.g:196:5: (lv_objects_3_0= ruleTerminalObject )
            	    {
            	    // InternalSystemDescriptor.g:196:5: (lv_objects_3_0= ruleTerminalObject )
            	    // InternalSystemDescriptor.g:197:6: lv_objects_3_0= ruleTerminalObject
            	    {

            	    						newCompositeNode(grammarAccess.getObjectAccess().getObjectsTerminalObjectParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_6);
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
            	    break loop3;
                }
            } while (true);

            otherlv_4=(Token)match(input,14,FOLLOW_2); 

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
    // InternalSystemDescriptor.g:223:1: entryRuleArray returns [EObject current=null] : iv_ruleArray= ruleArray EOF ;
    public final EObject entryRuleArray() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleArray = null;


        try {
            // InternalSystemDescriptor.g:223:46: (iv_ruleArray= ruleArray EOF )
            // InternalSystemDescriptor.g:224:2: iv_ruleArray= ruleArray EOF
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
    // InternalSystemDescriptor.g:230:1: ruleArray returns [EObject current=null] : (otherlv_0= '[' ( (lv_firstItem_1_0= ruleObjectValue ) ) (otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) ) )* otherlv_4= ']' ) ;
    public final EObject ruleArray() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_firstItem_1_0 = null;

        EObject lv_items_3_0 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:236:2: ( (otherlv_0= '[' ( (lv_firstItem_1_0= ruleObjectValue ) ) (otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) ) )* otherlv_4= ']' ) )
            // InternalSystemDescriptor.g:237:2: (otherlv_0= '[' ( (lv_firstItem_1_0= ruleObjectValue ) ) (otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) ) )* otherlv_4= ']' )
            {
            // InternalSystemDescriptor.g:237:2: (otherlv_0= '[' ( (lv_firstItem_1_0= ruleObjectValue ) ) (otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) ) )* otherlv_4= ']' )
            // InternalSystemDescriptor.g:238:3: otherlv_0= '[' ( (lv_firstItem_1_0= ruleObjectValue ) ) (otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) ) )* otherlv_4= ']'
            {
            otherlv_0=(Token)match(input,15,FOLLOW_7); 

            			newLeafNode(otherlv_0, grammarAccess.getArrayAccess().getLeftSquareBracketKeyword_0());
            		
            // InternalSystemDescriptor.g:242:3: ( (lv_firstItem_1_0= ruleObjectValue ) )
            // InternalSystemDescriptor.g:243:4: (lv_firstItem_1_0= ruleObjectValue )
            {
            // InternalSystemDescriptor.g:243:4: (lv_firstItem_1_0= ruleObjectValue )
            // InternalSystemDescriptor.g:244:5: lv_firstItem_1_0= ruleObjectValue
            {

            					newCompositeNode(grammarAccess.getArrayAccess().getFirstItemObjectValueParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_8);
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

            // InternalSystemDescriptor.g:261:3: (otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==13) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalSystemDescriptor.g:262:4: otherlv_2= ',' ( (lv_items_3_0= ruleObjectValue ) )
            	    {
            	    otherlv_2=(Token)match(input,13,FOLLOW_7); 

            	    				newLeafNode(otherlv_2, grammarAccess.getArrayAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalSystemDescriptor.g:266:4: ( (lv_items_3_0= ruleObjectValue ) )
            	    // InternalSystemDescriptor.g:267:5: (lv_items_3_0= ruleObjectValue )
            	    {
            	    // InternalSystemDescriptor.g:267:5: (lv_items_3_0= ruleObjectValue )
            	    // InternalSystemDescriptor.g:268:6: lv_items_3_0= ruleObjectValue
            	    {

            	    						newCompositeNode(grammarAccess.getArrayAccess().getItemsObjectValueParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_8);
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
            	    break loop4;
                }
            } while (true);

            otherlv_4=(Token)match(input,16,FOLLOW_2); 

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
    // InternalSystemDescriptor.g:294:1: entryRuleEmptyObject returns [EObject current=null] : iv_ruleEmptyObject= ruleEmptyObject EOF ;
    public final EObject entryRuleEmptyObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEmptyObject = null;


        try {
            // InternalSystemDescriptor.g:294:52: (iv_ruleEmptyObject= ruleEmptyObject EOF )
            // InternalSystemDescriptor.g:295:2: iv_ruleEmptyObject= ruleEmptyObject EOF
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
    // InternalSystemDescriptor.g:301:1: ruleEmptyObject returns [EObject current=null] : ( (lv_isEmpty_0_0= '{}' ) ) ;
    public final EObject ruleEmptyObject() throws RecognitionException {
        EObject current = null;

        Token lv_isEmpty_0_0=null;


        	enterRule();

        try {
            // InternalSystemDescriptor.g:307:2: ( ( (lv_isEmpty_0_0= '{}' ) ) )
            // InternalSystemDescriptor.g:308:2: ( (lv_isEmpty_0_0= '{}' ) )
            {
            // InternalSystemDescriptor.g:308:2: ( (lv_isEmpty_0_0= '{}' ) )
            // InternalSystemDescriptor.g:309:3: (lv_isEmpty_0_0= '{}' )
            {
            // InternalSystemDescriptor.g:309:3: (lv_isEmpty_0_0= '{}' )
            // InternalSystemDescriptor.g:310:4: lv_isEmpty_0_0= '{}'
            {
            lv_isEmpty_0_0=(Token)match(input,17,FOLLOW_2); 

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
    // InternalSystemDescriptor.g:325:1: entryRuleEmptyArray returns [EObject current=null] : iv_ruleEmptyArray= ruleEmptyArray EOF ;
    public final EObject entryRuleEmptyArray() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEmptyArray = null;


        try {
            // InternalSystemDescriptor.g:325:51: (iv_ruleEmptyArray= ruleEmptyArray EOF )
            // InternalSystemDescriptor.g:326:2: iv_ruleEmptyArray= ruleEmptyArray EOF
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
    // InternalSystemDescriptor.g:332:1: ruleEmptyArray returns [EObject current=null] : ( (lv_isEmpty_0_0= '[]' ) ) ;
    public final EObject ruleEmptyArray() throws RecognitionException {
        EObject current = null;

        Token lv_isEmpty_0_0=null;


        	enterRule();

        try {
            // InternalSystemDescriptor.g:338:2: ( ( (lv_isEmpty_0_0= '[]' ) ) )
            // InternalSystemDescriptor.g:339:2: ( (lv_isEmpty_0_0= '[]' ) )
            {
            // InternalSystemDescriptor.g:339:2: ( (lv_isEmpty_0_0= '[]' ) )
            // InternalSystemDescriptor.g:340:3: (lv_isEmpty_0_0= '[]' )
            {
            // InternalSystemDescriptor.g:340:3: (lv_isEmpty_0_0= '[]' )
            // InternalSystemDescriptor.g:341:4: lv_isEmpty_0_0= '[]'
            {
            lv_isEmpty_0_0=(Token)match(input,18,FOLLOW_2); 

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
    // InternalSystemDescriptor.g:356:1: entryRuleObjectValue returns [EObject current=null] : iv_ruleObjectValue= ruleObjectValue EOF ;
    public final EObject entryRuleObjectValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleObjectValue = null;


        try {
            // InternalSystemDescriptor.g:356:52: (iv_ruleObjectValue= ruleObjectValue EOF )
            // InternalSystemDescriptor.g:357:2: iv_ruleObjectValue= ruleObjectValue EOF
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
    // InternalSystemDescriptor.g:363:1: ruleObjectValue returns [EObject current=null] : ( ( (lv_value_0_0= RULE_STRING ) ) | this_Object_1= ruleObject | this_Array_2= ruleArray | this_EmptyObject_3= ruleEmptyObject | this_EmptyArray_4= ruleEmptyArray ) ;
    public final EObject ruleObjectValue() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;
        EObject this_Object_1 = null;

        EObject this_Array_2 = null;

        EObject this_EmptyObject_3 = null;

        EObject this_EmptyArray_4 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:369:2: ( ( ( (lv_value_0_0= RULE_STRING ) ) | this_Object_1= ruleObject | this_Array_2= ruleArray | this_EmptyObject_3= ruleEmptyObject | this_EmptyArray_4= ruleEmptyArray ) )
            // InternalSystemDescriptor.g:370:2: ( ( (lv_value_0_0= RULE_STRING ) ) | this_Object_1= ruleObject | this_Array_2= ruleArray | this_EmptyObject_3= ruleEmptyObject | this_EmptyArray_4= ruleEmptyArray )
            {
            // InternalSystemDescriptor.g:370:2: ( ( (lv_value_0_0= RULE_STRING ) ) | this_Object_1= ruleObject | this_Array_2= ruleArray | this_EmptyObject_3= ruleEmptyObject | this_EmptyArray_4= ruleEmptyArray )
            int alt5=5;
            switch ( input.LA(1) ) {
            case RULE_STRING:
                {
                alt5=1;
                }
                break;
            case 12:
                {
                alt5=2;
                }
                break;
            case 15:
                {
                alt5=3;
                }
                break;
            case 17:
                {
                alt5=4;
                }
                break;
            case 18:
                {
                alt5=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // InternalSystemDescriptor.g:371:3: ( (lv_value_0_0= RULE_STRING ) )
                    {
                    // InternalSystemDescriptor.g:371:3: ( (lv_value_0_0= RULE_STRING ) )
                    // InternalSystemDescriptor.g:372:4: (lv_value_0_0= RULE_STRING )
                    {
                    // InternalSystemDescriptor.g:372:4: (lv_value_0_0= RULE_STRING )
                    // InternalSystemDescriptor.g:373:5: lv_value_0_0= RULE_STRING
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
                    // InternalSystemDescriptor.g:390:3: this_Object_1= ruleObject
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
                    // InternalSystemDescriptor.g:399:3: this_Array_2= ruleArray
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
                    // InternalSystemDescriptor.g:408:3: this_EmptyObject_3= ruleEmptyObject
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
                    // InternalSystemDescriptor.g:417:3: this_EmptyArray_4= ruleEmptyArray
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
    // InternalSystemDescriptor.g:429:1: entryRuleTerminalObject returns [EObject current=null] : iv_ruleTerminalObject= ruleTerminalObject EOF ;
    public final EObject entryRuleTerminalObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTerminalObject = null;


        try {
            // InternalSystemDescriptor.g:429:55: (iv_ruleTerminalObject= ruleTerminalObject EOF )
            // InternalSystemDescriptor.g:430:2: iv_ruleTerminalObject= ruleTerminalObject EOF
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
    // InternalSystemDescriptor.g:436:1: ruleTerminalObject returns [EObject current=null] : ( ( (lv_element_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_content_2_0= ruleObjectValue ) ) ) ;
    public final EObject ruleTerminalObject() throws RecognitionException {
        EObject current = null;

        Token lv_element_0_0=null;
        Token otherlv_1=null;
        EObject lv_content_2_0 = null;



        	enterRule();

        try {
            // InternalSystemDescriptor.g:442:2: ( ( ( (lv_element_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_content_2_0= ruleObjectValue ) ) ) )
            // InternalSystemDescriptor.g:443:2: ( ( (lv_element_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_content_2_0= ruleObjectValue ) ) )
            {
            // InternalSystemDescriptor.g:443:2: ( ( (lv_element_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_content_2_0= ruleObjectValue ) ) )
            // InternalSystemDescriptor.g:444:3: ( (lv_element_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_content_2_0= ruleObjectValue ) )
            {
            // InternalSystemDescriptor.g:444:3: ( (lv_element_0_0= RULE_STRING ) )
            // InternalSystemDescriptor.g:445:4: (lv_element_0_0= RULE_STRING )
            {
            // InternalSystemDescriptor.g:445:4: (lv_element_0_0= RULE_STRING )
            // InternalSystemDescriptor.g:446:5: lv_element_0_0= RULE_STRING
            {
            lv_element_0_0=(Token)match(input,RULE_STRING,FOLLOW_9); 

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

            otherlv_1=(Token)match(input,19,FOLLOW_7); 

            			newLeafNode(otherlv_1, grammarAccess.getTerminalObjectAccess().getColonKeyword_1());
            		
            // InternalSystemDescriptor.g:466:3: ( (lv_content_2_0= ruleObjectValue ) )
            // InternalSystemDescriptor.g:467:4: (lv_content_2_0= ruleObjectValue )
            {
            // InternalSystemDescriptor.g:467:4: (lv_content_2_0= ruleObjectValue )
            // InternalSystemDescriptor.g:468:5: lv_content_2_0= ruleObjectValue
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
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000069010L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000012000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000080000L});

}