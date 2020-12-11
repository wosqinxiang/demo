package com.ahdms.framework.core.commom.util;

import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@UtilityClass
public class FileUtils extends org.springframework.util.FileCopyUtils {

    /**
     * 获取文件后缀名
     *
     * @param fullName 文件全名
     * @return {String}
     */
    public static String getFileExtension(String fullName) {
        Objects.requireNonNull(fullName, "file fullName is null.");
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf(CharPool.DOT);
        return (dotIndex == -1) ? StringPool.EMPTY : fileName.substring(dotIndex + 1);
    }

    /**
     * 获取文件名，去除后缀名
     *
     * @param file 文件
     * @return {String}
     */
    public static String getNameWithoutExtension(String file) {
        Objects.requireNonNull(file, "file is null.");
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf(CharPool.DOT);
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    /**
     * Returns the path to the system temporary directory.
     *
     * @return the path to the system temporary directory.
     */
    public static String getTempDirPath() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * Returns a {@link File} representing the system temporary directory.
     *
     * @return the system temporary directory.
     */
    public static File getTempDir() {
        return new File(getTempDirPath());
    }

    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     */
    public static String readToString(final File file) {
        return readToString(file, Charsets.UTF_8);
    }

    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file     the file to read, must not be {@code null}
     * @param encoding the encoding to use, {@code null} means platform default
     * @return the file contents, never {@code null}
     */
    public static String readToString(final File file, final Charset encoding) {
        try (InputStream in = Files.newInputStream(file.toPath())) {
            return IoUtils.readToString(in, encoding);
        } catch (IOException e) {
            throw ExceptionsUtils.unchecked(e);
        }
    }

    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     */
    public static byte[] readToByteArray(final File file) {
        try (InputStream in = Files.newInputStream(file.toPath())) {
            return IoUtils.readToByteArray(in);
        } catch (IOException e) {
            throw ExceptionsUtils.unchecked(e);
        }
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file the file to write
     * @param data the content to write to the file
     */
    public static void writeToFile(final File file, final String data) {
        writeToFile(file, data, Charsets.UTF_8, false);
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file   the file to write
     * @param data   the content to write to the file
     * @param append if {@code true}, then the String will be added to the
     *               end of the file rather than overwriting
     */
    public static void writeToFile(final File file, final String data, final boolean append) {
        writeToFile(file, data, Charsets.UTF_8, append);
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file     the file to write
     * @param data     the content to write to the file
     * @param encoding the encoding to use, {@code null} means platform default
     */
    public static void writeToFile(final File file, final String data, final Charset encoding) {
        writeToFile(file, data, encoding, false);
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file     the file to write
     * @param data     the content to write to the file
     * @param encoding the encoding to use, {@code null} means platform default
     * @param append   if {@code true}, then the String will be added to the
     *                 end of the file rather than overwriting
     */
    public static void writeToFile(final File file, final String data, final Charset encoding, final boolean append) {
        try (OutputStream out = new FileOutputStream(file, append)) {
            IoUtils.write(data, out, encoding);
        } catch (IOException e) {
            throw ExceptionsUtils.unchecked(e);
        }
    }

    /**
     * 转成file
     *
     * @param in   InputStream
     * @param file File
     */
    public static void toFile(InputStream in, final File file) {
        try (OutputStream out = new FileOutputStream(file)) {
            copy(in, out);
        } catch (IOException e) {
            throw ExceptionsUtils.unchecked(e);
        }
    }

    /**
     * Moves a file.
     * <p>
     * When the destination file is on another file system, do a "copy and delete".
     *
     * @param srcFile  the file to be moved
     * @param destFile the destination file
     * @throws NullPointerException if source or destination is {@code null}
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs moving the file
     */
    public static void moveFile(final File srcFile, final File destFile) throws IOException {
        Objects.requireNonNull(srcFile, "Source must not be null");
        Objects.requireNonNull(destFile, "Destination must not be null");
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        }
        if (destFile.exists()) {
            throw new IOException("Destination '" + destFile + "' already exists");
        }
        if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        }
        final boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copy(srcFile, destFile);
            if (!srcFile.delete()) {
                deleteQuietly(destFile);
                throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
            }
        }
    }

    /**
     * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
     * </ul>
     *
     * @param file file or directory to delete, can be {@code null}
     * @return {@code true} if the file or directory was deleted, otherwise
     * {@code false}
     */
    public static boolean deleteQuietly(@Nullable final File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
                FileSystemUtils.deleteRecursively(file);
            }
        } catch (final Exception ignored) {
        }

        try {
            return file.delete();
        } catch (final Exception ignored) {
            return false;
        }
    }

    /**
     * NIO 按行读取文件
     *
     * @param path 文件路径
     * @return 行列表
     */
    public static List<String> readLines(Path path) {
        return readLines(path, Charsets.UTF_8);
    }

    /**
     * NIO 按行读取文件
     *
     * @param file 文件
     * @return 行列表
     */
    public static List<String> readLines(File file) {
        return readLines(file.toPath());
    }

    /**
     * NIO 按行读取文件
     *
     * @param file 文件路径
     * @param cs   字符集
     * @return 行列表
     */
    public static List<String> readLines(File file, Charset cs) {
        return readLines(file.toPath(), cs);
    }

    /**
     * NIO 按行读取文件
     *
     * @param path 文件路径
     * @param cs   字符集
     * @return 行列表
     */
    public static List<String> readLines(Path path, Charset cs) {
		try {
			return Files.readAllLines(path, cs);
		} catch (IOException e) {
			throw ExceptionsUtils.unchecked(e);
		}
	}
}
