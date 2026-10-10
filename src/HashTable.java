public class HashTable {
    private static class Entry {
        int key;
        Entry next;
        Entry(int key, Entry next) { this.key = key; this.next = next; }
    }

    private static final double MAX_LOAD = 0.75;
    private Entry[] table = new Entry[16];
    private int size;

    private static int index(int key, int length) {
        int h = key ^ (key >>> 16);
        return h & (length - 1);
    }

    public boolean insert(int key) {
        int i = index(key, table.length);
        for (Entry e = table[i]; e != null; e = e.next)
            if (e.key == key) return false;
        table[i] = new Entry(key, table[i]);
        size++;
        if (size > MAX_LOAD * table.length) resize();
        return true;
    }

    public boolean search(int key) {
        for (Entry e = table[index(key, table.length)]; e != null; e = e.next)
            if (e.key == key) return true;
        return false;
    }

    public boolean delete(int key) {
        int i = index(key, table.length);
        Entry prev = null;
        for (Entry e = table[i]; e != null; prev = e, e = e.next) {
            if (e.key == key) {
                if (prev == null) table[i] = e.next;
                else prev.next = e.next;
                size--;
                return true;
            }
        }
        return false;
    }

    private void resize() {
        Entry[] old = table;
        table = new Entry[old.length * 2];
        for (Entry head : old) {
            for (Entry e = head; e != null; ) {
                Entry next = e.next;
                int i = index(e.key, table.length);
                e.next = table[i];
                table[i] = e;
                e = next;
            }
        }
    }

    public int size() { return size; }
}