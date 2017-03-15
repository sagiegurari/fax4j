package org.fax4j.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Properties;
import org.fax4j.FaxException;
import org.fax4j.common.Logger;
import org.fax4j.common.LoggerManager;

/**
 * This is an internal IO utility class.
 * 
 * @author  Sagie Gur-Ari
 * @version 1.02
 * @since   0.40.6
 */
public final class IOHelper
{
    /**The system default encoding*/
    private static final String DEFAULT_ENCODING=IOHelper.getDefaultEncodingImpl();
    /**The temporary directory*/
    private static final File FAX4J_TEMPORARY_DIRECTORY=IOHelper.getFax4jInternalTemporaryDirectoryImpl();

    /**
     * This is the default constructor.
     */
    private IOHelper()
    {
        super();
    }

    /**
     * This function returns the system default encoding.
     * 
     * @return  The system default encoding
     */
    private static String getDefaultEncodingImpl()
    {
        String encoding=System.getProperty("file.encoding");
        
        return encoding;
    }
    
    /**
     * This function returns the fax4j library internal temporary directory.
     * 
     * @return  The fax4j library internal temporary directory
     */
    private static File getFax4jInternalTemporaryDirectoryImpl()
    {
        File temporaryFile=null;
        try
        {
            //create temporary file
            temporaryFile=File.createTempFile("temp_",".temp");
        }
        catch(IOException exception)
        {
            throw new FaxException("Unable to create temporary file.",exception);
        }
        
        //get parent directory
        File temporaryDirectory=temporaryFile.getParentFile();
        
        //delete file
        boolean deleted=temporaryFile.delete();
        if(!deleted)
        {
            temporaryFile.deleteOnExit();
        }

        //read properties
        Properties properties=LibraryConfigurationLoader.readInternalConfiguration();

        //get values
        String name=properties.getProperty("org.fax4j.product.name");
        String version=properties.getProperty("org.fax4j.product.version");

        //create sub directory
        File fax4jTemporaryDirectory=new File(temporaryDirectory,name+"_"+version);
        if(!fax4jTemporaryDirectory.exists())
        {
            boolean created=fax4jTemporaryDirectory.mkdirs();
            if(!created)
            {
                throw new FaxException("Unable to create fax4j internal temporary directory: "+fax4jTemporaryDirectory);
            }
        }

        return fax4jTemporaryDirectory;
    }
    
    /**
     * This function returns the encoding to use.
     * If provided encoding is null, the default system encoding is returend.
     * 
     * @param   encoding
     *          The encoding (may be null for system default encoding)
     * @return  The encoding to use
     */
    private static String getEncodingToUse(String encoding)
    {
        //get encoding
        String updatedEncoding=encoding;
        if(updatedEncoding==null)
        {
            updatedEncoding=IOHelper.getDefaultEncoding();
        }
        
        return updatedEncoding;
    }

    /**
     * This function returns the system default encoding.
     * 
     * @return  The system default encoding
     */
    public static String getDefaultEncoding()
    {
        return IOHelper.DEFAULT_ENCODING;
    }
    
    /**
     * This function returns the fax4j library internal temporary directory.
     * 
     * @return  The fax4j library internal temporary directory
     */
    public static File getFax4jInternalTemporaryDirectory()
    {
        return IOHelper.FAX4J_TEMPORARY_DIRECTORY;
    }
    
    /**
     * This function converts the provided string to binary data.
     * 
     * @param   text
     *          The text to convert
     * @param   encoding
     *          The text encoding
     * @return  The binary data
     */
    public static byte[] convertStringToBinary(String text,String encoding)
    {
        byte[] data=null;
        if(text!=null)
        {
            if(text.length()==0)
            {
                data=new byte[0];
            }
            else
            {
                //get logger
                LoggerManager loggerManager=LoggerManager.getInstance();
                Logger logger=loggerManager.getLogger();

                String encodingToUse=encoding;
                if((encodingToUse!=null)&&(encodingToUse.length()>0))
                {
                    try
                    {
                        data=text.getBytes(encodingToUse);
                    }
                    catch(UnsupportedEncodingException exception)
                    {
                        logger.logError(new Object[]{"Unable to convert text to binary using encoding: ",encodingToUse," using default system encoding."},exception);
                    }
                }
                
                if(data==null)
                {
                    encodingToUse=IOHelper.getDefaultEncoding();

                    try
                    {
                        data=text.getBytes(encodingToUse);
                    }
                    catch(UnsupportedEncodingException exception)
                    {
                        logger.logError(new Object[]{"Unable to convert text to binary using default encoding: ",encodingToUse},exception);
                        
                        throw new FaxException("Unable to convert text to binary using encoding: "+encodingToUse,exception);
                    }
                }
            }
        }
        
        return data;
    }
    
