/*
 * Autopsy Forensic Browser
 *
 * Copyright 2021 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.mainui.datamodel;

import java.util.Objects;
import org.sleuthkit.datamodel.BlackboardArtifact;

/**
 * Base class for search params for analysis results that filter by set name.
 */
abstract class AnalysisResultSetSearchParam extends AnalysisResultSearchParam {
    
    private final String setName;

    public AnalysisResultSetSearchParam(BlackboardArtifact.Type artifactType, Long dataSourceId, String setName) {
        super(artifactType, dataSourceId);
        this.setName = setName;
    }

    public AnalysisResultSetSearchParam(BlackboardArtifact.Type artifactType, Long dataSourceId, String setName, long startItem, Long maxResultsCount) {
        super(artifactType, dataSourceId, startItem, maxResultsCount);
        this.setName = setName;
    }
    
    public String getSetName() {
        return setName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.getArtifactType());
        hash = 13 * hash + Objects.hashCode(this.getDataSourceId());
        hash = 13 * hash + Objects.hashCode(this.setName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AnalysisResultSetSearchParam other = (AnalysisResultSetSearchParam) obj;
        if (!Objects.equals(this.getArtifactType(), other.getArtifactType())) {
            return false;
        }
        if (!Objects.equals(this.getDataSourceId(), other.getDataSourceId())) {
            return false;
        }
        if (!Objects.equals(this.setName, other.setName)) {
            return false;
        }
        return true;
    }
}
