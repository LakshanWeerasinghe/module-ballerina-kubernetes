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

package org.ballerinax.kubernetes.models.openshift;

import org.ballerinax.kubernetes.models.KubernetesModel;

/**
 * Model class for OpenShift's Build Configs.
 */
public class OpenShiftBuildConfigModel extends KubernetesModel {
    private String dockerRegistry = null;
    private boolean generateImageStream = true;
    private boolean forcePullDockerImage = false;
    private boolean buildDockerWithNoCache = false;
    
    public boolean isGenerateImageStream() {
        return generateImageStream;
    }
    
    public void setGenerateImageStream(boolean generateImageStream) {
        this.generateImageStream = generateImageStream;
    }
    
    public boolean isForcePullDockerImage() {
        return forcePullDockerImage;
    }
    
    public void setForcePullDockerImage(boolean forcePullDockerImage) {
        this.forcePullDockerImage = forcePullDockerImage;
    }
    
    public boolean isBuildDockerWithNoCache() {
        return buildDockerWithNoCache;
    }
    
    public void setBuildDockerWithNoCache(boolean buildDockerWithNoCache) {
        this.buildDockerWithNoCache = buildDockerWithNoCache;
    }
    
    public String getDockerRegistry() {
        return dockerRegistry;
    }
    
    public void setDockerRegistry(String dockerRegistry) {
        this.dockerRegistry = dockerRegistry;
    }
}
