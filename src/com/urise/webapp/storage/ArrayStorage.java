package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import java.util.Arrays;
/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final int maxSize = 10_000;
    private Resume[] storage = new Resume[maxSize];
    private int size = 0;

    /**
     * Clear the array storage
     */
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
        System.out.println("Storage successfully cleared.");
    }

    /**
     * Save resume object in array storage
     * @param r current resume
     */
    public void save(Resume r) {
        if (size >= 0 && size < maxSize) {
            if (!exist(r.getUuid())) {
                storage[size] = r;
                System.out.println("Resume succesfully saved.");
                size++;
            }
            if (size >= maxSize) {
                System.out.println("Storage overloaded.");
            }
        }
    }

        /**
         * Replace resume in storage by UUID number
         * @param r incoming resume
         */
        public void update (Resume r) {
            for (int i = 0; i < size; i++) {
                if (exist(r.getUuid())) {
                    storage[i] = r;
                    System.out.println("Resume #" + r.getUuid() + " successfully updated.");
                    break;
                }
            }
            if (!exist(r.getUuid())) {
                System.out.println("Resume #" + r.getUuid() + " not found.");
            }
        }

        /**
         * Get UUID number from Resume object, if it exist and possible
         * @param uuid incoming UUID number
         * @return UUID number of existing Resume
         */
        public Resume get (String uuid) {
            Resume answer = new Resume();
            for (int i = 0; i < size; i++) {
                if (exist(uuid)) {
                    answer = storage[i];
                    break;
                } else {
                    answer.setUuid("Resume #" + uuid + " not found.");
                }
            }
            return answer;
        }

        /**
         * Delete existing Resume from array storage by UUID number
         * @param uuid incoming UUID number
         */
        public void delete (String uuid) {
            if (!exist(uuid)) {
                System.out.println("Resume #" + uuid + " not found.");
            }
            else {
                for (int i = 0; i < size; i++) {
                    if (exist(uuid)) {
                        System.arraycopy(storage, i + 1, storage, i, size - 1 - i);
                        size--;
                        System.out.println("Resume #" + uuid + " succesfully deleted.");
                    }
                }
            }
        }

        /**
         * Get all Resume's UUIN numbers, printed to stack
         * @return array, contains only Resumes in storage (without null)
         */
        public Resume[] getAll () {
            return Arrays.copyOf(storage, size);
        }

        /**
         * Call a current quantity of Resume of array storage
         * @return size of array (quantity of objects)
         */
        public int size () {
            return size;
        }

    /**
     * Searching for a existing resume by UUID number
     * @param uuid UUID number
     * @return true if resume exist
     */
        public boolean exist (String uuid){
            boolean isExist = false;
            for (int i = 0; i < size; i++) {
                if (storage[i].getUuid().equals(uuid)) {
                    isExist = true;
                }
            }
            return isExist;
        }
    }