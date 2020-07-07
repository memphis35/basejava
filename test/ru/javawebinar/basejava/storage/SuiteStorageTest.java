package ru.javawebinar.basejava.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ListStorageTest.class,
        MapStorageTest.class,
        MapResumeStorageTest.class,
        ObjectStreamFileStorageTest.class,
        ObjectStreamPathStorageTest.class,
        XMLFileStorageTest.class,
        JSONFileStorageTest.class,
        DataStreamSerializerTest.class,
        SqlStorageTest.class})
public class SuiteStorageTest {

}
