import java.util.Arrays;
/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;
    
    /**
     * Clear the array storage
     */
    public void clear() {
        Arrays.fill(storage, null);
        size = 0;
        System.out.println("Successfully cleared.");
    }

    /**
     * Save Resume object in array storage
     * @param r current Resume
     */
    public void save(Resume r) {
        boolean isExist = false;
        if (size >= 0 && size < 9999) {
            for (int i = 0; i < size; i++) {
                if (r.getUuid().equals(storage[i].getUuid())) {
                    isExist = true;
                }
            }
        }
        if (!isExist) {
            storage[size] = r;
            size++;
        }
        if (size >= 9999) {
            System.out.println("Storage overloaded.");
        }
    }

    /**
     * Replace resume in storage by UUID number
     * @param r incoming resume
     */
    public void update(Resume r) {
        boolean isExist = false;
        for (int i = 0; i < size; i++) {
            if (r.getUuid().equals(storage[i].getUuid())) {
                storage[i] = r;
                System.out.println("Resume #" + r.getUuid() + " successfully updated.");
                isExist = true;
            }
        }
        if (!isExist) {
            System.out.println("Sorry, resume #" + r.getUuid() + " not found.");
        }
    }

    /**
     * Get UUID number from Resume object, if it exist and possible
     * @param uuid incoming UUID number
     * @return UUID number of existing Resume
     */
    public Resume get(String uuid) {
        boolean isExist = false;
        Resume answer = new Resume();
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                answer = storage[i];
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            answer.setUuid("Resume #" + uuid + " not found.");
        }
        return answer;
    }

    /**
     * Delete existing Resume from array storage by UUID number
     * @param uuid incoming UUID number
     */
    void delete(String uuid) {
        boolean isExist = false;
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                isExist = true;
                System.arraycopy(storage, i + 1, storage, i, size - 1 - i);
                size--;
                System.out.println ("Resume #" + uuid + " succesfully deleted.");
            }
        }
        if (!isExist) {
            System.out.println ("Resume #" + uuid + " not found.");
        }
    }

    /**
     * Get all Resume's UUIN numbers, printed to stack
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    /**
     * Call a current quantity of Resume of array storage;
     * @return size of array (quantity of objects)
     */
    public int size() {
        return size;
    }
}
