package org.fax4j;

import java.io.File;

/**
 * This interface defines the fax job.
 *
 * @author Sagie Gur-Ari
 * @version 1.0
 * @since 0.1
 */
public interface FaxJob {
    /**
     * This is the fax job priority enum which holds the possible fax sending priority values.
     *
     * @author Sagie Gur-Ari
     * @version 1.0
     * @since 0.1
     */
    enum FaxJobPriority {
        /** The low priority value */
        LOW_PRIORITY,
        /** The medium priority value */
        MEDIUM_PRIORITY,
        /** The high priority value */
        HIGH_PRIORITY
    }

    /**
     * This function returns the fax job ID.
     *
     * @return The fax job ID
     */
    String getID();

    /**
     * This function sets the fax job ID.
     *
     * @param id
     *            The fax job ID
     */
    void setID(String id);

    /**
     * This function returns the file to fax.
     *
     * @return The file to fax
     */
    File getFile();

    /**
     * This function sets the file to fax.
     *
     * @param file
     *            The file to fax
     */
    void setFile(File file);

    /**
     * This function returns the path to the file to fax.
     *
     * @return The path to the file to fax
     */
    String getFilePath();

    /**
     * This function sets the path to the file to fax.
     *
     * @param filePath
     *            The path to the file to fax
     */
    void setFilePath(String filePath);

    /**
     * This function returns the priority.
     *
     * @return The priority
     */
    FaxJobPriority getPriority();

    /**
     * This function sets the priority.
     *
     * @param priority
     *            The priority
     */
    void setPriority(FaxJobPriority priority);

    /**
     * This function returns the fax job target address.
     *
     * @return The fax job target address
     */
    String getTargetAddress();

    /**
     * This function sets the fax job target address.
     *
     * @param targetAddress
     *            The fax job target address
     */
    void setTargetAddress(String targetAddress);

    /**
     * This function returns the fax job target name.
     *
     * @return The fax job target name
     */
    String getTargetName();

    /**
     * This function sets the fax job target name.
     *
     * @param targetName
     *            The fax job target name
     */
    void setTargetName(String targetName);

    /**
     * This function returns the fax job sender name.
     *
     * @return The fax job sender name
     */
    String getSenderName();

    /**
     * This function sets the fax job sender name.
     *
     * @param senderName
     *            The fax job sender name
     */
    void setSenderName(String senderName);

    /**
     * This function returns the fax job sender fax number.
     *
     * @return The fax job sender fax number
     */
    String getSenderFaxNumber();

    /**
     * This function sets the fax job sender fax number.
     *
     * @param senderFaxNumber
     *            The fax job sender fax number
     */
    void setSenderFaxNumber(String senderFaxNumber);

    /**
     * This function returns the fax job sender email address.
     *
     * @return The fax job sender email address
     */
    String getSenderEmail();

    /**
     * This function sets the fax job sender email address.
     *
     * @param senderEmail
     *            The fax job sender email address
     */
    void setSenderEmail(String senderEmail);

    /**
     * This function sets the fax job property.
     *
     * @param key
     *            The property key
     * @param value
     *            The property value
     */
    void setProperty(String key, String value);

    /**
     * This function returns the fax job property for the given key.
     *
     * @param key
     *            The property key
     * @param defaultValue
     *            The default value
     * @return The property value
     */
    String getProperty(String key, String defaultValue);
}
