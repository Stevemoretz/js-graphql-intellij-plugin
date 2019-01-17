/*
 * Copyright (c) 2018-present, Jim Kynde Meyer
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.intellij.lang.jsgraphql.ide.editor;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.icons.AllIcons;
import com.intellij.json.psi.JsonArray;
import com.intellij.json.psi.JsonObject;
import com.intellij.json.psi.JsonProperty;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

import static com.intellij.lang.jsgraphql.ide.editor.GraphQLIntrospectionHelper.createOrUpdateIntrospectionSDLFile;
import static com.intellij.lang.jsgraphql.ide.editor.GraphQLIntrospectionHelper.printIntrospectionJsonAsGraphQL;

/**
 * Line marker which shows an action to turn a GraphQL Introspection JSON result into a GraphQL schema expressed in GraphQL SDL.
 */
public class GraphQLIntrospectionJsonToSDLLineMarkerProvider implements LineMarkerProvider {
    @Nullable
    @Override
    @SuppressWarnings(value = "unchecked")
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        if (element instanceof JsonProperty) {
            if (PsiTreeUtil.getParentOfType(element, JsonProperty.class) == null) {
                // top level property
                final JsonProperty jsonProperty = (JsonProperty) element;
                final String propertyName = jsonProperty.getName();
                if ("__schema".equals(propertyName) && jsonProperty.getValue() instanceof JsonObject) {
                    for (JsonProperty property : ((JsonObject) jsonProperty.getValue()).getPropertyList()) {
                        if ("types".equals(property.getName()) && property.getValue() instanceof JsonArray) {
                            // likely a GraphQL schema with a { __schema: { types: [] } }
                            return new LineMarkerInfo<>(jsonProperty, jsonProperty.getTextRange(), AllIcons.General.Run, Pass.UPDATE_ALL, o -> "Generate GraphQL SDL schema file", (e, elt) -> {
                                try {
                                    final String introspectionJson = element.getContainingFile().getText();
                                    final String schemaAsSDL = printIntrospectionJsonAsGraphQL(introspectionJson);

                                    final VirtualFile jsonFile = element.getContainingFile().getVirtualFile();
                                    final String outputFileName = jsonFile.getName() + ".graphql";
                                    final Project project = element.getProject();

                                    createOrUpdateIntrospectionSDLFile(schemaAsSDL, jsonFile, outputFileName, project);

                                } catch (Exception exception) {
                                    Notifications.Bus.notify(new Notification("GraphQL", "Unable to create GraphQL SDL", exception.getMessage(), NotificationType.ERROR));
                                }
                            }, GutterIconRenderer.Alignment.CENTER);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }
}