package mlp.contracts;

import com.google.common.base.Throwables;
import mlp.cli.FlinkMlpCommandConfig;
import mlp.flink.ListCollector;
import mlp.pojos.*;
import mlp.text.PosTag;
import mlp.text.WikiTextUtils;
import mlp.text.WikiTextUtilsTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

public class TextAnnotatorMapperTest {

  private static final Random RND = new Random();

  public static final TextAnnotatorMapper TEST_INSTANCE = createTestInstance();

  @Test
  public void test() throws Exception {
    final String mathMLExtract = WikiTextUtilsTest.getTestResource("schrödinger_eq.xml").trim();
    List<RawWikiDocument> docs = readWikiTextDocuments("augmentendwikitext.xml");

    RawWikiDocument schroedingerIn = docs.get(0);
    assertTrue( "the seed math tag was not found", schroedingerIn.text.contains(mathMLExtract) );
    MathTag tag = new MathTag(0,mathMLExtract, WikiTextUtils.MathMarkUpType.MATHML);
    String placeholder = tag.placeholder();
    ParsedWikiDocument shroedingerOut = TEST_INSTANCE.map(schroedingerIn);

    Set<String> identifiers = shroedingerOut.getIdentifiers().elementSet();
    assertTrue(identifiers.containsAll(Arrays.asList("Ψ", "V", "h", "λ", "ρ", "τ")));

    List<Formula> formulas = shroedingerOut.getFormulas();
    Formula formula = null;
    for (Formula f : formulas) {
      if (placeholder.equals(f.getKey())) {
        formula = f;
        break;
      }
    }
    //@TODO: reactivate tests
    assertNotNull("the placeholder was not found", formula);
    assertTrue("the placeholder was not part of the sentence", contains(formula, shroedingerOut.getSentences()));
  }

  private static boolean contains(Formula formula, List<Sentence> sentences) {
    Word mathWord = new Word(formula.getKey(), PosTag.MATH);
    for (Sentence sentence : sentences) {
      List<Word> words = sentence.getWords();
      if (words.contains(mathWord)) {
        return true;
      }
    }
    return false;
  }

  public static Formula randomElement(List<Formula> formulas) {
    int idx = RND.nextInt(formulas.size());
    return formulas.get(idx);
  }

  public static List<RawWikiDocument> readWikiTextDocuments(String testFile) throws Exception {
    String rawImput = WikiTextUtilsTest.getTestResource(testFile);
    String[] pages = rawImput.split("</page>");
    TextExtractorMapper textExtractor = new TextExtractorMapper();

    ListCollector<RawWikiDocument> out = new ListCollector<>();
    for (String page : pages) {
      textExtractor.flatMap(page, out);
    }

    return out.getList();
  }

  private static TextAnnotatorMapper createTestInstance() {
    try {
      TextAnnotatorMapper textAnnotator = new TextAnnotatorMapper(FlinkMlpCommandConfig.test());
      textAnnotator.open(null);
      return textAnnotator;
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  @Test
  public void tokenization_formulaSuffexed() throws Exception {
    String text = "The <math>x</math>-axis shows...";
    RawWikiDocument doc = new RawWikiDocument("some doc", 1, text);
    ParsedWikiDocument result = TEST_INSTANCE.map(doc);

    List<Formula> formulas = result.getFormulas();
    assertEquals(1, formulas.size());

    Sentence sentence = result.getSentences().get(0);

    List<Word> expected = Arrays.asList(new Word("The", "DT"), new Word("x", "ID"), new Word("-axis",
      "-SUF"), new Word("shows", "VBZ"), new Word("...", ":"));
    assertEquals(expected, sentence.getWords());
  }

}
