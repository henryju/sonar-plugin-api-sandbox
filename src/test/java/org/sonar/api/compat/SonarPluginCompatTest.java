package org.sonar.api.compat;

import org.sonar.api.compat.internal.Version;

import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

public class SonarPluginCompatTest {

  @Test
  public void testGetVersion() {
    assertThat(SonarPluginCompat.getSonarQubeRuntimeVersion()).isEqualTo(Version.create("4.5.1"));
  }

}
