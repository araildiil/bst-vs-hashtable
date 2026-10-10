# BST vs Hash Table: Performance Analysis

## 1. Implementations
- `BST.java`: unbalanced binary search tree 
- `HashTable.java`: separate chaining, power-of-2 capacity, load factor 0.75, resize by doubling.
- `Benchmark.java`: measures total time for N inserts, N searches and N deletes. Averaged over 30 runs after 10 warm-up runs. Tested on random and sorted keys.

Run: `cd src && javac *.java && java Benchmark`

## 2. Complexity Analysis

| Operation | BST best | BST average | BST worst | Hash best | Hash average | Hash worst |
|-----------|----------|-------------|-----------|-----------|--------------|------------|
| Insert    | O(1)     | O(log n)    | O(n)      | O(1)      | O(1) amortized | O(n)     |
| Search    | O(1)     | O(log n)    | O(n)      | O(1)      | O(1)         | O(n)       |
| Delete    | O(1)     | O(log n)    | O(n)      | O(1)      | O(1)         | O(n)       |

**BST:** The best case is the target at the root. The cost is proportional to the tree height h. Random insertion gives h = O(log n). Sorted insertion produces a linked-list-shaped tree with h = n, so the worst case is O(n). Deletion needs a search plus, for two children, a successor search, which is still O(h).

**Hash table:** The best case is an empty bucket or a first-element hit. With a good hash function and load factor <= 0.75, chains are short, so the average is O(1). The worst case is all keys colliding into one bucket (O(n)). Insert is amortized O(1) because a resize costs O(n) but happens rarely.

## 3. Experimental Results

Average time in microseconds (us) over 30 runs after 10 warm-up runs.

| Dataset | N | Structure | Insert (us) | Search (us) | Delete (us) | BST height |
|---------|---|-----------|-------------|-------------|-------------|------------|
| random | 100 | BST | 15.2 | 14.4 | 15.3 | 15 |
| random | 100 | Hash Table | 34.5 | 7.3 | 13.2 | - |
| random | 1000 | BST | 146.8 | 120.4 | 102.4 | 23 |
| random | 1000 | Hash Table | 140.2 | 37.6 | 45.0 | - |
| random | 10000 | BST | 1255.5 | 755.3 | 894.3 | 32 |
| random | 10000 | Hash Table | 521.8 | 164.7 | 181.7 | - |
| sorted | 100 | BST | 11.8 | 15.7 | 8.9 | 100 |
| sorted | 100 | Hash Table | 2.5 | 0.7 | 1.1 | - |
| sorted | 1000 | BST | 2678.1 | 1575.5 | 756.0 | 1000 |
| sorted | 1000 | Hash Table | 30.3 | 9.0 | 13.0 | - |
| sorted | 10000 | BST | 117170.4 | 150736.4 | 78045.1 | 10000 |
| sorted | 10000 | Hash Table | 205.0 | 21.1 | 144.1 | - |

## 4. Comparison of Results

**Random keys.** The hash table was faster for search and delete at every size, and the gap grew with N. Search was about 2x faster at N=100 (7.3 vs 14.4 us) and about 4.6x faster at N=10,000 (164.7 vs 755.3 us). Delete at N=10,000 was about 4.9x faster. Insert was different: the BST won at N=100 (15.2 vs 34.5 us), they were roughly equal at N=1,000, and the hash table won by about 2.4x at N=10,000. At small N, the hash table's resizing (it starts with 16 buckets) and JIT warm-up cost more than its advantage.

**BST height.** For random keys the height was 15, 23 and 32 for N = 100, 1,000 and 10,000. It grows roughly logarithmically, but it is higher than the ideal log2(n) (6.6, 10, 13.3) because the tree is not balanced.

**Sorted keys.** The BST height equals N (100, 1,000, 10,000), so the tree becomes a linked list. At N=10,000 the BST needed 117,170 us for insert and 150,736 us for search, against 205.0 and 21.1 us for the hash table: more than 500x slower for insert and over 7,000x slower for search. From N=1,000 to N=10,000 the BST search time grew about 96x for 10x more data (each operation is O(n), so the total is O(n^2)), while the hash table search time grew only about 2.3x.

Note: absolute times depend on the machine, JIT compilation and timer noise, so the trends matter more than exact values.

## 5. Why Performance Differs in Practice
- The BST depends on input order. It has no self-balancing, so sorted input degrades it to a linked list.
- Hash table cost does not grow with n on average. BST cost grows with log n (or n).
- Pointer chasing: each BST step is a likely cache miss, while the hash table touches one bucket and a short chain.
- Hashing and resizing add constant overhead, so at small N the difference can be small or even reversed (see random insert at N=100).
- Consecutive integer keys spread evenly across buckets, which is why the hash table is very fast on sorted input.
- Hash tables do not support ordered operations (min, max, range queries, in-order traversal). BSTs do.

## 6. Conclusions
A hash table is the better choice when the main need is fast insert, search and delete by key, because its average cost stays almost constant as the data grows. A plain BST is risky: on sorted input it became hundreds or thousands of times slower than the hash table. A BST is still useful when sorted order, min/max or range queries are needed, and in that case it should be self-balancing (AVL or Red-Black tree) to guarantee O(log n).