    /**
     * Closes the provided resource.
     *  
     * @param   resource
     *          The resource to close
     */
    public static void closeResource(Closeable resource)
    {
        if(resource!=null)
        {
            try
            {
                //close resource
                resource.close();
            }
            catch(IOException exception)
            {
                //ignore
            }
        }
    }
    
    /**
     * This function creates and returns a new reader for the
     * provided input stream.
     * 
     * @param   inputStream
     *          The input stream
     * @param   encoding
     *          The encoding used by the reader (null for default system encoding)
     * @return  The reader
     */
    public static Reader createReader(InputStream inputStream,String encoding)
    {
        //get encoding
        String updatedEncoding=IOHelper.getEncodingToUse(encoding);
        
        //create reader
        Reader reader=null;
        try
        {
            reader=new InputStreamReader(inputStream,updatedEncoding);
        }
        catch(UnsupportedEncodingException exception)
        {
            throw new FaxException("Unable to create reader, unsupported encoding: "+encoding,exception);
        }
        
        return reader;
    }
    
    /**
     * This function creates and returns a new writer for the
     * provided output stream.
     * 
     * @param   outputStream
     *          The output stream
     * @param   encoding
     *          The encoding used by the writer (null for default system encoding)
     * @return  The writer
     */
    public static Writer createWriter(OutputStream outputStream,String encoding)
    {
        //get encoding
        String updatedEncoding=IOHelper.getEncodingToUse(encoding);
        
        //create writer
        Writer writer=null;
        try
        {
            writer=new OutputStreamWriter(outputStream,updatedEncoding);
        }
        catch(UnsupportedEncodingException exception)
        {
            throw new FaxException("Unable to create writer, unsupported encoding: "+encoding,exception);
        }
        
        return writer;
    }

    /**
     * Reads the text from the stream.
     * 
     * @param   reader
     *          The reader to the text
     * @return  The text read from the provided stream
     * @throws  IOException
     *          Any IO exception
     */
    public static String readTextStream(Reader reader) throws IOException
    {
        char[] buffer=new char[5000];
        Writer stringWriter=new StringWriter();
        int read=-1;
        try
        {
            do
            {
                //read next buffer
                read=reader.read(buffer);
                
                if(read!=-1)
                {
                    //write to string writer
                    stringWriter.write(buffer,0,read);
                }
            }while(read!=-1);
        }
        finally
        {
            //close streams
            IOHelper.closeResource(reader);
        }
        
        //get text
        String text=stringWriter.toString();
        
        return text;
    }
    
    /**
     * Reads the text from the file.
     * 
     * @param   file
     *          The text file
     * @return  The text read from the provided file
     * @throws  IOException
     *          Any IO exception
     */
    public static String readTextFile(File file) throws IOException
    {
        //create reader to file (with default encoding)
        InputStream inputStream=new FileInputStream(file);
        Reader reader=IOHelper.createReader(inputStream,null);

        //read text
        String text=IOHelper.readTextStream(reader);
        
        return text;
    }
    
    /**
     * Writes the text to the file.
     * 
     * @param   text
     *          The text to write to the provided file
     * @param   file
     *          The text file
     * @throws  IOException
     *          Any IO exception
     */
    public static void writeTextFile(String text,File file) throws IOException
    {
        Writer writer=null;
        try
        {
            //create writer to file (with default encoding)
            OutputStream outputStream=new FileOutputStream(file);
            writer=IOHelper.createWriter(outputStream,null);
            
            //write to file
            writer.write(text);
        }
        finally
        {
            //close writer
            IOHelper.closeResource(writer);
        }
    }

    /**
     * Reads the data from the input stream and writes to the
     * output stream.
     * 
     * @param   inputStream
     *          The inputStream to read from
     * @param   outputStream
     *          The output stream to write to
     * @throws  IOException
     *          Any IO exception
     */
    public static void readAndWriteStreams(InputStream inputStream,OutputStream outputStream) throws IOException
    {
        byte[] buffer=new byte[5000];
        int read=-1;
        try
        {
            do
            {
                //read next buffer
                read=inputStream.read(buffer);
                
                if(read!=-1)
                {
                    //write to in memory stream
                    outputStream.write(buffer,0,read);
                }
            }while(read!=-1);
        }
        finally
        {
            //close streams
            IOHelper.closeResource(inputStream);
            IOHelper.closeResource(outputStream);
        }
    }

