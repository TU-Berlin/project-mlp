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
package mlp.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.flink.types.ListValue;
import org.apache.flink.types.StringValue;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author rob
 */
public class Sentence extends ListValue<Word> implements Cloneable {

    public List<Integer> getWordPosition(String word) {
        List<Integer> positions = new ArrayList<>();
        String token;
        Integer pos = -1;
        Iterator<Word> it = this.iterator();
        while (it.hasNext()) {
            pos += 1;
            token = it.next().getWord();
            if (token.equals(word)) {
                positions.add(pos);
            }
        }
        return positions;
    }

    public boolean containsWord(String word) {
        return !getWordPosition(word).isEmpty();
    }

    public boolean containsWord(StringValue word) {
        return containsWord(word.getValue());
    }

    public boolean containsWord(Word word) {
        return containsWord(word.getWord());
    }

    @Override
    public Sentence clone() {
        Sentence obj = new Sentence();
        obj.addAll(this);
        return obj;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONArray words = new JSONArray();
        Iterator<Word> it = this.iterator();
        while (it.hasNext()) {
            words.put(it.next().toJSON());
        }
        Map<String, Object> json = new HashMap<>();
        json.put("words", words);
        return new JSONObject(json);
    }

}
