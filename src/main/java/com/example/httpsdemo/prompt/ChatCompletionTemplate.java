package com.example.httpsdemo.prompt;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>{@link com.theokanning.openai.completion.chat.ChatCompletionRequest}的问题模板
 *
 * @author lizhao 2023/3/22
 */
public interface ChatCompletionTemplate {
  String FIRST_QUESTION = "用户：《问题写在这里》\n" +
    "在正常回答完上述用书名号\"《》\"扩起来的问题后，在最后追加两段内容：\n" +
    "1. 对你的回答内容的简要说明，尽量简短，用关键字\"|summary|:\"开头\n" +
    "2. 对我提出的问题的缩略摘要，十个字以内，用关键字\"|title|:\"开头";

  String QUESTION_IN_CONTEXT = "《问题写在这里》\n" +
    "在正常回答完上述用书名号\"《》\"扩起来的问题后，在最后追加一段对回答内容的简要说明，尽量简短，用关键字\"|summary|:\"开头";
}
