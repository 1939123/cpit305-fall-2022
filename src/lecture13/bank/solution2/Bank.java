package lecture13.bank.solution2;

import java.util.concurrent.locks.ReentrantLock;

public class Bank {
    int[] accounts;
    int init_balance;
    ReentrantLock[] m;
    ReentrantLock mx;


    public Bank(int number_of_account, int init_balance) {
        accounts = new int[number_of_account];
        this.init_balance = init_balance;
        m = new ReentrantLock[number_of_account];

        for (int i = 0; i < number_of_account; i++) {
            accounts[i] = init_balance;
        }
        for (int i = 0; i < number_of_account; i++) {
            m[i] = new ReentrantLock();
        }
        mx = new ReentrantLock();
    }

    public void transfer(int from, int to, int amount) {
        mx.lock();
        try {
            m[from].lock();
            m[to].lock();
        } finally {
            mx.unlock();
        }
        System.out.printf("%s:transfer started from: %d to: %d\n", Thread.currentThread().getName(), from, to);
        try {
            accounts[from] -= amount;  // LD accounts[from]; DEC amount; STORE accounts[from]
            accounts[to] += amount;
        } finally {
            System.out.printf("%s:transfer ended from: %d to: %d\n", Thread.currentThread().getName(), from, to);
            m[from].unlock();
            m[to].unlock();
            // mx.unlock();
        }
    }

    public int getTotal() {
        int sum = 0;
        // m.lock();
        try {
            for (int account: accounts) {
                sum += account;
            }
        } finally {
            // m.unlock();
        }
        return sum;
    }

    public void displayAccounts() {
        for (int i = 0; i < accounts.length; i++) {
            System.out.printf("[%d]: %d\n", i, accounts[i]);
        }
    }
}
