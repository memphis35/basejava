import java.util.Arrays;
/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int size = 0;
    
    /**
     * Clear the array storage.
     */
    void clear() {
        for (int i = 0; i != storage.length; i++) {
            if (storage[i] != null) {
                storage[i] = null;
                size = 0;
            }
            else {
                break;
            }
        }
        System.out.println("Successfully cleared.");
    }

    /**
     * Save Resume object in array storage
     * @param r current Resume
     */
    void save(Resume r) {
        for (int i = 0; i != storage.length; i++) {
            if (storage[i] == null) {
                storage[i] = r;
                size++;
                break;
            }
        }
    }

    /**
     * Get UUID number from Resume object, if it exist and possible
     * @param uuid incoming UUID nub=mber
     * @return UUID number of existing Resume
     */
    String get(String uuid) {
        String answer = "Not found";
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                answer = storage[i].toString();
                break;
            }
        }
        return answer;
    }

    /**
     * Delete existing Resume from array storage by UUID number
     * @param uuid incoming UUID number
     */
    void delete(String uuid) {
        int count = 0;
        for (int i = 0; i != size; i++) {
            if (this.storage[i].uuid.equals(uuid)) {
                count++;
                System.arraycopy(this.storage, i + 1, this.storage, i, this.storage.length - 1 - i);
                size--;
            }
        }
        if (count == 0) {
            System.out.println ("Not found.");
        }
    }

    /**
     * Get all Resume's UUIN numbers, printed to stack
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] a1 = Arrays.copyOf(this.storage, i);
        return a1;
    }

    /**
     * Call a current quantity of Resume of array storage;
     * @return size of array (quantity of objects)
     */
    int size() {
        return this.size;
    }
}
