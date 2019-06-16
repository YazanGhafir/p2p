package com.snubbull.app.view;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Notice implements Serializable {

  @SerializedName("id")
  @Expose
  private UUID id;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("title")
  @Expose
  private String title;
  @SerializedName("content")
  @Expose
  private String content;
  @SerializedName("from")
  @Expose
  private String from;
  @SerializedName("to")
  @Expose
  private String to;

  public Notice() {
  }

  /**
   * Constructor.
   */
  public Notice(UUID id, String name, String title, String content, String from, String to) {
    this.id = id;
    this.name = name;
    this.title = title;
    this.content = content;
    this.from = from;
    this.to = to;

  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Notice notice = (Notice) o;
    return Objects.equals(title, notice.title)
        && Objects.equals(content, notice.content)
        && Objects.equals(from, notice.from)
        && Objects.equals(to, notice.to);


  }

  @Override
  public int hashCode() {
    return Objects.hash(title, content, from, to);
  }

  @Override
  public String toString() {
    return "Notice{" +
        "title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        '}';
  }
}
