package ru.javawebinar.basejava.serializer;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements Serializer {

    @Override
    public void write(OutputStream out, Resume resume) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            dos.writeInt(resume.contacts.size());
            for (Map.Entry<ContactType, String> entry : resume.contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            for (Map.Entry<SectionType, Section> entry : resume.personInfo.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                switch (entry.getKey()) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dos.writeUTF(((StringSection) entry.getValue()).getContent());
                        break;
                    case ACHIEVEMENTS:
                    case QUALIFICATION:
                        dos.writeInt(((ListSection) entry.getValue()).getContent().size());
                        for (String stroke : (((ListSection) entry.getValue()).getContent())) {
                            dos.writeUTF(stroke);
                        }
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        dos.writeInt(((OrganizationSection) entry.getValue()).getContent().size());
                        for (Organization org : ((OrganizationSection) entry.getValue()).getContent()) {
                            dos.writeUTF(org.getHomepage().getName());
                            dos.writeUTF(org.getHomepage().getUrl());
                            dos.writeInt(org.getPositions().size());
                            for (Organization.Position position : org.getPositions()) {
                                dos.writeUTF(position.getTitle());
                                dos.writeUTF(position.getStartDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
                                dos.writeUTF(position.getEndDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
                                dos.writeUTF(position.getDescription());
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            throw new StorageException("Storage write error", e, null);
        }
    }

    @Override
    public Resume read(InputStream in) {
        try (DataInputStream dis = new DataInputStream(in)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            for (int i = dis.readInt(); i > 0; i--) {
                resume.contacts.put(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            while (in.available() > 0) {
                String type = dis.readUTF();
                switch (type) {
                    case "OBJECTIVE":
                    case "PERSONAL":
                        resume.personInfo.put(SectionType.valueOf(type), new StringSection(dis.readUTF()));
                        break;
                    case "ACHIEVEMENTS":
                    case "QUALIFICATION":
                        List<String> list = new ArrayList<>();
                        for (int i = dis.readInt(); i > 0; i--) {
                            list.add(dis.readUTF());
                        }
                        resume.personInfo.put(SectionType.valueOf(type), new ListSection(list));
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        OrganizationSection org = new OrganizationSection();
                        for (int i = dis.readInt(); i > 0; i--) {
                            Link link = new Link(dis.readUTF(), dis.readUTF());
                            org.addOrganization(new Organization(link));
                            for (int j = dis.readInt(); j > 0; j--) {
                                org.addPosition(
                                        link.getName(), dis.readUTF(),
                                        LocalDate.from(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).parse(dis.readUTF())),
                                        LocalDate.from(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).parse(dis.readUTF())),
                                        dis.readUTF());
                            }
                        }
                        resume.personInfo.put(SectionType.valueOf(type), org);
                        break;
                    default:
                        break;
                }
            }
            return resume;
        } catch (IOException e) {
            throw new StorageException("Storage read error", e, null);
        }
    }
}
