import java.util.HashMap;

public class InMemoryDB {
    private HashMap<String, Integer> data;
    private HashMap<String, Integer> transactionWriteSet; //temporary storage to track changes made to database
    private boolean isActive; //whether transaction is in progress or not
    //Constructor
    public InMemoryDB(HashMap<String, Integer> data, HashMap<String, Integer> transactionWriteSet, boolean isActive){
        this.data = new HashMap<>();
        this.transactionWriteSet = null;
        this.isActive = false;
    }

    // Starts a new transaction
    void begin_transaction() {
        if (isActive == true) {
            System.out.println("Transaction already in progress");
            return;
        }
        transactionWriteSet = new HashMap<>();
        isActive = true;
        System.out.println("Transaction started");
    }

    // Get the value for a key
    void get(String key) {
        if (isActive == true && transactionWriteSet.containsKey(key)) {
            System.out.println("Value: " + transactionWriteSet.get(key));
        } 
        else if (data.containsKey(key)) {
            System.out.println("Value: " + data.get(key));
        } 
        else {
            System.out.println("Key not found");
            return;
        }
    }

    // Updates or adds a key-value pair during a transaction
    void put(String key, int value) {
        if (isActive == false) {
            System.out.println("No transaction active");
            return;
        }
        transactionWriteSet.put(key, value);
        System.out.println("Set " + key + " = " + value);
    }

    // Commits the transaction
    void commit() {
        if (isActive == false) {
            System.out.println("No transaction active");
            return;
        }
        for (String key : transactionWriteSet.keySet()) {
            data.put(key, transactionWriteSet.get(key));
        }
        transactionWriteSet = null;
        isActive = false;
        System.out.println("Transaction committed");
    }

    // Rolls back the transaction
    void rollback() {
        if (isActive == false) {
            System.out.println("No transaction active");
            return;
        }
        transactionWriteSet = null;
        isActive = false;
        System.out.println("Transaction rolled back");
    }

    public static void main(String[] args) {
        InMemoryDB inmemoryDB = new InMemoryDB();

        // should return null, because A doesn’t exist in the DB yet
        inmemoryDB.get("A");

        // should throw an error because a transaction is not in progress
        inmemoryDB.put("A", 5);

        // starts a new transaction
        inmemoryDB.begin_transaction();

        // set’s value of A to 5, but its not committed yet
        inmemoryDB.put("A", 5);

        // should return null, because updates to A are not committed yet
        inmemoryDB.get("A");

        // update A’s value to 6 within the transaction
        inmemoryDB.put("A", 6);

        // commits the open transaction
        inmemoryDB.commit();

        // should return 6, that was the last value of A to be committed
        inmemoryDB.get("A");

        // throws an error, because there is no open transaction
        inmemoryDB.commit();

        // throws an error because there is no ongoing transaction
        inmemoryDB.rollback();

        // should return null because B does not exist in the database
        inmemoryDB.get("B");

        // starts a new transaction
        inmemoryDB.begin_transaction();

        // Set key B’s value to 10 within the transaction
        inmemoryDB.put("B", 10);

        // Rollback the transaction - revert any changes made to B
        inmemoryDB.rollback();

        // Should return null because changes to B were rolled back
        inmemoryDB.get("B");

    }
}
