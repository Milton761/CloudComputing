package org.mcf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
//import java.io.FileNotFoundException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

public class ListDirectoryContents {
  public static void main(String[] args) throws IOException, URISyntaxException
  {

    URI p = new URI(args[0]);
    URI fileSystem = new URI(p.getScheme(),
        p.getUserInfo(), p.getHost(), p.getPort(),
        null, null,null);
 
    Configuration configuration = new Configuration();

    FileSystem hdfs = FileSystem.get(fileSystem, configuration);

     listFiles(new Path(p),hdfs);

  }

  public static void listFiles(Path dir, FileSystem hdfs) throws IOException, URISyntaxException {
    FileStatus[] fileStatus = hdfs.listStatus(dir);
    for( FileStatus stat : fileStatus)
    {
      System.out.println(stat.getPath());
      if(stat.isDirectory())
      {
        listFiles(stat.getPath(),hdfs);
      }
    }
  }
}
