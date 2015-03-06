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
package cc.clabs.stratosphere.mlp.io;

import eu.stratosphere.api.java.record.io.TextInputFormat;
import eu.stratosphere.configuration.Configuration;

/**
 *
 * @author rob
 */
public class WikiDocumentEmitter extends TextInputFormat {


    @Override
    public void configure(Configuration parameter) {
        parameter.setString(DEFAULT_CHARSET_NAME, "UTF-8");
        super.configure(parameter);
        this.setDelimiter("</page>");
    }

}