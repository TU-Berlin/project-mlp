package mlp;

import com.google.common.collect.Multiset;
import mlp.cli.CliParams;
import mlp.cli.ListCommandConfig;
import mlp.cli.MlpCommandConfig;
import mlp.contracts.CreateCandidatesMapper;
import mlp.contracts.TextAnnotatorMapper;
import mlp.pojos.ParsedWikiDocument;
import mlp.pojos.RawWikiDocument;
import mlp.pojos.Relation;
import mlp.pojos.WikiDocumentOutput;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class RelationExtractor {

  public static void main(String[] args) throws Exception {
    String params[] = {"extract", "-in", "C:/tmp/mlp/linear_regression.txt"};
    MlpCommandConfig config = CliParams.from(params).getMlp();
    run(config);
  }

  public static void run(MlpCommandConfig config) throws Exception {
    WikiDocumentOutput output = getWikiDocumentOutput(config);

    try (PrintWriter pw = createPrinter(config)) {
      List<Relation> relations = output.getRelations();

      for (Relation r : relations) {
        pw.println(r.getIdentifier() + "\t" + r.getDefinition() + "\t" + r.getScore());
      }
      pw.flush();
    }
  }

  private static WikiDocumentOutput getWikiDocumentOutput(MlpCommandConfig config) throws Exception {
    TextAnnotatorMapper annotator = new TextAnnotatorMapper(config);
    annotator.open(null);

    String filePath = config.getInput();
    String text = FileUtils.readFileToString(new File(filePath), "UTF-8");
    RawWikiDocument doc = new RawWikiDocument(filePath, 0, text);
    ParsedWikiDocument parsedDocument = annotator.map(doc);

    CreateCandidatesMapper mlp = new CreateCandidatesMapper(config);
    return mlp.map(parsedDocument);
  }

  private static PrintWriter createPrinter(MlpCommandConfig config) throws FileNotFoundException {
    if (StringUtils.isNotBlank(config.getOutput())) {
      return new PrintWriter(new File(config.getOutput()));
    }

    return new PrintWriter(System.out);
  }

  public static void list(ListCommandConfig config) {
    try {
      PrintWriter pw = createPrinter(config);
      WikiDocumentOutput output = getWikiDocumentOutput(config);
      for (Multiset.Entry<String> stringEntry : output.getIdentifiers()) {
        pw.println(stringEntry.getElement());
      }
      pw.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
