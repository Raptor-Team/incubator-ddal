//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package studio.raptor.ddal.common;


public class Pair<K, V> {

  private volatile K m_key;
  private volatile V m_value;

  public Pair() {
  }

  public Pair(K key, V value) {
    this.m_key = key;
    this.m_value = value;
  }

  public static <K, V> Pair<K, V> from(K key, V value) {
    return new Pair(key, value);
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof Pair) {
      Pair o = (Pair) obj;
      if (this.m_key == null) {
        if (o.m_key != null) {
          return false;
        }
      } else if (!this.m_key.equals(o.m_key)) {
        return false;
      }

      if (this.m_value == null) {
        if (o.m_value != null) {
          return false;
        }
      } else if (!this.m_value.equals(o.m_value)) {
        return false;
      }

      return true;
    } else {
      return false;
    }
  }

  public K getKey() {
    return this.m_key;
  }

  public V getValue() {
    return this.m_value;
  }

  public int hashCode() {
    byte hash = 0;
    int hash1 = hash * 31 + (this.m_key == null ? 0 : this.m_key.hashCode());
    hash1 = hash1 * 31 + (this.m_value == null ? 0 : this.m_value.hashCode());
    return hash1;
  }

  public void setKey(K key) {
    this.m_key = key;
  }

  public void setValue(V value) {
    this.m_value = value;
  }

  public int size() {
    return 2;
  }

  public String toString() {
    return String.format("Pair[key=%s, value=%s]", new Object[]{this.m_key, this.m_value});
  }
}
