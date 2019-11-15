package cn.xylink.mting.speech.list;

import java.util.List;

import cn.xylink.mting.bean.Article;

public interface SpeechList {
    boolean hasPrevious();
    boolean hasNext();
    boolean moveNext();
    Article getCurrent();
    Article select(String articleId);
    Article selectFirst();
    Article find(String articleId);
    boolean removeSome(List<String> removeIds);
    boolean removeAll();
    int size();
    void pushBack(List<Article> articles);
    void pushFront(List<Article> articles);
    List<Article> getArticles();
    Article getFirst();
    Article getLast();
}
