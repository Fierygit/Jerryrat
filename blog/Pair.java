package blog;


import java.io.Serializable;

/**
 * @author Firefly
 * @version 1.0
 * @date 2020/2/8 17:47
 * 在linux 的 1.8 中没有   Pair 这个工具类， 那就自己弄一个！！！
 */

public class Pair<K, V> implements Serializable {


    private K key;


    public K getKey() {
        return key;
    }


    private V value;


    public V getValue() {
        return value;
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public int hashCode() {
        return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
    }


}