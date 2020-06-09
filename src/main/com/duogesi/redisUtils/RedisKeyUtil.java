package com.duogesi.redisUtils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Set;

@Component
public class RedisKeyUtil {
    Jedis js = new Jedis("127.0.0.1", 6379);//登录redis

    /**
     * 点赞功能
     *
     * @param topicid 主题id
     * @param uid     用户id
     * @param flag    true就添加进去 false就删除
     */
    public void dianZan(String topicid, String uid, boolean flag) {
        if (flag) {//点赞加入
            js.set(topicid + ":" + uid, "1");
        } else {//去赞删除
            js.del(topicid + ":" + uid);
        }
    }

    /**
     * 获取特定主题的点赞总数
     *
     * @param topicid
     * @return
     */

    public long getCount(String topicid) {
        Set<String> set = js.keys(topicid + ":*");

        return set.size();
    }

    /**
     * 反对功能
     *
     * @param topicid
     * @param uid
     * @param flag    true就添加进去 false就删除
     */
    public void dianZanx(String topicid, String uid, boolean flag) {

        if (flag) {//反对加入
            js.set(topicid + "|" + uid, "1");

        } else {//反对删除
            js.del(topicid + "|" + uid);
        }
    }

    /**
     * 获取特定主题的反对总数
     *
     * @param topicid
     * @return
     */

    public long getCountx(String topicid) {
        Set<String> set = js.keys(topicid + "|*");

        return set.size();
    }

    /**
     * 判断登陆顾客是否在特定主题上点赞[detail.jsp]
     *
     * @param topicid
     * @param uid
     * @return
     */
    public int judge(Object topicid, String uid) {

        Set<String> set = js.keys("" + topicid + ":" + uid);
        return set.size();
    }

    /**
     * 判断登陆顾客是否在特定主题上反对数[detail.jsp]
     *
     * @param topicid
     * @param uid
     * @return
     */
    public int judgex(Object topicid, String uid) {

        Set<String> set = js.keys("" + topicid + "|" + uid);
        return set.size();
    }
}