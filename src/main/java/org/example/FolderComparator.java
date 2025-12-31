package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class FolderComparator {

    public static void main(String[] args) throws Exception {
        System.out.println("args[0]=========="+args[0]);
        System.out.println("args[1]=========="+args[1]);
        //System.out.println("args[2]=========="+args[2]);
        if (args.length != 2) {
            System.out.println("Usage: java FolderComparator <folder1> <folder2>");
            return;
        }

        Path folder1 = Paths.get(args[0]).toAbsolutePath().normalize();
        Path folder2 = Paths.get(args[1]).toAbsolutePath().normalize();

        if (!Files.isDirectory(folder1) || !Files.isDirectory(folder2)) {
            System.out.println("Both arguments must be directories.");
            return;
        }

        Map<String, FileInfo> filesInA = listFiles(folder1);
        Map<String, FileInfo> filesInB = listFiles(folder2);

        boolean identical = true;

        // Files only in A
        for (String f : difference(filesInA.keySet(), filesInB.keySet())) {
            System.out.println("❌ Missing in folder2: " + f);
            identical = false;
        }

        // Files only in B
        for (String f : difference(filesInB.keySet(), filesInA.keySet())) {
            System.out.println("❌ Missing in folder1: " + f);
            identical = false;
        }

        // Compare common files
        for (String f : intersection(filesInA.keySet(), filesInB.keySet())) {
            FileInfo a = filesInA.get(f);
            FileInfo b = filesInB.get(f);

            if (a.size != b.size) {
                System.out.println("❌ Size mismatch: " + f + " (" + a.size + " vs " + b.size + ")");
                identical = false;
            } else {
                // Optional: deep content check
                if (!a.hash.equals(b.hash)) {
                    System.out.println("❌ Content mismatch: " + f);
                    identical = false;
                }
            }
        }

        if (identical) {
            System.out.println("✅ The two folders are identical (structure + contents).");
        } else {
            System.out.println("❌ Differences found.");
        }
    }

    private static class FileInfo {
        long size;
        String hash;

        FileInfo(long size, String hash) {
            this.size = size;
            this.hash = hash;
        }
    }

    private static Map<String, FileInfo> listFiles(Path root) throws IOException, NoSuchAlgorithmException {
        Map<String, FileInfo> files = new HashMap<>();
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (attrs.isRegularFile()) {
                    String relative = root.relativize(file).toString().replace("\\", "/");
                    try {
                        files.put(relative, new FileInfo(attrs.size(), sha256(file)));
                    } catch (NoSuchAlgorithmException e) {
                        throw new IOException("Hashing error", e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return files;
    }

    private static String sha256(Path file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream in = Files.newInputStream(file);
             DigestInputStream din = new DigestInputStream(in, digest)) {
            byte[] buffer = new byte[8192];
            while (din.read(buffer) != -1) { }
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : digest.digest()) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static <T> Set<T> difference(Set<T> a, Set<T> b) {
        Set<T> diff = new TreeSet<>(a);
        diff.removeAll(b);
        return diff;
    }

    private static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        Set<T> inter = new TreeSet<>(a);
        inter.retainAll(b);
        return inter;
    }
}

