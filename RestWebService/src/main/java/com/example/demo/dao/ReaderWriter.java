package com.example.demo.dao;

import com.example.demo.model.SenderNotice;
import com.example.demo.model.TransporterNotice;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderWriter {

    File senderFile = new File("senderNotices.dat");
    File transporterFile = new File("transporterNotices.dat");

    /**
     * Read and deserialize a list of notices from an input stream.
     * @return an array of notices
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<SenderNotice> readSenderNoticesFromSFile() throws IOException, ClassNotFoundException {
        List<SenderNotice> list = new ArrayList<>();

        if (!senderFile.exists()) {
            senderFile.createNewFile();
        }

        try(ObjectInputStream input= new ObjectInputStream(new FileInputStream("senderNotices.dat")))
        {
            list = (List<SenderNotice>)input.readObject();
        } catch (EOFException ex) {
            System.out.println("All data were read");
        }
        return list;
    }

    public List<TransporterNotice> readTransporterNoticesFromSFile() throws IOException, ClassNotFoundException {
        List<TransporterNotice> list = new ArrayList<>();

        if (!transporterFile.exists()) {
            transporterFile.createNewFile();
        }

        try(ObjectInputStream input= new ObjectInputStream(new FileInputStream("transporterNotices.dat")))
        {
            list = (List<TransporterNotice>)input.readObject();
        } catch (EOFException ex) {
            System.out.println("All data were read");
        }
        return list;
    }

    /**
     * serialize and write an array of notices to an output stream
     * @param list an array of notices
     * @throws IOException
     */
    public void writeToSFile(List<SenderNotice> list) throws IOException {
        try(ObjectOutputStream output= new ObjectOutputStream(new FileOutputStream("senderNotices.dat")))
        {
            output.writeObject(list);
        }
    }

    public void writeToTFile(List<TransporterNotice> list) throws IOException {
        try(ObjectOutputStream output= new ObjectOutputStream(new FileOutputStream("transporterNotices.dat")))
        {
            output.writeObject(list);
        }
    }
    /*
    public List<TransporterNotice> readFromTFile() throws IOException, ClassNotFoundException {
        List<TransporterNotice> list = new ArrayList<>();

        if (!transporterFile.exists()) {
            transporterFile.createNewFile();
        }

        try(ObjectInputStream input= new ObjectInputStream(new FileInputStream("transporterNotices.dat")))
        {
            list = (List<TransporterNotice>)input.readObject();
        } catch (EOFException ex) {
            System.out.println("All data were read");
        }
        return list;
    }

    public void writeToTFile(List<TransporterNotice> list) throws IOException {
        try(ObjectOutputStream output= new ObjectOutputStream(new FileOutputStream("transporterNotices.dat")))
        {
            output.writeObject(list);
        }
    }*/





}
