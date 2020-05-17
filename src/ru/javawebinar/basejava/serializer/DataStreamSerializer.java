package ru.javawebinar.basejava.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class DataStreamSerializer implements Serializer {

    @Override
    public void write(OutputStream out, Resume resume) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, String> contacts = resume.getContacts();
            writeCollection(dos, contacts.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            writeCollection(dos, resume.getPersonInfo().entrySet(), entry -> {
                SectionType type = entry.getKey();
                Section section = entry.getValue();
                dos.writeUTF(type.name());
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dos.writeUTF(section.toString());
                        break;
                    case ACHIEVEMENTS:
                    case QUALIFICATION:
                        writeCollection(dos, ((ListSection) section).getContent(), dos::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeCollection(dos, ((OrganizationSection) entry.getValue()).getContent(), org -> {
                            if (org.getHomepage().getUrl() != null) {
                                dos.writeUTF("notNullURL");
                                dos.writeUTF(org.getHomepage().getName());
                                dos.writeUTF(org.getHomepage().getUrl());
                            } else {
                                dos.writeUTF("nullURL");
                                dos.writeUTF(org.getHomepage().getName());
                            }
                            writeCollection(dos, org.getPositions(), position -> {
                                dos.writeUTF(position.getTitle());
                                if (position.getDescription() != null) {
                                    dos.writeUTF("notNullDescription");
                                    dos.writeUTF(position.getDescription());
                                } else {
                                    dos.writeUTF("nullDescription");
                                }
                                dos.writeUTF(writeDate(position.getStartDate()));
                                dos.writeUTF(writeDate(position.getEndDate()));
                            });
                        });
                        break;
                    default:
                        break;
                }
            });
        }
    }

    @Override
    public Resume read(InputStream in) throws IOException {
        try (DataInputStream dis = new DataInputStream(in)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readCollection(dis, () -> resume.getContacts().put(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readCollection(dis, () -> {
                String type = dis.readUTF();
                switch (type) {
                    case "OBJECTIVE":
                    case "PERSONAL":
                        resume.getPersonInfo().put(SectionType.valueOf(type), new StringSection(dis.readUTF()));
                        break;
                    case "ACHIEVEMENTS":
                    case "QUALIFICATION":
                        List<String> list = new ArrayList<>();
                        readCollection(dis, () -> list.add(dis.readUTF()));
                        resume.getPersonInfo().put(SectionType.valueOf(type), new ListSection(list));
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        OrganizationSection org = new OrganizationSection();
                        readCollection(dis, () -> {
                            Link link;
                            if (dis.readUTF().equals("notNullURL")) {
                                link = new Link(dis.readUTF(), dis.readUTF());
                            } else {
                                link = new Link(dis.readUTF(), null);
                            }
                            org.addOrganization(new Organization(link));
                            readCollection(dis, () -> {
                                String name = dis.readUTF();
                                String description = null;
                                if (dis.readUTF().equals("notNullDescription")) description = dis.readUTF();
                                org.getContent().get(org.getContent().size() - 1).addPosition(
                                        name,
                                        description,
                                        readDate(dis.readUTF()),
                                        readDate(dis.readUTF()));
                            });
                        });
                        resume.getPersonInfo().put(SectionType.valueOf(type), org);
                        break;
                    default:
                        break;
                }
            });
            return resume;
        }
    }

    private String writeDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
    }

    private LocalDate readDate(String date) {
        return LocalDate.from(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).parse(date));
    }

    private <T> void writeCollection(DataOutputStream out, Collection<T> collection, WriterInterface<T> writer) throws IOException {
        out.writeInt(collection.size());
        for (T element : collection) {
            writer.writeData(element);
        }
    }

    private <T> void readCollection(DataInputStream in, ReaderInterface<T> reader) throws IOException {
        for (int i = in.readInt(); i > 0; i--) {
            reader.readData();
        }
    }

    @FunctionalInterface
    public interface WriterInterface<T> {
        void writeData(T element) throws IOException;
    }

    @FunctionalInterface
    private interface ReaderInterface<T> {
        void readData() throws IOException;
    }
}
