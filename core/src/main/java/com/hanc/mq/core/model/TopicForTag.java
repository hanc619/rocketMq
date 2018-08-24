package com.hanc.mq.core.model;

/**
 * @author: cuill
 * @date: 2018/8/20 15:01
 */
public class TopicForTag {

    private String topic;

    private String tag;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TopicForTag(String topic, String tag) {
        this.topic = topic;
        this.tag = tag;
    }

    /**
     * 参考String 方法里面生成的hashCode
     *
     * @return
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((topic == null) ? 0 : topic.hashCode());
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        return result;
    }

    /**
     * 如果改对象的topic和tag相同的话 可以考虑认为为同一个对象
     *
     * @param obj 对象
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TopicForTag)) {
            return false;
        }
        TopicForTag topicForTag = (TopicForTag) obj;
        return topicForTag.getTag().equals(this.tag)
                && topicForTag.getTopic().equals(this.topic);
    }

}
