package db;

import java.util.List;

import entity.ThreadInfo;

/**
 * Created by aspsine on 15-4-19.
 */
public interface ThreadInfoRepository{
    public void insert(ThreadInfo threadInfo);

    public void delete(String url, int threadId);

    public void update(String url, int threadId, int finished);

    public List<ThreadInfo> getThreadInfos(String url);

    public boolean exists(String url, int threadId);
}