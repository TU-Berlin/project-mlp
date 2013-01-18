/*        __
 *        \ \
 *   _   _ \ \  ______
 *  | | | | > \(  __  )
 *  | |_| |/ ^ \| || |
 *  | ._,_/_/ \_\_||_|
 *  | |
 *  |_|
 * 
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <rob ∂ CLABS dot CC> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return.
 * ----------------------------------------------------------------------------
 */
package cc.clabs.stratosphere.mlp.contracts;

import cc.clabs.stratosphere.mlp.types.PactSentence;
import cc.clabs.stratosphere.mlp.types.PactWord;
import cc.clabs.stratosphere.mlp.utils.SentenceUtils;
import eu.stratosphere.pact.common.stubs.Collector;
import eu.stratosphere.pact.common.stubs.MapStub;
import eu.stratosphere.pact.common.type.PactRecord;
import eu.stratosphere.pact.common.type.base.PactString;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import eu.stratosphere.nephele.configuration.Configuration;
import eu.stratosphere.pact.common.type.base.PactInteger;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rob
 */
public class SentenceEmitter extends MapStub {
    
    MaxentTagger tagger = null;
        
    private final PactRecord output = new PactRecord();

    @Override
    public void open(Configuration parameter) throws Exception {
      super.open( parameter );
      String model = parameter.getString( "POS-MODEL", "models/wsj-0-18-left3words-distsim.tagger" );
      tagger = new MaxentTagger( model );
    }
    
    @Override
    public void map( PactRecord record, Collector<PactRecord> collector ) {
        // field 0 remains the same (id of the document)
        output.setField( 0, record.getField( 0, PactInteger.class ) );       
        String plaintext = record.getField( 1, PactString.class ).getValue();        
                        
        // tokenize the plaintext
        for ( List<HasWord> tokens : MaxentTagger.tokenizeText( new StringReader( plaintext ) ) ) {
            // for each detected sentence
            PactSentence sentence = new PactSentence();
            // populate a wordlist
            for ( TaggedWord word : tagger.tagSentence( tokens ) )
                sentence.add( new PactWord( word ) );
            sentence = SentenceUtils.joinByTagPattern( sentence, "\" * \"", "ENTITY" );
            // emit the sentence
            output.setField( 1, sentence );
            collector.collect( output );
        }
    }
}