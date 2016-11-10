/**
 * Copyright (c) 2015-present, Jim Kynde Meyer
 * All rights reserved.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.intellij.lang.jsgraphql.endpoint.doc.lexer;
import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import static com.intellij.lang.jsgraphql.endpoint.doc.JSGraphQLEndpointDocTokenTypes.*;

%%

%{

    public JSGraphQLEndpointDocLexer() {
        this((java.io.Reader)null);
    }
%}

%public
%class JSGraphQLEndpointDocLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL="\r"|"\n"|"\r\n"
LINE_WS=[\ \t\f]
WHITE_SPACE=({LINE_WS}|{EOL})+
docName=@([_A-Za-z][_0-9A-Za-z]*)?
docText=[^\s]+

%state NAMED_TEXT
%state DOCUMENTATION

%%
<YYINITIAL> {
  {docName}             { yybegin(NAMED_TEXT); return DOCNAME; }
  {docText}             { return DOCTEXT; }
  {WHITE_SPACE}         { return com.intellij.psi.TokenType.WHITE_SPACE; }
}

<NAMED_TEXT> {
  {docText}             { yybegin(DOCUMENTATION); return DOCVALUE; }
  {WHITE_SPACE}         { return com.intellij.psi.TokenType.WHITE_SPACE; }
}

<DOCUMENTATION> {
  {docText}             { return DOCTEXT; }
  {WHITE_SPACE}         { return com.intellij.psi.TokenType.WHITE_SPACE; }
}