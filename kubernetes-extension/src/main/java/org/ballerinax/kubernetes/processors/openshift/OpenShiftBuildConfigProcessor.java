/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinax.kubernetes.processors.openshift;

import org.ballerinalang.model.tree.AnnotationAttachmentNode;
import org.ballerinalang.model.tree.FunctionNode;
import org.ballerinalang.model.tree.IdentifierNode;
import org.ballerinalang.model.tree.ServiceNode;
import org.ballerinalang.model.tree.SimpleVariableNode;
import org.ballerinax.kubernetes.exceptions.KubernetesPluginException;
import org.ballerinax.kubernetes.models.KubernetesContext;
import org.ballerinax.kubernetes.models.openshift.OpenShiftBuildConfigModel;
import org.ballerinax.kubernetes.processors.AbstractAnnotationProcessor;
import org.wso2.ballerinalang.compiler.tree.BLangAnnotationAttachment;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangRecordLiteral;

import java.util.List;

import static org.ballerinax.kubernetes.KubernetesConstants.OPENSHIFT_BUILD_CONFIG_POSTFIX;
import static org.ballerinax.kubernetes.utils.KubernetesUtils.getMap;
import static org.ballerinax.kubernetes.utils.KubernetesUtils.getValidName;
import static org.ballerinax.kubernetes.utils.KubernetesUtils.isBlank;
import static org.ballerinax.kubernetes.utils.KubernetesUtils.resolveValue;

/**
 * Annotation processor for OpenShift's Build Config.
 */
public class OpenShiftBuildConfigProcessor extends AbstractAnnotationProcessor {
    @Override
    public void processAnnotation(ServiceNode serviceNode, AnnotationAttachmentNode attachmentNode)
            throws KubernetesPluginException {
        processBuildConfigAnnotation(serviceNode.getName(), attachmentNode);
    }
    
    @Override
    public void processAnnotation(SimpleVariableNode variableNode, AnnotationAttachmentNode attachmentNode)
            throws KubernetesPluginException {
        processBuildConfigAnnotation(variableNode.getName(), attachmentNode);
    }
    
    @Override
    public void processAnnotation(FunctionNode functionNode, AnnotationAttachmentNode attachmentNode)
            throws KubernetesPluginException {
        processBuildConfigAnnotation(functionNode.getName(), attachmentNode);
    }
    
    /**
     * Process @kubernetes:OpenShiftBuildConfig annotation.
     *
     * @param identifierNode Node of which the annotation was attached to.
     * @param attachmentNode The annotation node.
     * @throws KubernetesPluginException Unable to process annotations.
     */
    private void processBuildConfigAnnotation(IdentifierNode identifierNode, AnnotationAttachmentNode attachmentNode)
            throws KubernetesPluginException {
    
        if (null == KubernetesContext.getInstance().getDataHolder().getOpenShiftBuildConfigModel()) {
    
            List<BLangRecordLiteral.BLangRecordKeyValue> bcFields =
                    ((BLangRecordLiteral) ((BLangAnnotationAttachment) attachmentNode).expr).getKeyValuePairs();
    
            OpenShiftBuildConfigModel openShiftBC = new OpenShiftBuildConfigModel();
            for (BLangRecordLiteral.BLangRecordKeyValue bcField : bcFields) {
                switch (OpenShiftBuildConfigFields.valueOf(bcField.getKey().toString())) {
                    case name:
                        openShiftBC.setName(getValidName(resolveValue(bcField.getValue().toString())));
                        break;
                    case namespace:
                        openShiftBC.setNamespace(resolveValue(bcField.getValue().toString()));
                        break;
                    case labels:
                        BLangRecordLiteral labelsField = (BLangRecordLiteral) bcField.getValue();
                        openShiftBC.setLabels(getMap(labelsField.getKeyValuePairs()));
                        break;
                    case annotations:
                        BLangRecordLiteral annotationsField = (BLangRecordLiteral) bcField.getValue();
                        openShiftBC.setAnnotations(getMap(annotationsField.getKeyValuePairs()));
                        break;
                    case dockerRegistry:
                        openShiftBC.setDockerRegistry(resolveValue(bcField.getValue().toString()));
                        break;
                    case generateImageStream:
                        openShiftBC.setGenerateImageStream(Boolean.valueOf(resolveValue(
                                bcField.getValue().toString())));
                        break;
                    case forcePullDockerImage:
                        openShiftBC.setForcePullDockerImage(Boolean.valueOf(resolveValue(
                                bcField.getValue().toString())));
                        break;
                    case buildDockerWithNoCache:
                        openShiftBC.setBuildDockerWithNoCache(Boolean.valueOf(resolveValue(
                                bcField.getValue().toString())));
                        break;
                    default:
                        throw new KubernetesPluginException("Unknown field found " +
                                                            "for @kubernetes:OpenShiftBuildConfig{} annotation.");
                }
            }
    
            if (isBlank(openShiftBC.getName())) {
                openShiftBC.setName(getValidName(identifierNode.getValue()) + OPENSHIFT_BUILD_CONFIG_POSTFIX);
            }
    
            KubernetesContext.getInstance().getDataHolder().setOpenShiftBuildConfigModel(openShiftBC);
        } else {
            throw new KubernetesPluginException("A module or a ballerina file can only have one " +
                                                "@kubernetes:OpenShiftBuildConfig{} annotation.");
        }
    }
    
    private enum OpenShiftBuildConfigFields {
        name,
        namespace,
        labels,
        annotations,
        dockerRegistry,
        generateImageStream,
        forcePullDockerImage,
        buildDockerWithNoCache
    }
}