    /**
     * Reads the data from the stream.
     * 
     * @param   inputStream
     *          The inputStream
     * @return  The data read from the provided stream
     * @throws  IOException
     *          Any IO exception
     */
    public static byte[] readStream(InputStream inputStream) throws IOException
    {
        //create output stream
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream(5000);
        
        //read/write
        IOHelper.readAndWriteStreams(inputStream,outputStream);
        
        //get data
        byte[] data=outputStream.toByteArray();

        return data;
    }

    /**
     * Reads the data from the file.
     * 
     * @param   file
     *          The file
     * @return  The data read from the provided file
     * @throws  IOException
     *          Any IO exception
     */
    public static byte[] readFile(File file) throws IOException
    {
        //create stream to file
        InputStream inputStream=new FileInputStream(file);

        //read data
        byte[] data=IOHelper.readStream(inputStream);

        return data;
    }
    
    /**
     * Writes the content to the file.
     * 
     * @param   content
     *          The content to write to the provided file
     * @param   file
     *          The target file
     * @throws  IOException
     *          Any IO exception
     */
    public static void writeFile(byte[] content,File file) throws IOException
    {
        OutputStream outputStream=null;
        try
        {
            //create output stream to file
            outputStream=new FileOutputStream(file);
            
            //write to file
            outputStream.write(content);
        }
        finally
        {
            //close writer
            IOHelper.closeResource(outputStream);
        }
    }

    /**
     * This function returns the file object of the first location in
     * which the requested file is found.
     * 
     * @param   fileNameWithNoPath
     *          The file name (with no directory path)
     * @param   pathList
     *          The path list
     * @return  The file object if found, else null will be returned
     */
    public static File getFileFromPathList(String fileNameWithNoPath,String[] pathList)
    {
        File foundFile=null;
        if(pathList!=null)
        {
            //get amount
            int amount=pathList.length;
            
            String directoryPath=null;
            File file=null;
            for(int index=0;index<amount;index++)
            {
                //get next element
                directoryPath=pathList[index];
    
                //get file
                file=new File(directoryPath,fileNameWithNoPath);
                
                if((file.exists())&&(file.isFile()))
                {
                    foundFile=file;
                    break;
                }
            }
        }
        
        return foundFile;
    }

    /**
     * This function returns the file object of the first location in
     * which the requested file is found.
     * The path list used, is the current native path of the process.
     * 
     * @param   fileNameWithNoPath
     *          The file name (with no directory path)
     * @return  The file object if found, else null will be returned
     */
    public static File getFileFromNativePath(String fileNameWithNoPath)
    {
        //get system path
        String systemPath=System.getProperty("java.library.path");
        String pathSeperator=System.getProperty("path.separator");
        String[] systemPathElements=systemPath.split(pathSeperator);
        
        //get file
        File file=IOHelper.getFileFromPathList(fileNameWithNoPath,systemPathElements);
        
        return file;
    }

    /**
     * This class reads the output of the process.
     * 
     * @author  Sagie Gur-Ari
     * @version 1.01
     * @since   0.21e
     */
    public static class OutputReadThread extends Thread
    {
        /**The input stream to read from*/
        private final InputStream INPUT_STREAM;
        /**The text read from the stream*/
        private final ByteArrayOutputStream OUTPUT_STREAM;

        /**
         * This is the default constructor.
         * 
         * @param   inputStream
         *          The input stream to read from
         */
        public OutputReadThread(InputStream inputStream)
        {
            super();
            
            this.OUTPUT_STREAM=new ByteArrayOutputStream(5000);
            this.INPUT_STREAM=inputStream;
        }
        
        /**
         * Reads the data from the stream.
         */
        @Override
        public void run()
        {
            byte[] buffer=new byte[100];
            int read=-1;
            try
            {
                do
                {
                    //read next buffer
                    read=this.INPUT_STREAM.read(buffer);
                    
                    if(read!=-1)
                    {
                        //write to output stream
                        this.OUTPUT_STREAM.write(buffer,0,read);
                    }
                }while(read!=-1);
            }
            catch(IOException exception)
            {
                //ignore
            }
        }
        
        /**
         * The text read from the stream.
         *  
         * @return  The text
         * @throws  UnsupportedEncodingException 
         *          In case encoding is not supported
         */
        public String getText() throws UnsupportedEncodingException
        {
            String encoding=IOHelper.getDefaultEncoding();
            String text=this.OUTPUT_STREAM.toString(encoding);
            
            return text;
        }
    }
}