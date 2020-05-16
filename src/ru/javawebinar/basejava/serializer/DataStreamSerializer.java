package ru.javawebinar.basejava.serializer;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.writers.WriterInterface;

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
                        dos.writeUTF(type.name());
                        writeCollection(dos, ((ListSection) section).getContent(), element -> dos.writeUTF(element));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeCollection(dos, ((OrganizationSection) entry.getValue()).getContent(), org -> {
                            dos.writeUTF(org.getHomepage().getName());
                            dos.writeUTF(org.getHomepage().getUrl());
                            writeCollection(dos, org.getPositions(), position -> {
                                dos.writeUTF(position.getTitle());
                                dos.writeUTF(position.getDescription());
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
            for (int i = dis.readInt(); i > 0; i--) {
                resume.getContacts().put(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            while (dis.available() > 0) {
                String type = dis.readUTF();
                if (type.isEmpty()) continue;
                switch (type) {
                    case "OBJECTIVE":
                    case "PERSONAL":
                        resume.getPersonInfo().put(SectionType.valueOf(type), new StringSection(dis.readUTF()));
                        break;
                    case "ACHIEVEMENTS":
                    case "QUALIFICATION":
                        List<String> list = new ArrayList<>();
                        for (int i = dis.readInt(); i > 0; i--) {
                            list.add(dis.readUTF());
                        }
                        resume.getPersonInfo().put(SectionType.valueOf(type), new ListSection(list));
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        OrganizationSection org = new OrganizationSection();
                        for (int i = dis.readInt(); i > 0; i--) {
                            Link link = new Link(dis.readUTF(), dis.readUTF());
                            org.addOrganization(new Organization(link));
                            for (int j = dis.readInt(); j > 0; j--) {
                                org.getContent().get(org.getContent().size() - 1).addPosition(
                                        dis.readUTF(),
                                        dis.readUTF(),
                                        readDate(dis.readUTF()),
                                        readDate(dis.readUTF()));
                            }
                        }
                        resume.getPersonInfo().put(SectionType.valueOf(type), org);
                        break;
                    default:
                        break;
                }
            }
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

}
