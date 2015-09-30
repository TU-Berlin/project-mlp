package mlp.pojos;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import mlp.text.WikiTextUtils.MathMarkUpType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.nio.charset.StandardCharsets;


public class MathTag {
  private static final HashFunction HASHER = Hashing.md5();
  private final int position;
  private final String content;
  private final MathMarkUpType markUpType;

  public MathTag(int position, String content, MathMarkUpType markUp) {
    this.position = position;
    this.content = content;
    this.markUpType = markUp;
  }

  public int getPosition() {
    return position;
  }

  public String getContent() {
    return content;
  }

  public String getTagContent() {
    return content.replaceAll("<math.*?>", "").replaceAll("</math>", "");
  }

  public String getContentHash() {
    return HASHER.hashString(content, StandardCharsets.UTF_8).toString();
  }

  public String placeholder() {
    return "FORMULA_" + getContentHash();
  }

  public MathMarkUpType getMarkUpType() {
    return markUpType;
  }

  @Override
  public String toString() {
    return "MathTag [position=" + position + ", content=" + content + "]";
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}

