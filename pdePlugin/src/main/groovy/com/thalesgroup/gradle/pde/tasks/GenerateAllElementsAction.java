package com.thalesgroup.gradle.pde.tasks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GenerateAllElementsAction {
    
    public static final String FEATURE_TEMPLATE = 
          "        <ant antfile=\"${genericTargets}\" target=\"${target}\">\n"
        + "            <property name=\"type\" value=\"feature\" />\n"
        + "            <property name=\"id\" value=\"feature.name\" />\n"
        + "        </ant>\n";

    public static final String ASSEMBLE_TEMPLATE = 
              "    <property name=\"assemble.feature.name\" value=\"true\" />\n"
            + "    <target name=\"assemble.feature.name\">\n"
            + "        <ant antfile=\"${assembleScriptName}\" dir=\"${buildDirectory}\"/>\n"
            + "    </target>\n";

    public GenerateAllElementsAction() {
    }

    public void generate(String builderDir, String... features) throws IOException {
        StringBuilder featureNames = new StringBuilder();
        StringBuilder featureAssemblies = new StringBuilder();
        
        for (String f : features) {
            featureNames.append(FEATURE_TEMPLATE.replace("feature.name", f));
            featureAssemblies.append(ASSEMBLE_TEMPLATE.replace("feature.name", f));
        }
        
        InputStream allElementsIs = this.getClass().getResourceAsStream("/feature/allElements.xml");
        
        ArrayList<ReplaceElt> replacements = new ArrayList<ReplaceElt>();
        
        replacements.add(new ReplaceElt("gradleFeatureNames", featureNames.toString()));
        replacements.add(new ReplaceElt("gradleFeatureAssemblies", featureAssemblies.toString()));
        
        AntUtil.processResource(allElementsIs, new File(builderDir), "allElements.xml", replacements);
        allElementsIs.close();
    }
}
