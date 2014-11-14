package org.sonar.api.compat;

import org.sonar.api.compat.internal.AnalyzerOptimizer;
import org.sonar.api.compat.internal.BlockCacheCompat;
import org.sonar.api.compat.internal.DuplicationCacheCompat;
import org.sonar.api.compat.internal.SensorContextCompatProvider;
import org.sonar.api.compat.internal.Version;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class SonarPluginCompat extends org.sonar.api.SonarPlugin {

  public final List getExtensions() {
    return filter(doGetExtensions());
  }

  private List filter(List doGetExtensions) {
    List extensions = new ArrayList();
    for (Object extension : doGetExtensions) {
      extensions.add(extension);
    }
    extensions.add(AnalyzerOptimizer.class);
    extensions.add(BlockCacheCompat.class);
    extensions.add(DuplicationCacheCompat.class);
    extensions.add(SensorContextCompatProvider.class);
    return extensions;
  }

  public abstract List doGetExtensions();

  static Version getSonarQubeRuntimeVersion() {
    InputStream propsStream = SonarPluginCompat.class.getResourceAsStream("/META-INF/maven/org.codehaus.sonar/sonar-plugin-api/pom.properties");
    try {
      Properties props = new Properties();
      props.load(propsStream);
      return Version.create(props.getProperty("version"));
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read SQ version", e);
    } finally {
      IOUtils.closeQuietly(propsStream);
    }
  }
}
