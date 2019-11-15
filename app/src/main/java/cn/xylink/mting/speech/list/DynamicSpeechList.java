package cn.xylink.mting.speech.list;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import cn.xylink.mting.bean.Article;

public class DynamicSpeechList implements SpeechList {
    List<Article> internalList;
    int currentIndex;

    public DynamicSpeechList(List<Article> articles) {
        currentIndex = -1;
        if (articles != null) {
            internalList = new ArrayList<>(articles);
            return;
        }
        internalList = new ArrayList<>();
    }

    public DynamicSpeechList() {
        this(null);
    }

    @Override
    public boolean hasPrevious() {
        if (currentIndex <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasNext() {
        if (currentIndex == -1) {
            return false;
        }
        if (currentIndex + 1 >= (internalList.size())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean moveNext() {
        if (currentIndex == -1) {
            return false;
        }

        if (hasNext() == false) {
            return false;
        }

        currentIndex += 1;
        return true;
    }

    @Override
    public Article getCurrent() {
        return currentIndex == -1 ? null : internalList.get(currentIndex);
    }

    @Override
    public Article select(String articleId) {
        if (articleId == null) {
            return null;
        }
        for (int i = 0, j = internalList.size(); i < j; i++) {
            if (articleId.equals(internalList.get(i).getArticleId())) {
                currentIndex = i;
                return internalList.get(i);
            }
        }
        return null;
    }


    @Override
    public Article find(String articleId) {
        if (articleId == null) {
            return null;
        }
        for (int i = 0, j = internalList.size(); i < j; i++) {
            if (articleId.equals(internalList.get(i).getArticleId())) {
                return internalList.get(i);
            }
        }
        return null;
    }

    @Override
    public Article selectFirst() {
        if (internalList.size() > 0) {
            currentIndex = 0;
            return internalList.get(0);
        }
        return null;
    }

    @Override
    public boolean removeSome(List<String> removeIds) {
        boolean currentIsDeleted = false;
        Article dabutyCurrent = (currentIndex != -1) ? internalList.get(currentIndex) : null;
        int indexOfPrivious = currentIndex;
        ListIterator<Article> e = internalList.listIterator();
        List<String> removeIdsClone = new ArrayList(removeIds);
        while (e.hasNext()) {
            Article article = e.next();
            if (currentIsDeleted == true && dabutyCurrent == null) {
                dabutyCurrent = article;
            }
            for (int i = 0; i < removeIdsClone.size(); i++) {
                if (article.getArticleId().equals(removeIdsClone.get(i))) {
                    e.remove();
                    removeIdsClone.remove(i);
                    //如果删除的是焦点
                    if (dabutyCurrent != null && article == dabutyCurrent) {
                        currentIsDeleted = true;
                        dabutyCurrent = null;
                    }
                    break;
                }
            }
        }

        if (dabutyCurrent != null) {
            currentIndex = internalList.indexOf(dabutyCurrent);
        }
        else if (indexOfPrivious != -1 && internalList.size() > 0) {
            currentIndex = 0;
        }
        else {
            currentIndex = -1;
        }
        return currentIsDeleted;
    }

    @Override
    public boolean removeAll() {
        int indexOfPrevious = currentIndex;
        internalList.clear();
        currentIndex = -1;
        return indexOfPrevious != -1;
    }

    @Override
    public int size() {
        return internalList.size();
    }

    @Override
    public void pushBack(List<Article> articles) {
        internalList.addAll(articles);
    }

    @Override
    public void pushFront(List<Article> articles) {
        if (articles == null || articles.size() <= 0) {
            return;
        }
        for (int i = articles.size() - 1; i >= 0; i--) {
            internalList.add(0, articles.get(i));
            if (currentIndex != -1) {
                ++currentIndex;
            }
        }
    }

    @Override
    public List<Article> getArticles() {
        return internalList;
    }

    @Override
    public Article getFirst() {
        return internalList.size() == 0? null : internalList.get(0);
    }

    @Override
    public Article getLast() {
        return internalList.size() == 0? null : internalList.get(internalList.size() - 1);
    }
}
