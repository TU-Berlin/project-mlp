package mlp.cli;

import mlp.cli.FlinkMlpCommandConfig;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MlpCommandConfigTest {

  @Test
  public void test() {
    String[] args = {"-in", "c:/tmp/mlp/input/", "-out", "c:/tmp/mlp/output/"};
    FlinkMlpCommandConfig config = FlinkMlpCommandConfig.from(args);
    assertEquals("c:/tmp/mlp/input/", config.getDataset());
    assertEquals("c:/tmp/mlp/output/", config.getOutputDir());
  }

}
