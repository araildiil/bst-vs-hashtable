# Experimental Results

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


