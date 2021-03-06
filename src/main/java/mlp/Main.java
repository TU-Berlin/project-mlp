package mlp;

import mlp.cli.CliParams;
import mlp.text.TokenCounter;


/**
 * Created by Moritz on 27.09.2015.
 */
public class Main {

  public static void main(String[] args) throws Exception {
    CliParams params = CliParams.from(args);
    String command = params.getCommand();

    if ("help".equals(command)) {
      params.printHelp();
    } else if ("count".equals(command)) {
      TokenCounter.run(params.getCount());
    } else if ("list".equals(command)) {
      RelationExtractor.list(params.getList());
    } else if ("extract".equals(command)) {
      RelationExtractor.run(params.getMlp());
    } else if ("mlp".equals(command)) {
      FlinkMlpRelationFinder.run(params.getFlinkMlp());
    } else {
      params.printHelp();
    }
  }
}
